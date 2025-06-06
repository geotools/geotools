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

package org.geotools.data.solr;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.FeatureStreams;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import si.uom.SI;

public class SolrGeometryTest extends SolrTestSupport {
    public void testBBOXLimitSplittedFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo", -185, -98, 185, 98, "EPSG:" + SOURCE_SRID);
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(11, features.size());
    }

    public void testPolygonLimitSplittedFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {-185, -98, 185, -98, 185, 98, -185, 98, -185, -98}, 2));
        Within f = ff.within(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(11, features.size());
    }

    public void testClipToWorldFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property = ff.equals(ff.property("standard_ss"), ff.literal("IEEE 802.11b"));
        BBOX bbox = ff.bbox("geo", -190, -190, 190, 190, "EPSG:" + SOURCE_SRID);
        And filter = ff.and(property, bbox);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(7, features.size());
    }

    public void testCrossesFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] {0, 0, 2, 2}, 2));
        Crosses f = ff.crosses(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.12");
        }
    }

    public void testNotCrossesFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] {0, 0, 1, 1}, 2));
        Crosses f = ff.crosses(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    public void testEqualFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {3, 2, 6, 2, 6, 7, 3, 7, 3, 2}, 2));
        Equals f = ff.equal(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.13");
        }
    }

    public void testDisjointFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] {0, 0}, 2));
        Disjoint f = ff.disjoint(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        try (final Stream<SimpleFeature> featureStream = FeatureStreams.toFeatureStream(features)) {
            final List<SimpleFeature> featuresList = featureStream.collect(Collectors.toList());
            assertEquals(11, featuresList.size());
            assertTrue("not-active.11 ID not found", featuresList.stream().anyMatch(x -> "not-active.11"
                    .equals(x.getID())));
            assertTrue("not-active.12 ID not found", featuresList.stream().anyMatch(x -> "not-active.12"
                    .equals(x.getID())));
            assertTrue(
                    "not-active.10 ID found", featuresList.stream().noneMatch(x -> "not-active.10".equals(x.getID())));
            assertTrue("not-active.1 ID found", featuresList.stream().noneMatch(x -> "not-active.1".equals(x.getID())));
        }
    }

    public void testTouchesFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] {1, 1}, 2));
        Touches f = ff.touches(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.12");
        }
    }

    public void testWithinFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {0, 0, 0, 6, 6, 6, 6, 0, 0, 0}, 2));
        Within f = ff.within(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.12");
        }
    }

    public void testOverlapsFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {5.5, 6, 7, 6, 7, 7, 5.5, 7, 5.5, 6}, 2));
        Overlaps f = ff.overlaps(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.13");
        }
    }

    public void testIntersectsFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {6, 6, 7, 6, 7, 7, 6, 7, 6, 6}, 2));
        Intersects f = ff.intersects(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.13");
        }
    }

    public void testContainsFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Polygon ls = gf.createPolygon(sf.create(new double[] {2, 2, 3, 2, 3, 3, 2, 3, 2, 2}, 2));
        Contains f = ff.contains(ff.property("geo"), ff.literal(ls));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "not-active.12");
        }
    }

    public void testDWithinFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] {1, 1}, 2));
        DWithin f = ff.dwithin(ff.property("geo"), ff.literal(ls), 3, SI.METRE.getSymbol());
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        try (final Stream<SimpleFeature> featureStream = FeatureStreams.toFeatureStream(features)) {
            final List<SimpleFeature> featuresList = featureStream.collect(Collectors.toList());
            assertEquals(5, featuresList.size());
            assertTrue("not-active.12 ID not found", featuresList.stream().anyMatch(x -> "not-active.12"
                    .equals(x.getID())));
            assertTrue("not-active.13 ID not found", featuresList.stream().anyMatch(x -> "not-active.13"
                    .equals(x.getID())));
        }
    }

    public void testBeyondFilter() throws Exception {
        init("not-active");
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        Point ls = gf.createPoint(sf.create(new double[] {1, 1}, 2));
        Beyond f = ff.beyond(ff.property("geo"), ff.literal(ls), 1, SI.METRE.getSymbol());
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        try (final Stream<SimpleFeature> featureStream = FeatureStreams.toFeatureStream(features)) {
            final List<SimpleFeature> featuresList = featureStream.collect(Collectors.toList());
            assertEquals(12, featuresList.size());
            assertTrue("not-active.13 ID not found", featuresList.stream().anyMatch(x -> "not-active.13"
                    .equals(x.getID())));
            assertTrue(
                    "not-active.12 ID found", featuresList.stream().noneMatch(x -> "not-active.12".equals(x.getID())));
        }
    }

    public void testAlternateGeometry() throws Exception {
        init("active", "geo2");
        SimpleFeatureType schema = featureSource.getSchema();
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertNotNull(gd);
        assertEquals("geo2", gd.getLocalName());

        FilterFactory ff = dataStore.getFilterFactory();
        BBOX bbox = ff.bbox("geo2", 6.5, 23.5, 7.5, 24.5, "EPSG:4326");
        SimpleFeatureCollection features = featureSource.getFeatures(bbox);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "active.9");
        }
    }
}
