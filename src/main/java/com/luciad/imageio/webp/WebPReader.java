
package com.luciad.imageio.webp;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

public class WebPReader extends ImageReader {
    private byte[] data;
    private int width;
    private int height;

    WebPReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        data = null;
        width = -1;
        height = -1;
    }

    @Override
    public int getNumImages(boolean allowSearch) {
        return 1;
    }

    private void readHeader() throws IOException {
        if (width != -1 && height != -1) {
            return;
        }

        readData();
        int[] info = WebP.getInfo(data, 0, data.length);
        width = info[0];
        height = info[1];
    }

    private void readData() throws IOException {
        if (data != null) {
            return;
        }

        ImageInputStream input = (ImageInputStream) getInput();
        long length = input.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Cannot read image of size " + length);
        }

        if (input.getStreamPosition() != 0L) {
            if (isSeekForwardOnly()) {
                throw new IOException();
            } else {
                input.seek(0);
            }
        }

        byte[] data;
        if (length > 0) {
            data = new byte[(int) length];
            input.readFully(data);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            data = out.toByteArray();
        }
        this.data = data;
    }

    private void checkIndex(int imageIndex) {
        if (imageIndex != 0) {
            throw new IndexOutOfBoundsException("Invalid image index: " + imageIndex);
        }
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        readHeader();
        return width;
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        readHeader();
        return height;
    }

    @Override
    public IIOMetadata getStreamMetadata() {
        return null;
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) {
        return null;
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) {
        return Collections.singletonList(
                ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB)
        ).iterator();
    }

    @Override
    public WebPReadParam getDefaultReadParam() {
        return new WebPReadParam();
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        checkIndex(imageIndex);
        readData();
        readHeader();
        WebPReadParam readParam;
        if (param instanceof WebPReadParam webPReadParam) {
            readParam = webPReadParam;
        } else {
            readParam = this.getDefaultReadParam();
        }

        int[] outParams = new int[4];
        int[] pixels = WebP.decode(readParam.options(), data, 0, data.length, outParams);

        int width = outParams[1];
        int height = outParams[2];
        boolean alpha = outParams[3] != 0;

        ColorModel colorModel;
        if (alpha) {
            colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);
        } else {
            colorModel = new DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff, 0x00000000);
        }

        SampleModel sampleModel = colorModel.createCompatibleSampleModel(width, height);
        DataBufferInt db = new DataBufferInt(pixels, width * height);
        WritableRaster raster = WritableRaster.createWritableRaster(sampleModel, db, null);

        return new BufferedImage(colorModel, raster, false, new Hashtable<>());
    }
}
