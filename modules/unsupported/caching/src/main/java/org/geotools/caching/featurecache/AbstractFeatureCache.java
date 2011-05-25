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
package org.geotools.caching.featurecache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.caching.CacheOversizedException;
import org.geotools.caching.util.BBoxFilterSplitter;
import org.geotools.caching.util.CacheUtil;
import org.geotools.data.DataAccess;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.OrImpl;
import org.geotools.filter.spatial.BBOXImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Abstract implementation of a feature cache.
 * <p>
 * This implementation uses envelopes to deal with determining what
 * has been added to the cache already and what items need to be
 * retrieved from the feature source.
 * </p>
 * 
 *
 *
 * @source $URL$
 */
public abstract class AbstractFeatureCache implements FeatureCache, FeatureListener {

    protected static Logger logger;
    protected static FilterFactory ff;

    static {
        ff = CommonFactoryFinder.getFilterFactory2(null);
        logger = org.geotools.util.logging.Logging.getLogger("org.geotools.caching");
    }

    protected SimpleFeatureSource fs;             //the feature source that backs the given feature type
    
    //statistics about the cache
    protected int source_hits = 0;
    protected int source_feature_reads = 0;
    
    //lock for reading/writing to cache
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Creates a new feature cache from the given feature source.
     * 
     * @param fs
     */
    public AbstractFeatureCache(SimpleFeatureSource fs) {
        this.fs = fs;
        fs.addFeatureListener(this);
    }

    /**
     * Adds a feature listener
     */
    public void addFeatureListener(FeatureListener listener) {
        this.fs.addFeatureListener(listener);
    }

    /**
     * Returns the DataAccess of the underlying feature source
     */
    public DataAccess getDataStore() {
        return this.fs.getDataStore();
    }

    /**
     * Gets all features from that are contained within the feature source.
     */
    public SimpleFeatureCollection getFeatures() throws IOException {
        return this.getFeatures(Filter.INCLUDE);
    }


