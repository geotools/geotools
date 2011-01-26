/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde;

import static org.geotools.arcsde.ArcSDEDataStoreFactory.ALLOW_NON_SPATIAL_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.NAMESPACE_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.VERSION_PARAM;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;

import org.geotools.arcsde.data.ArcSDEDataStore;
import org.geotools.arcsde.data.ArcSDEDataStoreConfig;
import org.geotools.arcsde.jndi.ArcSDEConnectionFactory;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.factory.GeoTools;
import org.geotools.util.logging.Logging;

/**
 * A GeoTools {@link DataStore} factory to access an ArcSDE database by grabbing the connection pool
 * from a JNDI reference.
 * <p>
 * This DataStore factory expects the arcsde connection information to be given as a JNDI resource
 * path through the {@link #JNDI_REFNAME jndiRefName} parameter at {@link #createDataStore(Map)}.
 * The resource provided by the JNDI context at that location may be either:
 * <ul>
 * <li>a {@code java.util.Map<String, String>} with the connection parameters from
 * {@link ArcSDEConnectionConfig}. If so, the {@link ISessionPool session pool} will be taken from
 * {@link ArcSDEConnectionFactory#getInstance(Map)}</li>
 * <li>a {@link ISessionPool} instance</li>
 * </ul>
 * </p>
 * <p>
 * If not an {@code ISessionPool}, the object will be used to get one from
 * {@link ArcSDEConnectionFactory}. Whether the resulting session (connection) pool is shared among
 * {@link ArcSDEDataStore} instances is dependent on how the JNDI resource is externally configured.
 * For example, on the J2EE container, it will depend on if the JNDI resource is globally configured
 * or not, and the required jar files are on a J2EE container shared libraries folder or not.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/ArcSDEJNDIDataStoreFactory.java $
 * @version $Id$
 * @since 2.5.7
 */
public class ArcSDEJNDIDataStoreFactory implements DataStoreFactorySpi {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde");

    private final ArcSDEDataStoreFactory delegateFactory;

    /**
     * JNDI context path name
     */
    public static final Param JNDI_REFNAME = new Param("ArcSDE_jndiReferenceName", String.class,
            "JNDI context path", true, "java:comp/env/geotools/arcsde");

    private static final String J2EE_ROOT_CONTEXT = "java:comp/env/";

    public ArcSDEJNDIDataStoreFactory() {
        this.delegateFactory = new ArcSDEDataStoreFactory();
    }

