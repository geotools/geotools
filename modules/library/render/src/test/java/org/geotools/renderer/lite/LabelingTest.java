/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.test.TestData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests the StreamingRenderer labelling algorithms
 *
 * @author jeichar
 * @since 0.9.0
 * @source $URL$
 */
public class LabelingTest extends TestCase {

    private long timout = 3000;
    private static final int CENTERX = 130;
    private static final int CENTERY = 40;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        FontCache.getDefaultInstance()
                .registerFont(
                        java.awt.Font.createFont(
                                java.awt.Font.TRUETYPE_FONT,
                                TestData.getResource(this, "Vera.ttf").openStream()));
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPointLabeling() throws Exception {
        FeatureCollection collection = createPointFeatureCollection();
        Style style = loadStyle("PointStyle.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);
        ReferencedEnvelope env = map.getLayerBounds();
        int boundary = 10;
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);
        RendererBaseTest.showRender("testPointLabeling", renderer, timout, env);
    }

    private Style loadStyle(String sldFilename) throws IOException {
        StyleFactory factory = StyleFactoryFinder.createStyleFactory();

        java.net.URL surl = TestData.getResource(this, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];
        return style;
    }

    private SimpleFeatureCollection createPointFeatureCollection() throws Exception {
        AttributeDescriptor[] types = new AttributeDescriptor[2];

        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(createPointFeature(2, 2, "LongLabel1", crs, geomFac, types));
        data.addFeature(createPointFeature(4, 4, "LongLabel2", crs, geomFac, types));
        data.addFeature(createPointFeature(0, 4, "LongLabel3", crs, geomFac, types));
        //        data.addFeature(createPointFeature(2,0,"Label4",crs, geomFac, types));
        data.addFeature(createPointFeature(2, 6, "LongLabel6", crs, geomFac, types));

        return data.getFeatureSource(Rendering2DTest.POINT).getFeatures();
    }

    private SimpleFeature createPointFeature(
            int x,
            int y,
            String name,
            CoordinateReferenceSystem crs,
            GeometryFactory geomFac,
            AttributeDescriptor[] types)
            throws Exception {
        Coordinate c = new Coordinate(x, y);
        Point point = geomFac.createPoint(c);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        if (crs != null) builder.add("point", point.getClass(), crs);
        else builder.add("centre", point.getClass());
        builder.add("name", String.class);
        builder.setName("pointfeature");
        SimpleFeatureType type = builder.buildFeatureType();
        return SimpleFeatureBuilder.build(type, new Object[] {point, name}, null);
    }

    public void testLineLabeling() throws Exception {
        FeatureCollection collection = createLineFeatureCollection();
        Style style = loadStyle("LineStyle.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);
        ReferencedEnvelope env = map.getLayerBounds();
        int boundary = 10;
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);

        RendererBaseTest.showRender("testLineLabeling", renderer, timout, env);
    }

    /**
     * This test tests for custom unit of measurement features, where the feature size translates to
     * something 0 < size < 1. in this case, an infinite loop used to occur, see
     * https://jira.codehaus.org/browse/GEOT-4284
     *
     * @throws Exception
     */
    public void testLineLabelingUom() throws Exception {
        FeatureCollection collection = createLineFeatureCollection();
        Style style = loadStyle("LineStyleUom.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);
        ReferencedEnvelope env = map.getLayerBounds();
        int boundary = 10000;
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);

        RendererBaseTest.showRender("testLineLabeling", renderer, timout, env);
    }

    /**
     * Checks we won't label a feature with a sharp U turn, when using a large font
     *
     * @throws Exception
     */
    public void testLineLabelingSharpTurn() throws Exception {
        FeatureCollection collection = createTightUTurnLineCollection();
        Style style = loadStyle("LineStyleLarge.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setContext(map);
        int boundary = 2;
        ReferencedEnvelope env = map.getLayerBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);

        BufferedImage image = RendererBaseTest.showRender("U turn label", renderer, 1000, env);
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/lineLabelSharpTurn.png";
        // small tolerance
        ImageAssert.assertEquals(new File(refPath), image, 100);
    }

