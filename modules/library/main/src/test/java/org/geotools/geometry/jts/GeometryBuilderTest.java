/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Tests for GeometryBuilder.
 *
 * @author michael
 */
public class GeometryBuilderTest {

    private static final double EPS = 1.0e-8;

    private static final Envelope RECT_ENV = new Envelope(-1.2, 3.4, -5.6, 7.8);
    private static final Envelope SQUARE_ENV = new Envelope(-1.2, 1.2, -1.2, 1.2);

    private static final GeometryBuilder builder = new GeometryBuilder();

    @Test
    public void box() throws Exception {
        Polygon p =
                builder.box(
                        RECT_ENV.getMinX(),
                        RECT_ENV.getMinY(),
                        RECT_ENV.getMaxX(),
                        RECT_ENV.getMaxY());
        assertBounds(RECT_ENV, p.getEnvelopeInternal(), 1.0e-8);
    }

    @Test
    public void boxZ() throws Exception {
        Polygon p =
                builder.boxZ(
                        RECT_ENV.getMinX(),
                        RECT_ENV.getMinY(),
                        RECT_ENV.getMaxX(),
                        RECT_ENV.getMaxY(),
                        42);
        assertBounds(RECT_ENV, p.getEnvelopeInternal(), 1.0e-8);
        assertEquals(42, (int) p.getCoordinate().z);
    }

    @Test
    public void circle() throws Exception {
        double radius = SQUARE_ENV.getWidth() / 2;

        Polygon p =
                builder.circle(
                        SQUARE_ENV.getMinX() + radius,
                        SQUARE_ENV.getMinY() + radius,
                        radius,
                        getNumSides(SQUARE_ENV));

        assertBounds(SQUARE_ENV, p.getEnvelopeInternal(), 0.01);
    }

    @Test
    public void ellipse() throws Exception {
        Polygon p =
                builder.ellipse(
                        RECT_ENV.getMinX(),
                        RECT_ENV.getMinY(),
                        RECT_ENV.getMaxX(),
                        RECT_ENV.getMaxY(),
                        getNumSides(RECT_ENV));

        assertBounds(RECT_ENV, p.getEnvelopeInternal(), 0.01);
    }

    @Test
    public void emptyLineString() throws Exception {
        LineString line = builder.lineString();
        assertTrue(line.isEmpty());
        assertEquals(2, line.getCoordinateSequence().getDimension());
    }

    @Test
    public void emptyLineStringZ() throws Exception {
        LineString line = builder.lineStringZ();
        assertEquals(3, line.getCoordinateSequence().getDimension());
    }

    @Test
    public void lineStringFromOrdinates() throws Exception {
        double[] ordinates = new double[] {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0};
        LineString line = builder.lineString(ordinates);

        Coordinate[] coords = line.getCoordinates();
        assertCoordinates(coords, ordinates, 2);
    }

    @Test
    public void lineStringZFromOrdinates() throws Exception {
        double[] ordinates = new double[] {1, 2, -3, 4, 5, -6, 7, 8, -9};
        LineString line = builder.lineStringZ(ordinates);

        Coordinate[] coords = line.getCoordinates();
        assertCoordinates(coords, ordinates, 3);
    }

    @Test
    public void linearRing() throws Exception {
        LinearRing ring = builder.linearRing();
        assertTrue(ring.isEmpty());
        assertEquals(2, ring.getCoordinateSequence().getDimension());
    }

    @Test
    public void linearRingZ() throws Exception {
        LinearRing ring = builder.linearRingZ();
        assertTrue(ring.isEmpty());
        assertEquals(3, ring.getCoordinateSequence().getDimension());
    }

    @Test
    public void linearRingFromOrdinates() throws Exception {
        double[] closedOrdinates = new double[] {1, 1, 1, 2, 2, 2, 2, 1, 1, 1};
        double[] openOrdinates = Arrays.copyOf(closedOrdinates, closedOrdinates.length - 2);

        LinearRing ring = builder.linearRing(openOrdinates);

        // ring should be closed even though ordinate sequence is not
        assertTrue(ring.isClosed());

        assertCoordinates(ring.getCoordinates(), closedOrdinates, 2);
    }

    @Test
    public void linearRingZFromOrdinates() throws Exception {
        double[] closedOrdinates =
                new double[] {
                    1, 1, -1,
                    1, 2, -2,
                    2, 2, -2,
                    2, 1, -1,
                    1, 1, -1
                };

        double[] openOrdinates = Arrays.copyOf(closedOrdinates, closedOrdinates.length - 3);

        LinearRing ring = builder.linearRingZ(openOrdinates);

        // ring should be closed even though ordinate sequence is not
        assertTrue(ring.isClosed());
        assertCoordinates(ring.getCoordinates(), closedOrdinates, 3);
    }

    @Test
    public void multiPoint() throws Exception {
        double[] ords = {1, 2, 3, 4};

        MultiPoint mp = builder.multiPoint(ords[0], ords[1], ords[2], ords[3]);
        assertEquals(2, mp.getNumGeometries());

        Point p = (Point) mp.getGeometryN(0);
        assertEquals(2, p.getCoordinateSequence().getDimension());
    }

    @Test
    public void multiPointZ() throws Exception {
        double[] ords = {1, 2, 3, 4, 5, 6};

        MultiPoint mp = builder.multiPointZ(ords[0], ords[1], ords[2], ords[3], ords[4], ords[5]);
        assertEquals(2, mp.getNumGeometries());

        Point p = (Point) mp.getGeometryN(0);
        assertEquals(3, p.getCoordinateSequence().getDimension());
    }

    /*
     * Fudges a value to use for the number of sides arg for GeometryBuilder
     * methods based on the dimensions of the bounding envelope.
     */
    private int getNumSides(Envelope env) {
        return (int) (10 * (env.getWidth() + env.getHeight()));
    }

    private void assertBounds(Envelope e1, Envelope e2, double tol) {
        if (e1.isNull()) {
            assertTrue(e2.isNull());
        }

        assertEquals(e1.getMinX(), e2.getMinX(), tol);
        assertEquals(e1.getMinY(), e2.getMinY(), tol);
        assertEquals(e1.getMaxX(), e2.getMaxX(), tol);
        assertEquals(e1.getMaxY(), e2.getMaxY(), tol);
    }

    private void assertCoordinates(Coordinate[] coords, double[] expectedOrdinates, int dim) {
        assertEquals(expectedOrdinates.length / dim, coords.length);

        for (int i = 0; i < expectedOrdinates.length; i += dim) {
            Coordinate c = coords[i / dim];
            for (int j = 0; j < dim; j++) {
                assertEquals(expectedOrdinates[i + j], c.getOrdinate(j), EPS);
            }
        }
    }
}
