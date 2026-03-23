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

import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

/** Online coverage for FilterFunction_area against a real DuckDB instance. */
public class DuckDBAreaFunctionOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DuckDBDataStoreAPITestSetup();
    }

    @Test
    public void testAreaFunctionFiltersPolygonFeatures() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter positiveArea = ff.greater(ff.function("area", ff.property(aname("geom"))), ff.literal(0d));
        Query query = new Query(tname("lake"), positiveArea);

        assertEquals(
                1, dataStore.getFeatureSource(tname("lake")).getFeatures(query).size());
    }
}