    /**
     * Checks we won't label a feature with a sharp U turn, when using a large font
     *
     * @throws Exception
     */
    public void testLineLabelingSharpTurn2() throws Exception {
        FeatureCollection collection = createTightUTurnLineCollection2();
        Style style = loadStyle("LineStyleLarge2.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setContext(map);
        double boundary = 2;
        ReferencedEnvelope env = map.getLayerBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);

        BufferedImage image = RendererBaseTest.showRender("U turn label", renderer, 1000, env);
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/lineLabelSharpTurn2.png";
        // small tolerance
        ImageAssert.assertEquals(new File(refPath), image, 300);
    }

    private SimpleFeatureCollection createLineFeatureCollection() throws Exception {
        AttributeDescriptor[] types = new AttributeDescriptor[2];

        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(createLineFeature("LongLabel1", crs, geomFac, 10, 0, 0, 10));
        data.addFeature(createLineFeature("LongLabel2", crs, geomFac, 10, 10, 0, 0));
        //        data.addFeature(createPointFeature(0,2,"LongLabel3",crs, geomFac, types));
        //        data.addFeature(createPointFeature(2,0,"Label4",crs, geomFac, types));
        //        data.addFeature(createPointFeature(0,4,"LongLabel6",crs, geomFac, types));

        return data.getFeatureSource(Rendering2DTest.LINE).getFeatures();
    }

    private SimpleFeatureCollection createTightUTurnLineCollection() throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(
                createLineFeature(
                        "TheUTurnLabel", crs, geomFac, 1, 2, 8.7, 2, 9, 2.1, 8.7, 2.2, 1, 2.2));

        return data.getFeatureSource(Rendering2DTest.LINE).getFeatures();
    }

    private SimpleFeatureCollection createTightUTurnLineCollection2() throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(createLineFeature("TheUTurnLabel", crs, geomFac, 1, 2, 9, 2, 7, 2.2));

        return data.getFeatureSource(Rendering2DTest.LINE).getFeatures();
    }

    private SimpleFeature createLineFeature(
            String name,
            CoordinateReferenceSystem crs,
            GeometryFactory geomFac,
            double... ordinates)
            throws Exception {
        LiteCoordinateSequence cs = new LiteCoordinateSequence(ordinates);
        LineString line = geomFac.createLineString(cs);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        if (crs != null) builder.add("line", line.getClass(), crs);
        else builder.add("centre", line.getClass());
        builder.add("name", String.class);
        builder.setName(Rendering2DTest.LINE);
        SimpleFeatureType type = builder.buildFeatureType();
        return SimpleFeatureBuilder.build(type, new Object[] {line, name}, null);
    }

    public void testPolyLabeling() throws Exception {
        FeatureCollection collection = createPolyFeatureCollection();
        Style style = loadStyle("PolyStyle.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);
        ReferencedEnvelope env = map.getLayerBounds();
        int boundary = 10;
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);
        RendererBaseTest.showRender("testPolyLabeling", renderer, timout, env);
    }

    private SimpleFeatureCollection createPolyFeatureCollection() throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(
                createPolyFeature(
                        CENTERX + 5,
                        CENTERY + 0,
                        CENTERX + 10,
                        CENTERY + 10,
                        "LongLabel1",
                        crs,
                        geomFac));
        data.addFeature(
                createPolyFeature(
                        CENTERX + 0,
                        CENTERY + 0,
                        CENTERX + 10,
                        CENTERY + 10,
                        "LongLabel2",
                        crs,
                        geomFac));

        return data.getFeatureSource(Rendering2DTest.POLYGON).getFeatures();
    }

    private SimpleFeature createPolyFeature(
            int startx,
            int starty,
            int width,
            int height,
            String name,
            CoordinateReferenceSystem crs,
            GeometryFactory geomFac)
            throws Exception {
        Coordinate[] c =
                new Coordinate[] {
                    new Coordinate(startx, starty),
                    new Coordinate(startx + width, starty),
                    new Coordinate(startx + width, starty + height),
                    new Coordinate(startx, starty),
                };
        LinearRing line = geomFac.createLinearRing(c);
        Polygon poly = geomFac.createPolygon(line, null);

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        if (crs != null) builder.add("polygon", poly.getClass(), crs);
        else builder.add("centre", line.getClass());
        builder.add("name", String.class);
        builder.setName(Rendering2DTest.POLYGON);
        SimpleFeatureType type = builder.buildFeatureType();
        return SimpleFeatureBuilder.build(type, new Object[] {poly, name}, null);
    }
}
