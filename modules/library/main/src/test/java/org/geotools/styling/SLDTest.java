/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the SLD utility class
 *
 * @author Jody
 */
public class SLDTest {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    StyleBuilder sb = new StyleBuilder(ff);

    @Before
    public void setUp() throws Exception {}
    /**
     * We should have a test case for the expected default values so we can be sure of correct SLD
     * rendering.
     */
    @Test
    public void testDefaults() {
        StrokeImpl stroke = sf.getDefaultStroke();
        assertEquals("default stroke width is one", 1, SLD.width(stroke));
        assertEquals("default stroke color is black", Color.BLACK, SLD.color(stroke));
    }
    /** See http://jira.codehaus.org/browse/UDIG-1374 */
    @Test
    public void testStroke() {
        StrokeImpl stroke = sf.createStroke(ff.literal("#FF0000"), ff.literal("3"));
        assertEquals("width", 3, SLD.width(stroke));
        assertEquals("color", Color.RED, SLD.color(stroke));

        stroke = sf.createStroke(ff.literal("#FF0000"), ff.literal("3.0"));
        assertEquals("width", 3, SLD.width(stroke));
    }

    /**
     * Test that setting the raster opacity correct duplicates the raster symbolizer as a different
     * object and correctly sets the opacity.
     */
    @Test
    public void testSetRasterOpacity() {
        RasterSymbolizerImpl rs = sb.createRasterSymbolizer();
        StyleImpl s = sb.createStyle(rs);

        assertEquals(1.0, SLD.opacity(SLD.rasterSymbolizer(s)), 0d);

        SLD.setRasterOpacity(s, 0.25);
        assertEquals(0.25, SLD.opacity(SLD.rasterSymbolizer(s)), 0d);
        assertNotSame(SLD.rasterSymbolizer(s), rs);
    }

    /**
     * Test to ensure that updating the channels duplicates the raster sybmolizer with the new rgb
     * channels
     */
    @Test
    public void testSetRasterRGBChannels() {
        RasterSymbolizerImpl rs = sb.createRasterSymbolizer();
        StyleImpl s = sb.createStyle(rs);

        SelectedChannelTypeImpl red =
                (SelectedChannelTypeImpl)
                        sf.createSelectedChannelType(
                                "red", sf.createContrastEnhancement(ff.literal(0.2)));
        SelectedChannelTypeImpl green =
                (SelectedChannelTypeImpl)
                        sf.createSelectedChannelType(
                                "green", sf.createContrastEnhancement(ff.literal(0.4)));
        SelectedChannelTypeImpl blue =
                (SelectedChannelTypeImpl)
                        sf.createSelectedChannelType(
                                "blue", sf.createContrastEnhancement(ff.literal(0.7)));

        SLD.setChannelSelection(s, new SelectedChannelTypeImpl[] {red, green, blue}, null);

        assertNull(SLD.rasterSymbolizer(s).getChannelSelection().getGrayChannel());
        assertNotNull(SLD.rasterSymbolizer(s).getChannelSelection().getRGBChannels());
        SelectedChannelTypeImpl[] selectedChannels =
                SLD.rasterSymbolizer(s).getChannelSelection().getRGBChannels();

        assertEquals("red", selectedChannels[0].getChannelName().evaluate(null, String.class));
        assertEquals("green", selectedChannels[1].getChannelName().evaluate(null, String.class));
        assertEquals("blue", selectedChannels[2].getChannelName().evaluate(null, String.class));

        assertNotSame(SLD.rasterSymbolizer(s), rs);
    }
}
