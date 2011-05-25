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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.Parameter;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.util.KVP;
import org.geotools.util.SimpleInternationalString;


/**
 * Creates a PostgisDataStore baed on the correct params.
 * 
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class PostgisDataStoreFactory extends AbstractDataStoreFactory
    implements org.geotools.data.DataStoreFactorySpi {
    /** Creates PostGIS-specific JDBC driver class. */
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    public static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'postgis'", true, "postgis",
            new KVP( Param.LEVEL, "program") );

    public static final Param HOST = new Param("host", String.class,
            "postgis host machine", true, "localhost");

    public static final Param PORT = new Param("port", Integer.class,
            "postgis connection port (default is 5432)", true, new Integer(5432));

    public static final Param DATABASE = new Param("database", String.class,
            "postgis database");

    public static final Param SCHEMA = new Param("schema", String.class,
    		"postgis schema", false, "public");
    
    public static final Param USER = new Param("user", String.class,
            "user name to login as");

    public static final Param PASSWD = new Param("passwd", String.class,
            new SimpleInternationalString("password used to login"),
            false, null, 
            new KVP( Parameter.IS_PASSWORD, Boolean.TRUE));
    
    public static final Param MAXCONN = new Param("max connections", Integer.class,
            "maximum number of open connections", false, 10,
            new KVP( Param.LEVEL, "program"));
    
    public static final Param MINCONN = new Param("min connections", Integer.class,
            "minimum number of pooled connection", false, 4,
            new KVP( Param.LEVEL, "program"));
    
    public static final Param VALIDATECONN = new Param("validate connections", Boolean .class,
            "check connection is alive before using it", false, Boolean.FALSE,
            new KVP( Param.LEVEL, "program"));

    public static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace prefix used", false,
            new KVP( Param.LEVEL, "advanced"));

    public static final Param WKBENABLED = new Param("wkb enabled", Boolean.class,
            "set to true if Well Known Binary should be used to read PostGIS "
            + "data (experimental)", false, new Boolean(true),
            new KVP( Param.LEVEL, "advanced"));

    public static final Param LOOSEBBOX = new Param("loose bbox", Boolean.class,
            "set to true if the Bounding Box should be 'loose', faster but "
            + "not as deadly accurate", false, new Boolean(true),
            new KVP( Param.LEVEL, "advanced"));

    public static final Param ESTIMATEDEXTENT = new Param( "estimated extent", Boolean.class,
    		"set to true if the bounds for a table should be computed using the " + 
    		"'estimated_extent' function, but beware that this function is less accurate, " +
    		"and in some cases *far* less accurate if the data within the actual bounds " +
    		"does not follow a uniform distribution. It also relies on the fact that you have" +
    		"accurate table stats available. So it is a good idea to 'VACUUM ANALYZE' " +
    		"the postgis table.", false, new Boolean(false),
    		new KVP( Param.LEVEL, "advanced"));
    
    /**
     * Creates a new instance of PostgisDataStoreFactory
     */
    public PostgisDataStoreFactory() {
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
     * @param params Set of parameters needed for a postgis data store.
     *
     * @return <code>true</code> if dbtype equals postgis, and contains keys
     *         for host, user, passwd, and database.
     */
    public boolean canProcess(Map params) {
        if( !super.canProcess( params ) ){
            return false; // was not in agreement with getParametersInfo
        }
        if (!(((String) params.get("dbtype")).equalsIgnoreCase("postgis"))) {
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
        // lookup will throw error message for
        // miscoversion or lack of required param
        //
        String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        Integer minConn = (Integer) MINCONN.lookUp(params);
        Boolean validateConn = (Boolean) VALIDATECONN.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        Boolean wkb_enabled = (Boolean) WKBENABLED.lookUp(params);
        Boolean is_loose_bbox = (Boolean) LOOSEBBOX.lookUp(params);
        Boolean is_estimated_extent = (Boolean) ESTIMATEDEXTENT.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        
        // Try processing params first so we can get real IO
        // error message back to the user
        //
        if (!canProcess(params)) {
            throw new IOException("The parameters map isn't correct!!");
        }        


        boolean validate = validateConn != null && validateConn.booleanValue();
        int maxActive = maxConn != null ? maxConn.intValue() : 10;
        int maxIdle = minConn != null ? minConn.intValue() : 4;
        DataSource source = getDefaultDataSource(host, user, passwd, port.intValue(), database, maxActive, maxIdle, validate);

        PostgisDataStore dataStore = createDataStoreInternal(source,namespace,schema);

        if (wkb_enabled != null) {
            dataStore.setWKBEnabled(wkb_enabled.booleanValue());
        }

        if (is_loose_bbox != null) {
            dataStore.setLooseBbox(is_loose_bbox.booleanValue());
        }
        
        if (is_estimated_extent != null) {
        	//ensure optimize mode set to OPTIMIZE_SQL
        	dataStore.setOptimizeMode( PostgisDataStore.OPTIMIZE_SQL );
        	dataStore.setEstimatedExtent(is_estimated_extent.booleanValue());
        }
        return dataStore;
    }
    
    public static ManageableDataSource getDefaultDataSource(String host, String user, String passwd, int port, String database, int maxActive, int minIdle, boolean validate) throws DataSourceException {
        String url = "jdbc:postgresql" + "://" + host + ":" + port + "/" + database;
        String driver = "org.postgresql.Driver";
        return DataSourceUtil.buildDefaultDataSource(url, driver, user, passwd, maxActive, minIdle, validate ? "select now()" : null, false, 0);
    }
    
    protected PostgisDataStore createDataStoreInternal(
		DataSource dataSource, String namespace, String schema
    ) throws IOException {
    	
    	if (schema == null && namespace == null)
    		return new PostgisDataStore(dataSource); 
    	
    	if (schema == null && namespace != null) {
    		return new PostgisDataStore(dataSource,namespace);
    	}
    	
    	return new PostgisDataStore(dataSource,schema,namespace);
    }
    

    /**
     * Postgis cannot create a new database.
     *
     * @param params
     *
     *
     * @throws IOException See UnsupportedOperationException
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException(
            "Postgis cannot create a new Database");
    }
    
    public String getDisplayName() {
        return "Postgis";
    }
    /**
     * Describe the nature of the datasource constructed by this factory.
     *
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.
     */
    public String getDescription() {
        return "PostGIS spatial database";
    }

    /**
     * Determines if the appropriate libraries are present for this datastore
     * factory to successfully produce postgis datastores.  
     * 
     * @return <tt>true</tt> if the postgresql jar is on the classpath.
     */
    public boolean isAvailable() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
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
            DBTYPE, HOST, PORT, SCHEMA, DATABASE, USER, PASSWD, MAXCONN, MINCONN, VALIDATECONN, WKBENABLED, 
            LOOSEBBOX, ESTIMATEDEXTENT, NAMESPACE 
        };
    }
}
