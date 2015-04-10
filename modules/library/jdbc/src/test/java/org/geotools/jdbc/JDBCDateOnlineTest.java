package org.geotools.jdbc;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCDateOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCDateTestSetup createTestSetup();

    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema( tname("dates") );
        
        assertEquals( Date.class, ft.getDescriptor( aname("d") ).getType().getBinding() );
        assertEquals( Timestamp.class, ft.getDescriptor( aname("dt") ).getType().getBinding() );
        assertEquals( Time.class, ft.getDescriptor( aname("t") ).getType().getBinding() );
    }
    
    public void testFiltersByDate() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource( tname("dates") );
        
        FilterFactory ff = dataStore.getFilterFactory();
    
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
    
        TimeZone[] zones = { TimeZone.getTimeZone("Etc/GMT+12"),
                TimeZone.getTimeZone("PST"), TimeZone.getTimeZone("EST"),
                TimeZone.getTimeZone("GMT"), TimeZone.getTimeZone("CET"),
                TimeZone.getTimeZone("Etc/GMT-12"),
                TimeZone.getTimeZone("Etc/GMT-14") };
        for (TimeZone zone : zones) {
            df.setTimeZone(zone);
            // set JVM time zone
            TimeZone.setDefault(zone);
            // less than
            Filter f = ff.lessOrEqual(ff.property(aname("d")),
                    ff.literal(df.parse("2009-28-06")));
            System.out.println(f);
            assertEquals("wrong number of records for "+zone.getDisplayName(), 2, fs.getCount(new DefaultQuery(tname("dates"), f)));
    
            f = ff.lessOrEqual(ff.property(aname("d")),
                    ff.literal(df.parse("2009-28-06")));
            assertEquals("wrong number of records for "+zone.getDisplayName(),2, fs.getCount(new DefaultQuery(tname("dates"), f)));
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
}
