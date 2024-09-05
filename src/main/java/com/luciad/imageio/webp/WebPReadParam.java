
package com.luciad.imageio.webp;

import javax.imageio.ImageReadParam;

public final class WebPReadParam extends ImageReadParam {
    private final WebPDecoderOptions options;

    public WebPReadParam() {
        this.options = new WebPDecoderOptions();
    }

    public WebPDecoderOptions options() {
        return options;
    }
}
