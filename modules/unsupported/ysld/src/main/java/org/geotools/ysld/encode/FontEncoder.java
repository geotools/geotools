package org.geotools.ysld.encode;

import org.geotools.styling.Font;

public class FontEncoder extends YsldEncodeHandler<Font> {
    FontEncoder(Font font) {
        super(font);
    }

    @Override
    protected void encode(Font font) {
        putName("font-family", font.getFontFamily());
        put("font-size", font.getSize());
        putName("font-style", font.getStyle());
        putName("font-weight", font.getWeight());
    }
}
