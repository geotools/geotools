/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Repository;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.postgis.PostgisDatastoreWrapper;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * This class simply builds an index for fast indexed queries.
 *
 * <p>TODO: we may consider converting {@link CoverageSlice}s to {@link SimpleFeature}s
 */
public class CoverageSlicesCatalog {

    /**
     * CoverageSlicesCatalog always used an hidden H2 DB to store granules index related to a
     * specific file.
     *
     * <p>Starting from 14.x it also can be setup on top of a shared PostGIS datastore.
     *
     * <p>Using a PostGIS shared index, we need to add a LOCATION attribute to distinguish the
     * different granules, as well as add a Filter setting the LOCATION value to each query from a
     * reader (1 reader <-> 1 file <-> 1 location)
     */
    public static class WrappedCoverageSlicesCatalog extends CoverageSlicesCatalog {

        private static final FilterFactory FF = FeatureUtilities.DEFAULT_FILTER_FACTORY;

        /** Internal query filter to be ANDED with the input query */
        private Filter queryFilter;

        public WrappedCoverageSlicesCatalog(
                DataStoreConfiguration config, File file, Repository repository)
                throws IOException {
            super(config, repository);
            queryFilter =
                    FF.equal(
                            FF.property(CoverageSlice.Attributes.LOCATION),
                            FF.literal(file.getCanonicalPath()),
                            true);
        }

        @Override
        public List<CoverageSlice> getGranules(Query q) throws IOException {
            return super.getGranules(refineQuery(q));
        }

        @Override
        public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
            super.computeAggregateFunction(refineQuery(query), function);
        }

        @Override
        public void removeGranules(String typeName, Filter filter, Transaction transaction)
                throws IOException {
            super.removeGranules(typeName, refineFilter(filter), transaction);
        }

        /** Refine query to make sure to restrict the query to the single file associated. */
        private Query refineQuery(Query q) {
            Query query = new Query(q);
            query.setFilter(refineFilter(q.getFilter()));
            return query;
        }

