package org.geotools.caching.grid.featurecache.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.caching.grid.featurecache.GridFeatureCache;
import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.DelegateSimpleFeatureIterator;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Envelope;
/**
 * Feature collection that reads features from two sources, a cache and a feature source.
 * <p>Responsible for reading features & caching features from the feature source.</p>
 * <p>This is a read only feature collection.</p>
 * 
 * @author Emily
 *
 *
 * @source $URL$
 */
public class GridCachingFeatureCollection implements SimpleFeatureCollection {
	private static final int MAX_FILTER_SIZE = 4; //the maximum number of "and" statements allowed in filter  
	
	private static Logger logger = org.geotools.util.logging.Logging.getLogger("org.geotools.caching");
	private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2(null);
	
	private BBOX preFilter;			//cache feature filter 
	private Filter postFilter;		//cache post filter
	private Query query;			//query (may be null)
	
	private GridFeatureCache grid;		//feature cache
	private SimpleFeatureSource fs;	//raw feature source
	private FeatureType featureType ;		//feature type associated with feature source
	private FeatureType newFeatureType;		//feature type associated with returned feature collection
	
	private Set<Object> iterators;			//list of the open iterators on the feature collection
	
	private boolean cacheFeatures = false;	//if features are cached
	
	private MathTransform reproject = null;	//reprojection to apply to features
	private Collection<CollectionListener> listeners;
	
	/**
	 * Creates a feature collection that will first
	 * read items from the cache then read any missing
	 * items from the feature source.
	 * 
	 * @param preFilter		the bounding filter of the request
	 * @param postFilter	the complete request filter
	 * @param g
	 * @param fs
	 * @param cacheFeatures
	 */
	public GridCachingFeatureCollection(BBOX preFilter, Filter postFilter, 
			GridFeatureCache g, SimpleFeatureSource fs, boolean cacheFeatures){
		this.grid = g;
		this.preFilter = preFilter;
		
		this.postFilter = postFilter;
		this.fs = fs;
		this.cacheFeatures = cacheFeatures;
		
		this.listeners = new HashSet<CollectionListener>();
		iterators = new HashSet<Object>();
		featureType = g.getSchema();
	}
	
	/**
	 * Creates a feature collection that will first read itmes from the cache
	 * and then read missing items from the feature source.
	 * 
	 * @param preFilter		the bounding filter of the request 
	 * @param query			the query
	 * @param g
	 * @param fs
	 * @param cacheFeatures
	 * @throws IOException
	 */
	public GridCachingFeatureCollection(BBOX preFilter, Query query, GridFeatureCache g,
	            SimpleFeatureSource fs, boolean cacheFeatures) throws IOException{
	    this(preFilter, query.getFilter(), g, fs, cacheFeatures);
	    
	    this.query = query;
	    	    
	    //setup crs transformation
	    CoordinateReferenceSystem source = g.getSchema().getCoordinateReferenceSystem();
	    CoordinateReferenceSystem dest = null;
	    if (query.getCoordinateSystemReproject() != null) {
            dest = query.getCoordinateSystemReproject();
        } else if (query.getCoordinateSystem() != null) {
            dest = query.getCoordinateSystem();
        }     
	    if (query.getCoordinateSystem() != null){
	        source = query.getCoordinateSystem();
	    }
	    if (dest != null && !dest.equals(source)){
	        try{
	            reproject = CRS.findMathTransform(source,dest);
	        }catch (FactoryException ex){
	        	IOException newex = new IOException("Could not reproject data to "+ dest);
	        	newex.initCause( ex );
	        	throw newex;
	        }
	    }

	    //determine new feature type (with new geometry and filtered attributes)
	    this.newFeatureType = createSchema((SimpleFeatureType)featureType, query, dest);
	    
	    //for now we can't sort queries
	    if (query.getSortBy() != null && query.getSortBy().length > 0){
	    	throw new IOException("Sorting not supported by this feature collection.");
	    }
	    //and we also don't support starting at some given index
	    if (query.getStartIndex() != null && query.getStartIndex() > 0){
	    	throw new IOException("Start index not supported by this feature collection.");
	    }
	}
	
