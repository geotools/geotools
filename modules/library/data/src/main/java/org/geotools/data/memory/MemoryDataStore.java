/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * This is an example implementation of a DataStore used for testing.
 * 
 * <p>
 * It serves as an example implementation of:
 * </p>
 * 
 * <ul>
 * <li>
 * FeatureListenerManager use: allows handling of FeatureEvents
 * </li>
 * </ul>
 * 
 * <p>
 * This class will also illustrate the use of In-Process locking when the time comes.
 * </p>
 *
 * @author jgarnett
 *
 *
 * @source $URL$
 */
public class MemoryDataStore extends ContentDataStore {
    /** Memory holds Map of Feature by fid by typeName. */
    protected Map<String,Map<String,SimpleFeature>> memory = new LinkedHashMap<String,Map<String,SimpleFeature>>();

    /** Schema holds FeatureType by typeName */
    protected Map<String,SimpleFeatureType> schema = new HashMap<String,SimpleFeatureType>();

    public MemoryDataStore() {
        super();
    }

    /**
     * Construct an MemoryDataStore around an empty collection of the provided SimpleFeatureType
     * @param schema An empty feature collection of this type will be made available
     */
    public MemoryDataStore(SimpleFeatureType featureType) {
        Map<String,SimpleFeature> featureMap = new LinkedHashMap<String,SimpleFeature>();
        String typeName = featureType.getTypeName();
        schema.put(typeName, featureType);
        memory.put(typeName, featureMap);
    }
    public MemoryDataStore(FeatureCollection<SimpleFeatureType,SimpleFeature> collection) {
        addFeatures(collection);
    }
    public MemoryDataStore(SimpleFeatureCollection collection) {
        addFeatures(collection);
    }

    public MemoryDataStore(SimpleFeature[] array){
        addFeatures(array);
    }

    public MemoryDataStore(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        addFeatures(reader);
    }
    public MemoryDataStore(SimpleFeatureIterator reader) throws IOException {
        addFeatures(reader);
    }

    /**
     * Configures MemoryDataStore with FeatureReader.
     *
     * @param reader New contents to add
     *
     * @throws IOException If problems are encountered while adding
     * @throws DataSourceException See IOException
     */
    public void addFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        try {
            SimpleFeature feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException("Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            addFeatureInternal(feature);
            
            while (reader.hasNext()) {
                feature = reader.next();
                addFeatureInternal(feature);
            }

        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Problem using reader", e);
        }
        finally {
        	reader.close();
        }
    }

    /**
     * Configures MemoryDataStore with FeatureReader.
     *
     * @param reader New contents to add
     *
     * @throws IOException If problems are encountered while adding
     * @throws DataSourceException See IOException
     */
    public void addFeatures(SimpleFeatureIterator reader) throws IOException {
        try {
            SimpleFeature feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException("Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            addFeatureInternal(feature);

            while (reader.hasNext()) {
                feature = reader.next();
                addFeatureInternal(feature);
            }
        }
        finally {
            reader.close();
        }
    }
    /**
     * Configures MemoryDataStore with Collection.
     * 
     * <p>
     * You may use this to create a MemoryDataStore from a FeatureCollection.
     * </p>
     *
     * @param collection Collection of features to add
     *
     * @throws IllegalArgumentException If provided collection is empty
     */
    public void addFeatures(Collection<?> collection) {
        if ((collection == null) || collection.isEmpty()) {
            throw new IllegalArgumentException("Provided SimpleFeatureCollection is empty");
        }

        synchronized (memory) {
            for (Iterator<?> i = collection.iterator(); i.hasNext();) {
                addFeatureInternal((SimpleFeature) i.next());
            }
        }
    }
    public void addFeatures(FeatureCollection<SimpleFeatureType,SimpleFeature> collection) {
        if ((collection == null) ) {
            throw new IllegalArgumentException("Provided SimpleFeatureCollection is empty");
        }
        synchronized (memory) {
            try {
                collection.accepts( new FeatureVisitor(){
                    public void visit(Feature feature) {
                        addFeatureInternal( (SimpleFeature) feature );
                    }                
                }, null );
            }
            catch( IOException ignore){
                LOGGER.log( Level.FINE, "Unable to add all features", ignore );
            }
        }
    }
    /**
     * Configures MemoryDataStore with feature array.
     *
     * @param features Array of features to add
     *
     * @throws IllegalArgumentException If provided feature array is empty
     */
    public void addFeatures(SimpleFeature[] features) {
        if ((features == null) || (features.length == 0)) {
            throw new IllegalArgumentException("Provided features are empty");
        }

        synchronized (memory) {
            for (int i = 0; i < features.length; i++) {
                addFeatureInternal(features[i]);
            }
        }
    }

    /**
     * Adds a single Feature to the correct typeName entry.
     * 
     * <p>
     * This is an internal opperation used for setting up MemoryDataStore - please use
     * FeatureWriter for generatl use.
     * </p>
     * 
     * <p>
     * This method is willing to create new FeatureTypes for MemoryDataStore.
     * </p>
     *
     * @param feature Individual feature to add
     */
    public void addFeature(SimpleFeature feature) {
        synchronized (memory) {
            addFeatureInternal(feature);
        }
    }

    private void addFeatureInternal(SimpleFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("Provided Feature is empty");
        }

        SimpleFeatureType featureType;
        featureType = feature.getFeatureType();

        String typeName = featureType.getTypeName();

        Map<String,SimpleFeature> featuresMap;

        if (!memory.containsKey(typeName)) {
            try {
                createSchema(featureType);
            } catch (IOException e) {
                // this should not of happened ?!?
                // only happens if typeNames is taken and
                // we just checked                    
            }
        }

        featuresMap = memory.get(typeName);
        featuresMap.put(feature.getID(), feature);
    }

