/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.oracle.OracleDatastoreWrapper;
import org.geotools.gce.imagemosaic.catalog.postgis.PostgisDatastoreWrapper;
import org.geotools.util.Utilities;
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

    final static FilterFactory2 ff = CommonFactoryFinder
            .getFilterFactory2(GeoTools.getDefaultHints());

    private DataStore tileIndexStore;

    Set<String> typeNames = new HashSet<String>();

    private String geometryPropertyName;

    PathType pathType;

    String locationAttribute;

    ImageReaderSpi suggestedRasterSPI;

    String parentLocation;

    boolean heterogeneous;

    boolean wrapstore = false;

    private Properties params;

    private DataStoreFactorySpi spi;

    public GTDataStoreGranuleCatalog(final Properties params, final boolean create,
            final DataStoreFactorySpi spi, final Hints hints) {
        super(hints);
        Utilities.ensureNonNull("params", params);
        Utilities.ensureNonNull("spi", spi);
        this.spi = spi;
        this.params = params;

        try {
            this.pathType = (PathType) params.get(Utils.Prop.PATH_TYPE);
            this.locationAttribute = (String) params.get(Utils.Prop.LOCATION_ATTRIBUTE);
            final String temp = (String) params.get(Utils.Prop.SUGGESTED_SPI);
            this.suggestedRasterSPI = temp != null
                    ? (ImageReaderSpi) Class.forName(temp).newInstance() : null;
            this.parentLocation = (String) params.get(Utils.Prop.PARENT_LOCATION);
            if (params.containsKey(Utils.Prop.HETEROGENEOUS)) {
                this.heterogeneous = (Boolean) params.get(Utils.Prop.HETEROGENEOUS);
            }
            if (params.containsKey(Utils.Prop.WRAP_STORE)) {
                this.wrapstore = (Boolean) params.get(Utils.Prop.WRAP_STORE);
            }

            // creating a store, this might imply creating it for an existing underlying store or
            // creating a brand new one
            Map<String, Serializable> dastastoreParams = Utils.filterDataStoreParams(params, spi);

            boolean isPostgis = Utils.isPostgisStore(spi);
            // H2 workadound
            if (Utils.isH2Store(spi)) {
                Utils.fixH2DatabaseLocation(dastastoreParams, parentLocation);
                Utils.fixH2MVCCParam(dastastoreParams);
            }
            if (isPostgis) {
                Utils.fixPostgisDBCreationParams(dastastoreParams);
            }

            if (!create) {
                tileIndexStore = spi.createDataStore(dastastoreParams);
            } else {
                // this works only with the shapefile datastore, not with the others
                // therefore I try to catch the error to try and use themethdo without *New*
                try {
                    tileIndexStore = spi.createNewDataStore(dastastoreParams);
                } catch (UnsupportedOperationException e) {
                    tileIndexStore = spi.createDataStore(dastastoreParams);
                }
            }

            if (isPostgis && wrapstore) {
                tileIndexStore = new PostgisDatastoreWrapper(tileIndexStore,
                        FilenameUtils.getFullPath(parentLocation));
            } else if (Utils.isOracleStore(spi)) {
                tileIndexStore = new OracleDatastoreWrapper(tileIndexStore,
                        FilenameUtils.getFullPath(parentLocation));
            }

            // is this a new store? If so we do not set any properties
            if (create) {
                return;
            }

            String typeName = null;
            boolean scanForTypeNames = false;

            if (params.containsKey(Utils.Prop.TYPENAME)) {
                typeName = (String) params.get(Utils.Prop.TYPENAME);
            }

            if (params.containsKey(Utils.SCAN_FOR_TYPENAMES)) {
                scanForTypeNames = Boolean.valueOf(params.get(Utils.SCAN_FOR_TYPENAMES).toString());
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
                checkMosaicSchema(typeName);
                addTypeName(typeName, false);
            } else {
                // pick the first suitable type name
                String[] typeNames = tileIndexStore.getTypeNames();
                if (typeNames != null) {
                    for (String tn : typeNames) {
                        if (isValidMosaicSchema(tn)) {
                            addTypeName(tn, false);
                            break;
                        }
                    }
                }
            }

            // if we got here and there is not typename in the list, we could not find one
            if (this.typeNames.size() == 0) {
                throw new IllegalArgumentException("Could not find a suitable mosaic type "
                        + "(with a footprint and a location attribute named "
                        + getLocationAttributeName() + " in the store");
            }

            if (this.typeNames.size() > 0) {
                // pick the first valid schema found
                for (String tn : typeNames) {
                    if (isValidMosaicSchema(tn)) {
                        extractBasicProperties(tn);
                        break;
                    }
                }
            } else if (typeName != null && typeName.contains(",")) {
                String[] typeNames = typeName.split(",");
                for (String tn : typeNames) {
                    extractBasicProperties(tn);
                }
            } else if (typeName != null) {
                extractBasicProperties(typeName);
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
     * Returns true if the type is usable as a mosaic index, that is, it has a geometry and the expected location property
     */
    private boolean isValidMosaicSchema(String typeName) throws IOException {
        SimpleFeatureType schema = tileIndexStore.getSchema(typeName);

        return Utils.isValidMosaicSchema(schema, getLocationAttributeName());
    }

    private String getLocationAttributeName() {
        if (locationAttribute == null) {
            return "location";
        } else {
            return locationAttribute;
        }
    }

    /**
     * Checks the provided schema, and throws an exception if not valid
     * 
     * @param schema
     * @throws IOException
     */
    private void checkMosaicSchema(String typeName) throws IOException {
        SimpleFeatureType schema = tileIndexStore.getSchema(typeName);
        if (schema == null) {
            throw new IllegalArgumentException("Could not find typename " + schema);
        } else {
            checkMosaicSchema(schema);
        }
    }

    /**
     * Checks the provided schema, and throws an exception if not valid
     * 
     * @param schema
     */
    private void checkMosaicSchema(SimpleFeatureType schema) {
        if (!Utils.isValidMosaicSchema(schema, getLocationAttributeName())) {
            throw new IllegalArgumentException("Invalid mosaic schema " + schema + ", "
                    + "it should have a geometry and a location property of name "
                    + locationAttribute);
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
                    LOGGER.warning(
                            "BBOXFilterExtractor::extractBasicProperties(): passed typename is null, using: "
                                    + typeName);
            }

            // loading all the features into memory to build an in-memory index.
            for (String type : typeNames) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): Looking for type \'"
                            + typeName + "\' in DataStore:getTypeNames(). Testing: \'" + type
                            + "\'.");
                if (type.equalsIgnoreCase(typeName)) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine(
                                "BBOXFilterExtractor::extractBasicProperties(): SUCCESS -> type \'"
                                        + typeName + "\' is equalsIgnoreCase() to \'" + type
                                        + "\'.");
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
        if (schema != null && schema.getGeometryDescriptor() != null) {
            geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine(
                        "BBOXFilterExtractor::extractBasicProperties(): geometryPropertyName is set to \'"
                                + geometryPropertyName + "\'.");

        } else {
            throw new IOException(
                    "BBOXFilterExtractor::extractBasicProperties(): unable to get a schema from the featureSource");
        }

    }

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();
            try {
                if (tileIndexStore != null) {
                    tileIndexStore.dispose();
                }
                if (multiScaleROIProvider != null) {
                    multiScaleROIProvider.dispose();
                }
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            } finally {
                tileIndexStore = null;
                multiScaleROIProvider = null;
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

            // visiting the features from the underlying store, caring for early bail out
            try(SimpleFeatureIterator fi = features.features()) {
                while(fi.hasNext() && !visitor.isVisitComplete()) {
                    final SimpleFeature sf = fi.next();
                    MultiLevelROI footprint = getGranuleFootprint(sf);
                    if (footprint == null || !footprint.isEmpty()) {
                        try {
                            final GranuleDescriptor granule = new GranuleDescriptor(sf,
                                    suggestedRasterSPI, pathType, locationAttribute,
                                    parentLocation, footprint, heterogeneous, q.getHints());

                            visitor.visit(granule, sf);
                        } catch (Exception e) {
                            LOGGER.log(Level.FINE, "Skipping invalid granule", e);
                        }
                    }
                }
            }
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
        try {
            lock.lock();
            checkStore();
            return this.tileIndexStore.getFeatureSource(typeName).getBounds();
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        } finally {
            lock.unlock();
        }

        return null;
    }

    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("typeSpec", typeSpec);
        final Lock lock = rwLock.writeLock();
        String type = null;
        try {
            lock.lock();
            checkStore();

            final SimpleFeatureType featureType = DataUtilities.createType(namespace, typeName,
                    typeSpec);
            checkMosaicSchema(featureType);
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

    private void removeTypeName(String typeName) {
        if (this.typeNames.contains(typeName)) {
            typeNames.remove(typeName);
        }
    }

    @Override
    public String[] getTypeNames() {
        if (this.typeNames != null && !this.typeNames.isEmpty()) {
            return this.typeNames.toArray(new String[] {});
        }
        return null;
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        Utilities.ensureNonNull("featureType", featureType);
        checkMosaicSchema(featureType);
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

    public void removeType(String typeName) throws IOException {
        Utilities.ensureNonNull("featureType", typeName);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            checkStore();

            tileIndexStore.removeSchema(typeName);
            removeTypeName(typeName);

        } finally {
            lock.unlock();
        }

    }

    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        Utilities.ensureNonNull("typeSpec", typeSpec);
        Utilities.ensureNonNull("identification", identification);
        final Lock lock = rwLock.writeLock();
        String typeName = null;
        try {
            lock.lock();
            checkStore();
            final SimpleFeatureType featureType = DataUtilities.createType(identification,
                    typeSpec);
            checkMosaicSchema(featureType);
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
                LOGGER.warning(
                        "This granule catalog was not properly dispose as it still points to:"
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
            int count = featureSource.getCount(q);
            if (count == -1) {
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

    @Override
    public void drop() throws IOException {

        // drop a datastore. Right now, only postGIS drop is supported

        final Map<?, ?> params = Utils.filterDataStoreParams(this.params, spi);
        // Use reflection to invoke dropDatabase on postGis factory DB

        final Method[] methods = spi.getClass().getMethods();
        boolean dropped = false;
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("dropDatabase")) {
                try {
                    method.invoke(spi, params);
                } catch (Exception e) {
                    throw new IOException("Unable to drop the database: ", e);
                }
                dropped = true;
                break;
            }
        }
        if (!dropped) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to drop catalog for SPI " + spi.getDisplayName());
            }
        }
    }
}