	/**
	 * Creates the schema of the resulting feature collection  
	 */
	private SimpleFeatureType createSchema(SimpleFeatureType current, Query query, CoordinateReferenceSystem destcrs){
	    try{
	        if (destcrs != null && !destcrs.equals(current.getCoordinateReferenceSystem())){
	            //need to change the projection of the schema
                return DataUtilities.createSubType(current, query.getPropertyNames(), destcrs, query.getTypeName(), null);
	        }else{
	            if (query.retrieveAllProperties()){
	                //nothing to change
	                return current;
	            }else{
	                return DataUtilities.createSubType(current, query.getPropertyNames());
	            }
	        }
	    }catch (SchemaException ex){
	        logger.log(Level.SEVERE, "Error converting feature type to match query request.", ex);
	    }
	    return null;
	}
	        
	
	/**
	 * Creates a feature collection that will only
	 * read items from the cache.
	 * 
	 * @param e
	 * @param g
	 */
	public GridCachingFeatureCollection(Envelope e, GridFeatureCache g){
		this.grid = g;
		
		this.preFilter = filterFactory.bbox(g.getSchema().getGeometryDescriptor().getLocalName(), e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY(), g.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem().toString());
		this.postFilter =filterFactory.bbox(g.getSchema().getGeometryDescriptor().getLocalName(), e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY(), g.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem().toString());
		
		this.fs = null;
		this.cacheFeatures = false;
		this.listeners = new HashSet<CollectionListener>();
		iterators = new HashSet<Object>();
		featureType = g.getSchema();
	}
	
	
	/**
	 * Visits all features in the collection.
	 */
	public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
	    SimpleFeatureIterator feats = features();
	    try {
            while( feats.hasNext() ) {
                SimpleFeature f = feats.next();
                visitor.visit(f);
            }
        } finally {
            feats.close();
        }
		
	}

	/**
	 * Closes the feature iterator
	 */
	public void close(FeatureIterator<SimpleFeature> close) {
		try {
			if (close != null) {
				close.close();  //this will call the close(Iterator) method
			}
		} finally {
		}
	}

	/**
	 * Close the iterator
	 */
	public void close(Iterator<SimpleFeature> close) {
		if (close == null) return;
		try{
			closeIterator(close);
		}catch (Throwable e){
		    logger.log(Level.WARNING, "Failed to close iterator", e);
		}finally{
			iterators.remove(close);
		}		
	}

	/**
	 * Determines if the feature collection contains the
	 * given object.
	 */
	public boolean contains(Object o) {
		Iterator<SimpleFeature> e = null;
		try {
			e = iterator();
			if (o == null) {
				while (e.hasNext())
					if (e.next() == null)
						return true;
			} else {
				while (e.hasNext())
					if (o.equals(e.next()))
						return true;
			}
			return false;
		} finally {
			close(e);
		}
	}

	/**
	 * Determines if the feature collection contains
	 * all objects in the collection
	 */
	public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        try {
            while (e.hasNext())
                if(!contains(e.next()))
                return false;
            return true;
        } finally {
            close( (Iterator<SimpleFeature>)e );
        }
	}

	/**
	 * @returns a feature iterator that iterators over all features in the collection
	 */
	public SimpleFeatureIterator features() {
		SimpleFeatureIterator iter = new DelegateSimpleFeatureIterator(this, openIterator());
		iterators.add(iter);
		return iter;
	}
	
	/**
	 * Closes a iterator
	 * @param it
	 */
	private void closeIterator(Iterator<SimpleFeature> it){
		if (it == null) return;
		FeatureReaderFeatureIterator iter = ((FeatureReaderFeatureIterator)it);
		try{
			if (it.hasNext()){
				//there are still features left so we cannot leave this area as registered
				this.grid.unregister(iter.getMissingNodes());
			}else if (iter.getMissingNodes().size() > 0){
			    this.grid.getIndex().flush();
			}
		}finally{
		}
		iter.close();
	}
	
	/**
	 * Opens a new iterator.  This will lock the cache and nodes as required.
	 * @return
	 */
	private Iterator<SimpleFeature> openIterator(){
		Iterator<SimpleFeature> it = null;
		List<NodeIdentifier> missing = null;
		List<NodeIdentifier> found = null;
		
		FeatureReader<SimpleFeatureType, SimpleFeature> cacheFeatureReader = null;
        FeatureReader<SimpleFeatureType, SimpleFeature> sourceFeatureReader = null;
        
        Envelope env = new Envelope(preFilter.getMinX(), preFilter.getMaxX(), preFilter.getMinY(), preFilter.getMaxY());
        
		this.grid.readLock();
		try{
			//get features from the cache and the source
		    List<NodeIdentifier>[] notcached = grid.matchNodeIds(env);
			missing = notcached[0];
			found = notcached[1];
			
			//acquire read locks
    		acquireReadLocks(missing, found);
    		
			if (missing.size() == 0){
			    //all locks acquired 
			    this.grid.readUnLock();
			    cacheFeatureReader = new GridCacheFeatureReader(found, (GridSpatialIndex)grid.getIndex());
			}else{
			    //need a write lock; release all read locks
			    for( Iterator<NodeIdentifier> iterator = found.iterator(); iterator.hasNext(); ) {
                    NodeIdentifier nodeid = (NodeIdentifier) iterator.next();
                    nodeid.readUnLock();
			    }
			    this.grid.readUnLock();
			    this.grid.writeLock();
			    try{
			        //re-get read locks
			        acquireReadLocks(missing, found);
			    
			        // acquire write locks
			        if (missing.size() > 0 && fs != null) {
                        acquireWriteLocks(missing, found);
			        }
			        
			        // register working area
			        if (missing.size() > 0 && fs != null) {
	                     this.grid.register(missing);
			        }
			    }finally{
			        this.grid.writeUnLock();
			    }
			    
                 if (fs != null) {
                    // note that for performance reasons this query might actually return
                    // features from an area larger than we are interested in caching
                    DefaultQuery dq = new DefaultQuery(featureType.getName().getLocalPart(), this.createFilter(missing));
                    dq.setCoordinateSystem(featureType.getCoordinateReferenceSystem());
                    if (query != null) {
                        dq.setHints(query.getHints());
                        dq.setHandle(query.getHandle());
                    } else {
                        dq.setHints(new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY,new LiteCoordinateSequenceFactory()));
                    }
                    try{
                        sourceFeatureReader = ((DataStore) fs.getDataStore()).getFeatureReader(dq,Transaction.AUTO_COMMIT);
                    }catch (Exception ex){
                        //nothing to get
                        missing.clear();
                        sourceFeatureReader = null;
                    }
                } else {
                    missing.clear(); // we didn't get anything so we don't want to register these as write locks
                }

                if (found.size() > 0) {
                    cacheFeatureReader = new GridCacheFeatureReader(found, (GridSpatialIndex) grid.getIndex());
                }
			}
			
			boolean localcache = cacheFeatures;
			if (query != null && query.getMaxFeatures() != Integer.MAX_VALUE){
			    localcache = false;
			}
			FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
			if (sourceFeatureReader != null && cacheFeatureReader != null){
			    fr = new CombiningCachingFeatureReader(cacheFeatureReader, sourceFeatureReader, false, localcache, grid.getIndex(), this.postFilter);
			}else if (sourceFeatureReader != null && cacheFeatureReader == null){
			    fr = new CombiningCachingFeatureReader(new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>((SimpleFeatureType)featureType), sourceFeatureReader, false, localcache, grid.getIndex(), this.postFilter);
			}else if (sourceFeatureReader == null && cacheFeatureReader != null){
			    fr = new CombiningCachingFeatureReader(cacheFeatureReader, new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>((SimpleFeatureType)featureType), false, false, grid.getIndex(), this.postFilter);
			}else{
			    fr = new CombiningCachingFeatureReader(new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>((SimpleFeatureType)featureType), new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>((SimpleFeatureType)featureType), false, false, grid.getIndex(), this.postFilter);
			}
			
			it = new FeatureReaderFeatureIterator(wrapFeatureReader(fr), missing, found);

		} catch (Exception ex) {
		    logger.log(Level.SEVERE, "Failed to create feature collection iterator.", ex);
	        if (missing != null){
	            for( Iterator<NodeIdentifier> iterator = missing.iterator(); iterator.hasNext(); ) {
	                NodeIdentifier nodeid = (NodeIdentifier) iterator.next();
	                nodeid.writeUnLock();
	            }
	        }
	        if (found != null){
	            for( Iterator<NodeIdentifier> iterator = found.iterator(); iterator.hasNext(); ) {
                    NodeIdentifier nodeid = (NodeIdentifier) iterator.next();
                    nodeid.readUnLock();
                }
	        }
		    if (missing != null){
		        this.grid.unregister(missing);
		    }
			
		}finally{
		    try{
		        //ensure we are unlocked
		        this.grid.readUnLock();
		    }catch (Exception ex){}
		}
		
		return it;
	}

	/**
	 * This function acquire write locks for everything in the missing array.
	 * Once the write lock is acquired if the node is valid it is converted to 
	 * a read lock.
	 *
	 * @param missing
	 * @param found
	 * @throws Exception
	 */
    private void acquireWriteLocks( List<NodeIdentifier> missing, List<NodeIdentifier> found )
            throws Exception {
        for( Iterator<NodeIdentifier> iterator = missing.iterator(); iterator.hasNext(); ) {
            NodeIdentifier nodeid = (NodeIdentifier) iterator.next();
            try {
                nodeid.writeLock();
            }catch (Exception ex){
                //error occurred; remove from list
                iterator.remove();
                logger.log(Level.SEVERE, "Could not acquire necessary write locks.", ex);
                continue;
            }
            if (nodeid.isValid()) {
                // might have been validated by previous thread; so lets make sure we have the correct queue
                nodeid.readLock();
                found.add(nodeid);
                nodeid.writeUnLock();
                iterator.remove();
            }
        }
    }

    /**
     * This function acquires reads locks for everything in the missing array.
     * Once the read lock is acquired if the node is not valid it is added 
     * to the missing array (write locks are not acquired here).
     *
     * @param missing
     * @param found
     */
    private void acquireReadLocks( List<NodeIdentifier> missing, List<NodeIdentifier> found ) {
        for( Iterator<NodeIdentifier> iterator = found.iterator(); iterator.hasNext(); ) {
            NodeIdentifier nodeid = (NodeIdentifier) iterator.next();
            try {
                nodeid.readLock();
            } catch (Exception ex) {
                // remove from list
                logger.log(Level.SEVERE, "Could not acquire necessary read locks.", ex);
                iterator.remove();
                continue;
            }
            if (!nodeid.isValid()) {
                // this node is no longer valid add to missing list
                nodeid.readUnLock();
                missing.add(nodeid);
                iterator.remove();
            }
        }
    }
	
	/**
	 * Wraps a feature reader in a reproject and retype feature reader
	 * if necessary (as determined by query used in collection).
	 * 
	 * @param reader
	 * @return
	 */
	private FeatureReader<SimpleFeatureType, SimpleFeature> wrapFeatureReader(FeatureReader<SimpleFeatureType, SimpleFeature> reader){
	    //creates a reproject feature reader
	    FeatureReader<SimpleFeatureType, SimpleFeature> newfr = reader;
        if (query != null && query.getMaxFeatures() != Integer.MAX_VALUE){
            newfr = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(newfr, query.getMaxFeatures());
        }
	    if (this.reproject != null){
	        newfr = new ReprojectFeatureReader(newfr, (SimpleFeatureType) featureType, this.reproject);
	    }
	    //creates a re-type feature reader
	    if (this.newFeatureType != null && !this.newFeatureType.equals(this.featureType)){
	    	newfr = new ReTypeFeatureReader(newfr, (SimpleFeatureType)this.newFeatureType);
	    }
	    return newfr;
	}
	
	
	/**
	 * Creates a filter from a collection of shapes.  If there are more than 4 shapes 
	 * then all the shapes are just expanded to include all.  This means that you may end up requesting 
	 * more features than are identified by the shapes.
	 * 
	 * @param shapes
	 * @return
	 */
	private Filter createFilter( Collection<NodeIdentifier> shapes ) {
        String geometryname = this.featureType.getGeometryDescriptor().getLocalName();
        String srs = this.featureType.getGeometryDescriptor().getCoordinateReferenceSystem().toString();
        ArrayList<Filter> filters = new ArrayList<Filter>(shapes.size());
      
        if (shapes.size() < MAX_FILTER_SIZE) {
            for( Iterator<NodeIdentifier> iterator = shapes.iterator(); iterator.hasNext(); ) {
                Region r = (Region) iterator.next().getShape();
                Filter bbox = filterFactory.bbox(geometryname, r.getLow(0), r.getLow(1), r.getHigh(0), r.getHigh(1), srs);
                filters.add(bbox);
            }
            return filterFactory.or(filters);
        } else {
            Region area = null;
            for( Iterator<NodeIdentifier> iterator = shapes.iterator(); iterator.hasNext(); ) {
                Region r = (Region) iterator.next().getShape();
                if (area == null) {
                    area = new Region(r);
                } else {
                    area = area.combinedRegion(r);
                }
            }
            return filterFactory.bbox(geometryname, area.getLow(0), area.getLow(1),area.getHigh(0), area.getHigh(1), srs);
        }
    }
	/**
	 * @throws UnsupportedOperationException
	 */
	public ReferencedEnvelope getBounds() {
		throw new UnsupportedOperationException("Bounds of this collection not known");
	}

	public String getID() {
		return "cachingFeatureCollection";
	}

	public SimpleFeatureType getSchema() {
		return (SimpleFeatureType) newFeatureType;
	}

	public Iterator<SimpleFeature> iterator() {
		Iterator<SimpleFeature> it = openIterator();
		iterators.add(it);
		return it;
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException("Cannot convert to array.");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public <O> O[] toArray(O[] a) {
		throw new UnsupportedOperationException("Cannot convert to array.");
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean isEmpty() {
		throw new UnsupportedOperationException("Size unknown.");
	}
	
	/**
	 * Determines the size of the feature collection
	 */
	public int size() {
		SimpleFeatureIterator fi = features();
		int cnt = 0;
		try {
			while (fi.hasNext()) {
				fi.next();
				cnt++;
			}
		} finally {
			close(fi);
		}
		return cnt;
	}

	
	public void removeListener(CollectionListener listener) throws NullPointerException {
        listeners.remove(listener);
    }


    public void addListener(CollectionListener listener) throws NullPointerException {
        listeners.add(listener);
    }
    
	/**
	 * @throws UnsupportedOperationException
	 */
	public void purge() {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}	

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	public SimpleFeatureCollection subCollection(Filter filter) {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	public SimpleFeatureCollection sort(SortBy order) {
		throw new UnsupportedOperationException("Cannot modify this feature collection");
	}
	
	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean add(SimpleFeature obj) {
		throw new UnsupportedOperationException("Cannot add features to this feature collection");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(Collection<? extends SimpleFeature> collection) {
		throw new UnsupportedOperationException("Cannot add features to this feature collection");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(FeatureCollection<? extends SimpleFeatureType, ? extends SimpleFeature> resource) {
		throw new UnsupportedOperationException("Cannot add features to this feature collection");
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void clear() {
		throw new UnsupportedOperationException("Cannot clear this collection");
		
	}

	
	/**
	 * A wrapper around a feature reader that
	 * implements the iterator interface.
	 *
	 */
	class FeatureReaderFeatureIterator implements Iterator<SimpleFeature>{

		private FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
		
		private Collection<NodeIdentifier> writelocks;
		private Collection<NodeIdentifier> readlocks;
		
		public FeatureReaderFeatureIterator(FeatureReader<SimpleFeatureType, SimpleFeature> fr, Collection<NodeIdentifier> writeLocks, Collection<NodeIdentifier> readLocks){
			this.reader = fr;
			this.writelocks = writeLocks;
			this.readlocks = readLocks;
		}
		
		
		private void releaseLocks(){
			try {
				for (Iterator<NodeIdentifier> iterator = writelocks.iterator(); iterator.hasNext();) {
					NodeIdentifier type = (NodeIdentifier) iterator.next();
					try{
					    type.writeUnLock();
					}catch (Exception ex){
					//    logger.log(Level.SEVERE, "Could not release write lock.", ex);
					}
				}
			} catch (Exception ex) {
			    logger.log(Level.SEVERE, "Could not release write locks.", ex);
			}
			try {
				for (Iterator<NodeIdentifier> iterator = readlocks.iterator(); iterator.hasNext();) {
					NodeIdentifier type = (NodeIdentifier) iterator.next();
					try{
					    type.readUnLock();
					}catch (Exception ex){
					//    logger.log(Level.SEVERE, "Could not release read lock.", ex);
					}
				}
			} catch (Exception ex) {
			    logger.log(Level.SEVERE, "Could not release read locks.", ex);;
			}
		}
		
		/**
		 * Returns the nodes that have been write locked.  These
		 * are the nodes that are currently being written
		 * to.
		 *
		 * @return
		 */
		public Collection<NodeIdentifier> getMissingNodes(){
		    return this.writelocks;
		}
		
		
		public boolean hasNext() {
			try {
			    if (reader == null) return false;
			    if (reader.hasNext()){
			        return true;
			    }else{
			        return false;
			    }
			} catch (IOException ex) {
			    logger.log(Level.SEVERE, "No more items.", ex);
			    close();
			    return false;
			}
		}
		public SimpleFeature next() throws NoSuchElementException {
		    if (reader == null){
		        throw new NoSuchElementException("Iterator has been closed.");
		    }
			try {
				return (SimpleFeature)reader.next();
			} catch (IOException ex) {
			    close();
				NoSuchElementException problem = new NoSuchElementException("Could not obtain the next feature:" + ex);
				problem.initCause( ex );
				throw problem;
			}
		}

		public void remove() {
			throw new UnsupportedOperationException("Modifications of the contents of this iterator is not supported.");					
		}
		
		public void close(){
		    releaseLocks();
		    
		    if (reader == null) return;
			try{
				reader.close();
			}catch(IOException ex){
			    logger.log(Level.SEVERE, "Error closing reader.", ex);
			}
			reader = null;
		}
	}
}
