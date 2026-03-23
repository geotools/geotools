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

import java.io.IOException;
import java.util.Map;
import org.geotools.jdbc.JDBCFeatureCollectionOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;

public class DuckDBFeatureCollectionOnlineTest extends JDBCFeatureCollectionOnlineTest {
    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(AbstractDuckDBDataStoreFactory.READ_ONLY.key, Boolean.FALSE);
        return params;
    }

    @Override
    @Test
    public void testAdd() throws IOException {
        super.testAdd();
    }
}