    /**
     * Access featureMap for typeName.
     *
     * @param typeName
     *
     * @return A Map of Features by FID
     *
     * @throws IOException If typeName cannot be found
     */
    protected Map<String,SimpleFeature> features(String typeName) throws IOException {
        synchronized (memory) {
            if (memory.containsKey(typeName)) {
                return memory.get(typeName);
            }
        }

        throw new IOException("Type name " + typeName + " not found");
    }
    
    

    /**
     * List of available types provided by this DataStore.
     *
     * @return List of type names
     *
     * @see org.geotools.data.ContentDataStore#getFeatureTypes()
     */
    protected List<Name> createTypeNames() {
        synchronized (memory) {
            List<Name> typeNames = new ArrayList<Name>();
            
            for (Iterator<String> i = schema.keySet().iterator(); i.hasNext();) {
                typeNames.add( new NameImpl(namespaceURI, i.next()));
            }
            return typeNames;
        }
    }
    
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) {
        return createFeatureSource(entry, Query.ALL);
    }
    
    protected ContentFeatureSource createFeatureSource(ContentEntry entry, Query query) {
        return new MemoryFeatureStore(entry, query);
    }

    /**
     * Adds support for a new featureType to MemoryDataStore.
     * 
     * <p>
     * FeatureTypes are stored by typeName, an IOException will be thrown if the requested typeName
     * is already in use.
     * </p>
     *
     * @param featureType SimpleFeatureType to be added
     *
     * @throws IOException If featureType already exists
     *
     * @see org.geotools.data.DataStore#createSchema(org.geotools.feature.SimpleFeatureType)
     */
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        String typeName = featureType.getTypeName();

        if (memory.containsKey(typeName)) {
            // we have a conflict
            throw new IOException(typeName + " already exists");
        }
            // insertion order preserving map
            Map<String,SimpleFeature> featuresMap = new LinkedHashMap<String,SimpleFeature>();
            schema.put(typeName, featureType);
            memory.put(typeName, featuresMap);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.store.ContentDataStore#removeSchema(java.lang.String)
     */
    @Override
    public void removeSchema(String typeName) throws IOException {
        if (typeName != null) {
            // graceful remove, its fine if the type has never been registered
            synchronized (schema) {
                schema.remove(typeName);
            }

            synchronized (memory) {
                memory.remove(typeName);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.geotools.data.store.ContentDataStore#removeSchema(org.opengis.feature.type.Name)
     */
    @Override
    public void removeSchema(Name typeName) throws IOException {
        if (typeName != null && typeName.getLocalPart() != null) {
            removeSchema(typeName.getLocalPart());
        }
    }

}