    /**
     * Gets all features which satisfy the given query.
     * <p>This version gets all the features from the cache and
     * datastore and combines them in a memory feature collection.</p> 
     * <p>This should probably be overwritten for any implementations.</p>
     */
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
    	String typename = query.getTypeName();
        String schemaname = this.getSchema().getTypeName();
        if ((query.getTypeName() != null)
                && (typename != schemaname)) {
            return new EmptyFeatureCollection(this.getSchema());
        } else {
            SimpleFeatureCollection fc = getFeatures(query.getFilter());
            if (fc.size() == 0) {
                return new EmptyFeatureCollection(this.getSchema());
            }

            //filter already applied so we really don't need to re-apply it
            //just include all
            query = new DefaultQuery(query.getTypeName(), query.getNamespace(), Filter.INCLUDE, query.getMaxFeatures(), query.getPropertyNames(), query.getHandle());

            //below is probably not the best way to apply a query
            //to a feature collection; but I don't know a better way.
            
            // now that we have a feature collection we need to wrap it
            // in a datastore so we can apply the query to it.
            // in this case we'll use a memory data store
            MemoryDataStore md = new MemoryDataStore();

            //add the features
            //we are using an array because 
            //making a memory data store from a feature collection results
            //in a null pointer exception
            ArrayList<SimpleFeature> features = new ArrayList<SimpleFeature>();
            SimpleFeatureIterator it = fc.features();
            try{
                for( ; it.hasNext(); ) {
                    SimpleFeature type = (SimpleFeature) it.next();
                    features.add(type);
                }
            }finally{
                it.close();
            }
            md.addFeatures(features.toArray(new SimpleFeature[features.size()]));

            //convert back to a feature collection with the query applied
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = md.getFeatureReader(query, Transaction.AUTO_COMMIT);
            SimpleFeatureCollection fc1 = new DefaultFeatureCollection("cachedfeaturecollection", (SimpleFeatureType) fr.getFeatureType());
            while( fr.hasNext() ) {
                fc1.add(fr.next());
            }
            fr.close();

            return fc1;
        }
    }

    /**
     * Get all features with satisfy a given filter.
     * <p>This version gets all features from the cache and from memory and combines
     * them in a memory feature collection.</p>
     * <p>This should probably be overwritten for any implementations.</p>
     */
    public SimpleFeatureCollection getFeatures(Filter filter)
        throws IOException {
    	    	
        /* PostPreProcessFilterSplittingVisitor may return
           a mixture of logical filters (or, and, not) and bbox filters,
           and for now I do not know how to handle this */

        //PostPreProcessFilterSplittingVisitor splitter = new PostPreProcessFilterSplittingVisitor(caps, this.fs.getSchema(), null) ;
        /* so we use this splitter which will return a single BBOX */
        BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        filter.accept(splitter, null);

        Filter spatial_restrictions = splitter.getFilterPre();
        Filter other_restrictions = splitter.getFilterPost();

        if (spatial_restrictions == Filter.EXCLUDE){
            //nothing to get
            return new EmptyFeatureCollection(this.getSchema());
        }else if (spatial_restrictions == Filter.INCLUDE ) {
            // we could not isolate any spatial restriction
            // delegate to source
            return this.fs.getFeatures(filter);
        } else {
            SimpleFeatureCollection fc;

            try {
                // first pre-process query from cache
                fc = _getFeatures(spatial_restrictions);
            } catch (UnsupportedOperationException e) {
                logger.log(Level.WARNING, "Querying cache : " + e.toString());
                return this.fs.getFeatures(filter);
            }

            // refine result set before returning
            return fc.subCollection(other_restrictions);
        }
    }

    /**
     * Gets features with satisfy a given filter.
     *
     * @param filter  must be a BBOXImpl filter
     * @return
     * @throws IOException
     */
    protected SimpleFeatureCollection _getFeatures(Filter filter)
        throws IOException {
        if (filter instanceof BBOXImpl) {
            //return _getFeatures((BBOXImpl) filter) ;
            return get(CacheUtil.extractEnvelope((BBOXImpl) filter)).subCollection(filter);
        } else {
            throw new UnsupportedOperationException("Cannot handle given filter :" + filter);
        }
    }

    /**
     * Get all features within a given envelope as a in memory feature collection.
     */
    public SimpleFeatureCollection get(Envelope e) throws IOException {
    	SimpleFeatureCollection fromCache;
        SimpleFeatureCollection fromSource;
        List<Envelope> notcached = null;
        
        String geometryname = getSchema().getGeometryDescriptor().getLocalName();
        String srs = getSchema().getGeometryDescriptor().getCoordinateReferenceSystem().toString();

        //      acquire R-lock
        lock.readLock().lock();
        try {
            notcached = match(e);
            if (notcached.isEmpty()) { // everything in cache
                // return result from cache
                fromCache = peek(e);
                return fromCache;
            }
        } finally {
            lock.readLock().unlock();
        }

        // got a miss from cache, need to get more data
        lock.writeLock().lock();
        try {
            notcached = match(e); // check again because another thread may have inserted data in between
            if (notcached.isEmpty()) {
                fromCache = peek(e);
                return fromCache;
            }
            
            //get items from cache
            fromCache = peek(e);
             
            // get what data we are missing from the cache based on the not cached array.
            Filter filter = null;
            if (notcached.size() == 1){
                Envelope env = notcached.get(0);
                filter = ff.bbox(geometryname, env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), srs);
            }else{
                //or the envelopes together into a single or filter
                ArrayList<Filter> filters = new ArrayList<Filter>(notcached.size());
                for (Iterator<Envelope> it = notcached.iterator(); it.hasNext();) {
                    Envelope next = (Envelope) it.next();
                    Filter bbox = ff.bbox(geometryname, next.getMinX(), next.getMinY(), next.getMaxX(), next.getMaxY(), srs);
                    filters.add(bbox);
                }
                filter = ff.or(filters);
            }
            
            //get the data from the source
            try{
                //cache these features in a local feature collection while we deal with them
                SimpleFeatureCollection localSource = new MemoryFeatureCollection(getSchema());
                fromSource = this.fs.getFeatures(filter);
                localSource.addAll(fromSource);
                fromSource = localSource;
            }catch (Exception ex){
                //something happened getting data from source
                //return what we have from the cache
                logger.log(Level.INFO, "Error getting data for cache from source feature store.", ex);
                return fromCache;
            }
            
            //update stats
            source_hits++;
            source_feature_reads += fromSource.size();

            fromCache.addAll(fromSource);
            
            //add it to the cache; 
            try {
                isOversized(fromSource);
                try{
                    register(filter); // get notice we discovered some new part of the universe
                    // add new data to cache - will raise an exception if cache is over sized
                    //here we are adding the everything (include the stuff that's already in the cache
                    //which is done to prevent multiple wfs calls 
                    put(fromCache);  
                    
                }catch (Exception ex){
                    //something happened here so we better unregister this area
                    //so if we try again next time we'll try getting data again
                    unregister(filter);
                }
            } catch (CacheOversizedException e1) {
                logger.log(Level.INFO, "Adding data to cache : " + e1.toString());
            }

        } finally {
            lock.writeLock().unlock();
        }
        
        return fromCache;
    }

    /**
     * Returns a list of envelopes that are not in the cache but
     * within the given bounds.
     *
     * @param e
     * @return
     */
    protected abstract List<Envelope> match(Envelope e);

    /**
     * Registers a given bounding box filter with the cache. 
     * <p>This informs the cache that it has data
     * for the given area.</p>
     *
     * @param f
     */
    protected void register(BBOXImpl f){
        register(CacheUtil.extractEnvelope(f));
    }

    /**
     * Registers a given envelope with the cache. 
     * <p>This informs the cache that it has data
     * for the given area.</p>
     *
     * @param e
     */
    protected abstract void register(Envelope e);

    /**
     * Unregisters a given envelope with the cache.
     * <p>This informs the cache that there is no data for the 
     * given envelope or that the data is incomplete.</p>
     *
     * @param e
     */
    protected abstract void unregister(Envelope e);
    
    /**
     * Unregisters a given bounding box filter with the cache.
     * <p>This informs the cache that there is no data for the 
     * given envelope or that the data is incomplete.</p>
     *
     * @param e
     */
    protected void unregister(BBOXImpl f){
        unregister(CacheUtil.extractEnvelope(f));
    }
    
    /**
     * Unregisters filter from the cache.
     * <p>
     * Filter needs to be an instance of BBOXImpl or a collection
     * of BBOXImpls that are combined using or.  Any other combination
     * will result in a UnsupportedOperationException.
     * </p>
     *
     * @param f
     */
    public void unregister(Filter f){
        if (f instanceof OrImpl) {
            for (Iterator<Filter> it = ((OrImpl)f).getChildren().iterator(); it.hasNext();) {
                Filter child = (Filter) it.next();
                unregister(child);
            }
        } else if (f instanceof BBOXImpl) {
            unregister((BBOXImpl) f);
        } else {
            throw new UnsupportedOperationException("Do not know how to handle this filter" + f);
        }
    }
    
    /**
     * Determines if the given feature collection is too big to put into the
     * cache.
     *
     * @param fc
     * @throws CacheOversizedException if feature collection is to big for cache
     */
    protected abstract void isOversized(FeatureCollection fc)
        throws CacheOversizedException;

    /**
     * Returns the feature schema of the underlying feature source.
     */
    public SimpleFeatureType getSchema() {
        return (SimpleFeatureType)this.fs.getSchema();
    }

    /**
     * Removes a feature listener.
     */
    public void removeFeatureListener(FeatureListener listener) {
        this.fs.removeFeatureListener(listener);
    }

    /**
     * This event is fired when the features in the feature source
     * have changed.
     * 
     */
    public void changed(FeatureEvent event) {
        // TODO: what to do if change is outside of root mbr ?
        // implementations should handle this case
        // add an abstract checkForOutOfBoundsEvent(event) ?
        remove(event.getBounds());
    }

    
    /**
     * Registers filter from the cache.
     * <p>
     * Filter needs to be an instance of BBOXImpl or a collection
     * of BBOXImpls that are combined using or.  Any other combination
     * will result in a UnsupportedOperationException.
     * </p>
     *
     * @param f
     */
    public void register(Filter f) {
        if (f instanceof OrImpl) {
            for (Iterator<Filter> it = ((OrImpl)f).getChildren().iterator(); it.hasNext();) {
                Filter child = (Filter) it.next();
                register(child);
            }
        } else if (f instanceof BBOXImpl) {
            register((BBOXImpl) f);
        } else {
            throw new UnsupportedOperationException("Do not know how to handle this filter" + f);
        }
    }

    /**
     * Returns a string containing statistics about 
     * data source reading.
     * <p>Stats include: Source hits and Feature Source Reads</p>
     *
     * @return
     */
    public String sourceAccessStats() {
        StringBuffer sb = new StringBuffer();
        sb.append("Source hits = " + source_hits);
        sb.append(" ; Feature reads = " + source_feature_reads);

        return sb.toString();
    }

    /**
     * Gets the feature cache statistics
     * @return
     */
    public abstract String getStats();

    /**
     * Gets a read lock
     */
    public void readLock() {
        lock.readLock().lock();
    }

    /**
     * Unlocks the read lock
     */
    public void readUnLock() {
        lock.readLock().unlock();
    }

    /**
     * Gets a write lock
     */
    public void writeLock() {
        lock.writeLock().lock();
    }

    /**
     * unlocks the write lock
     */
    public void writeUnLock() {
        lock.writeLock().unlock();
    }

    /**
     * @return the resource info from the main feature source being cached
     */
    public ResourceInfo getInfo() {
        return fs.getInfo();
    }

    /**
     * @return the name from the main feature source being cached
     */
    public Name getName() {
        //pass along to existing feature source
        return fs.getName();
    }

    /**
     * @return the query capabilities from the main feature source being cached
     */
    public QueryCapabilities getQueryCapabilities() {
        // pass along to feature source
        return fs.getQueryCapabilities();
    }
}



