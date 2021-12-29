/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

/** @author ImranR */
public class MarkAlongLineTest {

    private static final long TIME = 5000;

    SimpleFeatureSource lineFS;
    SimpleFeatureSource polygonFS;

    Style squareWaveMarkerStyle;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

    @Before
    public void setUp() throws Exception {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        // setup data
        File property = new File(TestData.getResource(this, "markAlongLine.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        lineFS = ds.getFeatureSource("markAlongLine");
        assertNotNull(lineFS);

        polygonFS = ds.getFeatureSource("markAlongLinePolygon");
        assertNotNull(polygonFS);

        squareWaveMarkerStyle = RendererBaseTest.loadStyle(this, "markAlongLine_sqaure_wave.sld");
        assertNotNull(squareWaveMarkerStyle);
    }

    @Test
    public void testAllSquareWaveAngles() throws Exception {

        FeatureLayer lineLayer = new FeatureLayer(lineFS, squareWaveMarkerStyle);

        SimpleFeatureCollection fc =
                lineLayer
                        .getSimpleFeatureSource()
                        .getFeatures(ff.equal(ff.property("name"), ff.literal("all_turns"), true));
        assertTrue(fc.size() > 0);
        ReferencedEnvelope env = fc.getBounds();
        env.expandBy(0.05);
        MapContent mc = new MapContent();
        mc.addLayer(lineLayer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image =
                RendererBaseTest.showRender("WKT drapped along line", renderer, TIME, env);

        // ImageIO.write(image, "png", new File("D:\\markAlongLine_sqaure_all_angles.png"));

        File squareLineAllAngles =
                new File(TestData.getResource(this, "markAlongLine_sqaure_all_angles.png").toURI());
        ImageAssert.assertEquals(squareLineAllAngles, image, 200);
    }

    @Test
    public void testCurvedSquareWaves() throws Exception {

        FeatureLayer lineLayer = new FeatureLayer(lineFS, squareWaveMarkerStyle);

        SimpleFeatureCollection fc =
                lineLayer
                        .getSimpleFeatureSource()
                        .getFeatures(ff.equal(ff.property("name"), ff.literal("curve"), true));
        assertTrue(fc.size() > 0);
        ReferencedEnvelope env = fc.getBounds();
        env.expandBy(0.05);
        MapContent mc = new MapContent();
        mc.addLayer(lineLayer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image =
                RendererBaseTest.showRender("WKT drapped along line", renderer, TIME, env);

        // ImageIO.write(image, "png", new File("D:\\markAlongLine_sqaure_curve.png"));

        File squareLineAllAngles =
                new File(TestData.getResource(this, "markAlongLine_sqaure_curve.png").toURI());
        ImageAssert.assertEquals(squareLineAllAngles, image, 200);
    }

    @Test
    public void testRightAngledSquareWaves() throws Exception {

        FeatureLayer lineLayer = new FeatureLayer(lineFS, squareWaveMarkerStyle);

        SimpleFeatureCollection fc =
                lineLayer
                        .getSimpleFeatureSource()
                        .getFeatures(
                                ff.equal(ff.property("name"), ff.literal("right_angle"), true));
        assertTrue(fc.size() > 0);
        ReferencedEnvelope env = fc.getBounds();
        env.expandBy(0.025);
        MapContent mc = new MapContent();
        mc.addLayer(lineLayer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image =
                RendererBaseTest.showRender("WKT drapped along line", renderer, TIME, env);

        // ImageIO.write(image, "png", new File("D:\\markAlongLine_sqaure_right_angle.png"));

        File squareLineAllAngles =
                new File(
                        TestData.getResource(this, "markAlongLine_sqaure_right_angle.png").toURI());
        ImageAssert.assertEquals(squareLineAllAngles, image, 200);
    }

    @Test
    public void testTurnBackAngledSquareWaves() throws Exception {

        FeatureLayer lineLayer = new FeatureLayer(lineFS, squareWaveMarkerStyle);

        SimpleFeatureCollection fc =
                lineLayer
                        .getSimpleFeatureSource()
                        .getFeatures(ff.equal(ff.property("name"), ff.literal("turn_back"), true));
        assertTrue(fc.size() > 0);
        ReferencedEnvelope env = fc.getBounds();
        env.expandBy(0.01);
        MapContent mc = new MapContent();
        mc.addLayer(lineLayer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image =
                RendererBaseTest.showRender("WKT drapped along line", renderer, TIME, env);

        // ImageIO.write(image, "png", new File("D:\\markAlongLine_sqaure_turn_back.png"));

        File squareLineAllAngles =
                new File(TestData.getResource(this, "markAlongLine_sqaure_turn_back.png").toURI());
        ImageAssert.assertEquals(squareLineAllAngles, image, 200);
    }

    @Test
    public void testPolygon() throws Exception {

        FeatureLayer polygonLayer = new FeatureLayer(polygonFS, squareWaveMarkerStyle);

        SimpleFeatureCollection fc =
                polygonLayer
                        .getSimpleFeatureSource()
                        .getFeatures(ff.equal(ff.property("name"), ff.literal("all_turns"), true));
        assertTrue(fc.size() > 0);
        ReferencedEnvelope env = fc.getBounds();
        env.expandBy(1.1);
        MapContent mc = new MapContent();
        mc.addLayer(polygonLayer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image =
                RendererBaseTest.showRender(
                        "WKT drapped along polygon boundary", renderer, TIME, env);

        // ImageIO.write(image, "png", new File("D:\\markAlongLine_polygon.png"));

        File squareLineAllAngles =
                new File(TestData.getResource(this, "markAlongLine_polygon.png").toURI());
        ImageAssert.assertEquals(squareLineAllAngles, image, 200);
    }
}
