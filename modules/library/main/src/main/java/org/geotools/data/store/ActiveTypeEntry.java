/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.AbstractFeatureLocking;
import org.geotools.data.AbstractFeatureSource;
import org.geotools.data.AbstractFeatureStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Diff;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.EmptyFeatureWriter;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.feature.FeatureCollection;

import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.DelegateFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SimpleInternationalString;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Starting place for holding information about a FeatureType.
 * <p>
 * Like say for instance the FeatureType, its metadata and so on.
 * </p>
 * <p>
 * The default implemenation should contain enough information to wean
 * us off of AbstractDataStore. That is it should provide its own locking
 * and event notification.
 * </p>
 * <p>
 * There is a naming convention:
 * <ul>
 * <li> data access follows bean conventions: getTypeName(), getSchema()
 * <li> resource access methods follow Collections conventions reader(), 
 * writer(), etc...
 * <li> overrrides are all protected and follow factory conventions:
 *  createWriter(), createAppend(), createFeatureSource(),
 *  createFeatureStore(), etc...
 * </ul>
 * <li>
 * </p>
 * <p>
 * Feedback:
 * <ul>
 * <li>even notification yes
 * <li>locking not - locking needs to be rejuggled
 * <li>naming convention really helps when subclassing
 * </ul>
 * </p>
 * 
 * @author jgarnett
 * @source $URL$
 */
public abstract class ActiveTypeEntry implements TypeEntry {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.store");
    
    /**
     * Remember parent.
     * <p>
     * We only refer to partent as a DataSource to keep hacks down.
     * </p>
     */
    protected DataStore parent;
    private final SimpleFeatureType schema;    
    private final Map metadata;    
    private FeatureListenerManager listeners = new FeatureListenerManager();
    
    /** Cached count */
    int count;
    
    /** Cached bounds */
    Envelope bounds; 
    
    public ActiveTypeEntry( DataStore parent, SimpleFeatureType schema, Map metadata ) {
        this.schema = schema;
        this.metadata = metadata;
        this.parent = parent;
        
        // TODO: Should listen to events and empty the cache when edits occur
        
    }

    /**
     * TODO summary sentence for getDisplayName ...
     * 
     * @see org.geotools.data.TypeEntry#getDisplayName()
     */
    public InternationalString getDisplayName() {
        return new SimpleInternationalString( schema.getTypeName() );
    }

    /**
     * TODO summary sentence for getDescription ...
     * 
     * @see org.geotools.data.TypeEntry#getDescription()
     */
    public InternationalString getDescription() {
        return null;
    }

    /**
     * TODO summary sentence for getFeatureType ...
     * 
     * @see org.geotools.data.TypeEntry#getFeatureType()
     * @throws IOException
     */
    public SimpleFeatureType getFeatureType() {
        return schema;
    }
    /**
     * Bounding box for associated Feature Collection, will be calcualted as needed.
     * <p>
     * Note bounding box is returned in lat/long - the coordinate system of the default geometry
     * is used to provide this reprojection.
     * </p>
     */
//    public synchronized Envelope getBounds() {        
//        if( bounds != null ) {
//            bounds = createBounds();            
//        }
//        return bounds;        
//    }
    /**
     * Override to provide your own optimized calculation of bbox.
     * <p>
     * Default impelmenation uses the a feature source.
     * 
     * @return BBox in lat long
     */
//    protected Envelope createBounds() {
//        Envelope bbox;
//        try {
//            FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
//            bbox = source.getBounds();
//            if( bbox == null ){
//                bbox = source.getFeatures().getBounds();
//            }
//            try {
//                CoordinateReferenceSystem cs = source.getSchema().getDefaultGeometry().getCoordinateSystem();
//                bbox = JTS.toGeographic(bbox,cs);
//            }
//            catch (Error badRepoject ) {
//                badRepoject.printStackTrace();
//            }
//        } catch (Exception e) {
//            bbox = new Envelope();
//        }        
//        return bbox;        
//    }
    
