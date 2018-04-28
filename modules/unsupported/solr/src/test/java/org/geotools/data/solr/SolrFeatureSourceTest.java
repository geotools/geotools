/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;

public class SolrFeatureSourceTest extends SolrTestSupport {

    public void testSchema() throws Exception {
        init();
        SimpleFeatureType schema = featureSource.getSchema();
        assertNotNull(schema);

        assertNotNull(schema.getGeometryDescriptor());
        assertTrue(schema.getDescriptor("geo") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo2") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo3") instanceof GeometryDescriptor);
    }

    public void testCount() throws Exception {
        init();
        assertEquals(11, featureSource.getCount(Query.ALL));
    }

    public void testBounds() throws Exception {
        init();
        ReferencedEnvelope bounds = featureSource.getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(24, Math.round(bounds.getMaxX()));
        assertEquals(44, Math.round(bounds.getMaxY()));
    }

    public void testCountWithIsEqualFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("vendor_s"), ff.literal("D-Link"));
        Query query = new Query();
        query.setFilter(filter);
        assertEquals(4, featureSource.getCount(query));
    }

    public void testCountWithIsNotEqualFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("vendor_s"), ff.literal("D-Link"));
        Query query = new Query();
        query.setFilter(filter);
        assertEquals(7, featureSource.getCount(query));
    }

    public void testCountWithOffsetLimit() throws Exception {
        init();
        Query query = new Query();
        query.setStartIndex(5);
        query.setMaxFeatures(11);
        assertEquals(6, featureSource.getCount(query));
    }

    public void testGetFeaturesWithAndLogicFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property =
                ff.equals(ff.property("standard_ss"), ff.literal("IEEE 802.11b"));
        BBOX bbox = ff.bbox("geo", -1, -1, 10, 10, "EPSG:" + SOURCE_SRID);
        And filter = ff.and(property, bbox);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(3, features.size());
    }

    public void testGetFeaturesWithORLogicFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property1 = ff.equals(ff.property("vendor_s"), ff.literal("D-Link"));
        PropertyIsEqualTo property2 = ff.equals(ff.property("vendor_s"), ff.literal("Linksys"));
        Or filter = ff.or(property1, property2);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(4, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            assertTrue(
                    f.getAttribute("vendor_s").equals("D-Link")
                            || f.getAttribute("vendor_s").equals("Linksys"));
        }
    }

    public void testGetFeaturesWithNOTLogicFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property1 = ff.equals(ff.property("vendor_s"), ff.literal("D-Link"));
        Not filter = ff.not(property1);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(7, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            assertTrue(!f.getAttribute("vendor_s").equals("D-Link"));
        }
    }

    public void testGetFeaturesWithIdFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        Id id =
                ff.id(
                        new HashSet<FeatureId>(
                                Arrays.asList(
                                        ff.featureId(this.layerName + ".1"),
                                        ff.featureId(this.layerName + ".7"))));
        SimpleFeatureCollection features = featureSource.getFeatures(id);
        assertEquals(2, features.size());
        SimpleFeatureIterator iterator = features.features();
        assertTrue(iterator.hasNext());
        SimpleFeature f = iterator.next();
        assertTrue(!f.getAttribute(pkField).equals(1));
        assertTrue(iterator.hasNext());
        f = iterator.next();
        assertTrue(!f.getAttribute(pkField).equals(7));
    }

    public void testGetFeaturesWithBetweenFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsBetween between =
                ff.between(ff.property("speed_is"), ff.literal(0), ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(between);
        assertEquals(9, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            boolean found = false;
            if (!(f.getAttribute("speed_is") instanceof String)) {
                int v = (Integer) f.getAttribute("speed_is");
                found = (v >= 0 && v <= 150);
            } else {
                String speeds = (String) f.getAttribute("speed_is");
                for (Object s : speeds.split(";")) {
                    int si = Integer.parseInt(s.toString());
                    if (si >= 0 && si <= 150) {
                        found = true;
                        break;
                    }
                }
            }
            assertTrue(found);
        }
        between = ff.between(ff.property("speed_is"), ff.literal(160), ff.literal(300));
        features = featureSource.getFeatures(between);
        assertEquals(5, features.size());
        iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            boolean found = false;
            if (!(f.getAttribute("speed_is") instanceof String)) {
                int v = (Integer) f.getAttribute("speed_is");
                found = (v >= 160 && v <= 300);
            } else {
                String speeds = (String) f.getAttribute("speed_is");
                for (Object s : speeds.split(";")) {
                    int si = Integer.parseInt(s.toString());
                    if (si >= 160 && si <= 300) {
                        found = true;
                        break;
                    }
                }
            }
            assertTrue(found);
        }
    }

    public void testGetFeaturesWithQuery() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("modem_b"), ff.literal(true));

        Query query = new Query();
        query.setPropertyNames(new String[] {"standard_ss", "security_ss"});
        query.setFilter(filter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertEquals(8, features.size());

        SimpleFeatureIterator iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertEquals(2, feature.getAttributeCount());
            String st = (String) feature.getAttribute("standard_ss");
            assertTrue(st.contains("IEEE 802.11b"));
        } finally {
            iterator.close();
        }
    }

    public void testGetFeaturesWithSort() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        SortBy sort = ff.sort("vendor_s", SortOrder.ASCENDING);
        Query query = new Query();
        query.setSortBy(new SortBy[] {sort});

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertEquals(11, features.size());

        SimpleFeatureIterator iterator = features.features();
        SimpleFeature f;
        try {
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("Asus", f.getAttribute("vendor_s"));
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("Cisco", f.getAttribute("vendor_s"));
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("Cisco", f.getAttribute("vendor_s"));
        } finally {
            iterator.close();
        }

        sort = ff.sort("vendor_s", SortOrder.DESCENDING);
        query.setSortBy(new SortBy[] {sort});
        features = featureSource.getFeatures(query);
        iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("TP-Link", f.getAttribute("vendor_s"));
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("Linksys", f.getAttribute("vendor_s"));
            assertTrue(iterator.hasNext());
            f = iterator.next();
            assertEquals("Linksys", f.getAttribute("vendor_s"));
        } finally {
            iterator.close();
        }
    }

    public void testGetFeaturesWithOffsetLimit() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        // no sorting, let's see if the database can use native one
        q.setStartIndex(1);
        q.setMaxFeatures(1);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(1, features.size());

        // check actual iteration
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            ReferencedEnvelope fe = ReferencedEnvelope.reference(f.getBounds());
            assertEquals(10, Integer.parseInt((String) f.getAttribute("id")));
            assertFalse(it.hasNext());
        } finally {
            it.close();
        }
    }

    public void testNaturalSortingAsc() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        SimpleFeatureIterator features = featureSource.getFeatures(q).features();
        String prevId = null;
        while (features.hasNext()) {
            String currId = features.next().getID();
            if (prevId != null) assertTrue(prevId.compareTo(currId) <= 0);
            prevId = currId;
        }
        features.close();
    }

    public void testNaturalSortingDesc() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        SimpleFeatureIterator features = featureSource.getFeatures(q).features();
        String prevId = null;
        while (features.hasNext()) {
            String currId = features.next().getID();
            if (prevId != null) assertTrue(prevId.compareTo(currId) >= 0);
            prevId = currId;
        }
        features.close();
    }

    public void testGetFeaturesWithIsGreaterThanFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("speed_is"), ff.literal(300));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    public void testGetFeaturesWithIsGreaterThanOrEqualToFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThanOrEqualTo f =
                ff.greaterOrEqual(ff.property("speed_is"), ff.literal(300));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(5, features.size());
    }

    public void testGetFeaturesWithIsLessThanFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLessThan f = ff.less(ff.property("speed_is"), ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    public void testGetFeaturesWithLessThanOrEqualToFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLessThanOrEqualTo f = ff.lessOrEqual(ff.property("speed_is"), ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(9, features.size());
    }

    public void testGetFeaturesWithIsLikeFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLike f = ff.like(ff.property("standard_ss"), "IEEE 802.11?");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(11, features.size());
    }

    public void testGetFeaturesWithIsNullFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsNull f = ff.isNull(ff.property("security_ss"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testBBOXFilterWithBBOXType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.bbox("geo3", 12.5, 7.5, 14, 19, "epsg:4326");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertCovered(features, 2, 5, 6);
    }

    void assertCovered(SimpleFeatureCollection features, Integer... ids) {
        assertEquals(ids.length, features.size());

        Set<Integer> s = new HashSet<>(Arrays.asList(ids));
        for (SimpleFeatureIterator it = features.features(); it.hasNext(); ) {
            SimpleFeature f = it.next();
            s.remove(Integer.parseInt(f.getAttribute("id").toString()));
        }
        assertTrue(s.isEmpty());
    }
}
