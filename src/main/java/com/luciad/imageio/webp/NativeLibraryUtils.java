package com.luciad.imageio.webp;

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

class NativeLibraryUtils {
    public static void loadFromJar() {
        val platform = getPlatform();
        val archBits = getArchBits(false);
        val libFilename = getLibraryFilename(platform);

        try (val inputStream = NativeLibraryUtils.class.getResourceAsStream(String.format("/native/%s/%s/%s", platform, archBits, libFilename))) {
            if (inputStream == null) {
                throw new RuntimeException(String.format("Could not find WebP native library for %s %s in the jar", platform, archBits));
            }

            val tmpLibraryFile = Files.createTempFile("webp-lib", libFilename).toFile();
            tmpLibraryFile.deleteOnExit();

            // Efficient copying of the input stream to the temp file
            try (FileOutputStream out = new FileOutputStream(tmpLibraryFile)) {
                Files.copy(inputStream, tmpLibraryFile.toPath());
            }

            System.load(tmpLibraryFile.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Could not load native WebP library", e);
        }
    }

    public static void loadLibrary() {
        val archBits = getArchBits(true);

        try {
            System.loadLibrary("webp-imageio" + archBits);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException(String.format("Failed to load the WebP library for architecture: %s", archBits), e);
        }
    }

    @Contract(pure = true)
    private static @NotNull String getLibraryFilename(@NotNull String platform) {
        return switch (platform) {
            case "win" -> "webp-imageio.dll";
            case "mac" -> "libwebp-imageio.dylib";
            case "linux" -> "libwebp-imageio.so";
            default -> throw new IllegalArgumentException("Unknown platform: " + platform);
        };
    }

    private static @NotNull String getArchBits(boolean env) {
        String archBits;
        if (env) {
            archBits = System.getenv("PROCESSOR_ARCHITECTURE");
            if (archBits == null) {
                archBits = getArchBits(false);
            }
        } else {
            archBits = System.getProperty("os.arch");
        }
        return archBits.contains("64") ? "64" : "32";
    }

    private static @NotNull String getPlatform() {
        val os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "win";
        } else if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("nux") || os.contains("nix")) {
            return "linux";
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }
    }
}