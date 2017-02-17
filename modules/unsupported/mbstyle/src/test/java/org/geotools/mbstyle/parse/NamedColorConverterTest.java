package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.awt.Color;

import org.geotools.util.Converter;
import org.junit.Test;
import org.junit.Ignore;

public class NamedColorConverterTest {

    @Test
    public void htmlColors() throws Exception {
        Converter html = NamedColorConverterFactory.CONVERT_HTML;

        // defined
        assertEquals(Color.WHITE, html.convert("white", Color.class));
        assertEquals(Color.WHITE, html.convert("WHITE", Color.class));

        // not defined
        assertNull(html.convert("orange", Color.class));
    }

    @Test
    public void webColors() throws Exception {
        Converter web = NamedColorConverterFactory.CONVERT_WEB;

        assertEquals(Color.WHITE, web.convert("white", Color.class));
        assertEquals(Color.BLACK, web.convert("BLACK", Color.class));
        assertEquals(new Color(255, 165, 0), web.convert("orange", Color.class));

        // web specific
        assertEquals(new Color(0x808080), web.convert("Gray", Color.class));

        // undefined
        assertNull(web.convert("RebeccaPurple", Color.class));
    }

    @Test
    public void cssColors() throws Exception {
        Converter css = NamedColorConverterFactory.CONVERT_CSS;

        assertEquals(Color.WHITE, css.convert("white", Color.class));
        assertEquals(new Color(255, 165, 0), css.convert("orange", Color.class));
        assertEquals(new Color(102, 51, 153), css.convert("RebeccaPurple", Color.class));
    }

    @Ignore
    public void x11Colors() throws Exception {
        Converter x11 = NamedColorConverterFactory.CONVERT_X11;

        assertEquals(Color.WHITE, x11.convert("white", Color.class));
        assertEquals(new Color(255, 165, 0), x11.convert("orange", Color.class));

        // x11 specific
        assertEquals(new Color(0x808080), x11.convert("Web Gray", Color.class));
        assertEquals(new Color(0xBEBEBE), x11.convert("Gray", Color.class));
    }

    @Ignore
    public void conflicts() throws Exception {
        Converter x11 = NamedColorConverterFactory.CONVERT_X11;
        Converter css = NamedColorConverterFactory.CONVERT_CSS;

        assertNotEquals(x11.convert("gray", Color.class), css.convert("gray", Color.class));
    }
}