    /**
     * TODO summary sentence for getCount ...
     * 
     * @see org.geotools.data.TypeEntry#getCount()
     */
//    public int getCount() {
//        if( count != -1 ) return count;
//        try {
//            FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
//            count = source.getCount( Query.ALL );
//            if( count == -1 ){
//                count = source.getFeatures().size();
//            }
//        } catch (IOException e) {
//            bounds = new Envelope();
//        }
//        return count;
//    }
 
    /**
     * Get unique data name for this CatalogEntry.
     * 
     * @return namespace:typeName
     */
//    public String getDataName() {
//        return schema.getNamespace().toString() + ":"+ schema.getTypeName();
//    }
//    
//    public Object resource() {
//        try {
//            return getFeatureSource();
//        } catch (IOException e) {
//            return null;
//        }
//    }
    
    /**
     * Metadata names from metadata.keySet().
     * 
     * @return metadata names mentioned in metadata.keySet();
     */
    public String[] getMetadataNames() {
        return (String[]) metadata.keySet().toArray( new String[ metadata.size() ] );
    }
    /**
     * Map of metadata by name.
     * 
     * @return Map of metadata by name
     */
    public Map metadata() {
        return Collections.unmodifiableMap( metadata );
    }
    
    /** Manages listener lists for FeatureSource<SimpleFeatureType, SimpleFeature> implementation */
    public FeatureListenerManager listenerManager = new FeatureListenerManager();
    
    //
    // Start of TypeEntry framework
    //
    public String getTypeName() {
        return schema.getTypeName();
    }
//    public FeatureType getSchema() {
//        return schema;
//    }
    /**
     * Create a new FeatueSource allowing interaction with content.
     * <p>
     * Subclass may optionally implement:
     * <ul>
     * <li>FeatureStore - to allow read/write access
     * <li>FeatureLocking - for locking support
     * </ul>
     * This choice may even be made a runtime (allowing the api
     * to represent a readonly file).
     * </p>
     * <p>
     * Several default implemenations are provided
     * 
     * @return FeatureLocking allowing access to content.
     */
//    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() throws IOException {        
//        return createFeatureSource();
//    }
    /**
     * Access a  FeatureReader<SimpleFeatureType, SimpleFeature> providing access to Feature information.
     * <p>
     * This implementation passes off responsibility to the following overrideable methods:
     * <ul>
     * <li>getFeatureReader(String typeName) - subclass *required* to implement
     * </ul>
     * </p>
     * <p>If you can handle some aspects of Query natively (say expressions or reprojection) override the following:
     * <li>
     * <li>getFeatureReader(typeName, query) - override to handle query natively
     * <li>getUnsupportedFilter(typeName, filter) - everything you cannot handle natively
     * <li>getFeatureReader(String typeName) - you must implement this, but you could point it back to getFeatureReader( typeName, Query.ALL );
     * </ul>
     * </p>
     */
    public  FeatureReader<SimpleFeatureType, SimpleFeature> reader(Query query, Transaction transaction) throws IOException {

    	if ( transaction == null ) {
    		throw new NullPointerException( "Transaction null, did you mean Transaction.AUTO_COMMIT" );
    	}
    	
    	FeatureCollection<SimpleFeatureType, SimpleFeature> features = createFeatureSource().getFeatures( query );
    	FeatureReader<SimpleFeatureType, SimpleFeature> reader = new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>(
			features.getSchema(), features.features()
    	);
    	
    	//wrap in diff reader if transaction specified
    	if ( !transaction.equals( Transaction.AUTO_COMMIT) ) {
    		reader = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>( reader, state( transaction ).diff() );
    	}
    	
    	return reader;
    	
//        Filter filter = query.getFilter();
//        String typeName = query.getTypeName();
//        String propertyNames[] = query.getPropertyNames();
//
//        if (filter == null) {
//            throw new NullPointerException("getFeatureReader requires Filter: "
//                + "did you mean Filter.INCLUDE?");
//        }
//        if( typeName == null ){
//            throw new NullPointerException(
//                "getFeatureReader requires typeName: "
//                + "use getTypeNames() for a list of available types");
//        }
//        if (transaction == null) {
//            throw new NullPointerException(
//                "getFeatureReader requires Transaction: "
//                + "did you mean to use Transaction.AUTO_COMMIT?");
//        }
//        FeatureType featureType = schema;
//
//        if( propertyNames != null || query.getCoordinateSystem() != null ){
//            try {
//                featureType = DataUtilities.createSubType( featureType, propertyNames, query.getCoordinateSystem() );
//            } catch (SchemaException e) {
//                LOGGER.log( Level.FINEST, e.getMessage(), e);
//                throw new DataSourceException( "Could not create Feature Type for query", e );
//
//            }
//        }
//        if ( filter == Filter.EXCLUDE || filter.equals( Filter.EXCLUDE )) {
//            return new EmptyFeatureReader(featureType);
//        }
//        //GR: allow subclases to implement as much filtering as they can,
//        //by returning just it's unsupperted filter
//        filter = getUnsupportedFilter( filter);
//        if(filter == null){
//            throw new NullPointerException("getUnsupportedFilter shouldn't return null. Do you mean Filter.INCLUDE?");
//        }
//
//        // This calls our subclass "simple" implementation
//        // All other functionality will be built as a reader around
//        // this class
//        //
//         FeatureReader<SimpleFeatureType, SimpleFeature> reader = createReader( query);
//
//        if (!filter.equals( Filter.INCLUDE ) ) {
//            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, filter);
//        }
//
//        if (transaction != Transaction.AUTO_COMMIT) {
//            Diff diff = state(transaction).diff();
//            reader = new DiffFeatureReader(reader, diff);
//        }
//
//        if (!featureType.equals(reader.getFeatureType())) {
//            LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
//            reader = new ReTypeFeatureReader(reader, featureType);
//        }
//
//        if (query.getMaxFeatures() != Query.DEFAULT_MAX) {
//                reader = new MaxFeatureReader(reader, query.getMaxFeatures());
//        }
//        return reader;
    }

