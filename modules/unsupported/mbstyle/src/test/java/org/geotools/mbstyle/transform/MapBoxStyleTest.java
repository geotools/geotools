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

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.styling.RasterSymbolizer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.PolygonSymbolizer;
import org.opengis.style.Rule;
import org.opengis.style.Symbolizer;

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

        assertEquals(1, fts.rules().size());
        for (Rule r : fts.rules()) {
            assertEquals(1, r.symbolizers().size());
            for (Symbolizer symbolizer : r.symbolizers()) {
                assertTrue(symbolizer instanceof PolygonSymbolizer);
                PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;
                Expression expr =  psym.getFill().getColor();
                assertNotNull("fillColor set", expr);
                assertEquals( Color.decode("#E100FF"), expr.evaluate(null,Color.class) );
                assertEquals(Double.valueOf(.84),
                        psym.getFill().getOpacity().evaluate(null, Double.class));
            }
        }

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

}
