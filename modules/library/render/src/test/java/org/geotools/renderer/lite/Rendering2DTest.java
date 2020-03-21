/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/** @author jamesm */
public class Rendering2DTest extends TestCase {

    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Rendering2DTest.class);

    private final int xCenter = -74;

    private final int yCenter = 41;

    static final String LINE = "linefeature";

    static final String POLYGON = "polygonfeature";

    static final String POINT = "pointfeature";

    static final String RING = "ringfeature";

    static final String COLLECTION = "collfeature";

    protected static final Map rendererHints = new HashMap();

    protected static final FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    {
        rendererHints.put("optimizedDataLoadingEnabled", Boolean.valueOf(true));
    }

    public Rendering2DTest(java.lang.String testName) {
        super(testName);
    }

    Style loadTestStyle() throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        URL surl = TestData.getResource(this, "test-sld.xml");
        SLDParser stylereader = new SLDParser(factory, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        UserLayer layer = (UserLayer) sld.getStyledLayers()[0];
        return layer.getUserStyles()[0];
    }

    Style createTestStyle() throws IllegalFilterException {
        StyleFactory sFac = CommonFactoryFinder.getStyleFactory(null);
        // The following is complex, and should be built from
        // an SLD document and not by hand
        PointSymbolizer pointsym = sFac.createPointSymbolizer();
        pointsym.setGraphic(sFac.getDefaultGraphic());

        Rule rule = sFac.createRule();
        rule.symbolizers().add(polysym(sFac));
        FeatureTypeStyle fts = sFac.createFeatureTypeStyle(new Rule[] {rule});
        fts.featureTypeNames().add(new NameImpl("polygonfeature"));

        Rule rule1 = sFac.createRule();
        rule.symbolizers().add(polysym(sFac));
        FeatureTypeStyle fts1 = sFac.createFeatureTypeStyle(new Rule[] {rule1});
        fts1.featureTypeNames().add(new NameImpl("polygonfeature"));

        Rule rule2 = sFac.createRule();
        rule2.symbolizers().add(linesym(sFac));
        FeatureTypeStyle fts2 = sFac.createFeatureTypeStyle();
        fts2.rules().add(rule2);
        fts2.featureTypeNames().add(new NameImpl("linefeature"));

        Rule rule3 = sFac.createRule();
        rule3.symbolizers().add(pointsym);
        FeatureTypeStyle fts3 = sFac.createFeatureTypeStyle();
        fts3.rules().add(rule3);
        fts3.featureTypeNames().add(new NameImpl("pointfeature"));

        Rule rule4 = sFac.createRule();
        rule4.symbolizers().addAll(Arrays.asList(polysym(sFac), linesym(sFac)));
        FeatureTypeStyle fts4 = sFac.createFeatureTypeStyle();
        fts4.rules().add(rule4);
        fts4.featureTypeNames().add(new NameImpl("collFeature"));

        Rule rule5 = sFac.createRule();
        rule5.symbolizers().add(linesym(sFac));
        FeatureTypeStyle fts5 = sFac.createFeatureTypeStyle();
        fts5.rules().add(rule5);
        fts5.featureTypeNames().add(new NameImpl("ringFeature"));

        Style style = sFac.createStyle();
        style.featureTypeStyles().addAll(Arrays.asList(fts1, fts, fts2, fts3, fts4, fts5));
        return style;
    }

    private LineSymbolizer linesym(StyleFactory sFac) throws IllegalFilterException {
        LineSymbolizer linesym = sFac.createLineSymbolizer();
        Stroke myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.literal("#0000ff"));
        myStroke.setWidth(filterFactory.literal(Integer.valueOf(5)));
        LOGGER.fine("got new Stroke " + myStroke);
        linesym.setStroke(myStroke);
        return linesym;
    }

    private PolygonSymbolizer polysym(StyleFactory sFac) throws IllegalFilterException {
        Stroke myStroke;
        PolygonSymbolizer polysym = sFac.createPolygonSymbolizer();
        Fill myFill = sFac.getDefaultFill();
        myFill.setColor(filterFactory.literal("#ff0000"));
        polysym.setFill(myFill);
        myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.literal("#0000ff"));
        myStroke.setWidth(filterFactory.literal(Integer.valueOf(2)));
        polysym.setStroke(myStroke);
        return polysym;
    }

    private PolygonSymbolizer polysym1(StyleFactory sFac) throws IllegalFilterException {
        Stroke myStroke;
        PolygonSymbolizer polysym = sFac.createPolygonSymbolizer();
        Fill myFill = sFac.getDefaultFill();
        myFill.setColor(filterFactory.literal("#00ff00"));
        polysym.setFill(myFill);
        myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.literal("#00ff00"));
        myStroke.setWidth(filterFactory.literal(Integer.valueOf(2)));
        polysym.setStroke(myStroke);
        return polysym;
    }

    SimpleFeatureCollection createTestFeatureCollection(
            CoordinateReferenceSystem crs, String typeName) throws Exception {
        GeometryFactory geomFac = new GeometryFactory();
        return createTestFeatureCollection(crs, geomFac, typeName);
    }

    SimpleFeatureCollection createTestFeatureCollection(
            CoordinateReferenceSystem crs, GeometryFactory geomFac, String typeName)
            throws Exception {

        LineString line = makeSampleLineString(geomFac);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(LINE);
        if (crs != null) builder.add("collection", line.getClass(), crs);
        else builder.add("centerline", line.getClass());
        builder.add("name", String.class);
        SimpleFeatureType lineType = builder.buildFeatureType();
        SimpleFeature lineFeature =
                SimpleFeatureBuilder.build(lineType, new Object[] {line, "centerline"}, null);

        Polygon polygon = makeSamplePolygon(geomFac);
        builder.setName(POLYGON);
        if (crs != null) builder.add("collection", polygon.getClass(), crs);
        else builder.add("edge", polygon.getClass());
        builder.add("name", String.class);
        SimpleFeatureType polygonType = builder.buildFeatureType();
        SimpleFeature polygonFeature =
                SimpleFeatureBuilder.build(polygonType, new Object[] {polygon, "edge"}, null);

        Point point = makeSamplePoint(geomFac);
        builder.setName(POINT);
        if (crs != null) builder.add("collection", point.getClass(), crs);
        else builder.add("centre", point.getClass());
        builder.add("name", String.class);
        SimpleFeatureType pointType = builder.buildFeatureType();
        SimpleFeature pointFeature =
                SimpleFeatureBuilder.build(pointType, new Object[] {point, "centre"}, null);

        LinearRing ring = makeSampleLinearRing(geomFac);
        builder.setName(RING);
        if (crs != null) builder.add("collection", line.getClass(), crs);
        else builder.add("centerline", line.getClass());
        builder.add("name", String.class);
        SimpleFeatureType lrType = builder.buildFeatureType();
        SimpleFeature ringFeature =
                SimpleFeatureBuilder.build(lrType, new Object[] {ring, "centerline"}, null);

        GeometryCollection coll = makeSampleGeometryCollection(geomFac);
        builder.setName(COLLECTION);
        if (crs != null) builder.add("collection", coll.getClass(), crs);
        else builder.add("collection", coll.getClass());
        builder.add("name", String.class);
        SimpleFeatureType collType = builder.buildFeatureType();
        SimpleFeature collFeature =
                SimpleFeatureBuilder.build(collType, new Object[] {coll, "collection"}, null);

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(lineFeature);
        data.addFeature(polygonFeature);
        data.addFeature(pointFeature);
        data.addFeature(ringFeature);
        data.addFeature(collFeature);

        return data.getFeatureSource(typeName).getFeatures();
    }

    public void testSimplePolygonRender() throws Exception {

        LOGGER.finer("starting rendering2DTest");

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING FEATURES
        //
        // ////////////////////////////////////////////////////////////////////
        final SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, POLYGON);

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STYLE
        //
        // ////////////////////////////////////////////////////////////////////
        final Style style = createTestStyle();

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING MAP CONTEXT
        //
        // ////////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STREAMING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        renderer.setRendererHints(rendererHints);

        // ////////////////////////////////////////////////////////////////////
        //
        // SHOWING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        RendererBaseTest.showRender("testSimplePolygonRender", renderer, 1000, map.getMaxBounds());
    }

    // public void testRenderLoadedStyle() throws Exception {
    //
    // // same as the datasource test, load in some features into a table
    // System.err.println("starting RenderLoadedStyle");
    //
    // SimpleFeatureCollection ft = createTestFeatureCollection(null, POLYGON);
    // Style style = loadTestStyle();
    //
    // MapContent map = new DefaultMapContent();
    // map.addLayer(new FeatureLayer(ft, style));
    // LiteRenderer2 renderer = new LiteRenderer2(map);
    // Envelope env = map.getMaxBounds();
    // env = new Envelope(env.getMinX() - 20, env.getMaxX() + 20, env.getMinY()
    // - 20, env
    // .getMaxY() + 20);
    // showRender("RenderLoadedStyle", renderer, 5000, env);
    //
    // }

    public void testSimpleLineRender() throws Exception {

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING FEATURES
        //
        // ////////////////////////////////////////////////////////////////////
        final SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, LINE);

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STYLE
        //
        // ////////////////////////////////////////////////////////////////////
        final Style style = createTestStyle();

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING MAP CONTEXT
        //
        // ////////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STREAMING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        renderer.setRendererHints(rendererHints);

        // ////////////////////////////////////////////////////////////////////
        //
        // SHOWING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        ReferencedEnvelope env = map.getMaxBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - 20,
                        env.getMaxX() + 20,
                        env.getMinY() - 20,
                        env.getMaxY() + 20,
                        map.getCoordinateReferenceSystem());
        RendererBaseTest.showRender("testSimpleLineRender", renderer, 1000, env);
    }

    public void testSimplePointRender() throws Exception {

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING FEATURES
        //
        // ////////////////////////////////////////////////////////////////////
        final SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, POINT);

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STYLE
        //
        // ////////////////////////////////////////////////////////////////////
        final Style style = createTestStyle();

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING MAP CONTEXT
        //
        // ////////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // ////////////////////////////////////////////////////////////////////
        //
        // CREATING STREAMING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        renderer.setRendererHints(rendererHints);

        // ////////////////////////////////////////////////////////////////////
        //
        // SHOWING RENDERER
        //
        // ////////////////////////////////////////////////////////////////////
        ReferencedEnvelope env = map.getMaxBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - 20,
                        env.getMaxX() + 20,
                        env.getMinY() - 20,
                        env.getMaxY() + 20,
                        map.getCoordinateReferenceSystem());
        RendererBaseTest.showRender("testSimplePointRender", renderer, 1000, env);
    }

    public void testReprojectionWithPackedCoordinateSequence() throws Exception {

        // same as the datasource test, load in some features into a table
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("starting testLiteRender2").append("\n");

        // //
        //
        // Create test features
        //
        // //
        GeometryFactory geomFac =
                new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY);
        SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, geomFac, POLYGON);
        Style style = createTestStyle();

        // //
        //
        // Create the map context
        //
        // //
        MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // //
        //
        // Create the streaming renderer
        //
        // //
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        renderer.setRendererHints(rendererHints);

        // //
        //
        // Transform the area of interest
        //
        // //
        final CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"NAD83 / BC"
                                + "Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS"
                                + "1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
                                + "TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],"
                                + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                                + "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],"
                                + "AUTHORITY[\"EPSG\",\"4269\"]],"
                                + "PROJECTION[\"Albers_Conic_Equal_Area\"],"
                                + "PARAMETER[\"standard_parallel_1\",50],"
                                + "PARAMETER[\"standard_parallel_2\",58.5],"
                                + "PARAMETER[\"latitude_of_center\",45],"
                                + "PARAMETER[\"longitude_of_center\",-126],"
                                + "PARAMETER[\"false_easting\",1000000],"
                                + "PARAMETER[\"false_northing\",0],"
                                + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
                                + "AUTHORITY[\"EPSG\",\"3005\"]]");
        final MathTransform t = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs, true);

        // //
        //
        // Set the new AOI
        //
        // //
        final ReferencedEnvelope env = (ReferencedEnvelope) map.getMaxBounds();
        final ReferencedEnvelope bounds =
                new ReferencedEnvelope(JTS.transform(env, null, t, 10), crs);

        RendererBaseTest.showRender("testReprojection", renderer, 1000, bounds);

        LOGGER.finer(stringBuffer.toString());
    }

    public void testLineReprojection() throws Exception {
        // ///////////////////////////////////////////////////////////////////
        //
        // LOAD FEATURES
        //
        //
        // /////////////////////////////////////////////////////////////////
        LOGGER.finer("starting testLiteRender2");
        final GeometryFactory geomFac =
                new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY);
        final SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, geomFac, LINE);
        final Style style = createTestStyle();

        //
        // ///////////////////////////////////////////////////////////////////
        //
        // CREATE MAP CONTEXT AND RENDERER
        //
        //
        // /////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // ///////////////////////////////////////////////////////////////////
        //
        // CREATE A PROJECTED AOI
        //
        //
        // /////////////////////////////////////////////////////////////////
        final CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"NAD83 / BC"
                                + "Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS"
                                + "1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
                                + "TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],"
                                + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                                + "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],"
                                + "AUTHORITY[\"EPSG\",\"4269\"]],"
                                + "PROJECTION[\"Albers_Conic_Equal_Area\"],"
                                + "PARAMETER[\"standard_parallel_1\",50],"
                                + "PARAMETER[\"standard_parallel_2\",58.5],"
                                + "PARAMETER[\"latitude_of_center\",45],"
                                + "PARAMETER[\"longitude_of_center\",-126],"
                                + "PARAMETER[\"false_easting\",1000000],"
                                + "PARAMETER[\"false_northing\",0],"
                                + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
                                + "AUTHORITY[\"EPSG\",\"3005\"]]");

        // /////////////////////////////////////////////////////////////////
        //
        // CREATE MAP CONTEXT AND RENDERER
        //
        //
        // /////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setRendererHints(rendererHints);
        renderer.setMapContent(map);

        ReferencedEnvelope env = map.getMaxBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - 20,
                        env.getMaxX() + 20,
                        env.getMinY() - 20,
                        env.getMaxY() + 20,
                        DefaultGeographicCRS.WGS84);
        final ReferencedEnvelope newbounds = env.transform(crs, true);
        RendererBaseTest.showRender("testLineReprojection", renderer, 1000, newbounds);
    }

    public void testPointReprojection() throws Exception {

        // ///////////////////////////////////////////////////////////////////
        //
        // LOAD FEATURES
        //
        //
        // /////////////////////////////////////////////////////////////////
        LOGGER.finer("starting testLiteRender2");
        final GeometryFactory geomFac =
                new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY);
        final SimpleFeatureCollection ft =
                createTestFeatureCollection(DefaultGeographicCRS.WGS84, geomFac, POINT);
        final Style style = createTestStyle();

        //
        // ///////////////////////////////////////////////////////////////////
        //
        // CREATE MAP CONTEXT AND RENDERER
        //
        //
        // /////////////////////////////////////////////////////////////////
        final MapContent map = new MapContent();
        map.addLayer(new FeatureLayer(ft, style));

        // ///////////////////////////////////////////////////////////////////
        //
        // CREATE A PROJECTED AOI
        //
        //
        // /////////////////////////////////////////////////////////////////
        // //
        //
        // Transform the area of interest
        //
        // //
        final CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"NAD83 / BC"
                                + "Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS"
                                + "1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
                                + "TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],"
                                + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                                + "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],"
                                + "AUTHORITY[\"EPSG\",\"4269\"]],"
                                + "PROJECTION[\"Albers_Conic_Equal_Area\"],"
                                + "PARAMETER[\"standard_parallel_1\",50],"
                                + "PARAMETER[\"standard_parallel_2\",58.5],"
                                + "PARAMETER[\"latitude_of_center\",45],"
                                + "PARAMETER[\"longitude_of_center\",-126],"
                                + "PARAMETER[\"false_easting\",1000000],"
                                + "PARAMETER[\"false_northing\",0],"
                                + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
                                + "AUTHORITY[\"EPSG\",\"3005\"]]");

        // /////////////////////////////////////////////////////////////////
        //
        // CREATE MAP CONTEXT AND RENDERER
        //
        //
        // /////////////////////////////////////////////////////////////////
        final StreamingRenderer renderer = new StreamingRenderer();
        renderer.setRendererHints(rendererHints);
        renderer.setMapContent(map);

        ReferencedEnvelope env = map.getMaxBounds();
        env =
                new ReferencedEnvelope(
                        env.getMinX() - 20,
                        env.getMaxX() + 20,
                        env.getMinY() - 20,
                        env.getMaxY() + 20,
                        DefaultGeographicCRS.WGS84);
        final ReferencedEnvelope newbounds = env.transform(crs, true);
        RendererBaseTest.showRender("testPointReprojection", renderer, 1000, newbounds);
    }

    /**
     * Tests the layer definition query behavior as implemented by StreamingRenderer.
     *
     * <p>This method relies on the features created on createTestFeatureCollection()
     */
    public void testDefinitionQueryProcessing() throws Exception {

        // LOGGER.info("starting definition query test");
        // final SimpleFeatureCollection ft = createTestDefQueryFeatureCollection();
        // final Style style = createDefQueryTestStyle();
        // FeatureResults results;
        // Envelope envelope = ft.getBounds();
        //
        // // we'll use this as the definition query for the layer
        // Query layerQuery;
        //
        // MapLayer layer = new DefaultMapLayer(ft, style);
        // MapContent map = new DefaultMapContent(new MapLayer[] { layer });
        // map.setAreaOfInterest(envelope, ft.getFeatureType()
        // .getDefaultGeometry().getCoordinateSystem());
        // StreamingRenderer renderer = new StreamingRenderer();
        // renderer.setMapContent(map);
        // renderer.setRendererHints(rendererHints);
        //
        // // this is the reader that StreamingRenderer obtains after applying
        // // the mixed filter to a given layer.
        // Filter filter = Filter.INCLUDE;
        // FilterFactory ffac = FilterFactoryFinder.createFilterFactory();
        //
        // // test maxFeatures, render just the first 2 features
        // layerQuery = new Query("querytest", filter, 2, null,
        // "handle");
        // layer.setQuery(layerQuery);
        //
        // results = renderer.queryLayer(layer,layer.getFeatureSource() ,
        // layer.getFeatureSource() .getSchema(), null, envelope,
        // DefaultGeographicCRS.WGS84, map.getCoordinateReferenceSystem(), null,
        // null);
        // assertEquals(2, results.getCount());
        // // just the 3 geometric atts should get be loaded
        // assertEquals(4, results.getSchema().getAttributeCount());
        //
        // RendererBaseTest.showRender("testDefinitionQuery1", renderer, 1000,
        // null);
        //
        // // test attribute based filter
        // FeatureType schema = ft.features().next().getFeatureType();
        // filter = ffac.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        // ((CompareFilter) filter).addLeftValue(ffac.createAttributeExpression(
        // schema, "id"));
        // ((CompareFilter) filter).addRightValue(ffac
        // .createLiteralExpression("ft1"));
        //
        // // note we include the "id" field in the layer query. Bad practice,
        // // since it goes
        // // against
        // // the performance gain of
        // // renderer.setOptimizedDataLoadingEnabled(true),
        // // but we should test it anyway
        // layerQuery = new Query("querytest", filter, Integer.MAX_VALUE,
        // new String[] { "id" }, "handle");
        // layer.setQuery(layerQuery);
        //
        // results = renderer.queryLayer(layer, null, envelope,
        // DefaultGeographicCRS.WGS84);
        // assertEquals(1, results.getCount());
        // // the 4 atts should be loaded since the definition query includes
        // "id"
        // assertEquals(4, results.getSchema().getAttributeCount());
        // // we can check this since we explicitly requested the "id"
        // attribute.
        // // If we not,
        // // it would be not loaded
        // String val = (String) results.reader().next().getAttribute("id");
        // assertEquals("ft1", val);
        //
        // RendererBaseTest.showRender("testDefinitionQuery2", renderer, 1000,
        // null);
        //
        // // try a bbox filter as definition query for the layer
        // filter = null;
        // GeometryFilter gfilter;
        // // contains the first 2 features
        // Envelope env = new Envelope(20, 130, 20, 130);
        // gfilter = ffac.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // gfilter
        // .addLeftGeometry(ffac
        // .createAttributeExpression(schema, "point"));
        // gfilter.addRightGeometry(ffac.createBBoxExpression(env));
        // filter = gfilter;
        //
        // gfilter = ffac.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // gfilter.addLeftGeometry(ffac.createAttributeExpression(schema,
        // "line"));
        // gfilter.addRightGeometry(ffac.createBBoxExpression(env));
        // filter = filter.or(gfilter);
        //
        // gfilter = ffac.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // gfilter.addLeftGeometry(ffac.createAttributeExpression(schema,
        // "polygon"));
        // gfilter.addRightGeometry(ffac.createBBoxExpression(env));
        // filter = filter.or(gfilter);
        //
        // System.err.println("trying with filter: " + filter);
        //
        // layerQuery = new Query("querytest", filter, Integer.MAX_VALUE,
        // null, "handle");
        // layer.setQuery(layerQuery);
        //
        // results = renderer.queryLayer(layer, null, envelope,
        // DefaultGeographicCRS.WGS84);
        // assertEquals(2, results.getCount());
        // // the 4 atts should be loaded since the definition query includes
        // "id"
        // assertEquals(4, results.getSchema().getAttributeCount());
        //
        // RendererBaseTest.showRender("testDefinitionQuery3", renderer, 1000,
        // null);

    }

    public void testDefinitionQuerySLDProcessing() throws Exception {
        // final SimpleFeatureCollection ft = createTestDefQueryFeatureCollection();
        // final Style style = createDefQueryTestStyle();
        // FeatureResults results;
        // Envelope envelope = ft.getBounds();
        //
        // // we'll use this as the definition query for the layer
        // Query layerQuery;
        //
        // MapLayer layer = new DefaultMapLayer(ft, style);
        // MapContent map = new DefaultMapContent(new MapLayer[] { layer });
        // map.setAreaOfInterest(envelope);
        // StreamingRenderer renderer = new StreamingRenderer();
        // renderer.setMapContent(map);
        // renderer.setRendererHints(rendererHints);
        //
        // // this is the reader that StreamingRenderer obtains after applying
        // // the mixed filter to a given layer.
        //  FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        // Filter filter = Filter.INCLUDE;
        // FilterFactory ffac = FilterFactoryFinder.createFilterFactory();
        //
        // // test maxFeatures, render just the first 2 features
        // layerQuery = new Query("querytest", filter);
        // layer.setQuery(layerQuery);
        //
        // ArrayList rules = new ArrayList();
        // ArrayList elseRules = new ArrayList();
        // StyleBuilder builder = new StyleBuilder();
        // Rule rule = builder.createRule(builder.createLineSymbolizer());
        // rules.add(rule);
        // rule.setFilter(
        // // JD: remove this null parameter
        // new AbstractFilterImpl(null) {
        // int i = 0;
        //
        // public boolean evaluate(Feature feature) {
        // i++;
        // return i < 3;
        // }
        //
        // // public void accept(FilterVisitor visitor) {
        // // visitor.visit(this);
        // // }
        // public Object accept(
        // org.opengis.filter.FilterVisitor visitor,
        // Object extraData) {
        // return extraData;
        // }
        // });
        // LiteFeatureTypeStyle fts = new LiteFeatureTypeStyle(null, rules,
        // elseRules);
        //
        // results = renderer.queryLayer(layer,
        // new LiteFeatureTypeStyle[] { fts }, envelope,
        // DefaultGeographicCRS.WGS84);
        // assertEquals(2, results.getCount());
        //
        // elseRules.add(rule);
        //
        // fts = new LiteFeatureTypeStyle(null, rules, elseRules);
        // results = renderer.queryLayer(layer,
        // new LiteFeatureTypeStyle[] { fts }, envelope,
        // DefaultGeographicCRS.WGS84);
        // assertEquals(3, results.getCount());

    }

    private SimpleFeatureCollection createTestDefQueryFeatureCollection() throws Exception {
        MemoryDataStore data = new MemoryDataStore();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("querytest");
        builder.add("id", String.class);
        builder.add("point", Point.class);
        builder.add("line", LineString.class);
        builder.add("polygon", Polygon.class);
        SimpleFeatureType type = builder.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f;
        LineString l;
        Polygon p;

        l = line(gf, new int[] {20, 20, 100, 20, 100, 100});
        p = (Polygon) l.convexHull();
        f =
                SimpleFeatureBuilder.build(
                        type, new Object[] {"ft1", point(gf, 20, 20), l, p}, "test.1");
        data.addFeature(f);

        l = line(gf, new int[] {130, 130, 110, 110, 110, 130, 30, 130});
        p = (Polygon) l.convexHull();
        f =
                SimpleFeatureBuilder.build(
                        type, new Object[] {"ft2", point(gf, 130, 130), l, p}, "test.2");
        data.addFeature(f);

        l = line(gf, new int[] {150, 150, 190, 140, 190, 190});
        p = (Polygon) l.convexHull();
        f =
                SimpleFeatureBuilder.build(
                        type, new Object[] {"ft3", point(gf, 150, 150), l, p}, "test.3");
        data.addFeature(f);

        String typeName = type.getTypeName();
        return data.getFeatureSource(typeName).getFeatures();
    }

    private Style createDefQueryTestStyle() throws IllegalFilterException {
        StyleFactory sFac = CommonFactoryFinder.getStyleFactory();

        PointSymbolizer pointsym = sFac.createPointSymbolizer();
        pointsym.setGraphic(sFac.getDefaultGraphic());
        pointsym.setGeometryPropertyName("point");

        Rule rulep = sFac.createRule();
        rulep.symbolizers().add(pointsym);
        FeatureTypeStyle ftsP = sFac.createFeatureTypeStyle();
        ftsP.rules().add(rulep);
        ftsP.featureTypeNames().add(new NameImpl("querytest"));

        LineSymbolizer linesym = sFac.createLineSymbolizer();
        linesym.setGeometryPropertyName("line");

        Stroke myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.literal("#0000ff"));
        myStroke.setWidth(filterFactory.literal(Integer.valueOf(3)));
        LOGGER.info("got new Stroke " + myStroke);
        linesym.setStroke(myStroke);

        Rule rule2 = sFac.createRule();
        rule2.symbolizers().add(linesym);
        FeatureTypeStyle ftsL = sFac.createFeatureTypeStyle();
        ftsL.rules().add(rule2);
        ftsL.featureTypeNames().add(new NameImpl("querytest"));

        PolygonSymbolizer polysym = sFac.createPolygonSymbolizer();
        polysym.setGeometryPropertyName("polygon");
        Fill myFill = sFac.getDefaultFill();
        myFill.setColor(filterFactory.literal("#ff0000"));
        polysym.setFill(myFill);
        polysym.setStroke(sFac.getDefaultStroke());
        Rule rule = sFac.createRule();
        rule.symbolizers().add(polysym);
        FeatureTypeStyle ftsPoly = sFac.createFeatureTypeStyle(new Rule[] {rule});
        // ftsPoly.setRules(new Rule[]{rule});
        ftsPoly.featureTypeNames().add(new NameImpl("querytest"));

        Style style = sFac.createStyle();
        style.featureTypeStyles().addAll(Arrays.asList(ftsPoly, ftsL, ftsP));

        return style;
    }

    public LineString line(final GeometryFactory gf, int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }

    public Point point(final GeometryFactory gf, int x, int y) {
        Coordinate coord = new Coordinate(x, y);
        return gf.createPoint(coord);
    }

    private Point makeSamplePoint(final GeometryFactory geomFac) {
        Coordinate c = new Coordinate(xCenter - 14.0d, yCenter - 14.0d);
        Point point = geomFac.createPoint(c);
        return point;
    }

    private LineString makeSampleLineString(final GeometryFactory geomFac) {
        Coordinate[] linestringCoordinates = new Coordinate[7];
        linestringCoordinates[0] = new Coordinate(xCenter - 5.0d, yCenter - 5.0d);
        linestringCoordinates[1] = new Coordinate(xCenter - 6.0d, yCenter - 5.0d);
        linestringCoordinates[2] = new Coordinate(xCenter - 6.0d, yCenter - 6.0d);
        linestringCoordinates[3] = new Coordinate(xCenter - 7.0d, yCenter - 6.0d);
        linestringCoordinates[4] = new Coordinate(xCenter - 7.0d, yCenter - 7.0d);
        linestringCoordinates[5] = new Coordinate(xCenter - 8.0d, yCenter - 7.0d);
        linestringCoordinates[6] = new Coordinate(xCenter - 8.0d, yCenter - 8.0d);
        LineString line = geomFac.createLineString(linestringCoordinates);

        return line;
    }

    private Polygon makeSamplePolygon(final GeometryFactory geomFac) {
        Coordinate[] polygonCoordinates = new Coordinate[10];
        polygonCoordinates[0] = new Coordinate(xCenter - 7, yCenter - 7);
        polygonCoordinates[1] = new Coordinate(xCenter - 6, yCenter - 9);
        polygonCoordinates[2] = new Coordinate(xCenter - 6, yCenter - 11);
        polygonCoordinates[3] = new Coordinate(xCenter - 7, yCenter - 12);
        polygonCoordinates[4] = new Coordinate(xCenter - 9, yCenter - 11);
        polygonCoordinates[5] = new Coordinate(xCenter - 11, yCenter - 12);
        polygonCoordinates[6] = new Coordinate(xCenter - 13, yCenter - 11);
        polygonCoordinates[7] = new Coordinate(xCenter - 13, yCenter - 9);
        polygonCoordinates[8] = new Coordinate(xCenter - 11, yCenter - 7);
        polygonCoordinates[9] = new Coordinate(xCenter - 7, yCenter - 7);
        try {
            LinearRing ring = geomFac.createLinearRing(polygonCoordinates);
            Polygon polyg = geomFac.createPolygon(ring, null);
            return polyg;
        } catch (TopologyException te) {
            fail("Error creating sample polygon for testing " + te);
        }
        return null;
    }

    private GeometryCollection makeSampleGeometryCollection(final GeometryFactory geomFac) {
        try {
            Geometry polyg = buildShiftedGeometry(makeSamplePolygon(geomFac), 50, 50);
            Geometry lineString = buildShiftedGeometry(makeSampleLineString(geomFac), 50, 50);
            return geomFac.createGeometryCollection(new Geometry[] {polyg, lineString});
        } catch (TopologyException te) {
            fail("Error creating sample polygon for testing " + te);
        }
        return null;
    }

    private LinearRing makeSampleLinearRing(final GeometryFactory geomFac) {
        try {
            Polygon polyg = (Polygon) buildShiftedGeometry(makeSamplePolygon(geomFac), 0, 100);
            return (LinearRing) polyg.getExteriorRing();
        } catch (TopologyException te) {
            fail("Error creating sample polygon for testing " + te);
        }
        return null;
    }

    private Geometry buildShiftedGeometry(Geometry g, double shiftX, double shiftY) {
        Geometry clone = g.copy();
        Coordinate[] coords = clone.getCoordinates();
        final int length = coords.length;
        for (int i = 0; i < length; i++) {
            Coordinate coord = coords[i];
            coord.x += shiftX;
            coord.y += shiftY;
        }

        return clone;
    }

    /** I am not sure this is really correct. We should check it with more care. */
    public void testScaleCalc() throws Exception {

        // 1388422.8746916912, 639551.3924667436
        // 1407342.5139777814, 650162.7155794351
        // 655,368
        // some location in bc albers
        CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"NAD83 / BC"
                                + "Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS"
                                + "1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],"
                                + "TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],"
                                + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
                                + "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],"
                                + "AUTHORITY[\"EPSG\",\"4269\"]],"
                                + "PROJECTION[\"Albers_Conic_Equal_Area\"],"
                                + "PARAMETER[\"standard_parallel_1\",50],"
                                + "PARAMETER[\"standard_parallel_2\",58.5],"
                                + "PARAMETER[\"latitude_of_center\",45],"
                                + "PARAMETER[\"longitude_of_center\",-126],"
                                + "PARAMETER[\"false_easting\",1000000],"
                                + "PARAMETER[\"false_northing\",0],"
                                + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],"
                                + "AUTHORITY[\"EPSG\",\"3005\"]]");
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        1388422.8746916912,
                        1407342.5139777814,
                        639551.3924667438,
                        650162.715579435,
                        crs);

        double s = RendererUtilities.calculateScale(envelope, 655, 368, 90.0);

        LOGGER.info(Double.toString(s));
        // assertTrue(Math.abs(102355 - s) < 10); // 102355.1639202933
    }

    public void testRenderEmptyLine() throws SchemaException, IllegalAttributeException {
        GeometryFactory gf = new GeometryFactory();
        StyleBuilder sb = new StyleBuilder();
        SimpleFeatureType pointType =
                DataUtilities.createType("emptyLines", "geom:LineString,name:String");
        SimpleFeature f =
                SimpleFeatureBuilder.build(
                        pointType,
                        new Object[] {gf.createLineString((Coordinate[]) null), "name"},
                        null);
        Style style = sb.createStyle(sb.createLineSymbolizer());

        renderEmptyGeometry(f, style);
    }

    public void testRenderEmptyCollection() throws SchemaException, IllegalAttributeException {
        GeometryFactory gf = new GeometryFactory();
        StyleBuilder sb = new StyleBuilder();
        SimpleFeatureType pointType =
                DataUtilities.createType("emptyPolygon", "geom:MultiPolygon,name:String");
        SimpleFeature f =
                SimpleFeatureBuilder.build(
                        pointType,
                        new Object[] {gf.createMultiPolygon((Polygon[]) null), "name"},
                        null);
        Style style = sb.createStyle(sb.createPolygonSymbolizer());

        renderEmptyGeometry(f, style);
    }

    public void testRenderCollectionWithEmptyItems()
            throws SchemaException, IllegalAttributeException {
        GeometryFactory gf = new GeometryFactory();
        StyleBuilder sb = new StyleBuilder();
        SimpleFeatureType pointType =
                DataUtilities.createType("emptyPolygon", "geom:MultiPolygon,name:String");
        Polygon p1 = gf.createPolygon(gf.createLinearRing((Coordinate[]) null), null);
        Polygon p2 =
                gf.createPolygon(
                        gf.createLinearRing(
                                new Coordinate[] {
                                    new Coordinate(0, 0),
                                    new Coordinate(1, 1),
                                    new Coordinate(1, 0),
                                    new Coordinate(0, 0)
                                }),
                        null);
        MultiPolygon mp = gf.createMultiPolygon(new Polygon[] {p1, p2});
        SimpleFeature f = SimpleFeatureBuilder.build(pointType, new Object[] {mp, "name"}, null);
        Style style = sb.createStyle(sb.createPolygonSymbolizer());

        renderEmptyGeometry(f, style);
    }

    public void testRenderPolygonEmptyRings() throws SchemaException, IllegalAttributeException {
        GeometryFactory gf = new GeometryFactory();
        StyleBuilder sb = new StyleBuilder();
        SimpleFeatureType pointType =
                DataUtilities.createType("emptyRings", "geom:MultiPolygon,name:String");
        LinearRing emptyRing = gf.createLinearRing((Coordinate[]) null);
        LinearRing realRing =
                gf.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0),
                            new Coordinate(1, 1),
                            new Coordinate(1, 0),
                            new Coordinate(0, 0)
                        });
        Polygon p1 = gf.createPolygon(realRing, new LinearRing[] {emptyRing});
        Polygon p2 = gf.createPolygon(emptyRing, new LinearRing[] {emptyRing});
        MultiPolygon mp = gf.createMultiPolygon(new Polygon[] {p1, p2});
        SimpleFeature f = SimpleFeatureBuilder.build(pointType, new Object[] {mp, "name"}, null);
        Style style = sb.createStyle(sb.createPolygonSymbolizer());

        renderEmptyGeometry(f, style);
    }

    public void testMixedEmptyMultiLine() throws SchemaException, IllegalAttributeException {
        GeometryFactory gf = new GeometryFactory();
        StyleBuilder sb = new StyleBuilder();
        SimpleFeatureType pointType =
                DataUtilities.createType("emptyRings", "geom:MultiLineString,name:String");
        LineString emptyLine = gf.createLineString((Coordinate[]) null);
        LineString realLine =
                gf.createLineString(new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 1)});
        MultiLineString mls = gf.createMultiLineString(new LineString[] {emptyLine, realLine});
        SimpleFeature f = SimpleFeatureBuilder.build(pointType, new Object[] {mls, "name"}, null);
        Style style = sb.createStyle(sb.createPolygonSymbolizer());

        renderEmptyGeometry(f, style);
    }

    private void renderEmptyGeometry(SimpleFeature f, Style style) {
        SimpleFeatureCollection fc = DataUtilities.collection(f);
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fc, style));
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);
        BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_4BYTE_ABGR);
        sr.addRenderListener(
                new RenderListener() {

                    public void featureRenderer(SimpleFeature feature) {}

                    public void errorOccurred(Exception e) {
                        java.util.logging.Logger.getGlobal()
                                .log(java.util.logging.Level.INFO, "", e);
                        fail(
                                "Got an exception during rendering, this should not happen, "
                                        + "not even with emtpy geometries");
                    }
                });
        sr.paint(
                (Graphics2D) bi.getGraphics(),
                new Rectangle(640, 480),
                new ReferencedEnvelope(new Envelope(0, 10, 0, 10), DefaultGeographicCRS.WGS84));
        mc.dispose();
    }
}
