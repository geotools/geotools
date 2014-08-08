package org.geotools.ysld.encode;

import org.geotools.styling.Font;

public class FontEncoder extends Encoder<Font> {
    FontEncoder(Font font) {
        super(font);
    }

    @Override
    protected void encode(Font font) {
        put("font-family", font.getFontFamily());
        put("font-size", font.getSize());
        put("font-style", font.getStyle());
        put("font-weight", font.getWeight());
    }
}
