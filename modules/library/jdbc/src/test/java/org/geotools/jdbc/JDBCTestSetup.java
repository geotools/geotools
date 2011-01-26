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
package org.geotools.jdbc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.test.OnlineTestCase;


/**
 * Sets up the test harness for a database.
 * <p>
 * The responsibilities of the test harness are the following:
 * <ol>
 *   <li>Create and configure the {@link DataSource} used to connect to the
 *   underlying database
 *   <li>Provide the dialect used to communicate with the underlying database
 *   <li>Populate the underlying database with the data used by the tests.
 * </ol>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public abstract class JDBCTestSetup {
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.jdbc");
    protected Properties fixture = null;
    private DataSource dataSource = null;

    public void setFixture(Properties fixture) {
        this.fixture = fixture;
    }
    
    /**
     * @see {@link OnlineTestCase#createOfflineFixture}
     */
    protected Properties createOfflineFixture() {
        return null;
    }
    
    /**
     * @see {@link OnlineTestCase#createExampleFixture}
     */
    protected Properties createExampleFixture() {
        return null;
    }
    
    public DataSource getDataSource() throws IOException {
        if(dataSource == null)
            dataSource = createDataSource();
        return dataSource;
    }

    public void setUp() throws Exception {
        //
    }

    protected void initializeDatabase() throws Exception {
    }

    protected void setUpData() throws Exception {
    }

    protected void setUpDataStore(JDBCDataStore dataStore) {
    }

    public void tearDown() throws Exception {
        if(dataSource instanceof BasicDataSource) {
            ((BasicDataSource) dataSource).close();
        } else if(dataSource instanceof ManageableDataSource) {
            ((ManageableDataSource) dataSource).close();
        }
    }
    
    /**
     * Runs an sql string aginst the database.
     *
     * @param input The sql.
     */
    protected void run(String input) throws Exception {
        run(new ByteArrayInputStream(input.getBytes()));
    }
    
    /**
     * Executes {@link #run(String)} ignoring any exceptions.
    */
    protected void runSafe( String input ) { 
        try {
            run( input );
        }
        catch( Exception ignore ) {
            // ignore.printStackTrace(System.out);
        }
    }

    /**
     * Runs an sql script against the database.
     *
     * @param script Input stream to the sql script to run.
     */
    protected void run(InputStream script) throws Exception {
        //load the script
        BufferedReader reader = new BufferedReader(new InputStreamReader(script));

        //connect
        Connection conn = getConnection();
        
        try {
            Statement st = conn.createStatement();

            try {
                String line = null;

                while ((line = reader.readLine()) != null) {
                    LOGGER.fine(line);
                    st.execute(line);
                }

                reader.close();
            } finally {
                st.close();
            }
        } finally {
            conn.close();
        }
    }
    
    /**
     * Returns a properly initialized connection. It's up to the calling code to close it
     */
    protected Connection getConnection() throws SQLException, IOException {
        Connection conn = getDataSource().getConnection();
        createDataStoreFactory().createSQLDialect(new JDBCDataStore()).initializeConnection(conn);
        return conn;
    }


    /**
     * This method is used whenever referencing the name
     * of a feature type / table name.
     * <p>
     * Subclasses should override this is in case where databases
     * can not respect case properly and need to force either 
     * upper or lower case. 
     * </p>
     *
     */
    protected String typeName( String raw ) {
        return raw;
    }
    
    /**
     * This method is used whenever referencing the name
     * of an attribute / column name.
     * <p>
     * Subclasses should override this is in case where databases
     * can not respect case properly and need to force either 
     * upper or lower case. 
     * </p>
     */
    protected String attributeName( String raw ) {
        return raw;
    }
    
    /**
     * Creates a data source by reading properties from a file called 'db.properties', 
     * located paralell to the test setup instance.
     */
    protected DataSource createDataSource() throws IOException {
        Properties db = fixture;

        BasicDataSource dataSource = new BasicDataSource();
        
        dataSource.setDriverClassName(db.getProperty( "driver") );
        dataSource.setUrl( db.getProperty( "url") );
        
        if ( db.containsKey( "user") ) {
            dataSource.setUsername(db.getProperty("user"));
        }
        else if ( db.containsKey( "username") ) {
            dataSource.setUsername(db.getProperty("username"));
        }
        if ( db.containsKey( "password") ) {
            dataSource.setPassword(db.getProperty("password"));
        }
        
        dataSource.setPoolPreparedStatements(true);
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(4);
        // if we cannot get a connection within 5 seconds give up
        dataSource.setMaxWait(5000);
        
        initializeDataSource( dataSource, db );
        
        // return a closeable data source (DisposableDataSource interface)
        // so that the connection pool will be tore down on datastore dispose
        return new DBCPDataSource(dataSource);
    }

   
    protected void initializeDataSource( BasicDataSource ds, Properties db ) {
        
    }

    protected abstract JDBCDataStoreFactory createDataStoreFactory();
    
    protected final SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return null;
    }
    
    /**
     * Called after a test case has determined that a connection to the data source
     * can be obtained. 
     * <p>This method is used for subclasses to make additional checks 
     * that determine if the test case should be run or not. This method is only
     * called if a connection can successfully be made to the database.</p>
     */
    public boolean shouldRunTests(Connection cx) throws SQLException{
        return true;
    }
}
