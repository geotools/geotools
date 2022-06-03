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

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class H2LinkedTableTest {
    H2DataStoreFactory factory;
    Map<String, Object> params;
    private JDBCDataStore ds1;

    @Before
    public void setUp() throws Exception {
        factory = new H2DataStoreFactory();
        params = new HashMap<>();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2");
        ds1 = factory.createDataStore(params);
    }

    @Test
    public void testLinkedTable() throws Exception {
        org.h2.Driver.load();
        Connection con2 = DriverManager.getConnection("jdbc:h2:mem:one", "sa", "sa");
        Statement sa = con2.createStatement();
        sa.execute(
                "DROP TABLE IF EXISTS \"test\"; "
                        + "CREATE TABLE \"test\" as select * from SYSTEM_RANGE(1,2) as n;");

        Connection con1 = ds1.getDataSource().getConnection();
        String sql =
                "DROP TABLE IF EXISTS \"test_linked\"; CREATE LINKED TABLE \"test_linked\"(NULL, "
                        + "'jdbc:h2:mem:one', 'sa', 'sa', '\"test\"')";
        Statement sb = con1.createStatement();
        sb.execute(sql);
        FeatureSource fs = ds1.getFeatureSource("test_linked");
        SimpleFeatureType featureType = ds1.getFeatureSource("test_linked").getSchema();
        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        assertEquals(2, fs.getCount(query));
        assertNotNull(featureType);
        assertEquals(1, featureType.getAttributeCount());
        assertEquals("X", featureType.getDescriptor(0).getName().getLocalPart());
        con2.close();
        ds1.dispose();
    }
}
