/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** Base class for online tests of JDBC time zone handling. */
public abstract class JDBCTimeZoneDateOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCDateTestSetup createTestSetup();

    TimeZone originalTimeZone;

    public void setTimeZone(TimeZone zone) {
        if (originalTimeZone == null) {
            // this method is called several times for each instance by the test setup
            // infrastructure via subclass createTestSetup() and the time zone is only
            // original during the first call (null originalTimeZone)
            originalTimeZone = TimeZone.getDefault();
        }
        // set JVM time zone
        TimeZone.setDefault(zone);
    }

    public void testFiltersByDate() throws Exception {
        setup.setUpData();
        FilterFactory ff = dataStore.getFilterFactory();
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("dates"));
        Filter f = ff.lessOrEqual(ff.property(aname("d")), ff.literal(df.parse("2009-28-06")));
        assertEquals(
                "wrong number of records for " + TimeZone.getDefault().getDisplayName(),
                2,
                fs.getCount(new Query(tname("dates"), f)));
        TimeZone.setDefault(originalTimeZone);
        setup.setUpData();
    }
}
