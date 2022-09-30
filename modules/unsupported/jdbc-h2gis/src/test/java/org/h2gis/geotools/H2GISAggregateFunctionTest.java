package org.h2gis.geotools;

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCAggregateTestSetup;

/** Test class implementing {@link org.geotools.jdbc.JDBCAggregateFunctionOnlineTest} */
public class H2GISAggregateFunctionTest extends JDBCAggregateFunctionOnlineTest {
    @Override
    protected JDBCAggregateTestSetup createTestSetup() {
        return new H2GISAggregateTestSetup();
    }
}
