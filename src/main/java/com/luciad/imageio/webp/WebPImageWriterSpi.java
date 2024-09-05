
package com.luciad.imageio.webp;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.util.Locale;

public class WebPImageWriterSpi extends ImageWriterSpi {
    private static final String vendorName = "Luciad";
    private static final String version = "1.0";
    private static final String[] names = {"webp", "WEBP", "WebP", "Webp"};
    private static final String[] suffixes = {"webp"};
    private static final String[] MIMETypes = {"image/webp"};

    public WebPImageWriterSpi() {
        super(
                vendorName,
                version,
                names,
                suffixes,
                MIMETypes,
                WebPReader.class.getName(),
                new Class[]{ImageOutputStream.class},
                new String[]{WebPImageReaderSpi.class.getName()},
                false,
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public boolean canEncodeImage(@NotNull ImageTypeSpecifier type) {
        ColorModel colorModel = type.getColorModel();
        SampleModel sampleModel = type.getSampleModel();
        int transferType = sampleModel.getTransferType();

        if (colorModel instanceof ComponentColorModel) {
            if (!(sampleModel instanceof ComponentSampleModel)) {
                return false;
            }

            if (transferType != DataBuffer.TYPE_BYTE && transferType != DataBuffer.TYPE_INT) {
                return false;
            }
        } else if (colorModel instanceof DirectColorModel) {
            if (!(sampleModel instanceof SinglePixelPackedSampleModel)) {
                return false;
            }

            if (transferType != DataBuffer.TYPE_INT) {
                return false;
            }
        }

        ColorSpace colorSpace = colorModel.getColorSpace();
        if (!(colorSpace.isCS_sRGB())) {
            return false;
        }

        int[] sampleSize = sampleModel.getSampleSize();
        for (int j : sampleSize) {
            if (j > 8) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ImageWriter createWriterInstance(Object extension) {
        return new WebPWriter(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP Writer";
    }
}
