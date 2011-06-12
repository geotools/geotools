package org.geotools.jdbc;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.DWithin;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public abstract class JDBCGeographyTest extends JDBCTestSupport {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    GeometryFactory gf = new GeometryFactory();

    @Override
    protected abstract JDBCGeographyTestSetup createTestSetup();

    protected boolean isGeographySupportAvailable() throws Exception {
        boolean available = ((JDBCGeographyTestSetup) setup).isGeographySupportAvailable();
        if(!available) {
            System.out.println("Skipping geography tests as geography column support is not available");
        }
        return available;
    }

    public void testSchema() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        SimpleFeatureType ft = dataStore.getFeatureSource(tname("geopoint")).getSchema();
        assertNotNull(ft);

        assertTrue(ft.getDescriptor(aname("geo")) instanceof GeometryDescriptor);
        assertEquals(Point.class, ft.getDescriptor("geo").getType().getBinding());

        int epsg = CRS.lookupEpsgCode(((GeometryDescriptor) ft.getDescriptor(aname("geo")))
                .getCoordinateReferenceSystem(), false);
        assertEquals(4326, epsg);
    }

    public void testReader() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        Query q = new Query(tname("geopoint"));

        FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertTrue(r.hasNext());
        while (r.hasNext()) {
            SimpleFeature f = (SimpleFeature) r.next();
            assertTrue(f.getAttribute(aname("geo")) instanceof Point);
        }
        r.close();
    }
    
    public void testBBoxLargerThanWorld() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -200, -200, 200, 200, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertTrue(r.hasNext());
        while (r.hasNext()) {
            SimpleFeature f = (SimpleFeature) r.next();
            assertTrue(f.getAttribute(aname("geo")) instanceof Point);
        }
        r.close();
    }
    
    public void testOutsideWorld() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -300, -40, -200, 40, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertFalse(r.hasNext());
        r.close();
    }
    
    public void testLargerThanHalfWorld() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox("", -140, -50, 140, 50, "EPSG:4326");
        // should select everything without bombing out
        Query q = new Query(tname("geopoint"));
        q.setFilter(bbox);
        FeatureReader r = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertTrue(r.hasNext());
        while (r.hasNext()) {
            SimpleFeature f = (SimpleFeature) r.next();
            assertTrue(f.getAttribute(aname("geo")) instanceof Point);
        }
        r.close();
    }

    public void testUpdate() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        FeatureWriter fw = dataStore.getFeatureWriter(tname("geopoint"), Transaction.AUTO_COMMIT);

        Point p = gf.createPoint(new Coordinate(1, 1));

        assertTrue(fw.hasNext());
        while (fw.hasNext()) {
            SimpleFeature f = (SimpleFeature) fw.next();
            f.setDefaultGeometry(p);
            fw.write();
        }
        fw.close();

        FeatureReader fr = dataStore.getFeatureReader(new Query(tname("geopoint")),
                Transaction.AUTO_COMMIT);
        while (fr.hasNext()) {
            SimpleFeature f = (SimpleFeature) fr.next();
            assertTrue(p.equals((Point) f.getDefaultGeometry()));
        }
        fr.close();
    }

    public void testAppend() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        FeatureWriter fw = dataStore.getFeatureWriterAppend(tname("geopoint"),
                Transaction.AUTO_COMMIT);

        assertFalse(fw.hasNext());
        SimpleFeature f = (SimpleFeature) fw.next();

        Point point = gf.createPoint(new Coordinate(10, 10));
        f.setAttribute("name", "append");
        f.setDefaultGeometry(point);

        fw.write();

        Filter filter = ff.equals(ff.property("name"), ff.literal("append"));
        Query q = new Query(tname("geopoint"), filter);

        FeatureReader fr = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        f = (SimpleFeature) fr.next();
        assertTrue(point.equals((Point) f.getDefaultGeometry()));
        fr.close();
    }

    public void testBounds() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        ReferencedEnvelope env = dataStore.getFeatureSource(tname("geopoint")).getBounds();
        ReferencedEnvelope expected = new ReferencedEnvelope(-110, 0, 29, 49, CRS
                .decode("EPSG:4326"));
        assertEquals(expected, env);
    }

    public void testBboxFilter() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geo"), -120, 25, -100, 40, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("geopoint"))
                .getFeatures(bbox);
        assertEquals(2, features.size());
    }

    public void testDistanceMeters() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }

        // where does that 74000 come from? Here:
        // GeodeticCalculator gc = new GeodeticCalculator();
        // gc.setStartingGeographicPoint(0, 49);
        // gc.setDestinationGeographicPoint(1, 49);
        // System.out.println(gc.getOrthodromicDistance()); --> 73171 m (we round to 74000)

        // if the proper distance is used, we get back only one point, 
        // otherwise we'll get back them all
        DWithin filter = ff.dwithin(ff.property(aname("geo")), ff.literal(gf
                .createPoint(new Coordinate(1, 49))), 74000d, "metre");
        FeatureCollection features = dataStore.getFeatureSource(tname("geopoint")).getFeatures(
                filter);
        assertEquals(1, features.size());
    }

    public void testDistanceGreatCircle() throws Exception {
        if (!isGeographySupportAvailable()) {
            return;
        }
        
        // This is the example reported in the PostGIS example:
        //
        // You can see the power of GEOGRAPHY in action by calculating the how close a plane
        // flying from Seattle to London (LINESTRING(-122.33 47.606, 0.0 51.5)) comes to Reykjavik
        // (POINT(-21.96 64.15)).
        // -- Distance calculation using GEOGRAPHY (122.2km)
        // SELECT ST_Distance('LINESTRING(-122.33 47.606, 0.0 51.5)'::geography, 'POINT(-21.96
        // 64.15)':: geography);

        // adding Reykjavik
        FeatureWriter fw = dataStore.getFeatureWriterAppend(tname("geopoint"),
                Transaction.AUTO_COMMIT);
        SimpleFeature f = (SimpleFeature) fw.next();
        Point point = gf.createPoint(new Coordinate(-21.96, 64.15));
        f.setAttribute("name", "Reykjavik");
        f.setDefaultGeometry(point);
        fw.write();
        fw.close();

        // testing distance filter
        LineString line = gf.createLineString(new Coordinate[] { new Coordinate(-122.33, 47.606),
                new Coordinate(0.0, 51.5) });
        DWithin filter = ff.dwithin(ff.property(aname("geo")), ff.literal(line), 130000d, "metre");
        FeatureCollection features = dataStore.getFeatureSource(tname("geopoint")).getFeatures(
                filter);
        assertEquals(1, features.size());
        FeatureIterator fi = features.features();
        assertTrue(fi.hasNext());
        SimpleFeature feature = (SimpleFeature) fi.next();
        assertEquals("Reykjavik", feature.getAttribute("name"));
        fi.close();
    }
}
