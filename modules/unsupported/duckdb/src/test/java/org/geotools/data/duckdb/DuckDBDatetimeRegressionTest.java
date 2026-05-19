/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Datetime regression coverage inspired by GeoPackage non-online tests, adapted to DuckDB behavior. */
public class DuckDBDatetimeRegressionTest {

    private static final NullProgressListener NULL_LISTENER = new NullProgressListener();
    private static final String TABLE_NAME = "datetime_cases";

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Test
    public void testSchemaUsesDateAndTimestampBindings() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            assertEquals(
                    Date.class,
                    store.getSchema(TABLE_NAME).getDescriptor("d").getType().getBinding());
            assertEquals(
                    Timestamp.class,
                    store.getSchema(TABLE_NAME).getDescriptor("ts").getType().getBinding());
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testTimestampVariantSchemaBindingsUseSqlTimestamp() throws Exception {
        JDBCDataStore store = createTimestampVariantStore();
        try {
            SimpleFeatureType schema = store.getSchema("timestamp_variants");
            assertEquals(
                    Timestamp.class, schema.getDescriptor("ts_plain").getType().getBinding());
            assertEquals(Timestamp.class, schema.getDescriptor("ts_s").getType().getBinding());
            assertEquals(
                    Timestamp.class, schema.getDescriptor("ts_ms").getType().getBinding());
            assertEquals(
                    Timestamp.class, schema.getDescriptor("ts_ns").getType().getBinding());
            assertEquals(
                    Timestamp.class, schema.getDescriptor("ts_tz").getType().getBinding());
            assertEquals(
                    Timestamp.class,
                    schema.getDescriptor("ts_tz_long").getType().getBinding());
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testTimeVariantSchemaBindingsUseSqlTime() throws Exception {
        JDBCDataStore store = createTemporalVariantStore();
        try {
            SimpleFeatureType schema = store.getSchema("temporal_variants");
            assertEquals(Date.class, schema.getDescriptor("d").getType().getBinding());
            assertEquals(Time.class, schema.getDescriptor("t_plain").getType().getBinding());
            assertEquals(Time.class, schema.getDescriptor("t_ns").getType().getBinding());
            assertEquals(Time.class, schema.getDescriptor("t_tz").getType().getBinding());
            assertEquals(Time.class, schema.getDescriptor("t_tz_long").getType().getBinding());
            assertEquals(
                    Timestamp.class, schema.getDescriptor("ts_tz").getType().getBinding());
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testTimeVariantRuntimeValuesUseSqlTypes() throws Exception {
        JDBCDataStore store = createTemporalVariantStore();
        try {
            SimpleFeatureSource source = store.getFeatureSource("temporal_variants");
            try (SimpleFeatureIterator features = source.getFeatures().features()) {
                assertTrue(features.hasNext());
                SimpleFeature feature = features.next();
                assertEquals(Time.class, feature.getAttribute("t_plain").getClass());
                assertEquals(Time.class, feature.getAttribute("t_ns").getClass());
                assertEquals(Time.class, feature.getAttribute("t_tz").getClass());
                assertEquals(Time.class, feature.getAttribute("t_tz_long").getClass());
                assertEquals(Timestamp.class, feature.getAttribute("ts_tz").getClass());
            }
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testDatetimeVisitorsReturnExpectedTypesAndValues() throws Exception {
        JDBCDataStore store = createSeededStore();
        try {
            SimpleFeatureSource source = store.getFeatureSource(TABLE_NAME);
            SimpleFeatureCollection features = source.getFeatures();

            UniqueVisitor unique = new UniqueVisitor("d");
            features.accepts(unique, NULL_LISTENER);
            Set<?> uniqueSet = unique.getUnique();
            assertEquals(3, uniqueSet.size());
            for (Object value : uniqueSet) {
                assertEquals(Date.class, toDate(value).getClass());
            }

            MinVisitor minTimestamp = new MinVisitor("ts");
            features.accepts(minTimestamp, NULL_LISTENER);
            assertEquals(Timestamp.valueOf("2020-02-19 22:00:00"), minTimestamp.getMin());

            MaxVisitor maxTimestamp = new MaxVisitor("ts");
            features.accepts(maxTimestamp, NULL_LISTENER);
            assertEquals(Timestamp.valueOf("2020-03-19 01:00:00"), maxTimestamp.getMax());

            FilterFactory ff = store.getFilterFactory();
            GroupByVisitor groupBy = new GroupByVisitor(
                    Aggregate.MAX, ff.property("d"), Arrays.asList(ff.property("grp")), NULL_LISTENER);
            features.accepts(groupBy, NULL_LISTENER);
            Map<?, ?> results = groupBy.getResult().toMap();

            assertEquals(Date.valueOf("2020-02-20"), toDate(results.get(Arrays.asList("a"))));
            assertEquals(Date.valueOf("2020-03-19"), toDate(results.get(Arrays.asList("b"))));
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testDateMaxRemainsStableAcrossTimezones() throws Exception {
        TimeZone original = TimeZone.getDefault();
        try {
            JDBCDataStore store = createSeededStore();
            try {
                TimeZone.setDefault(TimeZone.getTimeZone("GMT-12"));
                assertEquals(Date.valueOf("2020-03-19"), maxDate(store));

                TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                assertEquals(Date.valueOf("2020-03-19"), maxDate(store));

                TimeZone.setDefault(TimeZone.getTimeZone("GMT+14"));
                assertEquals(Date.valueOf("2020-03-19"), maxDate(store));
            } finally {
                store.dispose();
            }
        } finally {
            TimeZone.setDefault(original);
        }
    }

    private JDBCDataStore createSeededStore() throws Exception {
        JDBCDataStore store =
                DuckDBTestUtils.createStore(tmp.newFolder().toPath().resolve("datetime.duckdb"), false);
        DuckDBTestUtils.runSetupSql(
                store,
                """
                CREATE TABLE "%s" (
                    id INTEGER PRIMARY KEY,
                    geom GEOMETRY,
                    d DATE,
                    ts TIMESTAMP,
                    grp VARCHAR
                )
                """
                        .formatted(TABLE_NAME),
                """
                INSERT INTO "%s" VALUES
                    (1, ST_GeomFromText('POINT (0 0)'), DATE '2020-02-19', TIMESTAMP '2020-02-19 22:00:00', 'a')
                """
                        .formatted(TABLE_NAME),
                """
                INSERT INTO "%s" VALUES
                    (2, ST_GeomFromText('POINT (1 1)'), DATE '2020-02-20', TIMESTAMP '2020-02-20 02:00:00', 'a')
                """
                        .formatted(TABLE_NAME),
                """
                INSERT INTO "%s" VALUES
                    (3, ST_GeomFromText('POINT (2 2)'), DATE '2020-03-19', TIMESTAMP '2020-03-19 01:00:00', 'b')
                """
                        .formatted(TABLE_NAME));
        return store;
    }

    private JDBCDataStore createTimestampVariantStore() throws Exception {
        JDBCDataStore store =
                DuckDBTestUtils.createStore(tmp.newFolder().toPath().resolve("timestamp-variants.duckdb"), false);
        DuckDBTestUtils.runSetupSql(
                store,
                """
                CREATE TABLE "timestamp_variants" (
                    id INTEGER PRIMARY KEY,
                    ts_plain TIMESTAMP,
                    ts_s TIMESTAMP_S,
                    ts_ms TIMESTAMP_MS,
                    ts_ns TIMESTAMP_NS,
                    ts_tz TIMESTAMPTZ,
                    ts_tz_long TIMESTAMP WITH TIME ZONE
                )
                """
                        .formatted(),
                """
                INSERT INTO "timestamp_variants" VALUES
                    (1,
                    TIMESTAMP '2020-01-01 00:00:00',
                    CAST('2020-01-01 00:00:00' AS TIMESTAMP_S),
                    CAST('2020-01-01 00:00:00.123' AS TIMESTAMP_MS),
                    CAST('2020-01-01 00:00:00.123456789' AS TIMESTAMP_NS),
                    CAST('2020-01-01 00:00:00+00' AS TIMESTAMPTZ),
                    CAST('2020-01-01 00:00:00+00' AS TIMESTAMP WITH TIME ZONE))
                """
                        .formatted());
        return store;
    }

    private JDBCDataStore createTemporalVariantStore() throws Exception {
        JDBCDataStore store =
                DuckDBTestUtils.createStore(tmp.newFolder().toPath().resolve("temporal-variants.duckdb"), false);
        DuckDBTestUtils.runSetupSql(
                store,
                """
                CREATE TABLE "temporal_variants" (
                    id INTEGER PRIMARY KEY,
                    d DATE,
                    t_plain TIME,
                    t_ns TIME_NS,
                    t_tz TIMETZ,
                    t_tz_long TIME WITH TIME ZONE,
                    ts_tz TIMESTAMPTZ
                )
                """
                        .formatted(),
                """
                INSERT INTO "temporal_variants" VALUES
                    (1, DATE '2020-01-01', TIME '12:34:56',
                    CAST('12:34:56.123456789' AS TIME_NS),
                    CAST('12:34:56+02' AS TIMETZ),
                    CAST('12:34:56+02' AS TIME WITH TIME ZONE),
                    CAST('2020-01-01 12:34:56+02' AS TIMESTAMPTZ))
                """
                        .formatted());
        return store;
    }

    private Date maxDate(JDBCDataStore store) throws Exception {
        MaxVisitor maxDate = new MaxVisitor("d");
        store.getFeatureSource(TABLE_NAME).getFeatures().accepts(maxDate, NULL_LISTENER);
        return toDate(maxDate.getMax());
    }

    private Date toDate(Object value) {
        if (value instanceof Date date) {
            return date;
        }
        if (value instanceof LocalDate localDate) {
            return Date.valueOf(localDate);
        }
        throw new AssertionError("Expected java.sql.Date or LocalDate, found: " + value);
    }
}
