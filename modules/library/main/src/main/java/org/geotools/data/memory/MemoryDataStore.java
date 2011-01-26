/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


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
 * @source $URL$
 */
public class MemoryDataStore extends AbstractDataStore {
    /** Memory holds Map of Feature by fid by typeName. */
    protected Map<String,Map<String,SimpleFeature>> memory = new LinkedHashMap<String,Map<String,SimpleFeature>>();

    /** Schema holds FeatureType by typeName */
    protected Map<String,SimpleFeatureType> schema = new HashMap<String,SimpleFeatureType>();

    public MemoryDataStore() {
        super(true);
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
            SimpleFeatureType featureType;
            // use an order preserving map, so that features are returned in the same
            // order as they were inserted. This is important for repeatable rendering
            // of overlapping features.
            Map<String,SimpleFeature> featureMap = new LinkedHashMap<String,SimpleFeature>();
            String typeName;
            SimpleFeature feature;

            feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException("Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            featureType = feature.getFeatureType();
            typeName = featureType.getTypeName();

            featureMap.put(feature.getID(), feature);

            while (reader.hasNext()) {
                feature = reader.next();
                featureMap.put(feature.getID(), feature);
            }

            schema.put(typeName, featureType);
            memory.put(typeName, featureMap);
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
            SimpleFeatureType featureType;
            Map<String,SimpleFeature> featureMap = new LinkedHashMap<String,SimpleFeature>();
            String typeName;
            SimpleFeature feature;

            feature = reader.next();

            if (feature == null) {
                throw new IllegalArgumentException("Provided  FeatureReader<SimpleFeatureType, SimpleFeature> is closed");
            }

            featureType = feature.getFeatureType();
            typeName = featureType.getTypeName();

            featureMap.put(feature.getID(), feature);

            while (reader.hasNext()) {
                feature = reader.next();
                featureMap.put(feature.getID(), feature);
            }

            schema.put(typeName, featureType);
            memory.put(typeName, featureMap);
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
     * @return Array of type names
     *
     * @see org.geotools.data.AbstractDataStore#getFeatureTypes()
     */
    public String[] getTypeNames() {
        synchronized (memory) {
            String[] types = new String[schema.size()];
            int index = 0;

            for (Iterator<String> i = schema.keySet().iterator(); i.hasNext(); index++) {
                types[index] = i.next();
            }

            return types;
        }
    }

    /**
     * SimpleFeatureType access by <code>typeName</code>.
     *
     * @param typeName
     *
     * @return SimpleFeatureType for <code>typeName</code>
     *
     * @throws IOException
     * @throws SchemaNotFoundException DOCUMENT ME!
     *
     * @see org.geotools.data.AbstractDataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        synchronized (memory) {
            if (schema.containsKey(typeName)) {
                return schema.get(typeName);
            }
                throw new SchemaNotFoundException(typeName);
        }
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

    /**
     * Provides  FeatureReader<SimpleFeatureType, SimpleFeature> over the entire contents of <code>typeName</code>.
     * 
     * <p>
     * Implements getFeatureReader contract for AbstractDataStore.
     * </p>
     *
     * @param typeName
     *
     *
     * @throws IOException If typeName could not be found
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.AbstractDataStore#getFeatureSource(java.lang.String)
     */
    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final String typeName)
        throws IOException {
        return new FeatureReader<SimpleFeatureType, SimpleFeature>() {
                SimpleFeatureType featureType = getSchema(typeName);
                Iterator<SimpleFeature> iterator = features(typeName).values().iterator();

                public SimpleFeatureType getFeatureType() {
                    return featureType;
                }

                public SimpleFeature next()
                    throws IOException, IllegalAttributeException, NoSuchElementException {
                    if (iterator == null) {
                        throw new IOException("Feature Reader has been closed");
                    }

                    try {
                        return SimpleFeatureBuilder.copy((SimpleFeature) iterator.next());
                    } catch (NoSuchElementException end) {
                        throw new DataSourceException("There are no more Features", end);
                    }
                }

                public boolean hasNext(){
                    return (iterator != null) && iterator.hasNext();
                }

                public void close(){
                    if (iterator != null) {
                        iterator = null;
                    }

                    if (featureType != null) {
                        featureType = null;
                    }
                }
            };
    }

    /**
     * Provides FeatureWriter over the entire contents of <code>typeName</code>.
     * 
     * <p>
     * Implements getFeatureWriter contract for AbstractDataStore.
     * </p>
     *
     * @param typeName name of FeatureType we wish to modify
     *
     * @return FeatureWriter of entire contents of typeName
     *
     * @throws IOException If writer cannot be obtained for typeName
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.AbstractDataStore#getFeatureSource(java.lang.String)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> createFeatureWriter(final String typeName, final Transaction transaction)
        throws IOException {
        return new FeatureWriter<SimpleFeatureType, SimpleFeature>() {
                SimpleFeatureType featureType = getSchema(typeName);
                Map<String,SimpleFeature> contents = features(typeName);
                Iterator<SimpleFeature> iterator = contents.values().iterator();
                SimpleFeature live = null;

                SimpleFeature current = null; // current Feature returned to user        

                public SimpleFeatureType getFeatureType() {
                    return featureType;
                }

                public SimpleFeature next() throws IOException, NoSuchElementException {
                    if (hasNext()) {
                        // existing content
                        live = iterator.next();

                        try {
                            current = SimpleFeatureBuilder.copy(live);
                        } catch (IllegalAttributeException e) {
                            throw new DataSourceException("Unable to edit " + live.getID() + " of "
                                + typeName);
                        }
                    } else {
                        // new content
                        live = null;

                        try {
                            current = SimpleFeatureBuilder.template(featureType, null);
                        } catch (IllegalAttributeException e) {
                            throw new DataSourceException("Unable to add additional Features of "
                                + typeName);
                        }
                    }

                    return current;
                }

                public void remove() throws IOException {
                    if (contents == null) {
                        throw new IOException("FeatureWriter has been closed");
                    }

                    if (current == null) {
                        throw new IOException("No feature available to remove");
                    }

                    if (live != null) {
                        // remove existing content
                        iterator.remove();
                        listenerManager.fireFeaturesRemoved(typeName, transaction,
                            new ReferencedEnvelope(live.getBounds()), true);
                        live = null;
                        current = null;
                    } else {
                        // cancel add new content
                        current = null;
                    }
                }

                public void write() throws IOException {
                    if (contents == null) {
                        throw new IOException("FeatureWriter has been closed");
                    }

                    if (current == null) {
                        throw new IOException("No feature available to write");
                    }

                    if (live != null) {
                        if (live.equals(current)) {
                            // no modifications made to current
                            //
                            live = null;
                            current = null;
                        } else {
                            // accept modifications
                            //
                            try {
                                live.setAttributes(current.getAttributes());
                            } catch (Exception e) {
                                throw new DataSourceException("Unable to accept modifications to "
                                    + live.getID() + " on " + typeName);
                            }

                            ReferencedEnvelope bounds = new ReferencedEnvelope();
                            bounds.expandToInclude(new ReferencedEnvelope(live.getBounds()));
                            bounds.expandToInclude(new ReferencedEnvelope(current.getBounds()));
                            listenerManager.fireFeaturesChanged(typeName, transaction,
                                bounds, true);
                            live = null;
                            current = null;
                        }
                    } else {
                        // add new content
                        //
                        contents.put(current.getID(), current);
                        listenerManager.fireFeaturesAdded(typeName, transaction,
                        		new ReferencedEnvelope(current.getBounds()), true);
                        current = null;
                    }
                }

                public boolean hasNext() throws IOException {
                    if (contents == null) {
                        throw new IOException("FeatureWriter has been closed");
                    }

                    return (iterator != null) && iterator.hasNext();
                }

                public void close(){
                    if (iterator != null) {
                        iterator = null;
                    }

                    if (featureType != null) {
                        featureType = null;
                    }

                    contents = null;
                    current = null;
                    live = null;
                }
            };
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getBounds(java.lang.String,
     *      org.geotools.data.Query)
     */
    protected ReferencedEnvelope getBounds(Query query)
        throws IOException {
        String typeName = query.getTypeName();
        Map<String,SimpleFeature> contents = features(typeName);
        Iterator<SimpleFeature> iterator = contents.values().iterator();

        CoordinateReferenceSystem coordinateSystem = query.getCoordinateSystem();
        ReferencedEnvelope envelope = null;
        
        Filter filter = query.getFilter();
        
        int count = 0;
        while(iterator.hasNext() && (count < query.getMaxFeatures())) {
            count ++;
            SimpleFeature feature = iterator.next();
            if(filter.evaluate(feature)) {
                count++;
                Envelope env = ((Geometry) feature.getDefaultGeometry()).getEnvelopeInternal();
                if (null == envelope) {
                    envelope = new ReferencedEnvelope(coordinateSystem);
                }
                envelope.expandToInclude(env);
            }
        }

        return envelope;
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getCount(java.lang.String, org.geotools.data.Query)
     */
    protected int getCount(Query query)
        throws IOException {
        String typeName = query.getTypeName();
        Map<String,SimpleFeature> contents = features(typeName);
        Iterator<SimpleFeature> iterator = contents.values().iterator();

        int count = 0;

        Filter filter = query.getFilter();

        while (iterator.hasNext() && (count < query.getMaxFeatures())) {
            if (filter.evaluate(iterator.next())) {
                count++;
            }
        }

        return count;
    }

}
