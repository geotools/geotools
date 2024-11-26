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

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.awt.Color;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Converter;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColorConverterFactoryTest {

    private static final Color THISTLE = new Color(216, 191, 216);
    private static final Color LEMON_CHIFFON = new Color(255, 250, 205);
    private static final Color ALICE_BLUE = new Color(240, 248, 255);
    private static final Color GRAY_TRANSPARENT = new Color(128, 128, 128, 128);
    private static final Color GRAY = new Color(128, 128, 128);

    ColorConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ColorConverterFactory();
    }

    @Test
    public void testVsConstantExpression() throws Exception {
        ConstantExpression expr = ConstantExpression.color(Color.RED);
        String expected = expr.evaluate(null, String.class);

        Converter converter = factory.createConverter(Color.class, String.class, null);
        String actual = converter.convert(Color.RED, String.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFromString() throws Exception {
        Assert.assertEquals(RED, convert("#FF0000"));
    }

    @Test
    public void testFromInteger() throws Exception {
        Assert.assertEquals(RED, convert(0xFF0000));
        Assert.assertEquals("no alpha", new Color(0, 0, 255, 255), convert(0x000000FF));

        Assert.assertEquals("255 alpha", new Color(0, 0, 255, 255), convert(0xFF0000FF));

        Assert.assertEquals("1 alpha", new Color(0, 0, 255, 1), convert(0x010000FF));
    }

    @Test
    public void testFromLong() throws Exception {
        Assert.assertEquals(RED, convert(0xFF0000));
        Assert.assertEquals("no alpha", new Color(0, 0, 255, 255), convert((long) 0x000000FF));

        Assert.assertEquals("255 alpha", new Color(0, 0, 255, 255), convert((long) 0xFF0000FF));

        Assert.assertEquals("1 alpha", new Color(0, 0, 255, 1), convert((long) 0x010000FF));
    }

    @Test
    public void testToCSS() throws Exception {
        Converter converter =
                factory.createConverter(
                        Color.class, String.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));

        Assert.assertEquals("aliceblue", "aliceblue", converter.convert(ALICE_BLUE, String.class));
        // There are two names so we could get either.
        assertThat(converter.convert(GRAY, String.class), anyOf(is("gray"), is("grey")));

        Assert.assertEquals(
                "pale blue",
                "rgb(33,66,255)",
                converter.convert(new Color(33, 66, 255), String.class));

        Assert.assertEquals(
                "gray transparent",
                "rgba(128,128,128,0.5)",
                converter.convert(GRAY_TRANSPARENT, String.class));

        Assert.assertEquals(
                "blueish",
                "rgba(33,66,255,0.992)",
                converter.convert(new Color(33, 66, 255, 254), String.class));
    }

    @Test
    public void testFromCss() throws Exception {
        Converter converter =
                factory.createConverter(
                        String.class, Color.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));
        Assert.assertEquals("aliceblue", ALICE_BLUE, converter.convert("aliceblue", Color.class));
        Assert.assertEquals("AliceBlue", ALICE_BLUE, converter.convert("AliceBlue", Color.class));
        Assert.assertEquals("gray", GRAY, converter.convert("gray", Color.class));
        Assert.assertEquals(
                "lemonchiffon", LEMON_CHIFFON, converter.convert("lemonchiffon", Color.class));
        Assert.assertEquals("WHITE", WHITE, converter.convert("WHITE", Color.class));
        Assert.assertEquals("black", BLACK, converter.convert("black", Color.class));
        Assert.assertEquals("thistle", THISTLE, converter.convert("thistle", Color.class));
        Assert.assertEquals("hex", GRAY, converter.convert("#808080", Color.class));

        Assert.assertEquals(
                "hex alpha", GRAY_TRANSPARENT, converter.convert("#80808080", Color.class));

        Assert.assertEquals("rgb", GRAY, converter.convert("rgb(128,128,128)", Color.class));

        Assert.assertEquals(
                "rgba", GRAY_TRANSPARENT, converter.convert("rgba(128,128,128, 0.5)", Color.class));

        Assert.assertEquals("rgba", GRAY, converter.convert("rgba(128,128,128, 1)", Color.class));

        Assert.assertEquals(
                "rgba",
                new Color(33, 66, 255, 254),
                converter.convert("rgba(33,66,255,0.99607843)", Color.class));
    }

    @Test
    public void testAlpha() throws Exception {
        Converter converter = factory.createConverter(String.class, Color.class, null);

        Assert.assertEquals("hex", GRAY, converter.convert("#808080", Color.class));
        Assert.assertNull("hex alpha", converter.convert("#80808080", Color.class));
    }

    Color convert(Object value) throws Exception {
        Converter converter = factory.createConverter(value.getClass(), Color.class, null);
        return converter.convert(value, Color.class);
    }

    @Test
    public void testRegisteredWithConverters() {
        Color color = Converters.convert("#189E77", Color.class);
        Color expected = new Color(24, 158, 119);
        Assert.assertNotNull("converter not registered", color);
        Assert.assertEquals(expected, color);
    }

    @Test
    public void testCompactColor() {
        Color color = Converters.convert("#aaa", Color.class);
        Assert.assertEquals(170, color.getRed());
        Assert.assertEquals(170, color.getGreen());
        Assert.assertEquals(170, color.getBlue());
    }

    @Test
    public void testHsl() throws Exception {
        Converter converter =
                factory.createConverter(
                        String.class, Color.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));
        Assert.assertEquals(BLACK, converter.convert("hsl(0, 0, 0)", Color.class));
        Assert.assertEquals(WHITE, converter.convert("hsl(0, 0, 100%)", Color.class));
        Assert.assertEquals(RED, converter.convert("hsl(0, 100%, 50%)", Color.class));
        Assert.assertEquals(GREEN, converter.convert("hsl(120, 100%, 50%)", Color.class));
    }

    @Test
    public void testHsla() throws Exception {
        Converter converter =
                factory.createConverter(
                        String.class, Color.class, new Hints(Hints.COLOR_DEFINITION, "CSS"));
        Assert.assertEquals(BLACK, converter.convert("hsla(0, 0%, 0%, 1)", Color.class));
        Assert.assertEquals(
                new Color(255, 255, 255, 128),
                converter.convert("hsla(0, 0%, 100%, 0.5)", Color.class));
    }
}
