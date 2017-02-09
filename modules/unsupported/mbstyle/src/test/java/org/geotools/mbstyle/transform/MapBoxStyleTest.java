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
package org.geotools.mbstyle.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.LineMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Symbolizer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * Test parsing and transforming a Mapbox fill layer from json.
 */
public class MapBoxStyleTest {

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    JSONParser jsonParser = new JSONParser();

    /**
     * Test parsing a Mapbox fill layer
     */
    @Test
    public void testFill() throws IOException, ParseException {

        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("fillStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillMBLayer);
        FillMBLayer mbFill = (FillMBLayer) layers.get(0);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill);
        
        PolygonSymbolizer psym = SLD.polySymbolizer(fts);
        
        Expression expr =  psym.getFill().getColor();
        assertNotNull("fillColor set", expr);
        assertEquals( Color.decode("#E100FF"), expr.evaluate(null,Color.class) );
        assertEquals(Double.valueOf(.84),
                psym.getFill().getOpacity().evaluate(null, Double.class));
    }

    /**
     * Test parsing a Mapbox raster layer
     */
    @Test
    public void testRaster() throws IOException, ParseException {
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("rasterStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-raster");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof RasterMBLayer);
        RasterMBLayer mbFill = (RasterMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof RasterSymbolizer);
        RasterSymbolizer rsym = (RasterSymbolizer) symbolizer;

        assertEquals(Double.valueOf(.59), rsym.getOpacity().evaluate(null, Double.class));
    }

    /**
     * Test parsing a Mapbox line layer
     */
    @Test
    public void testLine() throws IOException, ParseException {
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("lineStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbLine);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;

        assertEquals(new Color(0, 153, 255),
                lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(4), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
               
        List<Integer> expectedDashes = Arrays.asList(10, 5, 3, 2);
        assertEquals(expectedDashes.size(), lsym.getStroke().dashArray().size());
        for (int i = 0; i < expectedDashes.size(); i++) {
            Integer n = (Integer) lsym.getStroke().dashArray().get(i).evaluate(null, Integer.class);
            assertEquals(expectedDashes.get(i), n);
        }        
    }
    
    @Test
    public void testLineDefaults() throws IOException, ParseException {
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("lineStyleTestEmpty.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbLine);

        assertEquals(1, fts.rules().size());
        Rule r = fts.rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;
        
        assertEquals(Color.BLACK, lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("butt", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("mitre", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(Integer.valueOf(1), lsym.getStroke().getOpacity().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(1), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(0), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
        assertTrue(lsym.getStroke().dashArray() == null);        
    }
    

}
