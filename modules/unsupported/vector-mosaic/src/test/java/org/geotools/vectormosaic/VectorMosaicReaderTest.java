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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.mockito.Mockito;

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
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;
        Query q = new Query();
        // queryables is in the index, not in the granules
        Filter qequals = FF.equals(FF.property("queryables"), FF.literal("time"));
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
        // only one index hit so only one granule checked
        assertEquals(1, tracker.getGranuleNames().size());
    }

    @Test
    public void testFilterThatHitsGranulesOnly() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;

        Query q = new Query();
        // tractorid is in the granule, not in the index
        Filter qequals = FF.equals(FF.property("tractorid"), FF.literal("deere2"));
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
        // all indexes are hit, so all granules are checked
        assertEquals(3, tracker.getGranuleNames().size());
        // we access the granules more times than we instantiate the datastore, avoiding redundant
        // store creation
        assertEquals(4, tracker.getCount());
    }

    @Test
    public void testFilterThatHitsGranulesAndIndex() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;

        Query q = new Query();
        // tractorid is in the granule, not in the index
        Filter qequals = FF.equals(FF.property("tractorid"), FF.literal("deere2"));
        // queryables is in the index, not in the granules
        Filter qequals2 = FF.equals(FF.property("queryables"), FF.literal("acreage"));
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
        // only one index hit and only one granule checked
        assertEquals(1, tracker.getGranuleNames().size());
    }

    @Test
    public void testFilterThatMatchesNeitherGranulesNorIndex() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;

        Query q = new Query();
        // tractorid is in the granule, not in the index
        Filter qequals = FF.equals(FF.property("tractorid"), FF.literal("deere5"));
        // queryables is in the index, not in the granules
        Filter qequals2 = FF.equals(FF.property("queryables"), FF.literal("province"));
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
        // No granules are checked because the index filter doesn't match
        assertEquals(0, tracker.getGranuleNames().size());
    }

    @Test
    public void testBBoxFilter() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;

        Query q = new Query();
        Filter fBbox =
                FF.bbox("the_geom", -271.40625, -136.40625, 271.40625, 136.40625, "EPSG:4326");
        // tractorid is in the granule, not in the index
        Filter qequals = FF.equals(FF.property("tractorid"), FF.literal("deere2"));

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
        // all indexes are hit, so all granules are checked
        assertEquals(3, tracker.getGranuleNames().size());
    }

    @Test
    public void testLikeFilter() throws Exception {
        SimpleFeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        GranuleTracker tracker = new GranuleTracker();
        GranuleStoreFinder finder = ((VectorMosaicFeatureSource) featureSource).finder;
        finder.granuleTracker = tracker;

        Query q = new Query();
        Filter fBbox =
                FF.bbox("the_geom", -271.40625, -136.40625, 271.40625, 136.40625, "EPSG:4326");
        // tractorid is in the granule, not in the index
        Filter qequals = FF.like(FF.property("tractorid"), "deere*");

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
        // all indexes are hit, so all granules are checked
        assertEquals(3, tracker.getGranuleNames().size());
    }

    @Test
    public void avoidSchemaRecalculation() throws Exception {
        VectorMosaicFeatureSource featureSource =
                (VectorMosaicFeatureSource) MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        VectorMosaicFeatureSource spy = spy(featureSource);

        // force it to read some features (count iterates here)
        assertEquals(4, DataUtilities.count(spy.getFeatures()));

        // the schema was computed once and then no more (when running stand alone, it happens
        // once, when running along with other tests, schema has been computed already)
        verify(spy, Mockito.atMost(1)).getFeatureType(any(), any());
    }
}
