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
import static org.junit.Assert.assertTrue;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.List;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Unit tests for the JTS utility class.
 *
 * @author Michael Bedward
 * @source $URL$
 * @version $Id$
 * @since 2.8
 */
public class JTSTest extends JTSTestBase {

    @Test
    public void toGeometry_Shape_Poly() {
        Shape shape = new Polygon(XPOINTS, YPOINTS, NPOINTS);
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
        final Shape shape = new Polygon(xPoints, yPoints, nPoints);
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