    TypeDiffState state(Transaction transaction) {
        synchronized (transaction) {
            TypeDiffState state = (TypeDiffState) transaction.getState(this);

            if (state == null) {
                state = new TypeDiffState(this);
                transaction.putState(this, state);
            }

            return state;
        }
    }    
    public void fireAdded( SimpleFeature newFeature, Transaction transaction ){
        ReferencedEnvelope bounds = 
            ReferencedEnvelope.reference(newFeature != null ? newFeature.getBounds() : null);
        listenerManager.fireFeaturesAdded( schema.getTypeName(), transaction, bounds, false );
    }
    public void fireRemoved( SimpleFeature removedFeature, Transaction transaction ){
        ReferencedEnvelope bounds = 
            ReferencedEnvelope.reference(removedFeature != null ? removedFeature.getBounds() : null);
        listenerManager.fireFeaturesRemoved( schema.getTypeName(), transaction, bounds, false );
    }
    public void fireChanged( SimpleFeature before, SimpleFeature after, Transaction transaction ){
        String typeName = after.getFeatureType().getTypeName();
        ReferencedEnvelope bounds = new ReferencedEnvelope();
        bounds.include( before.getBounds() );
        bounds.include( after.getBounds() );
        listenerManager.fireFeaturesChanged( typeName, transaction, bounds, false );
    }    
    //
    // Start of Overrides
    //    
    public abstract FeatureSource<SimpleFeatureType, SimpleFeature> createFeatureSource();
    
    /**
     * Override to provide readonly access
     * @param schema
     * @return FeatureSource<SimpleFeatureType, SimpleFeature> backed by this TypeEntry.
     */
//    protected FeatureSource<SimpleFeatureType, SimpleFeature> createFeatureSource() {
//        return new AbstractFeatureSource() {
//            public DataStore getDataStore() {
//                return parent;
//            }
//            public void addFeatureListener( FeatureListener listener ) {
//                listeners.addFeatureListener( this, listener );
//            }
//            public void removeFeatureListener( FeatureListener listener ) {
//                listeners.addFeatureListener( this, listener );
//            }
//            public FeatureType getSchema() {
//                return schema;
//            }
//        };
//    }
    
