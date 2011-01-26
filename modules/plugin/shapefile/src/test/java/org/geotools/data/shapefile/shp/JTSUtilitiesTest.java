package org.geotools.data.shapefile.shp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

public class JTSUtilitiesTest {
    GeometryFactory geomFactory;

    @Before
    public void setUp() {
        geomFactory = new GeometryFactory();
    }

    @After
    public void tearDown() {
        geomFactory = null;
    }

    @Test
    public void testReverseRing() {
        Coordinate[] coordinates = new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1),
                new Coordinate(0, 2), new Coordinate(0, 0) };
        LinearRing before = geomFactory.createLinearRing(coordinates);
        assertEquals(before.getCoordinateN(0), coordinates[0]);
        assertEquals(before.getCoordinateN(1), coordinates[1]);
        assertEquals(before.getCoordinateN(2), coordinates[2]);
        assertEquals(before.getCoordinateN(3), coordinates[3]);

        LinearRing after = JTSUtilities.reverseRing(before);

        // cannot use assertEquals since JTS has two equals and we need
        // to hit the .equals(Geometry) one instead of .equals(Object) one
        assertTrue( after.equals(before.reverse()) );

        assertEquals(after.getCoordinateN(0), coordinates[3]);
        assertEquals(after.getCoordinateN(1), coordinates[2]);
        assertEquals(after.getCoordinateN(2), coordinates[1]);
        assertEquals(after.getCoordinateN(3), coordinates[0]);
    }
}
