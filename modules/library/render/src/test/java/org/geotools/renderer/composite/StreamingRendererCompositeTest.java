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
package org.geotools.renderer.composite;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Runs the same compositions as the {@link CompositeTest}, but going through the StreamingRenderer
 * and testing feature type and symbolizer based compositions
 *
 * @author Andrea Aime - GeoSolutions
 */
@RunWith(Parameterized.class)
public class StreamingRendererCompositeTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private static GridCoverage2D BKG;

    private static GridCoverage2D BKG2;

    private static GridCoverage2D MAP;

    private static GridCoverage2D MAP2;

    private String composite;

    public StreamingRendererCompositeTest(String composite) {
        this.composite = composite;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> result = new ArrayList<>();

        // compositing modes
        result.add(new Object[] {"copy"});
        result.add(new Object[] {"destination"});
        result.add(new Object[] {"source-over"});
        result.add(new Object[] {"destination-over"});
        result.add(new Object[] {"source-in"});
        result.add(new Object[] {"destination-in"});
        result.add(new Object[] {"source-out"});
        result.add(new Object[] {"destination-out"});
        result.add(new Object[] {"source-atop"});
        result.add(new Object[] {"destination-atop"});
        result.add(new Object[] {"xor"});

        // blending modes
        result.add(new Object[] {"multiply"});
        result.add(new Object[] {"screen"});
        result.add(new Object[] {"overlay"});
        result.add(new Object[] {"darken"});
        result.add(new Object[] {"lighten"});
        result.add(new Object[] {"color-dodge"});
        result.add(new Object[] {"color-burn"});
        result.add(new Object[] {"hard-light"});
        result.add(new Object[] {"soft-light"});
        result.add(new Object[] {"difference"});
        result.add(new Object[] {"exclusion"});

        return result;
    }

    @BeforeClass
    public static void prepareData() throws Exception {
        BKG = readCoverage("bkg.png");
        BKG2 = readCoverage("bkg2.png");
        MAP = readCoverage("map.png");
        MAP2 = readCoverage("map2.png");
    }

    private static GridCoverage2D readCoverage(String imageFileName) throws Exception {
        BufferedImage bi =
                ImageIO.read(CompositeTest.class.getResourceAsStream("test-data/" + imageFileName));
        GridCoverageFactory factory = new GridCoverageFactory();
        Envelope2D envelope =
                new Envelope2D(
                        DefaultEngineeringCRS.GENERIC_2D, 0, 0, bi.getWidth(), bi.getHeight());
        return factory.create(imageFileName, bi, envelope);
    }

    @Test
    public void testCompositeFts1() throws Exception {
        BufferedImage blended = composeFts(BKG, MAP);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + composite
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    @Test
    public void testCompositeFts2() throws Exception {
        BufferedImage blended = composeFts(BKG2, MAP2);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + composite
                                + ".png");
        ImageAssert.assertEquals(reference, blended, 0);
    }

    @Test
    public void testCompositeExternalGraphicPoint1() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("pointBlend.sld");
        BufferedImage blended = composePoint(BKG, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + composite
                                + ".png");
        ImageIO.write(
                blended,
                "PNG",
                new File(reference.getParent(), "blend1-" + composite + "-point.png"));
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeExternalGraphicPoint2() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("pointBlend2.sld");
        BufferedImage blended = composePoint(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeRedMark() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("redMarkBlend.sld");
        BufferedImage blended = composePoint(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/bkg2-red-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeRedStrokeLine() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("redLineBlend.sld");
        BufferedImage blended = composeLine(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/bkg2-red-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeRedGraphicStrokeLine() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("redLineGraphicStrokeBlend.sld");
        BufferedImage blended = composeLine(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/bkg2-red-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeExternalGraphicLine1() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("lineBlend.sld");
        BufferedImage blended = composeLine(BKG, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeExternalGraphicLine2() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("lineBlend2.sld");
        BufferedImage blended = composeLine(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeRedFill() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("redFillBlend.sld");
        BufferedImage blended = composePolygon(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/bkg2-red-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeGraphicFill() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("fillBlend.sld");
        BufferedImage blended = composePolygon(BKG, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend1-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    @Test
    public void testCompositeGraphicFill2() throws Exception {
        Style style = applyCompositeOnFirstSymbolizer("fillBlend2.sld");
        BufferedImage blended = composePolygon(BKG2, style);

        // compare with expected image
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/composite/test-data/blend2-"
                                + composite
                                + ".png");
        // allow some tolerance, the JDK does not do exactly the same thing as blending two images
        // but only for the copy case... uh
        int threshold = 50;
        if ("copy".equals(composite)) {
            threshold = 200;
        }
        ImageAssert.assertEquals(reference, blended, threshold);
    }

    private BufferedImage composeFts(GridCoverage2D first, GridCoverage2D second) {
        // build the map content
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        Style baseStyle = sb.createStyle(sb.createRasterSymbolizer());
        mc.addLayer(new GridCoverageLayer(first, baseStyle));
        FeatureTypeStyle compositeFts = sb.createFeatureTypeStyle(sb.createRasterSymbolizer());
        compositeFts.getOptions().put(FeatureTypeStyle.COMPOSITE, composite);
        Style compositeStyle = sb.createStyle();
        compositeStyle.featureTypeStyles().add(compositeFts);
        mc.addLayer(new GridCoverageLayer(second, compositeStyle));

        // prepare the graphics for the streaming renderer and paint
        RenderedImage referenceImage = first.getRenderedImage();
        BufferedImage blended =
                new BufferedImage(
                        referenceImage.getWidth(),
                        referenceImage.getWidth(),
                        BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = blended.createGraphics();
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        sr.paint(
                graphics,
                new Rectangle(0, 0, referenceImage.getWidth(), referenceImage.getHeight()),
                ReferencedEnvelope.reference(first.getEnvelope()));
        graphics.dispose();
        mc.dispose();
        return blended;
    }

    private BufferedImage composePoint(GridCoverage2D first, Style pointStyle)
            throws SchemaException, IOException {
        // build the map content
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        // first layer is the usual coverage
        Style baseStyle = sb.createStyle(sb.createRasterSymbolizer());
        mc.addLayer(new GridCoverageLayer(first, baseStyle));

        // second layer is a point in the middle of the map
        RenderedImage referenceImage = first.getRenderedImage();
        Point midPoint =
                new Point(
                        new LiteCoordinateSequence(
                                referenceImage.getWidth() / 2d, referenceImage.getHeight() / 2d),
                        GEOMETRY_FACTORY);
        SimpleFeatureType ft = DataUtilities.createType("test", "geom:Point:srid=4326");
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(midPoint);
        SimpleFeature pointFeature = fb.buildFeature(null);
        SimpleFeatureCollection collection = DataUtilities.collection(pointFeature);

        // load the point style
        mc.addLayer(new FeatureLayer(collection, pointStyle));

        // prepare the graphics for the streaming renderer and paint
        BufferedImage blended =
                new BufferedImage(
                        referenceImage.getWidth(),
                        referenceImage.getWidth(),
                        BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = blended.createGraphics();
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        sr.paint(
                graphics,
                new Rectangle(0, 0, referenceImage.getWidth(), referenceImage.getHeight()),
                ReferencedEnvelope.reference(first.getEnvelope()));
        graphics.dispose();
        mc.dispose();
        return blended;
    }

    private BufferedImage composeLine(GridCoverage2D first, Style lineStyle)
            throws SchemaException, IOException {
        // build the map content
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        // first layer is the usual coverage
        Style baseStyle = sb.createStyle(sb.createRasterSymbolizer());
        mc.addLayer(new GridCoverageLayer(first, baseStyle));

        // second layer is a line in the middle of the map
        RenderedImage referenceImage = first.getRenderedImage();
        LineString midLine =
                new LineString(
                        new LiteCoordinateSequence(
                                0,
                                referenceImage.getHeight() / 2d,
                                referenceImage.getWidth(),
                                referenceImage.getHeight() / 2d),
                        GEOMETRY_FACTORY);
        SimpleFeatureType ft = DataUtilities.createType("test", "geom:LineString:srid=4326");
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(midLine);
        SimpleFeature pointFeature = fb.buildFeature(null);
        SimpleFeatureCollection collection = DataUtilities.collection(pointFeature);

        // load the point style
        mc.addLayer(new FeatureLayer(collection, lineStyle));

        // prepare the graphics for the streaming renderer and paint
        BufferedImage blended =
                new BufferedImage(
                        referenceImage.getWidth(),
                        referenceImage.getWidth(),
                        BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = blended.createGraphics();
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        sr.paint(
                graphics,
                new Rectangle(0, 0, referenceImage.getWidth(), referenceImage.getHeight()),
                ReferencedEnvelope.reference(first.getEnvelope()));
        graphics.dispose();
        mc.dispose();
        return blended;
    }

    private BufferedImage composePolygon(GridCoverage2D first, Style polygonStyle)
            throws SchemaException, IOException {
        // build the map content
        MapContent mc = new MapContent();
        StyleBuilder sb = new StyleBuilder();
        // first layer is the usual coverage
        Style baseStyle = sb.createStyle(sb.createRasterSymbolizer());
        mc.addLayer(new GridCoverageLayer(first, baseStyle));

        // second layer is a polygon covering the whole map
        RenderedImage referenceImage = first.getRenderedImage();
        int w = referenceImage.getWidth();
        int h = referenceImage.getHeight();
        LinearRing shell =
                new LinearRing(
                        new LiteCoordinateSequence(0, 0, 0, h, w, h, w, 0, 0, 0), GEOMETRY_FACTORY);
        Polygon polygon = new Polygon(shell, null, GEOMETRY_FACTORY);
        SimpleFeatureType ft = DataUtilities.createType("test", "geom:Polygon:srid=4326");
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(polygon);
        SimpleFeature pointFeature = fb.buildFeature(null);
        SimpleFeatureCollection collection = DataUtilities.collection(pointFeature);

        // load the point style
        mc.addLayer(new FeatureLayer(collection, polygonStyle));

        // prepare the graphics for the streaming renderer and paint
        BufferedImage blended =
                new BufferedImage(
                        referenceImage.getWidth(),
                        referenceImage.getWidth(),
                        BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = blended.createGraphics();
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        sr.paint(
                graphics,
                new Rectangle(0, 0, referenceImage.getWidth(), referenceImage.getHeight()),
                ReferencedEnvelope.reference(first.getEnvelope()));
        graphics.dispose();
        mc.dispose();
        return blended;
    }

    private Style applyCompositeOnFirstSymbolizer(String styleName) throws IOException {
        Style style = RendererBaseTest.loadStyle(StreamingRendererCompositeTest.class, styleName);
        Symbolizer symbolizer =
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        symbolizer.getOptions().put(FeatureTypeStyle.COMPOSITE, composite);
        return style;
    }
}
