/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 20'17, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.color;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Converters;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

public class ColorFunctionsTest {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testSaturateAbsolute() {
        Function function = FF.function("saturate", FF.literal("#80e619"), FF.literal("20%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80FF00", Converters.convert(result, String.class));
    }

    @Test
    public void testSaturateAbsoluteOvershoot() {
        Function function = FF.function("saturate", FF.literal("#80e619"), FF.literal("80%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80FF00", Converters.convert(result, String.class));
    }

    @Test
    public void testSaturateRelative() {
        Function function =
                FF.function(
                        "saturate",
                        FF.literal("#80e619"),
                        FF.literal("10%"),
                        FF.literal("relative"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80F00F", Converters.convert(result, String.class));
    }

    @Test
    public void testDesaturateAbsolute() {
        Function function = FF.function("desaturate", FF.literal("#80e619"), FF.literal("20%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80CC33", Converters.convert(result, String.class));
    }

    @Test
    public void testDesaturateAbsoluteOvershoot() {
        Function function = FF.function("desaturate", FF.literal("#80e619"), FF.literal("90%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#808080", Converters.convert(result, String.class));
    }

    @Test
    public void testDesaturateRelative() {
        Function function =
                FF.function(
                        "desaturate",
                        FF.literal("#80e619"),
                        FF.literal("10%"),
                        FF.literal("relative"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80DC23", Converters.convert(result, String.class));
    }

    @Test
    public void testDarken() {
        Function function = FF.function("darken", FF.literal("#3cb878"), FF.literal("30%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#16452D", Converters.convert(result, String.class));
    }

    @Test
    public void testLighten() {
        Function function = FF.function("lighten", FF.literal("#3cb878"), FF.literal("30%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#AAE3C6", Converters.convert(result, String.class));
    }

    @Test
    public void testMix() {
        Function function =
                FF.function("mix", FF.literal("#ff0000"), FF.literal("#0000ff"), FF.literal("50%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#800080", Converters.convert(result, String.class));
    }

    @Test
    public void testTint() {
        Function function = FF.function("tint", FF.literal("#007fff"), FF.literal("50%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80BFFF", Converters.convert(result, String.class));
    }

    @Test
    public void testShade() {
        Function function = FF.function("shade", FF.literal("#007fff"), FF.literal("50%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#004080", Converters.convert(result, String.class));
    }

    @Test
    public void testSpin() {
        Color color = new HSLColor(10, 0.9, 0.5).toRGB();
        Function function = FF.function("spin", FF.literal(color), FF.literal(30));
        Color result = (Color) function.evaluate(null);
        assertEquals("#F2A60D", Converters.convert(result, String.class));
    }

    @Test
    public void testSpinBackwards() {
        Color color = new HSLColor(10, 0.9, 0.5).toRGB(); // this conversion is lossy
        Function function = FF.function("spin", FF.literal(color), FF.literal(-30));
        Color result = (Color) function.evaluate(null);
        assertEquals("#F20D5A", Converters.convert(result, String.class));
    }

    @Test
    public void testGrayscale() {
        Function function = FF.function("grayscale", FF.literal("#ff0000"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#808080", Converters.convert(result, String.class));
    }

    @Test
    public void testContrastNoLightDark() {
        Function function = FF.function("contrast", FF.literal("#bbbbbb"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#000000", Converters.convert(result, String.class));
    }

    @Test
    public void testContrastDarkReference() {
        Function function = FF.function("contrast", FF.literal("#222222"), FF.literal("#101010"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#FFFFFF", Converters.convert(result, String.class));
    }

    @Test
    public void testContrastDarkLigthReference() {
        Function function =
                FF.function(
                        "contrast",
                        FF.literal("#222222"),
                        FF.literal("#101010"),
                        FF.literal("#dddddd"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#DDDDDD", Converters.convert(result, String.class));
    }

    @Test
    public void testContrastLowThreshold() {
        Color color = new HSLColor(90, 1, 0.5).toRGB();
        Function function =
                FF.function(
                        "contrast",
                        FF.literal(color),
                        FF.literal("#000000"),
                        FF.literal("#ffffff"),
                        FF.literal("30%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#000000", Converters.convert(result, String.class));
    }

    @Test
    public void testContrastHighThreshold() {
        Color color = new HSLColor(90, 1, 0.5).toRGB();
        Function function =
                FF.function(
                        "contrast",
                        FF.literal(color),
                        FF.literal("#000000"),
                        FF.literal("#ffffff"),
                        FF.literal("80%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#FFFFFF", Converters.convert(result, String.class));
    }

    @Test
    public void testHslFunction() {
        Function function =
                FF.function("hsl", FF.literal(90), FF.literal("100%"), FF.literal("50%"));
        Color result = (Color) function.evaluate(null);
        assertEquals("#80FF00", Converters.convert(result, String.class));
    }
}
