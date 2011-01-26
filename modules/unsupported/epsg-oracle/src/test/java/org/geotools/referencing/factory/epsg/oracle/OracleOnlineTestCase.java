/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.oracle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.geotools.factory.GeoTools;
import org.geotools.test.OnlineTestCase;

/**
 * This represents an online test case.
 * <p>
 * To run this test you
 * will need to supply a "fixture" in "%USERHOME%/.geotools/epsg/oracle.properties"
 * The contents of this file are as follows:
 * <pre><code>
 * user=****
 * password=****
 * url=jdbc:oracle:thin:@ant:1521:orcl
 * </code></pre>
 * <p>
 * This test requires ojdbc14.jar in order to run.
 * <p>
 * <b>Instructions for Maven:</b> set the environmental variable
 * "oracle.jdbc" to true (this will enable the oracle.jdbc-true profile and disable
 * the oracle.jdbc-false profile) ... basically sticking a real ojdbc14.jar in your
 * path so we have a real driver to work with.
 * <p>
 * @author Jody Garnett (Refractions Research)
 *
 * @source $URL$
 */
public class OracleOnlineTestCase extends OnlineTestCase {
    protected DataSource datasource;
    
    protected String getFixtureId() {
        return "epsg.oracle";
    }
    
    /**
     * Connect using OracleDataSource (by default).
     * <p>
     * Subclasses can override to wrap this DataSource, or make use of an alternate
     * Implementation as required.
     * 
     * @param params
     * @return
     * @throws SQLException
     */
    protected DataSource connect( String user, String password, String url, Properties params ) throws SQLException{
        OracleDataSource source = new OracleDataSource();

        source.setUser( user );
        source.setPassword( password );
        source.setURL( url );
        //source.setConnectionProperties( params ); //not available in dummy jar
        return source;
    }
    
    protected void connect() throws Exception {
        String user = fixture.getProperty("user");
        String password = fixture.getProperty("password");
        String url = fixture.getProperty("url");
        
        DataSource source = connect( user, password, url, fixture );
        if( !isEpsgDatabaseLoaded( source ) ){
            throw new SQLException("Could not find EPSG tables");
        }
        datasource = source;
        
        //  System.out.println(list);
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.memory.MemoryContextFactory");
        
        InitialContext context = new InitialContext(env);
        String name = "jdbc/EPSG";
        //String name = GeoTools.fixName(context, "jdbc/EPSG");        
        // System.out.println(name);
        context.bind(name, source);
        
        GeoTools.init(context);
    }

    /**
     * Confirm that the EPSG Database tables are present and accounted for.
     * <p>
     * This method is used as part of connect to ensure the EPSG tables
     * have been loaded correctly.
     * 
     * @param source
     * @return true if source is non null and tables are present
     * @throws Exception
     */
    public boolean isEpsgDatabaseLoaded( DataSource source ) throws Exception {
        if( source == null ) return false;
        Connection connection = source.getConnection();        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String user = fixture.getProperty("user").toUpperCase();
            ResultSet epsgTables = metaData
                    .getTables(null, user, "EPSG%", null);
            List list = new ArrayList();
            while (epsgTables.next()) {
                list.add(epsgTables.getObject(3));
            }
            if (list.isEmpty()) {
                throw new SQLException("Could not find EPSG tables");
            }
            return true;
        }
        finally {
            connection.close();
        }
    }
    
    protected void disconnect() throws Exception {
        datasource = null;
    }

}
