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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.geotools.TestData;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.FilterFunction_isometric;
import org.geotools.filter.function.FilterFunction_offset;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.function.FontAlternativesFunction;
import org.geotools.mbstyle.function.MapBoxFontBaseNameFunction;
import org.geotools.mbstyle.layer.BackgroundMBLayer;
import org.geotools.mbstyle.layer.CircleMBLayer;
import org.geotools.mbstyle.layer.FillExtrusionMBLayer;
import org.geotools.mbstyle.layer.FillMBLayer;
import org.geotools.mbstyle.layer.LineMBLayer;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.layer.RasterMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.renderer.lite.LabelShieldTest;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.renderer.style.FontCache;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.SLD;
import org.hamcrest.CoreMatchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/** Test parsing and transforming a Mapbox fill layer from json. */
public class StyleTransformTest {

    static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    Map<String, JSONObject> testLayersById = new HashMap<>();

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        JSONArray layers = (JSONArray) jsonObject.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
    }

    /** Test parsing a Mapbox fill layer */
    @Test
    public void testFill() throws IOException, ParseException {

        JSONObject jsonObject = parseTestStyle("fillStyleTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillMBLayer);
        FillMBLayer mbFill = (FillMBLayer) layers.get(0);
        List<FeatureTypeStyle> fts = mbFill.transform(mbStyle);

        PolygonSymbolizer psym = SLD.polySymbolizer(fts.get(0));

        Expression expr = psym.getFill().getColor();
        assertNotNull("fillColor set", expr);
        assertEquals(Color.decode("#FF595E"), expr.evaluate(null, Color.class));
        assertEquals(Double.valueOf(.84), psym.getFill().getOpacity().evaluate(null, Double.class));

        Expression colorStroke = psym.getStroke().getColor();
        assertNotNull("stroke color set", colorStroke);
        assertEquals(Color.decode("#1982C4"), colorStroke.evaluate(null, Color.class));

        assertNotNull("displacement not null", psym.getDisplacement());
        assertNotNull("displacementX not null", psym.getDisplacement().getDisplacementX());
        assertNotNull("displacementY not null", psym.getDisplacement().getDisplacementY());
        assertEquals(
                Integer.valueOf(20), psym.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(20), psym.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
    }

    /** Test parsing and generating a MapBox fill extrusion */
    @Test
    public void testFillExtrusion() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("fillExtrusionTest.json");
        MBObjectParser parse = new MBObjectParser(StyleTransformTest.class);

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers();

        assertEquals(2, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(1) instanceof FillExtrusionMBLayer);
        FillExtrusionMBLayer mbFill = (FillExtrusionMBLayer) layers.get(1);
        List<FeatureTypeStyle> fts = mbFill.transform(mbStyle);
        assertEquals(3, fts.size());

        PolygonSymbolizer psym = SLD.polySymbolizer(fts.get(0));
        PolygonSymbolizer sides = SLD.polySymbolizer(fts.get(1));
        PolygonSymbolizer roof = SLD.polySymbolizer(fts.get(2));

        Expression shadowGeometry = psym.getGeometry();
        assertEquals("offset", ((FilterFunction_offset) shadowGeometry).getName());
        assertEquals(3, ((FilterFunction_offset) shadowGeometry).getParameters().size());

        Expression roofGeometry = roof.getGeometry();
        assertEquals("offset", ((FilterFunction_offset) roofGeometry).getName());
        assertEquals(3, ((FilterFunction_offset) roofGeometry).getParameters().size());
        //        assertEquals("the_geom", ((FilterFunction_offset)
        // roofGeometry).getParameters().get(0).toString());
        assertEquals(
                "0",
                ((FilterFunction_offset) roofGeometry).getParameters().get(1).toString());
        assertEquals(
                "Height",
                ((FilterFunction_offset) roofGeometry).getParameters().get(2).toString());

        Expression sidesGeometry = sides.getGeometry();
        assertEquals("isometric", ((FilterFunction_isometric) sidesGeometry).getName());
        assertEquals(
                2, ((FilterFunction_isometric) sidesGeometry).getParameters().size());
        //        assertEquals("the_geom", ((FilterFunction_isometric)
        // sidesGeometry).getParameters().get(0).toString());
        assertEquals(
                "Height",
                ((FilterFunction_isometric) sidesGeometry)
                        .getParameters()
                        .get(1)
                        .toString());

        Expression expr = psym.getFill().getColor();
        assertNotNull("fillColor set", expr);
        assertEquals(parse.convertToColor("#aaa"), expr.evaluate(null, Color.class));
        assertEquals(Double.valueOf(0.6), psym.getFill().getOpacity().evaluate(null, Double.class));
    }

    /** Test parsing a Mapbox fill layer using a sprite fill-pattern */
    @Test
    public void testFillSprite() throws IOException, ParseException {

        JSONObject jsonObject = parseTestStyle("fillStyleSpriteTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");

        assertEquals(1, layers.size());

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.
        assertTrue(layers.get(0) instanceof FillMBLayer);
        FillMBLayer mbFill = (FillMBLayer) layers.get(0);
        List<FeatureTypeStyle> fts = mbFill.transform(mbStyle);

        PolygonSymbolizer psym = SLD.polySymbolizer(fts.get(0));
        Graphic g = psym.getFill().getGraphicFill();
        assertNotNull(g);
        assertNotNull(g.graphicalSymbols());
        assertEquals(1, g.graphicalSymbols().size());
    }

    /** Test parsing a Mapbox raster layer */
    @Test
    public void testRaster() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("rasterStyleTest.json");

        // Find the MBFillLayer and assert it contains the correct FeatureTypeStyle.

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-raster");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof RasterMBLayer);
        RasterMBLayer mbFill = (RasterMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbFill.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof RasterSymbolizer);
        RasterSymbolizer rsym = (RasterSymbolizer) symbolizer;

        assertEquals(Double.valueOf(.59), rsym.getOpacity().evaluate(null, Double.class));
    }

    /** Test parsing a Mapbox line layer */
    @Test
    public void testLine() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("lineStyleTest.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbLine.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;

        assertEquals(new Color(0, 153, 255), lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(4), lsym.getPerpendicularOffset().evaluate(null, Integer.class));

        List<Integer> expectedDashes = Arrays.asList(50, 50); // 5 times 10, the line width
        assertEquals(expectedDashes.size(), lsym.getStroke().dashArray().size());
        for (int i = 0; i < expectedDashes.size(); i++) {
            Integer n = lsym.getStroke().dashArray().get(i).evaluate(null, Integer.class);
            assertEquals(expectedDashes.get(i), n);
        }
    }

    @Test
    public void testLineGapWidth() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("lineStyleGapTest.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbLine.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(2, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof LineSymbolizer);
        LineSymbolizer lsym = (LineSymbolizer) symbolizer;

        assertEquals(new Color(0, 153, 255), lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(-5), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
    }

    @Test
    public void testLineGapStopWidth() throws IOException, ParseException, TransformerException, CQLException {
        JSONObject jsonObject = parseTestStyle("lineStyleGapStopsTest.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        assertThat(layers.get(0), CoreMatchers.instanceOf(LineMBLayer.class));

        StyledLayerDescriptor sld = mbStyle.transform();
        List<FeatureTypeStyle> fts = MapboxTestUtils.getStyle(sld, 0).featureTypeStyles();
        assertEquals(fts.size(), 1);

        // first and only fts
        assertEquals(1, fts.get(0).rules().size());
        Rule r0 = fts.get(0).rules().get(0);
        assertEquals(2, r0.symbolizers().size());
        LineSymbolizer s_0_0 = (LineSymbolizer) r0.symbolizers().get(0);
        assertEquals(Integer.valueOf(10), s_0_0.getStroke().getWidth().evaluate(null, Integer.class));
        Expression exected_po_0_0 =
                CQL.toExpression("8 - (Interpolate(zoomLevel(env('wms_scale_denominator'), 'EPSG:3857'), 12, 0, 20, 6, "
                        + "'numeric') + 10) / 2");
        assertEquals(exected_po_0_0, s_0_0.getPerpendicularOffset());

        LineSymbolizer s_0_1 = (LineSymbolizer) r0.symbolizers().get(1);
        assertEquals(Integer.valueOf(10), s_0_1.getStroke().getWidth().evaluate(null, Integer.class));
        Expression exected_po_0_1 =
                CQL.toExpression("8 + (Interpolate(zoomLevel(env('wms_scale_denominator'), 'EPSG:3857'), 12, 0, 20, 6, "
                        + "'numeric') + 10) / 2");
        assertEquals(exected_po_0_1, s_0_1.getPerpendicularOffset());
    }

    @Test
    public void testLineDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("lineStyleTestEmpty.json");

        // Find the LineMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof LineMBLayer);
        LineMBLayer mbLine = (LineMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbLine.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

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
        assertNull(lsym.getStroke().dashArray());
    }

    @Test
    public void testCircle() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("circleStyleTest.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof CircleMBLayer);
        CircleMBLayer mbCircle = (CircleMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbCircle.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PointSymbolizer);
        PointSymbolizer psym = (PointSymbolizer) symbolizer;

        assertNotNull(psym.getGraphic());

        assertEquals(Integer.valueOf(30), psym.getGraphic().getSize().evaluate(null, Integer.class));

        assertNotNull(psym.getGraphic().getDisplacement());
        assertEquals(
                Integer.valueOf(10),
                psym.getGraphic().getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(10),
                psym.getGraphic().getDisplacement().getDisplacementY().evaluate(null, Integer.class));

        assertEquals(1, psym.getGraphic().graphicalSymbols().size());

        GraphicalSymbol gs = psym.getGraphic().graphicalSymbols().get(0);
        assertTrue(gs instanceof Mark);
        Mark m = (Mark) gs;

        assertNotNull(m.getFill());
        assertEquals(Color.RED, m.getFill().getColor().evaluate(null, Color.class));
        assertEquals(.5, m.getFill().getOpacity().evaluate(null, Double.class), .0001);

        assertNotNull(m.getStroke());
        assertEquals(Color.GREEN, m.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(2), m.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(.75, m.getStroke().getOpacity().evaluate(null, Double.class), .0001);
    }

    @Test
    public void testCircleDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("circleStyleTestDefaults.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof CircleMBLayer);
        CircleMBLayer mbCircle = (CircleMBLayer) layers.get(0);

        List<FeatureTypeStyle> fts = mbCircle.transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PointSymbolizer);
        PointSymbolizer psym = (PointSymbolizer) symbolizer;

        assertNotNull(psym.getGraphic());

        assertEquals(Integer.valueOf(10), psym.getGraphic().getSize().evaluate(null, Integer.class));

        if (psym.getGraphic().getDisplacement() != null) {
            assertEquals(
                    Integer.valueOf(0),
                    psym.getGraphic().getDisplacement().getDisplacementX().evaluate(null, Integer.class));
            assertEquals(
                    Integer.valueOf(0),
                    psym.getGraphic().getDisplacement().getDisplacementY().evaluate(null, Integer.class));
        }

        assertEquals(1, psym.getGraphic().graphicalSymbols().size());

        GraphicalSymbol gs = psym.getGraphic().graphicalSymbols().get(0);
        assertTrue(gs instanceof Mark);
        Mark m = (Mark) gs;

        assertNotNull(m.getFill());
        assertEquals(Color.BLACK, m.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(1), m.getFill().getOpacity().evaluate(null, Integer.class));

        assertNotNull(m.getStroke());
        assertEquals(Color.BLACK, m.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(0), m.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(1), m.getStroke().getOpacity().evaluate(null, Integer.class));
    }

    @Test
    public void testBackgroundColor() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundColorStyleTest.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        Fill fill = ((BackgroundMBLayer) layers.get(0)).getFill(mbStyle);

        assertEquals(Color.GREEN, fill.getColor().evaluate(null, Color.class));
        assertEquals(.45, fill.getOpacity().evaluate(null, Double.class), .0001);
    }

    @Test
    public void testBackgroundDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundStyleTestDefault.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        Fill fill = ((BackgroundMBLayer) layers.get(0)).getFill(mbStyle);

        assertEquals(Color.BLACK, fill.getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(1), fill.getOpacity().evaluate(null, Integer.class));
    }

    @Test
    public void testBackgroundPattern() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundImgStyleTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("geoserver-states");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        Fill fill = ((BackgroundMBLayer) layers.get(0)).getFill(mbStyle);
        assertEquals(Color.BLUE, fill.getColor().evaluate(null, Color.class));
        assertEquals(1, fill.getGraphicFill().graphicalSymbols().size());
        assertEquals(Double.valueOf(0.75), fill.getOpacity().evaluate(null, Double.class));
    }

    @Test
    public void testSymbolFont() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolTextTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof SymbolMBLayer);
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof TextSymbolizer);
        TextSymbolizer tsym = (TextSymbolizer) symbolizer;

        assertEquals(2, tsym.fonts().size());
        List<Expression> family0 = tsym.fonts().get(0).getFamily();
        assertEquals(1, family0.size());
        List<Expression> family1 = tsym.fonts().get(1).getFamily();
        assertEquals(1, family1.size());

        assertEquals(ff.function("fontAlternatives", ff.literal("Bitstream Vera Sans")), family0.get(0));
        assertEquals(ff.function("fontAlternatives", ff.literal("Other Test Font")), family1.get(0));
    }

    @Test
    public void testSymbolTextAndIcon() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolTextAndIconTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        // only one symbolizer
        List<Symbolizer> symbolizers = r.symbolizers();
        assertEquals(1, symbolizers.size());
        TextSymbolizer ts = (TextSymbolizer) symbolizers.get(0);
        assertEquals("false", ts.getOptions().get("partials"));
        assertEquals("INDEPENDENT", ts.getOptions().get(org.geotools.api.style.TextSymbolizer.GRAPHIC_PLACEMENT_KEY));
        assertEquals("false", ts.getOptions().get(org.geotools.api.style.PointSymbolizer.FALLBACK_ON_DEFAULT_MARK));
        assertNotNull(ts.getGraphic());
        // text-offset is [0,0] here, so the label keeps the center anchor from text-anchor
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointX().evaluate(null, Double.class), 0d);
    }

    /** Without variable anchors the label keeps its text-anchor and only moves by the text-offset, as in Mapbox. */
    @Test
    public void testSymbolIconLabelKeepsTextAnchor() throws Exception {
        TextSymbolizer ts = symbolFromLayout(
                """
                "symbol-placement": "point",
                "icon-image": "foo",
                "text-field": "{name}",
                "text-offset": [1.1, 0]
                """);
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        assertEquals(0.5, pp.getAnchorPoint().getAnchorPointX().evaluate(null, Double.class), 0d);
    }

    /**
     * text-variable-anchor ["left", "right"] becomes the displacementMode search E,W, with a maxDisplacement just above
     * the offset length so the renderer runs the search.
     */
    @Test
    public void testSymbolVariableAnchorDisplacementMode() throws Exception {
        TextSymbolizer ts = symbolFromLayout(
                """
                "symbol-placement": "point",
                "icon-image": "foo",
                "text-field": "{name}",
                "text-size": 14,
                "text-offset": [1.1, 0],
                "text-variable-anchor": ["left", "right"]
                """);
        assertEquals("E,W", ts.getOptions().get(TextSymbolizer.DISPLACEMENT_MODE_KEY));
        // the offset length is 14 * 1.1 + 2 (text padding) = 17.4, rounded up plus one
        int maxDisplacement = ExpressionExtractor.extractCqlExpressions(
                        ts.getOptions().get(TextSymbolizer.MAX_DISPLACEMENT_KEY))
                .evaluate(null, Integer.class);
        assertEquals(19, maxDisplacement);
    }

    /** Each of the eight anchors maps to its own direction; "center" and repeated anchors are dropped. */
    @Test
    public void testSymbolVariableAnchorAllSides() throws Exception {
        TextSymbolizer ts = symbolFromLayout(
                """
                "symbol-placement": "point",
                "text-field": "{name}",
                "text-variable-anchor": ["left", "right", "top", "bottom", "top-left", "top-right",
                    "bottom-left", "bottom-right", "center", "left"]
                """);
        assertEquals("E,W,S,N,SE,SW,NE,NW", ts.getOptions().get(TextSymbolizer.DISPLACEMENT_MODE_KEY));
    }

    /**
     * A zoom step of variable anchors becomes the union of the anchors of all stops, since displacementMode cannot
     * follow the zoom.
     */
    @Test
    public void testSymbolVariableAnchorZoomExpression() throws Exception {
        TextSymbolizer ts = symbolFromLayout(
                """
                "symbol-placement": "point",
                "icon-image": "foo",
                "text-field": "{name}",
                "text-offset": [1.1, 0],
                "text-variable-anchor": [
                  "step",
                  ["zoom"],
                  ["literal", ["bottom", "left", "right", "top"]],
                  8,
                  ["literal", ["center"]]
                ]
                """);
        assertEquals("N,E,W,S", ts.getOptions().get(TextSymbolizer.DISPLACEMENT_MODE_KEY));
        // the offset length is 16 (default text size) * 1.1 + 2 (text padding) = 19.6, rounded up plus one
        int maxDisplacement = ExpressionExtractor.extractCqlExpressions(
                        ts.getOptions().get(TextSymbolizer.MAX_DISPLACEMENT_KEY))
                .evaluate(null, Integer.class);
        assertEquals(21, maxDisplacement);
    }

    /** Without text-variable-anchor, no displacementMode or maxDisplacement option is added. */
    @Test
    public void testSymbolNoVariableAnchorNoDisplacementMode() throws Exception {
        TextSymbolizer ts = symbolFromLayout(
                """
                "symbol-placement": "point",
                "icon-image": "foo",
                "text-field": "{name}",
                "text-offset": [1.1, 0]
                """);
        assertNull(ts.getOptions().get(TextSymbolizer.DISPLACEMENT_MODE_KEY));
        assertNull(ts.getOptions().get(TextSymbolizer.MAX_DISPLACEMENT_KEY));
    }

    @Test
    public void testSymbolAvoidEdgesNotSupplied() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolTextAndIconTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertEquals("false", symbolizer.getOptions().get("partials"));
    }

    @Test
    public void testSymbolAvoidEdgesFalse() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(false, layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertEquals("false", symbolizer.getOptions().get("partials"));
    }

    @Test
    public void testSymbolAvoidEdgesTrue() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTrueTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertEquals("false", symbolizer.getOptions().get("partials"));
    }

    @Test
    public void testSymbolIconIgnorePlacementNotProvided() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("icon-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        // no way to have only partial conflict resolution atm
        assertEquals(
                "true", symbolizer.getOptions().get(org.geotools.api.style.TextSymbolizer.CONFLICT_RESOLUTION_KEY));
    }

    @Test
    public void testSymbolIconIgnorePlacementTrue() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("icon-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        // no way to have only partial conflict resolution right now
        assertEquals(
                "true", symbolizer.getOptions().get(org.geotools.api.style.TextSymbolizer.CONFLICT_RESOLUTION_KEY));
    }

    @Test
    public void testSymbolTextIgnorePlacementNotProvided() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest2.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("text-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        TextSymbolizer symbolizer = (TextSymbolizer) r.symbolizers().get(0);
        assertEquals(
                "true", symbolizer.getOptions().get(org.geotools.api.style.TextSymbolizer.CONFLICT_RESOLUTION_KEY));
    }

    @Test
    public void testSymbolTextIgnorePlacementTrue() throws IOException, ParseException, TransformerException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest3.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("text-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("testsource");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertNull("true", symbolizer.getOptions().get("labelObstacle"));
    }

    /** Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}. */
    private JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        return MapboxTestUtils.parseTestStyle(filename);
    }

    @Test
    public void testMapboxTokenValues() throws Exception {

        File property =
                new File(TestData.getResource(this, "testpoints.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        ContentFeatureSource pointFS = ds.getFeatureSource("testpoints");

        MBStyleTransformer transformer = new MBStyleTransformer(new MBObjectParser(SymbolMBLayer.class));
        Expression e = transformer.cqlExpressionFromTokens("Replace text here: \"{text}\"");

        try (SimpleFeatureIterator sfi = pointFS.getFeatures().features()) {
            while (sfi.hasNext()) {
                SimpleFeature sf = sfi.next();
                assertNotNull(e.evaluate(sf, String.class));
            }
        }
    }

    @Test
    public void testTextFontStops() throws Exception {
        JSONObject jsonObject = parseTestStyle("textFontTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);

        List<MBLayer> layers = mbStyle.layers();
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof SymbolMBLayer);
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof TextSymbolizer);
        TextSymbolizer tsym = (TextSymbolizer) symbolizer;

        // the exact face first, then the base family
        assertEquals(2, tsym.fonts().size());
        assertEquals(1, tsym.fonts().get(1).getFamily().size());

        FontAlternativesFunction family =
                (FontAlternativesFunction) tsym.fonts().get(1).getFamily().get(0);
        MapBoxFontBaseNameFunction familyBaseName =
                (MapBoxFontBaseNameFunction) family.getParameters().get(0);
        assertEquals(
                "Apple-Chancery",
                ((CategorizeFunction) familyBaseName.getParameters().get(0))
                        .getParameters()
                        .get(1)
                        .toString());

        //        StyledLayerDescriptor sld = mbStyle.transform();
        //        SLDTransformer styleTransform = new SLDTransformer();
        //        String xml = styleTransform.transform(sld);
        //        System.out.print(xml);

    }

    @Test
    public void testTextAnchorStops() throws Exception {
        JSONObject jsonObject = parseTestStyle("symbolTextAnchorStopsTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        Style style = MapboxTestUtils.getStyle(sld, 0);

        assertEquals(1, style.featureTypeStyles().size());

        FeatureTypeStyle ft = style.featureTypeStyles().get(0);
        TextSymbolizer ts = (TextSymbolizer) ft.rules().get(0).symbolizers().get(0);
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        org.geotools.api.style.AnchorPoint ap = pp.getAnchorPoint();
        assertEquals(
                ECQL.toExpression(
                        "mbAnchor(Categorize(zoomLevel(env('wms_scale_denominator'), 'EPSG:3857'), 'left', 0, 'left', 8, 'center', 'succeeding'), 'x')"),
                ap.getAnchorPointX());
        assertEquals(
                ECQL.toExpression(
                        "mbAnchor(Categorize(zoomLevel(env('wms_scale_denominator'), 'EPSG:3857'), 'left', 0, 'left', 8, 'center', 'succeeding'), 'y')"),
                ap.getAnchorPointY());
    }

    @Test
    public void testHaloDefaultColorTest() throws Exception {
        JSONObject jsonObject = parseTestStyle("labelHaloNoColorTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        Style style = MapboxTestUtils.getStyle(sld, 0);
        TextSymbolizer ts = (TextSymbolizer)
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        // no halo, the halo color was not specified and it defaults to "fully transparent"
        assertNull(ts.getHalo());
    }

    @Test
    public void testTextOffsetEmsLinePlacement() throws Exception {
        JSONObject jsonObject = parseTestStyle("symbolStyleSimpleIconAndTextLinePlacementTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        Style style = MapboxTestUtils.getStyle(sld, 0);
        TextSymbolizer ts = (TextSymbolizer)
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        LinePlacement lp = (LinePlacement) ts.getLabelPlacement();
        // text-offset: [0, 0.5] as ems, with text-size set to 20, and opposite y direction
        assertEquals(-10, lp.getPerpendicularOffset().evaluate(null, Double.class), 0d);
        // check also the other vendor options
        assertThat(
                ts.getOptions(),
                allOf(
                        hasEntry("followLine", "true"),
                        hasEntry("maxAngleDelta", "45.0"),
                        hasEntry("group", "true"),
                        hasEntry("labelAllGroup", "true"),
                        hasEntry("forceLeftToRight", "true")));
    }

    @Test
    public void testTextOffsetEmsPointPlacement() throws Exception {
        JSONObject jsonObject = parseTestStyle("symbolStyleSimpleIconAndTextPointPlacementOffsetTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        Style style = MapboxTestUtils.getStyle(sld, 0);
        TextSymbolizer ts = (TextSymbolizer)
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        // text-offset: [1, 0.5] as ems, with text-size set to 12, and opposite y direction
        assertEquals(12, pp.getDisplacement().getDisplacementX().evaluate(null, Double.class), 0d);
        assertEquals(-6, pp.getDisplacement().getDisplacementY().evaluate(null, Double.class), 0d);
    }

    @Test
    public void testSymbolPriorityTest() throws Exception {
        JSONObject jsonObject = parseTestStyle("symbolPriorityTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        Style style = MapboxTestUtils.getStyle(sld, 0);
        List<FeatureTypeStyle> featureTypeStyles = style.featureTypeStyles();
        TextSymbolizer ts0 = getFirstSymbolizer(featureTypeStyles.get(0));
        assertEquals(1000, (int) ts0.getPriority().evaluate(null, Integer.class));
        TextSymbolizer ts1 = getFirstSymbolizer(featureTypeStyles.get(1));
        assertEquals(2000, (int) ts1.getPriority().evaluate(null, Integer.class));
        TextSymbolizer ts2 = getFirstSymbolizer(featureTypeStyles.get(2));
        assertEquals(3000, (int) ts2.getPriority().evaluate(null, Integer.class));
        TextSymbolizer ts3 = getFirstSymbolizer(featureTypeStyles.get(3));
        assertEquals(4000, (int) ts3.getPriority().evaluate(null, Integer.class));
    }

    @SuppressWarnings("unchecked")
    private <T extends Symbolizer> T getFirstSymbolizer(FeatureTypeStyle fts) {
        return (T) fts.rules().get(0).symbolizers().get(0);
    }

    /** Parses a single inline layer through the full json -> SLD path and returns its feature type styles. */
    private List<FeatureTypeStyle> transformInline(String layer) throws ParseException {
        MBStyle mbStyle = new MBStyle(MapboxTestUtils.object(MapboxTestUtils.style(layer)));
        return mbStyle.layers().get(0).transform(mbStyle);
    }

    /** Transforms a single point symbol layer with the given layout body and returns its (only) TextSymbolizer. */
    private TextSymbolizer symbolFromLayout(String layoutBody) throws ParseException {
        String layer =
                """
                {
                  "id": "poi",
                  "type": "symbol",
                  "source": "src",
                  "source-layer": "x",
                  "layout": { %s }
                }"""
                        .formatted(layoutBody);
        MBStyle mbStyle = new MBStyle(MapboxTestUtils.object(MapboxTestUtils.style(layer)));
        List<FeatureTypeStyle> fts = mbStyle.layers("src").get(0).transform(mbStyle);
        return (TextSymbolizer) fts.get(0).rules().get(0).symbolizers().get(0);
    }

    @Test
    public void testInterpolateTextSizeEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "symbol",
                  "source": "src",
                  "layout": {
                    "text-field": "{name}",
                    "text-size": ["interpolate", ["linear"], ["zoom"], 5, 10, 10, 20]
                  }
                }""");
        TextSymbolizer ts = getFirstSymbolizer(fts.get(0));
        Expression size = ts.getFont().getSize();
        // zoom 7.5 sits halfway between the 5 and 10 stops
        EnvFunction.setLocalValue("wms_scale_denominator", 1544261.172187233);
        try {
            assertEquals(15.0, size.evaluate(null, Double.class), 1e-6);
        } finally {
            EnvFunction.clearLocalValues();
        }
    }

    @Test
    public void testDataDrivenFontEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "symbol",
                  "source": "src",
                  "layout": {
                    "text-field": "{name}",
                    "text-font": [
                      "case",
                      ["<=", ["get", "min_zoom"], 5],
                      ["literal", ["Noto Sans Medium"]],
                      ["literal", ["Noto Sans Regular"]]
                    ]
                  }
                }""");
        TextSymbolizer ts = getFirstSymbolizer(fts.get(0));
        assertEquals(2, ts.fonts().size());
        Function exactFace = (Function) ts.fonts().get(0).getFamily().get(0);
        Function family = (Function) ts.fonts().get(1).getFamily().get(0);
        Expression baseName = family.getParameters().get(0);
        Expression fontName = ((Function) baseName).getParameters().get(0);
        SimpleFeature important = place(3);
        SimpleFeature minor = place(7);
        assertEquals("Noto Sans Medium", fontName.evaluate(important, String.class));
        assertEquals("Noto Sans Regular", fontName.evaluate(minor, String.class));
        assertEquals("Noto Sans", baseName.evaluate(important, String.class));
        assertEquals("Noto Sans", baseName.evaluate(minor, String.class));
        assertEquals("Noto Sans Medium", exactFace.getParameters().get(0).evaluate(important, String.class));
    }

    /** A face name with a weight renders with that exact face when it is installed. */
    @Test
    public void testExactFontFace() throws Exception {
        registerFont(StyleTransformTest.class, "DejaVuSans-ExtraLight.ttf");
        Font font = firstLabelFont("DejaVu Sans ExtraLight");
        assertEquals("DejaVu Sans ExtraLight", font.getName());
        assertFalse(font.isBold());
    }

    /** A heavy face that is not installed falls back to the bold base family. */
    @Test
    public void testMissingHeavyFontFace() throws Exception {
        registerFont(LabelShieldTest.class, "Vera.ttf");
        Font font = firstLabelFont("Bitstream Vera Sans Black");
        assertEquals("Bitstream Vera Sans", font.getName());
        assertTrue(font.isBold());
    }

    private static void registerFont(Class<?> base, String fileName) throws Exception {
        try (InputStream is = TestData.getResource(base, fileName).openStream()) {
            // drops font lists cached by earlier tests, which miss the font registered here
            FontCache.getDefaultInstance().resetCache();
            FontCache.getDefaultInstance().registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
        }
    }

    /** Returns the first Java font the renderer picks for a label using the given text-font. */
    private Font firstLabelFont(String textFont) throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "symbol",
                  "source": "src",
                  "layout": {"text-field": "{kind}", "text-font": ["%s"]}
                }"""
                        .formatted(textFont));
        TextSymbolizer ts = getFirstSymbolizer(fts.get(0));
        TextStyle2D style = (TextStyle2D) new SLDStyleFactory().createStyle(poi("shop", "1"), ts);
        return style.getFonts()[0];
    }

    private SimpleFeature place(int minZoom) throws Exception {
        SimpleFeatureType type = DataUtilities.createType("places", "min_zoom:int");
        return SimpleFeatureBuilder.build(type, new Object[] {minZoom}, "places.1");
    }

    private SimpleFeature poi(String kind, String ref) throws Exception {
        SimpleFeatureType type = DataUtilities.createType("e2e", "kind:String,ref:String,geom:Point");
        return SimpleFeatureBuilder.build(
                type, new Object[] {kind, ref, new GeometryFactory().createPoint(new Coordinate(0, 0))}, "e2e.1");
    }

    @Test
    public void testFormatTextFieldEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "symbol",
                  "source": "src",
                  "layout": {
                    "text-font": ["Noto Sans Regular"],
                    "text-field": [
                      "format",
                      ["get", "kind"],
                      {},
                      "\\n",
                      {},
                      ["get", "ref"],
                      {}
                    ]
                  }
                }""");
        TextSymbolizer ts = getFirstSymbolizer(fts.get(0));
        assertEquals("park\nA1", ts.getLabel().evaluate(poi("park", "A1"), String.class));
    }

    @Test
    public void testInFilterEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "fill",
                  "source": "src",
                  "filter": ["in", ["get", "kind"], ["literal", ["park", "wood"]]],
                  "paint": { "fill-color": "#ff0000" }
                }""");
        Filter filter = fts.get(0).rules().get(0).getFilter();
        assertTrue(filter.evaluate(poi("park", null)));
        assertFalse(filter.evaluate(poi("street", null)));
    }

    @Test
    public void testNotFilterEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "fill",
                  "source": "src",
                  "filter": ["!", ["has", "ref"]],
                  "paint": { "fill-color": "#ff0000" }
                }""");
        assertEquals(1, fts.size());
        assertEquals(1, fts.get(0).rules().size());
        Filter filter = fts.get(0).rules().get(0).getFilter();
        assertTrue(filter.evaluate(poi("park", null)));
        assertFalse(filter.evaluate(poi("park", "A1")));
    }

    @Test
    public void testIsSupportedScriptEndToEnd() throws Exception {
        List<FeatureTypeStyle> fts = transformInline(
                """
                {
                  "id": "l",
                  "type": "symbol",
                  "source": "src",
                  "layout": {
                    "text-font": ["Noto Sans Regular"],
                    "text-field": [
                      "case",
                      ["is-supported-script", ["get", "kind"]],
                      ["get", "kind"],
                      "?"
                    ]
                  }
                }""");
        TextSymbolizer ts = getFirstSymbolizer(fts.get(0));
        // a Latin label is displayable by the server fonts, so the case picks the native value
        assertEquals("park", ts.getLabel().evaluate(poi("park", null), String.class));
        // U+0378 is a permanently unassigned code point: no font has a glyph for it
        assertEquals("?", ts.getLabel().evaluate(poi("A\u0378B", null), String.class));
    }
}
