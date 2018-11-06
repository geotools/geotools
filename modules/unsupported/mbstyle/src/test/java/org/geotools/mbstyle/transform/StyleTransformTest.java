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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.geotools.TestData;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.FilterFunction_isometric;
import org.geotools.filter.function.FilterFunction_offset;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.layer.*;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.styling.*;
import org.geotools.xml.styling.SLDTransformer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/** Test parsing and transforming a Mapbox fill layer from json. */
public class StyleTransformTest {

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

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
                Integer.valueOf(20),
                psym.getDisplacement().getDisplacementX().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(20),
                psym.getDisplacement().getDisplacementY().evaluate(null, Integer.class));
    }

    /** Test parsing and generating a MapBox fill extrusion */
    @Test
    public void testFillExtrusion() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("fillExtrusionTest.json");
        MBObjectParser parse = new MBObjectParser(StyleTransformTest.class);

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("composite");

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
        assertEquals("0", ((FilterFunction_offset) roofGeometry).getParameters().get(1).toString());
        assertEquals(
                "Height", ((FilterFunction_offset) roofGeometry).getParameters().get(2).toString());

        Expression sidesGeometry = sides.getGeometry();
        assertEquals("isometric", ((FilterFunction_isometric) sidesGeometry).getName());
        assertEquals(2, ((FilterFunction_isometric) sidesGeometry).getParameters().size());
        //        assertEquals("the_geom", ((FilterFunction_isometric)
        // sidesGeometry).getParameters().get(0).toString());
        assertEquals(
                "Height",
                ((FilterFunction_isometric) sidesGeometry).getParameters().get(1).toString());

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

        assertEquals(
                new Color(0, 153, 255), lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(
                Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(4), lsym.getPerpendicularOffset().evaluate(null, Integer.class));

        List<Integer> expectedDashes = Arrays.asList(50, 50);
        assertEquals(expectedDashes.size(), lsym.getStroke().dashArray().size());
        for (int i = 0; i < expectedDashes.size(); i++) {
            Integer n = (Integer) lsym.getStroke().dashArray().get(i).evaluate(null, Integer.class);
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

        assertEquals(
                new Color(0, 153, 255), lsym.getStroke().getColor().evaluate(null, Color.class));
        assertEquals("square", lsym.getStroke().getLineCap().evaluate(null, String.class));
        assertEquals("round", lsym.getStroke().getLineJoin().evaluate(null, String.class));
        assertEquals(.5d, lsym.getStroke().getOpacity().evaluate(null, Double.class), .0001);
        assertEquals(
                Integer.valueOf(10), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(-5), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
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
        assertEquals(
                Integer.valueOf(1), lsym.getStroke().getOpacity().evaluate(null, Integer.class));
        assertEquals(Integer.valueOf(1), lsym.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(0), lsym.getPerpendicularOffset().evaluate(null, Integer.class));
        assertTrue(lsym.getStroke().dashArray() == null);
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

        assertTrue(psym.getGraphic() != null);

        assertEquals(
                Integer.valueOf(30), psym.getGraphic().getSize().evaluate(null, Integer.class));

        assertNotNull(psym.getGraphic().getDisplacement());
        assertEquals(
                Integer.valueOf(10),
                psym.getGraphic()
                        .getDisplacement()
                        .getDisplacementX()
                        .evaluate(null, Integer.class));
        assertEquals(
                Integer.valueOf(10),
                psym.getGraphic()
                        .getDisplacement()
                        .getDisplacementY()
                        .evaluate(null, Integer.class));

        assertEquals(1, psym.getGraphic().graphicalSymbols().size());

        GraphicalSymbol gs = psym.getGraphic().graphicalSymbols().get(0);
        assertTrue(gs instanceof Mark);
        Mark m = (Mark) gs;

        assertNotNull(m.getFill());
        assertEquals(Color.RED, m.getFill().getColor().evaluate(null, Color.class));
        assertEquals(
                Double.valueOf(.5), m.getFill().getOpacity().evaluate(null, Double.class), .0001);

        assertNotNull(m.getStroke());
        assertEquals(Color.GREEN, m.getStroke().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(2), m.getStroke().getWidth().evaluate(null, Integer.class));
        assertEquals(
                Double.valueOf(.75),
                m.getStroke().getOpacity().evaluate(null, Double.class),
                .0001);
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

        assertTrue(psym.getGraphic() != null);

        assertEquals(
                Integer.valueOf(10), psym.getGraphic().getSize().evaluate(null, Integer.class));

        if (psym.getGraphic().getDisplacement() != null) {
            assertEquals(
                    Integer.valueOf(0),
                    psym.getGraphic()
                            .getDisplacement()
                            .getDisplacementX()
                            .evaluate(null, Integer.class));
            assertEquals(
                    Integer.valueOf(0),
                    psym.getGraphic()
                            .getDisplacement()
                            .getDisplacementY()
                            .evaluate(null, Integer.class));
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
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PolygonSymbolizer);
        PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;

        assertEquals(Color.GREEN, psym.getFill().getColor().evaluate(null, Color.class));
        assertEquals(
                Double.valueOf(.45),
                psym.getFill().getOpacity().evaluate(null, Double.class),
                .0001);

        assertNull(psym.getStroke());
    }

    @Test
    public void testBackgroundDefaults() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundStyleTestDefault.json");

        // Find the CircleMBLayer and assert it contains the correct FeatureTypeStyle.
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PolygonSymbolizer);
        PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;

        assertEquals(Color.BLACK, psym.getFill().getColor().evaluate(null, Color.class));
        assertEquals(Integer.valueOf(1), psym.getFill().getOpacity().evaluate(null, Integer.class));

        assertNull(psym.getStroke());
    }

    @Test
    public void testBackgroundPattern() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("backgroundImgStyleTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof BackgroundMBLayer);
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(1, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertTrue(symbolizer instanceof PolygonSymbolizer);
        PolygonSymbolizer psym = (PolygonSymbolizer) symbolizer;

        assertEquals(Color.BLUE, psym.getFill().getColor().evaluate(null, Color.class));
        assertEquals(1, psym.getFill().getGraphicFill().graphicalSymbols().size());
        assertEquals(
                Double.valueOf(0.75), psym.getFill().getOpacity().evaluate(null, Double.class));

        assertNull(psym.getStroke());
    }

    @Test
    public void testSymbolFont() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolTextTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
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
        assertEquals(2, tsym.fonts().get(0).getFamily().size());

        assertEquals("Bitstream Vera Sans", tsym.fonts().get(0).getFamily().get(0).toString());
        assertEquals("Other Test Font", tsym.fonts().get(0).getFamily().get(1).toString());
    }

    /**
     * MapBox symbol-avoid-edges defaults to false, If true, the symbols will not cross tile edges
     * to avoid mutual collisions. This concept is represented by using the Partials option in
     * GeoTools. The partials options instructs the renderer to render labels that cross the map
     * extent, which are normally not painted since there is no guarantee that a map put on the side
     * of the current one (tiled rendering) will contain the other half of the label. By enabling
     * “partials” the style editor takes responsibility for the other half being there (maybe
     * because the label points have been placed by hand and are assured not to conflict with each
     * other, at all zoom levels).
     *
     * <p>Based upon the above if symbol-avoid-edges is true we do not need to add the partials
     * option as the renderer will do this by default. But if symbol-avoid-edges is missing or set
     * to false, then we do need to add the partials option set to true.
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSymbolAvoidEdgesNotSupplied() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolTextAndIconTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertEquals("true", ((TextSymbolizerImpl) symbolizer).getOption("partials"));
    }

    @Test
    public void testSymbolAvoidEdgesFalse() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(false, layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertEquals("true", ((TextSymbolizerImpl) symbolizer).getOption("partials"));
    }

    @Test
    public void testSymbolAvoidEdgesTrue() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTrueTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("symbol-avoid-edges"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertNull("true", ((TextSymbolizerImpl) symbolizer).getOption("partials"));
    }

    @Test
    public void testSymbolIconIgnorePlacementNotProvided() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("icon-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertEquals("true", symbolizer.getOptions().get("labelObstacle"));
    }

    @Test
    public void testSymbolIconIgnorePlacementTrue() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolAvoidEdgesTest.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("icon-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertEquals("true", ((TextSymbolizerImpl) symbolizer).getOption("labelObstacle"));
    }

    @Test
    public void testSymbolTextIgnorePlacementNotProvided() throws IOException, ParseException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest2.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertNull(layout.get("text-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertEquals("true", symbolizer.getOptions().get("labelObstacle"));
    }

    @Test
    public void testSymbolTextIgnorePlacementTrue()
            throws IOException, ParseException, TransformerException {
        JSONObject jsonObject = parseTestStyle("symbolStyleTest3.json");
        JSONArray jsonLayers = (JSONArray) jsonObject.get("layers");
        JSONObject jsonLayer = (JSONObject) jsonLayers.get(0);
        JSONObject layout = (JSONObject) jsonLayer.get("layout");

        assertEquals(true, layout.get("text-ignore-placement"));

        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = mbStyle.transform();
        SLDTransformer styleTransform = new SLDTransformer();
        String xml = styleTransform.transform(sld);
        // System.out.print(xml);
        List<MBLayer> layers = mbStyle.layers("test-source");
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);
        Rule r = fts.get(0).rules().get(0);
        Symbolizer symbolizer = r.symbolizers().get(0);
        assertNull("true", ((TextSymbolizerImpl) symbolizer).getOption("labelObstacle"));
    }

    /** Read a test Mapbox Style file (json) and parse it into a {@link JSONObject}. */
    private JSONObject parseTestStyle(String filename) throws IOException, ParseException {
        return MapboxTestUtils.parseTestStyle(filename);
    }

    @Test
    public void testMapboxTokenValues() throws Exception {

        File property = new File(TestData.getResource(this, "testpoints.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        ContentFeatureSource pointFS = ds.getFeatureSource("testpoints");

        MBStyleTransformer transformer =
                new MBStyleTransformer(new MBObjectParser(SymbolMBLayer.class));
        Expression e = transformer.cqlExpressionFromTokens("Replace text here: \"{text}\"");
        Map<String, String> m = new HashMap<>();

        SimpleFeatureIterator sfi = pointFS.getFeatures().features();
        while (sfi.hasNext()) {
            SimpleFeature sf = sfi.next();
            String s = e.evaluate(sf, String.class);
        }
    }

    @Test
    public void testTextFontStops() throws Exception {
        JSONObject jsonObject = parseTestStyle("textFontTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);

        List<MBLayer> layers = mbStyle.layers("test-source");
        assertEquals(1, layers.size());
        assertTrue(layers.get(0) instanceof SymbolMBLayer);
        List<FeatureTypeStyle> fts = layers.get(0).transform(mbStyle);

        assertEquals(1, fts.get(0).rules().size());
        Rule r = fts.get(0).rules().get(0);

        assertEquals(2, r.symbolizers().size());
        Symbolizer symbolizer = r.symbolizers().get(1);
        assertTrue(symbolizer instanceof TextSymbolizer);
        TextSymbolizer tsym = (TextSymbolizer) symbolizer;

        assertEquals(1, tsym.fonts().size());
        assertEquals(1, tsym.fonts().get(0).getFamily().size());

        assertEquals(
                "Apple-Chancery",
                (((CategorizeFunction) tsym.fonts().get(0).getFamily().get(0)).getParameters())
                        .get(1)
                        .toString());

        //        StyledLayerDescriptor sld = mbStyle.transform();
        //        SLDTransformer styleTransform = new SLDTransformer();
        //        String xml = styleTransform.transform(sld);
        //        System.out.print(xml);

    }
}
