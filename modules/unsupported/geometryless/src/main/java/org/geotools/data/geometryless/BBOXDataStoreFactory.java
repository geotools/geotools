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
import org.geotools.factory.AbstractFactory;

/**
 * Creates a Geometryless JDBC based on the conection params.
 * 
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * <p>
 * REVISIT: I believe the use of the namespace param needs to be revisited.  GeoServer
 * is going to start making use of this, as the XML namespace that the feature
 * type should be created with.  The use of namespace in this package is that
 * of a database schema name.  Though investigating futher it looks like all
 * the dbs use it that way.  So this is just a note that xml namespace and
 * database namespace need to be reconciled.  The work done in this package
 * seems to be begging some datastore hierarchy refactoring, hopefully when
 * we do that we can also get jdbc datastore factories in a hierarchy, instead
 * of each just doing their own thing. -ch
 * </p>
 *
 * @author Rob Atkinson, Social Change Online
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 */
public class BBOXDataStoreFactory extends AbstractFactory 
    implements org.geotools.data.DataStoreFactorySpi {
    	
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    private static final String GEOM_NAME_DEFAULT = "the_geom";
    
    /** Specified JDBC driver class. */
    static final Param  DRIVER = new Param("driver", String.class,
            "Java Class name of installed driver", true, "org.geotools.data.geometryless.wrapper.PGConnectionPool");
            
               /** Specified JDBC driver class calling URL */
   static final Param  URLPREFIX  = new Param("urlprefix", String.class,
            "complete jdbc URL for the database connection", true, "jdbc:postgresql://localhost:5432/postgres");

    /** Param, package visibiity for JUnit tests */
    static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'bbox'", true, "BBOX");

    /** Param, package visibiity for JUnit tests */
    static final Param MINXCOLUMN = new Param("minxcolumn", String.class,
            "name of JDBC results column containing eastmost easting (x, longitude etc)", true, "minx");

    /** Param, package visibiity for JUnit tests */
    static final Param MINYCOLUMN = new Param("minycolumn", String.class,
            "name of JDBC results column containing southmost northing (y, latitude etc)", true, "miny");

    /** Param, package visibiity for JUnit tests */
    static final Param MAXXCOLUMN = new Param("maxxcolumn", String.class,
            "name of JDBC results column containing westmost easting (x, longitude etc)", true, "maxx");

    /** Param, package visibiity for JUnit tests */
    static final Param MAXYCOLUMN = new Param("maxycolumn", String.class,
            "name of JDBC results column containing northmost northing (y, latitude etc)", true, "maxy");

    static final Param GEOMNAME = new Param("geom_name", String.class, "the " +
      "name of the geometry attribute to generate from the bbox columns", false, GEOM_NAME_DEFAULT);

 

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
    static final Param SCHEMA = new Param("schema", String.class,
            "database schema", false);
            
    /** Param, package visibiity for JUnit tests */
    static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace prefix used", false);

    /** Array with all of the params */
    static final Param[] arrayParameters = {
        DBTYPE, SCHEMA, USER, PASSWD, CHARSET, NAMESPACE,DRIVER,URLPREFIX,MINXCOLUMN,MINYCOLUMN,MAXXCOLUMN,MAXYCOLUMN, GEOMNAME
    };


    /**
     * Creates a new instance of - this constructor needed for factory finder apparently
     */
    public BBOXDataStoreFactory() {

}
    /**
     * Creates a new instance of PostgisDataStoreFactory.
     * <p>
     * Note: the map of thins is used when constructing datastore instandces,
     * as such it needs to be passed onto the datastores so that they can make use of this
     * information when creating content.
     * </p>
     */
    public BBOXDataStoreFactory( Map hints) {
        this.hints.putAll( hints );
    }

    /**
     * Checks to see if all the postgis params are there.
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
     * @return <code>true</code> if dbtype equals BBOX, and contains keys
     *         for host, user, passwd, and database.
     */
    public boolean canProcess(Map params) {
        Object value;

        if (params != null) {
            for (int i = 0; i < arrayParameters.length; i++) {
                if (!(((value = params.get(arrayParameters[i].key)) != null)
                        && (arrayParameters[i].type.isInstance(value)))) {
                    if (arrayParameters[i].required) {
                    	LOGGER.config("BBOXDataStoreFactory: canProcess() Cannot find param " + arrayParameters[i].key + ":" + arrayParameters[i].type + value );
                        return (false);
                    }
                }
            }
        } else {
                   	LOGGER.finer("BBOXDataStoreFactory: can Process Cannot find params " );
            return (false);
        }

  
        if (!(((String) params.get("dbtype")).equalsIgnoreCase("BBOX"))) {
            return (false);
        } else {
            return (true);
        }
    }

    /**
     * Construct a postgis data store using the params.
     *
     * @param params The full set of information needed to construct a live
     *        data source.  Should have  dbtype equal to BBOX, as well as
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

//        String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
//        String port = (String) PORT.lookUp(params);
 //       String database = (String) DATABASE.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
                Charset charSet = (Charset) CHARSET.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        String driver =   (String) DRIVER.lookUp(params);
        String urlprefix =   (String) URLPREFIX.lookUp(params);
      String minxcolumn =   (String) MINXCOLUMN.lookUp(params);
      String minycolumn =   (String) MINYCOLUMN.lookUp(params);
      String maxxcolumn =   (String) MAXXCOLUMN.lookUp(params);
      String maxycolumn =   (String) MAXYCOLUMN.lookUp(params);
      String geom_name = (String) GEOMNAME.lookUp(params);

        // Try processing params first so we can get an error message
        // back to the user
        //
        if (!canProcess(params)) {
            return null;
        }
        /*
         * 
         *    JDBCConnectionFactory connFact = new JDBCConnectionFactory( urlprefix, driver  );

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
        
	if (geom_name == null) {
	    geom_name = GEOM_NAME_DEFAULT;
	}
        if (namespace != null) {
            return new BBOXDataStore(dataSource, schema, namespace, minxcolumn,
                                            minycolumn,maxxcolumn,maxycolumn, geom_name);
        } else {
            return new BBOXDataStore(dataSource);
        }
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
        return "GeometrylessJDBC - constructs geometry from Bounding Box ";
    }
   
    public String getDisplayName() {
        return "GeometrylessJDBC - Bounding Box";
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
            DBTYPE, USER, PASSWD, CHARSET, NAMESPACE, DRIVER, URLPREFIX, MINXCOLUMN, MINYCOLUMN
,MAXXCOLUMN, MAXYCOLUMN , GEOMNAME      };
    }

	/* (non-Javadoc)
	 * @see org.geotools.factory.Factory#getImplementationHints()
	 */
	public Map getImplementationHints() {
		return hints;
	}
}
