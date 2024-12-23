package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public abstract class JDBCGeographyOnlineTest extends JDBCTestSupport {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    GeometryFactory gf = new GeometryFactory();

    @Override
    protected abstract JDBCGeographyTestSetup createTestSetup();

    @SuppressWarnings("PMD.SystemPrintln")
    protected boolean isGeographySupportAvailable() throws Exception {
        boolean available = ((JDBCGeographyTestSetup) setup).isGeographySupportAvailable();
        if (!available) {
            System.out.println("Skipping geography tests as geography column support is not available");
        }
        return available;
    }

    @Test
    public void testSchema() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        SimpleFeatureType ft = dataStore.getFeatureSource(tname("geopoint")).getSchema();
        assertNotNull(ft);

        assertTrue(ft.getDescriptor(aname("geo")) instanceof GeometryDescriptor);
        assertEquals(Point.class, ft.getDescriptor("geo").getType().getBinding());

        int epsg = CRS.lookupEpsgCode(
                ((GeometryDescriptor) ft.getDescriptor(aname("geo"))).getCoordinateReferenceSystem(), false);
        assertEquals(4326, epsg);
    }

    @Test
    public void testReader() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        Query q = new Query(tname("geopoint"));

        try (FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertTrue(r.hasNext());
            while (r.hasNext()) {
                SimpleFeature f = (SimpleFeature) r.next();
                assertTrue(f.getAttribute(aname("geo")) instanceof Point);
            }
        }
    }

    @Test
    public void testBBoxLargerThanWorld() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -200, -200, 200, 200, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        try (FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertTrue(r.hasNext());
            while (r.hasNext()) {
                SimpleFeature f = (SimpleFeature) r.next();
                assertTrue(f.getAttribute(aname("geo")) instanceof Point);
            }
        }
    }

    @Test
    public void testOutsideWorld() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -300, -40, -200, 40, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        try (FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertFalse(r.hasNext());
        }
    }

    @Test
    public void testLargerThanHalfWorld() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -140, -50, 140, 50, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        try (FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertTrue(r.hasNext());
            while (r.hasNext()) {
                SimpleFeature f = (SimpleFeature) r.next();
                assertTrue(f.getAttribute(aname("geo")) instanceof Point);
            }
        }
    }

    @Test
    public void testUpdate() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        Point p = gf.createPoint(new Coordinate(1, 1));
        try (FeatureWriter fw = dataStore.getFeatureWriter(tname("geopoint"), Transaction.AUTO_COMMIT)) {
            assertTrue(fw.hasNext());
            while (fw.hasNext()) {
                SimpleFeature f = (SimpleFeature) fw.next();
                f.setDefaultGeometry(p);
                fw.write();
            }
        }

        try (FeatureReader fr = dataStore.getFeatureReader(new Query(tname("geopoint")), Transaction.AUTO_COMMIT)) {
            while (fr.hasNext()) {
                SimpleFeature f = (SimpleFeature) fr.next();
                assertEquals(p, f.getDefaultGeometry());
            }
        }
    }

    @Test
    public void testAppend() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        Point point = gf.createPoint(new Coordinate(10, 10));
        try (FeatureWriter fw = dataStore.getFeatureWriterAppend(tname("geopoint"), Transaction.AUTO_COMMIT)) {

            assertFalse(fw.hasNext());
            SimpleFeature f = (SimpleFeature) fw.next();

            f.setAttribute("name", "append");
            f.setDefaultGeometry(point);

            fw.write();
        }

        Filter filter = ff.equals(ff.property("name"), ff.literal("append"));
        Query q = new Query(tname("geopoint"), filter);

        try (FeatureReader fr = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertTrue(fr.hasNext());
            SimpleFeature f = (SimpleFeature) fr.next();
            assertEquals(point, f.getDefaultGeometry());
        }
    }

    @Test
    public void testBounds() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        ReferencedEnvelope env = dataStore.getFeatureSource(tname("geopoint")).getBounds();
        ReferencedEnvelope expected = new ReferencedEnvelope(-110, 0, 29, 49, decodeEPSG(4326));
        assertEquals(expected, env);
    }

    @Test
    public void testBboxFilter() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geo"), -120, 25, -100, 40, "EPSG:4326");
        FeatureCollection features =
                dataStore.getFeatureSource(tname("geopoint")).getFeatures(bbox);
        assertEquals(2, features.size());
    }

    @Test
    public void testDistanceMeters() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        // where does that 74000 come from? Here:
        // GeodeticCalculator gc = new GeodeticCalculator();
        // gc.setStartingGeographicPoint(0, 49);
        // gc.setDestinationGeographicPoint(1, 49);
        // System.out.println(gc.getOrthodromicDistance()); --> 73171 m (we round to 74000)

        // if the proper distance is used, we get back only one point,
        // otherwise we'll get back them all
        DWithin filter = ff.dwithin(
                ff.property(aname("geo")), ff.literal(gf.createPoint(new Coordinate(1, 49))), 74000d, "metre");
        FeatureCollection features =
                dataStore.getFeatureSource(tname("geopoint")).getFeatures(filter);
        assertEquals(1, features.size());
    }

    @Test
    public void testDistanceGreatCircle() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        // This is the example reported in the PostGIS example:
        //
        // You can see the power of GEOGRAPHY in action by calculating the how close a plane
        // flying from Seattle to London (LINESTRING(-122.33 47.606, 0.0 51.5)) comes to Reykjavik
        // (POINT(-21.96 64.15)).
        // -- Distance calculation using GEOGRAPHY (122.2km)
        // SELECT ST_Distance('LINESTRING(-122.33 47.606, 0.0 51.5)'::geography, 'POINT(-21.96
        // 64.15)':: geography);

        // adding Reykjavik
        try (FeatureWriter fw = dataStore.getFeatureWriterAppend(tname("geopoint"), Transaction.AUTO_COMMIT)) {
            SimpleFeature f = (SimpleFeature) fw.next();
            Point point = gf.createPoint(new Coordinate(-21.96, 64.15));
            f.setAttribute("name", "Reykjavik");
            f.setDefaultGeometry(point);
            fw.write();
        }

        // testing distance filter
        LineString line =
                gf.createLineString(new Coordinate[] {new Coordinate(-122.33, 47.606), new Coordinate(0.0, 51.5)});
        DWithin filter = ff.dwithin(ff.property(aname("geo")), ff.literal(line), 130000d, "metre");
        FeatureCollection features =
                dataStore.getFeatureSource(tname("geopoint")).getFeatures(filter);
        assertEquals(1, features.size());
        try (FeatureIterator fi = features.features()) {
            assertTrue(fi.hasNext());
            SimpleFeature feature = (SimpleFeature) fi.next();
            assertEquals("Reykjavik", feature.getAttribute("name"));
        }
    }

    @Test
    public void testVirtualTable() throws Exception {
        // geopoint( id:Integer; name:String; geo:Geography(Point) )
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ");
        dialect.encodeTableName(tname("geopoint"), sb);

        VirtualTable vt = new VirtualTable("geopoint_vt", sb.toString());
        dataStore.createVirtualTable(vt);

        SimpleFeatureType featureType = dataStore.getSchema("geopoint_vt");
        assertNotNull(featureType);
        assertNotNull(featureType.getGeometryDescriptor());
    }
}
