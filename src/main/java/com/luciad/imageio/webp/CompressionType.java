package com.luciad.imageio.webp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public enum CompressionType {
    LOSSY(0, "Lossy"),
    LOSSLESS(1, "Lossless");

    private static final String[] names = new String[]{
            CompressionType.LOSSY.getName(),
            CompressionType.LOSSLESS.getName()
    };
    private final int id;
    private final String name;

    public boolean isLossy() {
        return this == LOSSY;
    }

    public boolean isLossless() {
        return this == LOSSLESS;
    }

    public static CompressionType of(final boolean lossless) {
        return lossless ? LOSSY : LOSSLESS;
    }

    @Contract(pure = true)
    public static @Nullable CompressionType ofId(final int id) {
        if (id == 0) {
            return LOSSY;
        } else if (id == 1) {
            return LOSSLESS;
        }
        return null;
    }

    @Contract(value = " -> new", pure = true)
    public static String @NotNull [] compressionTypes() {
        return names;
    }
}
