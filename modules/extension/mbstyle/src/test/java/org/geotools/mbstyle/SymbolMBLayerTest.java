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
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Font;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.mbstyle.function.FontAlternativesFunction;
import org.geotools.mbstyle.function.MapBoxFontBaseNameFunction;
import org.geotools.mbstyle.function.MapBoxFontStyleFunction;
import org.geotools.mbstyle.function.MapBoxFontWeightFunction;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.hamcrest.Matchers;
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
    MBStyle oneWayStyle;
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
        JSONObject jsonOneWay = MapboxTestUtils.parseTestStyle("symbolOneWayTest.json");
        fontStyle = MBStyle.create(jsonFont);
        lineStyle = MBStyle.create(jsonAngle);
        defaultStyle = MBStyle.create(jsonDefault);
        pointStyle = MBStyle.create(json);
        oneWayStyle = MBStyle.create(jsonOneWay);
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
        Rule r = featureTypeDefaults.get(0).rules().get(0);
        TextSymbolizer symbolizer = (TextSymbolizer) r.symbolizers().get(0);
        PointPlacement pp = (PointPlacement) symbolizer.getLabelPlacement();
        assertEquals("0.0", pp.getRotation().toString());
    }

    @Test
    public void testTextRotation() {
        // Test generated MBStyle value
        assertEquals(10, testLayer.getTextRotate().intValue());
        // Test values from FeatureTypeStyle transform
        Rule r = featureTypeTestValues.get(0).rules().get(0);
        TextSymbolizer symbolizer = (TextSymbolizer) r.symbolizers().get(0);
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
        List<FeatureTypeStyle> featureTypeFont = fontLayer.transformInternal(fontStyle);
        TextSymbolizer text =
                (TextSymbolizer) featureTypeFont.get(0).rules().get(0).symbolizers().get(0);
        Font font = text.fonts().get(0);
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
        Rule rule = featureTypeDefaults.get(0).rules().get(0);
        TextSymbolizer textSymbolizer = (TextSymbolizer) rule.symbolizers().get(0);
        Font font = textSymbolizer.fonts().get(0);

        Expression family = font.getFamily().get(0);
        assertThat(family, instanceOf(Literal.class));
        assertEquals("Sans", family.evaluate(null, String.class));
    }

    public <T> T firstParameter(Function function, Class<T> type) {
        return type.cast(function.getParameters().get(0));
    }

    @Test
    public void testIconStyle() throws Exception {
        MBLayer layer = oneWayStyle.layer("road_oneway");
        List<FeatureTypeStyle> featureTypes = layer.transformInternal(fontStyle);
        assertEquals(1, featureTypes.size());
        List<Rule> rules = featureTypes.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        TextSymbolizer ts = (TextSymbolizer) symbolizers.get(0);

        // empty icon, can align as it likes (for one ways)
        assertEquals(" ", ts.getLabel().evaluate(null, String.class));
        assertThat(ts.getLabelPlacement(), Matchers.instanceOf(LinePlacement.class));
        assertEquals("true", ts.getOptions().get(TextSymbolizer.FOLLOW_LINE_KEY));
        assertEquals("false", ts.getOptions().get(TextSymbolizer.FORCE_LEFT_TO_RIGHT_KEY));

        // tiny fixed font so that the cost of font alternative computation is not there,
        // and the symbol gets centered on the label point without vagaries related to the
        // specific font ascenders/descenders
        Font font = ts.getFont();
        // one font
        assertEquals(1, font.getFamily().size());
        assertThat(font.getFamily().get(0), Matchers.instanceOf(Literal.class));
        assertEquals("Sans", font.getFamily().get(0).evaluate(null, String.class));
        // a tiny font
        assertThat(font.getSize(), Matchers.instanceOf(Literal.class));
        assertEquals(1, (int) font.getSize().evaluate(null, Integer.class));
    }
}
