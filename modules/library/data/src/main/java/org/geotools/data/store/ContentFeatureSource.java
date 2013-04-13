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
package org.geotools.data.store;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Diff;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.TransactionStateDiff;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.sort.SortedFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.function.Collection_AverageFunction;
import org.geotools.filter.function.Collection_BoundsFunction;
import org.geotools.filter.function.Collection_MaxFunction;
import org.geotools.filter.function.Collection_MedianFunction;
import org.geotools.filter.function.Collection_MinFunction;
import org.geotools.filter.function.Collection_SumFunction;
import org.geotools.filter.function.Collection_UniqueFunction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Abstract implementation of FeatureSource.
 * <p>
 * This feature source works off of operations provided by {@link FeatureCollection}.
 * Individual SimpleFeatureCollection implementations are provided by subclasses:
 * <ul>
 *   {@link #all(ContentState)}: Access to entire dataset
 *   {@link #filtered(ContentState, Filter)}: Access to filtered dataset
 * </ul>
 * </p>
 * <p>
 * Even though a feature source is read-only, this class is transaction aware.
 * (see {@link #setTransaction(Transaction)}. The transaction is taken into 
 * account during operations such as {@link #getCount()} and {@link #getBounds()}
 * since these values may be affected by another operation (like writing to 
 * a FeautreStore) working against the same transaction. 
 * </p>
 * <p>
 * Subclasses must also implement the {@link #buildFeatureType()} method which 
 * builds the schema for the feature source.
 * </p>
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public abstract class ContentFeatureSource implements SimpleFeatureSource {
    /**
     * The entry for the feature source.
     */
    protected ContentEntry entry;
    /**
     * The transaction to work from
     */
    protected Transaction transaction;
    /**
     * current feature lock
     */
    protected FeatureLock lock = FeatureLock.TRANSACTION;    
    /**
     * hints
     */
    protected Set<Hints.Key> hints;
    /**
     * The query defining the feature source
     */
    protected Query query;
    /**
     * cached feature type (only set if this instance is a view)
     */
    protected SimpleFeatureType schema;
    
    /**
     * The query capabilities returned by this feature source
     */
    protected QueryCapabilities queryCapabilities;
    
  
    /**
     * Creates the new feature source from a query.
     * <p>
     * The <tt>query</tt> is taken into account for any operations done against
     * the feature source. For example, when getReader(Query) is called the 
     * query specified is "joined" to the query specified in the constructor. 
     * The <tt>query</tt> parameter may be <code>null</code> to specify that the 
     * feature source represents the entire set of features. 
     * </p>
     */
    public ContentFeatureSource(ContentEntry entry, Query query) {
        this.entry = entry;
        this.query = query;
        
        //set up hints
        hints = new HashSet<Hints.Key>();
        hints.add( Hints.JTS_GEOMETRY_FACTORY );
        hints.add( Hints.JTS_COORDINATE_SEQUENCE_FACTORY );
        
        //add subclass specific hints
        addHints( hints );
        
        //make hints unmodifiable
        hints = Collections.unmodifiableSet( hints );
    }
    /**
     * The entry for the feature source.
     */
    public ContentEntry getEntry() {
    	return entry;
    }
    
    /**
     * The current transaction the feature source is working against.
     * <p>
     * This transaction is used to derive the state for the feature source. A
     * <code>null</code> value for a transaction represents the auto commit
     * transaction: {@link Transaction#AUTO_COMMIT}.
     * </p>
     * @see {@link #getState()}.
     */
    public Transaction getTransaction() {
        return transaction;
    }
    
    /**
     * Sets the current transaction the feature source is working against.
     * <p>
     * <tt>transaction</tt> may be <code>null</code>. This signifies that the 
     * auto-commit transaction is used: {@link Transaction#AUTO_COMMIT}.
     * </p>
     * @param transaction The new transaction, or <code>null</code>.
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    /**
     * The current state for the feature source.
     * <p>
     * This value is derived from current transaction of the feature source.
     * </p>
     * 
     * @see {@link #setTransaction(Transaction)}.
     */
    public ContentState getState() {
        return entry.getState(transaction);
    }

    /**
     * The datastore that this feature source originated from.
     * <p>
     * Subclasses may wish to extend this method in order to type narrow its 
     * return type.
     * </p>
     */
    public ContentDataStore getDataStore() {
        return entry.getDataStore();
    }

    /**
     * Indicates if this feature source is actually a view.
     */
    public final boolean isView() {
        return query != null && query != Query.ALL;
    }
    /**
     * A default ResourceInfo with a generic description.
     * <p>
     * Subclasses should override to provide an explicit ResourceInfo
     * object for their content.
     * @return description of features contents
     */
    public ResourceInfo getInfo() {
        return new ResourceInfo(){
            final Set<String> words = new HashSet<String>();
            {
                words.add("features");
                words.add( ContentFeatureSource.this.getSchema().getTypeName() );
            }
            public ReferencedEnvelope getBounds() {
                try {
                    return ContentFeatureSource.this.getBounds();
                } catch (IOException e) {
                    return null;
                }
            }
            public CoordinateReferenceSystem getCRS() {
                return ContentFeatureSource.this.getSchema().getCoordinateReferenceSystem();
            }
    
            public String getDescription() {
                return null;
            }
        
            public Set<String> getKeywords() {
                return words;
            }
    
            public String getName() {
                return ContentFeatureSource.this.getSchema().getTypeName();
            }
    
            public URI getSchema() {
                Name name = ContentFeatureSource.this.getSchema().getName();
                URI namespace;
                try {
                    namespace = new URI( name.getNamespaceURI() );
                    return namespace;                    
                } catch (URISyntaxException e) {
                    return null;
                }                
            }
    
            public String getTitle() {
                Name name = ContentFeatureSource.this.getSchema().getName();
                return name.getLocalPart();
            }
            
        };
    }
    
    /**
     * Returns the same name than the feature type (ie,
     * {@code getSchema().getName()} to honor the simple feature land common
     * practice of calling the same both the Features produces and their types
     * 
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        return getSchema().getName();
    }
    
    /**
     * Returns the feature type or the schema of the feature source.
     * <p>
     * This method delegates to {@link #buildFeatureType()}, which must be 
     * implemented by subclasses. The result is cached in 
     * {@link ContentState#getFeatureType()}.
     * </p>
     */
    public final SimpleFeatureType getSchema() {
        
        //check schema override 
        if ( schema != null ) {
            return schema;
        }
        
        SimpleFeatureType featureType = getAbsoluteSchema();
        
        //this may be a view
        if ( query != null && query.getPropertyNames() != Query.ALL_NAMES) {
            synchronized ( this ) {
                if ( schema == null ) {
                    schema = SimpleFeatureTypeBuilder.retype(featureType, query.getPropertyNames() );
                }
            }
            
            return schema;
        }
        
        return featureType;
    }

    /**
     * Helper method for returning the underlying schema of the feature source.
     * This is a non-view this is the same as calling getSchema(), but in the 
     * view case the underlying "true" schema is returned. 
     */
    protected final SimpleFeatureType getAbsoluteSchema() {
        //load the type from the state shared among feature sources
        ContentState state = entry.getState(transaction);
        SimpleFeatureType featureType = state.getFeatureType();

        if (featureType == null) {
            //build and cache it
            synchronized (state) {
                if (featureType == null) {
                    try {
                        featureType = buildFeatureType();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    state.setFeatureType( featureType);
                }
            }
        }
        
        return featureType;
    }
    
    /**
     * Returns the bounds of the entire feature source.
     * <p>
     * This method delegates to {@link #getBounds(Query)}:
     * <pre>
     *   <code>return getBounds(Query.ALL)</code>.
     * </pre>
     * </p>
     */
    public final ReferencedEnvelope getBounds() throws IOException {
        
        //return all(entry.getState(transaction)).getBounds();
        return getBounds(Query.ALL);
    }

    /**
     * Returns the bounds of the results of the specified query against the 
     * feature source.
     * <p>
     * This method calls through to {@link #getBoundsInternal(Query)} which 
     * subclasses must implement. It also contains optimizations which check 
     * state for cached values.
     * </p>
     */
    public final ReferencedEnvelope getBounds(Query query) throws IOException {
        query = joinQuery( query );
        query = resolvePropertyNames(query);

        //
        //calculate the bounds
        //
        ReferencedEnvelope bounds;
        if(!canTransact() && transaction != null && transaction != Transaction.AUTO_COMMIT) {
            // grab the in memory transaction diff
            DiffTransactionState state = (DiffTransactionState) getTransaction().getState(getEntry());
            Diff diff = state.getDiff();
            
            // don't compute the bounds of the features that are modified or removed in the diff
            Iterator it = diff.getModified().values().iterator();
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Set<FeatureId> modifiedFids = new HashSet<FeatureId>();
            while(it.hasNext()){
                SimpleFeature feature = (SimpleFeature) it.next();
                modifiedFids.add(ff.featureId(feature.getID()));
            }
            Id idFilter = ff.id(modifiedFids);
            Query q = new Query(query);
            q.setFilter(ff.and(idFilter, query.getFilter()));
            bounds = getBoundsInternal(q);
            
            // update with the diff contents, all added feaatures and all modified, not deleted ones
            if(bounds != null) {
                // new ones
                it = diff.getAdded().values().iterator();
                while(it.hasNext()){
                    SimpleFeature feature = (SimpleFeature) it.next();
                    BoundingBox fb = feature.getBounds();
                    if(fb != null) {
                        bounds.expandToInclude(ReferencedEnvelope.reference(fb));
                    }
                }
                
                // modified ones
                it = diff.getModified().values().iterator();
                while(it.hasNext()){
                    SimpleFeature feature = (SimpleFeature) it.next();
                    if(feature != TransactionStateDiff.NULL) {
                        BoundingBox fb = feature.getBounds();
                        if(fb != null) {
                            bounds.expandToInclude(ReferencedEnvelope.reference(fb));
                        }
                    }
                }
            }
        } else {
            bounds = getBoundsInternal(query);
        }
        
        return bounds;
    }

    /**
     * Calculates the bounds of a specified query. Subclasses must implement this
     * method.
     */
    protected abstract ReferencedEnvelope getBoundsInternal(Query query) throws IOException;
    
    /**
     * Returns the count of the number of features of the feature source.
     * <p>
     * This method calls through to {@link #getCount(Query)} which 
     * subclasses must implement. It also contains optimizations which check 
     * state for cached values.
     * </p>
     */
    public final int getCount(Query query) throws IOException {
        query = joinQuery( query );
        query = resolvePropertyNames( query );
        
        //calculate the count
        int count = getCountInternal( query );

        // if the internal actually counted, consider transactions
        if(count >= 0 && !canTransact() && transaction != null && transaction != Transaction.AUTO_COMMIT) {
            DiffTransactionState state = (DiffTransactionState) getTransaction().getState(getEntry());
            Diff diff = state.getDiff();
            synchronized (diff) {
                // consider newly added features that satisfy the filter
                Iterator it = diff.getAdded().values().iterator();
                Filter filter = query.getFilter();
                while(it.hasNext()){
                    Object feature = it.next();
                    if(filter.evaluate(feature)) {
                        count++;
                    }
                        
                }
                
                // consider removed features that satisfy the filter
                it = diff.getModified().values().iterator();
                FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
                Set<FeatureId> modifiedFids = new HashSet<FeatureId>();
                int modifiedPostCount = 0;
                while(it.hasNext()){
                    SimpleFeature feature = (SimpleFeature) it.next();
                    
                    if(feature == TransactionStateDiff.NULL) {
                        count--;
                    } else {
                        modifiedFids.add(ff.featureId(feature.getID()));
                        if(filter.evaluate(feature)) {
                            modifiedPostCount++;
                        }
                    }
                }
                
                // consider the updated feature if any, we need to know how
                // many of those matched the filter before
                if(modifiedFids.size() > 0) {
                    Id idFilter = ff.id(modifiedFids);
                    Query q = new Query(query);
                    q.setFilter(ff.and(idFilter, query.getFilter()));
                    int modifiedPreCount = getCountInternal(q);
                    if(modifiedPreCount == -1) {
                        return -1;
                    } else {
                        count = count + modifiedPostCount - modifiedPreCount;
                    }
                }
            }
        }
        
        return count;
    }

    /**
     * Calculates the number of features of a specified query. Subclasses must 
     * implement this method.
     */
    protected abstract int getCountInternal(Query query) throws IOException;
    
    /**
     * Returns the feature collection of all the features of the feature source.
     */
    public final ContentFeatureCollection getFeatures() throws IOException {
        Query query = joinQuery(Query.ALL);
        return new ContentFeatureCollection( this, query );
    }

    /**
     * Returns a feature reader for all features.
     * <p>
     * This method calls through to {@link #getReader(Query)}.
     * </p>
     */
    public final  FeatureReader<SimpleFeatureType, SimpleFeature> getReader() throws IOException {
        return getReader(Query.ALL);
    }
    
    /**
     * Returns the feature collection if the features of the feature source which 
     * meet the specified query criteria.
     */
    public final ContentFeatureCollection getFeatures(Query query)
        throws IOException {
        query = joinQuery( query );
        
        return new ContentFeatureCollection( this, query );
    }

    /**
     * Returns a reader for the features specified by a query.
     * 
     */
    public final  FeatureReader<SimpleFeatureType, SimpleFeature> getReader(Query query) throws IOException {
        query = joinQuery( query );
        query = resolvePropertyNames(query);
        
        // see if we need to enable native sorting in order to support stable paging
        if (query.getStartIndex() != null
                && (query.getSortBy() == null || query.getSortBy().length == 0)) {
            Query dq = new Query(query);
            dq.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
            query = dq;
        }

        //check for a join
        if (!query.getJoins().isEmpty() && getQueryCapabilities().isJoiningSupported()) {
            throw new IOException("Feature source does not support joins");
        }

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReaderInternal( query );
        
        //
        //apply wrappers based on subclass capabilities
        //
        // transactions
        if( !canTransact() && transaction != null && transaction != Transaction.AUTO_COMMIT) {
            DiffTransactionState state = (DiffTransactionState) getTransaction().getState(getEntry());
            reader = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(reader, state.getDiff());
        }
        
        //filtering
        if ( !canFilter() ) {
            if (query.getFilter() != null && query.getFilter() != Filter.INCLUDE ) {
                reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>( reader, query.getFilter() );
            }    
        }
        
        //retyping
        if ( !canRetype() ) {
            if ( query.getPropertyNames() != Query.ALL_NAMES ) {
                //rebuild the type and wrap the reader
                SimpleFeatureType target = 
                    SimpleFeatureTypeBuilder.retype(getSchema(), query.getPropertyNames());
                
                // do an equals check because we may have needlessly retyped (that is,
                // the subclass might be able to only partially retype)
                if ( !target.equals( reader.getFeatureType() ) ) {
                    reader = new ReTypeFeatureReader( reader, target, false );    
                }
            }
        }
        
        // sorting
        if ( query.getSortBy() != null && query.getSortBy().length != 0 ) {
            if ( !canSort() ) {
                reader = new SortedFeatureReader(DataUtilities.simple(reader), query);
            } 
        }

        
        // offset
        int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
        if( !canOffset() && offset > 0 ) {
            // skip the first n records
            for(int i = 0; i < offset && reader.hasNext(); i++) {
                reader.next();
            }
        }
        
        // max feature limit
        if ( !canLimit() ) {
            if (query.getMaxFeatures() != -1 && query.getMaxFeatures() < Integer.MAX_VALUE ) {
                reader = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(reader, query.getMaxFeatures());
            }    
        }
        
        // reprojection
        if ( !canReproject() ) {
            CoordinateReferenceSystem targetCRS = query.getCoordinateSystemReproject();
            if (targetCRS != null) {
                CoordinateReferenceSystem nativeCRS = reader.getFeatureType().getCoordinateReferenceSystem();
                if(nativeCRS == null) {
                    throw new IOException("Cannot reproject data, the source CRS is not available");
                } else if(!nativeCRS.equals(targetCRS)) {
                    try {
                        reader = new ReprojectFeatureReader(reader, targetCRS);
                    } catch (Exception e) {
                        if(e instanceof IOException)
                            throw (IOException) e;
                        else
                            throw (IOException) new IOException("Error occurred trying to reproject data").initCause(e);
                    }
                }
            }    
        }
        
        // TODO: Use InProcessLockingManager to assert read locks?
        if(!canLock()) {
//            LockingManager lockingManager = getDataStore().getLockingManager();
//            return ((InProcessLockingManager)lockingManager).checkedReader(reader, transaction);
        }
        
        
        return reader;
    }
    
    /**
     * Visit the features matching the provided query.
     * <p>
     * The default information will use getReader( query ) and pass each feature to the provided visitor.
     * Subclasses should override this method to optimise common visitors:
     * <ul>
     * <li> {@link Collection_AverageFunction}
     * <li> {@link Collection_BoundsFunction}
     * <li> (@link Collection_CountFunction}
     * <li> {@link Collection_MaxFunction}
     * <li> {@link Collection_MedianFunction}
     * <li> {@link Collection_MinFunction}
     * <li> {@link Collection_SumFunction}
     * <li> {@link Collection_UniqueFunction}
     * </ul>
     * Often in the case of Filter.INCLUDES the information can be determined from a file header or metadata table.
     * <p>
     * 
     * @param visitor Visitor called for each feature 
     * @param progress Used to report progress; and errors on a feature by feature basis
     * @throws IOException
     */
    public void accepts( Query query, org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) throws IOException {
        
        if( progress == null ) {
            progress = new NullProgressListener();
        }
        

        if ( handleVisitor(query,visitor) ) {
            //all good, subclass handled
            return;
        }

        //subclass could not handle, resort to manually walkign through
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(query);
        try{
            float size = progress instanceof NullProgressListener ? 0.0f : (float) getCount( query );
            float position = 0;
            progress.started();
            while( reader.hasNext() ){
                SimpleFeature feature = null;
                if (size > 0) progress.progress( position++/size );
                try {
                    feature = reader.next();
                    visitor.visit(feature);
                }
                catch( IOException erp ){
                    progress.exceptionOccurred( erp );
                    throw erp;
                }
                catch( Exception unexpected ){
                    progress.exceptionOccurred( unexpected );
                    String fid = feature == null ? "feature" : feature.getIdentifier().toString();
                    throw new IOException("Problem visiting " + query.getTypeName() + " visiting " + fid
                            + ":" + unexpected, unexpected);
                }
            }
        }
        finally {
            progress.complete();            
            reader.close();
        }
    }
    
    /**
     * Subclass method which allows subclasses to natively handle a visitor.
     * <p>
     * Subclasses would override this method and return true in cases where the specific 
     * visitor could be handled without iterating over the entire result set of query. An 
     * example would be handling visitors that calculate aggregate values.
     * </p>
     * @param query The query being made.
     * @param visitor The visitor to 
     * 
     * @return true if the visitor can be handled natively, otherwise false. 
     */
    protected boolean handleVisitor( Query query, FeatureVisitor visitor ) throws IOException {
        return false;
    }
    
    /**
     * Subclass method for returning a native reader from the datastore.
     * <p>
     * It is important to note that if the native reader intends to handle any 
     * of the following natively:
     * <ul>
     *   <li>reprojection</li>
     *   <li>filtering</li>
     *   <li>max feature limiting</li>
     *   <li>sorting</li>
     *   <li>locking</li>
     *   <li>transactions</li>
     * </ul>
     * Then it <b>*must*</b> set the corresponding flags to <code>true</code>:
     * <ul>
     *   <li>{@link #canReproject()} - handles {@link Query#getCoordinateSystemReproject()} internally.
     *       Example would be PostGIS using Proj to handle reproejction internally</li>
     *   <li>{@link #canFilter()} - handles {@link Query#getFilter() internally.</li>
     *   <li>{@link #canLimit()} - handles {@link Query#getMaxFeatures()} and {@link Query#getStartIndex()} internally.</li>
     *   <li>{@link #canSort()} - handles {@link Query#getSortBy()} natively.</li>
     *   <li>{@link #canRetype()} - handles {@link Query#getProperties()} natively. Example would
     *   be only parsing the properties the user asks for from an XML file</li>
     *   <li>{@link #canLock()} - handles read-locks natively</li>
     *   <li>{@link #canTransact()} - handles transactions natively</li>
     * </ul>
     * </p>
     * 
     */
    protected abstract  FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal( Query query ) throws IOException;
    
    /**
     * Determines if the datastore can natively perform reprojection..
     * <p>
     * If the subclass can handle reprojection natively then it should override
     * this method to return <code>true</code>. In this case it <b>must</b> do 
     * the reprojection or throw an exception. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will case the
     * feature reader created by the subclass to be wrapped in a reprojecting 
     * decorator when the query specifies a coordinate system reproject.
     * </p>
     * @see ReprojectFeatureReader
     */
    protected boolean canReproject() {
        return false;
    }
    
    /**
     * Determines if the datastore can natively limit the number of features 
     * returned in a query.
     * <p>
     * If the subclass can handle a map feature cap natively then it should override
     * this method to return <code>true</code>. In this case it <b>must</b> do 
     * the cap or throw an exception. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will case the
     * feature reader created by the subclass to be wrapped in a max feature capping 
     * decorator when the query specifies a max feature cap. 
     * </p>
     * @see MaxFeatureReader
     */
    protected boolean canLimit() {
        return false;
    }
    
    /**
     * Determines if the datastore can natively skip the first <code>offset</code> number of features 
     * returned in a query.
     * <p>
     * If the subclass can handle a map feature cap natively then it should override
     * this method to return <code>true</code>. In this case it <b>must</b> do 
     * the cap or throw an exception. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will case the
     * feature reader created by the subclass to be be accesset offset times before
     * being returned to the caller. 
     * </p>
     */
    protected boolean canOffset() {
        return false;
    }
    
    /**
     * Determines if the datastore can natively perform a filtering.
     * <p>
     * If the subclass can handle filtering natively it should override this method 
     * to return <code>true</code>. In this case it <b>must</b> do the filtering
     * or throw an exception. This includes the case of partial native filtering
     * where the datastore can only handle part of the filter natively. In these
     * cases it is up to the subclass to apply a decorator to the reader it returns
     * which will handle any part of the filter can was not applied natively. See
     * {@link FilteringFeatureReader}. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will cause the
     * feature reader created by the subclass to be wrapped in a filtering feature
     * reader when the query specifies a filter. See {@link FilteringFeatureReader}.
     * </p>
     */
    protected boolean canFilter() {
        return false;
    }
    
    /**
     * Determines if the datasatore can natively perform "retyping" which includes 
     * limiting the number of attributes returned and reordering of those attributes
     * <p>
     * If the subclass can handle retyping natively it should override this method 
     * to return <code>true</code>. In this case it <b>must</b> do the retyping
     * or throw an exception. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will cause the
     * feature reader created by the subclass to be wrapped in a retyping feature
     * reader when the query specifies a retype.
     * </p>
     * @see ReTypeFeatureReader
     */
    protected boolean canRetype() {
        return false;
    }
    
    /**
     * Determines if the datastore can natively perform sorting.
     * <p>
     * If the subclass can handle retyping natively it should override this method 
     * to return <code>true</code>. In this case it <b>must</b> do the retyping
     * or throw an exception. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will cause an 
     * exception to be thrown when the query specifies sorting.
     * @see SortedFeatureReader
     */
    protected boolean canSort() {
        return false;
    }
    
    /**
     * Determines if the store can natively manage transactions.
     * <p>
     * If a subclass can handle transactions natively it should override this
     * method to return <code>true</code> and deal with transactions on its own,
     * including firing feature modifications events.
     * @return true if transaction independence has custom implementation
     */
    protected boolean canTransact() {
        return false;
    }
    
    /**
     * Determines if the store takes responsibility for issuing events.
     * <p>
     * If a subclass issue events (as part of its low level writer implementation)
     * then it should override this method to return true.
     * 
     * @return true if event notification has custom implementation
     */
    protected boolean canEvent() {
        return false;
    }
    
    
    /**
     * Creates a new feature source for the specified query.
     * <p>
     * If the current feature source already has a defining query it is joined
     * to the specified query.
     * </p>
     * @param query
     * @return
     * @throws IOException
     */
    public final ContentFeatureSource getView(Query query) throws IOException {
        query = joinQuery(query);
        query = resolvePropertyNames(query);
        
        //reflectively create subclass
        Class clazz = getClass();
        
        try {
            Constructor c = clazz.getConstructor(ContentEntry.class,Query.class);
            ContentFeatureSource source = (ContentFeatureSource) c.newInstance(getEntry(),query);
            
            //set the transaction
            source.setTransaction( transaction );
            return source;
        } 
        catch( Exception e ) {
            String msg = "Subclass must implement Constructor(ContentEntry,Query)";
            throw (IOException) new IOException( msg ).initCause(e);
        }
    }
    
    /**
     * Returns the feature collection for the features which match the specified
     * filter.
     * <p>
     * This method calls through to {@link #getFeatures(Query)}.
     * </p>
     */
    public final ContentFeatureCollection getFeatures(Filter filter)
        throws IOException {
        return getFeatures( new Query( getSchema().getTypeName(), filter ) );
    }
    
    /**
     * Returns a reader for features specified by a particular filter.
     * <p>
     * This method calls through to {@link #getReader(Query)}.
     * </p>
     */
    public final  FeatureReader<SimpleFeatureType, SimpleFeature> getReader(Filter filter) throws IOException {
        return getReader( new Query( getSchema().getTypeName(), filter ));
    }

    public final ContentFeatureSource getView(Filter filter) throws IOException {
        return getView( new Query( getSchema().getTypeName(), filter ) );
    }
    
    /**
     * Adds an listener or observer to the feature source.
     * <p>
     * Listeners are stored on a per-transaction basis. 
     * </p>
     */
    public final void addFeatureListener(FeatureListener listener) {
        entry.getState(transaction).addListener(listener);
    }

    /**
     * Removes a listener from the feature source.
     */
    public final void removeFeatureListener(FeatureListener listener) {
        entry.getState(transaction).removeListener(listener);
    }

    /**
     * The hints provided by the feature store.
     * <p>
     * Subclasses should implement {@link #addHints(Set)} to provide additional
     * hints.
     * </p>
     * 
     * @see FeatureSource#getSupportedHints()
     */
    public final Set getSupportedHints() {
        return hints;
    }
    //
    // Internal API
    //
    /**
     * Subclass hook too add additional hints.
     * <p>
     * By default, the followings are already present:
     * <ul>
     *   <li>{@link Hints#JTS_COORDINATE_SEQUENCE_FACTORY}
     *   <li>{@link Hints#JTS_GEOMETRY_FACTORY}
     * </ul>
     * 
     * </p>
     * @param hints The set of hints supported by the feature source.
     */
    protected void addHints( Set<Hints.Key> hints ) {
        
    }
 
    /**
     * Convenience method for joining a query with the definining query of the 
     * feature source.
     */
    protected Query joinQuery( Query query ) {
        // join the queries
        return DataUtilities.mixQueries(this.query, query, null);
    }

    /**
     * This method changes the query object so that all propertyName references are resolved
     * to simple attribute names against the schema of the feature source.
     * <p>
     * For example, this method ensures that propertyName's such as "gml:name" are rewritten as
     * simply "name".
     *</p>
     */
    protected Query resolvePropertyNames( Query query ) {
        return DataUtilities.resolvePropertyNames(query, getSchema() );
    }
    
    /** Transform provided filter; resolving property names */
    protected Filter resolvePropertyNames( Filter filter ) {
        return DataUtilities.resolvePropertyNames(filter, getSchema() );
    }
    /**
     * Creates the feature type or schema for the feature source.
     * <p>
     * Implementations should use {@link SimpleFeatureTypeBuilder} to build the 
     * feature type. Also, the builder should be injected with the feature factory
     * which has been set on the datastore (see {@link ContentDataStore#getFeatureFactory()}.
     * Example:
     * <pre>
     *   SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
     *   b.setFeatureTypeFactory( getDataStore().getFeatureTypeFactory() );
     *   
     *   //build the feature type
     *   ...
     * </pre>
     * </p>
     */
    protected abstract SimpleFeatureType buildFeatureType() throws IOException;
    
    /**
     * Builds the query capabilities for this feature source. The default 
     * implementation returns a newly built QueryCapabilities, subclasses
     * are advised to build their own.
     * @return
     * @throws IOException
     */
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities();
    }

    /**
     * SimpleFeatureCollection optimized for read-only access.
     * <p>
     * Available via getView( filter ):
     * <ul>
     * <li>getFeatures().sort( sort )
     * <li>getFeatures( filter ).sort( sort )
     * </ul>
     * <p>
     * In particular this method of data access is intended for rendering and other high speed
     * operations; care should be taken to optimize the use of FeatureVisitor.
     * <p>
     * @param transactionState
     * @param filter
     * @return readonly access
     */

    //protected abstract SimpleFeatureCollection readonly(ContentState state, Filter filter);

    public QueryCapabilities getQueryCapabilities() {
        // lazy initialization, so that the subclass has all its data structures ready
        // when the method is called (it might need to consult them in order to decide
        // what query capabilities are really supported)
        if(queryCapabilities == null) {
            queryCapabilities = buildQueryCapabilities();
        }
        
        // we have to glaze the subclass query capabilities since we always support offset
        // and we support more sorting cases using the sorting wrappers
        return new QueryCapabilities() {
            public boolean isOffsetSupported() {
                // we always support offset since we support sorting
                return true;
            }

            public boolean supportsSorting(SortBy[] sortAttributes) {
                if(queryCapabilities.supportsSorting(sortAttributes)) {
                    // natively supported
                    return true;
                } else {
                    // check if we can use the merge-sort support
                    return SortedFeatureReader.canSort(getSchema(), sortAttributes);
                }
            }

            public boolean isReliableFIDSupported() {
                return queryCapabilities.isReliableFIDSupported();
            }
            
            public boolean isUseProvidedFIDSupported() {
                return queryCapabilities.isUseProvidedFIDSupported();
            }
        };
    }
    
    // ----------------------------------------------------- 
    //  Locking support
    // -----------------------------------------------------
    
    /**
     * Sets the feature lock of the feature store.
     */
    public final void setFeatureLock(FeatureLock lock) {
        if(canLock()) lock = processLock(lock);
        this.lock = lock;
    }

    /**
     * Locks all features.
     * <p>
     * This method calls through to {@link #lockFeatures(Filter)}.
     * </p>
     */
    public final int lockFeatures() throws IOException {
        return lockFeatures(Filter.INCLUDE);
    }
    
    /**
     * Locks features specified by a query.
     * <p>
     * This method calls through to {@link #lockFeatures(Filter)}.
     * </p>
     */
    public final int lockFeatures(Query query) throws IOException {
        return lockFeatures( query.getFilter() );
    }

    /**
     * Locks features specified by a filter.
     */
    public final int lockFeatures(Filter filter) throws IOException {
        Logger logger = getDataStore().getLogger();
        
        String typeName = getSchema().getTypeName(); 
        
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(filter);
        try {
            int locked = 0;
            while( reader.hasNext() ) {
                SimpleFeature feature = reader.next();
                try {
                    //
                    // Use native locking?
                    //
                    if(canLock()) {
                        doLockInternal(typeName,feature);
                    } else {
                        getDataStore().getLockingManager()
                            .lockFeatureID(typeName, feature.getID(), transaction, lock);
                    }
                    
                    logger.fine( "Locked feature: " + feature.getID() );
                    locked++;
                }
                catch( FeatureLockException e ) {
                    //ignore
                    String msg = "Unable to lock feature:" + feature.getID() + "." + 
                        " Change logging to FINEST for stack trace";
                    logger.fine( msg );
                    logger.log( Level.FINEST, "Unable to lock feature: " + feature.getID(), e );
                }
            }
            
            return locked;
        }
        finally {
            reader.close();
        }
    }

    /**
     * Unlocks all features.
     * <p>
     * This method calls through to {@link #unLockFeatures(Filter)}.
     * </p>
     * 
     */
    public final void unLockFeatures() throws IOException {
        unLockFeatures(Filter.INCLUDE);
    }
    
    /**
     * Unlocks features specified by a query.
     * <p>
     * This method calls through to {@link #unLockFeatures(Filter)}.
     * </p>
     * 
     */
    public final void unLockFeatures(Query query) throws IOException {
        unLockFeatures(query.getFilter());
    }
    
    /**
     * Unlocks features specified by a filter.
     */
    public final void unLockFeatures(Filter filter) throws IOException {
        filter = resolvePropertyNames(filter);
        String typeName = getSchema().getTypeName(); 
        
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(filter);
        try {
            while( reader.hasNext() ) {
                SimpleFeature feature = reader.next();
                //
                // Use native locking?
                //
                if(canLock()) {
                    doLockInternal(typeName,feature);
                } else {
                    getDataStore().getLockingManager()
                        .unLockFeatureID(typeName, feature.getID(), transaction, lock);
                }
            }
        }
        finally {
            reader.close();
        }
    }    

    /**
     * Determines if the {@link #getDataStore() datastore} can perform 
     * feature locking natively.
     * <p>
     * If {@link #getWriterInternal(Query, int)} returns a 
     * {@link FeatureWriter feature writer} that supports feature 
     * locking natively, it should override this method 
     * to return <code>true</code>. 
     * </p>
     * <p>
     * Not overriding this method or returning <code>false</code> will cause 
     * {@link ContentFeatureStore} to use the {@link InProcessLockingManager}
     * to return a {@link #getWriter FeatureWriter} that honors locks.
     */
    protected boolean canLock() {
        return false;
    }   
    
    /**
     * If the subclass implements native locking, this 
     * method is invoked before the feature lock is 
     * (re)assigned to this store.
     * @param lock - a {@link FeatureLock} instance
     * @return a processed {@link FeatureLock} instance
     */
    protected FeatureLock processLock(FeatureLock lock) {
        return lock;
    }
    
    /**
     * This method must be implemented overridden when
     * native locking is indicated by {@link #canLock()}.
     * @param typeName {@link SimpleFeature} type name
     * @param feature {@link SimpleFeature} instance
     */
    protected void doLockInternal(String typeName, SimpleFeature feature) throws IOException {
        throw new UnsupportedOperationException("native locking not implemented");
    }
    
    /**
     * This method must be implemented overridden when
     * native locking is indicated by {@link #canLock()}.
     * @param typeName {@link Feature} type name
     * @param feature {@link Feature} instance
     */
    protected void doUnlockInternal(String typeName, SimpleFeature feature) throws IOException {
        throw new UnsupportedOperationException("native locking not implemented");
    }    
    
}
