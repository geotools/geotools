package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 *
 * @source $URL$
 */
public class GeometryCollectorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Test
    public void testCollectNull() {
        GeometryCollector collector = new GeometryCollector();
        collector.add(null);
        GeometryCollection result = collector.collect();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectNone() {
        GeometryCollector collector = new GeometryCollector();
        GeometryCollection result = collector.collect();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTwo() throws Exception {
        WKTReader reader = new WKTReader();

        GeometryCollector collector = new GeometryCollector();
        collector.setFactory(null);
        final Geometry p0 = reader.read("POINT(0 0)");
        collector.add(p0);
        final Geometry p1 = reader.read("POINT(1 1)");
        collector.add(p1);

        GeometryCollection result = collector.collect();
        assertEquals(2, result.getNumGeometries());
        assertSame(p0, result.getGeometryN(0));
        assertSame(p1, result.getGeometryN(1));
    }
    
    @Test
    public void testInvalidMultipolygon() throws Exception {
        WKTReader reader = new WKTReader();

        // three triangles that united form a rectangle with a triangular hole in the middle
        GeometryCollector collector = new GeometryCollector();
        collector.setFactory(null);
        final Geometry p0 = reader.read("POLYGON((0 0, 0 3, 3 3, 3 0, 0 0))");
        collector.add(p0);
        final Geometry p1 = reader.read("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))");
        collector.add(p1);

        GeometryCollection result = collector.collect();
        assertEquals(1, result.getNumGeometries());
        Polygon p = (com.vividsolutions.jts.geom.Polygon) result.getGeometryN(0);
        assertTrue(p.isValid());
    }

    @Test
    public void testTooMany() throws Exception {
        WKTReader reader = new WKTReader();

        GeometryCollector collector = new GeometryCollector();
        collector.setMaxCoordinates(1);
        final Geometry p0 = reader.read("POINT(0 0)");
        collector.add(p0);
        final Geometry p1 = reader.read("POINT(1 1)");
        try {
            collector.add(p1);
            fail("Should have complained about too many coordinates");
        } catch (IllegalStateException e) {
            // fine
        }
    }
    
    @Test
    public void testCRSSimple() throws Exception {
        WKTReader reader = new WKTReader();

        GeometryCollector collector = new GeometryCollector();
        collector.setFactory(null);
        final Geometry p0 = reader.read("POINT(0 0)");
        p0.setUserData(DefaultGeographicCRS.WGS84);
        collector.add(p0);
        final Geometry p1 = reader.read("POINT(1 1)");
        collector.add(p1);
        p1.setUserData(DefaultGeographicCRS.WGS84);

        GeometryCollection result = collector.collect();
        assertEquals(2, result.getNumGeometries());
        assertSame(DefaultGeographicCRS.WGS84, result.getUserData());
        assertSame(p0, result.getGeometryN(0));
        assertSame(p1, result.getGeometryN(1));
    }
    
    @Test
    public void testCRSNested() throws Exception {
        WKTReader reader = new WKTReader();

        GeometryCollector collector = new GeometryCollector();
        collector.setFactory(null);
        final Geometry p0 = reader.read("MULTIPOINT(0 0, 1 1)");
        p0.setUserData(DefaultGeographicCRS.WGS84);
        collector.add(p0);
        final Geometry p1 = reader.read("MULTIPOINT(2 2, 3 3)");
        collector.add(p1);
        p1.setUserData(DefaultGeographicCRS.WGS84);

        GeometryCollection result = collector.collect();
        assertEquals(4, result.getNumGeometries());
        assertSame(DefaultGeographicCRS.WGS84, result.getUserData());
        assertEquals(reader.read("POINT(0 0)"), result.getGeometryN(0));
    }

}
