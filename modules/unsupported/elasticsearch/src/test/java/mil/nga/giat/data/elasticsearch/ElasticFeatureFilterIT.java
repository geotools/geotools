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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
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

import mil.nga.giat.data.elasticsearch.ElasticDataStore.ArrayEncoding;

public class ElasticFeatureFilterIT extends ElasticTestSupport {

    @Test
    public void testSchema() throws Exception {
        init();
        SimpleFeatureType schema = featureSource.getSchema();
        assertNotNull(schema);

        assertNotNull(schema.getGeometryDescriptor());
        assertTrue(schema.getDescriptor("geo") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo2") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo3") instanceof GeometryDescriptor);
        assertTrue(schema.getDescriptor("geo5") instanceof GeometryDescriptor);
    }

    @Test
    public void testSchemaWithoutLayerConfig() throws Exception {
        init();
        ElasticFeatureSource featureSource = new ElasticFeatureSource(new ContentEntry(dataStore, new NameImpl("invalid")),null);
        SimpleFeatureType schema = featureSource.getSchema();        
        assertNotNull(schema);
        assertTrue(schema.getAttributeCount()==0);
    }

    @Test
    public void testSchemaWithShortName() throws Exception {
        init();
        ElasticLayerConfiguration layerConfig = dataStore.getLayerConfigurations().get("active");
        for (ElasticAttribute attribute : layerConfig.getAttributes()) {
            attribute.setUseShortName(true);
        }
        SimpleFeatureType schema = featureSource.getSchema();
        assertNotNull(schema);
        assertNotNull(schema.getDescriptor("hejda"));
    }

    @Test @Ignore
    public void testSchemaWithInvalidSrid() throws Exception {
        init();
        ElasticLayerConfiguration layerConfig = dataStore.getLayerConfigurations().get("active");
        for (ElasticAttribute attribute : layerConfig.getAttributes()) {
            attribute.setSrid(-1);
        }
        SimpleFeatureType schema = featureSource.getSchema();
        assertNotNull(schema);
        assertNull(schema.getGeometryDescriptor());
        assertNull(schema.getDescriptor("geo"));
    }

    @Test
    public void testCount() throws Exception {
        init();
        assertEquals(11, featureSource.getCount(Query.ALL));
    }

    @Test
    public void testBounds() throws Exception {
        init();
        ReferencedEnvelope bounds = featureSource.getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(24, Math.round(bounds.getMaxX()));
        assertEquals(44, Math.round(bounds.getMaxY()));
    }

    @Test
    public void testCountWithIsEqualFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("vendor_s"), ff.literal("D-Link"));
        Query query = new Query();
        query.setFilter(filter);
        assertEquals(4, featureSource.getCount(query));
    }

    @Test
    public void testCountWithIsNotEqualFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("vendor_s"), ff.literal("D-Link"));
        Query query = new Query();
        query.setFilter(filter);
        assertEquals(7, featureSource.getCount(query));
    }

    @Test
    public void testCountWithOffsetLimit() throws Exception {
        init();
        Query query = new Query();
        query.setStartIndex(5);
        query.setMaxFeatures(11);

        assertEquals(6, featureSource.getCount(query));
    }

    @Test
    public void testGetFeaturesWithAndLogicFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property = ff.equals(ff.property("standard_ss"),
                ff.literal("IEEE 802.11b"));
        BBOX bbox = ff.bbox("geo", -1, -1, 10, 10, "EPSG:" + SOURCE_SRID);
        And filter = ff.and(property, bbox);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(3, features.size());
    }

    @Test
    public void testGetFeaturesWithORLogicFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property1 = ff.equals(ff.property("vendor_s"), ff.literal("D-Link"));
        PropertyIsEqualTo property2 = ff.equals(ff.property("vendor_s"), ff.literal("Linksys"));
        Or filter = ff.or(property1, property2);
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(6, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            assertTrue(f.getAttribute("vendor_s").equals("D-Link")
                    || f.getAttribute("vendor_s").equals("Linksys"));
        }
    }

    @Test
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

    @Test
    public void testGetFeaturesWithIdFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        Id id = ff.id(new HashSet<FeatureId>(Arrays.asList(ff.featureId("01"),
                ff.featureId("07"))));
        SimpleFeatureCollection features = featureSource.getFeatures(id);
        assertEquals(2, features.size());
    }

    @Test
    public void testGetFeaturesWithBetweenFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsBetween between = ff.between(ff.property("speed_is"), ff.literal(0),
                ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(between);
        assertEquals(9, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            boolean found = false;
            if (f.getAttribute("speed_is") instanceof List) {
                for (Object obj : (List<?>) f.getAttribute("speed_is")) {
                    int v = (Integer) obj;
                    if (v >= 0 && v <= 150) {
                        found = true;
                        break;
                    }
                }
            } else {
                int v = (Integer) f.getAttribute("speed_is");
                found = (v >= 0 && v <= 150);
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
            if (f.getAttribute("speed_is") instanceof List) {
                for (Object obj : (List<?>) f.getAttribute("speed_is")) {
                    int v = (Integer) obj;
                    if (v >= 160 && v <= 300) {
                        found = true;
                        break;
                    }
                }
            } else {
                int v = (Integer) f.getAttribute("speed_is");
                found = (v >= 160 && v <= 300);
            }
            assertTrue(found);
        }
    }

    @Test
    public void testGetFeaturesWithQuery() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("modem_b"), ff.literal(true));

        Query query = new Query();
        query.setPropertyNames(new String[] { "standard_ss", "security_ss" });
        query.setFilter(filter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertEquals(8, features.size());

        SimpleFeatureIterator iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertEquals(2, feature.getAttributeCount());
            String st = (String) feature.getAttribute("standard_ss");
            // changed from "IEEE 802.11b" in SolrFeatureSourceTest
            assertTrue(st.contains("IEEE 802.11b"));
        } finally {
            iterator.close();
        }
    }

    @Test
    public void testReadStringArrayWithCsvStrategy() throws Exception {
        init();
        dataStore.setArrayEncoding(ArrayEncoding.CSV);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("modem_b"), ff.literal(true));

        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(8, features.size());

        SimpleFeatureIterator iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();
            String st = (String) feature.getAttribute("standard_ss");
            // changed from "IEEE 802.11b" in SolrFeatureSourceTest
            assertTrue(URLDecoder.decode(st, StandardCharsets.UTF_8.toString()).startsWith("IEEE 802.11"));
        } finally {
            iterator.close();
        }
    }

    @Test
    public void testReadNumericArrayWithCsvStrategy() throws Exception {
        init();
        dataStore.setArrayEncoding(ArrayEncoding.CSV);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsBetween between = ff.between(ff.property("speed_is"), ff.literal(160), ff.literal(300));
        SimpleFeatureCollection features = featureSource.getFeatures(between);
        assertEquals(5, features.size());
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature f = iterator.next();
            assertFalse(f.getAttribute("speed_is") instanceof List);
        }
    }

    @Test
    public void testGetFeaturesWithSort() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        SortBy sort = ff.sort("vendor_s", SortOrder.ASCENDING);
        Query query = new Query();
        query.setSortBy(new SortBy[] { sort });

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
        query.setSortBy(new SortBy[] { sort });
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

    @Test
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
            assertEquals(2, Integer.parseInt((String) f.getAttribute("id")));
            assertFalse(it.hasNext());
        } finally {
            it.close();
        }
    }

    @Test
    public void testNaturalSortingAsc() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
        SimpleFeatureIterator features = featureSource.getFeatures(q).features();
        String prevId = null;
        while (features.hasNext()) {
            String currId = features.next().getID();
            if (prevId != null)
                assertTrue(prevId.compareTo(currId) <= 0);
            prevId = currId;
        }
        features.close();
    }

    @Test
    public void testNaturalSortingDesc() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] { SortBy.REVERSE_ORDER });
        SimpleFeatureIterator features = featureSource.getFeatures(q).features();
        String prevId = null;
        while (features.hasNext()) {
            String currId = features.next().getID();
            if (prevId != null)
                assertTrue(prevId.compareTo(currId) >= 0);
            prevId = currId;
        }
        features.close();
    }

    @Test
    public void testGetFeaturesWithIsGreaterThanFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("speed_is"), ff.literal(300));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    @Test
    public void testGetFeaturesWithIsGreaterThanOrEqualToFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThanOrEqualTo f = ff.greaterOrEqual(ff.property("speed_is"),
                ff.literal(300));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(5, features.size());
    }

    @Test
    public void testGetFeaturesWithIsLessThanFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLessThan f = ff.less(ff.property("speed_is"), ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(0, features.size());
    }

    @Test
    public void testGetFeaturesWithLessThanOrEqualToFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLessThanOrEqualTo f = ff.lessOrEqual(ff.property("speed_is"), ff.literal(150));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(9, features.size());
    }

    @Test
    public void testGetFeaturesWithIsLikeFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsLike f = ff.like(ff.property("standard_ss"), "IEEE 802.11?");
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(11, features.size());
    }

    @Test
    public void testGetFeaturesWithIsNullFilter() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsNull f = ff.isNull(ff.property("security_ss"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    @Test
    public void testBBOXFilterWithBBOXType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.bbox("geo3", 12.5, 7.5, 14, 19, "epsg:4326");

        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertCovered(features, 2, 5, 6);
    }

    @Test
    public void testOnlyStoredFields() throws Exception {
        init();
        Name name = new NameImpl("active");
        for (final ElasticAttribute attribute : dataStore.getElasticAttributes(name) ){
            if (!attribute.isStored()) {
                attribute.setUse(false);
            }
        }
        assertEquals(11, featureSource.getCount(Query.ALL));
        SimpleFeatureIterator features = featureSource.getFeatures().features();
        for (int i=0; i<11; i++) {
            assertTrue(features.hasNext());
            features.next();
        }
    }

    @Test
    public void testOnlySourceFields() throws Exception {
        init();
        Name name = new NameImpl("active");
        for (final ElasticAttribute attribute : dataStore.getElasticAttributes(name) ){
            if (attribute.isStored()) {
                attribute.setUse(false);
            }
        }
        featureSource = (ElasticFeatureSource) dataStore.getFeatureSource(TYPE_NAME);

        assertEquals(11, featureSource.getCount(Query.ALL));

        SimpleFeatureIterator features = featureSource.getFeatures().features();
        for (int i=0; i<11; i++) {
            assertTrue(features.hasNext());
            features.next();
        }
    }

    @Test
    public void testOnlyStoredFieldsWithSourceFiltering() throws Exception {
        init();
        dataStore.setSourceFilteringEnabled(true);
        Name name = new NameImpl("active");
        for (final ElasticAttribute attribute : dataStore.getElasticAttributes(name) ){
            if (!attribute.isStored()) {
                attribute.setUse(false);
            }
        }
        assertEquals(11, featureSource.getCount(Query.ALL));
        SimpleFeatureIterator features = featureSource.getFeatures().features();
        for (int i=0; i<11; i++) {
            assertTrue(features.hasNext());
            features.next();
        }
    }

    @Test
    public void testOnlySourceFieldsWithSourceFiltering() throws Exception {
        init();
        dataStore.setSourceFilteringEnabled(true);
        Name name = new NameImpl("active");
        for (final ElasticAttribute attribute : dataStore.getElasticAttributes(name) ){
            if (attribute.isStored()) {
                attribute.setUse(false);
            }
        }
        featureSource = (ElasticFeatureSource) dataStore.getFeatureSource(TYPE_NAME);

        assertEquals(11, featureSource.getCount(Query.ALL));

        SimpleFeatureIterator features = featureSource.getFeatures().features();
        for (int i=0; i<11; i++) {
            assertTrue(features.hasNext());
            features.next();
        }
    }

    /**
     * This test ensures that when specifying properties in a query with source filtering enabled you only get back the
     * properties specified. If properties are not specified or Query.ALL_PROPERTIES is used then you get everything.
     */
    @Test
    public void testFieldsWithSourceFiltering() throws Exception {
        init();
        dataStore.setSourceFilteringEnabled(true);

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("modem_b"), ff.literal(true));

        Query query = new Query();
        query.setFilter(filter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);

        try(SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();

            Assert.assertTrue(feature.getProperties().size() > 2);
        }

        // Specify Columns
        query.setPropertyNames(new String[] { "standard_ss", "security_ss" });

        features = featureSource.getFeatures(query);

        try(SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();

            Iterator<Property> propertyIterator = feature.getProperties().iterator();

            Assert.assertEquals(query.getPropertyNames().length, feature.getProperties().size());
            Assert.assertEquals(query.getPropertyNames()[0], propertyIterator.next().getName().getLocalPart());
            Assert.assertEquals(query.getPropertyNames()[1], propertyIterator.next().getName().getLocalPart());
        }

        // Specify All
        query.setProperties(Query.ALL_PROPERTIES);

        features = featureSource.getFeatures(query);

        try(SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();

            Assert.assertTrue(feature.getProperties().size() > 2);
        }
    }

    @Test
    public void testGetFeaturesWithIsGreaterThanFilterOnObjectType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("object.hejda"), ff.literal(10));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(6, features.size());
    }

    @Test
    public void testGetFeaturesWithIsGreaterThanFilterOnNestedType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("nested.hej"), ff.literal(10));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(8, features.size());
    }

    @Test
    public void testGetFeaturesWithIsBetweenFilterOnObjectType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsBetween f = ff.between(ff.property("object.hejda"), ff.literal(5), ff.literal(15));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(5, features.size());
    }

    @Test
    public void testGetFeaturesWithIsBetweenFilterOnNestedType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsBetween f = ff.between(ff.property("nested.hej"), ff.literal(5), ff.literal(15));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(10, features.size());
    }

    @Test
    public void testGetFeaturesWithIsGreaterThanFilterOnNestedChildType() throws Exception {
        init();
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("nested.parent.child"), ff.literal("ba"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(8, features.size());
    }

    @Test
    public void testScrollSizesDoesntChangesOutputSize() throws Exception {
        init();
        dataStore.setScrollSize(3l);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("nested.parent.child"), ff.literal("ba"));
        List<SimpleFeature> features = readFeatures(featureSource.getFeatures(f).features());
        assertEquals(8, features.size());
    }

    @Test
    public void testScrollTimeDoesntChangesOutputSize() throws Exception {
        init();
        Integer initialScrollTime = dataStore.getScrollTime();
        dataStore.setScrollTime(initialScrollTime * 10);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("nested.parent.child"), ff.literal("ba"));
        List<SimpleFeature> features = readFeatures(featureSource.getFeatures(f).features());
        assertEquals(8, features.size());
    }

    @Test
    public void testScrollEnabledDoesntChangesOutputSize() throws Exception {
        init();
        dataStore.setScrollEnabled(true);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsGreaterThan f = ff.greater(ff.property("nested.parent.child"), ff.literal("ba"));
        List<SimpleFeature> features = readFeatures(featureSource.getFeatures(f).features());
        assertEquals(8, features.size());
    }

    @Test
    public void testScrollHonorsMaxFeatures() throws Exception {
        init();
        dataStore.setScrollSize(1l);
        Query q = new Query();
        q.setMaxFeatures(7);
        List<SimpleFeature> features = readFeatures(featureSource.getFeatures(q).features());
        assertEquals(7, features.size());
    }      

    @Test(expected=NoSuchElementException.class)
    public void testScrollNoSuchElement() throws Exception {
        init();
        dataStore.setScrollSize(1l);
        Query q = new Query();
        q.setMaxFeatures(1);
        SimpleFeatureIterator it = featureSource.getFeatures(q).features();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(!it.hasNext());
        it.next();
    }

    @Test
    public void testDefaultMaxFeatures() throws Exception {
        init();
        dataStore.setDefaultMaxFeatures(2);
        Query q = new Query();
        List<SimpleFeature> features = readFeatures(featureSource.getFeatures(q).features());
        assertEquals(2, features.size());
    }

    void assertCovered(SimpleFeatureCollection features, Integer... ids) {
        assertEquals(ids.length, features.size());

        Set<Integer> s = new HashSet<>(Arrays.asList(ids));
        SimpleFeatureIterator it = features.features();
        while (it.hasNext()) {
            SimpleFeature f = it.next();
            s.remove(Integer.parseInt(f.getAttribute("id").toString()));
        }
        assertTrue(s.isEmpty());
    }      

}
