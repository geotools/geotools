/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esri.sde.sdk.client.SDEPoint;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Unit test suite for {@link ArcSDEGeometryBuilder}
 * 
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/GeometryBuilderTest.java $
 * @version $Id$
 */
public class GeometryBuilderTest {

    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeometryBuilderTest.class
            .getPackage().getName());

    private WKTReader wktReader;

    private Geometry[] testPoints;

    private Geometry[] testLineStrings;

    private Geometry[] testPolygons;

    private Geometry[] testMultiPoints;

    private Geometry[] testMultiLineStrings;

    private Geometry[] testMultiPolygons;

    @Before
    public void setUp() throws Exception {
        wktReader = new WKTReader();
        testPoints = new Geometry[] { wktReader.read("POINT (-0.055 99.999)"),
                wktReader.read("POINT (120.324 89.999)"), wktReader.read("POINT (-79.9 75.55)"),
                wktReader.read("POINT (500000.0005 500000.005)") };

        testLineStrings = new Geometry[] {
                wktReader
                        .read("LINESTRING (80 360, 520 360, 520 40, 120 40, 120 300, 460 300, 460 100, 200 100, 200 240, 400 240, 400 140, 560 0)"),
                wktReader
                        .read("LINESTRING (60 380, 60 20, 200 400, 280 20, 360 400, 420 20, 500 400, 580 20, 620 400)") };

        testPolygons = new Geometry[] {
                wktReader
                        .read("POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0), (3 3, 3 6, 6 6, 6 3, 3 3), (7 7, 7 8, 8 8, 8 7, 7 7))"),
                wktReader
                        .read("POLYGON ((140 380, 140 390, 1290 380, 140 300, 40 300, 60 400, 140 380))"),
                wktReader.read("POLYGON ((280 380, 280 200, 60 200, 60 380, 180 220, 280 380))") };

        testMultiPoints = new Geometry[] {
                wktReader.read("MULTIPOINT (180 90, -180 -90, 0 0, 180 -90)"),
                wktReader.read("MULTIPOINT (85.4545 89.2343, 15.234 -76.23423)") };

        testMultiLineStrings = new Geometry[] {
                wktReader
                        .read("MULTILINESTRING ((80 360, 80 80, 100 80, 100 360, 120 360, 120 80, 140 80, 140 360, 160 360, 160 80), (180 100, 60 100, 60 140, 180 140, 180 180, 60 180, 60 220, 180 220, 180 260, 60 260, 60 300, 180 300, 180 340, 60 340), (40 380, 240 380, 240 40, 40 40))"),
                wktReader
                        .read("MULTILINESTRING ((300 380, 60 200, 340 20, 580 220, 340 360, 140 200, 340 80), (340 300, 480 220, 360 120, 220 200, 320 280, 420 220, 360 160, 280 200), (380 380, 580 260, 580 400, 420 400, 560 320, 560 380, 520 380), (40 180, 260 20, 40 20, 40 140, 180 40))") };

        testMultiPolygons = new Geometry[] {
                wktReader
                        .read("MULTIPOLYGON (((0 0, 0 1, 1 1, 1 0, 0 0)), ((1 1, 1 3, 3 3, 3 1, 1 1)))"),
                wktReader
                        .read("MULTIPOLYGON (((0.0008 0.0005, 0.0008 0.0007, 0.0012 0.0007, 0.0012 0.0005, 0.0008 0.0005)))"),
                wktReader
                        .read("MULTIPOLYGON (((0.0008 0.0005, 0.0008 1.0007, 1.0012 1.0007, 1.0012 0.0005, 0.0008 0.0005)))"),
                wktReader
                        .read("MULTIPOLYGON (((0.008 0.005, 0.008 0.007, 0.012 0.007, 0.012 0.005, 0.008 0.005)))"),
                wktReader
                        .read("MULTIPOLYGON ( ((0 0, 0 10, 10 10, 10 0, 0 0), (3 3, 3 6, 6 6, 6 3, 3 3)))"),
                wktReader
                        .read("MULTIPOLYGON ( ((0 0, 0 10, 10 10, 10 0, 0 0), (3 3, 3 6, 6 6, 6 3, 3 3), (7 7, 7 8, 8 8, 8 7, 7 7)), ((-1 -1, -1 -3, -3 -3, -3 -1, -1 -1)), ((280 380, 280 200, 60 200, 60 380, 180 220, 280 380)) )")

        };
    }

    @After
    public void tearDown() throws Exception {
        this.wktReader = null;
    }

    @Test
    public void testInsertGeometries() throws Exception {
        TestData testData = new TestData();
        testData.setUp();
        try {
            testData.createTempTable(false);
        } catch (Exception e) {
            LOGGER.warning("can't create temp table, connection params may be not set. Skipping testInsertGeometries");
            return;
        }
        testInsertGeometries(testPoints, testData);
        testInsertGeometries(testLineStrings, testData);
        testInsertGeometries(testPolygons, testData);
        testInsertGeometries(testMultiPoints, testData);
        testInsertGeometries(testMultiLineStrings, testData);
        testInsertGeometries(testMultiPolygons, testData);
    }

    private void testInsertGeometries(Geometry[] original, TestData testData) throws Exception {
        testData.truncateTempTable();
        ISession session = testData.getConnectionPool().getSession();
        SeLayer layer = testData.getTempLayer(session);

        Geometry[] fetched = new Geometry[original.length];
        try {
            testData.insertData(original, layer, session);
            final SeSqlConstruct sqlCons = new SeSqlConstruct(layer.getName());

            SeQuery query = session.createAndExecuteQuery(new String[] { "SHAPE" }, sqlCons);

            SdeRow row;
            SeShape shape;

            int i = 0;
            row = session.fetch(query);
            while (i < fetched.length && row != null) {
                shape = row.getShape(0);
                assertNotNull(shape);
                Class<? extends Geometry> clazz = ArcSDEAdapter.getGeometryTypeFromSeShape(shape);
                ArcSDEGeometryBuilder builder = ArcSDEGeometryBuilder.builderFor(clazz);
                fetched[i] = builder.construct(shape, new GeometryFactory());
                i++;
                row = session.fetch(query);
            }
            session.close(query);
        } finally {
            session.dispose();
        }

        for (int i = 0; i < fetched.length; i++) {
            final Geometry expected = original[i];
            final Geometry actual = fetched[i];
            assertEquals(expected, actual, 1E-6);
        }
    }

    private void assertEquals(Geometry g1, Geometry g2, double tolerance) {
        g1.normalize();
        g2.normalize();
        StringBuilder wkt = new StringBuilder();
        wkt.append("expected: ").append(g1).append(", actual: ").append(g2);
        Assert.assertEquals("geometry dimension " + wkt, g1.getDimension(), g2.getDimension());
        Assert.assertEquals("number of geometries " + wkt, g1.getNumGeometries(),
                g2.getNumGeometries());
        Assert.assertEquals("number of points " + wkt, g1.getNumPoints(), g2.getNumPoints());
    }

    @Test
    public void testBuilderRespectsGeometryFactory() throws Exception {
        testBuilderRespectsGeometryFactory(this.testLineStrings);
        testBuilderRespectsGeometryFactory(this.testPoints);
        testBuilderRespectsGeometryFactory(this.testPolygons);
        testBuilderRespectsGeometryFactory(this.testMultiLineStrings);
        testBuilderRespectsGeometryFactory(this.testMultiPoints);
        testBuilderRespectsGeometryFactory(this.testMultiPolygons);
    }

    private void testBuilderRespectsGeometryFactory(final Geometry[] testData) throws SeException,
            IOException, DataSourceException {
        final GeometryFactory geometryFactory = new GeometryFactory(
                new LiteCoordinateSequenceFactory());
        Geometry[] geoms = buildJTSgeometriesFromShapes(testData, geometryFactory);
        for (int i = 0; i < geoms.length; i++) {
            Assert.assertSame(geometryFactory, geoms[i].getFactory());
        }
    }

    @Test
    public void testGetDefaultValues() {
        testGetDefaultValue(Point.class);
        testGetDefaultValue(MultiPoint.class);
        testGetDefaultValue(LineString.class);
        testGetDefaultValue(MultiLineString.class);
        testGetDefaultValue(Polygon.class);
        testGetDefaultValue(MultiPolygon.class);
    }

    @Test
    public void testPointBuilder() throws Exception {
        testBuildJTSGeometries(testPoints);
    }

    @Test
    public void testMultiPointBuilder() throws Exception {
        testBuildJTSGeometries(testMultiPoints);
    }

    @Test
    public void testLineStringBuilder() throws Exception {
        testBuildJTSGeometries(testLineStrings);
    }

    @Test
    public void testMultiLineStringBuilder() throws Exception {
        testBuildJTSGeometries(testMultiLineStrings);
    }

    @Test
    public void testPolygonBuilder() throws Exception {
        testBuildJTSGeometries(testPolygons);
    }

    @Test
    public void testMultiPolygonBuilder() throws Exception {
        testBuildJTSGeometries(testMultiPolygons);
    }

    @Test
    public void testConstructShapePoint() throws Exception {
        testBuildSeShapes(testPoints);
    }

    @Test
    public void testConstructShapeMultiPoint() throws Exception {
        testBuildSeShapes(testMultiPoints);
    }

    @Test
    public void testConstructShapeLineString() throws Exception {
        testBuildSeShapes(testLineStrings);
    }

    @Test
    public void testConstructShapeMultiLineString() throws Exception {
        testBuildSeShapes(testMultiLineStrings);
    }

    /**
     * TODO: resurrect testConstructShapePolygon
     */
    @Test
    public void testConstructShapePolygon() throws Exception {
        testBuildSeShapes(testPolygons);
    }

    @Test
    public void testConstructShapeMultiPolygon() throws Exception {
        testBuildSeShapes(testMultiPolygons);
    }

    @Test
    public void testConstructShapeEmpty() throws Exception {
        Geometry[] testEmptys = new Geometry[6];
        testEmptys[0] = ArcSDEGeometryBuilder.builderFor(Point.class).getEmpty();
        testEmptys[1] = ArcSDEGeometryBuilder.builderFor(MultiPoint.class).getEmpty();
        testEmptys[2] = ArcSDEGeometryBuilder.builderFor(LineString.class).getEmpty();
        testEmptys[3] = ArcSDEGeometryBuilder.builderFor(MultiLineString.class).getEmpty();
        testEmptys[4] = ArcSDEGeometryBuilder.builderFor(Polygon.class).getEmpty();
        testEmptys[5] = ArcSDEGeometryBuilder.builderFor(MultiPolygon.class).getEmpty();
        testBuildSeShapes(testEmptys);
    }

    /**
     * tests each geometry in <code>geometries</code> using
     * <code>testConstructShape(Geometry)</code>
     */
    private static void testBuildSeShapes(Geometry[] geometries) throws Exception {
        for (int i = 0; i < geometries.length; i++) {
            testConstructShape(geometries[i]);
        }
    }

    /**
     * tests the building of SeShape objects from JTS Geometries. To do that, recieves a Geometry
     * object, then creates a ArcSDEGeometryBuilder for it's geometry type and ask it to construct
     * an equivalent SeShape. With this SeShape, checks that it's number of points is equal to the
     * number of points in <code>geometry</code>, and then creates an equivalent Geometry object,
     * wich in turn is checked for equality against <code>geometry</code>.
     */
    private static void testConstructShape(Geometry geometry) throws Exception {
        LOGGER.finer("testConstructShape: testing " + geometry);

        Class<? extends Geometry> geometryClass = geometry.getClass();
        ArcSDEGeometryBuilder builder = ArcSDEGeometryBuilder.builderFor(geometryClass);

        SeCoordinateReference cr = TestData.getGenericCoordRef();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("****************** CRS extent: " + cr.getXYEnvelope());
        }
        Geometry equivalentGeometry = null;

        SeShape equivalentShape = builder.constructShape(geometry, cr);
        int expectedNumOfPoints = geometry.getNumPoints();

        Assert.assertEquals(geometry + " - " + equivalentShape, expectedNumOfPoints,
                equivalentShape.getNumOfPoints());
        LOGGER.fine("geometry and SeShape contains the same number of points: "
                + equivalentShape.getNumOfPoints());

        LOGGER.finer("generating an SeShape's equivalent Geometry");
        equivalentGeometry = builder.construct(equivalentShape, new GeometryFactory());

        LOGGER.fine("now testing both geometries for equivalence: " + geometry + " -- "
                + equivalentGeometry);

        if (geometry instanceof Polygon) {
            int expectedNumInteriorRing = ((Polygon) geometry).getNumInteriorRing();
            int numInteriorRing = ((Polygon) equivalentGeometry).getNumInteriorRing();
            Assert.assertEquals(geometry.toString(), expectedNumInteriorRing, numInteriorRing);
        }
        Assert.assertEquals(geometry.getDimension(), equivalentGeometry.getDimension());
        LOGGER.fine("dimension test passed");

        Assert.assertEquals(geometry.getGeometryType(), equivalentGeometry.getGeometryType());
        LOGGER.fine("geometry type test passed");

        Assert.assertEquals(geometry + " - " + equivalentGeometry, geometry.getNumPoints(),
                equivalentGeometry.getNumPoints());
        LOGGER.fine("numPoints test passed");

        LOGGER.fine(geometry.getEnvelopeInternal() + " == "
                + equivalentGeometry.getEnvelopeInternal());

        /*
         * assertEquals(geometry.getEnvelopeInternal(), equivalentGeometry.getEnvelopeInternal());
         */
        Assert.assertEquals(geometry.getArea(), equivalentGeometry.getArea(), 0.1);
        LOGGER.fine("area test passed");
    }

    /**
     * Tests that the geometry builder for the geometry class given by <code>geometryClass</code>
     * correctly constcucts JTS geometries fom ArcSDE Java API's <code>SeShape</code>.
     * <p>
     * To do so, first parses the WKT geometries from the properties file pointed by
     * <code>"test-data/" + testDataSource</code>, then creates their corresponding
     * <code>SeShape</code> objects and finally used ArcSDEGeometryBuilder to build the JTS
     * geometries back, which are tested for equality against the original ones.
     * </p>
     * 
     * @param expectedGeometries
     *            the list of geometries (of the same type) to assert the building from their
     *            corresponding arcsde representation.
     * @throws Exception
     *             for any problem that could arise
     */
    private void testBuildJTSGeometries(final Geometry[] expectedGeometries) throws Exception {

        Geometry createdGeometry;
        Geometry expectedGeometry;
        Geometry[] createdGeometries = buildJTSgeometriesFromShapes(expectedGeometries,
                new GeometryFactory());

        for (int i = 0; i < createdGeometries.length; i++) {
            expectedGeometry = expectedGeometries[i];
            createdGeometry = createdGeometries[i];
            Assert.assertEquals(expectedGeometry.getClass(), createdGeometry.getClass());
        }
    }

    private Geometry[] buildJTSgeometriesFromShapes(final Geometry[] expectedGeometries,
            final GeometryFactory geometryFactory) throws SeException, IOException,
            DataSourceException {

        Class<? extends Geometry> geometryClass = expectedGeometries[0].getClass();
        final ArcSDEGeometryBuilder geometryBuilder = ArcSDEGeometryBuilder
                .builderFor(geometryClass);
        LOGGER.fine("created " + geometryBuilder.getClass().getName());

        Geometry[] createdGeometries = new Geometry[expectedGeometries.length];
        Geometry createdGeometry;
        Geometry expectedGeometry;
        double[][][] sdeCoords;

        // create a sde CRS with a huge value range and 5 digits of presission
        SeCoordinateReference seCRS = TestData.getGenericCoordRef();

        for (int i = 0; i < expectedGeometries.length; i++) {
            expectedGeometry = expectedGeometries[i];
            sdeCoords = geometryToSdeCoords(expectedGeometry, seCRS);

            // geometryBuilder.newGeometry is a protected method
            // and should not be called directly. We use it here
            // just for testing purposes. Instead,
            // geometryBuilder.construct(SeShape)
            // must be used
            createdGeometry = geometryBuilder.newGeometry(sdeCoords, geometryFactory);
            createdGeometries[i] = createdGeometry;
        }
        return createdGeometries;
    }

    private double[][][] geometryToSdeCoords(final Geometry jtsGeom,
            final SeCoordinateReference seCRS) throws SeException, IOException {
        int numParts;
        double[][][] sdeCoords;
        GeometryCollection gcol = null;

        if (jtsGeom instanceof GeometryCollection) {
            gcol = (GeometryCollection) jtsGeom;
        } else {
            Geometry[] geoms = { jtsGeom };
            gcol = new GeometryFactory().createGeometryCollection(geoms);
        }

        List<SDEPoint> allPoints = new ArrayList<SDEPoint>();
        numParts = gcol.getNumGeometries();

        int[] partOffsets = new int[numParts];
        Geometry geom;

        for (int currGeom = 0; currGeom < numParts; currGeom++) {
            partOffsets[currGeom] = allPoints.size();
            geom = gcol.getGeometryN(currGeom);

            Coordinate[] coords = geom.getCoordinates();

            for (int i = 0; i < coords.length; i++) {
                Coordinate c = coords[i];
                SDEPoint p = new SDEPoint(c.x, c.y);
                allPoints.add(p);
            }
        }

        SDEPoint[] points = new SDEPoint[allPoints.size()];
        allPoints.toArray(points);

        SeShape shape = new SeShape(seCRS);

        try {
            if (jtsGeom instanceof Point || gcol instanceof MultiPoint) {
                shape.generatePoint(points.length, points);
            } else if (jtsGeom instanceof LineString || jtsGeom instanceof MultiLineString) {
                shape.generateLine(points.length, numParts, partOffsets, points);
            } else {
                shape.generatePolygon(points.length, numParts, partOffsets, points);
            }
        } catch (SeException e) {
            LOGGER.warning(e.getSeError().getErrDesc());
            throw new DataSourceException(e.getSeError().getErrDesc() + ": " + jtsGeom, e);
        }

        sdeCoords = shape.getAllCoords();

        return sdeCoords;
    }

    /**
     * given a geometry class, tests that ArcSDEGeometryBuilder.defaultValueFor that class returns
     * an empty geometry of the same geometry class
     */
    private void testGetDefaultValue(Class<? extends Geometry> geometryClass) {
        Geometry geom = ArcSDEGeometryBuilder.defaultValueFor(geometryClass);
        assertNotNull(geom);
        assertTrue(geom.isEmpty());
        assertTrue(geometryClass.isAssignableFrom(geom.getClass()));
    }
}
