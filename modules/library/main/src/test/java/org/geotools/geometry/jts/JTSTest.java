/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.List;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.projection.PointOutsideEnvelopeException;
import org.geotools.referencing.wkt.Parser;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Unit tests for the {@link JTS} utility class.
 *
 * @author Jess Eichar
 * @author Martin Desruisseaux
 * @author Michael Bedward
 * @version $Id$
 * @since 2.2
 */
public class JTSTest extends JTSTestBase {
    /** The tolerance factor. */
    private static final double EPS = 0.000001;

    /** A CRS for testing purpose. */
    static final String UTM_ZONE_10N =
            "PROJCS[\"NAD_1983_UTM_Zone_10N\",\n"
                    + "  GEOGCS[\"GCS_North_American_1983\",\n"
                    + "    DATUM[\"D_North_American_1983\",\n"
                    + "      TOWGS84[0,0,0,0,0,0,0],\n"
                    + "      SPHEROID[\"GRS_1980\",6378137,298.257222101]],\n"
                    + "    PRIMEM[\"Greenwich\",0],\n"
                    + "    UNIT[\"Degree\",0.017453292519943295]],\n"
                    + "  PROJECTION[\"Transverse_Mercator\"],\n"
                    + "    PARAMETER[\"False_Easting\",500000],\n"
                    + "    PARAMETER[\"False_Northing\",0],\n"
                    + "    PARAMETER[\"Central_Meridian\",-123],\n"
                    + "    PARAMETER[\"Scale_Factor\",0.9996],\n"
                    + "    PARAMETER[\"Latitude_Of_Origin\",0],\n"
                    + "  UNIT[\"Meter\",1]]";
    /** A CRS for testing purpose. */
    static final String NAD83_BC =
            "PROJCS[\"NAD83 / BC Albers\",\n"
                    + "  GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",\n"
                    + "    SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],\n"
                    + "    TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],\n"
                    + "    PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],\n"
                    + "    UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],\n"
                    + "    AUTHORITY[\"EPSG\",\"4269\"]],\n"
                    + "  PROJECTION[\"Albers_Conic_Equal_Area\"],\n"
                    + "  PARAMETER[\"standard_parallel_1\",50],\n"
                    + "  PARAMETER[\"standard_parallel_2\",58.5],\n"
                    + "  PARAMETER[\"latitude_of_center\",45],\n"
                    + "  PARAMETER[\"longitude_of_center\",-126],\n"
                    + "  PARAMETER[\"false_easting\",1000000],\n"
                    + "  PARAMETER[\"false_northing\",0],\n"
                    + "  UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],\n"
                    + "  AUTHORITY[\"EPSG\",\"3005\"]]";

