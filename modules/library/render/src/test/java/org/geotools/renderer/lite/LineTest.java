/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2015, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.*;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

@Ignore
public class LineTest {

    private static final long TIME = 4000;

    SimpleFeatureSource fs;

    ReferencedEnvelope bounds;

    private ContentFeatureSource squares;

    private ContentFeatureSource fsAround;

    @AfterClass
    public static void clearClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @BeforeClass
    public static void setupClass() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "line.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("line");
        fsAround = ds.getFeatureSource("around");
        squares = ds.getFeatureSource("square");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    File file(String name) {
        return new File(
                "src/test/resources/org/geotools/renderer/lite/test-data/line/" + name + ".png");
    }

    @Test
    public void testLineCircle() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineCircle.sld");

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("circle"), image, 10);
    }

    @Test
    public void testLineDoubleDash() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineDoubleDash.sld");
        MapViewport viewport = renderer.getMapContent().getViewport();
        ReferencedEnvelope re = viewport.getBounds();
        ReferencedEnvelope shifted =
                new ReferencedEnvelope(
                        re.getMinX() + 2,
                        re.getMaxX() - 3,
                        re.getMinY() + 2,
                        re.getMaxY() - 3,
                        re.getCoordinateReferenceSystem());
        viewport.setBounds(shifted);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "Lines with double dash array (2 fts)", renderer, TIME, shifted);
        ImageAssert.assertEquals(file("doubleDash"), image, 10);
    }

    private StreamingRenderer setupLineMap(String styleFile) throws IOException {
        return setupMap(fs, styleFile);
    }

    private StreamingRenderer setupMap(SimpleFeatureSource fs, String styleFile)
            throws IOException {
        Style style = RendererBaseTest.loadStyle(this, styleFile);

        return setupMap(fs, style);
    }

    private StreamingRenderer setupMap(SimpleFeatureSource fs, Style style) {
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }

    @Test
    @Ignore
    public void testPerPropertyUOM10() throws Exception {
        StreamingRenderer renderer = setupLineMap("linePerPropertyUom.sld");

        BufferedImage image =
                RendererBaseTest.showRender("linePerPropertyUom", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("linePerPropertyUom10"), image, 10);
    }

    @Test
    public void testLineRailway() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineRailway.sld");

        BufferedImage image = RendererBaseTest.showRender("Railway", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("railway"), image, 500);
    }

    @Test
    public void testLineRotatedSymbol() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineRotatedSymbol.sld");

        BufferedImage image = RendererBaseTest.showRender("Rotated symbol", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("lineRotatedSymbol"), image, 10);
    }

    @Test
    public void testLineDisplacedSymbol() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineDisplacedSymbol.sld");

        BufferedImage image =
                RendererBaseTest.showRender("Dispaced symbol", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("lineDispacedSymbol"), image, 10);
    }

    @Test
    public void testLineAnchorSymbol() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineAnchorSymbol.sld");

        BufferedImage image =
                RendererBaseTest.showRender("Anchor point at 1:1", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("lineAnchorSymbol"), image, 20);
    }

    @Test
    public void testLineDisplacedGraphic() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineDisplacedGraphic.sld");

        BufferedImage image =
                RendererBaseTest.showRender("Dispaced graphic", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("lineDispacedGraphic"), image, 10);
    }

    @Test
    public void testDotsStars() throws Exception {
        StreamingRenderer renderer = setupLineMap("dotsStars.sld");

        BufferedImage image = RendererBaseTest.showRender("Dots and stars", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("dotstar"), image, 200);
    }

    @Test
    public void testRenderingTransform() throws Exception {
        StreamingRenderer renderer = setupLineMap("line_rendering_transform.sld");

        BufferedImage image =
                RendererBaseTest.showRender(
                        "Lines with buffer rendering transform", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("renderingTransform"), image, 10);
    }

    @Test
    public void testPerpendicularOffsetLeftRight() throws Exception {
        StreamingRenderer renderer =
                setupMap(fs, RendererBaseTest.loadSEStyle(this, "linePerpendicularOffset-se.sld"));

        BufferedImage image =
                RendererBaseTest.showRender("Perpendicular offset", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("linePerpendincularOffset"), image, 10);
    }

    @Test
    public void testPerpendicularOffsetLeftRightSquares() throws Exception {
        StreamingRenderer renderer =
                setupMap(
                        squares,
                        RendererBaseTest.loadSEStyle(this, "linePerpendicularOffset-se.sld"));

        BufferedImage image =
                RendererBaseTest.showRender("Perpendicular offset", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("squaresPerpendincularOffset"), image, 10);
    }

    @Test
    public void testPerpendicularOffsetNPE() throws Exception {
        StreamingRenderer renderer =
                setupMap(
                        fsAround,
                        RendererBaseTest.loadStyle(this, "linePerpendicularOffsetSmall.sld"));

        final AtomicInteger errors = new AtomicInteger(0);
        BufferedImage image =
                RendererBaseTest.showRender(
                        "Perpendicular offset",
                        renderer,
                        TIME,
                        new ReferencedEnvelope[] {
                            new ReferencedEnvelope(1, 4, 1, 4, DefaultGeographicCRS.WGS84)
                        },
                        new RenderListener() {

                            @Override
                            public void featureRenderer(SimpleFeature feature) {
                                // nothing to do
                            }

                            @Override
                            public void errorOccurred(Exception e) {
                                errors.incrementAndGet();
                            }
                        });
        assertEquals(0, errors.get());
    }
}
