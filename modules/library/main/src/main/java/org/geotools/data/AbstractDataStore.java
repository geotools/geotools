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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;


/**
 * Represents a stating point for implementing your own DataStore.
 *
 * <p>
 * The goal is to have this class provide <b>everything</b> else if you can
 * only provide:
 * </p>
 *
 * <ul>
 * <li>
 * String[] getFeatureTypes()
 * </li>
 * <li>
 * FeatureType getSchema(String typeName)
 * </li>
 * <li>
 *  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader( typeName )
 * </li>
 * <li>
 * FeatureWriter getFeatureWriter( typeName )
 * </li>
 * </ul>
 *
 * and optionally this protected methods to allow custom query optimizations:
 *
 * <ul>
 * <li>
 * Filter getUnsupportedFilter(String typeName, Filter filter)
 * </li>
 * <li>
 *  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName, Query query)
 * </li>
 * </ul>
 *
 * <p>
 * All remaining functionality is implemented against these methods, including
 * Transaction and Locking Support. These implementations will not be optimal
 * but they will work.
 * </p>
 *
 * <p>
 * Pleae note that there may be a better place for you to start out from, (like
 * JDBCDataStore).
 * </p>
 *
 * @author jgarnett
 * @source $URL$
 */
public abstract class AbstractDataStore implements DataStore {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data");

    /** Manages listener lists for SimpleFeatureSource implementation */
    public FeatureListenerManager listenerManager = new FeatureListenerManager();

    /**
     * Flags AbstractDataStore to allow Modification.
     * <p>
     * GetFeatureSource will return a FeatureStore is this is true.
     * </p>
     */
    protected final boolean isWriteable;

    /**
     * Manages InProcess locks for FeatureLocking implementations.
     *
     * <p>
     * May be null if subclass is providing real locking.
     * </p>
     */
    private InProcessLockingManager lockingManager;

    /** Default (Writeable) DataStore */
    public AbstractDataStore() {
        this(true);
    }

    /**
     * AbstractDataStore creation.
     * 
     * @param isWriteable true for writeable DataStore. 
     */
    public AbstractDataStore(boolean isWriteable) {
        this.isWriteable = isWriteable;
        lockingManager = createLockingManager();
    }

    /**
     * Currently returns an InProcessLockingManager.
     *
     * <p>
     * Subclasses that implement real locking may override this method to
     * return <code>null</code>.
     * </p>
     *
     * @return InProcessLockingManager or null.
     */
    protected InProcessLockingManager createLockingManager() {
        return new InProcessLockingManager();
    }

//    public void fireAdded( Feature newFeature ){
//        String typeName = newFeature.getFeatureType().getTypeName();
//        listenerManager.fireFeaturesAdded( typeName, Transaction.AUTO_COMMIT, newFeature.getBounds(), false );
//    }
//    public void fireRemoved( Feature removedFeature ){
//        String typeName = removedFeature.getFeatureType().getTypeName();
//        listenerManager.fireFeaturesRemoved( typeName, Transaction.AUTO_COMMIT, removedFeature.getBounds(), false );
//    }
//    public void fireChanged( Feature before, Feature after ){
//        String typeName = after.getFeatureType().getTypeName();
//        Envelope bounds = new Envelope();
//        bounds.expandToInclude( before.getBounds() );
//        bounds.expandToInclude( after.getBounds() );
//        listenerManager.fireFeaturesChanged( typeName, Transaction.AUTO_COMMIT, bounds, false );
//    }
           
    /**
     * Subclass override to provide access to metadata.
     * <p>
     * CreateTypeEntry uses this method to aquire metadata information,
     * if available.
     * </p>
     */
    protected Map createMetadata( String typeName ) {
        return Collections.EMPTY_MAP;
    }
       
