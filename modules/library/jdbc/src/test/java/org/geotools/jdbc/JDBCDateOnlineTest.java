package org.geotools.jdbc;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCDateOnlineTest extends JDBCTestSupport {

    protected boolean testNegativeDates = false;

    @Override
    protected abstract JDBCDateTestSetup createTestSetup();

    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema( tname("dates") );
        
        assertEquals( Date.class, ft.getDescriptor( aname("d") ).getType().getBinding() );
        assertEquals( Timestamp.class, ft.getDescriptor( aname("dt") ).getType().getBinding() );
        assertEquals( Time.class, ft.getDescriptor( aname("t") ).getType().getBinding() );
    }
    
    public void testFiltersByDate() throws Exception {
        
        boolean simple = false;
        //work around the fact that postgis (and others??) don't handle 
        //programs that change the timezone well.
        if(dataStore.dialect instanceof PreparedStatementSQLDialect) {
            simple = true; 
        }
        FilterFactory ff = dataStore.getFilterFactory();
    
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        TimeZone originalTimeZone = TimeZone.getDefault();
        TimeZone[] zones = { TimeZone.getTimeZone("Etc/GMT+12"),
                TimeZone.getTimeZone("PST"), TimeZone.getTimeZone("EST"),
                TimeZone.getTimeZone("GMT"), TimeZone.getTimeZone("CET"),
                TimeZone.getTimeZone("Etc/GMT-12"),
                TimeZone.getTimeZone("Etc/GMT-14") };
        if(simple) {
            zones = new TimeZone[1];
            zones[0] = originalTimeZone;
        }
        try {
            for (TimeZone zone : zones) {
    
                FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
                
                // set JVM time zone
                TimeZone.setDefault(zone);
                // regenerate the database table using the new JVM Timezone
    
                setup.setUpData();
                df.setTimeZone(zone);
                // less than
                Filter f = ff.lessOrEqual(ff.property(aname("d")),
                        ff.literal(df.parse("2009-28-06")));
                System.out.println(f);
                assertEquals(
                        "wrong number of records for " + zone.getDisplayName(), 2,
                        fs.getCount(new DefaultQuery(tname("dates"), f)));
    /*
                f = ff.lessOrEqual(ff.property(aname("d")),
                        ff.literal(df.parse("2009-28-06")));
                assertEquals(
                        "wrong number of records for " + zone.getDisplayName(), 2,
                        fs.getCount(new DefaultQuery(tname("dates"), f)));
    */
            }
        } finally {
            // set JVM time zone
            TimeZone.setDefault(originalTimeZone);
            // regenerate the database table using the new JVM Timezone
    
            setup.setUpData();
        }
    }

    public void testFilterByTimeStamp() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
    
        FilterFactory ff = dataStore.getFilterFactory();
    
        // equal to
        Filter f = ff.equals(ff.property(aname("dt")),
                ff.literal("2009-06-28 15:12:41"));
        assertEquals(1, fs.getCount(new DefaultQuery(tname("dates"), f)));
    
        f = ff.equals(ff.property(aname("dt")), ff.literal(new SimpleDateFormat(
                "HH:mm:ss,dd-yyyy-MM").parse("15:12:41,28-2009-06")));
        assertEquals(1, fs.getCount(new DefaultQuery(tname("dates"), f)));
    }
    
    public void testFilterByTime() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
    
        FilterFactory ff = dataStore.getFilterFactory();
    
        // greather than or equal to
        Filter f = ff.greaterOrEqual(ff.property(aname("t")),
                ff.literal("13:10:12"));
        assertEquals(3, fs.getCount(new DefaultQuery(tname("dates"), f)));
    
        f = ff.greaterOrEqual(ff.property(aname("t")),
                ff.literal(new SimpleDateFormat("ss:HH:mm").parse("12:13:10")));
        assertEquals(3, fs.getCount(new DefaultQuery(tname("dates"), f)));
    }

    public void testNegativeDates() throws Exception {
        if (!testNegativeDates) {
            return;
        }
        
//        Logger.getLogger("").setLevel(Level.ALL);
//        Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);
//        Logging.getLogger(JDBCDataStore.class).setLevel(Level.ALL);
        
        // make our negative year, reset hours/minutes/seconds
        // to allow test of date and datetime
        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(-500, 0, 1);
        java.util.Date d = cal.getTime();
        long ms = d.getTime();
        
        FeatureWriter writer = dataStore.getFeatureWriterAppend( tname("dates") , Transaction.AUTO_COMMIT);
        writer.hasNext();
        
        // to avoid a timezone offset bug in the converter framework at the time
        // of this writing, use the sql types to avoid conversion...
        SimpleFeature f = (SimpleFeature) writer.next();
        f.setAttribute("d", new java.sql.Date(ms));
        f.setAttribute("dt", new java.sql.Timestamp(ms));
        f.setAttribute("t", new java.sql.Time(ms));
        writer.write();
        
        FeatureSource featureSource = dataStore.getFeatureSource( tname("dates") );
        FeatureCollection features = featureSource.getFeatures(
                dataStore.getFilterFactory().id(Collections.singleton(f.getIdentifier())));
        Feature[] fa = new Feature[1];
        features.toArray(fa);
        
        // output value should be same as input for both date and datetime
        
        cal.setTime((java.util.Date) fa[0].getProperty("d").getValue());
        assertEquals(ms, cal.getTimeInMillis());
        
        cal.setTime((java.util.Date) fa[0].getProperty("dt").getValue());
        assertEquals(ms, cal.getTimeInMillis());
        writer.close();
    }
}
