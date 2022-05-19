/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

public class PostgisDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISPSTestSetup();
    }

    /**
     * verifies that the column width of a varchar column is larger than 255 if length is not
     * specified. See <a href="https://osgeo-org.atlassian.net/browse/GEOT-6888">GEOT-6888</a>.
     */
    @Test
    public void testDefaultVarcharSize() throws Exception {
        dataStore.createSchema(DataUtilities.createType("varchartest", "stringProperty:String"));

        SimpleFeatureType simpleFeatureType = dataStore.getSchema(tname("varchartest"));
        assertNotNull(simpleFeatureType);
        try (Transaction t = new DefaultTransaction("metadata");
                Connection cx = dataStore.getConnection(t);
                ResultSet rs =
                        cx.getMetaData().getColumns(null, null, "varchartest", "stringProperty")) {

            rs.absolute(1);
            int columnSize = rs.getInt("COLUMN_SIZE");
            assertTrue(
                    "column size should be larger than 255 (default) but was " + columnSize,
                    columnSize > 255);
        }
    }
}
