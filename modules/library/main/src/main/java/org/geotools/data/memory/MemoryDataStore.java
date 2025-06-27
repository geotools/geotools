/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.util.SuppressFBWarnings;

/**
 * This is an example implementation of a DataStore used for testing.
 *
 * <p>It serves as an example implementation of:
 *
 * <ul>
 *   <li>FeatureListenerManager use: allows handling of FeatureEvents
 * </ul>
 *
 * <p>This class will also illustrate the use of In-Process locking when the time comes.
 *
 * @author jgarnett
 */
// This code synchronizes on a ConcurrentHashMap, which does not seem very sensible (the structure
// is designed for concurrent access...). May want to revisit
@SuppressFBWarnings("JLM_JSR166_UTILCONCURRENT_MONITORENTER")
public class MemoryDataStore extends ContentDataStore {

    public MemoryDataStore() {
        super();
    }

    /** Use MemoryState to manage internal storage. */
    @Override
    protected MemoryState createContentState(ContentEntry entry) {
        return new MemoryState((MemoryEntry) entry);
    }

    /**
     * Construct an MemoryDataStore around an empty collection of the provided SimpleFeatureType
     *
     * @param featureType The initial feature type for the memory data store, an empty feature collection of this type
     *     will be made available
     */
    public MemoryDataStore(SimpleFeatureType featureType) {
        try {
            // creates new entry for FeatureType
            entry(featureType);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }

    public MemoryDataStore(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        addFeatures(collection);
    }

    public MemoryDataStore(SimpleFeatureCollection collection) {
        addFeatures(collection);
    }

    public MemoryDataStore(SimpleFeature... array) {
        addFeatures(array);
    }

    public MemoryDataStore(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        addFeatures(reader);
    }

    public MemoryDataStore(SimpleFeatureIterator reader) throws IOException {
        addFeatures(reader);
    }

    /**
     * Configures MemoryDataStore with FeatureReader.
     *
     * @param reader New contents to add
     * @throws IOException If problems are encountered while adding
     * @throws DataSourceException See IOException
     */
    public void addFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        try (reader) {
            SimpleFeature feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException(
                        "Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            addFeatureInternal(feature);

            while (reader.hasNext()) {
                feature = reader.next();
                addFeatureInternal(feature);
            }

        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Problem using reader", e);
        }
    }

    /**
     * Configures MemoryDataStore with FeatureReader.
     *
     * @param reader New contents to add
     * @throws IOException If problems are encountered while adding
     * @throws DataSourceException See IOException
     */
    public void addFeatures(SimpleFeatureIterator reader) throws IOException {
        try (reader) {
            SimpleFeature feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException(
                        "Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            addFeatureInternal(feature);

            while (reader.hasNext()) {
                feature = reader.next();
                addFeatureInternal(feature);
            }
        }
    }
    /**
     * Configures MemoryDataStore with Collection.
     *
     * <p>You may use this to create a MemoryDataStore from a FeatureCollection.
     *
     * @param collection Collection of features to add
     * @throws IllegalArgumentException If provided collection is empty
     */
    public void addFeatures(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Provided Collection is empty");
        }
        synchronized (entries) {
            for (Object item : collection) {
                addFeatureInternal((SimpleFeature) item);
            }
        }
    }

    public void addFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Provided FeatureCollection is empty");
        }
        synchronized (entries) {
            try (FeatureIterator<SimpleFeature> iterator = collection.features()) {
                while (iterator.hasNext()) {
                    addFeatureInternal(iterator.next());
                }
            }
        }
    }
    /**
     * Configures MemoryDataStore with feature array.
     *
     * @param features Array of features to add
     * @throws IllegalArgumentException If provided feature array is empty
     */
    public void addFeatures(SimpleFeature... features) {
        if (features == null || features.length == 0) {
            throw new IllegalArgumentException("Provided features are empty");
        }
        synchronized (entries) {
            for (SimpleFeature feature : features) {
                addFeatureInternal(feature);
            }
        }
    }

    /**
     * Adds a single Feature to the correct typeName entry.
     *
     * <p>This is an internal operation used for setting up MemoryDataStore - please use FeatureWriter for general use.
     *
     * <p>This method is willing to create new FeatureTypes for MemoryDataStore.
     *
     * @param feature Individual feature to add
     */
    public void addFeature(SimpleFeature feature) {
        synchronized (entries) {
            addFeatureInternal(feature);
        }
    }

    private void addFeatureInternal(SimpleFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("Provided Feature is empty");
        }
        SimpleFeatureType featureType = feature.getFeatureType();
        try {
            MemoryEntry entry = entry(featureType);
            entry.addFeature(feature);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }

    /**
     * Access MemoryState for typeName.
     *
     * <p>Technically this is accessing the MemoryState for {@link Transaction#AUTO_COMMIT}, which is the definitive
     * storage for the feature content.
     *
     * @return MemoryState storing feature (by FeatureID)
     * @throws IOException If typeName cannot be found
     */
    protected MemoryEntry entry(String typeName) throws IOException {
        synchronized (entries) {
            for (ContentEntry entry : this.entries.values()) {
                if (entry.getName().getLocalPart().equals(typeName)) {
                    return (MemoryEntry) entry;
                }
            }
        }
        throw new IOException("Type name " + typeName + " not found");
    }

    /**
     * Access to entry to store content of the provided schema, will create new entry if needed.
     *
     * <p>
     *
     * @return MemoryState used for content storage
     * @throws IOException If new entry could not be created due to typeName conflict
     */
    protected MemoryEntry entry(SimpleFeatureType schema) throws IOException {
        Name typeName = schema.getName();
        synchronized (entries) {
            if (entries.containsKey(typeName)) {
                MemoryEntry entry = (MemoryEntry) entries.get(typeName);
                if (FeatureTypes.equals(entry.schema, schema)) {
                    return entry;
                } else {
                    throw new IOException(
                            "Entry " + typeName + " schema " + entry.schema + " incompatible with provided " + schema);
                }
            } else {
                MemoryEntry entry = new MemoryEntry(this, schema);
                entries.put(typeName, entry);
                return entry;
            }
        }
    }

    /**
     * List of available types provided by this DataStore.
     *
     * @return List of type names
     * @see org.geotools.data.ContentDataStore#getFeatureTypes()
     */
    @Override
    protected List<Name> createTypeNames() {
        List<Name> names = new ArrayList<>(this.entries.keySet());
        Collections.sort(names, (n1, n2) -> n1.toString().compareTo(n2.toString()));
        return names;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) {
        return createFeatureSource(entry, Query.ALL);
    }

    protected ContentFeatureSource createFeatureSource(ContentEntry entry, Query query) {
        return new MemoryFeatureStore(entry, query);
    }

    /**
     * Adds support for a new featureType to MemoryDataStore.
     *
     * <p>FeatureTypes are stored by typeName, an IOException will be thrown if the requested typeName is already in
     * use.
     *
     * @param featureType SimpleFeatureType to be added
     * @throws IOException If featureType already exists
     * @see DataStore#createSchema(org.geotools.feature.SimpleFeatureType)
     */
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        Name typeName = featureType.getName();
        if (entries.containsKey(typeName)) {
            // we have a conflict
            throw new IOException(typeName + " already exists");
        }
        MemoryEntry entry = new MemoryEntry(this, featureType);
        entries.put(typeName, entry);
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        for (Name name : entries.keySet()) {
            if (name.getLocalPart().equals(typeName)) {
                removeSchema(name);
                return;
            }
        }
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        if (typeName != null) {
            // graceful remove, its fine if the type has never been registered
            synchronized (entries) {
                entries.remove(typeName);
            }
        }
    }
}
