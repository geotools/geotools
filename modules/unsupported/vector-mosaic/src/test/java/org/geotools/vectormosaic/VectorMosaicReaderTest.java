/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;

public class VectorMosaicReaderTest extends VectorMosaicTest {

    @Test
    public void testDelegateGeomFieldNameDifferentFromGranule() throws Exception {
        SimpleFeatureSource featureSource =
                MOSAIC_STORE_FROM_PROPERTIES.getFeatureSource(MOSAIC_PROPERTIES_TYPE_NAME);
        Query q = new Query();
        Filter intersects =
                FF.intersects(
                        FF.property("the_geom"),
                        FF.literal(
                                new ReferencedEnvelope(
                                        -82, 36, -76, 40, DefaultGeographicCRS.WGS84)));
        q.setFilter(intersects);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            assertEquals(1, count);
        }
    }

    @Test
    public void testFilterThatHitsIndexOnly() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;
        Query q = new Query();
        Filter qequals =
                FF.equals(
                        FF.property("queryables"),
                        FF.literal("time")); // queryables is in the index, not in the granules
        q.setFilter(qequals);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertEquals("time", feature.getAttribute("queryables"));
                count++;
            }
            assertEquals(1, count);
        }
        assertEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertEquals(1, tracker.size()); // only one index hit so only one granule checked
    }

    @Test
    public void testFilterThatHitsGranulesOnly() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;

        Query q = new Query();
        Filter qequals =
                FF.equals(
                        FF.property("tractorid"),
                        FF.literal("deere2")); // tractorid is in the granule, not in the index
        q.setFilter(qequals);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                SimpleFeature simpleFeature = iterator.next();
                assertEquals("deere2", simpleFeature.getAttribute("tractorid"));
                assertNotEquals("deer3", simpleFeature.getAttribute("tractorid"));
                count++;
            }
            assertEquals(1, count);
        }
        assertEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertEquals(3, tracker.size()); // all indexes are hit, so all granules are checked
    }

    @Test
    public void testFilterThatHitsGranulesAndIndex() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;

        Query q = new Query();
        Filter qequals =
                FF.equals(
                        FF.property("tractorid"),
                        FF.literal("deere2")); // tractorid is in the granule, not in the index

        Filter qequals2 =
                FF.equals(
                        FF.property("queryables"),
                        FF.literal("acreage")); // queryables is in the index, not in the granules
        Filter and = FF.and(qequals, qequals2);
        q.setFilter(and);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                SimpleFeature simpleFeature = iterator.next();
                assertEquals("deere2", simpleFeature.getAttribute("tractorid"));
                assertNotEquals("deer3", simpleFeature.getAttribute("tractorid"));
                count++;
            }
            assertEquals(1, count);
        }
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertEquals(1, tracker.size()); // only one index hit and only one granule checked
    }

    @Test
    public void testFilterThatMatchesNeitherGranulesNorIndex() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;

        Query q = new Query();
        Filter qequals =
                FF.equals(
                        FF.property("tractorid"),
                        FF.literal("deere5")); // tractorid is in the granule, not in the index

        Filter qequals2 =
                FF.equals(
                        FF.property("queryables"),
                        FF.literal("province")); // queryables is in the index, not in the granules
        Filter and = FF.and(qequals, qequals2);
        q.setFilter(and);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                count++;
            }
            assertEquals(0, count);
        }
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertEquals(
                0,
                tracker.size()); // No granules are checked because the index filter doesn't match
    }

    @Test
    public void testBBoxFilter() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;

        Query q = new Query();
        Filter fBbox =
                FF.bbox("the_geom", -271.40625, -136.40625, 271.40625, 136.40625, "EPSG:4326");

        Filter qequals =
                FF.equals(
                        FF.property("tractorid"),
                        FF.literal("deere2")); // tractorid is in the granule, not in the index

        Filter and = FF.and(fBbox, qequals);
        q.setFilter(and);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                SimpleFeature simpleFeature = iterator.next();
                assertEquals("deere2", simpleFeature.getAttribute("tractorid"));
                assertNotEquals("deer3", simpleFeature.getAttribute("tractorid"));
                count++;
            }
            assertEquals(1, count);
        }
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertEquals(3, tracker.size()); // all indexes are hit, so all granules are checked
    }

    @Test
    public void testLikeFilter() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Set<String> tracker = new HashSet<>();
        ((VectorMosaicFeatureSource) featureSource).granuleTracker = tracker;

        Query q = new Query();
        Filter fBbox =
                FF.bbox("the_geom", -271.40625, -136.40625, 271.40625, 136.40625, "EPSG:4326");

        Filter qequals =
                FF.like(
                        FF.property("tractorid"),
                        "deere*"); // tractorid is in the granule, not in the index

        Filter and = FF.and(fBbox, qequals);
        q.setFilter(and);
        SimpleFeatureCollection fc = featureSource.getFeatures(q);
        try (SimpleFeatureIterator iterator = fc.features(); ) {
            int count = 0;
            while (iterator.hasNext()) {
                SimpleFeature simpleFeature = iterator.next();
                assertTrue(simpleFeature.getAttribute("tractorid").toString().startsWith("deere"));
                count++;
            }
            assertEquals(3, count);
        }
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getDelegateFilter());
        assertNotEquals(
                Filter.INCLUDE,
                ((VectorMosaicFeatureSource) featureSource).filterTracker.getGranuleFilter());
        assertEquals(3, tracker.size()); // all indexes are hit, so all granules are checked
    }
}
