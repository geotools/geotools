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
import java.util.Map;

import org.geotools.data.DataAccessFactory.Param;


/**
 * Exercise DB2DataStoreFactory.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2DataStoreFactoryOnlineTest extends AbstractDB2OnlineTestCase {
    DB2DataStoreFactory factory = new DB2DataStoreFactory();

    public void connect() throws Exception {
        super.connect();
    }

    protected void disconnect() throws Exception {
        super.disconnect();
    }

    public void testIsAvailable() {
        assertTrue("isAvailable didn't return true", factory.isAvailable());
    }

    public void testCreateDataStore() {
        // Should succeed
        try {
            @SuppressWarnings("unused")
			DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(getParams());
            dataStore.dispose();
        } catch (IOException e) {
            fail("createDataStore failed:" + e);
        }
        

        // Should fail if dbtype is not "DB2"
        try {
            Map<String, String> params = getParams();  // always returns a new copy
            params.put("dbtype", "nodb");

            @SuppressWarnings("unused")
			DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(params);
            fail("createDataStore succeeded with invalid dbtype parameter");
        } catch (IOException e) {
            // We should come here as a result
        }
        // Should default schema to the user if schema is null
        try {
            Map<String, String> params = getParams();
            params.put("tabschema", null);

            DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(params);
            dataStore.dispose();
            String schema = dataStore.getTableSchema();
            assertEquals("DB2ADMIN", schema);
        } catch (IOException e) {
            fail("createDataStore failed:" + e);
        }       

    // Should default schema to the user if schema is blank
    try {
        Map<String, String> params = getParams();
        params.put("tabschema", "");

        DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(params);
        dataStore.dispose();
        String schema = dataStore.getTableSchema();
        assertEquals("DB2ADMIN", schema);
    } catch (IOException e) {
        fail("createDataStore failed:" + e);
    }  
    // Should convert schema to uppercase
    try {
        Map<String, String> params = getParams();
        params.put("tabschema", "sde");

        DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(params);
        dataStore.dispose();
        String schema = dataStore.getTableSchema();
        assertEquals("SDE", schema);
    } catch (IOException e) {
        fail("createDataStore failed:" + e);
    }
    // Should leave schema in mixed case
    try {
        Map<String, String> params = getParams();
        params.put("tabschema", "\"Test\"");

        DB2DataStore dataStore = (DB2DataStore) factory.createDataStore(params);
        dataStore.dispose();
        String schema = dataStore.getTableSchema();
        assertEquals("Test", schema);
    } catch (IOException e) {
        fail("createDataStore failed:" + e);
    }
}

    public void testCreateNewDataStore() {
        // Should fail
        try {
            @SuppressWarnings("unused")
			DB2DataStore dataStore = (DB2DataStore) factory.createNewDataStore(getParams());
            fail("createNewDataStore didn't fail");
        } catch (UnsupportedOperationException e) {
            // We should come here as a result
        }
    }

    public void testGetDescription() {
        assertEquals("DB2 Data Store", factory.getDescription());
    }

    public void testGetDisplayName() {
        assertEquals("DB2", factory.getDisplayName());
    }

    public void testGetParametersInfo() {
        Param[] params = factory.getParametersInfo();
        int i = 0;

        try {
            for (i = 0; i < params.length; i++) {
                params[0].lookUp(getParams());
            }
        } catch (IOException e) {
            // should never get here
            fail("lookUp failed on " + params[i]);
        }

        // test for missing parameter
        Map paramMap = getParams();
        paramMap.remove("dbtype");

        try {
            for (i = 0; i < params.length; i++) {
                params[0].lookUp(paramMap);
            }

            fail("Didn't fail on missing parameter dbtype");
        } catch (IOException e) {
            // should get here in successful case
        }
    }

    public void testCanProcess() {
        Map<String, String> params;

        // Make sure it succeeds for all good parameters
        params = getParams();
        assertTrue("all parameters valid - should have succeeded",
            factory.canProcess(params));

        // Should fail if "database" parameter is missing
        params = getParams();
        params.remove("database");
        assertFalse("database parameter is required", factory.canProcess(params));

        // Should fail if "dbtype" parameter is not "db2"
        params = getParams();
        params.put("dbtype", "nodb");
        assertFalse("dbtype parameter is required", factory.canProcess(params));

        // Should succeed if "dbname" parameter is not lowercase
        params = getParams();
        params.put("dbtype", "DB2");
        assertTrue("should succeed with uppercase DB2 as dbtype",
            factory.canProcess(params));

        // Should fail if "host" parameter is missing
        params = getParams();
        params.remove("host");
        assertFalse("host parameter is required", factory.canProcess(params));

        // Should fail if "port" parameter is missing
        params = getParams();
        params.remove("port");
        assertFalse("port parameter is required", factory.canProcess(params));

        // Should succeed if "user" parameter is missing - not sure if it should be mandatory
        params = getParams();
        params.remove("user");
        assertTrue("user parameter should be optional",
            factory.canProcess(params));

        // Should succeed if "passwd" parameter is missing - not sure if it should be mandatory
        params = getParams();
        params.remove("passwd");
        assertTrue("passwd parameter should be optional",
            factory.canProcess(params));

        // Should fail if "user" parameter is missing - not sure if it should be mandatory
        params = getParams();
        params.remove("tabschema");
        assertTrue("tabschema parameter should be optional",
            factory.canProcess(params));
    }
}
