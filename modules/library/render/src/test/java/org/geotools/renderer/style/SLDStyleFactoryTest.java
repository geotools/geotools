/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.collections4.IteratorUtils;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.style.SLDStyleFactory.SymbolizerKey;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author jamesm */
public class SLDStyleFactoryTest {

    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    SLDStyleFactory sld = new SLDStyleFactory();
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    NumberRange<Integer> range = NumberRange.create(1, 1);

    SimpleFeatureType featureType;
    SimpleFeature feature;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("test");
        ftb.add("geom", Point.class);
        ftb.add("symb", String.class);
        ftb.add("icon", String.class);

        featureType = ftb.buildFeatureType();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(featureType);
        fb.set("geom", new GeometryFactory().createPoint(new Coordinate(0, 0)));
        fb.set("symb", "0xF054");
        fb.set("icon", "draw.png");
        feature = fb.buildFeature(null);
    }

    @After
    public void tearDown() throws Exception {
        sld.setVectorRenderingEnabled(false);
        MarkStyle2D.setMaxMarkSizeEnabled(false);
    }

    /** This test created from Render2DTest.testSimplePointRender */
    @Test
    public void testCreatePointStyle() {
        // The following is complex, and should be built from
        // an SLD document and not by hand
        PointSymbolizer pointsym = sf.createPointSymbolizer();
        pointsym.setGraphic(sf.getDefaultGraphic());
        NumberRange<Double> scaleRange = NumberRange.create(1.0, 50000.0);
        Style2D style = sld.createPointStyle(null, pointsym, scaleRange);
        Assert.assertNotNull(style);
    }

    @Test
    public void testCreateLineStyle() {
        LineSymbolizer ls = sf.createLineSymbolizer();
        ls.setPerpendicularOffset(ff.literal(5));
        NumberRange<Double> scaleRange = NumberRange.create(1.0, 50000.0);
        LineStyle2D style = (LineStyle2D) sld.createLineStyle(null, ls, scaleRange);
        Assert.assertNotNull(style);
        Assert.assertEquals(5, style.getPerpendicularOffset(), 0d);
    }

    /** Test of createPolygonStyle method, of class org.geotools.renderer.style.SLDStyleFactory. */
    @Test
    public void testCreateIncompletePolygonStyle() {

        // full symbolizer
        PolygonSymbolizer symb = sf.createPolygonSymbolizer();
        sld.createPolygonStyle(null, symb, range);
    }

    /** Test of createPointStyle method, of class org.geotools.renderer.style.SLDStyleFactory. */
    @Test
    public void testCreateCompletePointStyle() {
        // full symbolizer
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();

        myMark.setFill(sf.createFill(ff.literal("#ffff00")));
        symb.getGraphic().setSize(ff.literal(10));
        symb.getGraphic().graphicalSymbols().add(myMark);
        symb.getGraphic().setOpacity(ff.literal(1));
        symb.getGraphic().setRotation(ff.literal(0));
        sld.createPointStyle(null, symb, range);
    }

    @Test
    public void testCreateIncompletePointStyle() {
        // full symbolizer
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();

        symb.getGraphic().graphicalSymbols().add(myMark);

        sld.createPointStyle(null, symb, range);
    }

    @Test
    public void testCreateDynamicMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        final String ttfUrl = "ttf://Serif#${symb}";
        myMark.setWellKnownName(ff.literal(ttfUrl));
        symb.getGraphic().graphicalSymbols().add(myMark);

        MarkStyle2D ms = (MarkStyle2D) sld.createStyle(feature, symb, range);
        Assert.assertNotNull(ms.getShape());
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        Assert.assertTrue(sld.dynamicSymbolizers.containsKey(key));
        Shape expected =
                new TTFMarkFactory().getShape(null, ff.literal("ttf://Serif#0xF054"), feature);

        // no general path equality implemented, we have to check manually
        PathIterator piExpected = expected.getPathIterator(new AffineTransform());
        PathIterator pi = ms.getShape().getPathIterator(new AffineTransform());
        double[] coordsExpected = new double[6];
        double[] coords = new double[6];
        Assert.assertEquals(piExpected.getWindingRule(), pi.getWindingRule());
        while (!piExpected.isDone()) {
            Assert.assertFalse(pi.isDone());
            piExpected.currentSegment(coordsExpected);
            pi.currentSegment(coords);
            Assert.assertEquals(coordsExpected[0], coords[0], 0.00001);
            Assert.assertEquals(coordsExpected[1], coords[1], 0.00001);
            piExpected.next();
            pi.next();
        }
        Assert.assertTrue(pi.isDone());
    }

    @Test
    public void testCreateDynamicExternalGraphics() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);

        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        Assert.assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage img = gs.getImage();
        BufferedImage expected =
                ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        Assert.assertEquals(expected.getHeight(), img.getHeight());
        Assert.assertEquals(expected.getWidth(), img.getWidth());
        // the two images are equal, but they have different color models due to the
        // different ways they have been loaded
    }

    @Test
    public void testCreateDynamicExternalGraphicsVector() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);
        sld.setVectorRenderingEnabled(true);

        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        Assert.assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage expected =
                ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        Assert.assertEquals(expected.getHeight(), gs.getImage().getHeight());
        Assert.assertEquals(expected.getWidth(), gs.getImage().getWidth());
    }

    @Test
    public void testDefaultSizeExternalGraphic() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);

        GraphicStyle2D gs = (GraphicStyle2D) sld.createPointStyle(feature, symb, range);
        BufferedImage img = gs.getImage();
        Assert.assertEquals(64, img.getHeight());
        Assert.assertEquals(64, img.getWidth());
    }

    @Test
    public void testCreateDynamicExternalFormat() throws Exception {
        feature.setAttribute("symb", "image/png");
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "${icon}", "${symb}");
        symb.getGraphic().graphicalSymbols().add(eg);

        GraphicStyle2D gs = (GraphicStyle2D) sld.createStyle(feature, symb, range);
        // make sure the style has been recognized as dynamic
        SymbolizerKey key = new SymbolizerKey(symb, range);
        Assert.assertTrue(sld.dynamicSymbolizers.containsKey(key));

        BufferedImage img = gs.getImage();
        BufferedImage expected =
                ImageIO.read(StreamingRenderer.class.getResource("test-data/draw.png"));
        Assert.assertEquals(expected.getHeight(), img.getHeight());
        Assert.assertEquals(expected.getWidth(), img.getWidth());
        // the two images are equal, but they have different color models due to the
        // different ways they have been loaded
    }

    @Test
    public void testResizeExternalGraphic() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);
        symb.getGraphic().setSize(ff.literal(20));

        GraphicStyle2D gs = (GraphicStyle2D) sld.createPointStyle(feature, symb, range);
        BufferedImage img = gs.getImage();
        Assert.assertEquals(20, img.getHeight());
        Assert.assertEquals(20, img.getWidth());
    }

    @Test
    public void testNonExistingExternalGraphic() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "iAmNotThere.png", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);

        // test normal fallback behavior, graphics not found implies using a default mark
        Style2D style2D = sld.createPointStyle(feature, symb, range);
        assertThat(style2D, CoreMatchers.instanceOf(MarkStyle2D.class));
        MarkStyle2D mark = (MarkStyle2D) style2D;
        Assert.assertEquals(Color.GRAY, mark.getFill());
    }

    @Test
    public void testNonExistingExternalGraphicNoFallback() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "iAmNotThere.png", "image/png");
        symb.getGraphic().graphicalSymbols().add(eg);
        symb.getOptions().put(PointSymbolizer.FALLBACK_ON_DEFAULT_MARK, "false");

        // fallback has been disabled
        Assert.assertNull(sld.createPointStyle(feature, symb, range));
    }

    @Test
    public void testResizeGraphicFill() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/");
        PolygonSymbolizer symb = sf.createPolygonSymbolizer();
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        Graphic g =
                sf.createGraphic(
                        new ExternalGraphic[] {eg}, null, null, null, ff.literal(20), null);
        Fill fill = sf.createFill(null, null, null, g);
        symb.setFill(fill);

        PolygonStyle2D ps = sld.createPolygonStyle(feature, symb, range);
        Assert.assertTrue(ps.getFill() instanceof TexturePaint);
        TexturePaint paint = (TexturePaint) ps.getFill();
        Assert.assertEquals(20, paint.getImage().getWidth());
    }

    @Test
    public void testDefaultSizeMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        symb.getGraphic().graphicalSymbols().add(myMark);

        MarkStyle2D ms = (MarkStyle2D) sld.createPointStyle(feature, symb, range);
        Assert.assertEquals(16.0, ms.getSize(), 0d);
    }

    @Test
    public void testDefaultExpressionSizeMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        symb.getGraphic().setSize(ff.multiply(ff.property("expression/nil"), ff.literal(1.0)));
        symb.getGraphic().graphicalSymbols().add(myMark);

        MarkStyle2D ms = (MarkStyle2D) sld.createPointStyle(feature, symb, range);
        Assert.assertEquals(16.0, ms.getSize(), 0d);
    }

    @Test
    public void testDefaultLineSymbolizerWithColor() throws Exception {
        LineSymbolizer symb = sf.createLineSymbolizer();
        symb.setStroke(sf.createStroke(ff.literal("#0000FF"), ff.literal(1.0)));
        symb.setPerpendicularOffset(ff.literal(10));

        Style2D s = sld.createLineStyle(feature, symb, range);
        Assert.assertNotNull(s);

        DynamicLineStyle2D s2 =
                (DynamicLineStyle2D) sld.createDynamicLineStyle(feature, symb, range);
        Assert.assertNotNull(s2);
        Assert.assertEquals(Color.BLUE, s2.getContour());
        Assert.assertNotNull(s2.getStroke());
        Assert.assertEquals(10, s2.getPerpendicularOffset(), 0d);
    }

    @Test
    public void testTexturePaintNoSize() throws Exception {
        PolygonSymbolizer symb = sf.createPolygonSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        org.geotools.styling.Fill fill = sf.createFill(null);
        fill.setGraphicFill(sf.createGraphic(null, new Mark[] {myMark}, null, null, null, null));
        symb.setFill(fill);

        PolygonStyle2D ps = sld.createPolygonStyle(feature, symb, range);
        Assert.assertTrue(ps.getFill() instanceof TexturePaint);
    }

    @Test
    public void testUnknownFont() throws Exception {
        TextSymbolizer ts = sf.createTextSymbolizer();
        ts.setFill(sf.createFill(null));
        Font font =
                sf.createFont(
                        ff.literal("notExistingFont"),
                        ff.literal("italic"),
                        ff.literal("bold"),
                        ff.literal(20));
        ts.setFont(font);

        TextStyle2D tsd = (TextStyle2D) sld.createTextStyle(feature, ts, range);
        Assert.assertEquals(20, tsd.getFont().getSize());
        Assert.assertEquals(java.awt.Font.ITALIC | java.awt.Font.BOLD, tsd.getFont().getStyle());
        Assert.assertEquals("Serif", tsd.getFont().getName());

        Assert.assertEquals(0.0, tsd.getAnchorX(), 0.0);
        Assert.assertEquals(0.5, tsd.getAnchorY(), 0.0);
    }

    @Test
    public void testAlternativeMarkSizeCalculation() {
        MarkStyle2D ms = new MarkStyle2D();
        ms.setSize(1);
        GeneralPath gp = new GeneralPath();
        gp.moveTo(500, 0);
        gp.lineTo(-500, 0);
        gp.moveTo(200, -275);
        gp.lineTo(200, 275);
        ms.setShape(gp);

        Shape shape = ms.getTransformedShape(0, 0);

        Rectangle2D rect = shape.getBounds2D();
        Assert.assertEquals(1.0, rect.getHeight(), 0.0001);
        Assert.assertEquals(1000.0 / 550.0, rect.getWidth(), 0.0001);

        MarkStyle2D.setMaxMarkSizeEnabled(true);
        shape = ms.getTransformedShape(0, 0);

        rect = shape.getBounds2D();
        Assert.assertEquals(550.0 / 1000.0, rect.getHeight(), 0.0001);
        Assert.assertEquals(1.0, rect.getWidth(), 0.0001);
    }

    @Test
    public void testMarkSizeCalculation() throws Exception {
        Assert.assertFalse(MarkStyle2D.isMaxMarkSizeEnabled());

        PointSymbolizer symb = sf.createPointSymbolizer();
        Mark myMark = sf.createMark();
        myMark.setWellKnownName(ff.literal("square"));
        symb.getGraphic().graphicalSymbols().add(myMark);
        sld.createPointStyle(feature, symb, range);
        Assert.assertFalse(MarkStyle2D.isMaxMarkSizeEnabled());

        MarkStyle2D.setMaxMarkSizeEnabled(true);
        sld.createPointStyle(feature, symb, range);
        Assert.assertTrue(MarkStyle2D.isMaxMarkSizeEnabled());
    }

    @Test
    public void testFallbackGraphicMark() throws Exception {
        PointSymbolizer symb = sf.createPointSymbolizer();
        ExternalGraphic eg =
                sf.createExternalGraphic("http://foo.com/invalid_or_missing_image_url", null);
        symb.getGraphic().graphicalSymbols().add(eg);

        Style2D icon = sld.createPointStyle(feature, symb, range);
        Assert.assertNotNull(icon);
    }

    @Test
    public void testSortBySingleAscending() throws Exception {
        checkSortByParsing("z", ff.sort("z", SortOrder.ASCENDING));
    }

    @Test
    public void testSortByTwoPropertiesAscending() throws Exception {
        checkSortByParsing(
                "cat ,    name",
                ff.sort("cat", SortOrder.ASCENDING),
                ff.sort("name", SortOrder.ASCENDING));
    }

    @Test
    public void testSortBySingleDescending() throws Exception {
        checkSortByParsing("cat D   ", ff.sort("cat", SortOrder.DESCENDING));
    }

    @Test
    public void testSortByMixed() throws Exception {
        checkSortByParsing(
                "cat D,name A,z D",
                ff.sort("cat", SortOrder.DESCENDING),
                ff.sort("name", SortOrder.ASCENDING),
                ff.sort("z", SortOrder.DESCENDING));
    }

    private void checkSortByParsing(String sortBySpec, SortBy... expected) {
        Map<String, String> options = new HashMap<>();
        options.put(FeatureTypeStyle.SORT_BY, sortBySpec);
        SortBy[] sortBy = SLDStyleFactory.getSortBy(options);
        assertArrayEquals(expected, sortBy);
    }

    @Test
    public void testKerningOnByDefault() throws Exception {
        TextSymbolizer ts = sf.createTextSymbolizer();
        ts.setFill(sf.createFill(null));
        Font font =
                sf.createFont(
                        ff.literal("Serif"),
                        ff.literal("italic"),
                        ff.literal("bold"),
                        ff.literal(20));
        ts.setFont(font);

        TextStyle2D tsd = (TextStyle2D) sld.createTextStyle(feature, ts, range);
        Assert.assertEquals(20, tsd.getFont().getSize());
        Map<TextAttribute, ?> attributes = tsd.getFont().getAttributes();
        Object kerningValue = attributes.get(TextAttribute.KERNING);
        Assert.assertEquals(TextAttribute.KERNING_ON, kerningValue);
    }

    @Test
    public void testKerningOffByDefault() throws Exception {
        TextSymbolizer ts = sf.createTextSymbolizer();
        ts.setFill(sf.createFill(null));
        Font font =
                sf.createFont(
                        ff.literal("Serif"),
                        ff.literal("italic"),
                        ff.literal("bold"),
                        ff.literal(20));
        ts.setFont(font);
        ts.getOptions().put(TextSymbolizer.KERNING_KEY, "false");

        TextStyle2D tsd = (TextStyle2D) sld.createTextStyle(feature, ts, range);
        Assert.assertEquals(20, tsd.getFont().getSize());
        Map<TextAttribute, ?> attributes = tsd.getFont().getAttributes();
        Object kerningValue = attributes.get(TextAttribute.KERNING);
        Assert.assertNull(kerningValue);
    }

    /**
     * A very large symbol can cause an Integer overflow in the renderer. The SLDFactory should give
     * a warning instead and try again with out meta tiling.
     */
    @Test
    public void testGEOT5878() {
        String[] wkt = {
            "Polygon ((6438348.98000000044703484 4962869.70000000018626451, 6438348.88999999966472387 4962867.28000000026077032, 6438343.53000000026077032 4962867.28000000026077032, 6438343.53000000026077032 4962869.76999999955296516, 6438343.61000000033527613 4962870.17999999970197678, 6438348.90000000037252903 4962870.0400000000372529, 6438348.98000000044703484 4962869.70000000018626451))",
            "Polygon ((6438339.7099999999627471 4962870.38999999966472387, 6438339.94000000040978193 4962873.86000000033527613, 6438346.65000000037252903 4962873.61000000033527613, 6438346.62999999988824129 4962870.38999999966472387, 6438339.7099999999627471 4962870.38999999966472387))"
        };
        double naglib = 1.5707964;
        // build two features
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("test");
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode("EPSG:31276");
        } catch (FactoryException e1) {
            Assert.fail(e1.getMessage());
        }
        ftb.add("the_geom", Polygon.class, crs);
        ftb.add("Nagib", Double.class);
        SimpleFeatureType schema = ftb.buildFeatureType();
        WKTReader reader = new WKTReader();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        List<SimpleFeature> features = new ArrayList<>();
        for (String w : wkt) {
            Geometry geom = null;
            try {
                geom = reader.read(w);
            } catch (ParseException e) {
                Assert.fail(e.getMessage());
            }
            fb.set("the_geom", geom);
            fb.set("Nagib", naglib);
            features.add(fb.buildFeature(null));
        }
        StyleBuilder sb = new StyleBuilder();
        Mark mark =
                sb.createMark(
                        "wkt://LINESTRING(0 0, ${sin(Nagib) * 20000} ${cos(Nagib) * 20000} )",
                        Color.red);
        Graphic graphic = sb.createGraphic(null, mark, null);
        graphic.setSize(ff.literal("40px"));
        Fill fill = sf.createFill(null, null, null, graphic);
        PolygonSymbolizer symb = sb.createPolygonSymbolizer();
        symb.setFill(fill);

        Style style = sb.createStyle();
        style.featureTypeStyles().add(sb.createFeatureTypeStyle(symb));
        Layer layer = new FeatureLayer(DataUtilities.collection(features), style);
        MapContent content = new MapContent();
        content.addLayer(layer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.addRenderListener(
                new RenderListener() {

                    @Override
                    public void featureRenderer(SimpleFeature feature) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void errorOccurred(Exception e) {
                        Assert.fail("an error occured");
                    }
                });
        renderer.setMapContent(content);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Rectangle paintArea = new Rectangle(0, 0, 100, 100);

        renderer.paint(
                img.createGraphics(),
                paintArea,
                layer.getBounds(),
                RendererUtilities.worldToScreenTransform(layer.getBounds(), paintArea));
    }

    @Test
    public void testDashArrayZero() throws Exception {
        LineSymbolizer ls = sf.createLineSymbolizer();
        Stroke stroke = sf.createStroke(ff.literal("red"), ff.literal(1));
        Expression nonObviousZero = ff.subtract(ff.literal(10), ff.literal(10));
        stroke.setDashArray(Arrays.asList(nonObviousZero, nonObviousZero, nonObviousZero));
        ls.setStroke(stroke);

        // no exception, the dash array gets ignored
        LineStyle2D ls2d = (LineStyle2D) sld.createLineStyle(feature, ls, range);
        assertThat(ls2d.getStroke(), CoreMatchers.instanceOf(BasicStroke.class));
        BasicStroke bs = (BasicStroke) ls2d.getStroke();
        Assert.assertNull(bs.getDashArray());
    }

    @Test
    public void testOptimizedMarkFactoryHints() {
        // create a list with two mark factories identifiers
        List<String> list = Arrays.asList("WellKnownMarkFactory", "WKTMarkFactory");
        Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(DynamicSymbolFactoryFinder.MARK_FACTORY_FILTER, new MarkFactoryListPredicate(list));
        map.put(DynamicSymbolFactoryFinder.MARK_FACTORY_ORDER, new MarkFactoryListComparator(list));

        Hints hints = new Hints(map);

        List<MarkFactory> factories =
                IteratorUtils.toList(DynamicSymbolFactoryFinder.getMarkFactories(hints));
        assertEquals(2, factories.size());
        List<String> result =
                factories.stream()
                        .map(mf -> mf.getClass().getSimpleName())
                        .collect(Collectors.toList());
        assertEquals(list, result);
    }
}
