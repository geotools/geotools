/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.dggs.clickhouse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/**
 * Fork of {@link JDBCDateOnlineTest} for ClickHouse, which has no support for Time columns, and might never have one
 * (support for it has been marked as won't fix: https://github.com/ClickHouse/ClickHouse/issues/979).
 */
public class ClickHouseDateOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new ClickHouseDateTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.SCHEMA.key, params.get(JDBCDataStoreFactory.DATABASE.key));
        return params;
    }

    // Etc/GMT+12 is UTC-12, the POSIX sign is inverted
    private static final String[] TIME_ZONES = {
        "Etc/GMT+12", "America/Los_Angeles", "America/New_York", "Etc/GMT", "Europe/Berlin", "Etc/GMT-12", "Etc/GMT-14"
    };

    @Test
    public void testReadData() throws Throwable {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        forEachTimeZone(() -> {
            List<String> days = new ArrayList<>();
            List<String> times = new ArrayList<>();
            for (SimpleFeature f : DataUtilities.list(fs.getFeatures())) {
                days.add(f.getAttribute(aname("d")).toString());
                times.add(f.getAttribute(aname("dt")).toString());
            }
            // java.sql.Date/Timestamp print in the JVM time zone, a day shift shows up here
            assertThat(days, containsInAnyOrder("2009-06-28", "2009-01-15", "2009-09-29"));
            assertThat(
                    times,
                    containsInAnyOrder("2009-06-28 15:12:41.0", "2009-01-15 13:10:12.0", "2009-09-29 17:54:23.0"));
        });
    }

    /** Runs the check in each of {@link #TIME_ZONES}, recreating the dates table first, then restores the JVM zone. */
    private void forEachTimeZone(ThrowingRunnable check) throws Throwable {
        TimeZone originalTimeZone = TimeZone.getDefault();
        try {
            for (String zone : TIME_ZONES) {
                TimeZone.setDefault(TimeZone.getTimeZone(zone));
                // the setup inserts local times, so the table must be rebuilt in the new zone
                ((ClickHouseDateTestSetup) setup).setUpData();
                check.run();
            }
        } finally {
            TimeZone.setDefault(originalTimeZone);
            ((ClickHouseDateTestSetup) setup).setUpData();
        }
    }

    @Test
    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("dates"));

        assertEquals(java.sql.Date.class, ft.getDescriptor(aname("d")).getType().getBinding());
        assertEquals(Timestamp.class, ft.getDescriptor(aname("dt")).getType().getBinding());
    }

    @Test
    public void testFiltersByDate() throws Throwable {
        FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        FilterFactory ff = dataStore.getFilterFactory();
        forEachTimeZone(() -> {
            // parse in the current zone, as a user would
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Filter f = ff.lessOrEqual(ff.property(aname("d")), ff.literal(df.parse("2009-06-28")));
            assertEquals(2, fs.getCount(new Query(tname("dates"), f)));
        });
    }

    @Test
    public void testFilterByTimeStamp() throws Throwable {
        FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        FilterFactory ff = dataStore.getFilterFactory();
        forEachTimeZone(() -> {
            Filter f = ff.equals(ff.property(aname("dt")), ff.literal("2009-06-28 15:12:41"));
            assertEquals(1, fs.getCount(new Query(tname("dates"), f)));
        });
    }

    @Test
    public void testMinDate() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        MinVisitor visitor = new MinVisitor(aname("d"));
        fs.getFeatures().accepts(visitor, null);
        Date date = (Date) visitor.getMin();
        assertNotNull(date);
    }

    @Test
    public void testMaxDate() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        MaxVisitor visitor = new MaxVisitor(aname("d"));
        fs.getFeatures().accepts(visitor, null);
        Date date = (Date) visitor.getMax();
        assertNotNull(date);
    }

    @Test
    public void testDistinctDate() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        UniqueVisitor visitor = new UniqueVisitor(aname("d"));
        fs.getFeatures().accepts(visitor, null);
        Set results = visitor.getUnique();
        for (Object result : results) {
            assertThat(result, CoreMatchers.instanceOf(Date.class));
        }
    }

    @Test
    public void testNearestDate() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        FilterFactory ff = dataStore.getFilterFactory();
        NearestVisitor visitor = new NearestVisitor(ff.property(aname("d")), new Date());
        fs.getFeatures().accepts(visitor, null);
        Date date = (Date) visitor.getNearestMatch();
        assertNotNull(date);
    }

    @Test
    public void testMinTimeStamp() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        MinVisitor visitor = new MinVisitor(aname("dt"));
        fs.getFeatures().accepts(visitor, null);
        Timestamp ts = (Timestamp) visitor.getMin();
        assertNotNull(ts);
    }

    @Test
    public void testMaxTimeStamp() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        MaxVisitor visitor = new MaxVisitor(aname("dt"));
        fs.getFeatures().accepts(visitor, null);
        Timestamp ts = (Timestamp) visitor.getMax();
        assertNotNull(ts);
    }

    @Test
    public void testDistinctTimeStamp() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        UniqueVisitor visitor = new UniqueVisitor(aname("dt"));
        fs.getFeatures().accepts(visitor, null);
        Set results = visitor.getUnique();
        for (Object result : results) {
            assertThat(result, CoreMatchers.instanceOf(Timestamp.class));
        }
    }

    @Test
    public void testNearestTimeStamp() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        FilterFactory ff = dataStore.getFilterFactory();
        NearestVisitor visitor =
                new NearestVisitor(ff.property(aname("dt")), new Timestamp(System.currentTimeMillis()));
        fs.getFeatures().accepts(visitor, null);
        Timestamp ts = (Timestamp) visitor.getNearestMatch();
        assertNotNull(ts);
    }
}
