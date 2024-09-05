
package com.luciad.imageio.webp;

import javax.imageio.ImageReadParam;

public final class WebPReadParam extends ImageReadParam {
    private final WebPDecoderOptions options;

    public WebPReadParam() {
        options = new WebPDecoderOptions();
    }

    public void setScaledHeight(int aScaledHeight) {
        options.setScaledHeight(aScaledHeight);
    }

    public void setUseScaling(boolean aUseScaling) {
        options.setUseScaling(aUseScaling);
    }

    public void setUseThreads(boolean aUseThreads) {
        options.setUseThreads(aUseThreads);
    }

    public int getCropHeight() {
        return options.getCropHeight();
    }

    public int getScaledWidth() {
        return options.getScaledWidth();
    }

    public boolean isUseCropping() {
        return options.isUseCropping();
    }

    public void setCropWidth(int aCropWidth) {
        options.setCropWidth(aCropWidth);
    }

    public boolean isBypassFiltering() {
        return options.isBypassFiltering();
    }

    public int getCropLeft() {
        return options.getCropLeft();
    }

    public int getCropWidth() {
        return options.getCropWidth();
    }

    public int getScaledHeight() {
        return options.getScaledHeight();
    }

    public void setBypassFiltering(boolean aBypassFiltering) {
        options.setBypassFiltering(aBypassFiltering);
    }

    public void setUseCropping(boolean aUseCropping) {
        options.setUseCropping(aUseCropping);
    }

    public void setCropHeight(int aCropHeight) {
        options.setCropHeight(aCropHeight);
    }

    public void setFancyUpsampling(boolean aFancyUpsampling) {
        options.setFancyUpsampling(aFancyUpsampling);
    }

    public boolean isUseThreads() {
        return options.isUseThreads();
    }

    public boolean isFancyUpsampling() {
        return options.isFancyUpsampling();
    }

    public boolean isUseScaling() {
        return options.isUseScaling();
    }

    public void setCropLeft(int aCropLeft) {
        options.setCropLeft(aCropLeft);
    }

    public int getCropTop() {
        return options.getCropTop();
    }

    public void setScaledWidth(int aScaledWidth) {
        options.setScaledWidth(aScaledWidth);
    }

    public void setCropTop(int aCropTop) {
        options.setCropTop(aCropTop);
    }

    WebPDecoderOptions getDecoderOptions() {
        return options;
    }
}
