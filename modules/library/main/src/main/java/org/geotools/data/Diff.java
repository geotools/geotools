/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * Captures changes made to a FeatureStore prior to being committed.
 * <p>
 * This is used to simulate the functionality of a database including transaction
 * independence.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class Diff{
    /** Map of modified features; by feature id */
    private final Map<String,SimpleFeature> modifiedFeatures;
    
    /**
     * Map of added features; by feature id.
     * <p>
     * Note deleted features are represented as a null value recorded against their feature id
     */
    private final Map<String,SimpleFeature> addedFeatures;
    
    /** List of added feature ids; values stored in added above */
    private final List<String> addedFidList;

    /**
     * Unmodifiable view of modified features. It is imperative that the user manually synchronize
     * on the map when iterating over any of its collection views:
     * 
     * <pre>
     *  Set s = diff.modified2.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on diff, not diff.modified2 or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * 
     * Failure to follow this advice may result in non-deterministic behavior.
     * 
     * <p>
     * The returned map will be serializable if the specified map is serializable.
     * @deprecated Please use getModified();
     */
    public final Map<String, SimpleFeature> modified2;
    
    /**
     * Unmodifiable view of added features. It is imperative that the user manually synchronize on
     * the map when iterating over any of its collection views:
     * 
     * <pre>
     *  Set s = diff.added.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on m, not diff.added or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * 
     * Failure to follow this advice may result in non-deterministic behavior.
     * 
     * <p>
     * The returned map will be serializable if the specified map is serializable.
     * @deprecated please use getAdded()
     */
    public final Map<String, SimpleFeature> added;
    
    private final List<String> order;
    
    /** counter used to genreate the "next" new feature id */
    public int nextFID = 0;
    
    /** Spatial index; allowing quick qccess to features */
    private SpatialIndex spatialIndex;

    /** Simple object used for locking */
    Object mutex;

    /** Create an empty Diff */
    public Diff() {
        // private fields
        modifiedFeatures = new ConcurrentHashMap<String, SimpleFeature>();
        addedFeatures =new ConcurrentHashMap<String, SimpleFeature>();
        addedFidList = new CopyOnWriteArrayList<String>();
        
        // public "views" requiring synchronised( mutex )
        modified2 = Collections.unmodifiableMap(modifiedFeatures);
        added = Collections.unmodifiableMap(addedFeatures);
        order = Collections.unmodifiableList(addedFidList);

        spatialIndex = new Quadtree();
        mutex = this;
    }
    
    /**
     * Diff copy.
     * @param other
     */
    public Diff(Diff other){
        // copy data
        modifiedFeatures=new ConcurrentHashMap<String, SimpleFeature>(other.modifiedFeatures);
        addedFeatures=new ConcurrentHashMap<String, SimpleFeature>(other.addedFeatures);
        addedFidList=new CopyOnWriteArrayList<String>(other.addedFidList);
        
        // create public "views"
        modified2=Collections.unmodifiableMap(modifiedFeatures);
        added=Collections.unmodifiableMap(addedFeatures);
        order = Collections.unmodifiableList(addedFidList);

        spatialIndex=copySTRtreeFrom(other);
        nextFID=other.nextFID;
        mutex=this;
    }
    
    /**
     * Check if modifiedFeatures and addedFeatures are empty.
     * 
     * @return true if Diff is empty
     */
    public boolean isEmpty() {
        synchronized (mutex) {
            return modifiedFeatures.isEmpty() && addedFeatures.isEmpty();
        }
    }
    /**
     * Clear diff - called during rollback.
     */
    public void clear() {
        synchronized (mutex) {
            nextFID = 0;
            addedFeatures.clear();
            addedFidList.clear();
            modifiedFeatures.clear();
            spatialIndex = new Quadtree();
        }
    }

    /**
     * Record a modification to the indicated fid
     * 
     * @param fid
     * @param f replacement feature; null to indicate remove
     */
	public void modify(String fid, SimpleFeature f) {
		synchronized (mutex) {
			SimpleFeature old;
            if( addedFeatures.containsKey(fid) ){
                old = addedFeatures.get(fid);
                if( f == null ){
                    addedFeatures.remove(fid);
                    addedFidList.remove(fid);
                }
                else {
                    addedFeatures.put(fid, f);
                }
            }
            else{
                old = modifiedFeatures.get(fid);
                modifiedFeatures.put(fid, f);
            }
            if(old != null) {
            	spatialIndex.remove(ReferencedEnvelope.reference(old.getBounds()), old);
            }
            addToSpatialIndex(f);
		}
	}
	
	public void add(String fid, SimpleFeature f) {
		synchronized (mutex) {
			addedFeatures.put(fid, f);
			addedFidList.add(fid); // preserve order features are added in
			addToSpatialIndex(f);
		}
	}
	
	protected void addToSpatialIndex(SimpleFeature f) {
		if (f.getDefaultGeometry() != null) {
			BoundingBox bounds = f.getBounds();
			if( !bounds.isEmpty() )
				spatialIndex.insert(ReferencedEnvelope.reference(bounds), f);
		}
	}
	
	public void remove(String fid) {
		synchronized (mutex) {
			SimpleFeature old = null;
			
			if( addedFeatures.containsKey(fid) ){
				old = (SimpleFeature) addedFeatures.get(fid);
				addedFeatures.remove(fid);
				addedFidList.remove(fid);
			} else {
				old = (SimpleFeature) modifiedFeatures.get(fid);
				modifiedFeatures.put(fid, TransactionStateDiff.NULL);
			}
			if( old != null ) {
				spatialIndex.remove(ReferencedEnvelope.reference(old.getBounds()), old);
			}			
		}
	}
	
	@SuppressWarnings("unchecked")
    public List<SimpleFeature> queryIndex(Envelope env) {
		synchronized (mutex) {
			return spatialIndex.query(env);
		}
	}

    /** Unmodifieable list indicating the order features were added */
    public List<String> getAddedOrder() {
        return addedFidList;
    }

    /**
     * Unmodifiable view of modified features. It is imperative that the user manually synchronize
     * on the map when iterating over any of its collection views:
     * 
     * <pre>
     *  Set s = diff.modified2.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on diff, not diff.modified2 or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * 
     * Failure to follow this advice may result in non-deterministic behavior.
     * 
     * <p>
     * The returned map will be serializable if the specified map is serializable.
     * 
     * @return Map of modified features, null user to represent a removed feature
     */
    public Map<String, SimpleFeature> getModified() {
        return modified2;
    }

    /**
     * Unmodifiable view of added features. It is imperative that the user manually synchronize on
     * the map when iterating over any of its collection views:
     * 
     * <pre>
     *  Set s = diff.added.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on m, not diff.added or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * 
     * Failure to follow this advice may result in non-deterministic behavior.
     * 
     * <p>
     * The returned map will be serializable if the specified map is serializable.
     * 
     * @return Map of added features
     */
    public Map<String, SimpleFeature> getAdded() {
        return added;
    }
	protected Quadtree copySTRtreeFrom(Diff diff) {
		Quadtree tree = new Quadtree();
		
		synchronized (diff) {
			Iterator<Entry<String,SimpleFeature>> i = diff.added.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String,SimpleFeature> e = i.next();
				SimpleFeature f = (SimpleFeature) e.getValue();
				if (!diff.modifiedFeatures.containsKey(f.getID())) {
					tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
				}
			}
			Iterator<Entry<String,SimpleFeature>> j = diff.modified2.entrySet().iterator();
			while( j.hasNext() ){
				Entry<String,SimpleFeature> e = j.next();
				SimpleFeature f = (SimpleFeature) e.getValue();
				tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
			}
		}
		
		return tree;
	}
}
