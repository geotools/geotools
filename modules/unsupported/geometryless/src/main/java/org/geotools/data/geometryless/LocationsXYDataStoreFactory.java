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
 * This factory should be registered in the META-INF/ folder, under services/ in
 * the DataStoreFactorySpi file.
 * </p>
 * 
 * <p>
 * REVISIT: I believe the use of the namespace param needs to be revisited.
 * GeoServer is going to start making use of this, as the XML namespace that the
 * feature type should be created with. The use of namespace in this package is
 * that of a database schema name. Though investigating futher it looks like all
 * the dbs use it that way. So this is just a note that xml namespace and
 * database namespace need to be reconciled. The work done in this package seems
 * to be begging some datastore hierarchy refactoring, hopefully when we do that
 * we can also get jdbc datastore factories in a hierarchy, instead of each just
 * doing their own thing. -ch
 * </p>
 * 
 * @author Rob Atkinson, Social Change Online
 * @author Chris Holmes, TOPP

 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/geometryless/src/main/java/org/geotools/data/geometryless/LocationsXYDataStoreFactory.java $
 * @version $Id: LocationsXYDataStoreFactory.java 25031 2007-04-05 09:52:31Z
 *          robatkinson $

 */
public class LocationsXYDataStoreFactory extends AbstractFactory implements
        org.geotools.data.DataStoreFactorySpi {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    private static final String GEOM_NAME_DEFAULT = "the_geom";

    /** Specified JDBC driver class. */
    static final Param DRIVER = new Param("driver", String.class,
            "Java Class name of installed ConectionPool JDBC driver", true,
            "org.geotools.data.geometryless.wrapper.PGConnectionPool");

    /** Specified JDBC driver class calling URL */
    static final Param URLPREFIX = new Param("urlprefix", String.class,
            "complete jdbc URL for the database connection", true,
            "jdbc:postgresql://localhost:5432/postgres");

    /** Param, package visibiity for JUnit tests */
    static final Param DBTYPE = new Param("dbtype", String.class, "must be 'locationsxy'", true,
            "locationsxy");

    /** Param, package visibiity for JUnit tests */
    static final Param XCOLUMN = new Param("xcolumn", String.class,
            "name of JDBC results column containing easting (x, longitude etc)", true, "longitude");

    /** Param, package visibiity for JUnit tests */
    static final Param YCOLUMN = new Param("ycolumn", String.class,
            "name of JDBC results column containing northing (y, latitude etc)", true, "latitude");

    static final Param GEOMNAME = new Param("geom_name", String.class, "the "
            + "name of the geometry attribute generated from the x,y columns", true,
            GEOM_NAME_DEFAULT);

    /** Param, package visibiity for JUnit tests */
    /*
     * static final Param HOST = new Param("host", String.class, "db host
     * machine", false, "localhost");
     */
    /** Param, package visibiity for JUnit tests */
    /*
     * static final Param PORT = new Param("port", String.class, "db connection
     * port", false, "3306");
     */
    /** Param, package visibiity for JUnit tests */
    /*
     * static final Param DATABASE = new Param("database", String.class, "jdbc
     * database", false, "" );
     */

    /** Param, package visibiity for JUnit tests */
    static final Param USER = new Param("user", String.class, "user name to login as", true, "");

    /** Param, package visibiity for JUnit tests */
    static final Param PASSWD = new Param("passwd", String.class, "password used to login", true,
            "");

    /**
     * Param, package visibiity for JUnit tests.
     * 
     * <p>
     * Example of a non simple Param type where custom parse method is required.
     * </p>
     * 
     * <p>
     * When we convert to BeanInfo custom PropertyEditors will be required for
     * this Param.
     * </p>
     */
    static final Param CHARSET = new Param("charset", Charset.class, "character set", false,
            Charset.forName("ISO-8859-1")) {
        public Object parse(String text) throws IOException {
            return Charset.forName(text);
        }

        public String text(Object value) {
            return ((Charset) value).name();
        }
    };

    /** Param, package visibiity for JUnit tests */
    static final Param NAMESPACE = new Param("namespace", String.class, "namespace prefix used",
            false);

    /** Param, package visibiity for JUnit tests */
    static final Param SCHEMA = new Param("schema", String.class, "database schema", false);

    /** Array with all of the params */
    static final Param[] arrayParameters = { DBTYPE, SCHEMA, USER, PASSWD, CHARSET, NAMESPACE,
            DRIVER, URLPREFIX, XCOLUMN, YCOLUMN, GEOMNAME };

    /**
     * Creates a new instance of - this constructor needed for factory finder
     * apparently
     */
    public LocationsXYDataStoreFactory() {

    }

    /**
     * Creates a new instance of PostgisDataStoreFactory
     */
    public LocationsXYDataStoreFactory(Map hints) {
        this.hints.putAll(hints);
    }

    /**
     * Checks to see if all the postgis params are there.
     * 
     * <p>
     * Should have:
     * </p>
     * 
     * <ul>
     * <li> dbtype: equal to postgis </li>
     * <li> host </li>
     * <li> user </li>
     * <li> passwd </li>
     * <li> database </li>
     * <li> charset </li>
     * </ul>
     * 
     * 
     * @param params
     *            Set of parameters needed for a jdbc data store.
     * 
     * @return <code>true</code> if dbtype equals locationsXy, and contains
     *         keys for host, user, passwd, and database.
     */
    public boolean canProcess(Map params) {
        Object value;

        if (params != null) {
            for (int i = 0; i < arrayParameters.length; i++) {
                if (!(((value = params.get(arrayParameters[i].key)) != null) && (arrayParameters[i].type
                        .isInstance(value)))) {
                    if (arrayParameters[i].required) {

                    	LOGGER.config("LocationsXYDataStoreFactory: canProcess() Cannot find param " + arrayParameters[i].key + ":" + arrayParameters[i].type + value );

                        return (false);
                    }
                }
            }
        } else {
            LOGGER.finer("LocationsXYDataStoreFactory: can Process Cannot find params ");
            return (false);
        }

        if (!(((String) params.get("dbtype")).equalsIgnoreCase("locationsxy"))) {
            return (false);
        } else {
            return (true);
        }
    }

    /**
     * Construct a postgis data store using the params.
     * 
     * @param params
     *            The full set of information needed to construct a live data
     *            source. Should have dbtype equal to locationsxy, as well as
     *            host, user, passwd, database, and table.
     * 
     * @return The created DataSource, this may be null if the required resource
     *         was not found or if insufficent parameters were given. Note that
     *         canProcess() should have returned false if the problem is to do
     *         with insuficent parameters.
     * 
     * @throws IOException
     *             See DataSourceException
     * @throws DataSourceException
     *             Thrown if there were any problems creating or connecting the
     *             datasource.
     */
    public DataStore createDataStore(Map params) throws IOException {
        if (canProcess(params)) {
        } else {
            throw new IOException("The parameteres map isn't correct!!");
        }

        // String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        // String port = (String) PORT.lookUp(params);
        // String database = (String) DATABASE.lookUp(params);
        Charset charSet = (Charset) CHARSET.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
        String driver = (String) DRIVER.lookUp(params);
        String urlprefix = (String) URLPREFIX.lookUp(params);
        String xcolumn = (String) XCOLUMN.lookUp(params);
        String ycolumn = (String) YCOLUMN.lookUp(params);
        String geom_name = (String) GEOMNAME.lookUp(params);

        // Try processing params first so we can get an error message
        // back to the user
        //
        if (!canProcess(params)) {
            return null;
        }
        
        /*
         * 
         
        JDBCConnectionFactory connFact = new JDBCConnectionFactory(urlprefix, driver);

        // MySQLConnectionFactory connFact = new MySQLConnectionFactory(host,
        // Integer.parseInt(port), database);

        connFact.setLogin(user, passwd);

        if (charSet != null) {
            connFact.setCharSet(charSet.name());
        }

        ConnectionPool pool;

        try {
            pool = connFact.getConnectionPool();

            java.sql.Connection c = pool.getConnection();
            if (c == null) {
                throw new SQLException("Pool created but connection null ");
            } else {
                c.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException("Could not create connection", e);
        }

        if (geom_name == null) {
            geom_name = GEOM_NAME_DEFAULT;
        }
*/
       DataSource dataSource = DataSourceUtil.buildDefaultDataSource(urlprefix, driver, user, passwd, null);
        
       
        
        LocationsXYDataStore dataStore = new LocationsXYDataStore(dataSource, schema, namespace, xcolumn, ycolumn, geom_name);
        
        ViewRegisteringFactoryHelper.registerSqlViews(dataStore, params);
        
        return dataStore;

    }

    /**
     * The datastore cannot create a new database.
     * 
     * @param params
     * 
     * 
     * @throws IOException
     *             See UnsupportedOperationException
     * @throws UnsupportedOperationException
     *             Cannot create new database
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
        return "Generic JDBC database with X,Y columns holding point coordinates";
    }

    public String getDisplayName() {
        return "Geometryless JDBC source - implementing location from numeric X,Y columns";
    }

    // public DataSourceMetadataEnity createMetadata( Map params ) throws
    // IOException {
    // String host = (String) HOST.lookUp(params);
    // String user = (String) USER.lookUp(params);
    // String port = (String) PORT.lookUp(params);
    // String database = (String) DATABASE.lookUp(params);
    // return new DataSourceMetadataEnity( host+":"+port, database, "Connection
    // to "+getDisplayName()+" on "+host+" as "+user );
    // }
    /**
     * Test to see if this datastore is available, if it has all the appropriate
     * libraries to construct a datastore. This datastore just returns true for
     * now. This method is used for gui apps, so as to not advertise data store
     * capabilities they don't actually have.
     * 
     * @return <tt>true</tt> if and only if this factory is available to
     *         create DataStores.
     */
    public boolean isAvailable() {
        /*
         * try { Class.forName(DRIVER); } catch (ClassNotFoundException cnfe) {
         * return false; }
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
        return new Param[] { DBTYPE, SCHEMA, USER, PASSWD, CHARSET, NAMESPACE, DRIVER, URLPREFIX,
                XCOLUMN, YCOLUMN, GEOMNAME };
    }
}
