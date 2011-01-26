package org.geotools.caching.grid.featurecache;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import org.geotools.caching.CacheOversizedException;
import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.featurecache.readers.GridCachingFeatureCollection;
import org.geotools.caching.grid.spatialindex.NodeLockInvalidatingVisitor;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.caching.util.BBoxFilterSplitter;
import org.geotools.caching.util.CacheUtil;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Implementation of a GridFeatureCache that uses
 * streaming feature collections.  Feature are streamed out of the cache
 * and the feature source only when requested.  Features streams out of the 
 * feature source are cached as they are read.
 * 
 * <p>This is a read only implementation; you cannot add
 * features to this cache.</p>
 * <p>This implementation of a feature cache locks the cache at the node level
 * not the entire cache.  So as long as you are accessing different nodes you should
 * be able to write to and read from the cache at the same time.
 * </p>
 * <p>Only supports SimpleFeatureType & SimpleFeature</p>
 * 
 * @author Emily
 * @since 1.2.0
 *
 * @source $URL$
 */
public class StreamingGridFeatureCache extends GridFeatureCache {

    
    public StreamingGridFeatureCache(SimpleFeatureSource fs, int indexcapacity, int capacity, Storage store)  throws FeatureCacheException {
		super(fs, indexcapacity, capacity, store);
	}
    
	public StreamingGridFeatureCache(SimpleFeatureSource fs, ReferencedEnvelope env, int indexcapacity, int capacity, Storage store){
		super(fs, env, indexcapacity, capacity, store);
	}

	
	/**
	 * Gets all feature within a given envelope.
	 */
	@Override
    public SimpleFeatureCollection get(Envelope e) throws IOException{
		Filter f = ff.bbox(getSchema().getGeometryDescriptor().getLocalName(), e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY(), getSchema().getCoordinateReferenceSystem().toString());		
		return getFeatures(f);
	}
	
	/**
	 * Gets all features that match a query.
	 * 
	 * <p>Currently the query handle is not supported.</p>
	 */
	@Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException{
		//setup types
		if (query.getTypeName() == null){
			query = new DefaultQuery(query);
			((DefaultQuery)query).setTypeName(this.getSchema().getTypeName());
		}else if (!query.getTypeName().equals(this.getSchema().getTypeName())){
			//types do not match 
			return new EmptyFeatureCollection(this.getSchema());
		}

		Filter[] filters = splitFilter(query.getFilter());
		Filter envFilter = filters[0];
		//Filter postFilter = filters[1];
				
		if (envFilter.equals(Filter.EXCLUDE)){
			//nothing to get
			return new EmptyFeatureCollection(this.getSchema());
		}else if (envFilter.equals(Filter.INCLUDE)){
			//lets create a new bbox filter out of the bounds
			envFilter = createFilterFromBounds();
			return new GridCachingFeatureCollection((BBOX)envFilter, query, this, this.fs, true);
		}else if (envFilter instanceof BBOX){
			return new GridCachingFeatureCollection((BBOX)envFilter, query, this, this.fs, true);
		}else{
			throw new UnsupportedOperationException("Invalid filter created.");
		}
	}
	
	/**
	 * Get all features that match given filter.
	 */
	@Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException{
	    
		Filter[] filters = splitFilter(filter);
		Filter envFilter = filters[0];
				
		if (envFilter.equals(Filter.EXCLUDE)){
            //nothing to get
            return new EmptyFeatureCollection(this.getSchema());
        }else if (envFilter.equals(Filter.INCLUDE)) {
        	//get bounds of data
        	envFilter = createFilterFromBounds();
			return generateFeatureCollection((BBOX)envFilter, filter);
        } else if (envFilter instanceof BBOX){   
        	SimpleFeatureCollection fc = generateFeatureCollection((BBOX)envFilter, filter);
        	return fc;
        }else{
        	throw new UnsupportedOperationException("Invalid filter created.");
        }
	}
	
