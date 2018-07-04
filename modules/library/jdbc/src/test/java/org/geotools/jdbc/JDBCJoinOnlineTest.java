/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.geotools.data.Join;
import org.geotools.data.Join.Type;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public abstract class JDBCJoinOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCJoinTestSetup createTestSetup();

    public void testSimpleJoin() throws Exception {
        doTestSimpleJoin(false);
        doTestSimpleJoin(true);
    }

    public void testJoinSchema() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        Join join =
                new Join(
                        tname("ftjoin"),
                        ff.equal(
                                ff.property(aname("stringProperty")),
                                ff.property(aname("name")),
                                true));
        join.setAlias("b");
        q.getJoins().add(join);

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        SimpleFeatureType schema = features.getSchema();
        AttributeDescriptor ad = schema.getDescriptor("b");
        assertNotNull(ad);
        assertEquals(SimpleFeature.class, ad.getType().getBinding());
        SimpleFeatureType joinedSchema =
                (SimpleFeatureType) ad.getUserData().get(ContentDataStore.JOINED_FEATURE_TYPE);
        assertEquals(dataStore.getSchema(tname("ftjoin")), joinedSchema);
    }

    void doTestSimpleJoin(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        try (SimpleFeatureIterator ita =
                dataStore.getFeatureSource(tname("ft1")).getFeatures().features()) {
            try (SimpleFeatureIterator itb =
                    dataStore.getFeatureSource(tname("ftjoin")).getFeatures().features()) {

                FilterFactory ff = dataStore.getFilterFactory();
                Query q = new Query(tname("ft1"));
                q.getJoins()
                        .add(
                                new Join(
                                        tname("ftjoin"),
                                        ff.equal(
                                                ff.property(aname("stringProperty")),
                                                ff.property(aname("name")),
                                                true)));

                SimpleFeatureCollection features =
                        dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
                assertEquals(
                        dataStore.getFeatureSource(tname("ft1")).getFeatures(q).size(),
                        features.size());

                try (SimpleFeatureIterator it = features.features()) {
                    assertTrue(it.hasNext() && ita.hasNext() && itb.hasNext());

                    while (it.hasNext()) {
                        SimpleFeature f = it.next();
                        assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());

                        SimpleFeature g = (SimpleFeature) f.getAttribute(tname("ftjoin"));

                        SimpleFeature a = ita.next();
                        SimpleFeature b = itb.next();

                        for (int i = 0; i < a.getAttributeCount(); i++) {
                            assertAttributeValuesEqual(a.getAttribute(i), f.getAttribute(i));
                        }
                        for (int i = 0; i < b.getAttributeCount(); i++) {
                            assertAttributeValuesEqual(b.getAttribute(i), g.getAttribute(i));
                        }
                    }
                }
            }
        }
    }

    public void testSimpleJoinOnPrimaryKey() throws Exception {
        dataStore.setExposePrimaryKeyColumns(true);

        try (SimpleFeatureIterator ita =
                        dataStore.getFeatureSource(tname("ft1")).getFeatures().features();
                SimpleFeatureIterator itb =
                        dataStore.getFeatureSource(tname("ftjoin")).getFeatures().features()) {

            FilterFactory ff = dataStore.getFilterFactory();
            Query q = new Query(tname("ft1"));
            Join join =
                    new Join(
                            tname("ftjoin"),
                            ff.equal(
                                    ff.property(aname("id")),
                                    ff.property(aname("ftjoin.id")),
                                    true));
            join.setAlias(tname("ftjoin"));
            q.getJoins().add(join);

            SimpleFeatureCollection features =
                    dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
            assertEquals(
                    dataStore.getFeatureSource(tname("ft1")).getFeatures(q).size(),
                    features.size());

            try (SimpleFeatureIterator it = features.features()) {
                assertTrue(it.hasNext() && ita.hasNext() && itb.hasNext());

                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    assertEquals(6, f.getAttributeCount());

                    SimpleFeature g = (SimpleFeature) f.getAttribute(tname("ftjoin"));

                    SimpleFeature a = ita.next();
                    SimpleFeature b = itb.next();

                    for (int i = 0; i < a.getAttributeCount(); i++) {
                        assertAttributeValuesEqual(a.getAttribute(i), f.getAttribute(i));
                    }
                    for (int i = 0; i < b.getAttributeCount(); i++) {
                        assertAttributeValuesEqual(b.getAttribute(i), g.getAttribute(i));
                    }
                }
            }
        }
    }

    public void testSimpleJoinInvertedAliases() throws Exception {
        doTestSimpleJoinInvertedAliases(false);
        doTestSimpleJoinInvertedAliases(true);
    }

    void doTestSimpleJoinInvertedAliases(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        try (SimpleFeatureIterator ita =
                        dataStore.getFeatureSource(tname("ft1")).getFeatures().features();
                SimpleFeatureIterator itb =
                        dataStore.getFeatureSource(tname("ftjoin")).getFeatures().features()) {
            FilterFactory ff = dataStore.getFilterFactory();
            Query q = new Query(tname("ft1"));
            q.setAlias("b");
            Join join =
                    new Join(
                            tname("ftjoin"),
                            ff.equal(
                                    ff.property(aname("stringProperty")),
                                    ff.property(aname("name")),
                                    true));
            join.setAlias("a");
            q.getJoins().add(join);

            SimpleFeatureCollection features =
                    dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
            assertEquals(
                    dataStore.getFeatureSource(tname("ft1")).getFeatures(q).size(),
                    features.size());

            try (SimpleFeatureIterator it = features.features()) {
                assertTrue(it.hasNext() && ita.hasNext() && itb.hasNext());

                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());

                    SimpleFeature g = (SimpleFeature) f.getAttribute("a");

                    SimpleFeature a = ita.next();
                    SimpleFeature b = itb.next();

                    for (int i = 0; i < a.getAttributeCount(); i++) {
                        assertAttributeValuesEqual(a.getAttribute(i), f.getAttribute(i));
                    }
                    for (int i = 0; i < b.getAttributeCount(); i++) {
                        assertAttributeValuesEqual(b.getAttribute(i), g.getAttribute(i));
                    }
                }
            }
        }
    }

    public void testSimpleJoinWithFilter() throws Exception {
        doTestSimpleJoinWithFilter(false);
        doTestSimpleJoinWithFilter(true);
    }

    void doTestSimpleJoinWithFilter(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        q.getJoins()
                .add(
                        new Join(
                                tname("ftjoin"),
                                ff.equal(
                                        ff.property(aname("stringProperty")),
                                        ff.property(aname("name")),
                                        true)));
        q.setFilter(ff.equal(ff.property(aname("stringProperty")), ff.literal("two"), true));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            SimpleFeature f = it.next();
            assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());
            assertEquals(2, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            SimpleFeature g = (SimpleFeature) f.getAttribute(aname("ftjoin"));
            assertEquals(3 + (exposePrimaryKeys ? 1 : 0), g.getAttributeCount());
            if (exposePrimaryKeys) {
                assertEquals(2, ((Number) g.getAttribute(aname("id"))).intValue());
            }
            assertEquals("two", g.getAttribute(aname("name")));
        }
    }

    public void testSimpleJoinWithFilterNoProperties() throws Exception {
        doTestSimpleJoinWithFilterNoProperties(false);
        doTestSimpleJoinWithFilterNoProperties(true);
    }

    void doTestSimpleJoinWithFilterNoProperties(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        Join j =
                new Join(
                        tname("ftjoin"),
                        ff.equal(
                                ff.property(aname("stringProperty")),
                                ff.property(aname("name")),
                                true));
        j.setProperties(Query.NO_PROPERTIES);
        q.getJoins().add(j);
        q.setFilter(ff.equal(ff.property(aname("stringProperty")), ff.literal("two"), true));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            SimpleFeature f = it.next();
            assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());
            assertEquals(2, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            SimpleFeature g = (SimpleFeature) f.getAttribute(aname("ftjoin"));
            assertEquals(0, g.getAttributeCount());
        }
    }

    public void testSimpleJoinWithFilterCount() throws Exception {
        doTestSimpleJoinWithFilterCount(false);
        doTestSimpleJoinWithFilterCount(true);
    }

    void doTestSimpleJoinWithFilterCount(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        Join j =
                new Join(
                        tname("ftjoin"),
                        ff.equal(
                                ff.property(aname("stringProperty")),
                                ff.property(aname("name")),
                                true));
        j.filter(ff.greater(ff.property(aname("join1intProperty")), ff.literal(1)));
        q.getJoins().add(j);
        q.setFilter(ff.less(ff.property(aname("intProperty")), ff.literal(3)));

        assertEquals(1, dataStore.getFeatureSource(tname("ft1")).getCount(q));
    }

    public void testSimpleJoinWithPostFilter() throws Exception {
        doTestSimpleJoinWithPostFilter(false);
        doTestSimpleJoinWithPostFilter(true);
    }

    void doTestSimpleJoinWithPostFilter(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();

        Filter j = ff.equal(ff.property(aname("stringProperty")), ff.property(aname("name")), true);
        Query q = new Query(tname("ft1"));
        q.getJoins().add(new Join(tname("ftjoin"), j));
        q.setFilter(
                ff.equal(
                        ff.function(
                                "__equals",
                                ff.property(aname("stringProperty")),
                                ff.literal("one")),
                        ff.literal(true),
                        true));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        // test with post filter on table being joined
        q = new Query(tname("ft1"));
        Join join = new Join(tname("ftjoin"), j);
        join.filter(
                ff.equal(
                        ff.function("__equals", ff.property(aname("name")), ff.literal("one")),
                        ff.literal(true),
                        true));
        q.getJoins().add(join);

        features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());
    }

    public void testSimpleJoinWithPostFilterNoProperties() throws Exception {
        doTestSimpleJoinWithPostFilterNoProperties(false);
        doTestSimpleJoinWithPostFilterNoProperties(true);
    }

    void doTestSimpleJoinWithPostFilterNoProperties(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();

        Filter j = ff.equal(ff.property(aname("stringProperty")), ff.property(aname("name")), true);
        Query q = new Query(tname("ft1"));
        Join join = new Join(tname("ftjoin"), j);
        join.setProperties(Query.NO_PROPERTIES);
        q.getJoins().add(join);
        q.setFilter(
                ff.equal(
                        ff.function(
                                "__equals",
                                ff.property(aname("stringProperty")),
                                ff.literal("one")),
                        ff.literal(true),
                        true));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        // test with post filter on table being joined
        q = new Query(tname("ft1"));
        join = new Join(tname("ftjoin"), j);
        join.setProperties(Query.NO_PROPERTIES);
        join.filter(
                ff.equal(
                        ff.function("__equals", ff.property(aname("name")), ff.literal("one")),
                        ff.literal(true),
                        true));
        q.getJoins().add(join);

        features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());
    }

    public void testSimpleJoinWithSort() throws Exception {
        doTestSimpleJoinWithSort(false);
        doTestSimpleJoinWithSort(true);
    }

    void doTestSimpleJoinWithSort(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();

        Filter j = ff.equal(ff.property(aname("stringProperty")), ff.property(aname("name")), true);
        Query q = new Query(tname("ft1"));
        q.getJoins().add(new Join(tname("ftjoin"), j));
        q.setSortBy(new SortBy[] {ff.sort(aname("intProperty"), SortOrder.DESCENDING)});

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());
            assertEquals("two", it.next().getAttribute(aname("stringProperty")));
            assertTrue(it.hasNext());
            assertEquals("one", it.next().getAttribute(aname("stringProperty")));
            assertTrue(it.hasNext());
            assertEquals("zero", it.next().getAttribute(aname("stringProperty")));
        }
    }

    public void testSimpleJoinWithLimitOffset() throws Exception {
        doTestSimpleJoinWithLimitOffset(false);
        doTestSimpleJoinWithLimitOffset(true);
    }

    void doTestSimpleJoinWithLimitOffset(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();

        Filter j = ff.equal(ff.property(aname("stringProperty")), ff.property(aname("name")), true);
        Query q = new Query(tname("ft1"));
        q.getJoins().add(new Join(tname("ftjoin"), j));
        q.setFilter(ff.greater(ff.property(aname("intProperty")), ff.literal(0)));
        q.setStartIndex(1);
        q.setSortBy(new SortBy[] {ff.sort(aname("intProperty"), SortOrder.ASCENDING)});

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());

            SimpleFeature f = it.next();
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            SimpleFeature g = (SimpleFeature) f.getAttribute(aname("ftjoin"));
            assertEquals("two", g.getAttribute(aname("name")));
        }
    }

    public void testSelfJoin() throws Exception {
        doTestSelfJoin(false);
        doTestSelfJoin(true);
    }

    public void doTestSelfJoin(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        q.getJoins()
                .add(
                        new Join(
                                        tname("ft1"),
                                        ff.equal(
                                                ff.property(aname("intProperty")),
                                                ff.property(aname("foo.intProperty")),
                                                true))
                                .alias(aname("foo")));
        q.setFilter(ff.equal(ff.property(aname("stringProperty")), ff.literal("two"), true));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());

            SimpleFeature f = it.next();
            assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());
            assertEquals(2, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            SimpleFeature g = (SimpleFeature) f.getAttribute(aname("foo"));
            assertEquals(4 + (exposePrimaryKeys ? 1 : 0), g.getAttributeCount());
            assertEquals(2, ((Number) g.getAttribute(aname("intProperty"))).intValue());
            assertEquals("two", g.getAttribute(aname("stringProperty")));
        }
    }

    public void testSpatialJoin() throws Exception {
        // doTestSpatialJoin(false);
        doTestSpatialJoin(true);
    }

    void doTestSpatialJoin(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        Query q = new Query(tname("ft1"));
        q.setPropertyNames(Arrays.asList(aname("geometry"), aname("intProperty")));
        q.setSortBy(new SortBy[] {ff.sort(aname("intProperty"), SortOrder.ASCENDING)});
        q.getJoins()
                .add(
                        new Join(
                                tname("ftjoin"),
                                ff.contains(
                                        ff.property(aname("geom")),
                                        ff.property(aname("geometry")))));

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(q);
        assertEquals(6, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            SimpleFeature f;

            Set<String> s = new HashSet<String>(Arrays.asList("zero", "one", "two"));

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(0, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            s.remove(((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(0, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            s.remove(((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(0, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            s.remove(((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertEquals(0, s.size());

            s = new HashSet<String>(Arrays.asList("one", "two"));

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(1, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            s.remove(((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(1, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            s.remove(((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertEquals(0, s.size());

            assertTrue(it.hasNext());
            f = it.next();
            assertEquals(2, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertEquals(
                    "two",
                    ((SimpleFeature) f.getAttribute(tname("ftjoin"))).getAttribute(aname("name")));

            assertFalse(it.hasNext());
        }
    }

    public void testOuterJoin() throws Exception {
        doTestOuterJoin(false);
        doTestOuterJoin(true);
    }

    void doTestOuterJoin(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ftjoin"));
        q.getJoins()
                .add(
                        new Join(
                                        tname("ft1"),
                                        ff.equal(
                                                ff.property(aname("name")),
                                                ff.property(aname("stringProperty")),
                                                true))
                                .type(Type.OUTER));

        SimpleFeatureCollection features =
                dataStore.getFeatureSource(tname("ftjoin")).getFeatures(q);
        assertEquals(
                dataStore.getFeatureSource(tname("ftjoin")).getFeatures(q).size(), features.size());

        try (SimpleFeatureIterator it = features.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertEquals(4 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());

                SimpleFeature g = (SimpleFeature) f.getAttribute(tname("ft1"));
                if ("three".equals(f.getAttribute(aname("name")))) {
                    assertNull(g);
                } else {
                    assertNotNull(g);
                }
            }
        }
    }

    public void testJoinMoreThanTwo() throws Exception {
        doJoinMoreThanTwo(false);
        doJoinMoreThanTwo(true);
    }

    void doJoinMoreThanTwo(boolean exposePrimaryKeys) throws Exception {
        dataStore.setExposePrimaryKeyColumns(exposePrimaryKeys);
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query(tname("ftjoin"));
        q.getJoins()
                .add(
                        new Join(
                                tname("ft1"),
                                ff.equal(
                                        ff.property(aname("name")),
                                        ff.property(aname("stringProperty")),
                                        true)));

        q.getJoins()
                .add(
                        new Join(
                                tname("ftjoin2"),
                                ff.equal(
                                        ff.property(aname("join2intProperty")),
                                        ff.property(aname("join1intProperty")),
                                        true)));

        SimpleFeatureCollection features =
                dataStore.getFeatureSource(tname("ftjoin")).getFeatures(q);
        assertEquals(3, features.size());

        try (SimpleFeatureIterator it = features.features()) {
            String[] ft1StringProp = new String[] {"zero", "one", "two"};
            String[] ftjoin2StringProp = new String[] {"2nd zero", "2nd one", "2nd two"};

            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertEquals(5 + (exposePrimaryKeys ? 1 : 0), f.getAttributeCount());
                Number nmb = (Number) f.getAttribute(aname("join1intProperty"));
                Integer idx = nmb.intValue();
                assertTrue(idx < 3);
                SimpleFeature g = (SimpleFeature) f.getAttribute(tname("ft1"));
                assertNotNull(g);
                assertEquals(ft1StringProp[idx], g.getAttribute(aname("stringProperty")));
                g = (SimpleFeature) f.getAttribute(tname("ftjoin2"));
                assertNotNull(g);
                assertEquals(ftjoin2StringProp[idx], g.getAttribute(aname("stringProperty2")));
            }
        }
    }
}
