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
package org.geotools.arcsde.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsqlparser.statement.select.PlainSelect;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataSourceException;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeDefs;
import com.esri.sde.sdk.client.SeError;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeTable;

/**
 * Maintains a cache of {@link FeatureTypeInfo} objects for fast retrieval of ArcSDE vector layer
 * information and its corresponding geotools {@link FeatureType}.
 * <p>
 * {@link SeLayer} objects are not cached, they hold a reference to its connection and hence may
 * only be used inside the connection's context. Instead, a set of layer names is kept and the set
 * of actual {@link FeatureTypeInfo}s is lazily loaded on demand.
 * </p>
 * <p>
 * This class may set up a background process to periodically update the list of available layer
 * names in the server and clear the feature type cache. See the constructor's javadoc for more
 * info.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.6
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/data/FeatureTypeInfoCache.java $
 */
final class FeatureTypeInfoCache {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.data");

    /**
     * ArcSDE registered layers definitions
     */
    private final Map<String, FeatureTypeInfo> featureTypeInfos;

    /**
     * In process view definitions. This map is populated through
     * {@link #registerView(String, PlainSelect)}
     */
    private final Map<String, FeatureTypeInfo> inProcessFeatureTypeInfos;

    private final ISessionPool sessionPool;

    /**
     * list of available featureclasses in the database. Does not contain in-process view type
     * names. SeLayer objects are not cached because they hold a reference to its SeConnection and
     * hence need to be used only inside its connection context.
     */
    private final Set<String> availableLayerNames;

    /**
     * Namespace URI to construct FeatureTypes and AttributeTypes with
     */
    private final String namespace;

    /**
     * Scheduler for cache updating.
     */
    private ScheduledExecutorService cacheUpdateScheduler;

    /**
     * Lock for protecting featureTypeInfos cache.
     */
    private final ReentrantReadWriteLock cacheLock;

    private final boolean allowNonSpatialTables;

    private final long cacheUpdateFreqSecs;

    /**
     * Creates a FeatureTypeInfoCache
     * <p>
     * The provided {@link ISessionPool} is used to grab an {@link ISession} when the list of
     * available layers needs to be updated. This update happens at this class' construction time
     * and, optionally, every {@code cacheUpdateFreqSecs} seconds.
     * </p>
     * 
     * @param sessionPool
     * @param namespace
     *            the namespace {@link FeatureType}s are created with, may be {@code null}
     * @param cacheUpdateFreqSecs
     *            layer name cache update frequency, in seconds. {@code <= 0} means do never update.
     * @param allowNonSpatialTables
     *            whether non spatial table names are requested
     * @throws IOException
     */
    public FeatureTypeInfoCache(final ISessionPool sessionPool, final String namespace,
            final int cacheUpdateFreqSecs, boolean allowNonSpatialTables) throws IOException {

        availableLayerNames = new TreeSet<String>();
        featureTypeInfos = new HashMap<String, FeatureTypeInfo>();
        inProcessFeatureTypeInfos = new HashMap<String, FeatureTypeInfo>();
        this.sessionPool = sessionPool;
        this.allowNonSpatialTables = allowNonSpatialTables;
        this.namespace = namespace;
        this.cacheLock = new ReentrantReadWriteLock();
        this.cacheUpdateFreqSecs = cacheUpdateFreqSecs;

        reset();
    }

    public void reset() {
        dispose();

        CacheUpdater cacheUpdater = new CacheUpdater();
        // run now, populate table name cache and then register for periodic running
        cacheUpdater.run();
        if (cacheUpdateFreqSecs > 0) {
            cacheUpdateScheduler = Executors.newScheduledThreadPool(1);
            LOGGER.info("Scheduling the layer name cache to be updated every "
                    + this.cacheUpdateFreqSecs + " seconds.");
            cacheUpdateScheduler.scheduleWithFixedDelay(cacheUpdater, this.cacheUpdateFreqSecs,
                    this.cacheUpdateFreqSecs, TimeUnit.SECONDS);
        } else {
            cacheUpdateScheduler = null;
        }
    }

