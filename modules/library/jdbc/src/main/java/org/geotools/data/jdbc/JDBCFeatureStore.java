/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.IllegalAttributeException;
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
 *
 * @task REVISIT: Make modify/add/remove atomic if the transaction is
 *       AUTO_COMMIT.  This is done by the start of each of those method
 *       checking to see if the transaction is auto-commit, if it is then they
 *       make a new transaction and pass that to the writer.  The writer does
 *       its thing, and then at the end of the method you just commit the
 *       transaction.  This way if the writer messes up its changes are rolled
 *       back.  The old jdbc datasources supported this, and it'd be nice to
 *       do here as well.
 * @task UPDATE: made modify atomic as an example, I actually have
 *       the beginings of a smart idea in mind. Similar to
 *       SwingUtilities.runLater...
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCFeatureStore extends JDBCFeatureSource implements SimpleFeatureStore {
    
   /** The logger for the postgis module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.jdbc");

/** Current Transaction this SimpleFeatureSource is opperating against */
    protected Transaction transaction = Transaction.AUTO_COMMIT;

    public JDBCFeatureStore(JDBC1DataStore jdbcDataStore, SimpleFeatureType featureType) {
        super(jdbcDataStore, featureType);
    }

    /* Retrieve the Transaction this SimpleFeatureSource is opperating against. */
    public Transaction getTransaction() {
        return transaction;
    }
    
    /**
     * Used by subclasses to access locking manager.
     * <p>
     * All our implementations here are rely on
     * FeatureWriter to check the locks.
     * </p>
     * <p>
     * When making your own SQL opperations, have a look at
     * assertFids( Set fids ), and assertFids( Filter ). You 
     * may use these to check against the lockingManager if one is used.
     * </p>
     * If the lockingManager is not used, ie is null, it assumed that you are
     * making use of native database locks. Or doing your own thing.
     * </p>
     * <p>
     * That is the assertFids functions only when lockingManager is non null.
     * </p>
     * @return LockingManager
     */
    protected InProcessLockingManager getInProcessLockingManager(){
        LockingManager lockingManager = getJDBCDataStore().getLockingManager();
        if( lockingManager instanceof InProcessLockingManager){
            return (InProcessLockingManager) lockingManager;
        }
        return null;
    }
    protected Set fids( Filter filter ) throws NoSuchElementException, IOException, IllegalAttributeException{
        Set fids = new HashSet();
        String typeName = getSchema().getTypeName();        
        DefaultQuery query = new DefaultQuery( typeName, filter, Integer.MAX_VALUE, Query.ALL_NAMES, "fids" );
         FeatureReader<SimpleFeatureType, SimpleFeature> reader =
            getJDBCDataStore().getFeatureReader( query, getTransaction() );
        try {
            while( reader.hasNext() ){
                fids.add( reader.next().getID() );   
            }
        }
        finally {
            reader.close();            
        }
        return fids;
    }
    protected void assertFilter( Filter filter ) throws IOException {
        if( getInProcessLockingManager() == null ){
            return; // subclass is doing its own thing
        }        
        Set set = null;
        try {
            set = fids( filter );    
        }
        catch( NoSuchElementException huh){
            throw new FeatureLockException("Could not verify filter:", huh );
        }
        catch( IllegalAttributeException eh){
            throw new FeatureLockException("Could not verify filter:", eh );            
        } 
        assertFids( set );
    }
    protected void assertFids( Set fids ) throws FeatureLockException {
        InProcessLockingManager lockingManager = getInProcessLockingManager();
        if( lockingManager == null ){
            return; // subclass is doing its own thing
        }
        Transaction t = getTransaction();
        String typeName = getSchema().getTypeName();
        String fid;
        for( Iterator i=fids.iterator(); i.hasNext();){
            fid = (String) i.next();
            lockingManager.assertAccess( typeName, fid, transaction );
        }        
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
     * modifyFeatures( new AttributeDescriptor[]{ type, }, new Object[]{ value, }, filter );
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
     * @throws IOException
     */
    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
        throws IOException {
        modifyFeatures( new Name[]{type.getName()}, new Object[]{value}, filter );
    }

    public void modifyFeatures(Name name, Object value, Filter filter) throws IOException {
        modifyFeatures(new Name[] { name }, new Object[] { value }, filter);
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
     * @throws IOException
     */
    public void modifyFeatures(AttributeDescriptor[] type, Object[] value,
        Filter filter) throws IOException {
        
        Name attributeNames[] = new Name[ type.length ];
        for( int i=0; i < type.length; i ++){
            attributeNames[i] = type[i].getName();
        }
        modifyFeatures( attributeNames, value, filter );       
    }
    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter)  throws IOException{
        
        String typeName = getSchema().getTypeName();
        
        if( getTransaction() == Transaction.AUTO_COMMIT ){
            // implement as atomic
            Transaction atomic = new DefaultTransaction();
            try {
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriter(typeName, filter, atomic);
                modifyFeatures( attributeNames, attributeValues, writer );                
                atomic.commit();                
            }
            catch( Throwable t ){
                atomic.rollback();   
            }
            finally {
                atomic.close();
            }                        
        }
        else {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriter(typeName, filter, getTransaction() );
            modifyFeatures( attributeNames, attributeValues, writer );            
        }
    }
    protected void modifyFeatures(Name[] names, Object[] values,
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer) throws DataSourceException,
            IOException {
        SimpleFeature feature;        
        try {
        	while (writer.hasNext()) {
                feature = writer.next();

                for (int i = 0; i < names.length; i++) {
                    try {
                        feature.setAttribute(names[i], values[i]);
                    } catch (IllegalAttributeException e) {
                        throw new DataSourceException(
                            "Could not update feature " + feature.getID()
                            + " with " + names[i] + "=" + values[i], e);
                    }
                }

                writer.write();
            }
        } finally {
            writer.close();
        }        
    }

    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(new Name[] { new NameImpl(name), }, new Object[] { attributeValue, }, filter);
    }

    public void modifyFeatures(String[] names, Object[] values, Filter filter) throws IOException {
        Name attributeNames[] = new Name[names.length];
        for (int i = 0; i < names.length; i++) {
            attributeNames[i] = new NameImpl(names[i]);
        }
        modifyFeatures(attributeNames, values, filter);
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
     * @throws IOException
     *
     * @see org.geotools.data.FeatureStore#addFeatures(org.geotools.data.FeatureReader)
     */
    public Set addFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        Set addedFids = new HashSet();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature = null;
        SimpleFeature newFeature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriterAppend(typeName,
                getTransaction());

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
                } catch (IllegalAttributeException writeProblem) {
                    throw new DataSourceException("Could not create "
                        + typeName + " out of provided feature: "
                        + feature.getID(), writeProblem);
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

    public List<FeatureId> addFeatures(FeatureCollection collection) throws IOException {
        List<FeatureId> addedFids= new LinkedList<FeatureId>();
        String typeName = getSchema().getTypeName();
        SimpleFeature feature = null;
        SimpleFeature newFeature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriterAppend(typeName,
                getTransaction());

        Iterator iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                try {
                    feature = (SimpleFeature) iterator.next();
                } catch (Exception e) {
                    throw new DataSourceException("Could not add Features, problem with provided reader",
                        e);
                }

                newFeature = (SimpleFeature)writer.next();

                try {
                	//JD: we may have a case that the source feature type does not 
                	//match exactly the target feature type, so build attributes
                	// based oin target
                    //newFeature.setAttributes(feature.getAttributes(null));
                	Object[] attributes = new Object[ newFeature.getAttributeCount() ];
                	for ( int i = 0; i < attributes.length; i++) {
                		AttributeDescriptor type = 
                			newFeature.getFeatureType().getDescriptor( i );
                		attributes[ i ] = feature.getAttribute( type.getLocalName() );
                	}
                	newFeature.setAttributes( attributes );
                	
                } catch (IllegalAttributeException writeProblem) {
                    throw new DataSourceException("Could not create "
                        + typeName + " out of provided feature: "
                        + feature.getID(), writeProblem);
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
        SimpleFeature feature;

        try {        	
        	
            while (writer.hasNext()) {
                feature = writer.next();
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
     * @throws IOException
     */
    public void setFeatures(FeatureReader <SimpleFeatureType, SimpleFeature> reader) throws IOException {
        String typeName = getSchema().getTypeName();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getDataStore().getFeatureWriter(
                typeName, getTransaction());
        SimpleFeature feature;
        SimpleFeature newFeature;

        try {
            while (writer.hasNext()) {
                feature = writer.next();
		LOGGER.finer("removing feature " + feature);
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
		LOGGER.finer("writing feature " + newFeature);
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
