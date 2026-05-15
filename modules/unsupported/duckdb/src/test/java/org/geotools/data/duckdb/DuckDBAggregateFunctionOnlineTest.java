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

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCAggregateTestSetup;
import org.junit.Ignore;
import org.junit.Test;

public class DuckDBAggregateFunctionOnlineTest extends JDBCAggregateFunctionOnlineTest {

    private static final String QUERY_LIMIT_WRAPPING_ORDERING_GAP =
            "JDBC aggregate SQL wraps query-limit requests in a DISTINCT subquery and loses deterministic order when"
                    + " visitor.hasLimits() is true; requires core JDBCDataStore fix";

    @Override
    protected JDBCAggregateTestSetup createTestSetup() {
        return new DuckDBAggregateTestSetup();
    }

    @Override
    @Test
    @Ignore(QUERY_LIMIT_WRAPPING_ORDERING_GAP)
    public void testStoreChecksVisitorLimits() throws Exception {
        // ignored on purpose. See @Ignore reason.
    }
}
