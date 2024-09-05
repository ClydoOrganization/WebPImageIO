
package com.luciad.imageio.webp;

import javax.imageio.ImageWriteParam;
import java.util.Locale;

public class WebPWriteParam extends ImageWriteParam {
    public static final int LOSSY_COMPRESSION = 0;
    public static final int LOSSLESS_COMPRESSION = 1;
    private final boolean fDefaultLossless;
    private final WebPEncoderOptions options;

    public WebPWriteParam(Locale aLocale) {
        super(aLocale);
        options = new WebPEncoderOptions();
        fDefaultLossless = options.isLossless();
        canWriteCompressed = true;
        compressionTypes = new String[]{
                "Lossy",
                "Lossless"
        };
        compressionType = compressionTypes[fDefaultLossless ? LOSSLESS_COMPRESSION : LOSSY_COMPRESSION];
        compressionQuality = options.getCompressionQuality() / 100f;
        compressionMode = MODE_EXPLICIT;
    }

    @Override
    public float getCompressionQuality() {
        return super.getCompressionQuality();
    }

    @Override
    public void setCompressionQuality(float quality) {
        super.setCompressionQuality(quality);
        options.setCompressionQuality(quality * 100f);
    }

    @Override
    public void setCompressionType(String compressionType) {
        super.setCompressionType(compressionType);
        for (int i = 0; i < compressionTypes.length; i++) {
            if (compressionTypes[i].equals(compressionType)) {
                options.setLossless(i == LOSSLESS_COMPRESSION);
                break;
            }
        }

    }

    @Override
    public void unsetCompression() {
        super.unsetCompression();
        options.setLossless(fDefaultLossless);
    }

    public void setSnsStrength(int aSnsStrength) {
        options.setSnsStrength(aSnsStrength);
    }

    public void setAlphaQuality(int aAlphaQuality) {
        options.setAlphaQuality(aAlphaQuality);
    }

    public int getSegments() {
        return options.getSegments();
    }

    public int getPreprocessing() {
        return options.getPreprocessing();
    }

    public int getFilterStrength() {
        return options.getFilterStrength();
    }

    public void setEmulateJpegSize(boolean aEmulateJpegSize) {
        options.setEmulateJpegSize(aEmulateJpegSize);
    }

    public int getPartitions() {
        return options.getPartitions();
    }

    public void setTargetPSNR(float aTargetPSNR) {
        options.setTargetPSNR(aTargetPSNR);
    }

    public int getEntropyAnalysisPassCount() {
        return options.getEntropyAnalysisPassCount();
    }

    public int getPartitionLimit() {
        return options.getPartitionLimit();
    }

    public int getFilterType() {
        return options.getFilterType();
    }

    public int getFilterSharpness() {
        return options.getFilterSharpness();
    }

    public int getAlphaQuality() {
        return options.getAlphaQuality();
    }

    public boolean isShowCompressed() {
        return options.isShowCompressed();
    }

    public boolean isReduceMemoryUsage() {
        return options.isReduceMemoryUsage();
    }

    public void setThreadLevel(int aThreadLevel) {
        options.setThreadLevel(aThreadLevel);
    }

    public boolean isAutoAdjustFilterStrength() {
        return options.isAutoAdjustFilterStrength();
    }

    public void setReduceMemoryUsage(boolean aLowMemory) {
        options.setReduceMemoryUsage(aLowMemory);
    }

    public void setFilterStrength(int aFilterStrength) {
        options.setFilterStrength(aFilterStrength);
    }

    public int getTargetSize() {
        return options.getTargetSize();
    }

    public void setEntropyAnalysisPassCount(int aPass) {
        options.setEntropyAnalysisPassCount(aPass);
    }

    public void setFilterSharpness(int aFilterSharpness) {
        options.setFilterSharpness(aFilterSharpness);
    }

    public int getAlphaFiltering() {
        return options.getAlphaFiltering();
    }

    public int getSnsStrength() {
        return options.getSnsStrength();
    }

    public void setPartitionLimit(int aPartitionLimit) {
        options.setPartitionLimit(aPartitionLimit);
    }

    public void setMethod(int aMethod) {
        options.setMethod(aMethod);
    }

    public void setAlphaFiltering(int aAlphaFiltering) {
        options.setAlphaFiltering(aAlphaFiltering);
    }

    public int getMethod() {
        return options.getMethod();
    }

    public void setFilterType(int aFilterType) {
        options.setFilterType(aFilterType);
    }

    public void setPartitions(int aPartitions) {
        options.setPartitions(aPartitions);
    }

    public void setAutoAdjustFilterStrength(boolean aAutofilter) {
        options.setAutoAdjustFilterStrength(aAutofilter);
    }

    public boolean isEmulateJpegSize() {
        return options.isEmulateJpegSize();
    }

    public int getAlphaCompression() {
        return options.getAlphaCompression();
    }

    public void setShowCompressed(boolean aShowCompressed) {
        options.setShowCompressed(aShowCompressed);
    }

    public void setSegments(int aSegments) {
        options.setSegments(aSegments);
    }

    public float getTargetPSNR() {
        return options.getTargetPSNR();
    }

    public int getThreadLevel() {
        return options.getThreadLevel();
    }

    public void setTargetSize(int aTargetSize) {
        options.setTargetSize(aTargetSize);
    }

    public void setAlphaCompression(int aAlphaCompression) {
        options.setAlphaCompression(aAlphaCompression);
    }

    public void setPreprocessing(int aPreprocessing) {
        options.setPreprocessing(aPreprocessing);
    }

    WebPEncoderOptions getEncoderOptions() {
        return options;
    }
}
