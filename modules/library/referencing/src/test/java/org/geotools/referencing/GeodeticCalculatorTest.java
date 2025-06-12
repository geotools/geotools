/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.csvreader.CsvReader;
import java.awt.Shape;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.measure.MetricPrefix;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import si.uom.SI;

/**
 * Tests the geodetic calculator.
 *
 * @version $Id$
 */
public final class GeodeticCalculatorTest {
    /** Tests some trivial azimuth directions. */
    @Test
    public void testAzimuth() {
        final double EPS = 2E-1;
        final GeodeticCalculator calculator = new GeodeticCalculator();
        assertTrue(calculator.getCoordinateReferenceSystem() instanceof GeographicCRS);
        calculator.setStartingGeographicPoint(12, 20);
        calculator.setDestinationGeographicPoint(13, 20);
        assertEquals("East", 90, calculator.getAzimuth(), EPS);
        calculator.setDestinationGeographicPoint(12, 21);
        assertEquals("North", 0, calculator.getAzimuth(), EPS);
        calculator.setDestinationGeographicPoint(11, 20);
        assertEquals("West", -90, calculator.getAzimuth(), EPS);
        calculator.setDestinationGeographicPoint(12, 19);
        assertEquals("South", 180, calculator.getAzimuth(), EPS);
    }

    /**
     * Test path on the 45th parallel. Data for this test come from the <A
     * HREF="http://www.univ-lemans.fr/~hainry/articles/loxonavi.html">Orthodromie et loxodromie</A> page.
     */
    @Test
    @SuppressWarnings("fallthrough")
    public void testParallel45() {
        // Column 1: Longitude difference in degrees.
        // Column 2: Orthodromic distance in kilometers
        // Column 3: Loxodromic  distance in kilometers
        final double[] DATA = {
            0.00, 0, 0, 11.25, 883, 884, 22.50, 1762, 1768, 33.75, 2632, 2652, 45.00, 3489, 3536, 56.25, 4327, 4419,
            67.50, 5140, 5303, 78.75, 5923, 6187, 90.00, 6667, 7071, 101.25, 7363, 7955, 112.50, 8002, 8839, 123.75,
            8573, 9723, 135.00, 9064, 10607, 146.25, 9463, 11490, 157.50, 9758, 12374, 168.75, 9939, 13258, 180.00,
            10000, 14142
        };
        final double R = 20000 / Math.PI;
        final DefaultEllipsoid ellipsoid = DefaultEllipsoid.createEllipsoid("Test", R, R, MetricPrefix.KILO(SI.METRE));
        final GeodeticCalculator calculator = new GeodeticCalculator(ellipsoid);
        calculator.setStartingGeographicPoint(0, 45);
        for (int i = 0; i < DATA.length; i += 3) {
            calculator.setDestinationGeographicPoint(DATA[i], 45);
            final double orthodromic = calculator.getOrthodromicDistance();
            //          final double loxodromic  = calculator. getLoxodromicDistance();
            assertEquals("Orthodromic distance", DATA[i + 1], orthodromic, 0.75);
            //          assertEquals( "Loxodromic distance", DATA[i+2], loxodromic,  0.75);
            /*
             * Test the orthodromic path. We compare its length with the expected length.
             */
            int count = 0;
            double length = 0, lastX = Double.NaN, lastY = Double.NaN;
            final Shape path = calculator.getGeodeticCurve(1000);
            final PathIterator iterator = path.getPathIterator(null, 0.1);
            final double[] buffer = new double[6];
            while (!iterator.isDone()) {
                switch (iterator.currentSegment(buffer)) {
                    case PathIterator.SEG_LINETO: {
                        count++;
                        length += ellipsoid.orthodromicDistance(lastX, lastY, buffer[0], buffer[1]);
                        // Fall through
                    }
                    case PathIterator.SEG_MOVETO: {
                        lastX = buffer[0];
                        lastY = buffer[1];
                        break;
                    }
                    default: {
                        throw new IllegalPathStateException();
                    }
                }
                iterator.next();
            }
            assertEquals("Segment count", 1001, count); // Implementation check; will no longer be
            // valid when the path will contains curves.
            assertEquals("Orthodromic path length", orthodromic, length, 1E-4);
        }
    }

    /**
     * Tests geodetic calculator involving a coordinate operation. Our test uses a simple geographic CRS with only the
     * axis order interchanged.
     */
    @Test
    public void testUsingTransform() throws FactoryException, TransformException {
        final GeographicCRS crs = new DefaultGeographicCRS(
                "Test",
                DefaultGeodeticDatum.WGS84,
                new DefaultEllipsoidalCS(
                        "Test", DefaultCoordinateSystemAxis.LATITUDE, DefaultCoordinateSystemAxis.LONGITUDE));
        final GeodeticCalculator calculator = new GeodeticCalculator(crs);
        assertSame(crs, calculator.getCoordinateReferenceSystem());

        final double x = 45;
        final double y = 30;
        calculator.setStartingPosition(new Position2D(x, y));
        Point2D point = calculator.getStartingGeographicPoint();
        assertEquals(y, point.getX(), 1E-5);
        assertEquals(x, point.getY(), 1E-5);

        calculator.setDirection(10, 100);
        Position position = calculator.getDestinationPosition();
        point = calculator.getDestinationGeographicPoint();
        assertEquals(point.getX(), position.getOrdinate(1), 1E-5);
        assertEquals(point.getY(), position.getOrdinate(0), 1E-5);
    }

