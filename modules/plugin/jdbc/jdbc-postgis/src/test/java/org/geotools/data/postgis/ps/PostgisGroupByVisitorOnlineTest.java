/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.ps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.data.postgis.PostGISPSDialect;
import org.geotools.data.postgis.PostgisGroupByVisitorTestSetup;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.jdbc.JDBCGroupByVisitorOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;

public class PostgisGroupByVisitorOnlineTest extends JDBCGroupByVisitorOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostgisGroupByVisitorTestSetup(new PostGISPSTestSetup());
    }

    @Test
    public void testAggregateOnNonEncodableFunction() throws Exception {
        PostGISPSDialect sqlDialect = (PostGISPSDialect) dataStore.getSQLDialect();
        boolean oldValue = sqlDialect.isFunctionEncodingEnabled();
        sqlDialect.setFunctionEncodingEnabled(false);
        try {
            testAggregateOnFunction(false);
        } finally {
            sqlDialect.setFunctionEncodingEnabled(oldValue);
        }
    }

    @Test
    public void testAggregateOnEncodableFunction() throws Exception {
        PostGISPSDialect sqlDialect = (PostGISPSDialect) dataStore.getSQLDialect();
        boolean oldValue = sqlDialect.isFunctionEncodingEnabled();
        sqlDialect.setFunctionEncodingEnabled(true);
        try {
            testAggregateOnFunction(true);
        } finally {
            sqlDialect.setFunctionEncodingEnabled(oldValue);
        }
    }

    public void testAggregateOnFunction(boolean expectOptimized) throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function buildingTypeSub =
                ff.function("strSubstring", ff.property("building_type"), ff.literal(0), ff.literal(3));

        List<Object[]> value = genericGroupByTestTest(Query.ALL, Aggregate.MAX, expectOptimized, buildingTypeSub);
        assertNotNull(value);

        assertEquals(3, value.size());
        checkValueContains(value, "HOU", "6.0");
        checkValueContains(value, "FAB", "500.0");
        checkValueContains(value, "SCH", "60.0");
    }

    @Test
    public void testTimestampHistogramDate() throws Exception {
        testTimestampHistogram("last_update_date");
    }
}
