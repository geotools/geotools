/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.oracle.OracleDatastoreWrapper;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DefaultProgressListener;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

/**
 * This class simply builds an SRTREE spatial index in memory for fast indexed geometric queries.
 * 
 * <p>
 * Since the {@link ImageMosaicReader} heavily uses spatial queries to find out which are the involved tiles during mosaic creation, it is better to
 * do some caching and keep the index in memory as much as possible, hence we came up with this index.
 * 
 * @author Simone Giannecchini, S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.5
 * 
 * @source $URL$
 */
class GTDataStoreGranuleCatalog extends GranuleCatalog {

    /** Logger. */
    final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GTDataStoreGranuleCatalog.class);

    final static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools
            .getDefaultHints());

    private DataStore tileIndexStore;

    Set<String> typeNames = new HashSet<String>();

    private String geometryPropertyName;

    private Map<String, ReferencedEnvelope> bounds = new ConcurrentHashMap<String, ReferencedEnvelope>();

    PathType pathType;

    String locationAttribute;

    ImageReaderSpi suggestedRasterSPI;

    String parentLocation;

    boolean heterogeneous;

    public GTDataStoreGranuleCatalog(final Map<String, Serializable> params, final boolean create,
            final DataStoreFactorySpi spi, final Hints hints) {
        super(hints);
        Utilities.ensureNonNull("params", params);
        Utilities.ensureNonNull("spi", spi);

        try {
            this.pathType = (PathType) params.get("PathType");
            this.locationAttribute = (String) params.get("LocationAttribute");
            final String temp = (String) params.get("SuggestedSPI");
            this.suggestedRasterSPI = temp != null ? (ImageReaderSpi) Class.forName(temp)
                    .newInstance() : null;
            this.parentLocation = (String) params.get("ParentLocation");
            Object heterogen = params.get("Heterogeneous");
            if (heterogen != null) {
                this.heterogeneous = ((Boolean) heterogen).booleanValue();
            }

            // H2 workadound
            if (Utils.isH2Store(spi)) {
                Utils.fixH2DatabaseLocation(params, parentLocation);
            }

            // creating a store, this might imply creating it for an existing underlying store or
            // creating a brand new one
            if (!create)
                tileIndexStore = spi.createDataStore(params);
            else {
                // this works only with the shapefile datastore, not with the others
                // therefore I try to catch the error to try and use themethdo without *New*
                try {
                    tileIndexStore = spi.createNewDataStore(params);
                } catch (UnsupportedOperationException e) {
                    tileIndexStore = spi.createDataStore(params);
                }
            }

            if(Utils.isOracleStore(spi)) {
                tileIndexStore = new OracleDatastoreWrapper(tileIndexStore,
                        FilenameUtils.getFullPath(parentLocation));
            }

            // is this a new store? If so we do not set any properties
            if (create) {
                return;
            }

            String typeName = null;
            boolean scanForTypeNames = false;

            if (params.containsKey("TypeName")) {
                typeName = (String) params.get("TypeName");
            }

            if (params.containsKey(Utils.SCAN_FOR_TYPENAMES)) {
                scanForTypeNames = (Boolean) params.get(Utils.SCAN_FOR_TYPENAMES);
            }

            // if this is not a new store let's extract basic properties from it
            if (scanForTypeNames) {
                String[] typeNames = tileIndexStore.getTypeNames();
                if (typeNames != null) {
                    for (String tn : typeNames) {
                        this.typeNames.add(tn);
                    }
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
                if (tileIndexStore != null)
                    tileIndexStore.dispose();
            } catch (Throwable e1) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e1.getLocalizedMessage(), e1);
            } finally {
                tileIndexStore = null;
            }

            throw new IllegalArgumentException(e);
        }

    }

    /**
     * If the underlying store has been disposed we throw an {@link IllegalStateException}.
     * <p>
     * We need to arrive here with at least a read lock!
     * 
     * @throws IllegalStateException in case the underlying store has been disposed.
     */
    private void checkStore() throws IllegalStateException {
        if (tileIndexStore == null) {
            throw new IllegalStateException("The index store has been disposed already.");
        }
    }

    private void extractBasicProperties(String typeName) throws IOException {
        if (typeName != null && typeName.contains(",")) {
            String[] typeNames = typeName.split(",");
            for (String tn : typeNames) {
                extractBasicProperties(tn);
            }
        } else {
            if (typeName == null) {
                final String[] typeNames = tileIndexStore.getTypeNames();
                if (typeNames == null || typeNames.length <= 0)
                    throw new IllegalArgumentException(
                            "BBOXFilterExtractor::extractBasicProperties(): Problems when opening the index,"
                                    + " no typenames for the schema are defined");

                if (typeName == null) {
                    typeName = typeNames[0];
                    addTypeName(typeName, false);
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.warning("BBOXFilterExtractor::extractBasicProperties(): passed typename is null, using: "
                                + typeName);
                }

                // loading all the features into memory to build an in-memory index.
                for (String type : typeNames) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): Looking for type \'"
                                + typeName
                                + "\' in DataStore:getTypeNames(). Testing: \'"
                                + type
                                + "\'.");
                    if (type.equalsIgnoreCase(typeName)) {
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): SUCCESS -> type \'"
                                    + typeName + "\' is equalsIgnoreCase() to \'" + type + "\'.");
                        typeName = type;
                        addTypeName(typeName, false);
                        break;
                    }
                }
            }

            final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(typeName);
            if (featureSource == null) {
                throw new IOException(
                        "BBOXFilterExtractor::extractBasicProperties(): unable to get a featureSource for the qualified name"
                                + typeName);
            }

            final FeatureType schema = featureSource.getSchema();
            if (schema != null) {
                geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): geometryPropertyName is set to \'"
                            + geometryPropertyName + "\'.");

            } else {
                throw new IOException(
                        "BBOXFilterExtractor::extractBasicProperties(): unable to get a schema from the featureSource");
            }
        }
    }

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();
            try {
                if (tileIndexStore != null)
                    tileIndexStore.dispose();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            } finally {
                tileIndexStore = null;
            }

        } finally {
            l.unlock();
        }
    }

    @Override
    public int removeGranules(Query query) {
        Utilities.ensureNonNull("query", query);
        query = mergeHints(query);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();
            String typeName = query.getTypeName();
            SimpleFeatureStore fs = null;
            try {
                // create a writer that appends this features
                fs = (SimpleFeatureStore) tileIndexStore.getFeatureSource(typeName);
                final int retVal = fs.getCount(query);
                fs.removeFeatures(query.getFilter());

                // update bounds
                bounds.put(typeName, tileIndexStore.getFeatureSource(typeName).getBounds());

                return retVal;

            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                return -1;
            }
            // do your thing
        } finally {
            lock.unlock();

        }
    }

    @Override
    public void addGranules(final String typeName, final Collection<SimpleFeature> granules,
            final Transaction transaction) throws IOException {
        Utilities.ensureNonNull("granuleMetadata", granules);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();

            SimpleFeatureStore store = (SimpleFeatureStore) tileIndexStore
                    .getFeatureSource(typeName);
            store.setTransaction(transaction);

            ListFeatureCollection featureCollection = new ListFeatureCollection(
                    tileIndexStore.getSchema(typeName));

            // add them all
            Set<FeatureId> fids = new HashSet<FeatureId>();
            for (SimpleFeature f : granules) {
                // Add the feature to the feature collection
                featureCollection.add(f);
                fids.add(ff.featureId(f.getID()));
            }
            store.addFeatures(featureCollection);

            // update bounds
            if (bounds.containsKey(typeName)) {
                bounds.remove(typeName);
            }

        } finally {
            lock.unlock();

        }
    }

    @Override
    public void getGranuleDescriptors(Query query, final GranuleCatalogVisitor visitor)
            throws IOException {
        Utilities.ensureNonNull("query", query);
        final Query q = mergeHints(query);
        String typeName = q.getTypeName();
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(typeName);
            if (featureSource == null) {
                throw new NullPointerException(
                        "The provided SimpleFeatureSource is null, it's impossible to create an index!");
            }

            final SimpleFeatureCollection features = featureSource.getFeatures(q);
            if (features == null)
                throw new NullPointerException(
                        "The provided SimpleFeatureCollection is null, it's impossible to create an index!");

            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Index Loaded");

            // visiting the features from the underlying store
            final DefaultProgressListener listener = new DefaultProgressListener();
            features.accepts(new AbstractFeatureVisitor() {
                public void visit(Feature feature) {
                    if (feature instanceof SimpleFeature) {
                        // get the feature
                        final SimpleFeature sf = (SimpleFeature) feature;
                        final GranuleDescriptor granule = new GranuleDescriptor(sf,
                                suggestedRasterSPI, pathType, locationAttribute, parentLocation,
                                heterogeneous, q.getHints());

                        visitor.visit(granule, null);

                        // check if something bad occurred
                        if (listener.isCanceled() || listener.hasExceptions()) {
                            if (listener.hasExceptions()) {
                                throw new RuntimeException(listener.getExceptions().peek());
                            } else {
                                throw new IllegalStateException("Feature visitor for query " + q
                                        + " has been canceled");
                            }
                        }
                    }
                }
            }, listener);

        } catch (Throwable e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        } finally {
            lock.unlock();

        }
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        Utilities.ensureNonNull("query", q);
        q = mergeHints(q);
        String typeName = q.getTypeName();
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(typeName);
            if (featureSource == null) {
                throw new NullPointerException(
                        "The provided SimpleFeatureSource is null, it's impossible to create an index!");
            }
            return featureSource.getFeatures(q);

        } catch (Throwable e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        } finally {
            lock.unlock();

        }
    }

    @Override
    public BoundingBox getBounds(final String typeName) {
        final Lock lock = rwLock.readLock();
        ReferencedEnvelope bound = null;
        try {
            lock.lock();
            checkStore();
            if (bounds.containsKey(typeName)) {
                bound = bounds.get(typeName);
            } else {
                bound = this.tileIndexStore.getFeatureSource(typeName).getBounds();
                bounds.put(typeName, bound);
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            bounds.remove(typeName);
        } finally {
            lock.unlock();
        }

        // return bounds;
        return bound;
    }

    public void createType(String namespace, String typeName, String typeSpec) throws IOException,
            SchemaException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("typeSpec", typeSpec);
        final Lock lock = rwLock.writeLock();
        String type = null;
        try {
            lock.lock();
            checkStore();

            final SimpleFeatureType featureType = DataUtilities.createType(namespace, typeName,
                    typeSpec);
            tileIndexStore.createSchema(featureType);
            type = featureType.getTypeName();
            if (typeName != null) {
                addTypeName(typeName, true);
            }
            extractBasicProperties(type);
        } finally {
            lock.unlock();
        }

    }

    private void addTypeName(String typeName, final boolean check) {
        if (check && this.typeNames.contains(typeName)) {
            throw new IllegalArgumentException("This typeName already exists: " + typeName);
        }
        this.typeNames.add(typeName);
    }

    @Override
    public String[] getTypeNames() {
        if (this.typeNames != null && !this.typeNames.isEmpty()) {
            return (String[]) this.typeNames.toArray(new String[] {});
        }
        return null;
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        Utilities.ensureNonNull("featureType", featureType);
        final Lock lock = rwLock.writeLock();
        String typeName = null;
        try {
            lock.lock();
            checkStore();

            tileIndexStore.createSchema(featureType);
            typeName = featureType.getTypeName();
            if (typeName != null) {
                addTypeName(typeName, true);
            }
            extractBasicProperties(typeName);
        } finally {
            lock.unlock();
        }

    }

    public void createType(String identification, String typeSpec) throws SchemaException,
            IOException {
        Utilities.ensureNonNull("typeSpec", typeSpec);
        Utilities.ensureNonNull("identification", identification);
        final Lock lock = rwLock.writeLock();
        String typeName = null;
        try {
            lock.lock();
            checkStore();
            final SimpleFeatureType featureType = DataUtilities
                    .createType(identification, typeSpec);
            tileIndexStore.createSchema(featureType);
            typeName = featureType.getTypeName();
            if (typeName != null) {
                addTypeName(typeName, true);
            }
            extractBasicProperties(typeName);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            if (this.typeNames.isEmpty() || !this.typeNames.contains(typeName)) {
                return null;
            }
            return tileIndexStore.getSchema(typeName);
        } finally {
            lock.unlock();
        }

    }

    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
        query = mergeHints(query);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            SimpleFeatureSource fs = tileIndexStore.getFeatureSource(query.getTypeName());

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

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            return tileIndexStore.getFeatureSource(typeName).getQueryCapabilities();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Unable to collect QueryCapabilities", e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        // warn people
        if (this.tileIndexStore != null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("This granule catalog was not properly dispose as it still points to:"
                        + tileIndexStore.getInfo().toString());
            }
            // try to dispose the underlying store if it has not been disposed yet
            this.dispose();
        }

    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        Utilities.ensureNonNull("query", q);
        q = mergeHints(q);
        String typeName = q.getTypeName();
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            //
            // Load tiles informations, especially the bounds, which will be
            // reused
            //
            final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(typeName);
            if (featureSource == null) {
                throw new NullPointerException(
                        "The provided SimpleFeatureSource is null, it's impossible to create an index!");
            }
            int count= featureSource.getCount(q);
            if(count==-1){
                return featureSource.getFeatures(q).size();
            }
            return count;

        } catch (Throwable e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        } finally {
            lock.unlock();

        }
    }
}
