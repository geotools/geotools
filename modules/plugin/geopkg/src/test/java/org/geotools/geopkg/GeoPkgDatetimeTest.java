/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.util.NullProgressListener;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class GeoPkgDatetimeTest {
    private static final NullProgressListener NULL_LISTENER = new NullProgressListener();
    private DataStore gpkg;

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        URL url = TestData.url(this.getClass(), "datetime_date.gpkg");
        // open the geopkg
        HashMap<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, GeoPkgDataStoreFactory.DBTYPE.sample);
        params.put(GeoPkgDataStoreFactory.DATABASE.key, URLs.urlToFile(url));

        gpkg = DataStoreFinder.getDataStore(params);
        assertNotNull(gpkg);
    }

    /**
     * Test for GEOS-9392 - recognition of DATETIME and DATE.
     *
     * @throws IOException
     */
    @Test
    public void testGetContents() throws IOException {
        String[] typeNamesArr = gpkg.getTypeNames();
        assertNotNull("no types found", typeNamesArr);
        List<String> typeNames = Arrays.asList(typeNamesArr);
        assertFalse("no types found", typeNames.isEmpty());
        String name = typeNamesArr[0];
        SimpleFeatureSource fs = gpkg.getFeatureSource(name);
        assertNotNull(fs);

        SimpleFeatureType schema = fs.getSchema();
        assertEquals(Timestamp.class, schema.getDescriptor("datetime").getType().getBinding());
        assertEquals(Date.class, schema.getDescriptor("date").getType().getBinding());

        SimpleFeatureCollection features = fs.getFeatures();
        try (SimpleFeatureIterator itr = features.features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                assertNotNull(f);

                assertThat(f.getAttribute("datetime"), CoreMatchers.instanceOf(Timestamp.class));
                assertThat(f.getAttribute("date"), CoreMatchers.instanceOf(Date.class));
            }
        }
    }

    /**
     * Test avoidance of aggregate UniqueVisitor optimization (as they do not respect dialect type
     * mapping for Date and Timestamp at this time.
     *
     * @throws IOException
     */
    @Test
    public void testUnique() throws IOException {
        UniqueVisitor highlander = new UniqueVisitor("date");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(highlander, NULL_LISTENER);

        Set uniqueSet = highlander.getUnique();
        assertEquals(uniqueSet.size(), features.size());
        for (Object value : uniqueSet) {
            assertThat(value, CoreMatchers.instanceOf(Date.class));
        }
    }

    /**
     * Tests attribute "time" (timestamp) as is used by LIST in WMS time dimension GetCapabilities
     * CF. testUnique()
     *
     * @throws IOException
     */
    @Test
    public void testUnique_timestamp() throws IOException {
        UniqueVisitor highlander = new UniqueVisitor("time");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(highlander, NULL_LISTENER);

        Set uniqueSet = highlander.getUnique();
        assertEquals(uniqueSet.size(), features.size());
        for (Object value : uniqueSet) {
            assertThat(value, CoreMatchers.instanceOf(java.sql.Timestamp.class));
        }
    }

    // messes with the timezone to do testing in different timezones
    // Noticed that people in different timezones had different results
    // So, this tests 3 different timezone a negative TZ, UTC, and a positive TZ
    @Test
    public void testMax_Timezone() throws IOException {
        TimeZone originalTZ = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("EST")); // UTC-5
            testMax();
            testMax_timestamp();
            TimeZone.setDefault(TimeZone.getTimeZone("GMT")); // UTC
            testMax();
            testMax_timestamp();
            TimeZone.setDefault(TimeZone.getTimeZone("CET")); // UTC+1
            testMax();
            testMax_timestamp();
        } finally {
            TimeZone.setDefault(originalTZ);
        }
    }

    @Test
    public void testMax() throws IOException {
        MaxVisitor max = new MaxVisitor("date");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(max, NULL_LISTENER);

        assertEquals(java.sql.Date.valueOf("2020-02-23"), max.getMax());
    }

    @Test
    public void testMax_timestamp() throws IOException {
        MaxVisitor max = new MaxVisitor("time");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(max, NULL_LISTENER);

        assertEquals(java.sql.Timestamp.valueOf("2020-03-19 01:00:00"), max.getMax());
    }

    @Test
    public void testMin() throws IOException {
        MinVisitor min = new MinVisitor("date");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(min, NULL_LISTENER);

        assertEquals(java.sql.Date.valueOf("2020-02-19"), min.getMin());
    }

    @Test
    public void testMin_timestamp() throws IOException {
        MinVisitor min = new MinVisitor("time");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(min, NULL_LISTENER);

        assertEquals(java.sql.Timestamp.valueOf("2020-02-19 22:00:00"), min.getMin());
    }

    @Test
    public void testGroupBy() throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        GroupByVisitor visitor =
                new GroupByVisitor(
                        Aggregate.MAX,
                        ff.property("date"),
                        Arrays.asList(ff.property("txt")),
                        NULL_LISTENER);

        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);
        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(visitor, NULL_LISTENER);
        Map results = visitor.getResult().toMap();
        assertEquals(java.sql.Date.valueOf("2020-02-19"), results.get(singletonList("1")));
        assertEquals(java.sql.Date.valueOf("2020-02-20"), results.get(singletonList("2")));
    }

    @Test
    public void testGroupBy_timestamp() throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        GroupByVisitor visitor =
                new GroupByVisitor(
                        Aggregate.MAX,
                        ff.property("time"),
                        Arrays.asList(ff.property("txt")),
                        NULL_LISTENER);

        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);
        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(visitor, NULL_LISTENER);
        Map results = visitor.getResult().toMap();

        assertEquals(5, results.size());

        assertEquals(
                java.sql.Timestamp.valueOf("2020-02-19 22:00:00.0"),
                results.get(singletonList("1")));
        assertEquals(
                java.sql.Timestamp.valueOf("2020-02-19 23:00:00.0"),
                results.get(singletonList("2")));
        assertEquals(
                java.sql.Timestamp.valueOf("2020-03-19 00:00:00.0"),
                results.get(singletonList("3")));
        assertEquals(
                java.sql.Timestamp.valueOf("2020-03-19 01:00:00.0"),
                results.get(singletonList("4")));
        assertEquals(
                java.sql.Timestamp.valueOf("2020-02-20 02:00:00.0"),
                results.get(singletonList("5")));
    }

    /**
     * Test avoidance of aggregate between filter (as this needed some help to respect date and
     * timestamp)
     *
     * @throws IOException
     */
    @Test
    public void testBetween() throws IOException, CQLException {
        Filter between = ECQL.toFilter("date BETWEEN '2020-02-20' AND '2020-02-22'");

        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);
        SimpleFeatureCollection features = fs.getFeatures(between);
        assertEquals(3, features.size());
    }

    @Test
    public void testBetween_timestamp() throws IOException, CQLException {
        Filter between =
                ECQL.toFilter("time BETWEEN '2020-02-19 23:00:00' AND '2020-03-19 00:00:00'");

        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);
        SimpleFeatureCollection features = fs.getFeatures(between);
        assertEquals(3, features.size());
    }
}