	/**
	 * Creates a bbox filter from the data bounds
	 * @return
	 * @throws IOException
	 */
	private BBOX createFilterFromBounds() throws IOException{
		Envelope env = this.getBounds();
		BBOX myfilter = ff.bbox(getSchema().getGeometryDescriptor().getLocalName(), env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), getSchema().getCoordinateReferenceSystem().toString());
        return myfilter;
	}
	
	/**
	 * Creates a streaming feature collection
	 * 
	 * @param envFilter
	 * @param postFilter
	 * @return
	 */
	private SimpleFeatureCollection generateFeatureCollection(BBOX envFilter, Filter postFilter){
		GridCachingFeatureCollection collection = new GridCachingFeatureCollection(envFilter, postFilter, this, this.fs, true);
		return collection;
	}
	
	/**
	 * Creates a streaming feature collection
	 * 
	 * @param envFilter
	 * @param postFilter
	 * @return
	 */
	private SimpleFeatureCollection generateFeatureCollection(Envelope env){
		GridCachingFeatureCollection collection = new GridCachingFeatureCollection(env, this);
		return collection;
	}
    
	/**
	 * Splits the filter into two parts; the "bounds" part which
	 * will be used to get features from the cache; the filter part which
	 * will be applied after to ensure the feature meets the entire
	 * filter needs.
	 * 
	 * 
	 * @param filter
	 */
	private Filter[] splitFilter(Filter filter){
		BBoxFilterSplitter splitter = new BBoxFilterSplitter();
        filter.accept(splitter, null);

        Filter spatial_restrictions = splitter.getFilterPre();
        Filter other_restrictions = splitter.getFilterPost();

        return new Filter[]{spatial_restrictions, other_restrictions};
        
	}
  
	/**
	 * Returns all features in the cache that match the given envelope.
	 */
	public SimpleFeatureCollection peek(Envelope e) {
       	return generateFeatureCollection(e);
	}
	

	/**
	 * Unsupported.
	 */
	public void put(FeatureCollection fc, Envelope e) throws CacheOversizedException {
		throw new UnsupportedOperationException("Streaming feature cache does not put entire feature collections.");
	}

	/**
	 * Unsupported.
	 */
	public void put(FeatureCollection fc) throws CacheOversizedException {
		throw new UnsupportedOperationException("Streaming feature cache does not put entire feature collections.");
	}
	
	/**
	 * Removes all nodes from the cache that match the given envelope.
	 */
	public void remove(Envelope e) {
		NodeLockInvalidatingVisitor v = new NodeLockInvalidatingVisitor(this.tracker);
		try {
			if (e == null) {
				e = getBounds();// no envelope specified so assume everything
			}
			this.tracker.intersectionQuery(CacheUtil.convert(e), v);			// invalidates nodes
			this.tracker.getStatistics().addToDataCounter(-v.getDataCount());    //update stats to remove all data
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error removing elements from cache.", ex);
		}
		this.tracker.flush();
	}
	
	 /**
     * Registers a collection of nodes as valid.
     * This function assumes that the necessary nodes are locked
     * so that synchronization problems don't occur.
     * @param nodes
     */
    public void register( Collection<NodeIdentifier> nodes ) {
        // we don't want to track access while we register; this will ensure that just because a
        // node touches another node it
        // isn't considered an access; it might be better to improve the way containment query is
        // done.
        boolean recordaccess = this.tracker.getDoRecordAccess();
        try {
            this.tracker.setDoRecordAccess(false);
            for( Iterator<NodeIdentifier> iterator = nodes.iterator(); iterator.hasNext(); ) {
                NodeIdentifier nodeIdentifier = (NodeIdentifier) iterator.next();
                nodeIdentifier.setValid(true);
            }
        } finally {
            this.tracker.setDoRecordAccess(recordaccess);
        }
    }
    
    /**
     * Un-registers a collection of nodes.
     * This function assumes that the necessary nodes are locked
     * so that synchronization problems don't occur.
     * 
     * @param nodes
     */
    @Override
    public void unregister( Collection<NodeIdentifier> nodes ) {
        // we don't want to track access while we register; this will ensure that just because a
        // node touches another node it
        // isn't considered an access; it might be better to improve the way containment query is
        // done.
        boolean recordaccess = this.tracker.getDoRecordAccess();
        try {
            this.tracker.setDoRecordAccess(false);
            for( Iterator<NodeIdentifier> iterator = nodes.iterator(); iterator.hasNext(); ) {
                NodeIdentifier nodeIdentifier = (NodeIdentifier) iterator.next();
                nodeIdentifier.setValid(false);
            }
        } finally {
            this.tracker.setDoRecordAccess(recordaccess);
        }
    }
}
