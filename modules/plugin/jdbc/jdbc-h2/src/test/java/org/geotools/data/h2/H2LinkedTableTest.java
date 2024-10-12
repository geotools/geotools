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
package org.geotools.data.h2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.Before;
import org.junit.Test;

public class H2LinkedTableTest {
    H2DataStoreFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new H2DataStoreFactory();
    }

    @Test
    public void testLinkedTable() throws Exception {
        org.h2.Driver.load();
        try (Connection con = DriverManager.getConnection("jdbc:h2:geotools2", "sa", "sa")) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS \"test\"; "
                        + "CREATE TABLE \"test\" as select * from SYSTEM_RANGE(1,2) as n;");
            }
        }

        Map<String, String> params1 = new HashMap<>();
        params1.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params1.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params1.put(JDBCDataStoreFactory.DBTYPE.key, "h2");

        JDBCDataStore ds = null;
        try {
            ds = factory.createDataStore(params1);
            assertNotNull(ds);
            assertTrue(ds.getDataSource() instanceof ManageableDataSource);
            try (Connection con = ds.getDataSource().getConnection()) {
                try (Statement stmt = con.createStatement()) {
                    String sql = "DROP TABLE IF EXISTS \"test_linked\"; CREATE LINKED TABLE \"test_linked\"(NULL, "
                            + "'jdbc:h2:geotools2', 'sa', 'sa', '\"test\"')";
                    stmt.execute(sql);
                }
            }

            FeatureSource fs = ds.getFeatureSource("test_linked");
            SimpleFeatureType featureType = ds.getFeatureSource("test_linked").getSchema();
            Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
            assertEquals(2, fs.getCount(query));
            assertNotNull(featureType);
            assertEquals(1, featureType.getAttributeCount());
            assertEquals("X", featureType.getDescriptor(0).getName().getLocalPart());

        } finally {
            if (ds != null) {
                ds.dispose();
            }
        }
    }
}
