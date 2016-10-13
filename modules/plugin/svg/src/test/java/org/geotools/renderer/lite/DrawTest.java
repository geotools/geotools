package org.geotools.renderer.lite;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * 
 * 
 * @source $URL$
 */
public class DrawTest {
    private static final long TIME = 4000;

    SimpleFeatureSource squareFS;

    SimpleFeatureSource lineFS;

    SimpleFeatureSource pointFS;
    
    SimpleFeatureSource singlePointFS;

    SimpleFeatureSource pointRotateFS;

    ReferencedEnvelope bounds;

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        lineFS = ds.getFeatureSource("line");
        pointFS = ds.getFeatureSource("point");
        singlePointFS = ds.getFeatureSource("pointSingle");
        pointRotateFS = ds.getFeatureSource("pointRotation");
        bounds = squareFS.getBounds();
        bounds.expandBy(0.2, 0.2);

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    private void runFillTest(String styleName) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(squareFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender(styleName, renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/" + styleName + ".png"),
                image, 1000);
    }

    @Test
    public void testImageFill() throws Exception {
        runFillTest("fillHouse.sld");
    }

    @Test
    public void testRandomImageFill() throws Exception {
        runFillTest("fillRandomSVG.sld");
    }

    @Test
    public void testRandomRotatedImageFill() throws Exception {
        runFillTest("fillRandomRotatedSVG.sld");
    }

    @Test
    public void testRenderingHints() throws Exception {
        //check if rendering fails when there are no anti aliasing hints set.
        String styleName = "labeledPolygon.sld";
        Style style = RendererBaseTest.loadStyle(this, styleName);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(squareFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> hints = null;//renderer.getRendererHints();
        if (hints == null) {
            hints = new HashMap<Object, Object>();
        }
        
        if (renderer instanceof StreamingRenderer) {
            LabelCache labelCache;
            if (hints.containsKey(StreamingRenderer.LABEL_CACHE_KEY)) {
                labelCache = (LabelCache) hints.get(StreamingRenderer.LABEL_CACHE_KEY);
            } else {
                labelCache = new LabelCacheImpl();
                hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
            }
        }
        
        renderer.setRendererHints(hints);
        renderer.setMapContent(mc);
        Document document = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        
        db = dbf.newDocumentBuilder();
        

        // Create an instance of org.w3c.dom.Document
        document = db.getDOMImplementation().createDocument(null, "svg", null);

        SVGGeneratorContext ctx1 = SVGGeneratorContext.createDefault(document);
        SVGGeneratorContext ctx = ctx1;
        ctx.setComment("Generated by GeoTools with Batik SVG Generator");

        SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);

        Dimension canvasSize = new Dimension(300, 300);
        g2d.setSVGCanvasSize(canvasSize );
        Rectangle outputArea = new Rectangle(g2d.getSVGCanvasSize());
        ReferencedEnvelope dataArea = mc.getMaxBounds();

        renderer.paint(g2d, outputArea, dataArea);
    }
    @Test
    public void testPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouse.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouse", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouse.png"), image,
                1000);
    }

    @Test
    public void testDisplacedPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouseDisplaced.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouseDisplaced", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                  "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseDisplaced.png"),
                  image, 1000);
    }

    @Test
    public void testDisplacedLine() throws Exception {
        StreamingRenderer renderer = setupLineRenderer("lineHouseDisplaced.sld");

        BufferedImage image = RendererBaseTest.showRender("LineHouseDisplaced", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                  "./src/test/resources/org/geotools/renderer/lite/test-data/lineHouseDisplaced.png"),
                  image, 1000);
    }

    @Test
    public void testAnchorPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouseAnchor.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouse", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseAnchor.png"),
                image, 1000);
    }

    @Test
    public void testAnchorPointRotateBase() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "rotateSVGHouseBase.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointRotateFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender("PointHouseRotate", renderer, TIME,
                bounds);
        ImageAssert
                .assertEquals(
                        new File(
                                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseAnchorRotateBase.png"),
                        image, 1000);
    }

    @Test
    public void testAnchorPointRotateSide() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "rotateSVGHouseSide.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointRotateFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender("PointHouseRotate", renderer, TIME,
                bounds);
        ImageAssert.assertEquals(new File(
                                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseAnchorRotateSide.png"),
                image, 1000);
    }
    

    @Test
    public void testParametricNoValues() throws Exception {
        StreamingRenderer renderer = setupSinglePointRenderer("firestationNoParams.sld");

        BufferedImage image = RendererBaseTest.showRender("FireStation", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/firestationNoParams.png"),
                image, 1000);
    }
    
    @Test
    public void testParametricOnlyFill() throws Exception {
        StreamingRenderer renderer = setupSinglePointRenderer("firestationOnlyFill.sld");

        BufferedImage image = RendererBaseTest.showRender("FireStation", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/firestationOnlyFill.png"),
                image, 1000);
    }
    
    @Test
    public void testParametricAllValues() throws Exception {
        StreamingRenderer renderer = setupSinglePointRenderer("firestationAllParams.sld");

        BufferedImage image = RendererBaseTest.showRender("FireStation", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/firestationAllParams.png"),
                image, 1000);
    }



    private StreamingRenderer setupPointRenderer(String pointStyle) throws IOException {
        Style pStyle = RendererBaseTest.loadStyle(this, pointStyle);
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }
    
    private StreamingRenderer setupSinglePointRenderer(String pointStyle) throws IOException {
        Style pStyle = RendererBaseTest.loadStyle(this, pointStyle);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(singlePointFS, pStyle));
        mc.getViewport().setBounds(lineFS.getBounds());

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }


    private StreamingRenderer setupLineRenderer(String lineStyle) throws IOException {
        Style lStyle = RendererBaseTest.loadStyle(this, lineStyle);
        Style baseStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, baseStyle));
        mc.addLayer(new FeatureLayer(lineFS, lStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }
}
