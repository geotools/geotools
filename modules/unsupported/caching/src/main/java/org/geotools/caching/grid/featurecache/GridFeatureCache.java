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
package org.geotools.caching.grid.featurecache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.caching.CacheOversizedException;
import org.geotools.caching.FeatureCollectingVisitor;
import org.geotools.caching.featurecache.AbstractFeatureCache;
import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.spatialindex.GridInvalidatingVisitor;
import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.SpatialIndex;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.caching.util.CacheUtil;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * An implementation of a feature cache.
 * 
 * <p>This implementation holds a write lock on the
 * cache while it access features from the feature source.  As
 * a result during this time no other features can read or write
 * to the cache.</p>
 * 
 *
 * @source $URL$
 */
public class GridFeatureCache extends AbstractFeatureCache {
	protected int max_tiles = 10;			//number of tiles to insert data into before written to "root" node
	
    protected GridSpatialIndex tracker;		//spatial index
    protected int capacity;					// maximum number of features to cache

    /**
     * @param FeatureStore from which to cache features
     * @param indexcapacity = number of tiles in index
     * @param capacity = max number of features to cache; Integer.MAX_VALUE will cache all features
     * @throws FeatureCacheException
     * @throws IOException 
     */
    public GridFeatureCache(SimpleFeatureSource fs, int indexcapacity, int capacity, Storage store)
        throws FeatureCacheException {
        
        this(fs, getFeatureBounds(fs), indexcapacity, capacity, store);   
    }

    
    /**
     * Creates a new grid feature cache.
     * @param fs            FeatureStore from which to cache features
     * @param env           The size of the feature cache; once defined features outside this bounds cannot be added to the featurestore/cache
     * @param gridsize  	number of tiles in the index
     * @param capacity       maximum number of features to cache
     * @param store         the cache storage
     */
    public GridFeatureCache(SimpleFeatureSource fs, ReferencedEnvelope env, int gridsize, int capacity, Storage store){
        super(fs);
        tracker = new GridSpatialIndex(CacheUtil.convert(env), gridsize, store, capacity);
        this.capacity = capacity;

        //lets compare the feature type in the store to the feature type of the current feature source
        //if they differ we need to  clear the cache as the features have changed.
        if (store.getFeatureTypes().size() == 0){
            store.addFeatureType(fs.getSchema());
        }else if (store.getFeatureTypes().size() ==1){
            SimpleFeatureType sft = (SimpleFeatureType)store.getFeatureTypes().iterator().next();
            if (!sft.equals(fs.getSchema()) && fs.getSchema() != null){
                tracker.clear();
                store.clearFeatureTypes();
                store.addFeatureType(fs.getSchema());
            }
        }else{
            //we have multiple feature types; really this shouldn't happen
            tracker.clear();
            store.clearFeatureTypes();
            store.addFeatureType(fs.getSchema());
        }
        
        //setup bounds
        if (env != null && !env.equals(store.getBounds())){
            store.setBounds(env);
        }
        
        //flush cache here to write to disk
        tracker.flush();
    }
    
    /**
     * returns the spatial index that is used to implement the cache
     * @return
     */
    public SpatialIndex getIndex(){
        return this.tracker;
    }
    
    /**
     * Private function used to get the bounds of a feature collection and convert the IOException
     * to a FeatureCacheException
     * 
     * @param fs
     * @return
     * @throws FeatureCacheException
     */
    protected static ReferencedEnvelope getFeatureBounds(FeatureSource fs) throws FeatureCacheException{
        try{
            return fs.getBounds();
        }catch(IOException ex){
            throw new FeatureCacheException(ex);
        }
    }

    /**
     * Returns the feature schema of the underlying feature source; checks the feature
     * store first to see if it's in there
     */
    @Override
    public SimpleFeatureType getSchema() {
        if (tracker.getStorage().getFeatureTypes().size() == 1){
            return (SimpleFeatureType)tracker.getStorage().getFeatureTypes().iterator().next();
        }
        //we don't really know what's going on so let's just return
        //what we have cached
        return (SimpleFeatureType)this.fs.getSchema();
    }

