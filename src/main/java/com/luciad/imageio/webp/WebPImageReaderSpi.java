package com.luciad.imageio.webp;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;

public class WebPImageReaderSpi extends ImageReaderSpi {
    private static final byte[] RIFF = {'R', 'I', 'F', 'F'};
    private static final byte[] WEBP = {'W', 'E', 'B', 'P'};
    private static final byte[] VP8_ = {'V', 'P', '8', ' '};
    private static final byte[] VP8L = {'V', 'P', '8', 'L'};
    private static final byte[] VP8X = {'V', 'P', '8', 'X'};

    private static final String vendorName = "Luciad";
    private static final String version = "1.0";
    private static final String[] names = {"webp", "WEBP", "WebP", "Webp"};
    private static final String[] suffixes = {"webp"};
    private static final String[] MIMETypes = {"image/webp"};

    public WebPImageReaderSpi() {
        super(
                vendorName,
                version,
                names,
                suffixes,
                MIMETypes,
                WebPReader.class.getName(),
                new Class[]{ImageInputStream.class},
                new String[]{WebPImageWriterSpi.class.getName()},
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
    public WebPReader createReaderInstance(Object extension) {
        return new WebPReader(this);
    }

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        if (!(source instanceof ImageInputStream stream)) {
            return false;
        }
        byte[] b = new byte[4];
        ByteOrder oldByteOrder = stream.getByteOrder();
        stream.mark();
        stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);

        try {
            stream.readFully(b);
            if (!Arrays.equals(b, RIFF)) {
                return false;
            }
            long chunkLength = stream.readUnsignedInt();
            long streamLength = stream.length();
            if (streamLength != -1 && streamLength != chunkLength + 8) {
                return false;
            }
            stream.readFully(b);
            if (!Arrays.equals(b, WEBP)) {
                return false;
            }

            stream.readFully(b);
            if (!Arrays.equals(b, VP8_) && !Arrays.equals(b, VP8L) && !Arrays.equals(b, VP8X)) {
                return false;
            }
        } finally {
            stream.setByteOrder(oldByteOrder);
            stream.reset();
        }

        return true;
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP Reader";
    }
}