    /**
     * Tests orthrodromic distance on the equator. The main purpose of this method is actually to get Java assertions to
     * be run, which will compare the Geodetic Calculator results with the Default Ellipsoid computations.
     */
    @Test
    public void testEquator() {
        assertTrue(GeodeticCalculator.class.desiredAssertionStatus());
        final GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(0, 0);
        double last = Double.NaN;
        for (double x = 0; x <= 180; x += 0.125) {
            calculator.setDestinationGeographicPoint(x, 0);
            final double distance = calculator.getOrthodromicDistance() / 1000; // In kilometers
            /*
             * Checks that the increment is constant and then tapers off.
             */
            assertTrue(
                    x == 0
                            ? (distance == 0)
                            : (x < 179.5 ? (Math.abs(distance - last - 13.914936) < 2E-6) : (distance - last < 13)));
            last = distance;
        }
    }

    @Test
    public void testGEOT1535() {
        final GeodeticCalculator calculator = new GeodeticCalculator();

        calculator.setStartingGeographicPoint(10, 40);
        calculator.setDestinationGeographicPoint(-175, -30);

        calculator.setStartingGeographicPoint(180, 40);
        calculator.setDestinationGeographicPoint(-5, -30);
    }

    @Test
    public void testGEOT3826() {
        final GeodeticCalculator calculator = new GeodeticCalculator();

        calculator.setStartingGeographicPoint(0, 0);
        calculator.setDestinationGeographicPoint(0, 90);
        assertEquals(0.0, calculator.getAzimuth(), 0.0);
        assertEquals(1.0001966E7, calculator.getOrthodromicDistance(), 1);
    }

    @Test
    public void testGEOT4207() {
        Point2D startPoint = new Point2D.Double(-33.56099261594231, 1.480512392340082);
        Point2D destPoint = new Point2D.Double(-33.56099261594231, 1.4805123923400947);
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(startPoint);
        calculator.setDestinationGeographicPoint(destPoint);
        assertEquals(0.001, calculator.getOrthodromicDistance(), 0.001);
    }

    @Test
    public void testGEOT4604() {
        Point2D startPoint = new Point2D.Double(8.54, 47.38);

        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(startPoint);
        calculator.setDirection(0, 100000);
        Point2D dest = calculator.getDestinationGeographicPoint();
        double len = calculator.getOrthodromicDistance();
        assertEquals(100000.0, len, 1.0);
        assertEquals(8.54, dest.getX(), 1.0E-5);
        assertEquals(48.27938, dest.getY(), 1.0E-5);
        calculator.setStartingGeographicPoint(startPoint);
        calculator.setDirection(90, 100000);
        dest = calculator.getDestinationGeographicPoint();
        len = calculator.getOrthodromicDistance();
        assertEquals(100000.0, len, 1.0);
        assertEquals(9.86411116, dest.getX(), 1.0E-5);
        assertEquals(47.37235197, dest.getY(), 1.0E-5);
    }

    @Test
    public void testGEOT6026() {
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(0, 90);
        calculator.setDirection(10, 50_000);
        Point2D dest = calculator.getDestinationGeographicPoint();
        assertEquals(170.0, dest.getX(), 1.0E-5);
        assertEquals(89.5523482, dest.getY(), 1.0E-5);
    }

    @Test
    @Ignore // cannot make this time assumptions on containerized builds...
    public void testGEOT6077() {
        long start = System.currentTimeMillis();
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(6.95997388, 50.9383611);
        calculator.setDirection(-156.512, 13.04);
        calculator.getDestinationGeographicPoint();
        long end = System.currentTimeMillis();
        long timeDelta = end - start;
        assertTrue(timeDelta < 10);
    }

    @Test
    public void testGetPathAlongLongitude() {
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(0, 10);
        calculator.setDestinationGeographicPoint(0, -10);
        List<Point2D> path = calculator.getGeodeticPath(19);
        double y = 10.0;
        for (Point2D p : path) {
            assertEquals(y, p.getY(), 0.001);
            y--;
        }
    }

    @Test
    public void testGetPathAlongLatitude() {
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(10, 0);
        calculator.setDestinationGeographicPoint(-10, 0);
        List<Point2D> path = calculator.getGeodeticPath(19);
        double x = 10.0;
        for (Point2D p : path) {
            assertEquals(x, p.getX(), 0.001);
            x--;
        }
    }

    @Test
    public void testVincentyFails() throws FileNotFoundException, IOException {
        // check pairs of points known to fail with the Vincenty method
        // taken from Wikipedia Talk page.
        // https://en.wikipedia.org/wiki/Talk:Geodesics_on_an_ellipsoid#Computations
        try (InputStream in = TestData.openStream(this, "vincenty.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            CsvReader creader = new CsvReader(reader);
            creader.setComment('#');
            creader.setUseComments(true);
            while (creader.readRecord()) {

                double lat1 = Double.parseDouble(creader.get(0));
                double lon1 = Double.parseDouble(creader.get(1));
                double lat2 = Double.parseDouble(creader.get(2));
                double lon2 = Double.parseDouble(creader.get(3));

                GeodeticCalculator calculator = new GeodeticCalculator();
                calculator.setStartingGeographicPoint(lon1, lat1);
                calculator.setDestinationGeographicPoint(lon2, lat2);
                double dist = calculator.getOrthodromicDistance();
                // really we are just proving it works as previous code failed
                // to converge for these points.
                // but this way there is no chance of optimising the call away.
                assertTrue("Bad distance calculation", dist > 0.0d);
            }
        }
    }
}
