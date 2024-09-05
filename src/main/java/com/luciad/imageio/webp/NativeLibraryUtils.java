package com.luciad.imageio.webp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

class NativeLibraryUtils {
    public static void loadFromJar() {
        String os = System.getProperty("os.name").toLowerCase();
        String bits = System.getProperty("os.arch").contains("64") ? "64" : "32";

        String libFilename = "libwebp-imageio.so";
        String platform = "linux";

        if (os.contains("win")) {
            platform = "win";
            libFilename = "webp-imageio.dll";
        } else if (os.contains("mac")) {
            platform = "mac";
            libFilename = "libwebp-imageio.dylib";
        }

        try (InputStream in = NativeLibraryUtils.class.getResourceAsStream(String.format("/native/%s/%s/%s", platform, bits, libFilename))) {
            if (in == null) {
                throw new RuntimeException(String.format("Could not find WebP native library for %s %s in the jar", platform, bits));
            }

            File tmpLibraryFile = Files.createTempFile("", libFilename).toFile();
            tmpLibraryFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tmpLibraryFile)) {
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            System.load(tmpLibraryFile.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Could not load native WebP library", e);
        }
    }

    public static void loadLibrary() {
        String str1 = System.getenv("PROCESSOR_ARCHITECTURE");
        if (str1 == null)
            str1 = System.getProperty("os.arch");
        String str2 = str1.contains("64") ? "64" : "32";
        try {
            System.loadLibrary("webp-imageio" + str2);
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            unsatisfiedLinkError.printStackTrace();
        }
    }
}