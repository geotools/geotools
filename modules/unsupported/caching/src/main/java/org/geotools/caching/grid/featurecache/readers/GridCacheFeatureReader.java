package org.geotools.caching.grid.featurecache.readers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.geotools.caching.grid.spatialindex.GridData;
import org.geotools.caching.grid.spatialindex.GridNode;
import org.geotools.caching.grid.spatialindex.GridRootNode;
import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature Reader that reads features from a cache.
 *  
 * <p>
 *
 * </p>
 * @author Emily
 * @since 1.2.0
 *
 *
 * @source $URL$
 */
public class GridCacheFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

	private Stack<NodeIdentifier> tovisit = new Stack<NodeIdentifier>();   //nodes to visit
	private Iterator<GridData> currentNodeIterator = null;         //current node data iterator
	private GridSpatialIndex grid;     //grid cache 

	private SimpleFeature next;    //next feature in the collection
	private HashSet<String> collectedFeatureIds;	//need this for now because the "grid" may store the same feature multiple times per node; we only want to get each feature once
	
	/**
	 * Creates a feature reader which reads features from a collection
	 * of nodes.
	 * 
	 * @param nodeids
	 * @param g
	 */
	public GridCacheFeatureReader(Collection<NodeIdentifier> nodeids, GridSpatialIndex g){
	    this.grid = g;
	    collectedFeatureIds = new HashSet<String>();
	    
	    GridRootNode gn = g.getRootNode();
	    tovisit.add(gn.getIdentifier());
	    if (nodeids != null) {
            for( Iterator<NodeIdentifier> iterator = nodeids.iterator(); iterator.hasNext(); ) {
                NodeIdentifier id = (NodeIdentifier) iterator.next();
                if (id.isValid()) {
                    tovisit.add(id);
                }
            }
            init();
        }
	}
	
	/**
	 * Closes the feature reader
	 */
	public void close() throws IOException {
		tovisit.clear();
		tovisit =null;
		currentNodeIterator = null;
		next = null;
		grid = null;
		
	}

	/**
	 * @returns the type of features that the feature reader returns
	 */
	public SimpleFeatureType getFeatureType() {
		if (grid.getStorage().getFeatureTypes().size() == 0){
			return null;
		}else{
			return (SimpleFeatureType)grid.getStorage().getFeatureTypes().iterator().next();
		}
	}

	/**
	 * @returns true if more features to be read; false if all features read
	 */
	public boolean hasNext() throws IOException {
		return next != null;
	}

	/**
	 * Finds the first element in the collection (or null if no elements).
	 */
	private void init(){
		//find first
		currentNodeIterator = null;
		next = findNext();
	}
	
	/**
	 * Finds the next element to read.
	 *
	 * @return next element or null if no more elements
	 */
	private SimpleFeature findNext(){
		SimpleFeature sf = null;
		
		while(true){
			if (currentNodeIterator != null && currentNodeIterator.hasNext()){
				sf = (SimpleFeature)((GridData)currentNodeIterator.next()).getData();
				if (!collectedFeatureIds.contains(sf.getID())){
					//we want to return sf
					break;
				}
			}
			if (currentNodeIterator == null || !currentNodeIterator.hasNext()){
				if (tovisit.size() == 0){
					//nothing found; no other things to look at
					return null;
				}
				NodeIdentifier nodeid = tovisit.pop();
				GridNode currentNode = (GridNode)grid.readNode(nodeid);
				currentNodeIterator = currentNode.getData().iterator();
			}
		}
		
		if (sf != null){
			collectedFeatureIds.add(sf.getID());
		}
		return sf;
	}
	
	/**
	 * Returns the next element in the collection
	 */
	public SimpleFeature next() throws IOException, IllegalArgumentException,
			NoSuchElementException {
		
		SimpleFeature current = next;
		next = findNext();
		if (current == null){
			throw new NoSuchElementException("Invalid call; no more feature here.");
		}
		return current;	
	}
}