    /**
     * This function looks in the cache for missing tiles.
     * 
     * @param e
     * @return if there are more than 10 missing tiles than a single envelope is returned that
     * encompasses all missing tiles; otherwise only the envelopes for the tiles
     * that are missing are returned. 
     */
    public List<Envelope> match(Envelope e) {
        Region search = CacheUtil.convert(e);
        ArrayList<Envelope> missing = new ArrayList<Envelope>();

        if (!this.tracker.getRootNode().getShape().intersects(search)){
            //this request is outside of the cached area so nothing to be found
            return new ArrayList<Envelope>();
        }
        if (!this.tracker.getRootNode().getShape().contains(search)) { 
            // query is partially outside of root mbr;  we limit our search to the inside of the root mbr
            Envelope r = CacheUtil.convert((Region) this.tracker.getRootNode().getShape());
            r = r.intersection(e);
            search = CacheUtil.convert(r);
        }

        List<NodeIdentifier>[] tiles = tracker.findMissingTiles(search);
        List<NodeIdentifier> missing_tiles = tiles[0];
        
        if (missing_tiles.size() > max_tiles) {
        	Envelope env = new Envelope(e);
        	for (Iterator<NodeIdentifier> it = missing_tiles.iterator(); it.hasNext();) {
        		NodeIdentifier id = it.next();
                Envelope nextenv = CacheUtil.convert((Region) (id.getShape()));
                env.expandToInclude(nextenv);
        	}
            missing.add(env);
        } else {
            for (Iterator<NodeIdentifier> it = missing_tiles.iterator(); it.hasNext();) {
            	NodeIdentifier id = it.next();
                Region next = (Region) (id.getShape());
                missing.add(CacheUtil.convert(next));
            }
        }

        return missing;
    }

    /**
     * Converts and envelope into a list of nodes that
     * the envelope covers.  This returns array that contains
     * two lists; the first is all the nodes that intersect the envelope
     * and are missing from the cache; the second contains all the nodes that intersect
     * the envelope and are present in the cache.
     *
     * @param e envelope to search
     * 
     * @return list of two arrays {missing nodes, present nodes}
     */
    public List<NodeIdentifier>[] matchNodeIds(Envelope e) {
        Region search = CacheUtil.convert(e);

        if (!this.tracker.getRootNode().getShape().intersects(search)){
            //this request is outside of the cached area so nothing to be found or missing
            return new List[]{Collections.emptyList(), Collections.emptyList()};
        }
        if (!this.tracker.getRootNode().getShape().contains(search)) { 
            // query is partially outside of root mbr;  we limit our search to the inside of the root mbr
            Envelope r = CacheUtil.convert((Region) this.tracker.getRootNode().getShape());
            r = r.intersection(e);
            search = CacheUtil.convert(r);
        }

        List<NodeIdentifier>[] tiles = tracker.findMissingTiles(search);
        List<NodeIdentifier> missing_tiles = tiles[0];
        List<NodeIdentifier> found_tiles = tiles[1];
        return new List[]{missing_tiles, found_tiles};
    }
    
    /**
     * Clears the cache.
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            tracker.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Disposes of the cache.
     */
    public void dispose(){
        lock.writeLock().lock();
        try{
            this.tracker.dispose();
        }finally{
            lock.writeLock().unlock();
        }
    }

    /**
     * Looks in the cache for any elements within the given envelope.
     * <p>Returns a in-memory feature collection.</p>
     */
    public SimpleFeatureCollection peek(Envelope e) {

        FeatureCollectingVisitor v = new FeatureCollectingVisitor(this.getSchema());
        lock.readLock().lock();

        try {
            this.tracker.intersectionQuery(CacheUtil.convert(e), v);
        } finally {
            lock.readLock().unlock();
        }

        return v.getCollection();
    }

