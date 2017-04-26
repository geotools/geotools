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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.styling.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *
 * Created by vickdw on 4/17/17.
 */
public class MBStopsTest {
    private StyleFactory sf;
    /**
     * This test accepts a MapBox Style JSONObject and finds all distinct zoom levels contained within.
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testGetStopLevels() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<Long> stopLevels = MBObjectStops.getStopLevels(mbStyle);

        assertEquals(9, stopLevels.size());

    }

    @Test
    public void testGetLayerStopLevels() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        MBLayer mbLayer = mbStyle.layer("function3");

        List<Long> zoomLevels = MBObjectStops.getLayerStopLevels(mbLayer);

        assertEquals(3, zoomLevels.size());

    }

    @Test
    public void testFeatureTypeStyleForLayerStops() throws IOException, ParseException {
        sf = CommonFactoryFinder.getStyleFactory();
        Style sld = sf.createStyle();
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        MBLayer mbLayer = mbStyle.layer("function2");

        List<Long> zoomLevels = MBObjectStops.getLayerStopLevels(mbLayer);
        List<long[]> ranges = MBObjectStops.getStopLevelRanges(zoomLevels);

        List<MBStyle> zoomStyles = MBObjectStops.getLayerStylesForStops(zoomLevels, mbStyle, mbLayer);



        for (MBStyle style : zoomStyles) {
            long stopLevel = MBObjectStops.getStop(style.layers().get(0));
            long[] rangeForStopLevel = MBObjectStops.getRangeForStop(stopLevel, ranges);

            // create the FeatureTypeStyles based upon the stops for a layer
            FeatureTypeStyle fts = new MBStyleTransformer().transform(style.layers().get(0), style);

            // the created FetureTypeStyle as default values set for MIN/MAX Scale Denominator
            // Edit these values to correspond to the stop level of the layer.
            Rule rule = fts.rules().get(0);
            rule.setMinScaleDenominator(MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[0]));
            if (stopLevel != rangeForStopLevel[1]) {
                rule.setMaxScaleDenominator(MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[1]));
            }

            sld.featureTypeStyles().add(fts);
        }
        assertEquals(3, sld.featureTypeStyles().size());
        assertEquals(34123.67333684649, sld.featureTypeStyles().get(0).rules().get(0).getMaxScaleDenominator(), 0.1);
        assertEquals(5.590822639508929E8, sld.featureTypeStyles().get(0).rules().get(0).getMinScaleDenominator(), 0.1);

        assertEquals(133.2955989720566, sld.featureTypeStyles().get(1).rules().get(0).getMaxScaleDenominator(), 0.1);
        assertEquals(34123.67333684649, sld.featureTypeStyles().get(1).rules().get(0).getMinScaleDenominator(), 0.1);

        assertEquals(1.1181645279017859E9, sld.featureTypeStyles().get(2).rules().get(0).getMaxScaleDenominator(), 0.1);
        assertEquals(133.2955989720566, sld.featureTypeStyles().get(2).rules().get(0).getMinScaleDenominator(), 0.1);
    }

    @Test
    public void testMBStyle() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);

        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        assertEquals("functions", sld.getName());
        UserLayerImpl userLayer = (UserLayerImpl) sld.layers().get(0);
        Style[] styles = userLayer.getUserStyles();
        assertEquals(1, styles.length);
        List<FeatureTypeStyle> fts = styles[0].featureTypeStyles();
        assertEquals(22, fts.size());
    }

    @Test
    public void testMBStylesForStops() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<Long> zoomLevels = MBObjectStops.getStopLevels(mbStyle);
        List<long[]> ranges = MBObjectStops.getStopLevelRanges(zoomLevels);

        List<MBStyle> zoomStyles = MBObjectStops.getStylesForStopLevels(zoomLevels, mbStyle);

        assertEquals(9, zoomStyles.size());

        // Zoom-and-property functions Style has been "smuched" into normal properies / function calls
        MBStyle s = zoomStyles.get(0);
        MBLayer layer = s.layers().get(5);
        JSONObject circleRadius = (JSONObject) layer.getPaint().get("circle-radius");
        JSONArray stopsArray = (JSONArray) circleRadius.get("stops");

        assertEquals("rating", circleRadius.get("property"));
        assertEquals(2, stopsArray.size());
        assertFalse(stopsArray.get(0) instanceof JSONObject);
    }

}
