package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.*;

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
    public void testSVG () throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        final File svgFile = TestData.file(this, "sample.svg");
        GradientColorMapGenerator colorMapGenerator = GradientColorMapGenerator.getColorMapGenerator(svgFile);
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 80);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(6, colorEntries.length);

        // First and last color entry are transparent
        assertEquals("0", colorEntries[0].getOpacity().toString());
        assertEquals("0", colorEntries[5].getOpacity().toString());

        assertEquals("#ff", colorEntries[1].getColor().toString());
        assertEquals("#ffff", colorEntries[2].getColor().toString());
        assertEquals("#ffff00", colorEntries[3].getColor().toString());
        assertEquals("#ff0000", colorEntries[4].getColor().toString());

        assertEquals("17.0", colorEntries[1].getQuantity().toString());
        assertEquals("38.0", colorEntries[2].getQuantity().toString());
        assertEquals("66.0", colorEntries[3].getQuantity().toString());
        assertEquals("80.0", colorEntries[4].getQuantity().toString());
    }

    @Test
    public void testHEXcolors () throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        GradientColorMapGenerator colorMapGenerator = GradientColorMapGenerator.getColorMapGenerator("#0000ff;#00ffff;#ffff00;#ff0000");
        ColorMap colorMap = colorMapGenerator.generateColorMap(10, 100);
        assertNotNull(colorMap);
        ColorMapEntry[] colorEntries = colorMap.getColorMapEntries();
        assertNotNull(colorEntries);
        assertEquals(6, colorEntries.length);

        // First and last color entry are transparent
        assertEquals("0", colorEntries[0].getOpacity().toString());
        assertEquals("0", colorEntries[5].getOpacity().toString());

        assertEquals("#ff", colorEntries[1].getColor().toString());
        assertEquals("#ffff", colorEntries[2].getColor().toString());
        assertEquals("#ffff00", colorEntries[3].getColor().toString());
        assertEquals("#ff0000", colorEntries[4].getColor().toString());

        assertEquals("10.0", colorEntries[1].getQuantity().toString());
        assertEquals("40.0", colorEntries[2].getQuantity().toString());
        assertEquals("70.0", colorEntries[3].getQuantity().toString());
        assertEquals("100.0", colorEntries[4].getQuantity().toString());
    }
}
