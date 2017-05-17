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
package org.geotools.mbstyle;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class SymbolMBLayerTest {
    SymbolMBLayer testLayerDefault;
    SymbolMBLayer testLayer;
    MBStyle defaultStyle;

    
    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonDefault = MapboxTestUtils.parseTestStyle("symbolStyleTestDefaults.json");
        JSONObject json = MapboxTestUtils.parseTestStyle("symbolStyleTest.json");
        defaultStyle = MBStyle.create(jsonDefault);
        testLayerDefault = (SymbolMBLayer) MBStyle.create(jsonDefault).layer("testid");
        testLayer = (SymbolMBLayer) MBStyle.create(json).layer("testid");

        
    }

    @Test
    public void testTextAnchorEnum(){
        // cannot use valueOf directly
        assertEquals( TextAnchor.CENTER, TextAnchor.parse("center"));
        assertEquals( TextAnchor.BOTTOM_LEFT, TextAnchor.parse("bottom-left"));
        
        // json
        assertEquals( "bottom-left", TextAnchor.BOTTOM_LEFT.json() );
        
        // justification
        assertEquals( 0.5, TextAnchor.getAnchorX("center"), 0.0 );
        assertEquals( 0.5, TextAnchor.getAnchorY("center"), 0.0 );
    }

    @Test
    public void testTextMaxWidth(){
        //Test default MBStyle value
        assertEquals(160, testLayerDefault.getTextMaxWidth().intValue() * testLayerDefault.getTextSize().intValue());
        //Test generated MBStyle values
        assertEquals(100, testLayer.getTextMaxWidth().intValue() * testLayer.getTextSize().intValue());
        
    }
}