        /** Refine filter to make sure to AND the filter with a filter selecting the proper file */
        private Filter refineFilter(Filter filter) {
            return filter != null ? FF.and(filter, queryFilter) : queryFilter;
        }
    }

    /** Logger. */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(CoverageSlicesCatalog.class);

    /** The slices index store */
    private DataStore slicesIndexStore;

    /** The feature type name */
    private Set<String> typeNames = new HashSet<String>();

    public static final String IMAGE_INDEX_ATTR = "imageindex";

    private static final String HIDDEN_FOLDER = ".mapping";

    private final SoftValueHashMap<Integer, CoverageSlice> coverageSliceDescriptorsCache =
            new SoftValueHashMap<Integer, CoverageSlice>(0);

    private boolean repositoryStore;

    public CoverageSlicesCatalog(final String database, final File parentLocation) {
        this(database, parentLocation, null);
    }

    public CoverageSlicesCatalog(
            final String database, final File parentLocation, Repository repository) {
        this(
                new DataStoreConfiguration(
                        DataStoreConfiguration.getDefaultParams(database, parentLocation)),
                repository);
    }

    public CoverageSlicesCatalog(DataStoreConfiguration datastoreConfig) {
        this(datastoreConfig, null);
    }

    public CoverageSlicesCatalog(DataStoreConfiguration datastoreConfig, Repository repository) {
        DataStoreFactorySpi spi = datastoreConfig.getDatastoreSpi();
        String storeName = datastoreConfig.getStoreName();
        boolean useRepository = storeName != null && repository != null;
        try {
            // creating a store, this might imply creating it for an existing underlying store or
            // creating a brand new one
            boolean isPostgis = Utils.isPostgisStore(spi);
            boolean isH2 = Utils.isH2Store(spi);

            Map<String, Serializable> params = datastoreConfig.getParams();
            if (isPostgis && params != null) {
                Utils.fixPostgisDBCreationParams(params);
            }
            if (useRepository) {
                Name name = new NameImpl(storeName);
                slicesIndexStore = repository.dataStore(name);
                if (slicesIndexStore == null && storeName.indexOf(':') > -1) {
                    int idx = storeName.lastIndexOf(':');
                    name = new NameImpl(storeName.substring(0, idx), storeName.substring(idx + 1));
                    slicesIndexStore = repository.dataStore(name);
                }

                if (slicesIndexStore == null) {
                    throw new IllegalArgumentException("Could not locate store named " + storeName);
                }
                this.repositoryStore = true;
            } else {
                if (!(isH2 || isPostgis)) {
                    throw new IllegalArgumentException(
                            "Low level index for multidim granules only supports"
                                    + " H2 and PostGIS databases");
                }
                Utilities.ensureNonNull("params", params);

                slicesIndexStore = spi.createDataStore(params);
            }
            boolean wrapDatastore = false;
            String parentLocation = (String) params.get(Utils.Prop.PARENT_LOCATION);
            if (params.containsKey(Utils.Prop.WRAP_STORE)) {
                wrapDatastore = (Boolean) params.get(Utils.Prop.WRAP_STORE);
            }
            if (isPostgis && wrapDatastore) {
                slicesIndexStore =
                        new PostgisDatastoreWrapper(
                                slicesIndexStore, parentLocation, HIDDEN_FOLDER);
            }

            String typeName = null;
            String[] typeNamesValues = null;
            boolean scanForTypeNames = false;

            // Handle multiple typeNames
            if (params.containsKey(Utils.Prop.TYPENAME)) {
                typeName = (String) params.get(Utils.Prop.TYPENAME);
                if (typeName != null && typeName.contains(",")) {
                    typeNamesValues = typeName.split(",");
                }
            }

            if (params.containsKey(Utils.SCAN_FOR_TYPENAMES)) {
                scanForTypeNames = Boolean.valueOf((String) (params.get(Utils.SCAN_FOR_TYPENAMES)));
            }
            if (typeNamesValues == null && scanForTypeNames) {
                typeNamesValues = slicesIndexStore.getTypeNames();
            }

            if (typeNamesValues != null) {
                for (String tn : typeNamesValues) {
                    this.typeNames.add(tn);
                }
            } else if (typeName != null) {
                addTypeName(typeName, false);
            }

            if (this.typeNames.size() > 0) {
                extractBasicProperties(typeNames.iterator().next());
            } else {
                extractBasicProperties(typeName);
            }
        } catch (Throwable e) {
            try {
                if (slicesIndexStore != null) {
                    slicesIndexStore.dispose();
                }
            } catch (Throwable e1) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, e1.getLocalizedMessage(), e1);
                }
            } finally {
                slicesIndexStore = null;
            }

            throw new IllegalArgumentException(e);
        }
    }

    /**
     * If the underlying store has been disposed we throw an {@link IllegalStateException}.
     *
     * <p>We need to arrive here with at least a read lock!
     *
     * @throws IllegalStateException in case the underlying store has been disposed.
     */
    private void checkStore() throws IllegalStateException {
        if (slicesIndexStore == null) {
            throw new IllegalStateException("The index store has been disposed already.");
        }
    }

    private void extractBasicProperties(String typeName) throws IOException {

        if (typeName == null) {
            final String[] typeNames = slicesIndexStore.getTypeNames();
            if (typeNames == null || typeNames.length <= 0) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "BBOXFilterExtractor::extractBasicProperties(): Problems when opening the index,"
                                    + " no typenames for the schema are defined");
                }
                return;
            }
            if (typeName == null) {
                typeName = typeNames[0];
                addTypeName(typeName, false);
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.warning(
                            "BBOXFilterExtractor::extractBasicProperties(): passed typename is null, using: "
                                    + typeName);
            }

            // loading all the features into memory to build an in-memory index.
            for (String type : typeNames) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(
                            "BBOXFilterExtractor::extractBasicProperties(): Looking for type \'"
                                    + typeName
                                    + "\' in DataStore:getTypeNames(). Testing: \'"
                                    + type
                                    + "\'.");
                if (type.equalsIgnoreCase(typeName)) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine(
                                "BBOXFilterExtractor::extractBasicProperties(): SUCCESS -> type \'"
                                        + typeName
                                        + "\' is equalsIgnoreCase() to \'"
                                        + type
                                        + "\'.");
                    typeName = type;
                    addTypeName(typeName, false);
                    break;
                }
            }
        }
    }

    private void addTypeName(String typeName, final boolean check) {
        if (check && this.typeNames.contains(typeName)) {
            throw new IllegalArgumentException("This typeName already exists: " + typeName);
        }
        this.typeNames.add(typeName);
    }

    public String[] getTypeNames() {
        if (this.typeNames != null && !this.typeNames.isEmpty()) {
            return (String[]) this.typeNames.toArray(new String[] {});
        }
        return null;
    }

    public boolean hasTypeName(String typeName) {
        return typeNames != null && typeNames.contains(typeName);
    }

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();
            try {
                if (slicesIndexStore != null && !repositoryStore) slicesIndexStore.dispose();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            } finally {
                slicesIndexStore = null;
            }
        } finally {
            l.unlock();
        }
    }

    public void addGranule(
            final String typeName, final SimpleFeature granule, final Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("granule", granule);
        Utilities.ensureNonNull("transaction", transaction);
        final DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(granule);
        addGranules(typeName, collection, transaction);
    }

    public void addGranules(
            final String typeName,
            final SimpleFeatureCollection granules,
            final Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("granuleMetadata", granules);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();

            final SimpleFeatureStore store =
                    (SimpleFeatureStore) slicesIndexStore.getFeatureSource(typeName);
            store.setTransaction(transaction);
            store.addFeatures(granules);

        } finally {
            lock.unlock();
        }
    }

    public List<CoverageSlice> getGranules(final Query q) throws IOException {
        Utilities.ensureNonNull("query", q);
        final List<CoverageSlice> returnValue = new ArrayList<CoverageSlice>();
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            final String typeName = q.getTypeName();

            //
            // Load tiles informations, especially the bounds, which will be reused
            //
            final SimpleFeatureSource featureSource = slicesIndexStore.getFeatureSource(typeName);
            if (featureSource == null) {
                throw new NullPointerException(
                        "The provided SimpleFeatureSource is null, it's impossible to create an index!");
            }
            Transaction tx = null;
            SimpleFeatureIterator it = null;
            try {

                // Transform feature stores will use an autoCommit transaction which doesn't
                // have any state. Getting the features iterator may throw an exception
                // by interpreting a null state as a closed transaction. Therefore
                // we use a DefaultTransaction instance when dealing with stores.
                if (featureSource instanceof FeatureStore) {
                    tx = new DefaultTransaction("getGranulesTransaction" + System.nanoTime());
                    ((FeatureStore) featureSource).setTransaction(tx);
                }
                String[] requestedProperties = q.getPropertyNames();
                boolean postRetypeRequired = requestedProperties != Query.ALL_NAMES;
                SimpleFeatureType target = null;
                if (postRetypeRequired) {
                    List<String> propertiesList =
                            new ArrayList<>(Arrays.asList(requestedProperties));
                    if (!propertiesList.contains(IMAGE_INDEX_ATTR)) {
                        // IMAGE_INDEX_ATTRIBUTE is mandatory for coverage slices descriptor
                        // caching.
                        // add that the property
                        String[] properties = new String[requestedProperties.length + 1];
                        System.arraycopy(
                                requestedProperties, 0, properties, 0, requestedProperties.length);
                        properties[requestedProperties.length] = IMAGE_INDEX_ATTR;
                        q.setPropertyNames(properties);
                    }

                    // prepare target FeatureType
                    target =
                            SimpleFeatureTypeBuilder.retype(
                                    featureSource.getSchema(), requestedProperties);
                }

                final SimpleFeatureCollection features = featureSource.getFeatures(q);
                if (features == null) {
                    throw new NullPointerException(
                            "The provided SimpleFeatureCollection is null, it's impossible to create an index!");
                }

                // load the feature from the underlying datastore as needed
                it = features.features();
                if (it == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "The provided SimpleFeatureCollection returned a null iterator, it's impossible to create an index!");
                    }
                    return Collections.emptyList();
                }
                if (!it.hasNext()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "The provided SimpleFeatureCollection is empty, it's impossible to create an index!");
                    }
                    return Collections.emptyList();
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Index Loaded");
                }

                // getting the features
                while (it.hasNext()) {
                    SimpleFeature feature = it.next();
                    final SimpleFeature sf = (SimpleFeature) feature;
                    final CoverageSlice slice;

                    // caching by granule's index
                    synchronized (coverageSliceDescriptorsCache) {
                        Integer granuleIndex = (Integer) sf.getAttribute(IMAGE_INDEX_ATTR);
                        if (coverageSliceDescriptorsCache.containsKey(granuleIndex)) {
                            slice = coverageSliceDescriptorsCache.get(granuleIndex);
                        } else {
                            // create the granule coverageDescriptor (eventually retyping its
                            // feature)
                            slice =
                                    new CoverageSlice(
                                            postRetypeRequired
                                                    ? SimpleFeatureBuilder.retype(sf, target)
                                                    : sf);
                            coverageSliceDescriptorsCache.put(granuleIndex, slice);
                        }
                    }
                    returnValue.add(slice);
                }
            } finally {
                if (it != null) {
                    it.close();
                }

                if (tx != null) {
                    tx.close();
                }
            }
            // return
            return returnValue;
        } catch (Throwable e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        } finally {
            lock.unlock();
        }
    }

    public ReferencedEnvelope getBounds(final String typeName) {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            return this.slicesIndexStore.getFeatureSource(typeName).getBounds();

        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        Utilities.ensureNonNull("featureType", featureType);
        final Lock lock = rwLock.writeLock();
        String typeName = null;
        try {
            lock.lock();
            checkStore();
            SimpleFeatureType existing = null;

            // Add existence checks
            Name name = featureType.getName();
            if (this instanceof WrappedCoverageSlicesCatalog) {
                try {
                    // Check the store doesn't already exists
                    existing = slicesIndexStore.getSchema(name);
                } catch (IOException ioe) {

                    // Logs existence check at finer level
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer(ioe.getLocalizedMessage());
                    }
                }
            }
            if (existing == null) {
                slicesIndexStore.createSchema(featureType);
            } else {

                // Logs existence check at finer level
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("schema " + name + " already exists");
                }
            }
            typeName = featureType.getTypeName();
            if (typeName != null) {
                addTypeName(typeName, true);
            }
            extractBasicProperties(typeName);
        } finally {
            lock.unlock();
        }
    }

    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        Utilities.ensureNonNull("typeSpec", typeSpec);
        Utilities.ensureNonNull("identification", identification);
        final SimpleFeatureType featureType = DataUtilities.createType(identification, typeSpec);
        createType(featureType);
    }

    public SimpleFeatureType getSchema(final String typeName) throws IOException {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            if (typeName == null) {
                return null;
            }
            return slicesIndexStore.getSchema(typeName);
        } finally {
            lock.unlock();
        }
    }

    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            SimpleFeatureSource fs = slicesIndexStore.getFeatureSource(query.getTypeName());

            if (fs instanceof ContentFeatureSource)
                ((ContentFeatureSource) fs).accepts(query, function, null);
            else {
                final SimpleFeatureCollection collection = fs.getFeatures(query);
                collection.accepts(function, null);
            }
        } finally {
            lock.unlock();
        }
    }

    public QueryCapabilities getQueryCapabilities(final String typeName) {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            return slicesIndexStore.getFeatureSource(typeName).getQueryCapabilities();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to collect QueryCapabilities", e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        super.finalize();

        // warn people
        if (this.slicesIndexStore != null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "This granule catalog was not properly dispose as it still points to:"
                                + slicesIndexStore.getInfo().toString());
            }
            // try to dispose the underlying store if it has not been disposed yet
            this.dispose();
        }
    }

    public void removeGranules(String typeName, Filter filter, Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("filter", filter);
        Utilities.ensureNonNull("transaction", transaction);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();

            final SimpleFeatureStore store =
                    (SimpleFeatureStore) slicesIndexStore.getFeatureSource(typeName);
            store.setTransaction(transaction);
            store.removeFeatures(filter);

        } finally {
            lock.unlock();
        }
    }

    public void purge(Filter filter) throws IOException {
        DefaultTransaction transaction = null;
        try {
            transaction = new DefaultTransaction("CleanupTransaction" + System.nanoTime());
            for (String typeName : typeNames) {
                removeGranules(typeName, filter, transaction);
            }
            transaction.commit();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rollback");
            }
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException(e);
        } finally {
            try {
                if (transaction != null) {
                    transaction.close();
                }
            } catch (Throwable t) {

            }
        }
    }
}
