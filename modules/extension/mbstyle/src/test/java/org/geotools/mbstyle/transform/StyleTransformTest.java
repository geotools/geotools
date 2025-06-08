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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.geotools.TestData;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
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
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
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
import org.geotools.styling.SLD;
import org.hamcrest.CoreMatchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

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

        assertEquals(1, tsym.fonts().size());
        assertEquals(1, tsym.fonts().get(0).getFamily().size());

        FontAlternativesFunction family =
                (FontAlternativesFunction) tsym.fonts().get(0).getFamily().get(0);
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
}
