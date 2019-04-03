/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.util;

import java.awt.Color;
import junit.framework.TestCase;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Converter;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;

public class ColorConverterFactoryTest extends TestCase {

    private static final Color THISTLE = new Color(216, 191, 216);
    private static final Color LEMON_CHIFFON = new Color(255, 250, 205);
    private static final Color ALICE_BLUE = new Color(240, 248, 255);
    private static final Color GRAY_TRANSPARENT = new Color(128, 128, 128, 128);
    private static final Color GRAY = new Color(128, 128, 128);

    ColorConverterFactory factory;

    protected void setUp() throws Exception {
        factory = new ColorConverterFactory();
    }

    public void testVsConstantExpression() throws Exception {
        ConstantExpression expr = ConstantExpression.color(Color.RED);
        String expected = expr.evaluate(null, String.class);

        Converter converter = factory.createConverter(Color.class, String.class, null);
        String actual = converter.convert(Color.RED, String.class);

        assertEquals(expected, actual);
    }

    public void testFromString() throws Exception {
        assertEquals(Color.RED, convert("#FF0000"));
    }

    public void testFromInteger() throws Exception {
        assertEquals(Color.RED, convert(0xFF0000));
        assertEquals("no alpha", new Color(0, 0, 255, 255), convert(0x000000FF));

        assertEquals("255 alpha", new Color(0, 0, 255, 255), convert(0xFF0000FF));

        assertEquals("1 alpha", new Color(0, 0, 255, 1), convert(0x010000FF));
    }

    public void testFromLong() throws Exception {
        assertEquals(Color.RED, convert(0xFF0000));
        assertEquals("no alpha", new Color(0, 0, 255, 255), convert((long) 0x000000FF));

        assertEquals("255 alpha", new Color(0, 0, 255, 255), convert((long) 0xFF0000FF));

        assertEquals("1 alpha", new Color(0, 0, 255, 1), convert((long) 0x010000FF));
    }

    public void testToCSS() throws Exception {
        Converter converter =
                factory.createConverter(
                        Color.class, String.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));

        assertEquals("aliceblue", "aliceblue", converter.convert(ALICE_BLUE, String.class));
        assertEquals("gray", "gray", converter.convert(GRAY, String.class));

        assertEquals(
                "pale blue",
                "rgb(33,66,255)",
                converter.convert(new Color(33, 66, 255), String.class));

        assertEquals(
                "gray transparent",
                "rgba(128,128,128,0.5)",
                converter.convert(GRAY_TRANSPARENT, String.class));

        assertEquals(
                "blueish",
                "rgba(33,66,255,0.992)",
                converter.convert(new Color(33, 66, 255, 254), String.class));
    }

    public void testFromCss() throws Exception {
        Converter converter =
                factory.createConverter(
                        String.class, Color.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));
        assertEquals("aliceblue", ALICE_BLUE, converter.convert("aliceblue", Color.class));
        assertEquals("AliceBlue", ALICE_BLUE, converter.convert("AliceBlue", Color.class));
        assertEquals("gray", GRAY, converter.convert("gray", Color.class));
        assertEquals("lemonchiffon", LEMON_CHIFFON, converter.convert("lemonchiffon", Color.class));
        assertEquals("WHITE", Color.WHITE, converter.convert("WHITE", Color.class));
        assertEquals("black", Color.BLACK, converter.convert("black", Color.class));
        assertEquals("thistle", THISTLE, converter.convert("thistle", Color.class));
        assertEquals("hex", GRAY, converter.convert("#808080", Color.class));

        assertEquals("hex alpha", GRAY_TRANSPARENT, converter.convert("#80808080", Color.class));

        assertEquals("rgb", GRAY, converter.convert("rgb(128,128,128)", Color.class));

        assertEquals(
                "rgba", GRAY_TRANSPARENT, converter.convert("rgba(128,128,128, 0.5)", Color.class));

        assertEquals("rgba", GRAY, converter.convert("rgba(128,128,128, 1)", Color.class));

        assertEquals(
                "rgba",
                new Color(33, 66, 255, 254),
                converter.convert("rgba(33,66,255,0.99607843)", Color.class));
    }

    public void testAlpha() throws Exception {
        Converter converter = factory.createConverter(String.class, Color.class, null);

        assertEquals("hex", GRAY, converter.convert("#808080", Color.class));
        assertNull("hex alpha", converter.convert("#80808080", Color.class));
    }

    Color convert(Object value) throws Exception {
        Converter converter = factory.createConverter(value.getClass(), Color.class, null);
        return (Color) converter.convert(value, Color.class);
    }

    public void testRegisteredWithConverters() {
        Color color = Converters.convert("#189E77", Color.class);
        Color expected = new Color(24, 158, 119);
        assertNotNull("converter not registered", color);
        assertEquals(expected, color);
    }

    public void testCompactColor() {
        Color color = Converters.convert("#aaa", Color.class);
        assertEquals(170, color.getRed());
        assertEquals(170, color.getGreen());
        assertEquals(170, color.getBlue());
    }
}
