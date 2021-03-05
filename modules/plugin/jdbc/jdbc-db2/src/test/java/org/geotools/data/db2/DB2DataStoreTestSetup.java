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

package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class DB2DataStoreTestSetup extends JDBCDelegatingTestSetup {

    private String srsName = "SRS_26713";

    public DB2DataStoreTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected final void setUpData() throws Exception {
        // remove the srs
        try (Connection con = getConnection()) {
            try {

                removeSRS(con);
            } catch (SQLException e) {
            }
            // create the SRS
            createSRS(con);
        }
        super.setUpData();
    }

    protected void createSRS(Connection con) throws Exception {
        DB2TestUtil.executecCreateSRS(srsName, 26713, 0, 1, 0, 1, "NAD_1927_UTM_ZONE_13N", con);
    }

    protected void removeSRS(Connection con) throws Exception {
        DB2TestUtil.executeDropSRS(srsName, con);
    }
}
