/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
import org.junit.Test;

public class HSLColorTest {

    @Test
    public void testBlack() {
        testComponents(Color.BLACK, 0, 0, 0);
        testRoundTrip(Color.BLACK);
    }

    @Test
    public void testWhite() {
        testComponents(Color.WHITE, 0, 0, 1);
        testRoundTrip(Color.WHITE);
    }

    @Test
    public void testRed() {
        testComponents(Color.RED, 0f, 1f, 0.5f);
        testRoundTrip(Color.RED);
    }

    @Test
    public void testGreen() {
        testComponents(Color.GREEN, 120f, 1f, 0.5f);
        testRoundTrip(Color.GREEN);
    }

    @Test
    public void testBlue() {
        testComponents(Color.BLUE, 240f, 1f, 0.5f);
        testRoundTrip(Color.BLUE);
    }

    @Test
    public void testYellow() {
        testComponents(Color.YELLOW, 60f, 1f, 0.5f);
        testRoundTrip(Color.YELLOW);
    }

    @Test
    public void testCyan() {
        testComponents(Color.CYAN, 180f, 1f, 0.5f);
        testRoundTrip(Color.CYAN);
    }

    @Test
    public void testLightGray() {
        testComponents(Color.LIGHT_GRAY, 0, 0f, 0.75f);
        testRoundTrip(Color.LIGHT_GRAY);
    }

    @Test
    public void testDarkGray() {
        testComponents(Color.DARK_GRAY, 0, 0f, 0.25f);
        testRoundTrip(Color.DARK_GRAY);
    }

    private void testComponents(Color color, float h, float s, float l) {
        HSLColor hsl = new HSLColor(color);
        assertEquals(h, hsl.getHue(), 1e-2f);
        assertEquals(s, hsl.getSaturation(), 1e-2f);
        assertEquals(l, hsl.getLightness(), 1e-2f);
    }

    private void testRoundTrip(Color color) {
        HSLColor hsl = new HSLColor(color);
        Color rgb = hsl.toRGB();
        assertEquals(color, rgb);
    }
}