    /**
     * Create the FeatureSource, override for your own custom implementation.
     * <p>
     * Default implementation makes use of DataStore getReader( ... ), and listenerManager.
     * </p>
     */
    protected FeatureSource<SimpleFeatureType, SimpleFeature> createFeatureSource( final SimpleFeatureType featureType ) {
        return new AbstractFeatureSource() {
            public DataStore getDataStore() {
                return parent;
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
    
    /**
     * Create the FeatureStore, override for your own custom implementation.
     */
    protected FeatureStore<SimpleFeatureType, SimpleFeature> createFeatureStore() {
        
        // This implementation needs FeatureWriters to work
        // please provide your own override for a datastore that does not 
        // support FeatureWriters (like WFS).
        //
        return new AbstractFeatureStore() {
            public DataStore getDataStore() {
                return parent;
            }

            public void addFeatureListener(FeatureListener listener) {
                listenerManager.addFeatureListener(this, listener);
            }

            public void removeFeatureListener(
                FeatureListener listener) {
                listenerManager.removeFeatureListener(this, listener);
            }

            public SimpleFeatureType getSchema() {
                return schema;
            }
        };
    }
    /**
     * Create the FeatureLocking, override for your own custom implementation.
     * <p>
     * Warning: The default implementation of this method uses lockingManger.
     * You must override this method if you support your own locking system (like WFS).
     * <p>
     */
    protected FeatureLocking<SimpleFeatureType, SimpleFeature> createFeatureLocking() {
        return new AbstractFeatureLocking() {
            public DataStore getDataStore() {
                return parent;
            }

            public void addFeatureListener(FeatureListener listener) {
                listenerManager.addFeatureListener(this, listener);
            }

            public void removeFeatureListener(
                FeatureListener listener) {
                listenerManager.removeFeatureListener(this, listener);
            }

            public SimpleFeatureType getSchema() {
                return schema;
            }
        };
    }

    /**
     * Create a reader for this query.
     * <p>
     * Subclass must override this to actually aquire content.
     * </p>
     * @param typeName
     * @param query
     * @return  FeatureReader<SimpleFeatureType, SimpleFeature> for all content
     */
    public  FeatureReader<SimpleFeatureType, SimpleFeature> createReader() {
        return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>( schema );
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
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> createReader(Query query)
    throws IOException
    {
      return createReader();
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
    protected Filter getUnsupportedFilter( Filter filter )
    {
      return filter;
    }
    
    /* (non-Javadoc)
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String, org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> writer( Transaction transaction) throws IOException {        
        if (transaction == null) {
            throw new NullPointerException(
                "getFeatureWriter requires Transaction: "
                + "did you mean to use Transaction.AUTO_COMMIT?");
        }

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        if (transaction == Transaction.AUTO_COMMIT) {
            writer = createWriter();
        } else {
            writer = state(transaction).writer();
        }

        if (parent.getLockingManager() != null &&
            parent.getLockingManager() instanceof InProcessLockingManager ) {
            // subclass has not provided locking so we will
            // fake it with InProcess locks
            InProcessLockingManager lockingManger = (InProcessLockingManager) parent.getLockingManager();            
            writer = lockingManger.checkedWriter(writer, transaction);            
        }
        return writer;
    }
    
    /**
     * Low level feature writer access.
     * <p>
     * This is the only method you must implement to aquire content.
     * </p>
     * @return Subclass must supply a FeatureWriter
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> createWriter() {
        return new EmptyFeatureWriter( schema );
    }
    
    /**
     * It would be great to kill this method, and add a "skipToEnd" method to featureWriter?
     * <p>
     * Override this if you can provide a native optimization for this.
     * (aka copy file, open the file in append mode, replace origional on close).
     * </p>
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> createAppend( Transaction transaction) throws IOException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer( transaction );
        while (writer.hasNext()) {
            writer.next(); // Hmmm this would be a use for skip() then?
        }
        return writer;
    }
       
}
