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
package org.geotools.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;


/**
 * This is a starting point for providing your own FeatureStore implementation.
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public abstract class AbstractFeatureStore extends AbstractFeatureSource
    implements SimpleFeatureStore {
    /** Current Transaction this SimpleFeatureSource is opperating against */
    protected Transaction transaction = Transaction.AUTO_COMMIT;
    
    public AbstractFeatureStore() {
        // just to keep the default constructor around
    }
    
    /**
     * This constructors allows to set the supported hints 
     * @param hints
     */
    public AbstractFeatureStore(Set hints) {
        super(hints);
    }

    /**
     * Retrieve the Transaction this SimpleFeatureSource is opperating against.
     *
     * @return Transaction SimpleFeatureSource is operating against
     */
    public Transaction getTransaction() {
        return transaction;
    }

    // 
    // FeatureStore implementation against DataStore API
    // 

    /**
     * Modifies features matching <code>filter</code>.
     * 
     * <p>
     * Equivelent to:
     * </p>
     * <pre><code>
     * FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter( typeName, filter, transaction );
     * while( writer.hasNext() ){
     *    feature = writer.next();
     *    feature.setAttribute( type.getName(), value );
     *    writer.write();
     * }
     * writer.close();
     * </code>
     * </pre>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param type Attribute to modify
     * @param value Modification being made to type
     * @param filter Identifies features to modify
     *
     * @throws IOException If modification could not be made
     */
    public final void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
        throws IOException {
        Name attributeName = type.getName();
        modifyFeatures( attributeName, value, filter);
    }
    
    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)  throws IOException {
        modifyFeatures(new Name[] { attributeName, }, new Object[] { attributeValue, },
                filter);
    }
    
    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(new Name[] { new NameImpl( name ), }, new Object[] { attributeValue, },
                filter);
    }

    public void modifyFeatures(String[] names, Object[] values, Filter filter)
            throws IOException {
        Name attributeNames[] = new Name[ names.length ];
        for( int i=0; i < names.length; i ++){
            attributeNames[i] = new NameImpl(names[i]);
        }
        modifyFeatures( attributeNames, values, filter ); 
    }

    /**
     * Modifies features matching <code>filter</code>.
     * 
     * <p>
     * Equivelent to:
     * </p>
     * <pre><code>
     * FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter( typeName, filter, transaction );
     * Feature feature;
     * while( writer.hasNext() ){
     *    feature = writer.next();
     *    feature.setAttribute( type[0].getName(), value[0] );
     *    feature.setAttribute( type[1].getName(), value[1] );
     *    ...
     *    feature.setAttribute( type[N].getName(), value[N] ); 
     *    writer.write();
     * }
     * writer.close();
     * </code>
     * </pre>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param type Attributes to modify
     * @param value Modifications being made to type
     * @param filter Identifies features to modify
     *
     * @throws IOException If we could not modify Feature
     * @throws DataSourceException See IOException
     */
    public final void modifyFeatures(AttributeDescriptor[] type, Object[] value,
        Filter filter) throws IOException {
        
        Name attributeNames[] = new Name[ type.length ];
        for( int i=0; i < type.length; i ++){
            attributeNames[i] = type[i].getName();
        }
        modifyFeatures( attributeNames, value, filter );       
    }

    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter)  throws IOException{
        String typeName = getSchema().getTypeName();
        if ( filter == null ) {
            String msg = "Must specify a filter, must not be null.";
            throw new IllegalArgumentException( msg );
        }
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ((DataStore) getDataStore())
                .getFeatureWriter(typeName, filter, getTransaction());
        SimpleFeature feature;        
        for( Name attributeName : attributeNames ){
            if( getSchema().getDescriptor( attributeName) == null ){
                throw new DataSourceException("Cannot modify "+attributeName+" as it is not an attribute of "+getSchema().getName() );
            }
        }
        try {
            while (writer.hasNext()) {
                feature = writer.next();

                for (int i = 0; i < attributeNames.length; i++) {
                    try {
                        feature.setAttribute(attributeNames[i], attributeValues[i]);
                    } catch (Exception e) {
                        throw new DataSourceException(
                            "Could not update feature " + feature.getID()
                            + " with " + attributeNames[i] + "=" + attributeValues[i], e);
                    }
                }

                writer.write();
            }
        } finally {
            writer.close();
        }
    }
    /**
     * Add Features from reader to this FeatureStore.
     * 
     * <p>
     * Equivelent to:
     * </p>
     * <pre><code>
     * Set set = new HashSet();
     * FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter( typeName, true, transaction );
     * Featrue feature, newFeature;
     * while( reader.hasNext() ){
     *    feature = reader.next();
     *    newFeature = writer.next();
     *    newFeature.setAttributes( feature.getAttribtues( null ) );
     *    writer.write();
     *    set.add( newfeature.getID() );
     * }
     * reader.close();
     * writer.close();
     * 
     * return set;
     * </code>
     * </pre>
     * 
     * <p>
     * (If you don't have a  FeatureReader<SimpleFeatureType, SimpleFeature> handy DataUtilities.reader() may be
     * able to help out)
     * </p>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param reader
     *
     * @return The Set of FeatureIDs added
     *
     * @throws IOException If we encounter a problem encounter writing content
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.FeatureStore#addFeatures(org.geotools.data.FeatureReader)
     */
    public Set<String> addFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        Set<String> addedFids = new HashSet<String>();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature = null;
        SimpleFeature newFeature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore()
                .getFeatureWriterAppend(typeName, getTransaction());

        try {
            while (reader.hasNext()) {
                try {
                    feature = reader.next();
                } catch (Exception e) {
                    throw new DataSourceException("Could not add Features, problem with provided reader",
                        e);
                }

                newFeature = (SimpleFeature)writer.next();

                try {
                    newFeature.setAttributes(feature.getAttributes());
                } catch (Exception writeProblem) {
                    throw new DataSourceException("Could not create "
                        + typeName + " out of provided feature: "
                        + feature.getID(), writeProblem);
                }
                
                boolean useExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
                if(getQueryCapabilities().isUseProvidedFIDSupported() && useExisting) {
                    ((FeatureIdImpl) newFeature.getIdentifier()).setID(feature.getID());
                }

                writer.write();
                addedFids.add(newFeature.getID());
            }
        } finally {
            reader.close();
            writer.close();
        }

        return addedFids;
    }

    public List<FeatureId> addFeatures(FeatureCollection<SimpleFeatureType,SimpleFeature> collection)
            throws IOException {
    	List<FeatureId> addedFids = new LinkedList<FeatureId>();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature = null;
        SimpleFeature newFeature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore()
                .getFeatureWriterAppend(typeName, getTransaction());

        Iterator iterator = collection.iterator();
        try {
        	
            while (iterator.hasNext()) {
                feature = (SimpleFeature) iterator.next();
                newFeature = (SimpleFeature)writer.next();
                try {
                    newFeature.setAttributes(feature.getAttributes());
                } catch (Exception writeProblem) {
                    throw new DataSourceException("Could not create "
                        + typeName + " out of provided feature: "
                        + feature.getID(), writeProblem);
                }
                
                boolean useExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
                if(getQueryCapabilities().isUseProvidedFIDSupported() && useExisting) {
                    ((FeatureIdImpl) newFeature.getIdentifier()).setID(feature.getID());
                }

                writer.write();
                addedFids.add(newFeature.getIdentifier());
            }
        } finally {
            collection.close( iterator );
            writer.close();
        }
        return addedFids;
    }
    
    /**
     * Removes features indicated by provided filter.
     * 
     * <p>
     * Equivelent to:
     * </p>
     * <pre><code>
     * FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter( typeName, filter, transaction );
     * Feature feature;
     * while( writer.hasNext() ){
     *    feature = writer.next();
     *    writer.remove();
     * }
     * writer.close();
     * </code>
     * </pre>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param filter Identifies features to remove
     *
     * @throws IOException
     */
    public void removeFeatures(Filter filter) throws IOException {
        String typeName = getSchema().getTypeName();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriter(typeName,
                filter, getTransaction());

        try {
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Replace with contents of reader.
     * 
     * <p>
     * Equivelent to:
     * </p>
     * <pre><code>
     * FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter( typeName, false, transaction );
     * Feature feature, newFeature;
     * while( writer.hasNext() ){
     *    feature = writer.next();
     *    writer.remove();
     * }
     * while( reader.hasNext() ){
     *    newFeature = reader.next();
     *    feature = writer.next();
     *    newFeature.setAttributes( feature.getAttributes( null ) );
     *    writer.write();
     * }
     * reader.close();
     * writer.close();
     * </code>
     * </pre>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param reader Contents to replace with
     *
     * @throws IOException if anything goes wrong during replacement
     * @throws DataSourceException See IOException
     */
    public void setFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        String typeName = getSchema().getTypeName();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriter(typeName,
                getTransaction());
        SimpleFeature feature;
        SimpleFeature newFeature;

        try {
            while (writer.hasNext()) {
                feature = writer.next();
                writer.remove();
            }

            while (reader.hasNext()) {
                try {
                    feature = reader.next();
                } catch (Exception readProblem) {
                    throw new DataSourceException("Could not add Features, problem with provided reader",
                        readProblem);
                }

                newFeature = (SimpleFeature)writer.next();

                try {
                    newFeature.setAttributes(feature.getAttributes());
                } catch (IllegalAttributeException writeProblem) {
                    throw new DataSourceException("Could not create "
                        + typeName + " out of provided feature: "
                        + feature.getID(), writeProblem);
                }

                writer.write();
            }
        } finally {
            reader.close();
            writer.close();
        }
    }

    public void setTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException(
                "Transaction cannot be null, did you mean Transaction.AUTO_COMMIT?");
        }

        this.transaction = transaction;
    }
}
