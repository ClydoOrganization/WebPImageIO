
package com.luciad.imageio.webp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Objects;

public final class WebP {
    private static boolean NATIVE_LIBRARY_LOADED = false;

    static synchronized void loadNativeLibrary() {
        if (!NATIVE_LIBRARY_LOADED) {
            NativeLibraryUtils.loadLibrary();
            NATIVE_LIBRARY_LOADED = true;
        }
    }

    static {
        loadNativeLibrary();
    }

    public static final WebPImageReaderSpi IMAGE_READER = new WebPImageReaderSpi();

    public static final WebPImageWriterSpi IMAGE_WRITER = new WebPImageWriterSpi();

    private WebP() {
    }

    public static int[] decode(WebPDecoderOptions options, byte[] data, int offset, int length, int[] out) throws IOException {
        if (options == null) throw new NullPointerException("Decoder options may not be null");
        if (data == null) throw new NullPointerException("Input data may not be null");
        if (offset + length > data.length) throw new IllegalArgumentException("Offset/length exceeds array size");

        int[] pixels = decode(options.fPointer, data, offset, length, out, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
        VP8StatusCode status = VP8StatusCode.getStatusCode(out[0]);
        return switch (Objects.requireNonNull(status)) {
            case VP8_STATUS_OK -> pixels;
            case VP8_STATUS_OUT_OF_MEMORY -> throw new OutOfMemoryError();
            default -> throw new IOException("Decode returned code " + status);
        };
    }

    public static int @NotNull [] getInfo(byte[] data, int offset, int length) throws IOException {
        int[] out = new int[2];
        int result = getInfo(data, offset, length, out);
        if (result == 0)
            throw new IOException("Invalid WebP data");

        return out;
    }

    public static byte[] encodeRGBA(@NotNull WebPEncoderOptions options, byte[] rgbaData, int width, int height, int stride) {
        return encodeRGBA(options.fPointer, rgbaData, width, height, stride);
    }

    public static byte[] encodeRGB(@NotNull WebPEncoderOptions options, byte[] rgbaData, int width, int height, int stride) {
        return encodeRGB(options.fPointer, rgbaData, width, height, stride);
    }

    private static native int[] decode(long aDecoderOptionsPointer, byte[] data, int offset, int length, int[] flags, boolean bigEndian);

    private static native int getInfo(byte[] data, int offset, int length, int[] out);

    private static native byte[] encodeRGBA(long config, byte[] rgbaData, int width, int height, int stride);

    private static native byte[] encodeRGB(long options, byte[] rgbaData, int width, int height, int stride);
}
