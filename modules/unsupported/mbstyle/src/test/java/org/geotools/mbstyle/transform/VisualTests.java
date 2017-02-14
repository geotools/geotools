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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.BackgroundMBLayer;
import org.geotools.mbstyle.CircleMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.referencing.CRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

public class VisualTests {

    JSONParser jsonParser = new JSONParser();
    
    private static final long DISPLAY_TIME = 6000;
    
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    SimpleFeatureSource pointFS;
    SimpleFeatureSource pointleftFS;
    SimpleFeatureSource polyRight;
    SimpleFeatureSource lineFS;
    SimpleFeatureSource gridFS;
    SimpleFeatureSource bgFS;
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
        lineFS = ds.getFeatureSource("testlines");
        bgFS = ds.getFeatureSource("background");
        pointleftFS = ds.getFeatureSource("testpointsleft");
        polyRight = ds.getFeatureSource("testpolygonsright");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        System.setProperty("org.geotools.test.interactive", "true");
    }
    
    /**
     * Test all QGIS marks in a single image.
     */
    @Test
    public void testMBCircleVisual() throws Exception {
        
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("circleStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().tranform(mbStyle);        
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style pointStyle = l.getUserStyles()[0];
        
        
        Style defaultLineStyle = defaultLineStyle();
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, pointStyle));
        mc.addLayer(new FeatureLayer(lineFS, defaultLineStyle));

        StreamingRenderer renderer = new StreamingRenderer();        
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
    }

    @Test
    public void testMBLineVisual() throws Exception {
        
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("lineStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().tranform(mbStyle);        
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style lineStyle = l.getUserStyles()[0];
        
        
        MapContent mc = new MapContent();
        // mc.addLayer(new FeatureLayer(pointFS, pointStyle));
        mc.addLayer(new FeatureLayer(lineFS, lineStyle));

        StreamingRenderer renderer = new StreamingRenderer();        
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
    }

    @Test
    public void testMBBackgroundVisual() throws Exception {
        
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("backgroundColorStyleTest.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().tranform(mbStyle);        
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];
        
        
        MapContent mc = new MapContent();
        // mc.addLayer(new FeatureLayer(pointFS, pointStyle));
        mc.addLayer(new FeatureLayer(bgFS, style));

        StreamingRenderer renderer = new StreamingRenderer();        
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
    }    
    
    @Test
    public void testMixed() throws Exception {               
        MapContent mc = new MapContent();
        // mc.addLayer(new FeatureLayer(pointFS, pointStyle));
        
        mc.addLayer(new FeatureLayer(pointleftFS, defaultPointStyle()));
        mc.addLayer(new FeatureLayer(polyRight, defaultPolyStyle()));
        mc.addLayer(new FeatureLayer(gridFS, defaultLineStyle()));

        StreamingRenderer renderer = new StreamingRenderer();        
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
    }
    
    @Test
    public void testMixed2() throws Exception {
        
        // Read file to JSONObject
        InputStream is = this.getClass().getResourceAsStream("testVisualize.json");
        String fileContents = IOUtils.toString(is, "utf-8");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        
        List<MBLayer> bgs = mbStyle.layers("test-source", "test-source-bg");
        List<MBLayer> circles = mbStyle.layers("test-source", "test-source-circles");
        List<MBLayer> fills = mbStyle.layers("test-source", "test-source-fills");
        List<MBLayer> lines =mbStyle.layers("test-source", "test-source-lines");
        
        assertEquals(1, bgs.size());
        assertEquals(1, circles.size());
        assertEquals(1, fills.size());
        assertEquals(1, lines.size());
        
        MapContent mc = new MapContent();
        MBStyleTransformer transformer = new MBStyleTransformer();
       
        Style bgStyle = styleFactory.createStyle();
        bgStyle.featureTypeStyles().add(transformer.transform(bgs.get(0)));
        mc.addLayer(new FeatureLayer(bgFS, bgStyle));
        
        Style circleStyle = styleFactory.createStyle();
        circleStyle.featureTypeStyles().add(transformer.transform(circles.get(0)));
        mc.addLayer(new FeatureLayer(pointleftFS, circleStyle));
        
        Style fillStyle = styleFactory.createStyle();
        fillStyle.featureTypeStyles().add(transformer.transform(fills.get(0)));
        mc.addLayer(new FeatureLayer(polyRight, fillStyle));
        
        Style lineStyle = styleFactory.createStyle();
        lineStyle.featureTypeStyles().add(transformer.transform(lines.get(0)));
        mc.addLayer(new FeatureLayer(lineFS, lineStyle));
        
//        
//        mc.addLayer(new FeatureLayer(pointleftFS, defaultPointStyle()));
//        mc.addLayer(new FeatureLayer(polyRight, defaultPolyStyle()));
//        mc.addLayer(new FeatureLayer(gridFS, defaultLineStyle()));

        StreamingRenderer renderer = new StreamingRenderer();        
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
    }
    
    
    public Style defaultLineStyle() {        
        Rule rule = styleFactory.createRule();
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));
        rule.symbolizers().add(styleFactory.createLineSymbolizer(stroke, null));
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});                
        Style lineStyle = styleFactory.createStyle();
        lineStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return lineStyle;
    }        
    
    public Style defaultPointStyle() {        
        
        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLACK), filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.BLACK)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(1));
        
        
        Rule rule = styleFactory.createRule();
        PointSymbolizer p = styleFactory.createPointSymbolizer(gr, null);
        
        rule.symbolizers().add(p);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});                
        Style pointStyle = styleFactory.createStyle();
        pointStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return pointStyle;
    }    
    
    public Style defaultPolyStyle() {        

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1),
                filterFactory.literal(1));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }    
    
    
    /**
     * bounds may be null
     * 
     * @param testName
     *            Name reported in event of failure
     * @param renderer
     *            Renderer being tested
     * @param timeOut
     *            Maximum time allowed for test
     * @param bounds
     *            area to draw
     * @param listener
     *            Optional listener
     * @throws Exception
     *             In the event of failure
     */
    protected static BufferedImage showRender(String testName, GTRenderer renderer, long timeOut,
            ReferencedEnvelope[] bounds, RenderListener listener) throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = RendererBaseTest.renderImage(renderer, bounds[i], listener);
        }
        final BufferedImage image = RendererBaseTest.mergeImages(images);

        RendererBaseTest.showImage(testName, timeOut, image);
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert (hasData);
        return image;
    }

}
