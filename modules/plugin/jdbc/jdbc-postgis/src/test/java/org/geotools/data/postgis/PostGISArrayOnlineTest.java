/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.opengis.filter.MultiValuedFilter.MatchAction;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.Converters;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;

public class PostGISArrayOnlineTest extends JDBCTestSupport {

    private static final Logger LOGGER = Logger.getLogger(PostGISArrayOnlineTest.class.getName());
    Timestamp expectedDate;

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        expectedDate = Converters.convert("2009-06-28 15:12:41", Timestamp.class);
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISArrayTestSetup(new PostGISTestSetup());
    }

    @Test
    public void testWritable() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));
        assertTrue(fs instanceof FeatureStore);
    }

    @Test
    public void testRead() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        // check the non null array
        SimpleFeature first =
                getSingleArrayFeature(
                        ff.id(Collections.singleton(ff.featureId(tname("arraytest") + ".0"))));
        assertArrayEquals(new String[] {"A", "B"}, (String[]) first.getAttribute(aname("strings")));
        assertArrayEquals(new Integer[] {1, 2}, (Integer[]) first.getAttribute(aname("ints")));
        assertArrayEquals(new Float[] {3.4f, 5.6f}, (Float[]) first.getAttribute(aname("floats")));
        assertArrayEquals(
                new Timestamp[] {expectedDate},
                (Timestamp[]) first.getAttribute(aname("timestamps")));

        // check the one containing null values inside non null arrays
        SimpleFeature nullValues =
                getSingleArrayFeature(
                        ff.id(Collections.singleton(ff.featureId(tname("arraytest") + ".1"))));
        assertArrayEquals(
                new String[] {null, "C"}, (String[]) nullValues.getAttribute(aname("strings")));
        assertArrayEquals(
                new Integer[] {null, 3}, (Integer[]) nullValues.getAttribute(aname("ints")));
        assertArrayEquals(
                new Float[] {null, 7.8f}, (Float[]) nullValues.getAttribute(aname("floats")));
        assertArrayEquals(
                new Timestamp[] {null, expectedDate},
                (Timestamp[]) nullValues.getAttribute(aname("timestamps")));

        // check the one containing null arrays
        SimpleFeature nullArrays =
                getSingleArrayFeature(
                        ff.id(Collections.singleton(ff.featureId(tname("arraytest") + ".2"))));
        assertNull(nullArrays.getAttribute(aname("strings")));
        assertNull(nullArrays.getAttribute(aname("ints")));
        assertNull(nullArrays.getAttribute(aname("floats")));
    }

    @Test
    public void testWrite() throws Exception {
        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname("arraytest"));

        // build new feature
        SimpleFeatureType schema = fs.getSchema();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        String[] stringArray = {null, "test"};
        fb.set("strings", stringArray);
        Integer[] intArray = {null, 1234};
        fb.set("ints", intArray);
        Float[] floatArray = {null, 123.4f};
        fb.set("floats", floatArray);
        fb.set("timestamps", null);
        SimpleFeature feature = fb.buildFeature(null);

        List<FeatureId> ids = fs.addFeatures(DataUtilities.collection(feature));
        assertEquals(1, ids.size());

        // read back and check
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeature read = getSingleArrayFeature(ff.id(Collections.singleton(ids.get(0))));
        assertArrayEquals(stringArray, (String[]) read.getAttribute(aname("strings")));
        assertArrayEquals(intArray, (Integer[]) read.getAttribute(aname("ints")));
        assertArrayEquals(floatArray, (Float[]) read.getAttribute(aname("floats")));
        assertNull(read.getAttribute(aname("timestamps")));
    }

    private SimpleFeature getSingleArrayFeature(Id filter) throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));
        ContentFeatureCollection fc = fs.getFeatures(filter);
        return DataUtilities.first(fc);
    }

    @Test
    public void testEqualityFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // check string equality
        assertMatchedFeatureIds(
                fs,
                ff.equals(ff.property(aname("strings")), ff.literal(new String[] {"A", "B"})),
                0);
        // check string equality with null values
        assertMatchedFeatureIds(
                fs,
                ff.equals(ff.property(aname("strings")), ff.literal(new String[] {null, "C"})),
                1);

        // check int equality
        assertMatchedFeatureIds(
                fs, ff.equals(ff.property(aname("ints")), ff.literal(new Integer[] {1, 2})), 0);
        // check int equality with null values
        assertMatchedFeatureIds(
                fs, ff.equals(ff.property(aname("ints")), ff.literal(new Integer[] {null, 3})), 1);

        // check float equality
        assertMatchedFeatureIds(
                fs,
                ff.equals(ff.property(aname("floats")), ff.literal(new Float[] {3.4f, 5.6f})),
                0);
        // check float equality with null values
        assertMatchedFeatureIds(
                fs,
                ff.equals(ff.property(aname("floats")), ff.literal(new Float[] {null, 7.8f})),
                1);

        // check timestamp equality
        assertMatchedFeatureIds(
                fs,
                ff.equals(
                        ff.property(aname("timestamps")),
                        ff.literal(new Timestamp[] {expectedDate})),
                0);
        // check float equality with null values
        assertMatchedFeatureIds(
                fs,
                ff.equals(
                        ff.property(aname("timestamps")),
                        ff.literal(new Timestamp[] {null, expectedDate})),
                1);
    }

    @Test
    public void testAnyMatchEquals() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // string, straight
        assertMatchedFeatureIds(
                fs,
                ff.equal(ff.property(aname("strings")), ff.literal("A"), false, MatchAction.ANY),
                0);
        // string, flipped
        assertMatchedFeatureIds(
                fs,
                ff.equal(ff.literal("A"), ff.property(aname("strings")), false, MatchAction.ANY),
                0);
        // string, nulls
        assertMatchedFeatureIds(
                fs,
                ff.equal(ff.property(aname("strings")), ff.literal(null), false, MatchAction.ANY),
                1);

        // ints
        assertMatchedFeatureIds(
                fs, ff.equal(ff.property(aname("ints")), ff.literal(1), false, MatchAction.ANY), 0);

        // timestamps
        assertMatchedFeatureIds(
                fs,
                ff.equal(
                        ff.property(aname("timestamps")),
                        ff.literal(expectedDate),
                        false,
                        MatchAction.ANY),
                0,
                1);
    }

    @Test
    public void testGreaterMatchOne() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // A > A -> false, should match the first, and null > A -> false, so also the second
        assertMatchedFeatureIds(
                fs,
                ff.greater(ff.property(aname("strings")), ff.literal("A"), false, MatchAction.ONE),
                0,
                1);

        // A = A true only in the first
        assertMatchedFeatureIds(
                fs,
                ff.equal(ff.property(aname("strings")), ff.literal("A"), false, MatchAction.ONE),
                0);
    }

    @Test
    public void testGreaterThanAny() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // all non null string arrays besides the null ones contain
        assertMatchedFeatureIds(
                fs,
                ff.greater(ff.property(aname("strings")), ff.literal("A"), false, MatchAction.ANY),
                0,
                1);
        // all non null int arrays besides the null ones contain
        assertMatchedFeatureIds(
                fs,
                ff.greater(ff.property(aname("ints")), ff.literal(1), false, MatchAction.ANY),
                0,
                1);

        // none has numbers so high
        assertMatchedFeatureIds(
                fs, ff.greater(ff.property(aname("ints")), ff.literal(20), false, MatchAction.ANY));

        // only one matching here
        assertMatchedFeatureIds(
                fs,
                ff.greater(ff.property(aname("floats")), ff.literal(6f), false, MatchAction.ANY),
                1);

        // timestamps, none matching
        assertMatchedFeatureIds(
                fs,
                ff.greater(
                        ff.property(aname("timestamps")),
                        ff.literal(expectedDate),
                        false,
                        MatchAction.ANY));

        // timestamps, all matching this time
        assertMatchedFeatureIds(
                fs,
                ff.greater(
                        ff.property(aname("timestamps")),
                        ff.literal(new Date(expectedDate.getTime() - 3600)),
                        false,
                        MatchAction.ANY),
                0,
                1);
    }

    @Test
    public void testAnyMatchBetween() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // strings
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("B"),
                        ff.literal("C"),
                        MatchAction.ANY),
                0,
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("A"),
                        ff.literal("B"),
                        MatchAction.ANY),
                0);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("C"),
                        ff.literal("F"),
                        MatchAction.ANY),
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("D"),
                        ff.literal("F"),
                        MatchAction.ANY));

        // ints
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(1), ff.literal(5), MatchAction.ANY),
                0,
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(1), ff.literal(2), MatchAction.ANY),
                0);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(3), ff.literal(5), MatchAction.ANY),
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")),
                        ff.literal(8),
                        ff.literal(100),
                        MatchAction.ANY));

        // timestamps
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("timestamps")),
                        ff.literal(expectedDate),
                        ff.literal(expectedDate),
                        MatchAction.ANY),
                0,
                1);
        LocalDateTime futureDateTime = expectedDate.toLocalDateTime().plusDays(1);
        Date laterDate = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("timestamps")),
                        ff.literal(laterDate),
                        ff.literal(laterDate),
                        MatchAction.ANY));
    }

    @Test
    public void testMatchOneBetween() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // strings
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("B"),
                        ff.literal("C"),
                        MatchAction.ONE),
                0,
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("A"),
                        ff.literal("B"),
                        MatchAction.ONE));
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("C"),
                        ff.literal("F"),
                        MatchAction.ONE),
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("D"),
                        ff.literal("F"),
                        MatchAction.ONE));

        // ints
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(1), ff.literal(5), MatchAction.ONE),
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(1), ff.literal(2), MatchAction.ONE));
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(3), ff.literal(5), MatchAction.ONE),
                1);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")),
                        ff.literal(8),
                        ff.literal(100),
                        MatchAction.ONE));

        // timestamps
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("timestamps")),
                        ff.literal(expectedDate),
                        ff.literal(expectedDate),
                        MatchAction.ONE),
                0,
                1);
        LocalDateTime futureDateTime = expectedDate.toLocalDateTime().plusDays(1);
        Date laterDate = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("timestamps")),
                        ff.literal(laterDate),
                        ff.literal(laterDate),
                        MatchAction.ONE));
    }

    @Test
    public void testMatchAllBetween() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("arraytest"));

        // strings
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("A"),
                        ff.literal("C"),
                        MatchAction.ALL),
                0);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("strings")),
                        ff.literal("D"),
                        ff.literal("F"),
                        MatchAction.ONE));

        // ints
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(1), ff.literal(5), MatchAction.ALL),
                0);
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("ints")), ff.literal(2), ff.literal(3), MatchAction.ALL));

        // timestamps
        assertMatchedFeatureIds(
                fs,
                ff.between(
                        ff.property(aname("timestamps")),
                        ff.literal(expectedDate),
                        ff.literal(expectedDate),
                        MatchAction.ALL),
                0);
    }

    private void assertMatchedFeatureIds(ContentFeatureSource fs, Filter filter, Integer... ids)
            throws IOException {
        ContentFeatureCollection fc = fs.getFeatures(filter);
        if (ids.length > 0) {
            Set<Integer> expected = new HashSet<>(Arrays.asList(ids));
            fc.accepts(
                    feature -> {
                        String id = feature.getIdentifier().getID();
                        assertThat(id, CoreMatchers.startsWith(tname("arraytest") + "."));
                        int key = Integer.parseInt(id.substring("arraytest".length() + 1));
                        assertTrue("Found unexpected id " + key, expected.remove(key));
                    },
                    null);
            assertTrue("Some of the expected ids were not found " + expected, expected.isEmpty());
        } else {
            assertEquals(
                    "Expected to find an empty result, but instead it was "
                            + new ListFeatureCollection(fc),
                    0,
                    fc.size());
        }
    }
}
