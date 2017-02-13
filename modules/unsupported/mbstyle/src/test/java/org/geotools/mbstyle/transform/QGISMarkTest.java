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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.geotools.mbstyle.CircleMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.referencing.CRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
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

public class QGISMarkTest {

    JSONParser jsonParser = new JSONParser();
    
    private static final long DISPLAY_TIME = 3000;
    
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();


    SimpleFeatureSource pointFS;

    SimpleFeatureSource lineFS;

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
        lineFS = ds.getFeatureSource("testlines");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        System.setProperty("org.geotools.test.interactive", "true");
    }

    File file(String name) {
        return new File(
                "src/test/resources/org/geotools/renderer/lite/test-data/mark/" + name + ".png");
    }

    /**
     * Test all QGIS marks in a single image.
     */
    @Test
    public void testQGIS() throws Exception {
        Style lineStyle = styleFactory.createStyle();
        Rule rule = styleFactory.createRule();
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));
        rule.symbolizers().add(styleFactory.createLineSymbolizer(stroke, null));
        FeatureTypeStyle fts2 = styleFactory.createFeatureTypeStyle(new Rule[]{rule});        
        lineStyle.featureTypeStyles().addAll(Arrays.asList(fts2));
//
        MapContent mc = new MapContent();
//        mc.addLayer(new FeatureLayer(pointFS, pStyle));
        mc.addLayer(new FeatureLayer(lineFS, lineStyle));
//
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("test name", renderer, DISPLAY_TIME, new ReferencedEnvelope[] {bounds}, null);

        // ImageAssert.assertEquals(file("qgis"), image, 50);
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
    
    /**
     * Test all QGIS marks in a single image.
     */
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
    
    
    public Style defaultLineStyle() {        
        Rule rule = styleFactory.createRule();
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));
        rule.symbolizers().add(styleFactory.createLineSymbolizer(stroke, null));
        FeatureTypeStyle fts2 = styleFactory.createFeatureTypeStyle(new Rule[]{rule});                
        Style lineStyle = styleFactory.createStyle();
        lineStyle.featureTypeStyles().addAll(Arrays.asList(fts2));
        return lineStyle;
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
