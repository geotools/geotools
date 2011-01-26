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
package org.geotools.data.geometryless;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.sql.ViewRegisteringFactoryHelper;
import org.geotools.factory.AbstractFactory;

/**
 * Creates a Geometryless JDBC based on the conection params.
 * 
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * @author Rob Atkinson, Social Change Online
 * @source $URL$
 */
public class JDBCDataStoreFactory extends AbstractFactory
    implements org.geotools.data.DataStoreFactorySpi {
    	
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");


    /** Specified JDBC driver class. */
    static final Param  DRIVER = new Param("driver", String.class,
            "Java Class name of installed ConnectionPool driver", true, "org.geotools.data.geometryless.wrapper.PGConnectionPool");
            
               /** Specified JDBC driver class calling URL */
   static final Param  URLPREFIX  = new Param("urlprefix", String.class,
            "eg jdbc:mysql://", true, "jdbc:postgresql://localhost:5432/postgres");

    /** Param, package visibiity for JUnit tests */
    static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'jdbc'", true, "jdbc");



    /** Param, package visibiity for JUnit tests */
    static final Param USER = new Param("user", String.class,
            "user name to login as", true, "");

    /** Param, package visibiity for JUnit tests */
    static final Param PASSWD = new Param("passwd", String.class,
            "password used to login", true, "");


    /**
     * Param, package visibiity for JUnit tests.
     * 
     * <p>
     * Example of a non simple Param type where custom parse method is
     * required.
     * </p>
     * 
     * <p>
     * When we convert to BeanInfo custom PropertyEditors will be required for
     * this Param.
     * </p>
     */
    static final Param CHARSET = new Param("charset", Charset.class,
            "character set", false, Charset.forName("ISO-8859-1")) {
            public Object parse(String text) throws IOException {
                return Charset.forName(text);
            }

            public String text(Object value) {
                return ((Charset) value).name();
            }
        };

    /** Param, package visibiity for JUnit tests */
    static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace prefix used", false);

    /** Param, package visibiity for JUnit tests */
    static final Param SCHEMA = new Param("schema", String.class,
            "database schema", false);

    /** Array with all of the params */
    static final Param[] arrayParameters = {
        DBTYPE, SCHEMA, USER, PASSWD, CHARSET, NAMESPACE,DRIVER,URLPREFIX
    };

   /**
     * Creates a new instance of - this constructor needed for factory finder apparently
     */
    public JDBCDataStoreFactory() {

}

    /**
     * Creates a new instance of PostgisDataStoreFactory
     */
    public JDBCDataStoreFactory( Map hints ) {
	if( hints != null )
	        this.hints.putAll( hints );
    }

    /**
     * Checks to see if all the postgis params are there.
     * 
     * <p>
     * Should have:
     * </p>
     * 
     * <ul>
     * <li>
     * dbtype: equal to postgis
     * </li>
     * <li>
     * host
     * </li>
     * <li>
     * user
     * </li>
     * <li>
     * passwd
     * </li>
     * <li>
     * database
     * </li>
     * <li>
     * charset
     * </li>
     * </ul>
     * 
     *
     * @param params Set of parameters needed for a jdbc data store.
     *
     * @return <code>true</code> if dbtype equals jdbc, and contains keys
     *         for host, user, passwd, and database.
     */
    public boolean canProcess(Map params) {
        Object value;

        if (params != null) {
            for (int i = 0; i < arrayParameters.length; i++) {
                if (!(((value = params.get(arrayParameters[i].key)) != null)
                        && (arrayParameters[i].type.isInstance(value)))) {
                    if (arrayParameters[i].required) {
                    	LOGGER.config("JDBCDataStoreFactory: canProcess() Cannot find param " + arrayParameters[i].key + ":" + arrayParameters[i].type + value );
                        return (false);
                    }
                }
            }
        } else {
                   	LOGGER.finer("JDBCDataStoreFactory: can Process Cannot find params " );
            return (false);
        }

  
        if (!(((String) params.get("dbtype")).equalsIgnoreCase("jdbc"))) {
            return (false);
        } else {
            return (true);
        }
    }

    /**
     * Construct a postgis data store using the params.
     *
     * @param params The full set of information needed to construct a live
     *        data source.  Should have  dbtype equal to postgis, as well as
     *        host, user, passwd, database, and table.
     *
     * @return The created DataSource, this may be null if the required
     *         resource was not found or if insufficent parameters were given.
     *         Note that canProcess() should have returned false if the
     *         problem is to do with insuficent parameters.
     *
     * @throws IOException See DataSourceException
     * @throws DataSourceException Thrown if there were any problems creating
     *         or connecting the datasource.
     */
    public DataStore createDataStore(Map params) throws IOException {
        if (canProcess(params)) {
        } else {
            throw new IOException("The parameteres map isn't correct!!");
        }

 //       String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
 //       String port = (String) PORT.lookUp(params);
 //       String database = (String) DATABASE.lookUp(params);
        Charset charSet = (Charset) CHARSET.lookUp(params);
        String schema = (String) SCHEMA.lookUp( params ); 

        String namespace = (String) NAMESPACE.lookUp(params);
        String driver =   (String) DRIVER.lookUp(params);
        String urlprefix =   (String) URLPREFIX.lookUp(params);

        // Try processing params first so we can get an error message
        // back to the user
        //
        if (!canProcess(params)) {
            return null;
        }
/*
 * 
 *   All this stuff replaced with the  DataSource implementation
 *   
 *   which is basically exactly the same pattern here...
 *  
 *   JDBCConnectionFactory connFact = new JDBCConnectionFactory( urlprefix, driver );

 //  MySQLConnectionFactory connFact = new MySQLConnectionFactory(host,            Integer.parseInt(port), database);
           
        connFact.setLogin(user, passwd);

        if (charSet != null) {
            connFact.setCharSet(charSet.name());
        }

        ConnectionPool pool;

        try {
            pool = connFact.getConnectionPool();
            
            java.sql.Connection c = pool.getConnection();
            if( c == null )
            {
          	throw new SQLException("Pool created but connection null ");
            }
            else {
             	c.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException("Could not create connection", e);
        }
*/

        DataSource dataSource = DataSourceUtil.buildDefaultDataSource(urlprefix, driver, user, passwd, null);
        
        JDBCDataStore dataStore;
        if (namespace != null) {
            dataStore = new JDBCDataStore(dataSource, schema, namespace);
        } else {
            dataStore = new JDBCDataStore(dataSource);
        }
        
        ViewRegisteringFactoryHelper.registerSqlViews(dataStore, params);
        
        return dataStore;
    }

 
    
    /**
     * The datastore  cannot create a new database.
     *
     * @param params
     *
     *
     * @throws IOException See UnsupportedOperationException
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException(
            "Generic JDBC datastore cannot create a new Database");
    }

    /**
     * Describe the nature of the datasource constructed by this factory.
     *
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.
     */
    public String getDescription() {
        return "Generic JDBC database";
    }
   
    public String getDisplayName() {
        return "GeometrylessJDBC - no geometry per feature";
    }
    
//    	public DataSourceMetadataEnity createMetadata( Map params ) throws IOException {
//	    String host = (String) HOST.lookUp(params);
//        String user = (String) USER.lookUp(params);
//        String port = (String) PORT.lookUp(params);
//        String database = (String) DATABASE.lookUp(params);
//        return new DataSourceMetadataEnity( host+":"+port, database, "Connection to "+getDisplayName()+" on "+host+" as "+user );
//	}
    /**
     * Test to see if this datastore is available, if it has all the
     * appropriate libraries to construct a datastore.  This datastore just
     * returns true for now.  This method is used for gui apps, so as to not
     * advertise data store capabilities they don't actually have.
     *
     * @return <tt>true</tt> if and only if this factory is available to create
     *         DataStores.
     */
    public boolean isAvailable() {
  /*       try {
             Class.forName(DRIVER);
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
   */
        return true;
    }

    /**
     * Describe parameters.
     *
     *
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] {
        		  DBTYPE, SCHEMA, USER, PASSWD, CHARSET, NAMESPACE,DRIVER,URLPREFIX
  
        };
    }
}
