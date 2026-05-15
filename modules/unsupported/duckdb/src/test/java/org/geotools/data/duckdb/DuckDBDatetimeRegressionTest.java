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

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
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
                "CREATE TABLE \"" + TABLE_NAME + "\" ("
                        + "id INTEGER PRIMARY KEY, "
                        + "geom GEOMETRY, "
                        + "d DATE, "
                        + "ts TIMESTAMP, "
                        + "grp VARCHAR"
                        + ")",
                "INSERT INTO \"" + TABLE_NAME + "\" VALUES "
                        + "(1, ST_GeomFromText('POINT (0 0)'), DATE '2020-02-19', TIMESTAMP '2020-02-19 22:00:00', 'a')",
                "INSERT INTO \"" + TABLE_NAME + "\" VALUES "
                        + "(2, ST_GeomFromText('POINT (1 1)'), DATE '2020-02-20', TIMESTAMP '2020-02-20 02:00:00', 'a')",
                "INSERT INTO \"" + TABLE_NAME + "\" VALUES "
                        + "(3, ST_GeomFromText('POINT (2 2)'), DATE '2020-03-19', TIMESTAMP '2020-03-19 01:00:00', 'b')");
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
