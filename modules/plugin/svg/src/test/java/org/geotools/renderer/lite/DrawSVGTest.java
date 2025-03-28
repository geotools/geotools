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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class DrawSVGTest {
    private static final Logger LOGGER = Logging.getLogger(DrawSVGTest.class);

    final RenderListener listener = new RenderListener() {
        @Override
        public void featureRenderer(SimpleFeature feature) {
            LOGGER.config(feature.toString());
        }

        @Override
        public void errorOccurred(Exception e) {
            LOGGER.severe(e.toString());
            fail(e.getMessage());
        }
    };

    SimpleFeatureSource squareFS;

    SimpleFeatureSource lineFS;

    SimpleFeatureSource pointFS;

    SimpleFeatureSource singlePointFS;

    SimpleFeatureSource pointRotateFS;

    ReferencedEnvelope[] bounds = new ReferencedEnvelope[1];

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        File property =
                new File(TestData.getResource(this, "square2.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square2");
        lineFS = ds.getFeatureSource("line");
        pointFS = ds.getFeatureSource("point");
        singlePointFS = ds.getFeatureSource("pointSingle");
        pointRotateFS = ds.getFeatureSource("pointRotation");
        bounds[0] = squareFS.getBounds();
        bounds[0].expandBy(0.2, 0.2);

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    @Test
    public void testPoint() throws Exception {
        setupPointRenderer("pointHouse.sld");
    }

    @Test
    public void testDisplacedPoint() throws Exception {
        setupPointRenderer("pointHouseDisplaced.sld");
    }

    @Test
    public void testDisplacedLine() throws Exception {
        setupLineRenderer("lineHouseDisplaced.sld");
    }

    @Test
    public void testSvgMark() throws Exception {
        setupSinglePointRenderer("hospital.sld");
    }

    @Test
    public void testSvgMarkOrientation() throws Exception {
        setupSinglePointRenderer("atm.sld");
    }

    /**
     * This one does not really render as I'd like, but we cannot have a shape in the group that has its own specific
     * winding rule in Java. The test is there because this one also has a transformation on the shapes to re-position
     * them.
     */
    @Test
    public void testSvgMarkTransformedShape() throws Exception {
        setupSinglePointRenderer("convenience.sld");
    }

    private void setupPointRenderer(String pointStyle) throws IOException, ParserConfigurationException {
        Style pStyle = RendererBaseTest.loadStyle(this, pointStyle);
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Create an instance of org.w3c.dom.Document
        Document document = db.getDOMImplementation().createDocument(null, "svg", null);

        // Set up the map
        SVGGeneratorContext ctx1 = SVGGeneratorContext.createDefault(document);
        SVGGeneratorContext ctx = ctx1;
        ctx.setComment("Generated by GeoTools2 with Batik SVG Generator");

        SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);

        Dimension canvasSize = new Dimension(300, 300);
        g2d.setSVGCanvasSize(canvasSize);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        Rectangle outputArea = new Rectangle(g2d.getSVGCanvasSize());
        ReferencedEnvelope dataArea = mc.getMaxBounds();

        LOGGER.finest("rendering map");
        renderer.paint(g2d, outputArea, dataArea);
        LOGGER.finest("writing to file");
        String output = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputStreamWriter osw = new OutputStreamWriter(out, UTF_8)) {
            g2d.stream(osw);
            output = new String(out.toByteArray(), UTF_8);
        }
        assertNotNull(output);

        assertTrue(output.contains("path"));
        mc.dispose();
    }

    private void setupSinglePointRenderer(String pointStyle) throws IOException, ParserConfigurationException {
        Style pStyle = RendererBaseTest.loadStyle(this, pointStyle);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(singlePointFS, pStyle));

        mc.getViewport().setBounds(lineFS.getBounds());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Create an instance of org.w3c.dom.Document
        Document document = db.getDOMImplementation().createDocument(null, "svg", null);

        // Set up the map
        SVGGeneratorContext ctx1 = SVGGeneratorContext.createDefault(document);
        SVGGeneratorContext ctx = ctx1;
        ctx.setComment("Generated by GeoTools2 with Batik SVG Generator");

        SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);

        Dimension canvasSize = new Dimension(300, 300);
        g2d.setSVGCanvasSize(canvasSize);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        Rectangle outputArea = new Rectangle(g2d.getSVGCanvasSize());
        ReferencedEnvelope dataArea = mc.getMaxBounds();
        dataArea.expandBy(5); // some of these have 0 size

        LOGGER.finest("rendering map");
        renderer.paint(g2d, outputArea, dataArea);
        LOGGER.finest("writing to file");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String output = null;
        try (OutputStreamWriter osw = new OutputStreamWriter(out, UTF_8)) {
            g2d.stream(osw);
            output = new String(out.toByteArray(), UTF_8);
        }
        assertNotNull(output);
        assertTrue(output.contains("path"));
        mc.dispose();
    }

    private void setupLineRenderer(String lineStyle) throws IOException, ParserConfigurationException {
        Style lStyle = RendererBaseTest.loadStyle(this, lineStyle);
        Style baseStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, baseStyle));
        mc.addLayer(new FeatureLayer(lineFS, lStyle));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Create an instance of org.w3c.dom.Document
        Document document = db.getDOMImplementation().createDocument(null, "svg", null);

        // Set up the map
        SVGGeneratorContext ctx1 = SVGGeneratorContext.createDefault(document);
        SVGGeneratorContext ctx = ctx1;
        ctx.setComment("Generated by GeoTools2 with Batik SVG Generator");

        SVGGraphics2D g2d = new SVGGraphics2D(ctx, true);

        Dimension canvasSize = new Dimension(300, 300);
        g2d.setSVGCanvasSize(canvasSize);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        Rectangle outputArea = new Rectangle(g2d.getSVGCanvasSize());
        ReferencedEnvelope dataArea = mc.getMaxBounds();

        LOGGER.finest("rendering map");
        renderer.paint(g2d, outputArea, dataArea);
        LOGGER.finest("writing to file");
        String output = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputStreamWriter osw = new OutputStreamWriter(out, UTF_8)) {
            g2d.stream(osw);
            output = new String(out.toByteArray(), UTF_8);
        }
        assertNotNull(output);

        assertTrue(output.contains("path"));
        mc.dispose();
    }
}
