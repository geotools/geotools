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
import java.util.List;

import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.geotools.styling.FeatureTypeStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class SymbolMBLayerTest {
    SymbolMBLayer testLayerDefault;
    SymbolMBLayer testLayer;
    SymbolMBLayer testAngleLayer;
    MBStyle defaultStyle;
    MBStyle angleStyle;
    List<FeatureTypeStyle> featureTypeWithAngle;
    List<FeatureTypeStyle> featureTypeDefaults;

    
    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonDefault = MapboxTestUtils.parseTestStyle("symbolStyleTestDefaults.json");
        JSONObject json = MapboxTestUtils.parseTestStyle("symbolStyleTest.json");
        JSONObject jsonAngle = MapboxTestUtils.parseTestStyle("symbolTextLinePlacementTest.json");
        angleStyle = MBStyle.create(jsonAngle);
        defaultStyle = MBStyle.create(jsonDefault);
        testAngleLayer = (SymbolMBLayer) angleStyle.layer("testid");
        testLayerDefault = (SymbolMBLayer) MBStyle.create(jsonDefault).layer("testid");
        testLayer = (SymbolMBLayer) MBStyle.create(json).layer("testid");
        featureTypeWithAngle = testAngleLayer.transformInternal(angleStyle);
        featureTypeDefaults = testLayerDefault.transformInternal(defaultStyle);
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
    @Test
    public void testTextMaxAngle(){
    	// Test json values
    	assertEquals(25, testAngleLayer.getTextMaxAngle().intValue());
    	// now null assertEquals(45, testLayerDefault.getTextMaxAngle().intValue());
    	// Test FeatureTypeStyle
    	// assertEquals("45.0", featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0].getOptions().get("maxAngleDelta"));
    	// now null assertEquals("false", featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0].getOptions().get("followLine"));
    	assertEquals("true", featureTypeWithAngle.get(0).rules().get(0).getSymbolizers()[0].getOptions().get("followLine"));
    }
}