    @Test
    public void testJTSFactory() {
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);
        assertNotNull(gf);
    }

    /** Tests the transformation of a single coordinate. */
    @Test
    public void testTransformCoordinate() throws FactoryException, TransformException {
        Coordinate coord = new Coordinate(10, 10);
        AffineTransform at = AffineTransform.getScaleInstance(0.5, 1);
        MathTransform2D t =
                (MathTransform2D)
                        ReferencingFactoryFinder.getMathTransformFactory(null)
                                .createAffineTransform(new GeneralMatrix(at));
        coord = JTS.transform(coord, coord, t);
        assertEquals(new Coordinate(5, 10), coord);
        coord = JTS.transform(coord, coord, t.inverse());
        assertEquals(new Coordinate(10, 10), coord);

        CoordinateReferenceSystem crs =
                ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(UTM_ZONE_10N);
        t =
                (MathTransform2D)
                        ReferencingFactoryFinder.getCoordinateOperationFactory(null)
                                .createOperation(DefaultGeographicCRS.WGS84, crs)
                                .getMathTransform();
        coord = new Coordinate(-123, 55);
        coord = JTS.transform(coord, coord, t);
        coord = JTS.transform(coord, coord, t.inverse());
        assertEquals(-123, coord.x, EPS);
        assertEquals(55, coord.y, EPS);
    }

    /*
     * Tests the transformation of an envelope.
     */
    public void testTransformEnvelopeMathTransform() throws FactoryException, TransformException {
        Envelope envelope = new Envelope(0, 10, 0, 10);
        AffineTransform at = AffineTransform.getScaleInstance(0.5, 1);
        MathTransform2D t =
                (MathTransform2D)
                        ReferencingFactoryFinder.getMathTransformFactory(null)
                                .createAffineTransform(new GeneralMatrix(at));
        envelope = JTS.transform(envelope, t);
        assertEquals(new Envelope(0, 5, 0, 10), envelope);
        envelope = JTS.transform(envelope, t.inverse());
        assertEquals(new Envelope(0, 10, 0, 10), envelope);

        envelope = JTS.transform(envelope, null, t, 10);
        assertEquals(new Envelope(0, 5, 0, 10), envelope);
        envelope = JTS.transform(envelope, null, t.inverse(), 10);
        assertEquals(new Envelope(0, 10, 0, 10), envelope);

        CoordinateReferenceSystem crs =
                ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(UTM_ZONE_10N);
        t =
                (MathTransform2D)
                        ReferencingFactoryFinder.getCoordinateOperationFactory(null)
                                .createOperation(DefaultGeographicCRS.WGS84, crs)
                                .getMathTransform();
        envelope = new Envelope(-123, -133, 55, 60);
        envelope = JTS.transform(envelope, t);
        envelope = JTS.transform(envelope, t.inverse());
        /*
         * Use a large tolerance factory for comparaisons because an accurate transformed envelope
         * is bigger than the envelope that we get if we transformed only the 4 corners, and the
         * inverse envelope way expand yet again the envelope for exactly the same reason.
         */
        assertEquals(-133, envelope.getMinX(), 1.5);
        assertEquals(-123, envelope.getMaxX(), EPS);
        assertEquals(55, envelope.getMinY(), 0.5);
        assertEquals(60, envelope.getMaxY(), 0.5);
    }

    /** Tests the distance between points function */
    public void testOrthodromicDistance() throws Exception {
        final Parser parser = new Parser();
        final DefaultProjectedCRS crs = (DefaultProjectedCRS) parser.parseObject(NAD83_BC);
        double d =
                JTS.orthodromicDistance(
                        new Coordinate(1402848.1938534670, 651571.1729878788),
                        new Coordinate(1389481.3104009738, 641990.9430108378),
                        crs);
        double realValue = 16451.33114;
        assertEquals(realValue, d, 0.1);
    }

    public void testCheckCoordinateRange() throws Exception {
        DefaultGeographicCRS crs = DefaultGeographicCRS.WGS84;

        // valid
        JTS.checkCoordinatesRange(JTS.toGeometry(new Envelope(-10, 10, -10, 10)), crs);

        // invalid lat
        try {
            JTS.checkCoordinatesRange(JTS.toGeometry(new Envelope(-10, 10, -100, 10)), crs);
            fail("Provided invalid coordinates, yet check did not throw an exception");
        } catch (PointOutsideEnvelopeException e) {
            // fine
        }

        // invalid lon
        try {
            JTS.checkCoordinatesRange(JTS.toGeometry(new Envelope(-190, 10, -10, 10)), crs);
            fail("Provided invalid coordinates, yet check did not throw an exception");
        } catch (PointOutsideEnvelopeException e) {
            // fine
        }
    }

    public void testToGeoemtry() {
        DefaultGeographicCRS crs = DefaultGeographicCRS.WGS84;

        // straight up
        Polygon polygon = JTS.toGeometry(new Envelope(-10, 10, -10, 10));
        assertEquals(5, polygon.getExteriorRing().getCoordinateSequence().size());

        // bounding box
        polygon = JTS.toGeometry((BoundingBox) new ReferencedEnvelope(-10, 10, -10, 10, crs));
        assertEquals(5, polygon.getExteriorRing().getCoordinateSequence().size());
    }

    @Test
    public void toGeometry_Shape_Poly() {
        Shape shape = new java.awt.Polygon(XPOINTS, YPOINTS, NPOINTS);
        Geometry geom = JTS.toGeometry(shape);
        assertTrue(geom instanceof LinearRing);

        Coordinate[] coords = geom.getCoordinates();
        assertEquals(NPOINTS + 1, coords.length);

        CoordList list = new CoordList(coords);
        Coordinate c = new Coordinate();
        for (int i = 0; i < NPOINTS; i++) {
            c.x = XPOINTS[i];
            c.y = YPOINTS[i];
            assertTrue(list.contains(c));
        }
    }

    @Test
    public void toGeometry_Shape_Line() {
        GeneralPath path = new GeneralPath();

        path.moveTo(XPOINTS[0], YPOINTS[0]);
        for (int i = 1; i < NPOINTS; i++) {
            path.lineTo(XPOINTS[i], YPOINTS[i]);
        }

        Geometry geom = JTS.toGeometry(path);
        assertTrue(geom instanceof LineString);

        Coordinate[] coords = geom.getCoordinates();
        assertEquals(NPOINTS, coords.length);

        CoordList list = new CoordList(coords);
        Coordinate c = new Coordinate();
        for (int i = 0; i < NPOINTS; i++) {
            c.x = XPOINTS[i];
            c.y = YPOINTS[i];
            assertTrue(list.contains(c));
        }
    }

    @Test
    public void getEnvelope2D() {
        ReferencedEnvelope refEnv =
                new ReferencedEnvelope(-10, 10, -5, 5, DefaultGeographicCRS.WGS84);

        Envelope2D env2D = JTS.getEnvelope2D(refEnv, refEnv.getCoordinateReferenceSystem());

        CRS.equalsIgnoreMetadata(
                refEnv.getCoordinateReferenceSystem(), env2D.getCoordinateReferenceSystem());

        assertTrue(env2D.boundsEquals(refEnv, 0, 1, TOL));
    }

    @Test
    public void toGeometry_Envelope() {
        Envelope refEnv = new Envelope(-10, 10, -5, 5);
        Geometry geom = JTS.toGeometry(refEnv);
        assertTrue(geom instanceof org.locationtech.jts.geom.Polygon);

        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }

    @Test
    public void toGeometry_ReferencedEnvelope() {
        ReferencedEnvelope refEnv =
                new ReferencedEnvelope(-10, 10, -5, 5, DefaultGeographicCRS.WGS84);
        Geometry geom = JTS.toGeometry(refEnv);
        assertTrue(geom instanceof org.locationtech.jts.geom.Polygon);

        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }

    @Test
    public void toEnvelope() {
        Coordinate[] coords = getPolyCoords();
        GeometryFactory gf = new GeometryFactory();
        Geometry geom = gf.createPolygon(gf.createLinearRing(coords), null);

        ReferencedEnvelope refEnv = JTS.toEnvelope(geom);
        assertTrue(geom.getEnvelopeInternal().equals(refEnv));
    }

    @Test
    public void toDirectPosition() {
        Coordinate c = new Coordinate(40, 40);
        DirectPosition wrapper = JTS.toDirectPosition(c, DefaultGeographicCRS.WGS84);

        GeneralDirectPosition expected = new GeneralDirectPosition(DefaultGeographicCRS.WGS84);
        expected.setOrdinate(0, 40);
        expected.setOrdinate(1, 40);

        assertEquals(expected, wrapper);
    }

    @Test
    public void toGeometry_BoundingBox() {
        BoundingBox bbox = new ReferencedEnvelope(-10, 10, -5, 5, null);
        Geometry geom = JTS.toGeometry(bbox);
        assertTrue(geom instanceof org.locationtech.jts.geom.Polygon);

        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }

    /**
     * Added this test after a bug was reported in JTS.transform for converting between WGS84 (2D)
     * and DefaultGeocentric.CARTESIAN (3D).
     */
    @Test
    public void transformCoordinate2DCRSTo3D() throws Exception {
        CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
        CoordinateReferenceSystem targetCRS = DefaultGeocentricCRS.CARTESIAN;
        MathTransform transform = CRS.findMathTransform(srcCRS, targetCRS);

        Coordinate srcCoord = new Coordinate(0, 0);
        Coordinate dest0 = JTS.transform(srcCoord, null, transform);

        srcCoord.x = 180;
        Coordinate dest180 = JTS.transform(srcCoord, null, transform);

        // Only a perfunctory check on the return values - mostly we
        // just wanted to make sure there was no exception
        assertEquals(dest0.x, -dest180.x, TOL);
        assertEquals(dest0.y, dest180.y, TOL);
        assertEquals(dest0.z, dest180.z, TOL);
    }

    @Test
    public void testTransformToWGS84() throws Exception {
        String wkt =
                "GEOGCS[\"GDA94\","
                        + " DATUM[\"Geocentric Datum of Australia 1994\","
                        + "  SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]],"
                        + "  TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "
                        + " AUTHORITY[\"EPSG\",\"6283\"]], "
                        + " PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                        + " UNIT[\"degree\", 0.017453292519943295], "
                        + " AXIS[\"Geodetic longitude\", EAST], "
                        + " AXIS[\"Geodetic latitude\", NORTH], "
                        + " AXIS[\"Ellipsoidal height\", UP], "
                        + " AUTHORITY[\"EPSG\",\"4939\"]]";

        CoordinateReferenceSystem gda94 = CRS.parseWKT(wkt);
        ReferencedEnvelope bounds =
                new ReferencedEnvelope3D(
                        130.875825803896,
                        130.898939990319,
                        -16.4491956225999,
                        -16.4338185791628,
                        0.0,
                        0.0,
                        gda94);

        ReferencedEnvelope worldBounds = JTS.toGeographic(bounds);
        assertEquals(DefaultGeographicCRS.WGS84, worldBounds.getCoordinateReferenceSystem());

        Envelope envelope =
                new Envelope(
                        130.875825803896, 130.898939990319, -16.4491956225999, -16.4338185791628);

        Envelope worldBounds2 = JTS.toGeographic(envelope, gda94);
        if (worldBounds2 instanceof BoundingBox) {
            assertEquals(
                    DefaultGeographicCRS.WGS84,
                    ((BoundingBox) worldBounds2).getCoordinateReferenceSystem());
        }
    }

    @Test
    public void testToGeographic() throws Exception {
        String wkt =
                "GEOGCS[\"GDA94\","
                        + " DATUM[\"Geocentric Datum of Australia 1994\","
                        + "  SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]],"
                        + "  TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "
                        + " AUTHORITY[\"EPSG\",\"6283\"]], "
                        + " PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                        + " UNIT[\"degree\", 0.017453292519943295], "
                        + " AXIS[\"Geodetic longitude\", EAST], "
                        + " AXIS[\"Geodetic latitude\", NORTH], "
                        + " AXIS[\"Ellipsoidal height\", UP], "
                        + " AUTHORITY[\"EPSG\",\"4939\"]]";

        CoordinateReferenceSystem gda94 = CRS.parseWKT(wkt);
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(130.875825803896, -16.4491956225999, 0.0));

        Geometry worldPoint = JTS.toGeographic(point, gda94);
        assertTrue(worldPoint instanceof Point);
        assertEquals(point.getX(), worldPoint.getCoordinate().x, 0.00000001);
    }

    @Test
    public void testToGeographicGeometry() throws Exception {
        // This time we are in north / east order
        String wkt =
                "GEOGCS[\"GDA94\","
                        + " DATUM[\"Geocentric Datum of Australia 1994\","
                        + "  SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]],"
                        + "  TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "
                        + " AUTHORITY[\"EPSG\",\"6283\"]], "
                        + " PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                        + " UNIT[\"degree\", 0.017453292519943295], "
                        + " AXIS[\"Geodetic latitude\", NORTH], "
                        + " AXIS[\"Geodetic longitude\", EAST], "
                        + " AXIS[\"Ellipsoidal height\", UP], "
                        + " AUTHORITY[\"EPSG\",\"4939\"]]";
        CoordinateReferenceSystem gda94 = CRS.parseWKT(wkt);

        GeometryFactory gf = new GeometryFactory();
        Point point =
                gf.createPoint(
                        new Coordinate(-16.4463909341494, 130.882672103999, 97.009018073082));

        Point world = (Point) JTS.toGeographic(point, gda94);
        assertEquals(point.getX(), world.getY(), 0.00000005);
        assertEquals(point.getY(), world.getX(), 0.00000005);
    }

    @Test
    public void testRemoveCollinear() throws Exception {
        // This polygon (* = vertix)
        //
        // ******
        // |    |
        // *-*--*
        //
        // should become like this after the remove collinear
        //
        // *----*
        // |    |
        // *----*

        final int[] xPoints = {0, 1, 2, 3, 4, 5, 5, 2, 0};

        final int[] yPoints = {0, 0, 0, 0, 0, 0, 2, 2, 2};

        final int nPoints = xPoints.length;
        final Shape shape = new java.awt.Polygon(xPoints, yPoints, nPoints);
        final Geometry original = JTS.toGeometry(shape);
        final Geometry reduced = JTS.removeCollinearVertices(original);
        assertEquals(10, original.getNumPoints());
        assertEquals(5, reduced.getNumPoints());
        final double DELTA = 1E-9;

        final Coordinate[] coords = reduced.getCoordinates();
        assertEquals(0, coords[0].x, DELTA);
        assertEquals(0, coords[0].y, DELTA);
        assertEquals(5, coords[1].x, DELTA);
        assertEquals(0, coords[1].y, DELTA);
        assertEquals(5, coords[2].x, DELTA);
        assertEquals(2, coords[2].y, DELTA);
        assertEquals(0, coords[3].x, DELTA);
        assertEquals(2, coords[3].y, DELTA);
        assertEquals(0, coords[4].x, DELTA);
        assertEquals(0, coords[4].y, DELTA);
    }

    @Test
    public void testMakeValid() throws Exception {
        // An invalid polygon similar to this one
        //
        // *----*
        // |    |
        // *----*----*
        //      |    |
        //      *----*
        //
        // Will be split into 2 separate polygons through the makeValid method
        final int[] xPoints = {0, 5, 5, 5, 10, 10, 5, 0};
        final int[] yPoints = {0, 0, 5, 10, 10, 5, 5, 5};
        final int nPoints = xPoints.length;

        final Shape shape = new java.awt.Polygon(xPoints, yPoints, nPoints);
        final LinearRing geom = (LinearRing) JTS.toGeometry(shape);
        final GeometryFactory factory = new GeometryFactory();
        final org.locationtech.jts.geom.Polygon polygon = factory.createPolygon(geom);
        assertFalse(polygon.isValid());

        final List<org.locationtech.jts.geom.Polygon> validPols = JTS.makeValid(polygon, false);

        assertEquals(2, validPols.size());
        org.locationtech.jts.geom.Polygon polygon1 = validPols.get(0);
        org.locationtech.jts.geom.Polygon polygon2 = validPols.get(1);
        assertEquals(5, polygon1.getNumPoints());
        assertEquals(5, polygon2.getNumPoints());
    }
}
