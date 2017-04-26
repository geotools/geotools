/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.TestData;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Perceptual tests for {@link MBStyleTransformer}.
 * 
 * In order to display the visual tests as they run, uncomment the following line below:
 * 
 * // System.setProperty("org.geotools.test.interactive", "true");
 * 
 * HOW TO ADD A NEW TEST:
 * 
 * The first time you run a new test, the reference image must be generated. To do so, run the test with
 * <code>-Dorg.geotools.image.test.interactive=true</code>.
 *
 */
public class VisualTransformerTest {

    JSONParser jsonParser = new JSONParser();

    private static final long DISPLAY_TIME = 5000;

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    SimpleFeatureSource pointFS;

    SimpleFeatureSource gridFS;
    
    SimpleFeatureSource lineFS;

    SimpleFeatureSource bgFS;

    SimpleFeatureSource polygonFS;

    SimpleFeatureSource polygonsBigFS;    

    ReferencedEnvelope bounds;

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "testpoints.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        pointFS = ds.getFeatureSource("testpoints");
        gridFS = ds.getFeatureSource("testgrid");
        polygonFS = ds.getFeatureSource("testpolygons");
        polygonsBigFS = ds.getFeatureSource("testpolygonsbig");
        lineFS = ds.getFeatureSource("testlines");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        // UNCOMMENT THE BELOW LINE TO DISPLAY VISUAL TESTS
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    /**
     * Test generation of a GeoTools style from an MBFillLayer
     */
    @Test
    public void mbFillLayerVisualTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(polygonFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Fill Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("fill"), image, 50);
        mc.dispose();
    }
    
    /**
     * Test generation of a GeoTools style from an MBFillLayer (using a {tokenized} sprite fill pattern)
     */
    @Test
    public void mbFillLayerSpritesTokenizedVisualTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleTokenizedSpriteTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(polygonsBigFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Fill Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("fill-sprite-tokenized"), image, 50);
        mc.dispose();
    }    
    
    /**
     * Test generation of a GeoTools style from an MBFillLayer (using a constant sprite fill pattern)
     */
    @Test
    public void mbFillLayerSpritesVisualTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleSpriteTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(polygonsBigFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Fill Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("fill-sprite"), image, 50);
        mc.dispose();
    }   
    
    /**
     * Test generation of a GeoTools style from an MBFillLayer (using a function-defined sprite fill pattern)
     */
    @Test
    public void mbFillLayerSpritesFunctionTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleFunctionSpriteTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(polygonsBigFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Fill Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("fill-sprite-function"), image, 50);
        mc.dispose();
    } 
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerSpritesVisualTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleSimpleIconTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Symbol Sprite Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("symbol-sprite"), image, 50);
        mc.dispose();
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerTextVisualTest() throws Exception {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolTextTest.json");
        testVisualizeStyleWithPointFeatures(jsonObject, "Symbol Text Test", "symbol-text", true, 300, 300);
    }
    
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerTextAndIconVisualTest() throws Exception {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolTextAndIconTest.json");
        testVisualizeStyleWithPointFeatures(jsonObject, "Symbol Text and Icon Test", "symbol-text-icon", true, 450, 450);
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerHaloTextVisualTest() throws Exception {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolTextHaloTest.json");
        testVisualizeStyleWithPointFeatures(jsonObject, "Symbol Text Halo Test", "symbol-halo-text", true, 300, 300);
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerTextLinePlacementVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolTextLinePlacementTest.json");        
        testVisualizeStyleWithLineFeatures(jsonObject, "Symbol Text Line Placement", "symbol-text-line-placement", true);
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerIconLinePlacementVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleSimpleIconLinePlacementTest.json");        
        testVisualizeStyleWithLineFeatures(jsonObject, "Symbol Icon Line Placement", "symbol-icon-line-placement", true);
    }
    
    
    /**
     * Test visualization of a GeoTools style from an MB Symbol Layer
     */
    @Test
    public void mbSymbolLayerIconAndTextLinePlacementVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleSimpleIconAndTextLinePlacementTest.json");        
        testVisualizeStyleWithLineFeatures(jsonObject, "Symbol Text+Icon Line Placement", "symbol-text-icon-line-placement", true);
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Circle Layer
     */
    @Test
    public void mbCircleLayerVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("circleStyleTest.json");        
        testVisualizeStyleWithPointFeatures(jsonObject, "Circle Style Test", "circle-style-test", true, 300, 300);
    }
    
    /**
     * 
     * Test visualization of a GeoTools style from an MB Circle Layer using defaults
     */
    @Test
    public void mbCircleLayerDefaultsVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("circleStyleTestDefaults.json");        
        testVisualizeStyleWithPointFeatures(jsonObject, "Circle Style Test Defaults", "circle-style-test-defaults", true, 300, 300);
    }
    
    /**
     * Test visualization of a GeoTools style from an MB Circle Layer with opacity and overlaps
     */
    @Test
    public void mbCircleLayerOverlapVisualTest() throws Exception {
        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("circleStyleTestOverlap.json");        
        testVisualizeStyleWithPointFeatures(jsonObject, "Circle Style Test Overlap", "circle-style-test-overlap", true, 300, 300);
    }
    
    public void testVisualizeStyleWithPointFeatures(JSONObject jsonStyle, String renderTitle, String renderComparisonFileName, boolean includeGrid, int width, int height) throws Exception {

        // Read file to JSONObject

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonStyle);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();        
        if (includeGrid) {
            mc.addLayer(new FeatureLayer(gridFS, defaultLineStyle()));    
        }        
        mc.addLayer(new FeatureLayer(pointFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));        
        BufferedImage image = MapboxTestUtils.showRender(renderTitle, renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null, width, height);
        ImageAssert.assertEquals(file(renderComparisonFileName), image, 50);
        mc.dispose();
    }
    

    public void testVisualizeStyleWithLineFeatures(JSONObject jsonStyle, String renderTitle, String renderComparisonFileName, boolean showLines) throws Exception {
        // Read file to JSONObject

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonStyle);
        StyledLayerDescriptor sld = new MBStyleTransformer().transform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();

        if (showLines) {
            mc.addLayer(new FeatureLayer(lineFS, defaultLineStyle()));    
        }
        mc.addLayer(new FeatureLayer(lineFS, style));    
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender(renderTitle, renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file(renderComparisonFileName), image, 50);        
        mc.dispose();
    }
    

    public Style defaultLineStyle() {
        Rule rule = styleFactory.createRule();
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));
        rule.symbolizers().add(styleFactory.createLineSymbolizer(stroke, null));
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style lineStyle = styleFactory.createStyle();
        lineStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return lineStyle;
    }

    public Style defaultPointStyle() {

        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.BLACK)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(1));

        Rule rule = styleFactory.createRule();
        PointSymbolizer p = styleFactory.createPointSymbolizer(gr, null);

        rule.symbolizers().add(p);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style pointStyle = styleFactory.createStyle();
        pointStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return pointStyle;
    }

    public Style defaultPolyStyle() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1), filterFactory.literal(1));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to draw the default geometry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    File file(String name) throws IOException {
        // The first time you run a new test, the reference image must be generated. To do so, run the test with
        // -Dorg.geotools.image.test.interactive=true</code>
        return new File("src/test/resources/org/geotools/mbstyle/transform/test-data/rendered/"
                + name + ".png");
    }

}