    public boolean isAllowNonSpatialTables() {
        return allowNonSpatialTables;
    }

    public void dispose() {
        if (cacheUpdateScheduler != null) {
            LOGGER.info("Shutting down cache update scheduler");
            cacheUpdateScheduler.shutdownNow();
        }
    }

    public void addInprocessViewInfo(final FeatureTypeInfo typeInfo) {
        inProcessFeatureTypeInfos.put(typeInfo.getFeatureTypeName(), typeInfo);
    }

    public String getNamesapceURI() {
        return namespace;
    }

    public List<String> getTypeNames() {
        cacheLock.readLock().lock();

        List<String> layerNames;
        try {
            layerNames = new ArrayList<String>(availableLayerNames);
        } finally {
            cacheLock.readLock().unlock();
        }
        layerNames.addAll(this.inProcessFeatureTypeInfos.keySet());
        Collections.sort(layerNames);
        return layerNames;
    }

    public List<Name> getNames() {
        final List<String> typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.size());
        for (String typeName : typeNames) {
            NameImpl name = namespace == null ? new NameImpl(typeName) : new NameImpl(namespace,
                    typeName);
            names.add(name);
        }
        return names;
    }

    /**
     * Check inProcessFeatureTypeInfos and featureTypeInfos for the provided typeName, checking the
     * ArcSDE server as a last resort.
     * 
     * @param typeName
     * @return
     */
    public FeatureTypeInfo getFeatureTypeInfo(final String typeName) throws IOException {
        FeatureTypeInfo typeInfo = getCachedTypeInfo(typeName);
        if (typeInfo != null) {
            return typeInfo;
        }

        ISession session;
        try {
            session = sessionPool.getSession(false);
        } catch (UnavailableConnectionException e) {
            throw new RuntimeException("Can't get type info for " + typeName
                    + ". Connection pool exhausted", e);
        }
        try {
            typeInfo = getFeatureTypeInfo(typeName, session);
        } finally {
            session.dispose();
        }
        return typeInfo;
    }

    /**
     * Used by feature reader and writer to get the schema information.
     * <p>
     * They are making use of this function because they already have their own Session to request
     * the ftInfo if needed.
     * </p>
     * 
     * @param typeName
     * @param session
     * @return
     * @throws IOException
     */
    public FeatureTypeInfo getFeatureTypeInfo(final String typeName, final ISession session)
            throws IOException {

        FeatureTypeInfo typeInfo = getCachedTypeInfo(typeName);
        if (typeInfo != null) {
            return typeInfo;
        }

        cacheLock.writeLock().lock();
        // Recheck so it hasn't been done already.
        try {
            typeInfo = featureTypeInfos.get(typeName);
            if (typeInfo == null) {
                typeInfo = ArcSDEAdapter.fetchSchema(typeName, this.namespace, session);
                featureTypeInfos.put(typeName, typeInfo);
            }
        } finally {
            cacheLock.writeLock().unlock();
        }
        return typeInfo;
    }

    /**
     * @param typeName
     * @return the cached type info if there's one for typeName, {@code null} otherwise
     * @throws DataSourceException
     */
    private FeatureTypeInfo getCachedTypeInfo(final String typeName) throws DataSourceException {
        FeatureTypeInfo typeInfo = inProcessFeatureTypeInfos.get(typeName);
        if (typeInfo != null) {
            return typeInfo;
        }

        // Check if this is a known featureType
        cacheLock.readLock().lock();
        try {
            if (!availableLayerNames.contains(typeName)) {
                throw new DataSourceException(typeName + " does not exist");
            }
            typeInfo = featureTypeInfos.get(typeName);
        } finally {
            cacheLock.readLock().unlock();
        }

        return typeInfo;
    }

    private final class CacheUpdater implements Runnable {

        public void run() {
            LOGGER.finer("FeatureTypeCache background process running...");

            List<String> typeNames;
            try {
                typeNames = fetchRegistrations();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Updating TypeNameCache failed.", e);
                return;
            }

            final Set<String> removed;
            {// just some logging..
                cacheLock.readLock().lock();
                Set<String> added = new TreeSet<String>(typeNames);
                added.removeAll(availableLayerNames);
                if (added.size() > 0) {
                    LOGGER.finest("FeatureTypeCache: added the following layers: " + added);
                }
                removed = new TreeSet<String>(availableLayerNames);
                removed.removeAll(typeNames);
                if (removed.size() > 0) {
                    LOGGER.finest("FeatureTypeCache: the following layers are no "
                            + "longer available: " + removed);
                }
                cacheLock.readLock().unlock();
            }
            cacheLock.writeLock().lock();

            availableLayerNames.clear();
            availableLayerNames.addAll(typeNames);

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("FeatureTypeCache: updated server layer list: " + typeNames);
            }

            // discard any removed feature type
            for (String typeName : removed) {
                LOGGER.fine("Removing FeatureTypeInfo for layer " + typeName
                        + " since it does no longer exist on the database");
                featureTypeInfos.remove(typeName);
            }

            LOGGER.finer("Finished updated type name cache");
            cacheLock.writeLock().unlock();
        }

        private List<String> fetchRegistrations() throws Exception {
            final List<String> typeNames;
            final ISession session = sessionPool.getSession(false);
            try {
                typeNames = session.issue(new FetchRegistrationsCommand(allowNonSpatialTables));
            } finally {
                session.dispose();
            }
            return typeNames;
        }
    }

    private static final class FetchRegistrationsCommand extends Command<List<String>> {

        private final boolean allowNonSpatialTables;

        public FetchRegistrationsCommand(boolean allowNonSpatialTables) {
            this.allowNonSpatialTables = allowNonSpatialTables;
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<String> execute(ISession session, SeConnection connection) throws SeException,
                IOException {
            /*
             * Note we could do almost the same by calling
             * connection.getRegisteredTables():Vector<SeRegistration> but SeRegistration does not
             * have a getQualifiedName() method so I fear we can loose ability to serve feature
             * types from different users. So first getting the list of all the tables with select
             * privilege and then checking if it's registered...
             */
            final List<SeTable> registeredTables = connection.getTables(SeDefs.SE_SELECT_PRIVILEGE);

            /*
             * Get the list of raster tables so they're ignored as feature types
             */
            final List<String> rasterColumns = session.getRasterColumns();

            final List<String> typeNames = new ArrayList<String>(registeredTables.size());
            for (SeTable table : registeredTables) {
                String tableName = table.getQualifiedName().toUpperCase();
                SeRegistration reg;
                try {
                    reg = new SeRegistration(connection, tableName);
                    // do not call getInfo or it failst with tables owned by other user than the
                    // connection one
                    // reg.getInfo();
                } catch (SeException e) {
                    if (e.getSeError().getSdeError() == SeError.SE_TABLE_NOREGISTERED) {
                        LOGGER.finest("Ignoring non registered table " + tableName);
                        continue;
                    }
                    throw e;
                }

                boolean isSystemTable = reg.getRowIdAllocation() == SeRegistration.SE_REGISTRATION_ROW_ID_ALLOCATION_SINGLE;
                if (isSystemTable) {
                    LOGGER.finer("Ignoring ArcSDE registered table " + tableName
                            + " as it is a system table");
                    continue;
                }

                if (reg.isHidden()) {
                    LOGGER.finer("Ignoring ArcSDE registered table " + tableName
                            + " as it is hidden");
                    continue;
                }
                boolean hasLayer = reg.hasLayer();

                if (!hasLayer) {
                    if (!allowNonSpatialTables) {
                        LOGGER.finer("Ignoring ArcSDE registered table " + tableName
                                + " as it is non spatial");
                        continue;
                    }
                    if (reg.getRowIdColumnType() == SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE) {
                        LOGGER.finer("Ignoring ArcSDE registered table " + tableName
                                + " as it has no row id column");
                        continue;
                    }

                }

                if (!rasterColumns.contains(tableName)) {
                    typeNames.add(tableName);
                }
            }

            return typeNames;
        }
    }

}
