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

import java.awt.Color;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;

/**
 * Test cases for the SLD utility class
 * @author Jody
 *
 *
 * @source $URL$
 */
public class SLDTest extends TestCase {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    StyleBuilder sb = new StyleBuilder(ff);
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    /**
     * We should have a test case for the expected default values
     * so we can be sure of correct SLD rendering.
     */
    public void testDefaults(){
        Stroke stroke = sf.getDefaultStroke();        
        assertEquals( "default stroke width is one", 1, SLD.width( stroke ));
        assertEquals( "default stroke color is black", Color.BLACK, SLD.color( stroke ));
    }
    /**
     * See http://jira.codehaus.org/browse/UDIG-1374
     */
    public void testStroke(){
        Stroke stroke = sf.createStroke( ff.literal("#FF0000"), ff.literal("3") );
        assertEquals( "width", 3, SLD.width( stroke ));
        assertEquals( "color", Color.RED, SLD.color( stroke ));
        
        stroke = sf.createStroke( ff.literal("#FF0000"), ff.literal("3.0") );
        assertEquals( "width", 3, SLD.width( stroke ));
    }
    
    
    /**
     * Test that setting the raster opacity correct duplicates the
     * raster symbolizer as a different object and correctly
     * sets the opacity.
     */
    public void testSetRasterOpacity(){
    	RasterSymbolizer rs = sb.createRasterSymbolizer();
    	Style s = sb.createStyle(rs);
    	
    	assertEquals(1.0, SLD.opacity(SLD.rasterSymbolizer(s)));
    	
    	SLD.setRasterOpacity(s, 0.25);
    	assertEquals(0.25, SLD.opacity(SLD.rasterSymbolizer(s)));
    	assertNotSame(SLD.rasterSymbolizer(s), rs);
    }
    
    /**
     * Test to ensure that updating the channels duplicates
     * the raster sybmolizer with the new rgb channels
     */
    public void testSetRasterRGBChannels(){
    	RasterSymbolizer rs = sb.createRasterSymbolizer();
    	Style s = sb.createStyle(rs);
    	
    	SelectedChannelType red =  sf.createSelectedChannelType("red", sf.createContrastEnhancement(ff.literal(0.2)));
    	SelectedChannelType green =  sf.createSelectedChannelType("green", sf.createContrastEnhancement(ff.literal(0.4)));
    	SelectedChannelType blue =  sf.createSelectedChannelType("blue", sf.createContrastEnhancement(ff.literal(0.7)));
    	
    	SLD.setChannelSelection(s, new SelectedChannelType[]{red, green, blue}, null);
    	
    	assertNull(SLD.rasterSymbolizer(s).getChannelSelection().getGrayChannel());
    	assertNotNull(SLD.rasterSymbolizer(s).getChannelSelection().getRGBChannels());
    	SelectedChannelType[] selectedChannels = SLD.rasterSymbolizer(s).getChannelSelection().getRGBChannels();
    	
    	assertEquals("red", selectedChannels[0].getChannelName());
    	assertEquals("green", selectedChannels[1].getChannelName());
    	assertEquals("blue", selectedChannels[2].getChannelName());
    	
    	assertNotSame(SLD.rasterSymbolizer(s), rs);
    	
    }
}
