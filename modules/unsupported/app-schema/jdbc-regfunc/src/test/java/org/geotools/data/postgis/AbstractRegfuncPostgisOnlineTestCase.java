/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.postgis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.geotools.test.OnlineTestCase;

/**
 * Online test fixture for registered function support.
 * 
 * <p>
 * 
 * The id of this test fixture and its subclasses is "postgis-regfunc". This test fixture requires
 * PostGIS connection parameter entries like those listed below. The target should be a PostGIS
 * database to which the user has write permission.
 * 
 * <pre>
 *  host = HOSTNAME.example.com
 *  port = 5432
 *  schema = public
 *  database = DATABASE
 *  user = USER
 *  passwd = PASSWD
 *  skip.on.failure = false
 * </pre>
 * 
 * See {@link OnlineTestCase} for more about online test fixtures.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public abstract class AbstractRegfuncPostgisOnlineTestCase extends OnlineTestCase {

    /**
     * The name of the test table in the database.
     */
    public static final String TEST_TABLE_NAME = "regfunc_test_table";

    /**
     * The column in the test table used as its primary key.
     */
    public static final String ID_COLUMN_NAME = "id";

    /**
     * The columns in the table that contains text describing the feature.
     */
    public static final String DESCRIPTION_COLUMN_NAME = "description";

    /**
     * The description column for row with id 3.
     */
    public static final String DESCRIPTION_FOR_ID_3 = "Basalt, tuff, and some lumpy bits.";

    /**
     * The description column for row with id 27.
     */
    public static final String DESCRIPTION_FOR_ID_27 = "Sandstone, on top of basalt.";

    /**
     * The description column for row with id 41.
     */
    public static final String DESCRIPTION_FOR_ID_41 = "Tuff, limestone, and rich creamy butter.";

    /**
     * The description column for row with id 93.
     */
    public static final String DESCRIPTION_FOR_ID_93 = "Two pointy ones, a flat one, and a packet of gravel.";

    /**
     * Name of the test function created in the database.
     */
    public static final String TEST_FUNCTION_NAME = "regfunc_test_function_contains_text";

    /**
     * SQL to create the test function created in the database.
     * 
     * <p>
     * 
     * This function determines whether the column named in the first argument contains the text
     * given in the second argument, and returns 1 for true and 0 for false. The text comparison is
     * case insensitive.
     */
    /*
     * Do not use $$ for function body quoting as not supported by JDBC driver. Use
     * single-apostrophe instead. See comment at end of
     * http://www.postgresql.org/docs/8.2/interactive/plpgsql-development-tips.html
     */
    public static final String CREATE_FUNCTION_SQL = //
    "-- Return 1 if s contains ss (case insensitive), else return 0\n" //
            + "CREATE FUNCTION " //
            + TEST_FUNCTION_NAME //
            + "(s text, ss text) RETURNS integer AS '\n" //
            + "BEGIN\n" //
            + "    IF position(lower(ss) in lower(s) ) > 0\n" //
            + "    THEN\n" //
            + "        RETURN 1;\n" //
            + "    ELSE\n" //
            + "        RETURN 0;\n" //
            + "    END IF;\n" //
            + "END;\n" + "' LANGUAGE plpgsql"; //

    /**
     * The test function returns 1 for true and 0 for false.
     * 
     * <p>
     * 
     * GeoSciML Testbed 3 implementers report Oracle does not support boolean datatype, so use cases
     * implemented with filter functions that return these integers in place of boolean.
     * 
     * @see https://www.seegrid.csiro.au/twiki/bin/view/CGIModel/TestBed3UseCase3AProfile
     */
    public static final int TEST_FUNCTION_RETURN_TRUE = 1;

    /**
     * The data store under test.
     */
    private RegfuncPostgisDataStore datastore;

    /**
     * Define the id for this fixture.
     * 
     * @see org.geotools.test.OnlineTestCase#getFixtureId()
     */
    protected String getFixtureId() {
        return (String) RegfuncPostgisDataStoreFactory.DBTYPE.sample;
    }

    /**
     * Create a PostGIS data store with registered function support, test table, and test function.
     * 
     * @see org.geotools.test.OnlineTestCase#connect()
     */
    protected void connect() throws Exception {
        super.connect();
        datastore = (RegfuncPostgisDataStore) new RegfuncPostgisDataStoreFactory()
                .createDataStore(getParams());
        createTable();
        createFunction();
    }

    /**
     * Read the data store configuration parameters from the fixture properties.
     * 
     * @return data store configuration parameters
     */
    public Map getParams() {
        Map params = new HashMap();
        params
                .put(PostgisDataStoreFactory.DBTYPE.key,
                        RegfuncPostgisDataStoreFactory.DBTYPE.sample);
        loadParam(params, PostgisDataStoreFactory.HOST.key);
        loadParam(params, PostgisDataStoreFactory.PORT.key);
        loadParam(params, PostgisDataStoreFactory.SCHEMA.key);
        loadParam(params, PostgisDataStoreFactory.DATABASE.key);
        loadParam(params, PostgisDataStoreFactory.USER.key);
        loadParam(params, PostgisDataStoreFactory.PASSWD.key);
        return params;
    }

    /**
     * If fixture properties contains a given key, update the configuration parameters map with the
     * corresponding value taken from the fixture properties, with the same key.
     * 
     * @param params
     *                the parameter map used to configure a DataStore
     * @param key
     *                the key for the value in both the properties and parameter map
     */
    private void loadParam(Map params, String key) {
        if (fixture.containsKey(key)) {
            params.put(key, fixture.getProperty(key));
        }
    }

    /**
     * Remove test table and function, and disconnect.
     * 
     * @see org.geotools.test.OnlineTestCase#disconnect()
     */
    protected void disconnect() throws Exception {
        dropTable();
        dropFunction();
        datastore.dispose();
        datastore = null;
        super.disconnect();
    }

    /**
     * Get a JDBC Connection to the database.
     * 
     * <p>
     * 
     * Caller must <code>close<code> this resource.
     * 
     * @return a JDBC connection
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        return datastore.getDataSource().getConnection();
    }

    /**
     * Create the test table in the database.
     * 
     * @throws Exception
     */
    public void createTable() throws Exception {
        // first make sure no old test function present
        dropTable();
        // create the table
        execute("CREATE TABLE " + TEST_TABLE_NAME + " (" + ID_COLUMN_NAME
                + " INTEGER PRIMARY KEY, " + DESCRIPTION_COLUMN_NAME + " VARCHAR(80))");
        // insert the rows
        populateTable();
    }

    /**
     * Execute an SQL statement on the database.
     * 
     * @param sql
     *                the test of the SQL statement (no trailing semicolon)
     * @throws Exception
     */
    public void execute(String sql) throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(sql);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert the four test rows into the test table, in arbitrary order.
     * 
     * @throws Exception
     */
    private void populateTable() throws Exception {
        insertRow(41, DESCRIPTION_FOR_ID_41);
        insertRow(3, DESCRIPTION_FOR_ID_3);
        insertRow(27, DESCRIPTION_FOR_ID_27);
        insertRow(93, DESCRIPTION_FOR_ID_93);
    }

    /**
     * Insert a row into the test table.
     * 
     * @param id
     *                the key for this row
     * @param description
     *                descriptive text
     * @throws Exception
     */
    private void insertRow(int id, String description) throws Exception {
        execute("INSERT INTO " + TEST_TABLE_NAME + " (" + ID_COLUMN_NAME + ", "
                + DESCRIPTION_COLUMN_NAME + ") VALUES (" + id + ", '" + description + "')");
    }

    /**
     * Create the test function in the database by executing SQL.
     * 
     * @throws Exception
     */
    public void createFunction() throws Exception {
        // first make sure no old test function present
        dropFunction();
        execute(CREATE_FUNCTION_SQL);
    }

    /**
     * Remove the test table from the database. Exceptions are ignored.
     */
    public void dropTable() {
        try {
            execute("DROP TABLE " + TEST_TABLE_NAME);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Remove the test function from the database. Exceptions are ignored.
     */
    public void dropFunction() {
        try {
            execute("DROP FUNCTION " + TEST_FUNCTION_NAME + "(text, text)");
        } catch (Exception e) {
            // ignore
        }
    }

}
