/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package mil.nga.giat.data.elasticsearch;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

import static org.junit.Assert.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

public class ElasticGeometryFilterIT extends ElasticTestSupport {

    @Test
    public void testBBOXFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo", -180, -98, 180, 98, "EPSG:" + SOURCE_SRID);
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(11, features.size());
    }

    @Test
    public void testWithinPolygonFilter() throws Exception {
        init();
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] { -180, -90, 180, -90, 180, 90, -180, 90, -180, -90 }, 2));
        Within f = ff.within(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(11, features.size());
    }

    @Test
    public void testBBOXAndEqualsFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property = ff.equals(ff.property("standard_ss"),
                ff.literal("IEEE 802.11b"));
        BBOX bbox = ff.bbox("geo", -180, -180, 180, 180, "EPSG:" + SOURCE_SRID);
        And filter = ff.and(property, bbox);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(7, features.size());
    }

    @Test
    public void testCrossesFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 0, 0, 2, 2 }, 2));
        Crosses f = ff.crosses(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testNotCrossesFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 0, 0, 1, 1 }, 2));
        Crosses f = ff.crosses(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    @Test
    public void testEqualFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] { 3, 2, 6, 2, 6, 7, 3, 7, 3, 2 }, 2));
        Equals f = ff.equal(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.13");
    }

    @Test
    public void testDisjointFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] { 0, 0 }, 2));
        Disjoint f = ff.disjoint(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(2, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.13");
    }

    @Test
    public void testTouchesFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] { 1, 1 }, 2));
        Touches f = ff.touches(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testWithinFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] { 0, 0, 0, 6, 6, 6, 6, 0, 0, 0 }, 2));
        Within f = ff.within(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testOverlapsFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(
                new double[] { 5.5, 6, 7, 6, 7, 7, 5.5, 7, 5.5, 6 }, 2));
        Overlaps f = ff.overlaps(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.13");
    }

    @Test
    public void testIntersectsFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] { 6, 6, 7, 6, 7, 7, 6, 7, 6, 6 }, 2));
        Intersects f = ff.intersects(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.13");
    }

    @Test
    public void testContainsFilter() throws Exception {
        init("not-active","geo3");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] { 2, 2, 3, 2, 3, 3, 2, 3, 2, 2 }, 2));
        Contains f = ff.contains(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");
    }

    @Test
    public void testDWithinFilter() throws Exception {
        init();
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] { 0, 0 }, 2));
        DWithin f = ff.dwithin(ff.property("geo"), ff.literal(ls), 3, "m");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(2, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.01");
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.10");
    }

    @Test
    public void testBeyondFilter() throws Exception {
        init();
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] { 0, 0 }, 2));
        Beyond f = ff.beyond(ff.property("geo"), ff.literal(ls), 1, "m");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(9, features.size());
    }

    @Test
    public void testAlternateGeometry() throws Exception {
        init("active", "geo2");
        SimpleFeatureType schema = featureSource.getSchema();
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertNotNull(gd);
        assertEquals("geo2", gd.getLocalName());

        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo2", 6.5, 23.5, 7.5, 24.5, "EPSG:4326");
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.09");
    }

    @Test
    public void testOgrStyleGeoPoint() throws Exception {
        init("not-active","geo4.coordinates");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo4.coordinates", 0, 0, 5, 5, "EPSG:4326");
        assertNotNull(featureSource.getSchema().getDescriptor("geo4.coordinates"));
        assertNull(featureSource.getSchema().getDescriptor("geo4.type"));

        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        SimpleFeature feature = fsi.next();
        assertEquals(feature.getID(), "active.13");
        assertNotNull(feature.getDefaultGeometry());
    }

    @Test
    public void testGeoPointAsArray() throws Exception {
        init("active","geo5");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] { 0, 0 }, 2));
        DWithin f = ff.dwithin(ff.property("geo5"), ff.literal(ls), 3, "m");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(2, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        SimpleFeature feature = fsi.next();
        assertEquals(feature.getID(), "active.01");
        assertNotNull(feature.getDefaultGeometry());
        assertTrue(fsi.hasNext());
        feature = fsi.next();
        assertEquals(feature.getID(), "active.10");
        assertNotNull(feature.getDefaultGeometry());
    }

    @Test
    public void testBBOXCoveringDateline() throws Exception {
        init("not-active","geo");
        FilterFactory ff = dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo", 178, -98, 182, 98, "EPSG:" + SOURCE_SRID);
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(2, features.size());
    }

    @Test
    public void testBBOXBeyondDateline() throws Exception {
        init("not-active","geo");
        FilterFactory ff = dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo", 180.5, -98, 182, 98, "EPSG:" + SOURCE_SRID);
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(1, features.size());
    }

    @Test
    public void testGeoShapeAsWkt() throws Exception {
        if (client.getVersion() < 6.2) {
            // wkt unsupported prior to v6.2
            return;
        }
        init("not-active","geo6");
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 0, 0, 2, 2 }, 2));
        Crosses f = ff.crosses(ff.property("geo3"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        SimpleFeatureIterator fsi = features.features();
        assertTrue(fsi.hasNext());
        assertEquals(fsi.next().getID(), "active.12");

        sf = new PackedCoordinateSequenceFactory();
        ls = gf.createLineString(sf.create(new double[] { 0, 0, 1, 1 }, 2));
        f = ff.crosses(ff.property("geo5"), ff.literal(ls));
        features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

}
