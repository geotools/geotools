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
package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.styling.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/** Created by vickdw on 4/17/17. */
public class MBStopsTest {
    private StyleFactory sf;

    @Test
    public void testHasPropertyAndZoomFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("textFontTest.json");

        MBStyle mbStyle = new MBStyle(styleJson);
        List<MBLayer> layers = mbStyle.layers();
        if (layers.isEmpty()) {
            throw new MBFormatException("layers empty");
        }
        for (MBLayer layer : layers) {
            MBObjectStops mbObjectStops = new MBObjectStops(layer);
            if (mbObjectStops.ls.hasStops) {
                // System.out.println(layer.getId() + " Contains property and zoom functions");
                assertTrue(
                        layer.getId() + " Contains property and zoom functions",
                        mbObjectStops.ls.hasStops);
            } else {
                // System.out.println(layer.getId() + " does not contain property and zoom
                // functions");
                assertFalse(
                        layer.getId() + " does not contain property and zoom functions",
                        mbObjectStops.ls.hasStops);
            }
        }
    }

    @Test
    public void testReducePropertyAndZoomFunctions() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers();
        if (layers.isEmpty()) {
            throw new MBFormatException("layers empty");
        }
        for (MBLayer layer : layers) {
            MBObjectStops mbObjectStops = new MBObjectStops(layer);
            if (mbObjectStops.ls.hasStops) {
                /*
                * Here we are testing this function, when reduced to zoom level 0.
                *
                   "circle-radius": {
                                       "property": "rating",
                                       "stops": [
                                         [{"zoom": 0,  "value": 0}, 0],
                                         [{"zoom": 0,  "value": 5}, 5],
                                         [{"zoom": 20, "value": 0}, 0],
                                         [{"zoom": 20, "value": 5}, 20]
                                       ]
                   }
                *
                *  Reduced for zoom level 0, it should look like this:
                *
                   "circle-radius": {
                                       "property": "rating",
                                       "stops": [
                                         [0, 0],
                                         [5, 5]
                                       ]
                   }
                *
                *
                */
                assertEquals("function4", layer.getId());
                assertEquals(2, mbObjectStops.stops.size());
                assertEquals(2, mbObjectStops.layersForStop.size());
                int i = 0;
                for (MBLayer l : mbObjectStops.layersForStop) {
                    if (i == 0) {
                        JSONObject circleRadius = (JSONObject) l.getPaint().get("circle-radius");
                        JSONArray stopsArray = (JSONArray) circleRadius.get("stops");
                        assertEquals("rating", circleRadius.get("property"));
                        assertEquals(2, stopsArray.size());
                        Object stop0Obj = stopsArray.get(0);
                        assertTrue(stop0Obj instanceof JSONArray);
                        JSONArray stop0 = (JSONArray) stop0Obj;
                        // For zoom level 0, Expect: [0, 0]
                        assertEquals(0L, stop0.get(0));
                        assertEquals(0L, stop0.get(1));

                        Object stop1Obj = stopsArray.get(1);
                        assertTrue(stop1Obj instanceof JSONArray);
                        // For zoom level 0,  Expect: [5, 5]
                        JSONArray stop1 = (JSONArray) stop1Obj;
                        assertEquals(5L, stop1.get(0));
                        assertEquals(5L, stop1.get(1));
                    } else if (i == 1) {
                        JSONObject circleRadius = (JSONObject) l.getPaint().get("circle-radius");
                        JSONArray stopsArray = (JSONArray) circleRadius.get("stops");
                        assertEquals("rating", circleRadius.get("property"));
                        assertEquals(2, stopsArray.size());
                        Object stop0Obj = stopsArray.get(0);
                        assertTrue(stop0Obj instanceof JSONArray);
                        JSONArray stop0 = (JSONArray) stop0Obj;
                        // For zoom level 0, Expect: [0, 0]
                        assertEquals(0L, stop0.get(0));
                        assertEquals(0L, stop0.get(1));

                        Object stop1Obj = stopsArray.get(1);
                        assertTrue(stop1Obj instanceof JSONArray);
                        // For zoom level 20,  Expect: [5, 20]
                        JSONArray stop1 = (JSONArray) stop1Obj;
                        assertEquals(5L, stop1.get(0));
                        assertEquals(20L, stop1.get(1));
                    }
                    i++;
                }
            }
        }
    }

    @Test
    public void testPropertyAndZoomScaleDenominators() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor transformed = mbStyle.transform();
        List<StyledLayer> styledLayers = transformed.layers();
        List<FeatureTypeStyle> fts =
                ((UserLayer) styledLayers.get(0)).getUserStyles()[0].featureTypeStyles();

        int i = 0;
        for (FeatureTypeStyle layer : fts) {
            // layer named function4 has the property zoom functions and there should be 2 feature
            // type styles
            // for this layer.
            if (layer.getName().equalsIgnoreCase("function4")) {
                if (i == 0) {
                    // max scale denominator should be set for zoom level 20
                    Double minScaleDenom = MBObjectStops.zoomLevelToScaleDenominator(20L);
                    Double maxScaleDenom = MBObjectStops.zoomLevelToScaleDenominator(0L);
                    assertEquals(0, i);
                    assertEquals(
                            minScaleDenom, (Double) layer.rules().get(0).getMinScaleDenominator());
                    assertEquals(
                            maxScaleDenom, (Double) layer.rules().get(0).getMaxScaleDenominator());
                } else if (i == 1) {
                    assertEquals(1, i);
                    Double maxScaleDenom = MBObjectStops.zoomLevelToScaleDenominator(20L);
                    assertEquals(
                            maxScaleDenom, (Double) layer.rules().get(0).getMaxScaleDenominator());
                }
                i++;
            }
        }
    }
}
