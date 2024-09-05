package com.luciad.imageio.webp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

enum VP8StatusCode {
    VP8_STATUS_OK,
    VP8_STATUS_OUT_OF_MEMORY,
    VP8_STATUS_INVALID_PARAM,
    VP8_STATUS_BITSTREAM_ERROR,
    VP8_STATUS_UNSUPPORTED_FEATURE,
    VP8_STATUS_SUSPENDED,
    VP8_STATUS_USER_ABORT,
    VP8_STATUS_NOT_ENOUGH_DATA;

    private static final VP8StatusCode[] VALUES;

    @Contract(pure = true)
    public static @Nullable VP8StatusCode getStatusCode(int index) {
        if (index >= 0 && index < VALUES.length)
            return VALUES[index];
        return null;
    }

    static {
        VALUES = values();
    }
}
