package com.luciad.imageio.webp;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
class NativeLibraryUtils {
//    public static void loadFromJar() {
//        val archBits = PlatformUtil.getArchBits(false);
//        val libFilename = getLibraryFilename(platform);
//        val platform = PlatformUtil.getPlatform();
//
//        try (val inputStream = NativeLibraryUtils.class.getResourceAsStream(String.format("/native/%s/%s/%s", platform.getTelemetryName(), archBits, libFilename))) {
//            if (inputStream == null) {
//                throw new RuntimeException(String.format("Could not find WebP native library for %s %s in the jar", platform.getTelemetryName(), archBits));
//            }
//
//            val tmpLibraryFile = Files.createTempFile("webp-lib", libFilename).toFile();
//            tmpLibraryFile.deleteOnExit();
//
//            // Efficient copying of the input stream to the temp file
//            try (FileOutputStream out = new FileOutputStream(tmpLibraryFile)) {
//                Files.copy(inputStream, tmpLibraryFile.toPath());
//            }
//
//            System.load(tmpLibraryFile.getAbsolutePath());
//
//        } catch (IOException e) {
//            throw new RuntimeException("Could not load native WebP library", e);
//        }
//    }

    public static void loadLibrary() {
        val archBits = PlatformUtil.getArchBits();

        try {
            System.loadLibrary(getLibraryName());
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException(String.format("Failed to load the WebP library for architecture: %s", archBits), e);
        }
    }

//    @Contract(pure = true)
//    private static @NotNull String getLibraryFilename() {
//        val libraryName = NativeLibraryUtils.getLibraryName();
//        val platform = PlatformUtil.getPlatform();
//
//        return switch (platform) {
//            case WINDOWS -> "%s.dll".formatted(libraryName);
//            case LINUX -> "lib%s.so".formatted(libraryName);
//            case OSX -> "lib%s.dylib".formatted(libraryName);
//            default -> throw new IllegalArgumentException("Unknown platform: " + platform);
//        };
//    }

    private static @NotNull String getLibraryName() {
        return "webp-imageio";
    }

}