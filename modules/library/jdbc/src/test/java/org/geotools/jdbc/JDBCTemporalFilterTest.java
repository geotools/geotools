/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.data.Join;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

public abstract class JDBCTemporalFilterTest extends JDBCTestSupport {

   /* dates(d:Date,dt:Datetime,t:Time) 
    * </p>
    * <p>
    * The table has the following data:
    *
    *  2009-06-28 | 2009-06-28 15:12:41 | 15:12:41
    *  2009-01-15 | 2009-01-15 13:10:12 | 13:10:12
    *  2009-09-29 | 2009-09-29 17:54:23 | 17:54:23
    */
    
    @Override
    protected abstract JDBCDateTestSetup createTestSetup();

    void assertDatesMatch(Filter filter, String...dates) throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        
        Query query = new Query(aname("dates"), filter);
        query.setSortBy(new SortBy[]{ff.sort(aname("dt"), SortOrder.ASCENDING)});
        assertDatesMatch(query, dates);
    }
    
    void assertDatesMatch(Query query, String...dates) throws Exception {
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("dates"));
        
        assertEquals(dates.length, source.getCount(query));

        SimpleFeatureCollection features = source.getFeatures(query);
        SimpleFeatureIterator it = features.features();
        int i = 0;
        try {
            while(it.hasNext()) {
                SimpleFeature f = it.next();
                Date expected = FORMAT.parse(dates[i++]);

                assertEquals(Converters.convert(expected, Timestamp.class), f.getAttribute(aname("dt")));
            }
        }
        finally {
            it.close();
        }
    }

    public void testAfter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        
        After after = ff.after(ff.property(aname("dt")), ff.literal("2009-02-01 00:00:00"));
        assertDatesMatch(after, "2009-06-28 15:12:41", "2009-09-29 17:54:23");
    }

    public void testAfterInterval() throws Exception {
        Period period = period("2009-02-01 00:00:00", "2009-07-01 00:00:00");
        
        FilterFactory ff = dataStore.getFilterFactory();
        
        After after = ff.after(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(after, "2009-09-29 17:54:23");
        
        after = ff.after(ff.literal(period), ff.property(aname("dt")));
        assertDatesMatch(after, "2009-01-15 13:10:12");
    }
    
    public void testBefore() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        
        Before before = ff.before(ff.property(aname("dt")), ff.literal("2009-02-01 00:00:00"));
        assertDatesMatch(before, "2009-01-15 13:10:12");
    }

    public void testBeforeInterval() throws Exception {
        Period period = period("2009-07-01 00:00:00", "2009-12-01 00:00:00");
        
        FilterFactory ff = dataStore.getFilterFactory();
        
        Before before = ff.before(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(before, "2009-01-15 13:10:12", "2009-06-28 15:12:41");

        period = period("2009-07-01 00:00:00", "2009-08-01 00:00:00");
        before = ff.before(ff.literal(period), ff.property(aname("dt")));
        assertDatesMatch(before, "2009-09-29 17:54:23");
    }

    public void testBegins() throws Exception {
        Period period = period("2009-01-15 13:10:12", "2009-06-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        Begins before = ff.begins(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(before, "2009-01-15 13:10:12");
    }

    public void testBegunBy() throws Exception {
        Period period = period("2009-01-15 13:10:12", "2009-06-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        BegunBy before = ff.begunBy(ff.literal(period), ff.property(aname("dt")));
        assertDatesMatch(before, "2009-01-15 13:10:12");
    }
    
    public void testEnds() throws Exception {
        Period period = period("2009-01-15 13:10:12", "2009-06-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        Ends before = ff.ends(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(before, "2009-06-28 15:12:41");
    }
    
    public void testEndedBy() throws Exception {
        Period period = period("2009-01-15 13:10:12", "2009-06-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        Ends before = ff.ends(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(before, "2009-06-28 15:12:41");
    }
    
    public void testDuring() throws Exception {
        Period period = period("2009-01-01 00:00:00", "2009-07-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        During during = ff.during(ff.property(aname("dt")), ff.literal(period));
        assertDatesMatch(during, "2009-01-15 13:10:12", "2009-06-28 15:12:41");
    }
    
    public void testTContains() throws Exception {
        Period period = period("2009-01-01 00:00:00", "2009-07-28 15:12:41");
        FilterFactory ff = dataStore.getFilterFactory();
        
        TContains during = ff.tcontains(ff.literal(period), ff.property(aname("dt")));
        assertDatesMatch(during, "2009-01-15 13:10:12", "2009-06-28 15:12:41");
    }
    
    public void testTEquals() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        
        TEquals equals = ff.tequals(ff.literal("2009-01-15 13:10:12"), ff.property(aname("dt")));
        assertDatesMatch(equals, "2009-01-15 13:10:12");
    }

    public void testTemporalJoin() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        After after = ff.after(ff.property(aname("dt")), ff.property("other." + aname("dt")));
        Query q = new Query(tname("dates"));
        q.getJoins().add(new Join(tname("dates"), after).alias("other"));
        q.setSortBy(new SortBy[]{ff.sort(aname("dt"), SortOrder.ASCENDING)});
        
        assertDatesMatch(q, "2009-06-28 15:12:41", "2009-09-29 17:54:23", "2009-09-29 17:54:23");
    }

    static DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected Date date(String date) throws ParseException {
        return FORMAT.parse(date);
    }

    protected Instant instant(String d) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(d)));
    }

    protected Period period(String d1, String d2) throws ParseException {
        return new DefaultPeriod(instant(d1), instant(d2));
    }
}
