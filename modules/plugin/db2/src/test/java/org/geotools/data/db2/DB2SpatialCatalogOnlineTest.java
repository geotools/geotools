/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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

import java.io.IOException;
import java.sql.Connection;


/**
 * Exercise DB2SpatialCatalog.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2SpatialCatalogOnlineTest extends AbstractDB2OnlineTestCase {
    private Connection conn;
    private DB2SpatialCatalog catalog;
    private String tabSchema = null;
    private String dbUrl = null;

    /**
     * Setup gets a database connection that will be live for the duration of
     * all tests.
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        super.connect();
        conn = getConnection();
        tabSchema = getDataStore().getTableSchema();
        dbUrl = getDataStore().getDbURL();
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

    /**
     * Test various combinations of getting an instance of a DB2SpatialCatalog
     *
     * @throws Exception
     */
    public void testGetInstance() throws Exception {
        DB2SpatialCatalog.reset(); // Force reset in case other tests may have set static variables

        // Test that an initial call with a null connection fails		
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, null);
        assertNull("Catalog not created with valid parameter supplied", catalog);

        // Test that an initial call with a valid connection is successful
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, conn);
        assertEquals("Catalog toString not expected",
            dbUrl + "-" + tabSchema, catalog.toString());

        //	Test that an initial call with a null connection is successful
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, conn);
        assertEquals("Catalog not found", dbUrl + "-" + tabSchema,
            catalog.toString());

        //		Test that an initial call with a valid connection but undefined
        //  schema name is successful.  Does this make sense?
        String notFoundSchema = "WillNotBeFound";
        catalog = DB2SpatialCatalog.getInstance(dbUrl, notFoundSchema, conn);
        assertEquals(dbUrl + "-" + notFoundSchema, catalog.toString());
    }

    public void testGetSRID() throws Exception {
        // Get a catalog - this shouldn't fail
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, conn);

        int srid = catalog.getSRID(tabSchema, "Places", "Geom");
        assertEquals(1, srid);

        try {
            catalog.getSRID(tabSchema, "Places", "NotFound");
            fail("getSRID should fail for undefined geometry");
        } catch (IOException e) {
        }

        try {
            catalog.getSRID(tabSchema, "NotFound", "Geom");
            fail("getSRID should fail for undefined table");
        } catch (IOException e) {
        }

        try {
            catalog.getSRID("NotFound", "Places", "Geom");
            fail("getSRID should fail for undefined table");
        } catch (IOException e) {
        }
    }

    public void testGetGeomType() throws Exception {
        // Get a catalog - this shouldn't fail
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, conn);

        String typeFound = catalog.getDB2GeometryTypeName(tabSchema, "Places",
                "Geom");
        assertEquals("ST_POLYGON", typeFound);

        @SuppressWarnings("unused")
		String typeNotFound = "yes";

        try {
            typeNotFound = catalog.getDB2GeometryTypeName(tabSchema, "Places",
                    "NotFound");
            fail("getDB2GeometryTypeName should fail for undefined geometry");
        } catch (IOException e) {
        }

        try {
            typeNotFound = catalog.getDB2GeometryTypeName(tabSchema,
                    "NotFound", "Geom");
            fail("getDB2GeometryTypeName should fail for undefined table");
        } catch (IOException e) {
        }

        try {
            typeNotFound = catalog.getDB2GeometryTypeName("NotFound", "Places",
                    "Geom");
            fail("getDB2GeometryTypeName should fail for undefined table");
        } catch (IOException e) {
        }
    }

    public void testGetTypes() throws Exception {
        // Get a catalog - this shouldn't fail
        catalog = DB2SpatialCatalog.getInstance(dbUrl, tabSchema, conn);

        int foundCount = 0;
        String[] typeNames = catalog.getTypeNames();

        for (int i = 0; i < typeNames.length; i++) {
            if (typeNames[i].equals("Places")) {
                foundCount++;
            }

            if (typeNames[i].equals("Roads")) {
                foundCount++;
            }
        }

        assertTrue(foundCount == 2);
    }
}
