/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** Tests labeling in multiple scripts on point and line labels */
public class MultiScriptTest extends TestCase {

    private static final long TIME = 10000;

    static final int TOLERANCE = 3000;

    GeometryFactory gf = new GeometryFactory();

    SimpleFeatureSource points;

    SimpleFeatureSource lines;

    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        RendererBaseTest.setupVeraFonts();
        FontCache.getDefaultInstance()
                .registerFont(
                        Font.createFont(
                                Font.TRUETYPE_FONT,
                                TestData.getResource(this, "DroidSansArmenian.ttf").openStream()));
        FontCache.getDefaultInstance()
                .registerFont(
                        Font.createFont(
                                Font.TRUETYPE_FONT,
                                TestData.getResource(this, "DroidSansArmenian.ttf").openStream()));
        FontCache.getDefaultInstance()
                .registerFont(
                        Font.createFont(
                                Font.TRUETYPE_FONT,
                                TestData.getResource(this, "DroidNaskh-Regular.ttf").openStream()));

        bounds = new ReferencedEnvelope(0, 10, 0, 10, null);

        // System.setProperty("org.geotools.test.interactive", "true");

        buildPointFeatures();
        buildLineFeatures();
    }

    private void buildPointFeatures() throws IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("multiScript");
        SimpleFeatureType type = builder.buildFeatureType();

        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 9)),
                            "Some latin and some armenian\n\u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566"
                        },
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 6)),
                            "Latin, arab, armenian"
                                    + "\n\u0627\u062E\u062A\u0628\u0627\u0631\n"
                                    + "\u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566"
                        },
                        null);
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(7.5, 3)),
                            "armenian \u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566"
                        },
                        null);
        SimpleFeature f4 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(2.5, 3)),
                            "\u0627\u062E\u062A\u0628\u0627\u0631"
                        },
                        null);

        SimpleFeature f5 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPoint(new Coordinate(5, 1)),
                            "\u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566  abc  \u0627\u062E\u062A\u0628\u0627\u0631"
                        },
                        null);

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f1);
        data.addFeature(f2);
        data.addFeature(f3);
        data.addFeature(f4);
        data.addFeature(f5);
        points = data.getFeatureSource("multiScript");
    }

    private void buildLineFeatures() throws IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", LineString.class);
        builder.add("label", String.class);
        builder.setName("multiScript");
        SimpleFeatureType type = builder.buildFeatureType();

        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            circleArcInBounds(5, 0, 8, bounds),
                            "armenian    \u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566"
                        },
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            circleArcInBounds(5, -3, 8, bounds),
                            "\u0562\u0561\u0580\u0565\u0582 \u0541\u0565\u0566 abc \u0627\u062E\u062A\u0628\u0627\u0631"
                        },
                        null);
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            circleArcInBounds(5, -6, 8, bounds),
                            "\u062A\u0635\u0628\u062D/ \u062A\u0635\u0628\u062D\u064A\u0646 \u0639\u0644\u0649 \u062E\u064A\u0631"
                        },
                        null);

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f1);
        data.addFeature(f2);
        data.addFeature(f3);
        lines = data.getFeatureSource("multiScript");
    }

    private LineString circleArcInBounds(
            double x, double y, double radius, ReferencedEnvelope bounds) {
        Point center = gf.createPoint(new Coordinate(x, y));
        Polygon buffered = (Polygon) center.buffer(radius, 64);
        Polygon mask = JTS.toGeometry(bounds);
        Geometry intersection = buffered.getExteriorRing().intersection(mask);
        return (LineString) intersection;
    }

    public void testMultiScriptPoint() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "multiscript/textMultiScript.sld");
        BufferedImage image = renderLabels(points, style, "Multi script");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/multiscript/textMultiScript.png";
        ImageAssert.assertEquals(new File(refPath), image, TOLERANCE);
    }

    public void testMultiScriptPointWrap() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "multiscript/textMultiScriptWrap.sld");
        BufferedImage image = renderLabels(points, style, "Multi script wrap");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/multiscript/textMultiScriptWrap.png";
        ImageAssert.assertEquals(new File(refPath), image, TOLERANCE);
    }

    public void testMultiScriptLine() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "multiscript/textMultiScriptLine.sld");
        BufferedImage image = renderLabels(lines, style, "Multi script lines");
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/multiscript/textMultiScriptLine.png";
        ImageAssert.assertEquals(new File(refPath), image, TOLERANCE);
    }

    private BufferedImage renderLabels(SimpleFeatureSource fs, Style style, String title)
            throws Exception {
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        return RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }

    public void testFollowLineWithLocalTransform() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "multiscript/textMultiScriptLine.sld");
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(lines, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        int SIZE = 300;
        final BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, SIZE, SIZE);
        g.setTransform(
                new AffineTransform(
                        1.1,
                        Math.sin(Math.toRadians(15)),
                        -Math.sin(Math.toRadians(15)),
                        1.1,
                        15,
                        20));
        renderer.paint(g, new Rectangle(SIZE, SIZE), bounds);
        mc.dispose();
        renderer.getMapContent().dispose();

        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/multiscript/textMultiScriptTransformedLine.png";
        ImageAssert.assertEquals(new File(refPath), image, TOLERANCE);
    }
}
