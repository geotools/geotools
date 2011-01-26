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
 *
 */
package org.geotools.arcsde;

import static org.geotools.arcsde.data.ArcSDEDataStoreConfig.ALLOW_NON_SPATIAL_TABLES_PARAM_NAME;
import static org.geotools.arcsde.data.ArcSDEDataStoreConfig.DBTYPE_PARAM_NAME;
import static org.geotools.arcsde.data.ArcSDEDataStoreConfig.NAMESPACE_PARAM_NAME;
import static org.geotools.arcsde.data.ArcSDEDataStoreConfig.VERSION_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.CONNECTION_TIMEOUT_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MAX_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MIN_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PASSWORD_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PORT_NUMBER_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.USER_NAME_PARAM_NAME;

import java.awt.RenderingHints;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.arcsde.data.ArcSDEDataStore;
import org.geotools.arcsde.data.ArcSDEDataStoreConfig;
import org.geotools.arcsde.data.ViewRegisteringFactoryHelper;
import org.geotools.arcsde.session.Commands;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeRelease;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import com.esri.sde.sdk.pe.PeFactory;

/**
 * Factory to create DataStores over a live ArcSDE instance.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @source $URL: http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/
 *         datastore/src/main/java /org/geotools/arcsde/ArcSDEDataStoreFactory.java $
 * @version $Id: ArcSDEDataStoreFactory.java 95099 2011-01-13 14:18:17Z WIEN1::lanadvhmu $
 */
@SuppressWarnings("unchecked")
public final class ArcSDEDataStoreFactory implements DataStoreFactorySpi {
    /** package's logger */
    protected static final Logger LOGGER = Logging
            .getLogger(ArcSDEDataStoreFactory.class.getName());

    /** friendly factory description */
    public static final String FACTORY_DESCRIPTION = "ESRI(tm) ArcSDE 9.2+ vector data store";

    private static List<Param> paramMetadata = new ArrayList<Param>(10);

    public static final int JSDE_VERSION_DUMMY = -1;

    public static final int JSDE_VERSION_90 = 0;

    public static final int JSDE_VERSION_91 = 1;

    public static final int JSDE_VERSION_92 = 2;

    public static final int JSDE_VERSION_93 = 3;

    private static int JSDE_CLIENT_VERSION;

    public static final Param NAMESPACE_PARAM = new Param(NAMESPACE_PARAM_NAME, String.class,
            "namespace associated to this data store", false);

    public static final Param DBTYPE_PARAM = new Param(DBTYPE_PARAM_NAME, String.class,
            "fixed value. Must be \"arcsde\"", true, "arcsde");

    public static final Param SERVER_PARAM = new Param(SERVER_NAME_PARAM_NAME, String.class,
            "sever name where the ArcSDE gateway is running", true);

    /** In order to use Direct Connect, port parameter has to be of type String */
    public static final Param PORT_PARAM = new Param(
            PORT_NUMBER_PARAM_NAME,
            String.class,
            "port number in wich the ArcSDE server is listening for connections.Generally it's 5151",
            true, Integer.valueOf(5151));

    public static final Param INSTANCE_PARAM = new Param(INSTANCE_NAME_PARAM_NAME, String.class,
            "the specific database to connect to. Only applicable to "
                    + "certain databases. Value ignored if not applicable.", false);

    public static final Param USER_PARAM = new Param(USER_NAME_PARAM_NAME, String.class,
            "name of a valid database user account.", true);

