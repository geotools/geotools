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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.DataSourceUtil;

/**
 * Builds instances of the versioned Postgis datastore
 * 
 * @author aaime
 * @since 2.4
 * 
 *
 *
 * @source $URL$
 */
public class VersionedPostgisDataStoreFactory extends AbstractDataStoreFactory {
    
    /** The logger for the postgis module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    /** Creates PostGIS-specific JDBC driver class. */
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    public static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'postgis-versioned'", true, "postgis-versioned");

    public static final Param HOST = new Param("host", String.class, "postgis host machine", true,
            "localhost");

    public static final Param PORT = new Param("port", Integer.class,
            "postgis connection port (default is 5432)", true, new Integer(5432));

    public static final Param DATABASE = new Param("database", String.class, "postgis database");

    public static final Param SCHEMA = new Param("schema", String.class, "postgis schema", false,
            "public");

    public static final Param USER = new Param("user", String.class, "user name to login as");

    public static final Param PASSWD = new Param("passwd", String.class, "password used to login",
            false, null, Param.IS_PASSWORD, true);

    public static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace prefix used", false);
    
    /** parameter for data source */
    public static final Param DATASOURCE = new Param( "Data Source", DataSource.class, "Data Source", false );

    public static final Param WKBENABLED = new Param("wkb enabled", Boolean.class,
            "set to true if Well Known Binary should be used to read PostGIS "
                    + "data (experimental)", false, new Boolean(true));

    public static final Param LOOSEBBOX = new Param("loose bbox", Boolean.class,
            "set to true if the Bounding Box should be 'loose', faster but "
                    + "not as deadly accurate", false, new Boolean(true));
    
    public static final Param VERSIONALL = new Param("version enable all", Boolean.class,
            "set to true if you want all feature types to be version enabled on connection", false, new Boolean(false));

    /** Array with all of the params */
    static final Param[] arrayParameters = { DBTYPE, HOST, PORT, DATABASE, USER, PASSWD,
            WKBENABLED, LOOSEBBOX, NAMESPACE, VERSIONALL, DATASOURCE };

    /**
     * Creates a new instance of PostgisDataStoreFactory
     */
    public VersionedPostgisDataStoreFactory() {
    }

    public boolean canProcess(Map params) {
        if (!super.canProcess(params)) {
            return false; // was not in agreement with getParametersInfo
        }
        return ((String) params.get("dbtype")).equalsIgnoreCase("postgis-versioned");
    }

    /**
     * Construct a postgis data store using the params.
     * 
     * @param params
     *            The full set of information needed to construct a live data
     *            source. Should have dbtype equal to postgis, as well as host,
     *            user, passwd, database, and table.
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
        // lookup will throw error message for
        // miscoversion or lack of required param
        //
        String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        Boolean wkb_enabled = (Boolean) WKBENABLED.lookUp(params);
        Boolean is_loose_bbox = (Boolean) LOOSEBBOX.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        Boolean versionAll = (Boolean) VERSIONALL.lookUp(params);

        // Try processing params first so we can get real IO
        // error message back to the user
        //
        if (!canProcess(params)) {
            throw new IOException("The parameters map isn't correct!!");
        }
        
        DataSource source = (DataSource)DATASOURCE.lookUp(params);
        if(source == null) {
        	String url = "jdbc:postgresql" + "://" + host + ":" + port + "/" + database;
        	source = DataSourceUtil.buildDefaultDataSource(url, "org.postgresql.Driver", user, passwd, "select now()");
        }
        VersionedPostgisDataStore dataStore = createDataStoreInternal(source, namespace, schema);

        if (wkb_enabled != null) {
            dataStore.setWKBEnabled(wkb_enabled.booleanValue());
        }

        if (is_loose_bbox != null) {
            dataStore.setLooseBbox(is_loose_bbox.booleanValue());
        }
        
        if(versionAll != null && versionAll.booleanValue()) {
            String[] typeNames = dataStore.getTypeNames();
            for (int i = 0; i < typeNames.length; i++) {
                if(typeNames[i].equals(VersionedPostgisDataStore.TBL_CHANGESETS))
                    continue;
                try {
                    dataStore.setVersioned(typeNames[i], true, null, null);
                } catch(IOException e) {
                    LOGGER.log(Level.SEVERE, "Could not version enable: " + typeNames[i], e);
                }
            }
        }

        return dataStore;
    }

    protected VersionedPostgisDataStore createDataStoreInternal(DataSource dataSource,
            String namespace, String schema) throws IOException {

        if (schema == null && namespace == null)
            return new VersionedPostgisDataStore(dataSource);

        if (schema == null && namespace != null) {
            return new VersionedPostgisDataStore(dataSource, namespace);
        }

        return new VersionedPostgisDataStore(dataSource, schema, namespace);
    }

    /**
     * Postgis cannot create a new database.
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
        throw new UnsupportedOperationException("Postgis cannot create a new Database");
    }

    public String getDisplayName() {
        return "Versioning Postgis";
    }

    /**
     * Describe the nature of the datasource constructed by this factory.
     * 
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.
     */
    public String getDescription() {
        return "PostGIS spatial database with versioning support";
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
        return arrayParameters;
    }
}