    /**
     * Adds a feature collection to the cache.
     */
    public void put(SimpleFeatureCollection fc, Envelope e) throws CacheOversizedException {
        isOversized(fc);
        lock.writeLock().lock();
        try {
            //put then register put fails
            register(e);
            put(fc);
        }catch (Exception ex){
            unregister(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void isOversized(FeatureCollection fc) throws CacheOversizedException {
        if (this.capacity != Integer.MAX_VALUE && fc.size() > this.capacity) {
            throw new CacheOversizedException("Cannot cache collection of size " + fc.size()
                + " (capacity = " + capacity + " )");
        }
    }

    /**
     * Removes an area from the cache.
     */
    public void remove(Envelope e) {
    	GridInvalidatingVisitor v = new GridInvalidatingVisitor(this.tracker);
        lock.writeLock().lock();
        try {
            if (e == null){
                e = getBounds();//no envelope specified so assume everything
            }
            this.tracker.intersectionQuery(CacheUtil.convert(e), v); //invalidates nodes
        }catch (IOException ex) {
        	logger.log(Level.SEVERE, "Error removing elements from the cache.", ex);
            //
        } finally {
            lock.writeLock().unlock();
        }
        this.tracker.flush();
    }

    /**
     * Gets the bounds of the cache.
     * <p>This maybe be larger than the data bounds.</p>
     */
    public ReferencedEnvelope getBounds() throws IOException {
        CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem(); 
        return new ReferencedEnvelope ( CacheUtil.convert((Region) this.tracker.getRootNode().getShape()), crs);
    }

    /**
     * Gets the bounds of the data that match a given query.
     * <p>This function is passed along to the feature source.</p>
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return this.fs.getBounds(query);
    }
    
    /**
     * Gets the count of the data that match a given query.
     * <p>This function is passed along to the feature source.</p>
     */
    public int getCount(Query query) throws IOException {
        return this.fs.getCount(query);
    }

    /**
     * Adds a feature collection to the cache.
     * 
     * @throws CacheOversizedException if the feature collection has too many features for the cache.
     */
    public void put(SimpleFeatureCollection fc) throws CacheOversizedException {
        isOversized(fc);
        lock.writeLock().lock();
        try {
            SimpleFeatureIterator it = fc.features();
            try{
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    this.tracker.insertData(f, CacheUtil.convert((Envelope)f.getBounds()));
                }
            }finally{
                fc.close(it);
            }
        } finally {
            lock.writeLock().unlock();
        }
        //flush cache at this point to write to store
        this.tracker.flush();
    }

    /**
     * Registers a given envelope in the cache.  All nodes within this envelope
     * are flagged as valid.
     */
    protected void register(Envelope e) {
        Region r = CacheUtil.convert(e);
        ValidatingVisitor v = new ValidatingVisitor(r);

        lock.writeLock().lock();
        try {
            //we don't want to track access while we register; this will ensure that just because a node touches another node it
            //isn't considered an access; it might be better to improve the way containment query is done.
            boolean recordaccess = this.tracker.getDoRecordAccess();
            this.tracker.setDoRecordAccess(false);
            try{
                this.tracker.containmentQuery(r, v);
            }finally{
                this.tracker.setDoRecordAccess(recordaccess);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Registers a collection of nodes as valid.
     * @param nodes
     */
    public void register(Collection<NodeIdentifier> nodes) {
        lock.writeLock().lock();
        try {
            
            //we don't want to track access while we register; this will ensure that just because a node touches another node it
            //isn't considered an access; it might be better to improve the way containment query is done.
            boolean recordaccess = this.tracker.getDoRecordAccess();
            try{
                this.tracker.setDoRecordAccess(false);
                for( Iterator<NodeIdentifier> iterator = nodes.iterator(); iterator.hasNext(); ) {
                    NodeIdentifier nodeIdentifier = (NodeIdentifier) iterator.next();
                    nodeIdentifier.setValid(true);
                }
            }finally{
                this.tracker.setDoRecordAccess(recordaccess);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Un-registers a collection of nodes.
     * @param nodes
     */
    public  void unregister(Collection<NodeIdentifier> nodes) {
        lock.writeLock().lock();
        try {
            //we don't want to track access while we register; this will ensure that just because a node touches another node it
            //isn't considered an access; it might be better to improve the way containment query is done.
            boolean recordaccess = this.tracker.getDoRecordAccess();
            try{
                this.tracker.setDoRecordAccess(false);
                for( Iterator<NodeIdentifier> iterator = nodes.iterator(); iterator.hasNext(); ) {
                    NodeIdentifier nodeIdentifier = (NodeIdentifier) iterator.next();
                    nodeIdentifier.setValid(false);
                }
            }finally{
                this.tracker.setDoRecordAccess(recordaccess);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Unregisters all nodes in a given envelope.
     */
    protected void unregister(Envelope e){
        Region r = CacheUtil.convert(e);
        GridInvalidatingVisitor v = new GridInvalidatingVisitor(this.tracker, r);
        
        lock.writeLock().lock();
        try {
            this.tracker.containmentQuery(r, v);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("GridFeatureCache [");
        sb.append(" Source = " + this.fs);
        sb.append(" Capacity = " + this.capacity);
        sb.append(" Nodes = " + this.tracker.getStatistics().getNumberOfNodes());
        sb.append(" ]");
        sb.append("\n" + tracker.getIndexProperties());

        return sb.toString();
    }

    public Set getSupportedHints() {
        return new HashSet();
    }

    public String getStats() {
        StringBuffer sb = new StringBuffer();
        sb.append(tracker.getStatistics().toString());
        sb.append("\n" + sourceAccessStats());

        return sb.toString();
    }
}
