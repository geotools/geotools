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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.Period;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.Test;

/** Online end-to-end checks for DuckDB temporal variants. */
public class DuckDBTemporalVariantsOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTemporalVariantsTestSetup();
    }

    @Test
    public void testFeatureCollectionReadExposesSqlTemporalTypes() throws Exception {
        SimpleFeatureSource source = dataStore.getFeatureSource(tname(DuckDBTemporalVariantsTestSetup.TABLE));
        Query query = new Query(source.getName().getLocalPart());
        query.setSortBy(dataStore.getFilterFactory().sort(aname("ts_plain"), SortOrder.ASCENDING));

        SimpleFeatureCollection collection = source.getFeatures(query);
        assertEquals(3, collection.size());

        try (SimpleFeatureIterator it = collection.features()) {
            assertTrue(it.hasNext());
            SimpleFeature feature = it.next();
            assertEquals(
                    Timestamp.class, feature.getAttribute(aname("ts_plain")).getClass());
            assertEquals(Timestamp.class, feature.getAttribute(aname("ts_s")).getClass());
            assertEquals(Timestamp.class, feature.getAttribute(aname("ts_ms")).getClass());
            assertEquals(Timestamp.class, feature.getAttribute(aname("ts_ns")).getClass());
            assertEquals(Timestamp.class, feature.getAttribute(aname("ts_tz")).getClass());
            assertEquals(
                    Timestamp.class, feature.getAttribute(aname("ts_tz_long")).getClass());
            assertEquals(Time.class, feature.getAttribute(aname("t_plain")).getClass());
            assertEquals(Time.class, feature.getAttribute(aname("t_ns")).getClass());
            assertEquals(Time.class, feature.getAttribute(aname("t_tz")).getClass());
            assertEquals(Time.class, feature.getAttribute(aname("t_tz_long")).getClass());
        }
    }

    @Test
    public void testTemporalFiltersOnAllTimestampVariants() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        assertIds(ff.after(ff.property(aname("ts_plain")), ff.literal("2020-01-01 12:00:00")), 2, 3);
        assertIds(ff.after(ff.property(aname("ts_s")), ff.literal("2020-01-01 12:00:00")), 2, 3);
        assertIds(ff.after(ff.property(aname("ts_ms")), ff.literal("2020-01-01 12:00:00")), 2, 3);
        assertIds(ff.after(ff.property(aname("ts_ns")), ff.literal("2020-01-01 12:00:00")), 2, 3);
        assertIds(ff.after(ff.property(aname("ts_tz")), ff.literal("2020-01-01 12:00:00")), 2, 3);
        assertIds(ff.after(ff.property(aname("ts_tz_long")), ff.literal("2020-01-01 12:00:00")), 2, 3);
    }

    @Test
    public void testBeforeDuringAndEqualsTemporalFilters() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        Before before = ff.before(ff.property(aname("ts_ns")), ff.literal("2020-01-03 00:00:00"));
        assertIds(before, 1, 2);

        During during = ff.during(
                ff.property(aname("ts_tz_long")), ff.literal(period("2020-01-01 12:00:00", "2020-01-03 00:00:00")));
        assertIds(during, 2, 3);

        TEquals equalsSecondPrecision = ff.tequals(ff.property(aname("ts_s")), ff.literal("2020-01-02 00:00:00"));
        assertIds(equalsSecondPrecision, 2);

        TEquals equalsLongTimezone = ff.tequals(ff.property(aname("ts_tz_long")), ff.literal("2020-01-02 00:00:00"));
        assertIds(equalsLongTimezone, 2);
    }

    @Test
    public void testTimeFamilyComparisonFilters() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Expression noon = ff.literal(Time.valueOf("12:00:00"));

        assertIds(ff.greater(ff.property(aname("t_plain")), noon), 3);
        assertIds(ff.greater(ff.property(aname("t_ns")), noon), 2, 3);
        assertIds(ff.greater(ff.property(aname("t_tz")), noon), 3);
        assertIds(ff.greater(ff.property(aname("t_tz_long")), noon), 3);
    }

    private void assertIds(Filter filter, int... expectedIds) throws Exception {
        SimpleFeatureSource source = dataStore.getFeatureSource(tname(DuckDBTemporalVariantsTestSetup.TABLE));
        Query query = new Query(source.getName().getLocalPart(), filter);
        query.setSortBy(dataStore.getFilterFactory().sort(aname("ts_plain"), SortOrder.ASCENDING));
        SimpleFeatureCollection collection = source.getFeatures(query);
        assertEquals(expectedIds.length, collection.size());

        try (SimpleFeatureIterator it = collection.features()) {
            int idx = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                assertEquals(expectedIds[idx++], extractFeatureId(feature));
            }
        }
    }

    private int extractFeatureId(SimpleFeature feature) {
        String featureId = feature.getID();
        int separator = featureId.lastIndexOf('.');
        return Integer.parseInt(featureId.substring(separator + 1));
    }

    private Date date(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
    }

    private Instant instant(String value) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(value)));
    }

    private Period period(String start, String end) throws ParseException {
        return new DefaultPeriod(instant(start), instant(end));
    }
}
