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
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.Rule;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class SymbolMBLayerTest {
    SymbolMBLayer testLayerDefault;
    SymbolMBLayer testLayer;
    SymbolMBLayer testLineLayer;
    SymbolMBLayer testAngleLayer;
    MBStyle defaultStyle;
    MBStyle lineStyle;
    MBStyle pointStyle;
    MBStyle fontStyle;
    List<FeatureTypeStyle> featureTypeLine;
    List<FeatureTypeStyle> featureTypePoint;
    MBStyle angleStyle;
    MBStyle style;
    List<FeatureTypeStyle> featureTypeWithAngle;
    List<FeatureTypeStyle> featureTypeDefaults;
    List<FeatureTypeStyle> featureTypeTestValues;

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonDefault = MapboxTestUtils.parseTestStyle("symbolStyleTestDefaults.json");
        JSONObject json = MapboxTestUtils.parseTestStyle("symbolStyleTest.json");
        JSONObject jsonAngle = MapboxTestUtils.parseTestStyle("symbolTextLinePlacementTest.json");
        JSONObject jsonFont = MapboxTestUtils.parseTestStyle("textFontFamilyTest.json");
        fontStyle = MBStyle.create(jsonFont);
        lineStyle = MBStyle.create(jsonAngle);
        defaultStyle = MBStyle.create(jsonDefault);
        pointStyle = MBStyle.create(json);
        testLineLayer = (SymbolMBLayer) lineStyle.layer("testid");
        testLayerDefault = (SymbolMBLayer) MBStyle.create(jsonDefault).layer("testid");
        testLayer = (SymbolMBLayer) MBStyle.create(json).layer("testid");
        featureTypePoint = testLayer.transformInternal(pointStyle);
        featureTypeLine = testLineLayer.transformInternal(lineStyle);
        style = MBStyle.create(json);
        angleStyle = MBStyle.create(jsonAngle);
        defaultStyle = MBStyle.create(jsonDefault);
        testLayer = (SymbolMBLayer) style.layer("testid");
        testAngleLayer = (SymbolMBLayer) angleStyle.layer("testid");
        testLayerDefault = (SymbolMBLayer) defaultStyle.layer("testid");
        featureTypeTestValues = testLayer.transformInternal(style);
        featureTypeWithAngle = testAngleLayer.transformInternal(angleStyle);
        featureTypeDefaults = testLayerDefault.transformInternal(defaultStyle);
    }

    @Test
    public void testTextAnchorEnum() {
        // cannot use valueOf directly
        assertEquals(TextAnchor.CENTER, TextAnchor.parse("center"));
        assertEquals(TextAnchor.BOTTOM_LEFT, TextAnchor.parse("bottom-left"));

        // json
        assertEquals("bottom-left", TextAnchor.BOTTOM_LEFT.json());

        // justification
        assertEquals(0.5, TextAnchor.getAnchorX("center"), 0.0);
        assertEquals(0.5, TextAnchor.getAnchorY("center"), 0.0);
    }

    @Test
    public void testTextMaxWidth() {
        // Test default MBStyle value
        assertEquals(
                160,
                testLayerDefault.getTextMaxWidth().intValue()
                        * testLayerDefault.getTextSize().intValue());
        // Test generated MBStyle values
        assertEquals(
                100, testLayer.getTextMaxWidth().intValue() * testLayer.getTextSize().intValue());
        // Test values in FeatureTypeStyle transform
        assertEquals(
                "100.0",
                featureTypeTestValues
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[1]
                        .getOptions()
                        .get("autoWrap"));
    }

    @Test
    public void testTextRotationDefault() {
        // Default MBStyle value
        assertEquals(0, testLayerDefault.getTextRotate().intValue());
        // Default values from FeatureTypeStyle transform
        Rule r = featureTypeDefaults.get(0).rules().get(0);
        TextSymbolizer2 symbolizer = (TextSymbolizer2) r.symbolizers().get(0);
        PointPlacement pp = (PointPlacement) symbolizer.getLabelPlacement();
        assertEquals("0.0", pp.getRotation().toString());
    }

    @Test
    public void testTextRotation() {
        // Test generated MBStyle value
        assertEquals(10, testLayer.getTextRotate().intValue());
        // Test values from FeatureTypeStyle transform
        Rule r = featureTypeTestValues.get(0).rules().get(0);
        TextSymbolizer2 symbolizer = (TextSymbolizer2) r.symbolizers().get(1);
        PointPlacement pp = (PointPlacement) symbolizer.getLabelPlacement();
        assertEquals("10", pp.getRotation().toString());
    }

    @Test
    public void testTextMaxAngle() {
        // Test json values
        assertEquals(25, testLineLayer.getTextMaxAngle().intValue());
        // now null assertEquals(45, testLayerDefault.getTextMaxAngle().intValue());
        // Test FeatureTypeStyle
        // assertEquals("45.0",
        // featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0].getOptions().get("maxAngleDelta"));
        // now null assertEquals("false",
        // featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0].getOptions().get("followLine"));
        assertEquals(
                "true",
                featureTypeLine
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[0]
                        .getOptions()
                        .get("followLine"));
    }

    @Test
    public void testTextKeepUpright() {
        assertEquals(false, testLineLayer.getTextKeepUpright());
        assertEquals(true, testLayerDefault.getTextKeepUpright());
        assertEquals(
                "false",
                featureTypeLine
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[0]
                        .getOptions()
                        .get("forceLeftToRight"));
    }

    @Test
    public void testTextPadding() {
        assertEquals(20.0, testLayer.getTextPadding());
        // defaults to 2.0
        assertEquals(2.0, testLayerDefault.getTextPadding());
        assertEquals(
                "20.0",
                featureTypePoint
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[1]
                        .getOptions()
                        .get("spaceAround"));
    }

    @Test
    public void testIconPadding() {
        assertEquals(30.0, testLayer.getIconPadding());
        // defaults to 2.0
        assertEquals(2.0, testLayerDefault.getIconPadding());
        assertEquals(
                "20.0",
                featureTypePoint
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[1]
                        .getOptions()
                        .get("spaceAround"));
        assertEquals(30.0, testLineLayer.getIconPadding());
        assertEquals(
                "15.0",
                featureTypeLine
                        .get(0)
                        .rules()
                        .get(0)
                        .getSymbolizers()[0]
                        .getOptions()
                        .get("spaceAround"));
    }

    @Test
    public void testTextFont() {
        MBLayer fontLayer = (SymbolMBLayer) fontStyle.layer("text-font");
        List<FeatureTypeStyle> featureTypeFont = fontLayer.transformInternal(fontStyle);
        assertEquals(
                true, ((JSONObject) fontLayer.getLayout().get("text-font")).containsKey("stops"));
        assertEquals(
                "Apple-Chancery",
                ((JSONArray)
                                ((JSONArray)
                                                ((JSONArray)
                                                                ((JSONObject)
                                                                                fontLayer
                                                                                        .getLayout()
                                                                                        .get(
                                                                                                "text-font"))
                                                                        .get("stops"))
                                                        .get(0))
                                        .get(1))
                        .get(0));
        assertEquals(
                "Apple-Chancery",
                ((CategorizeFunction)
                                ((TextSymbolizer)
                                                featureTypeFont
                                                        .get(0)
                                                        .rules()
                                                        .get(0)
                                                        .getSymbolizers()[0])
                                        .fonts()
                                        .get(0)
                                        .getFamily()
                                        .get(0))
                        .getParameters()
                        .get(1)
                        .toString());
        // System.out.println(
        //                ((TextSymbolizer)
        // featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0])
        //                        .fonts()
        //                        .get(0)
        //                        .getFamily());
        assertEquals(
                "Open Sans Regular",
                ((TextSymbolizer) featureTypeDefaults.get(0).rules().get(0).getSymbolizers()[0])
                        .fonts()
                        .get(0)
                        .getFamily()
                        .get(0)
                        .toString());
    }
}
