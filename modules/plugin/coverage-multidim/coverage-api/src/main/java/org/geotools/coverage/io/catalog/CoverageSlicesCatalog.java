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
package org.geotools.coverage.io.catalog;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * This class simply builds an index for fast indexed queries.
 * 
 * TODO: we may consider converting {@link CoverageSlice}s to {@link SimpleFeature}s
 */
public class CoverageSlicesCatalog {
    

    /** Logger. */
    final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CoverageSlicesCatalog.class);

    final static H2DataStoreFactory INTERNAL_STORE_SPI = new H2DataStoreFactory();
    
    static final String SCAN_FOR_TYPENAMES = "ScanTypeNames";

    /** The slices index store */
    private DataStore slicesIndexStore;

    /** The feature type name */
    private Set<String> typeNames = new HashSet<String>();

    private String geometryPropertyName;

    private ReferencedEnvelope bounds;

    public final static String IMAGE_INDEX_ATTR = "imageindex";

    private final SoftValueHashMap<Integer, CoverageSlice> coverageSliceDescriptorsCache = new SoftValueHashMap<Integer, CoverageSlice>(0);

    public CoverageSlicesCatalog(final String database, final File parentLocation) {
        this(createParams(database,parentLocation));
    }

    /**
     * @param database
     * @param parentLocation2
     * @return
     */
    private static Map<String, Serializable> createParams(String database, File parentLocation) {
        Utilities.ensureNonNull("database", database);
        Utilities.ensureNonNull("parentLocation", parentLocation);
        final Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("ScanTypeNames", Boolean.valueOf(true));
        final String url = DataUtilities.fileToURL(parentLocation).toExternalForm();
        String updatedDB;
        try {
            updatedDB = "file:" + (new File(DataUtilities.urlToFile(new URL(url)), database)).getPath();
            params.put("ParentLocation", url);
            params.put("database", updatedDB);
            params.put("dbtype", "h2");
            params.put("user", "geotools");
            params.put("passwd", "geotools");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        return params;
    }

    private CoverageSlicesCatalog(final Map<String, Serializable> params) {
        Utilities.ensureNonNull("params", params);
        try {

            // creating a store, this might imply creating it for an existing underlying store or
            // creating a brand new one
            slicesIndexStore = INTERNAL_STORE_SPI.createDataStore(params);

            // if this is not a new store let's extract basic properties from it
            String typeName = null;
            boolean scanForTypeNames = false;
            
            // Handle multiple typeNames
            if(params.containsKey("TypeName")){
                typeName=(String) params.get("TypeName");
            }  
            if (params.containsKey(SCAN_FOR_TYPENAMES)) {
                scanForTypeNames = (Boolean) params.get(SCAN_FOR_TYPENAMES);
            }
            
            if (scanForTypeNames) {
                String[] typeNames = slicesIndexStore.getTypeNames();
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
                if (slicesIndexStore != null){
                    slicesIndexStore.dispose();
                }
            } catch (Throwable e1) {
                if (LOGGER.isLoggable(Level.FINE)){
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
     * <p>
     * We need to arrive here with at least a read lock!
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
            if (typeNames == null || typeNames.length <= 0){
                if(LOGGER.isLoggable(Level.FINE)){
                       LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): Problems when opening the index,"
                                + " no typenames for the schema are defined");
                }
                return;
            }
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

        final SimpleFeatureSource featureSource = slicesIndexStore.getFeatureSource(typeName);
        if (featureSource == null){
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

    private void addTypeName(String typeName, final boolean check) {
          if (check && this.typeNames.contains(typeName)) {
              throw new IllegalArgumentException("This typeName already exists: " + typeName);
          }
          this.typeNames.add(typeName);    
  }

    public String[] getTypeNames() {
        if (this.typeNames != null && !this.typeNames.isEmpty()) {
            return (String[]) this.typeNames.toArray(new String[]{});
        }
        return null;
    }

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();
            try {
                if (slicesIndexStore != null)
                    slicesIndexStore.dispose();
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

    public void addGranule(final String typeName, final SimpleFeature granule, final Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("granule", granule);
        Utilities.ensureNonNull("transaction", transaction);
        final DefaultFeatureCollection collection= new DefaultFeatureCollection();
        collection.add(granule);
        addGranules(typeName, collection, transaction);
    }

    public void addGranules(final String typeName, final SimpleFeatureCollection granules, final Transaction transaction)
            throws IOException {
        Utilities.ensureNonNull("granuleMetadata", granules);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();

            final SimpleFeatureStore store = (SimpleFeatureStore) slicesIndexStore.getFeatureSource(typeName);
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
            final SimpleFeatureCollection features = featureSource.getFeatures(q);
            if (features == null)
                throw new NullPointerException(
                        "The provided SimpleFeatureCollection is null, it's impossible to create an index!");

            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Index Loaded");

            // load the feature from the underlying datastore as needed
            final SimpleFeatureIterator it = features.features();
            try {
                if (!it.hasNext()) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("The provided SimpleFeatureCollection  or empty, it's impossible to create an index!");
                    return Collections.emptyList();
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
                            // create the granule coverageDescriptor
                            slice = new CoverageSlice(sf);
                            coverageSliceDescriptorsCache.put(granuleIndex, slice);
                        }
                    }
                    returnValue.add(slice);
                }
            } finally {
                it.close();
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
            if (bounds == null) {
                bounds = this.slicesIndexStore.getFeatureSource(typeName).getBounds();
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            bounds = null;
        } finally {
            lock.unlock();
        }
        return bounds;
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        Utilities.ensureNonNull("featureType", featureType);
        final Lock lock = rwLock.writeLock();
        String typeName = null;
        try {
            lock.lock();
            checkStore();

            slicesIndexStore.createSchema(featureType);
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
    protected void finalize() throws Throwable {
        super.finalize();

        // warn people
        if (this.slicesIndexStore != null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("This granule catalog was not properly dispose as it still points to:"
                        + slicesIndexStore.getInfo().toString());
            }
            // try to dispose the underlying store if it has not been disposed yet
            this.dispose();
        }
    }

    public void removeGranules(String typeName,Filter filter, Transaction transaction) throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        Utilities.ensureNonNull("filter", filter);
        Utilities.ensureNonNull("transaction", transaction);
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            // check if the index has been cleared
            checkStore();

            final SimpleFeatureStore store = (SimpleFeatureStore) slicesIndexStore.getFeatureSource(typeName);
            store.setTransaction(transaction);
            store.removeFeatures(filter);

        } finally {
            lock.unlock();
        }
    }
}
