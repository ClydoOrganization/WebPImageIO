package com.luciad.imageio.webp;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

@UtilityClass
public class WebPUtil {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public void fileToWebP(Path input, Path output, CompressionType compressionType) throws IOException {
        fileToWebP(input, output, options -> options.setCompressionType(compressionType));
    }

    public void fileToWebP(Path input, Path output, Consumer<WebPEncoderOptions> optionsConsumer) throws IOException {
        if (!Files.exists(input)) {
            throw new IllegalArgumentException("Input path does not exist: " + input);
        }

        var originalImage = ImageIO.read(input.toFile());
        if (originalImage == null) {
            throw new IOException("Could not read image from file: " + input);
        }

        var writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        var writeParam = new WebPWriteParam(writer.getLocale());
        optionsConsumer.accept(writeParam.options());

        Files.createDirectories(output.getParent()); // Ensure output directory exists

        try (var imageOutputStream = new FileImageOutputStream(output.toFile())) {
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(originalImage, null, null), writeParam);
        } finally {
            writer.dispose();
        }
    }

    public void directoryToWebP(Path inputDir, Path outputDir, String filesSuffix, CompressionType compressionType, Consumer<Path> endEvery) throws IOException {
        directoryToWebP(inputDir, outputDir, filesSuffix, options -> options.setCompressionType(compressionType), endEvery);
    }

    public void directoryToWebP(Path inputDir, Path outputDir, String filesSuffix, Consumer<WebPEncoderOptions> optionsConsumer, Consumer<Path> endEvery) throws IOException {
        if (!Files.exists(inputDir)) {
            throw new IllegalArgumentException("Input directory path does not exist: " + inputDir);
        }

        Optional.ofNullable(filesSuffix)
                .filter(suffix -> suffix.startsWith("."))
                .orElseThrow(() -> new IllegalArgumentException("Files suffix must start with '.'"));

        try (var directoryStream = Files.newDirectoryStream(inputDir, entry -> entry.getFileName().toString().toLowerCase().endsWith(filesSuffix))) {
            for (Path input : directoryStream) {
                String fileName = input.getFileName().toString();
                Path output = outputDir.resolve(fileName.substring(0, fileName.lastIndexOf('.')) + ".webp");

                fileToWebP(input, output, optionsConsumer);
                if (endEvery != null) {
                    endEvery.accept(input);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        printBanner();
        try (Scanner scanner = new Scanner(System.in)) {

            // Get input directory
            val inputDir = getInputPath(scanner, "Enter the input directory:");

            // Get output directory
            val outputDir = getInputPath(scanner, "Enter the output directory:");

            // Get file suffix
            String filesSuffix;
            do {
                System.out.print(CYAN + "Enter the files suffix (e.g., .jpg, .png): " + RESET);
                filesSuffix = scanner.nextLine();
                if (!filesSuffix.startsWith(".")) {
                    System.out.println(RED + "Invalid input! File suffix must start with '.'" + RESET);
                }
            } while (!filesSuffix.startsWith("."));

            // Get compression type
            CompressionType compressionType;
            do {
                System.out.print(CYAN + "Enter Compression Type [0 = Lossy (Lower quality, smaller size), 1 = Lossless (Higher quality, larger size)]: " + RESET);
                while (!scanner.hasNextInt()) {
                    System.out.println(RED + "Invalid input! Please enter 0 for Lossy or 1 for Lossless." + RESET);
                    scanner.next(); // discard invalid input
                }
                compressionType = CompressionType.ofId(scanner.nextInt());
                if (compressionType == null) {
                    System.out.println(RED + "Invalid compression type! Please enter 0 for Lossy or 1 for Lossless." + RESET);
                }
            } while (compressionType == null);

            System.out.println(YELLOW + "\nStarting conversion..." + RESET);

            directoryToWebP(inputDir, outputDir, filesSuffix, compressionType, input ->
                    System.out.println(GREEN + "Successfully converted file: " + input + RESET)
            );

            System.out.println(GREEN + "\nAll files processed successfully." + RESET);
        }
    }

    private static Path getInputPath(@NotNull Scanner scanner, String prompt) {
        Path path;
        do {
            System.out.print(CYAN + prompt + " " + RESET);
            path = Path.of(scanner.nextLine());
            if (!Files.exists(path)) {
                System.out.println(RED + "Invalid path! Directory does not exist." + RESET);
            }
        } while (!Files.exists(path));
        return path;
    }

    private static void printBanner() {
        System.out.println(BLUE + "======================================" + RESET);
        System.out.println(BOLD + BLUE + "      WebP Image Conversion Tool      " + RESET);
        System.out.println(BLUE + "======================================" + RESET);
        System.out.println(YELLOW + "This tool converts images in a directory" + RESET);
        System.out.println(YELLOW + "to WebP format. Please follow the prompts." + RESET);
        System.out.println(BLUE + "--------------------------------------" + RESET);
    }
}