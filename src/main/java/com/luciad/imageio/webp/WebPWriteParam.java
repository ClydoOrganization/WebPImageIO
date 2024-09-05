
package com.luciad.imageio.webp;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageWriteParam;
import java.util.Locale;

public class WebPWriteParam extends ImageWriteParam {
    private final CompressionType defaultType;
    private final WebPEncoderOptions options;

    public WebPWriteParam(Locale locale) {
        super(locale);
        this.options = new WebPEncoderOptions(this);
        this.defaultType = CompressionType.of(this.options.isLossless());
        this.canWriteCompressed = true;
        this.compressionTypes = CompressionType.compressionTypes();
        this.compressionType = this.defaultType.getName();
        this.compressionQuality = this.options.getCompressionQuality() / 100f;
        this.compressionMode = MODE_EXPLICIT;
    }

    @Override
    public float getCompressionQuality() {
        return super.getCompressionQuality();
    }

    @Override
    public void setCompressionQuality(float quality) {
        super.setCompressionQuality(quality);
        this.options.setCompressionQuality(quality * 100f);
    }

    void setCompressionType(@NotNull CompressionType type) {
        super.setCompressionType(type.getName());
    }

    @Deprecated(forRemoval = true)
    @Override
    public void setCompressionType(String compressionType) {
        throw new UnsupportedOperationException("Not supported yet. use setCompressionType(CompressionType) instead.");
    }

    @Override
    public void unsetCompression() {
        super.unsetCompression();
        this.options.unsetCompression(this.defaultType);
    }

    public WebPEncoderOptions options() {
        return this.options;
    }
}