    /** helper method for retrieving all the names. */
    public abstract String[] getTypeNames() throws IOException;

    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from "+getClass().getSimpleName() );
        info.setSchema( FeatureTypes.DEFAULT_NAMESPACE );        
        return info;
    }
    
    /** Retrive schema information for typeName */
    public abstract SimpleFeatureType getSchema(String typeName)
        throws IOException;

    /**
     * Subclass must implement.
     *
     * @param typeName
     *
     * @return  FeatureReader<SimpleFeatureType, SimpleFeature> over contents of typeName
     * 
     */
    protected abstract  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName)
        throws IOException;
    /**
     * Subclass can implement this to provide writing support.
     *
     * @param typeName
     *
     * @return FeatureWriter over contents of typeName
     * @throws IOException 
     *
     * @throws IOException Subclass may throw IOException
     * @throws UnsupportedOperationException Subclass may implement
     * @deprecated
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName) throws IOException{
        throw new UnsupportedOperationException("FeatureWriter not implemented - is this a Read-only DataStore?");    	
    }

    /**
     * Subclass should implement this to provide writing support.
     * <p>A feature writer writes to the resource so it should considered to always be committing.
     * The transaction is passed in so that it can be known what FeatureListeners should be notified of the
     * changes.  If the Transaction is AUTOCOMMIT then all listeners should be notified.  If not
     * all listeners that are NOT registered with that transaction should be notified.<p>
     * @param typeName
     * @param transaction a feature writer
     * @return FeatureWriter over contents of typeName
     * @throws IOException 
     *
     * @throws IOException Subclass may throw IOException
     * @throws UnsupportedOperationException Subclass may implement
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> createFeatureWriter(String typeName, Transaction transaction)
    throws IOException {
        throw new UnsupportedOperationException("FeatureWriter not supported");    	
    }
    /**
     * Subclass should implement to provide writing support.
     *
     * @param featureType Requested FeatureType
     * @throws IOException 
     *
     * @throws IOException Subclass may throw IOException
     * @throws UnsupportedOperationException Subclass may implement
     */
    public void createSchema(SimpleFeatureType featureType) throws IOException{
        throw new UnsupportedOperationException("Schema creation not supported");
    }
    /* (non-Javadoc)
     * @see org.geotools.data.DataStore#updateSchema(java.lang.String, org.geotools.feature.FeatureType)
     */
    public void updateSchema(String typeName, SimpleFeatureType featureType){
        throw new UnsupportedOperationException("Schema modification not supported");
    }

    /**
     * Default implementation based on getFeatureReader and getFeatureWriter.
     *
     * <p>
     * We should be able to optimize this to only get the RowSet once
     * </p>
     *
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public SimpleFeatureSource getFeatureSource(final String typeName)
        throws IOException {
        final SimpleFeatureType featureType = getSchema(typeName);

        if (isWriteable) {
            if (lockingManager != null)
                return new AbstractFeatureLocking(getSupportedHints()) {
                    public DataStore getDataStore() {
                        return AbstractDataStore.this;
                    }
                    public String toString() {
                        return "AbstractDataStore.AbstractFeatureLocking("+typeName+")";
                    }
                    public void addFeatureListener(FeatureListener listener) {
                        listenerManager.addFeatureListener(this, listener);
                    }

                    public void removeFeatureListener(
                        FeatureListener listener) {
                        listenerManager.removeFeatureListener(this, listener);
                    }

                    public SimpleFeatureType getSchema() {
                        return featureType;
                    }
                };
                return new AbstractFeatureStore(getSupportedHints()) {
                    public DataStore getDataStore() {
                        return AbstractDataStore.this;
                    }
                    public String toString() {
                        return "AbstractDataStore.AbstractFeatureStore("+typeName+")";
                    }
                    public void addFeatureListener(FeatureListener listener) {
                        listenerManager.addFeatureListener(this, listener);
                    }

                    public void removeFeatureListener(
                        FeatureListener listener) {
                        listenerManager.removeFeatureListener(this, listener);
                    }

                    public SimpleFeatureType getSchema() {
                        return featureType;
                    }
                };
        }
            return new AbstractFeatureSource(getSupportedHints()) {
                public DataStore getDataStore() {
                    return AbstractDataStore.this;
                }
                public String toString() {
                    return "AbstractDataStore.AbstractFeatureSource("+typeName+")";
                }
                public void addFeatureListener(FeatureListener listener) {
                    listenerManager.addFeatureListener(this, listener);
                }

                public void removeFeatureListener(FeatureListener listener) {
                    listenerManager.removeFeatureListener(this, listener);
                }

                public SimpleFeatureType getSchema() {
                    return featureType;
                }
            };
    }


    // Jody - Recomend moving to the following
    // When we are ready for CoordinateSystem support
    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,Transaction transaction) throws IOException {
        Filter filter = query.getFilter();
        String typeName = query.getTypeName();
        String propertyNames[] = query.getPropertyNames();

        if (filter == null) {
            throw new NullPointerException("getFeatureReader requires Filter: "
                + "did you mean Filter.INCLUDE?");
        }
        if( typeName == null ){
            throw new NullPointerException(
                "getFeatureReader requires typeName: "
                + "use getTypeNames() for a list of available types");
        }
        if (transaction == null) {
            throw new NullPointerException(
                "getFeatureReader requires Transaction: "
                + "did you mean to use Transaction.AUTO_COMMIT?");
        }
        SimpleFeatureType featureType = getSchema( query.getTypeName() );

        if( propertyNames != null || query.getCoordinateSystem()!=null ){
            try {
                featureType = DataUtilities.createSubType( featureType, propertyNames, query.getCoordinateSystem() );
            } catch (SchemaException e) {
                LOGGER.log( Level.FINEST, e.getMessage(), e);
                throw new DataSourceException( "Could not create Feature Type for query", e );

            }
        }
        if ( filter == Filter.EXCLUDE || filter.equals( Filter.EXCLUDE )) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(featureType);
        }
        //GR: allow subclases to implement as much filtering as they can,
        //by returning just it's unsupperted filter
        filter = getUnsupportedFilter(typeName, filter);
        if(filter == null){
            throw new NullPointerException("getUnsupportedFilter shouldn't return null. Do you mean Filter.INCLUDE?");
        }

        // There are cases where the readers have to lock.  Take shapefile for example.  Getting a Reader causes
        // the file to be locked.  However on a commit TransactionStateDiff locks before a writer is obtained.  In order to 
        // prevent deadlocks either the diff has to obtained first or the reader has to be obtained first.
        // Because shapefile writes to a buffer first the actual write lock is not flipped until the transaction has most of the work
        // done.  As a result I suggest getting the diff first then getting the reader.
        // JE
        Diff diff=null;
        if (transaction != Transaction.AUTO_COMMIT) {
            TransactionStateDiff state = state(transaction);
            if( state != null ){
                diff = state.diff(typeName);
            }
        }
        
        // This calls our subclass "simple" implementation
        // All other functionality will be built as a reader around
        // this class
        //
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = getFeatureReader(typeName, query);

        if( diff!=null )
            reader = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(reader, diff, query.getFilter());

        if (!filter.equals( Filter.INCLUDE ) ) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, filter);
        }

        if (!featureType.equals(reader.getFeatureType())) {
            LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
            reader = new ReTypeFeatureReader(reader, featureType, false);
        }

        if (query.getMaxFeatures() != Query.DEFAULT_MAX) {
			    reader = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(reader, query.getMaxFeatures());
        }

        return reader;
    }

    /**
     * GR: this method is called from inside getFeatureReader(Query ,Transaction )
     * to allow subclasses return an optimized  FeatureReader<SimpleFeatureType, SimpleFeature> wich supports the
     * filter and attributes truncation specified in <code>query</code>
     * <p>
     * A subclass that supports the creation of such an optimized FeatureReader
     * shold override this method. Otherwise, it just returns
     * <code>getFeatureReader(typeName)</code>
     * <p>
     */
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName, Query query)
    throws IOException
    {
      return getFeatureReader(typeName);
    }
    /**
     * GR: if a subclass supports filtering, it should override this method
     * to return the unsupported part of the passed filter, so a
     * FilteringFeatureReader will be constructed upon it. Otherwise it will
     * just return the same filter.
     * <p>
     * If the complete filter is supported, the subclass must return <code>Filter.INCLUDE</code>
     * </p>
     */
    protected Filter getUnsupportedFilter(String typeName, Filter filter)
    {
      return filter;
    }

    /**
     * Used to retrive the TransactionStateDiff for this transaction.
     * If you subclass is doing its own thing (ArcSDE I am talking to
     * you) then you should arrange for this method to return null.
     * <p>
     * By default a TransactionStateDiff will be created that holds
     * any changes in memory.
     * <p>
     * @param transaction
     * @return TransactionStateDiff or null if subclass is handling differences
     */
    protected TransactionStateDiff state(Transaction transaction) {
        synchronized (transaction) {
            TransactionStateDiff state = (TransactionStateDiff) transaction
                .getState(this);

            if (state == null) {
                state = new TransactionStateDiff(this);
                transaction.putState(this, state);
            }

            return state;
        }
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String, org.geotools.filter.Filter, org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Filter filter,
        Transaction transaction) throws IOException {
        if (filter == null) {
            throw new NullPointerException("getFeatureReader requires Filter: "
                + "did you mean Filter.INCLUDE?");
        }

        if (filter == Filter.EXCLUDE) {
        	SimpleFeatureType featureType = getSchema(typeName);

            return new EmptyFeatureWriter(featureType);
        }

        if (transaction == null) {
            throw new NullPointerException(
                "getFeatureWriter requires Transaction: "
                + "did you mean to use Transaction.AUTO_COMMIT?");
        }

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        if (transaction == Transaction.AUTO_COMMIT) {
        	try{
        		writer = createFeatureWriter(typeName, transaction);
        	}catch (UnsupportedOperationException e) {
				// This is for backward compatibility.
            	try {
            		writer = getFeatureWriter(typeName);
            	}
            	catch( UnsupportedOperationException eek){
            		throw e; // throw original - our fallback did not work
            	}
			}
        } else {
            TransactionStateDiff state = state(transaction);
            if( state != null ){
                writer = state.writer(typeName, filter);
            }
            else {
                throw new UnsupportedOperationException("Subclass sould implement");
            }
        }

        if (lockingManager != null) {
            // subclass has not provided locking so we will
            // fake it with InProcess locks
            writer = lockingManager.checkedWriter(writer, transaction);
        }

        if (filter != Filter.INCLUDE) {
            writer = new FilteringFeatureWriter(writer, filter);
        }

        return writer;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String, org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
        Transaction transaction) throws IOException {

    	return getFeatureWriter(typeName, Filter.INCLUDE, transaction);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStore#getFeatureWriterAppend(java.lang.String, org.geotools.data.Transaction)
     *
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
        Transaction transaction) throws IOException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getFeatureWriter(typeName, transaction);

        while (writer.hasNext()) {
            writer.next(); // Hmmm this would be a use for skip() then?
        }

        return writer;
    }

    /**
     * Locking manager used for this DataStore.
     *
     * <p>
     * By default AbstractDataStore makes use of InProcessLockingManager.
     * </p>
     *
     *
     * @see org.geotools.data.DataStore#getLockingManager()
     */
    public LockingManager getLockingManager() {
        return lockingManager;
    }

    /**
     * Computes the bounds of the features for the specified feature type that
     * satisfy the query provided that there is a fast way to get that result.
     * <p>
     * Will return null if there is not fast way to compute the bounds. Since
     * it's based on some kind of header/cached information, it's not guaranteed
     * to be real bound of the features
     * </p>
     * @param query
     * @return the bounds, or null if too expensive
     * @throws SchemaNotFoundException 
     * @throws IOException
     */
    protected ReferencedEnvelope getBounds(Query query) throws IOException{
        return null; // too expensive
    }


    /**
     * Gets the number of the features that would be returned by this query for
     * the specified feature type.
     * <p>
     * If getBounds(Query) returns <code>-1</code> due to expense consider
     * using <code>getFeatures(Query).getCount()</code> as a an alternative.
     * </p>
     *
     * @param query Contains the Filter and MaxFeatures to find the bounds for.
     * @return The number of Features provided by the Query or <code>-1</code>
     *         if count is too expensive to calculate or any errors or occur.
     * @throws IOException 
     *
     * @throws IOException if there are errors getting the count
     */
    protected int getCount(Query query) throws IOException{
        return -1; // too expensive
    }
    
    /**
     * If you are using the automated FeatureSource/Store/Locking creation, this method
     * allows for the specification of the supported hints. 
     * @return
     */
    protected Set getSupportedHints() {
        return Collections.EMPTY_SET;
    }
    
    /**
     * Dummy implementation, it's a no-op. Subclasses holding to system resources must
     * override this method and release them.
     */
    public void dispose() {
        // nothing to do
    }
        
    /**
     * Delegates to {@link #getFeatureSource(String)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the
     * returned Names have no namespace set.
     * 
     * @since 2.5
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

    /**
     * Delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getSchema(Name)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    /**
     * Delegates to {@link #updateSchema(String, SimpleFeatureType)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }
}
