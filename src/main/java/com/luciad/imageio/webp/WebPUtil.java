package com.luciad.imageio.webp;

import lombok.experimental.UtilityClass;
import lombok.val;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@UtilityClass
public class WebPUtil {
    public void fileToWebP(Path input, Path output, CompressionType compressionType) throws IOException {
        WebPUtil.fileToWebP(input, output, options -> options.setCompressionType(compressionType));
    }

    public void fileToWebP(Path input, Path output, Consumer<WebPEncoderOptions> optionsConsumer) throws IOException {
        if (!Files.exists(input)) {
            throw new IllegalArgumentException("Input path does not exist: " + input);
        }


        try (val inputStream = Files.newInputStream(input)) {
            val originalImage = ImageIO.read(inputStream);
            val writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            val writeParam = new WebPWriteParam(writer.getLocale());
            optionsConsumer.accept(writeParam.options());

            Files.createFile(output);
            writer.setOutput(new FileImageOutputStream(output.toFile()));
            writer.write(null, new IIOImage(originalImage, null, null), writeParam);
        }
    }

    public void directoryToWebP(Path inputDir, Path outputDir, Consumer<WebPEncoderOptions> optionsConsumer) throws IOException {
        if (!Files.exists(inputDir)) {
            throw new IllegalArgumentException("Input directory path does not exist: " + inputDir);
        }

        try (val directoryStream = Files.newDirectoryStream(inputDir)) {
            for (Path input : directoryStream) {
                WebPUtil.fileToWebP(input, outputDir, optionsConsumer);
            }
        }
    }
}
