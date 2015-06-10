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
 * @source $URL$
 */
public abstract class JDBCTimeZoneDateOnlineTest extends JDBCTestSupport {
    
    @Override
    protected abstract JDBCDateTestSetup createTestSetup();
    
    public void setTimeZone(TimeZone zone) {
        // set JVM time zone
        TimeZone.setDefault(zone);
    }
    
    public void testFiltersByDate() throws Exception {
    
        FilterFactory ff = dataStore.getFilterFactory();
    
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        FeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        Filter f = ff.lessOrEqual(ff.property(aname("d")),
                ff.literal(df.parse("2009-28-06")));
        System.out.println(f);
        assertEquals("wrong number of records for "
                + TimeZone.getDefault().getDisplayName(), 2,
                fs.getCount(new DefaultQuery(tname("dates"), f)));
    
    }
}
