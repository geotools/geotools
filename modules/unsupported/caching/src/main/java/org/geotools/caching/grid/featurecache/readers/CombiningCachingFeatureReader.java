package org.geotools.caching.grid.featurecache.readers;

import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.geotools.caching.spatialindex.SpatialIndex;
import org.geotools.caching.util.CacheUtil;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
/**
 * This feature reader reads features from two sources.  The results
 * of either are optionally cached.
 * <p>
 * All features are read from the feature readers and cached (if requested) however
 * only those features which pass the postFilter filter are
 * returned.
 * </p>
 * 
 * @author Emily
 * @since 1.2.0
 *
 * @source $URL$
 */
public class CombiningCachingFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature>{

	private FeatureReader<SimpleFeatureType, SimpleFeature> r1 = null;
	private FeatureReader<SimpleFeatureType, SimpleFeature> r2 = null;
	
	private boolean cache1 = false;    //if feature reader1 results are to be cached
	private boolean cache2 = false;    //if feature reader 2 results are to be cached
	
	private SpatialIndex index = null; //cache location
	private Filter postFilter = null;  //filter to apply to features as they are read from readers

    private SimpleFeature next = null;  //the next feature to return on a call to next();
    private HashSet<String> collectedFeatureIds;	//need this for now because the "grid" may store the same feature multiple times per node; we only want to get each feature once
    
	/**
	 * Creates a new feature reader that combines the results from two feature
	 * reads and optionally caches the results.
	 * 
	 * 
	 * All features are read from the feature readers and cached (if requested)
	 * however only those features which pass the postFilter filter are
	 * returned. </p>
	 * 
	 * @param reader1	feature reader 1
	 * @param reader2	feature reader 2
	 * @param cache1	if results from feature reader 1 should be cached
	 * @param cache2	if results from feature reader 2 should be cached
	 * @param cache		cache				
	 * @param postFilter	the filter to be applied as feature read 
	 */
	public CombiningCachingFeatureReader(FeatureReader<SimpleFeatureType, SimpleFeature> reader1, FeatureReader<SimpleFeatureType, SimpleFeature> reader2, boolean cache1, boolean cache2, SpatialIndex cache, Filter postFilter) throws IOException{
		this.r1 = reader1;
		this.r2 = reader2;
		
		this.cache1 = cache1;
		this.cache2 = cache2;
		this.index = cache;
		this.postFilter = postFilter;
		
		this.collectedFeatureIds = new HashSet<String>();
		init();
	}

	/**
	 * Closes the feature reader
	 */
	public void close() throws IOException {
		r1.close();
		r2.close();
		next = null;
	}

	/**
	 * Get type associated with reader.
	 * <p>This function returns the type from feature reader 1.  Assumes that
	 * both feature readers have the same type.</p>
	 */
	public SimpleFeatureType getFeatureType() {
		return (SimpleFeatureType)r1.getFeatureType();
	}

	/**
	 * @returns true if there are more features to read
	 */
	public boolean hasNext() throws IOException {
		return next != null;
	}

	/**
	 * This function finds the first element in the collection
	 */
	private void init() throws IOException{
		next = findNext();
	}
	
	/**
	 * Finds the next feature to return in the given collection.
	 * <p>This function is responsible for caching the 
	 * features as it processes them.  All features are cached
	 * however only features which pass the postFilter
	 * are returned.  
	 * </p>
	 * <p>The function iterators through all the features
	 * in the first feature reader; then moves on to the features in the second
	 * feature reader.
	 * </p>
	 *
	 * @return the next feature in the collection
	 */
	private SimpleFeature findNext() throws IOException {
		while (r1.hasNext()) {
			SimpleFeature sf = (SimpleFeature) r1.next();
			if (cache1) {
				this.index.insertData(sf, CacheUtil.convert((Envelope) sf.getBounds()));
			}
			if (postFilter.evaluate(sf) && !collectedFeatureIds.contains(sf.getID())) {
				return sf;
			}
		}
		while (r2.hasNext()) {
			SimpleFeature sf = (SimpleFeature) r2.next();
			if (cache2) {
				this.index.insertData(sf, CacheUtil.convert((Envelope) sf.getBounds()));
			}
			if (postFilter.evaluate(sf) && !collectedFeatureIds.contains(sf.getID())) {
				return sf;
			}
		}
		return null;
	}
	
	/**
	 * Returns the next element in the collection.
	 */
	public SimpleFeature next() throws IOException, IllegalArgumentException,
			NoSuchElementException {
		
		SimpleFeature current = next;
		collectedFeatureIds.add(current.getID());
		
		next = findNext();
		if (current == null){
			throw new NoSuchElementException("Both of the readers of been closed.");
		}
		return current;
	}
}
