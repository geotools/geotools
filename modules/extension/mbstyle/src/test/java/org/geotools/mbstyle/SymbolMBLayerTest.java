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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.mbstyle.function.FontAlternativesFunction;
import org.geotools.mbstyle.function.MapBoxFontBaseNameFunction;
import org.geotools.mbstyle.function.MapBoxFontStyleFunction;
import org.geotools.mbstyle.function.MapBoxFontWeightFunction;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.geotools.styling.*;
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
    List<FeatureTypeStyleImpl> featureTypeLine;
    List<FeatureTypeStyleImpl> featureTypePoint;
    MBStyle angleStyle;
    MBStyle style;
    List<FeatureTypeStyleImpl> featureTypeWithAngle;
    List<FeatureTypeStyleImpl> featureTypeDefaults;
    List<FeatureTypeStyleImpl> featureTypeTestValues;

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
                        .symbolizers()
                        .get(0)
                        .getOptions()
                        .get("autoWrap"));
    }

    @Test
    public void testTextRotationDefault() {
        // Default MBStyle value
        assertEquals(0, testLayerDefault.getTextRotate().intValue());
        // Default values from FeatureTypeStyle transform
        RuleImpl r = (RuleImpl) featureTypeDefaults.get(0).rules().get(0);
        TextSymbolizerImpl symbolizer = (TextSymbolizerImpl) r.symbolizers().get(0);
        PointPlacementImpl pp = (PointPlacementImpl) symbolizer.getLabelPlacement();
        assertEquals("0.0", pp.getRotation().toString());
    }

    @Test
    public void testTextRotation() {
        // Test generated MBStyle value
        assertEquals(10, testLayer.getTextRotate().intValue());
        // Test values from FeatureTypeStyle transform
        RuleImpl r = (RuleImpl) featureTypeTestValues.get(0).rules().get(0);
        TextSymbolizerImpl symbolizer = (TextSymbolizerImpl) r.symbolizers().get(0);
        PointPlacementImpl pp = (PointPlacementImpl) symbolizer.getLabelPlacement();
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
                        .symbolizers()
                        .get(0)
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
                        .symbolizers()
                        .get(0)
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
                        .symbolizers()
                        .get(0)
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
                        .symbolizers()
                        .get(0)
                        .getOptions()
                        .get("spaceAround"));
        assertEquals(30.0, testLineLayer.getIconPadding());
        assertEquals(
                "15.0",
                featureTypeLine
                        .get(0)
                        .rules()
                        .get(0)
                        .symbolizers()
                        .get(0)
                        .getOptions()
                        .get("spaceAround"));
    }

    @Test
    public void testTextFont() {
        // check the json
        MBLayer fontLayer = fontStyle.layer("text-font");
        assertTrue(((JSONObject) fontLayer.getLayout().get("text-font")).containsKey("stops"));
        JSONArray stops =
                (JSONArray) ((JSONObject) fontLayer.getLayout().get("text-font")).get("stops");
        assertEquals("Apple-Chancery", ((JSONArray) ((JSONArray) stops.get(0)).get(1)).get(0));

        // check the SLD
        List<FeatureTypeStyleImpl> featureTypeFont = fontLayer.transformInternal(fontStyle);
        TextSymbolizerImpl text =
                (TextSymbolizerImpl) featureTypeFont.get(0).rules().get(0).symbolizers().get(0);
        FontImpl font = text.fonts().get(0);
        Expression family = font.getFamily().get(0);
        assertThat(family, instanceOf(FontAlternativesFunction.class));
        FontAlternativesFunction fontAlternatives = (FontAlternativesFunction) family;
        assertThat(
                firstParameter(fontAlternatives, MapBoxFontBaseNameFunction.class),
                instanceOf(MapBoxFontBaseNameFunction.class));
        MapBoxFontBaseNameFunction baseNameFunction =
                firstParameter(fontAlternatives, MapBoxFontBaseNameFunction.class);
        assertEquals(
                "Apple-Chancery",
                firstParameter(baseNameFunction, CategorizeFunction.class)
                        .getParameters()
                        .get(1)
                        .toString());
        // weight
        Expression weight = font.getWeight();
        assertThat(weight, instanceOf(MapBoxFontWeightFunction.class));
        MapBoxFontWeightFunction fontWeight = (MapBoxFontWeightFunction) weight;
        assertEquals(
                "Apple-Chancery",
                firstParameter(fontWeight, CategorizeFunction.class)
                        .getParameters()
                        .get(1)
                        .toString());
        // style
        Expression style = font.getStyle();
        assertThat(style, instanceOf(MapBoxFontStyleFunction.class));

        assertEquals(
                "Apple-Chancery",
                firstParameter(fontWeight, CategorizeFunction.class)
                        .getParameters()
                        .get(1)
                        .toString());
    }

    @Test
    public void testTextFeatureTypeDefaults() {
        RuleImpl rule = (RuleImpl) featureTypeDefaults.get(0).rules().get(0);
        TextSymbolizerImpl textSymbolizer = (TextSymbolizerImpl) rule.symbolizers().get(0);
        FontImpl font = textSymbolizer.fonts().get(0);

        Expression family = font.getFamily().get(0);
        assertThat(family, instanceOf(FontAlternativesFunction.class));
        FontAlternativesFunction fontAlternatives = (FontAlternativesFunction) family;
        assertThat(firstParameter(fontAlternatives, Literal.class), instanceOf(Literal.class));
        assertEquals("Open Sans", firstParameter(fontAlternatives, Literal.class).toString());
    }

    public <T> T firstParameter(Function function, Class<T> type) {
        return type.cast(function.getParameters().get(0));
    }
}