    public static final Param PASSWORD_PARAM = new Param(PASSWORD_PARAM_NAME, String.class,
            new SimpleInternationalString("the database user's password."), false, null,
            Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));

    public static final Param MIN_CONNECTIONS_PARAM = new Param(MIN_CONNECTIONS_PARAM_NAME,
            Integer.class, "Minimun number of open connections", false,
            Integer.valueOf(ArcSDEDataStoreConfig.DEFAULT_CONNECTIONS));

    public static final Param MAX_CONNECTIONS_PARAM = new Param(MAX_CONNECTIONS_PARAM_NAME,
            Integer.class, "Maximun number of open connections (will not work if < 2)", false,
            Integer.valueOf(ArcSDEDataStoreConfig.DEFAULT_MAX_CONNECTIONS));

    public static final Param TIMEOUT_PARAM = new Param(CONNECTION_TIMEOUT_PARAM_NAME,
            Integer.class,
            "Milliseconds to wait for an available connection before failing to connect", false,
            Integer.valueOf(ArcSDEDataStoreConfig.DEFAULT_MAX_WAIT_TIME));

    public static final Param VERSION_PARAM = new Param(VERSION_PARAM_NAME, String.class,
            "The ArcSDE database version to use.", false);

    public static final Param ALLOW_NON_SPATIAL_PARAM = new Param(
            ALLOW_NON_SPATIAL_TABLES_PARAM_NAME, Boolean.class,
            "If enabled, registered non-spatial tables are also published.", false, Boolean.FALSE);

    static {
        paramMetadata.add(NAMESPACE_PARAM);
        paramMetadata.add(DBTYPE_PARAM);
        paramMetadata.add(SERVER_PARAM);
        paramMetadata.add(PORT_PARAM);
        paramMetadata.add(INSTANCE_PARAM);
        paramMetadata.add(USER_PARAM);
        paramMetadata.add(PASSWORD_PARAM);
        // optional parameters:
        paramMetadata.add(MIN_CONNECTIONS_PARAM);
        paramMetadata.add(MAX_CONNECTIONS_PARAM);
        paramMetadata.add(TIMEOUT_PARAM);
        paramMetadata.add(VERSION_PARAM);
        paramMetadata.add(ALLOW_NON_SPATIAL_PARAM);

        // determine which JSDE api we're running against
        determineJsdeVersion();
    }

    private static void determineJsdeVersion() {
        try {
            // this class only exists in the dummy api...
            Class.forName("com.esri.sde.sdk.GeoToolsDummyAPI");
            JSDE_CLIENT_VERSION = JSDE_VERSION_DUMMY;
        } catch (Throwable t) {
            // good, we're not using the Dummy API placeholder.
            try {
                // SeDBTune only exists in 9.2
                Class.forName("com.esri.sde.sdk.client.SeDBTune");
                JSDE_CLIENT_VERSION = JSDE_VERSION_92;
                LOGGER.fine("Using ArcSDE API version 9.2 (or higher)");
            } catch (Throwable t2) {
                // we're using 9.1 or 9.0.
                try {
                    int[] projcss = PeFactory.projcsCodelist();
                    if (projcss.length == 16380) {
                        // perhaps I am the hack-master.
                        JSDE_CLIENT_VERSION = JSDE_VERSION_91;
                        LOGGER.fine("Using ArcSDE API version 9.1");
                    } else {
                        JSDE_CLIENT_VERSION = JSDE_VERSION_90;
                        LOGGER.fine("Using ArcSDE API version 9.0 (or an earlier 8.x version)");
                    }
                } catch (Throwable crap) {
                    // not sure what happened here... This next line is
                    // un-intelligent.
                    JSDE_CLIENT_VERSION = JSDE_VERSION_90;
                }
            }
        }
    }

    /** factory of connection pools to different SDE databases */
    private static final ISessionPoolFactory poolFactory = SessionPoolFactory.getInstance();

    /**
     * empty constructor
     */
    public ArcSDEDataStoreFactory() {
        if (!isAvailable()) {
            LOGGER.finest("The ESRI ArcSDE Java API seems to not be on your classpath. Please"
                    + " verify that all needed jars are. ArcSDE data stores"
                    + " will not be available.");
        }
    }

    /**
     * @throws UnsupportedOperationException
     *             always as the operation is not supported
     * @see DataStoreFactorySpi#createNewDataStore(Map)
     */
    public DataStore createNewDataStore(java.util.Map<String, Serializable> map) {
        throw new UnsupportedOperationException(
                "ArcSDE DataStore does not support the creation of new databases. "
                        + "This should be done through database's specific tools");
    }

    /**
     * crates an SdeDataSource based on connection parameters held in <code>params</code>.
     * <p>
     * Expected parameters are:
     * <ul>
     * <li>{@code dbtype}: MUST be <code>"arcsde"</code></li>
     * <li>{@code server}: machine name where ArcSDE is running</li>
     * <li>{@code port}: port number where ArcSDE listens for connections on server</li>
     * <li>{@code instance}: database instance name to connect to</li>
     * <li>{@code user}: database user name with at least reading privileges over SDE instance</li>
     * <li>{@code password}: database user password</li>
     * </ul>
     * </p>
     * <p>
     * Optional parameters:
     * <ul>
     * <li>{@code pool.minConnections}: how many connections to open when the datastore is created
     * <li>{@code pool.maxConnections}: max limit of connections for the connection pool
     * <li>{@code pool.timeOut}: how many milliseconds to wait for a free connection before failing
     * to execute a request
     * <li>{@code version}: name of the ArcSDE version for the data store to work upon
     * </ul>
     * </p>
     * 
     * @param params
     *            connection parameters
     * @return a new <code>SdeDataStore</code> pointing to the database defined by
     *         <code>params</code>
     * @throws java.io.IOException
     *             if something goes wrong creating the datastore.
     */
    public DataStore createDataStore(final Map<String, Serializable> params)
            throws java.io.IOException {
        if (JSDE_CLIENT_VERSION == JSDE_VERSION_DUMMY) {
            throw new DataSourceException("Can't connect to ArcSDE with the dummy jar.");
        }

        ArcSDEDataStore sdeDStore = null;
        ArcSDEDataStoreConfig config = new ArcSDEDataStoreConfig(params);
        sdeDStore = createDataStore(config);

        ViewRegisteringFactoryHelper.registerSqlViews(sdeDStore, params);

        return sdeDStore;
    }

    final ArcSDEDataStore createDataStore(ArcSDEDataStoreConfig config) throws IOException {
        // create a new session pool to be used only by this datastore
        final ISessionPool connPool = poolFactory.createPool(config.getSessionConfig());

        return createDataStore(config, connPool);
    }

    final ArcSDEDataStore createDataStore(ArcSDEDataStoreConfig config, final ISessionPool connPool)
            throws IOException {
        ArcSDEDataStore sdeDStore;
        ISession session;
        try {
            session = connPool.getSession(false);
        } catch (UnavailableConnectionException e) {
            throw new RuntimeException(e);
        }
        try {
            // check to see if our sdk is compatible with this arcsde instance
            SeRelease releaseInfo = session.getRelease();
            int majVer = releaseInfo.getMajor();
            int minVer = releaseInfo.getMinor();

            if (majVer == 9 && minVer > 1 && JSDE_CLIENT_VERSION < JSDE_VERSION_91) {
                // we can't connect to ArcSDE 9.2 with the arcsde 9.0 jars. It
                // just won't
                // work when trying to draw maps. Oh well, at least we'll warn
                // people.
                LOGGER.severe("\n\n**************************\n"
                        + "DANGER DANGER DANGER!!!  You're using the ArcSDE 9.0 (or earlier) jars with "
                        + "ArcSDE "
                        + majVer
                        + "."
                        + minVer
                        + " on host '"
                        + config.getServerName()
                        + "' .  "
                        + "This PROBABLY WON'T WORK.  If you have issues "
                        + "or unexplained exceptions when rendering maps, upgrade your ArcSDE jars to version "
                        + "9.2 or higher.  See http://docs.codehaus.org/display/GEOTOOLS/ArcSDE+Plugin\n"
                        + "**************************\n\n");
            }

            // if a version was specified, verify it exists
            final String versionName = config.getVersion();
            if (versionName != null && !("".equals(versionName.trim()))) {
                session.issue(new Commands.GetVersionCommand(versionName));
            }
        } finally {
            session.dispose();
        }

        String namespaceUri = config.getNamespaceUri();
        if (namespaceUri != null && "".equals(namespaceUri.trim())) {
            namespaceUri = null;
        }
        String versionName = config.getVersion();
        if (versionName != null && "".equals(versionName.trim())) {
            versionName = null;
        }
        boolean allowNonSpatialTables = config.isAllowNonSpatialTables();
        /**
         * Multiple GeoDB Patch
         * 
         * check if directconnect string has a schema Multiple GeoDB Syntax: Oracle 10g user's
         * schema geodatabase <schema_name> is the name of the schema that owns the geodatabase.
         * sde:oracle10g:/:<schema_name>
         * 
         * Default single GeoDB Syntax: sde:oracle10g or in MultipleGeoDB Notation:
         * sde:oracle10g:/:sde
         * 
         * for further infos look at
         * http://webhelp.esri.com/arcgisserver/9.3/java/geodatabases/arcsde-2034353163.htm
         */
        final String port = config.getPortNumber();

        final String[] directConnectParts = port.split(":");
        final int length = directConnectParts.length;
        final int multiGeoDB = 4;

        if (length == multiGeoDB) {
            final int multiGeoDBIndex = port.lastIndexOf(":");
            final String user_schema = port.substring(multiGeoDBIndex + 1);

            versionName = user_schema + ".DEFAULT";

        }
        sdeDStore = new ArcSDEDataStore(connPool, namespaceUri, versionName, allowNonSpatialTables);
        return sdeDStore;
    }

    /**
     * Display name for this DataStore Factory
     * 
     * @return <code>"ArcSDE"</code>
     */
    public String getDisplayName() {
        return "ArcSDE";
    }

    /**
     * A human friendly name for this data source factory
     * 
     * @return this factory's description
     */
    public String getDescription() {
        return FACTORY_DESCRIPTION;
    }

    public boolean canProcess(Map<String, Serializable> params) {
        if (JSDE_CLIENT_VERSION == JSDE_VERSION_DUMMY) {
            return false;
        }
        boolean canProcess = true;

        try {
            new ArcSDEDataStoreConfig(params);
        } catch (NullPointerException ex) {
            canProcess = false;
        } catch (IllegalArgumentException ex) {
            canProcess = false;
        }

        return canProcess;
    }

    /**
     * Test to see if this datastore is available, if it has all the appropriate libraries to
     * construct a datastore.
     * 
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     */
    public boolean isAvailable() {
        if (JSDE_CLIENT_VERSION == JSDE_VERSION_DUMMY) {
            LOGGER.warning("You must download and install the *real* ArcSDE JSDE jar files. "
                    + "Currently the GeoTools ArcSDE 'dummy jar' is on your classpath. "
                    + "ArcSDE connectivity is DISABLED. "
                    + "See http://docs.codehaus.org/display/GEOTOOLS/ArcSDE+Plugin");
            return false;
        }
        try {
            LOGGER.finest(SeConnection.class.getName() + " is in place.");
            LOGGER.finest(PeCoordinateSystem.class.getName() + " is in place.");
        } catch (Throwable t) {
            return false;
        }

        return true;
    }

    /**
     * @see DataStoreFactorySpi#getParametersInfo()
     */
    public DataStoreFactorySpi.Param[] getParametersInfo() {
        return paramMetadata.toArray(new Param[paramMetadata.size()]);
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     * 
     * @see DataStoreFactorySpi#getImplementationHints()
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

    public static int getSdeClientVersion() {
        return JSDE_CLIENT_VERSION;
    }
}
