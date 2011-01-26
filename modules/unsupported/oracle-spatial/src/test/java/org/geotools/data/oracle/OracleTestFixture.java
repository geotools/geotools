/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.geotools.data.jdbc.datasource.ManageableDataSource;

/**
 * Capture in one spot the location of *your* oracle instance.
 * <p>
 * This class is used by all test cases to provide a connection
 * to the database.
 * </p>
 * @author jgarnett
 * @source $URL$
 */
public class OracleTestFixture {	
	    public Connection connection;
	    
		private Properties properties;

		/** schema name incase you need access to types - like SDO */
		private String schemaName;

		/** Connection pool - incase you want to test a EPSG authority */
		private ManageableDataSource cPool;
	    
	    /**
	     * OracleTestFixture used in JUnit 
	     * <p>
	     * Connects to database and grabs the contents of the first row.
	     * </p>
	     * Example:
	     * <code><pre>
	     * protected void setUp() throws Exception {
	     *         super.setUp();
	     *         fixture = new OracleTestFixture();
	     *         geom = fixture.geom;
	     * }
	     * </pre></code>
	     */
	    public OracleTestFixture()  throws Exception {
	       // make connection to DB
	       // DriverManager.registerDriver( new oracle.jdbc.driver.OracleDriver() );
	       
	       properties = new Properties();
	       properties.load(this.getClass().getResourceAsStream("remote.properties"));
	       
	       schemaName = properties.getProperty("schema");
	       
	       try {
		       cPool = OracleDataStoreFactory.getDefaultDataSource(properties.getProperty("host"), 
	                    properties.getProperty("user"), properties.getProperty("passwd"),
	                    Integer.parseInt(properties.getProperty("port")), properties.getProperty("instance"),
	                    10, 0, false);	       
		       System.out.println( "Connect to"+ properties );
	    	   
	    	   connection = cPool.getConnection();
	       
	        	       
	    	   // connection = (OracleConnection) DriverManager.getConnection  ("jdbc:oracle:thin:@hydra:1521:rrdev","dblasby","dave2000");
	    	   // connection = (OracleConnection) DriverManager.getConnection  ("jdbc:oracle:thin:@hydra:1521:dev","egouge","emily2004");
	       }
	       catch( Throwable t ){
	    	   t.printStackTrace();
	    	   System.out.println("Warning: could not connect, configure "+getClass().getResource("test.properties"));
	    	   return;
	       }
	       System.out.println( connection.getTypeMap());
	    }	   
	    
	    public void close() throws SQLException{
	        if( connection != null ) connection.close();
	        connection = null;
	        if(cPool != null)
	            cPool.close();
	    }    
	}
