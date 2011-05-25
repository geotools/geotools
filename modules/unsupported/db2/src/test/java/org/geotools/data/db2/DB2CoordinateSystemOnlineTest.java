/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Exercise DB2CoordinateSystem.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2CoordinateSystemOnlineTest extends AbstractDB2OnlineTestCase {
    private Connection conn;

    /**
     * Setup gets a database connection that will be live for the duration of
     * all tests.
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        super.connect();
        conn = getConnection();
    }

    /**
     * Closes the database connection before we terminate.
     *
     * @throws Exception
     */
    protected void disconnect() throws Exception {
        conn.close();
        super.disconnect();
    }

    public void testCreate() {
        // Should succeed for srid 1		
        try {
            DB2CoordinateSystem cs = new DB2CoordinateSystem(this.conn, 1);
            assertEquals("GCS_NORTH_AMERICAN_1983=EPSG:4269", cs.toString());
        } catch (SQLException e) {
            // Shouldn't get here
            fail("DB2CoordinateSystem constructor failed");
        }

        // Should fail for srid -1
        int invalidSrid = -1;

        try {
            @SuppressWarnings("unused")
			DB2CoordinateSystem cs = new DB2CoordinateSystem(conn, invalidSrid);
            fail("DB2CoordinateSystem constructor sucessful for invalid srid "
                + invalidSrid);
        } catch (SQLException e) {
            // Should get here in successful case
        }
    }
}