    /**
     * Creates and {@link ArcSDEDataStore} from the provided {@code params}, where the connection
     * pool is provided by JNDI.
     * <p>
     * See {@link #getParametersInfo()} to check which datastore creation parameters are expected by
     * this factory method.
     * </p>
     * 
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        final String jndiName = (String) JNDI_REFNAME.lookUp(params);

        final Object lookup = lookupJndiResource(jndiName);

        final ISessionPool sessionPool = getSessionPool(jndiName, lookup);

        final String nsUri = (String) NAMESPACE_PARAM.lookUp(params);
        final String version = (String) VERSION_PARAM.lookUp(params);
        final boolean nonSpatial;
        {
            final Boolean allowNonSpatialTables = (Boolean) ALLOW_NON_SPATIAL_PARAM.lookUp(params);
            nonSpatial = allowNonSpatialTables == null ? false : allowNonSpatialTables
                    .booleanValue();
        }

        final ArcSDEConnectionConfig connectionConfig = sessionPool.getConfig();
        final ArcSDEDataStoreConfig dsConfig;
        dsConfig = new ArcSDEDataStoreConfig(connectionConfig, nsUri, version, nonSpatial);

        LOGGER.info("Creating ArcSDE JNDI DataStore with shared session pool for " + dsConfig);
        final ArcSDEDataStore dataStore = delegateFactory.createDataStore(dsConfig, sessionPool);

        return dataStore;
    }

    @SuppressWarnings("unchecked")
    private ISessionPool getSessionPool(final String jndiName, final Object lookup)
            throws IOException, DataSourceException {

        final ISessionPool sessionPool;

        if (lookup instanceof ISessionPool) {

            LOGGER.info("Creating JNDI ArcSDE DataStore with shared session pool for " + lookup);
            sessionPool = (ISessionPool) lookup;

        } else if (lookup instanceof Map) {
            Map<String, Serializable> map = new HashMap<String, Serializable>();
            {
                Map<Object, Object> props = (Map<Object, Object>) lookup;
                String key;
                Object value;
                for (Map.Entry<Object, Object> e : props.entrySet()) {
                    key = String.valueOf(e.getKey());
                    value = e.getValue();
                    map.put(key, value == null ? null : String.valueOf(e.getValue()));
                }
            }
            // use the JNDI factory to grab the shared pool
            ArcSDEConnectionFactory factory = new ArcSDEConnectionFactory();
            sessionPool = factory.getInstance(map);

        } else {
            throw new DataSourceException("Unknown JNDI resource on path " + jndiName
                    + ". Expected one of [" + ArcSDEConnectionConfig.class.getName() + ", "
                    + ISessionPool.class.getName() + "] but got " + lookup.getClass().getName()
                    + " (" + lookup + ")");
        }
        return sessionPool;
    }

    /**
     * Looks up and returns the JNDI resource addressed by {@code jndiName}
     * 
     * @param jndiName
     * @return the resource mapped at {@code jndiName}, which shall be either a
     *         {@code java.util.Map<String, String>}, an {@link ArcSDEConnectionConfig} or a
     *         {@link ISessionPool}.
     * @throws IOException
     *             if a resource is not found at {@code jndiName}
     */
    private Object lookupJndiResource(final String jndiName) throws IOException {
        if (jndiName == null) {
            throw new IOException("Missing " + JNDI_REFNAME.description);
        }

        final Context ctx;

        try {
            ctx = GeoTools.getInitialContext(GeoTools.getDefaultHints());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        Object lookup = null;
        try {
            lookup = ctx.lookup(jndiName);
        } catch (NamingException e1) {
            // check if the user did not specify "java:comp/env"
            // and this code is running in a J2EE environment
            try {
                if (jndiName.startsWith(J2EE_ROOT_CONTEXT) == false) {
                    lookup = ctx.lookup(J2EE_ROOT_CONTEXT + jndiName);
                    // success --> issue a waring
                    LOGGER.warning("Using " + J2EE_ROOT_CONTEXT + jndiName + " instead of "
                            + jndiName + " would avoid an unnecessary JNDI lookup");
                }
            } catch (NamingException e2) {
                // do nothing, was only a try
            }
        }

        if (lookup == null) {
            throw new IOException("Cannot find JNDI data source: " + jndiName);
        }
        return lookup;
    }

    /**
     * Returns whether this factory <i>could</i> process the given parameters. That is, it does not
     * check the validity of the parameter, it only asserts the {@link #JNDI_REFNAME} parameter is
     * present. That is so so any failure is handled by {@link #createDataStore(Map)} instead of
     * getting client code silently failing (as this method does not throw an exception)
     * 
     * @see org.geotools.data.DataAccessFactory#canProcess(java.util.Map)
     */
    public boolean canProcess(Map<String, Serializable> params) {
        if (params == null) {
            return false;
        }
        String lookUpKey;
        try {
            lookUpKey = (String) JNDI_REFNAME.lookUp(params);
        } catch (IOException e) {
            return false;
        }
        if (lookUpKey == null) {
            return false;
        }
        return true;
    }

    /**
     * @see org.geotools.data.DataAccessFactory#getDescription()
     */
    public String getDescription() {
        return delegateFactory.getDescription() + " (JNDI)";
    }

    /**
     * @see org.geotools.data.DataAccessFactory#getDisplayName()
     */
    public String getDisplayName() {
        return delegateFactory.getDisplayName() + " (JNDI)";
    }

    /**
     * Provides the datastore creation parameter metadata for this factory.
     * <p>
     * The returned parameters are:
     * <ul>
     * <li>{@link #JNDI_REFNAME jndiReferenceName}: the JNDI path to the {@link ISessionPool
     * connection pool}
     * <li> {@link ArcSDEDataStoreConfig#NAMESPACE_PARAM_NAME namespace}: the namespace uri the
     * datastore should create feature types in
     * <li> {@link ArcSDEDataStoreConfig#VERSION_PARAM_NAME database.version} the arcsde database
     * version the datastore shall work upon. If non provided or empty, the DEFAULT version will be
     * used.
     * <li> {@link ArcSDEDataStoreConfig#ALLOW_NON_SPATIAL_TABLES_PARAM_NAME
     * datastore.allowNonSpatialTables} whether to publish non spatial registered tables (aka,
     * Object Classes). Defaults to {@code false}.
     * </ul>
     * </p>
     * 
     * @see org.geotools.data.DataAccessFactory#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] { JNDI_REFNAME, NAMESPACE_PARAM, VERSION_PARAM, ALLOW_NON_SPATIAL_PARAM };
    }

    /**
     * Determines if the datastore is available.
     * <p>
     * Check in an Initial Context is available, that is all what can be done Checking for the right
     * jdbc jars in the classpath is not possible here
     * </p>
     * 
     * @see org.geotools.data.DataAccessFactory#isAvailable()
     */
    public boolean isAvailable() {
        try {
            GeoTools.getInitialContext(GeoTools.getDefaultHints());
        } catch (NamingException e) {
            return false;
        }
        return delegateFactory.isAvailable();
    }

    /**
     * @see org.geotools.factory.Factory#getImplementationHints()
     */
    public Map<Key, ?> getImplementationHints() {
        return delegateFactory.getImplementationHints();
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException("ArcSDE PlugIn does not support createNewDataStore");
    }
}
