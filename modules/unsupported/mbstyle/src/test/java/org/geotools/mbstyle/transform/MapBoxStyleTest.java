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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.geotools.mbstyle.MBFillLayer;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.PolygonSymbolizer;
import org.opengis.style.Rule;
import org.opengis.style.Symbolizer;




/**
 * Test parsing and transforming a Mapbox fill layer from json.
 */
public class MapBoxStyleTest {
    @Test
    public void testFill() throws IOException, ParseException {
        assertTrue("hello world", true);
        
        InputStream is = this.getClass().getResourceAsStream("fillStyleTest.json");       
        String s = IOUtils.toString(is, "utf-8");
        
        JSONParser j = new JSONParser();
        JSONObject o = (JSONObject) j.parse(s);
        JSONArray array = (JSONArray) o.get("layers");        
        assertEquals(1, array.size());
        
        JSONObject fillLayer = (JSONObject) array.get(0);

        MBFillLayer mbFill = new MBFillLayer(fillLayer);
        FeatureTypeStyle fts = new MBStyleTransformer().transform(mbFill);
        
        
        assertEquals(1, fts.rules().size());
        for (Rule r : fts.rules()) {
            assertEquals(1, r.symbolizers().size());
            for (Symbolizer symbolizer : r.symbolizers()) {
                assertTrue(PolygonSymbolizer.class.isAssignableFrom(symbolizer.getClass()));
                PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;
                assertTrue("#ffff00".equalsIgnoreCase((String) psym.getFill().getColor().evaluate(null)));
                
                System.out.println("Fill Color: " + psym.getFill().getColor());
                System.out.println("Opacity: " + psym.getFill().getOpacity());                
            }
        }
        
    }

}
