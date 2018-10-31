package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test for ReferencedEnvelope3D
 *
 * @author Niels Charlier
 */
public class ReferencedEnvelope3DTest {

    @Test
    public void testEverything() {
        ReferencedEnvelope3D everything = ReferencedEnvelope3D.EVERYTHING;
        ReferencedEnvelope3D world = new ReferencedEnvelope3D(ReferencedEnvelope3D.EVERYTHING);
        assertEquals(world.getDimension(), 3);

        assertSame(everything, ReferencedEnvelope3D.EVERYTHING);
        assertNotSame(everything, world);
        assertEquals(everything, world);
        assertEquals(world, everything);

        assertFalse("This is not an empty 3d envelope", everything.isEmpty());
        assertTrue("This is a null 3d envelope", everything.isNull());

        Coordinate center = everything.centre();
        assertNotNull(center);

        double volume = everything.getVolume();
        assertTrue("volume=" + volume, Double.isInfinite(volume));

        volume = world.getVolume();
        assertTrue("volume=" + volume, Double.isInfinite(volume));

        double area = everything.getArea();
        assertTrue("area=" + area, Double.isInfinite(area));

        area = world.getArea();
        assertTrue("area=" + area, Double.isInfinite(area));

        try {
            everything.setBounds(new ReferencedEnvelope3D());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ignore
        }
        everything.setToNull();
        everything.translate(1.0, 1.0, 1.0);

        assertEquals(everything, world);
        assertEquals(world, everything);

        assertEquals(world.getMaximum(0), everything.getMaximum(0), 0.0);
        assertEquals(world.getMaximum(1), everything.getMaximum(1), 0.0);
        assertEquals(world.getMaximum(2), everything.getMaximum(2), 0.0);

        assertEquals(world.getMinimum(0), everything.getMinimum(0), 0.0);
        assertEquals(world.getMinimum(1), everything.getMinimum(1), 0.0);
        assertEquals(world.getMinimum(2), everything.getMinimum(2), 0.0);

        assertEquals(world.getMedian(0), everything.getMedian(0), 0.0);
        assertEquals(world.getMedian(1), everything.getMedian(1), 0.0);
        assertEquals(world.getMedian(2), everything.getMedian(2), 0.0);
    }

    @Test
    public void intersection() throws Exception {
        ReferencedEnvelope3D australia = new ReferencedEnvelope3D(DefaultGeographicCRS.WGS84_3D);
        australia.include(40, 110, 0);
        australia.include(10, 150, 10);

        ReferencedEnvelope3D newZealand =
                new ReferencedEnvelope3D(DefaultEngineeringCRS.CARTESIAN_3D);
        newZealand.include(50, 165, 0);
        newZealand.include(33, 180, 5);
        try {
            australia.intersection(newZealand);
            fail("Expected a mismatch of CoordinateReferenceSystem");
        } catch (MismatchedReferenceSystemException t) {
            // expected
        }
    }

    @Test
    public void include() throws Exception {
        ReferencedEnvelope3D australia = new ReferencedEnvelope3D(DefaultGeographicCRS.WGS84_3D);
        australia.include(40, 110, 0);
        australia.include(10, 150, 10);

        ReferencedEnvelope3D newZealand =
                new ReferencedEnvelope3D(DefaultEngineeringCRS.CARTESIAN_3D);
        newZealand.include(50, 165, 0);
        newZealand.include(33, 180, 5);

        try {
            australia.expandToInclude(newZealand);
            fail("Expected a mismatch of CoordinateReferenceSystem");
        } catch (MismatchedReferenceSystemException t) {
            // expected
        }
        try {
            australia.include(newZealand);
            fail("Expected a mismatch of CoordinateReferenceSystem");
        } catch (MismatchedReferenceSystemException t) {
            // expected
        }
    }

    @Test
    public void empty() {
        // ensure empty can grab a default CRS when starting from nothing
        ReferencedEnvelope3D bbox = new ReferencedEnvelope3D(); // this is empty
        assertNull(bbox.getCoordinateReferenceSystem());

        ReferencedEnvelope3D australia = new ReferencedEnvelope3D(DefaultGeographicCRS.WGS84_3D);
        australia.include(40, 110, 0);
        australia.include(10, 150, 10);

        bbox.include(australia);

        assertEquals(australia.getCoordinateReferenceSystem(), bbox.getCoordinateReferenceSystem());
        assertEquals(0, bbox.getMinZ(), 0d);
        assertEquals(10, bbox.getMaxZ(), 0d);
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

        ReferencedEnvelope worldBounds3D = bounds.transform(DefaultGeographicCRS.WGS84_3D, true);
        assertEquals(DefaultGeographicCRS.WGS84_3D, worldBounds3D.getCoordinateReferenceSystem());

        ReferencedEnvelope worldBounds2D = bounds.transform(DefaultGeographicCRS.WGS84, true);
        assertEquals(DefaultGeographicCRS.WGS84, worldBounds2D.getCoordinateReferenceSystem());
    }

    @Test
    public void testDistanceWhenMinZOfThisIsGreaterThanMaxZOfOther() {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_3D;
        ReferencedEnvelope3D a = new ReferencedEnvelope3D(2.0, 3.0, 2.0, 3.0, 2.0, 3.0, crs);
        ReferencedEnvelope3D b = new ReferencedEnvelope3D(0.0, 1.0, 0.0, 1.0, 0.0, 0.5, crs);
        assertEquals(Math.sqrt(1 * 1 + 1 * 1 + 1.5 * 1.5), a.distance(b), 0.00001);
    }

    @Test
    public void testIntersectXYZ() {
        ReferencedEnvelope3D envelope = new ReferencedEnvelope3D();

        // Nothing should intersect with the envelope when isNull() is true.
        assertTrue(envelope.isNull());
        assertFalse(envelope.intersects(1.0, 2.0, 3.0));

        double x_min = 10.0;
        double x_max = 20.0;
        double x_ave = (x_min + x_max) / 2.0;
        double y_min = 30.0;
        double y_max = 40.0;
        double y_ave = (y_min + y_max) / 2.0;
        double z_min = 50.0;
        double z_max = 60.0;
        double z_ave = (z_min + z_max) / 2.0;

        envelope.init(x_min, x_max, y_min, y_max, z_min, z_max);

        assertTrue(envelope.intersects(x_ave, y_ave, z_ave));

        // Extremes
        assertTrue(envelope.intersects(x_min, y_min, z_min));
        assertTrue(envelope.intersects(x_max, y_max, z_max));

        assertFalse(envelope.intersects(-x_ave, -y_ave, -z_ave));

        double delta = 0.1;
        assertFalse(envelope.intersects(x_min - delta, y_ave, z_ave));
        assertFalse(envelope.intersects(x_max + delta, y_ave, z_ave));
        assertFalse(envelope.intersects(x_ave, y_min - delta, z_ave));
        assertFalse(envelope.intersects(x_ave, y_max + delta, z_ave));
        assertFalse(envelope.intersects(x_ave, y_ave, z_min - delta));
        assertFalse(envelope.intersects(x_ave, y_ave, z_max + delta));
    }
}
