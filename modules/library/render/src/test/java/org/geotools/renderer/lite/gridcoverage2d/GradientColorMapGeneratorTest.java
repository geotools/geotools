/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.test.TestData;
import org.junit.Test;
import org.xml.sax.SAXException;

public class GradientColorMapGeneratorTest {

    @Test
    public void testSVG()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        final File svgFile = TestData.file(this, "sample.svg");
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(svgFile);
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 80);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(6, colorEntries.length);

        // First and last color entry are transparent
        assertEquals(0, colorEntries[0].getOpacity().evaluate(null, Double.class), 0d);
        assertEquals(0, colorEntries[5].getOpacity().evaluate(null, Double.class), 0d);

        assertEquals("#0000FF", colorEntries[1].getColor().toString());
        assertEquals("#00FFFF", colorEntries[2].getColor().toString());
        assertEquals("#FFFF00", colorEntries[3].getColor().toString());
        assertEquals("#FF0000", colorEntries[4].getColor().toString());

        assertEquals("17.0", colorEntries[1].getQuantity().toString());
        assertEquals("38.0", colorEntries[2].getQuantity().toString());
        assertEquals("66.0", colorEntries[3].getQuantity().toString());
        assertEquals("80.0", colorEntries[4].getQuantity().toString());
    }

    @Test
    public void testHEXcolors()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator("#0000ff;#00ffff;#ffff00;#ff0000");
        testBlueAcquaYellowRed(colorMapGenerator);
    }

    @Test
    public void testHEXcolors2()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "0x0000ff;0x00ffff;0xffff00;0xff0000");
        testBlueAcquaYellowRed(colorMapGenerator);
    }

    @Test
    public void testRGBcolors()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "rgb(0,0,255);rgb(0,255,255);rgb(255,255,0);rgb(255,0,0)");
        testBlueAcquaYellowRed(colorMapGenerator);
    }

    private void testBlueAcquaYellowRed(GradientColorMapGenerator colorMapGenerator) {
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(6, colorEntries.length);

        // First and last color entry are transparent
        assertEquals(0, colorEntries[0].getOpacity().evaluate(null, Double.class), 0d);
        assertEquals(0, colorEntries[5].getOpacity().evaluate(null, Double.class), 0d);

        assertEquals("#0000FF", colorEntries[1].getColor().toString());
        assertEquals("#00FFFF", colorEntries[2].getColor().toString());
        assertEquals("#FFFF00", colorEntries[3].getColor().toString());
        assertEquals("#FF0000", colorEntries[4].getColor().toString());

        assertEquals("10.0", colorEntries[1].getQuantity().toString());
        assertEquals("40.0", colorEntries[2].getQuantity().toString());
        assertEquals("70.0", colorEntries[3].getQuantity().toString());
        assertEquals("100.0", colorEntries[4].getQuantity().toString());
    }

    @Test
    public void testRGBAcolors()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "rgba(0,0,0,1);rgba(255,0,0,0.1);rgba(0,0,255,0)");
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(5, colorEntries.length);

        // first and last fully transparent
        assertEquals(0, colorEntries[0].getOpacity().evaluate(null, Double.class), 0d);
        assertEquals(0, colorEntries[4].getOpacity().evaluate(null, Double.class), 0d);

        // checking colors and ranges
        assertEntry(colorEntries[1], 10, Color.BLACK, 1f);
        assertEntry(colorEntries[2], 55, Color.RED, 0.1f);
        assertEntry(colorEntries[3], 100, Color.BLUE, 0f);
    }

    @Test
    public void testBeforeAfterColors()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "rgba(0,0,0,1);rgba(255,0,0,0.5);rgba(0,0,255,0.1)");
        colorMapGenerator.setBeforeColor(Color.RED);
        colorMapGenerator.setAfterColor(new Color(0, 0, 0, 0));
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(5, colorEntries.length);

        // checking colors and ranges
        assertEntry(colorEntries[0], 10, Color.RED, 1f);
        assertEntry(colorEntries[1], 10, Color.BLACK, 1f);
        assertEntry(colorEntries[2], 55, Color.RED, 0.5f);
        assertEntry(colorEntries[3], 100, Color.BLUE, 0.1f);
        assertEntry(colorEntries[4], 100, Color.BLACK, 0f);
    }

    @Test
    public void testBeforeAfterColorsString()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "rgba(0,0,0,1);rgba(255,0,0,0.5);rgba(0,0,255,0.1)");
        colorMapGenerator.setBeforeColor("rgba(0,0,255,0.5)");
        colorMapGenerator.setAfterColor("rgba(255,0,0,0.5)");
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(5, colorEntries.length);

        // checking colors and ranges
        assertEntry(colorEntries[0], 10, Color.BLUE, 0.5f);
        assertEntry(colorEntries[1], 10, Color.BLACK, 1f);
        assertEntry(colorEntries[2], 55, Color.RED, 0.5f);
        assertEntry(colorEntries[3], 100, Color.BLUE, 0.1f);
        assertEntry(colorEntries[4], 100, Color.RED, 0.5f);
    }

    @Test
    public void testBeforeAfterNullColors()
            throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator =
                GradientColorMapGenerator.getColorMapGenerator(
                        "rgba(0,0,0,1);rgba(255,0,0,0.5);rgba(0,0,255,0.1)");
        colorMapGenerator.setBeforeColor((String) null);
        colorMapGenerator.setAfterColor((String) null);
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(5, colorEntries.length);

        // checking colors and ranges
        assertEntry(colorEntries[0], 10, Color.BLACK, 0f);
        assertEntry(colorEntries[1], 10, Color.BLACK, 1f);
        assertEntry(colorEntries[2], 55, Color.RED, 0.5f);
        assertEntry(colorEntries[3], 100, Color.BLUE, 0.1f);
        assertEntry(colorEntries[4], 100, Color.BLACK, 0f);
    }

    private void assertEntry(ColorMapEntry entry, double quantity, Color color, float opacity) {
        assertEquals(quantity, entry.getQuantity().evaluate(null, Double.class), 0d);
        assertEquals(color, entry.getColor().evaluate(null, Color.class));
        assertEquals(opacity, entry.getOpacity().evaluate(null, Double.class), 0.01f);
    }
}
