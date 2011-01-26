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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class Diff{
	private final Map modifiedFeatures;
	private final Map addedFeatures;
	
	/**
	 * Unmodifiable view of modified features.
     * It is imperative that the user manually synchronize on the
     * map when iterating over any of its collection views:
     * <pre>
     *  Set s = diff.modified2.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on diff, not diff.modified2 or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * <p>The returned map will be serializable if the specified map is
     * serializable.
	 */
	public final Map modified2;
	/**
	 * Unmodifiable view of added features.
     * It is imperative that the user manually synchronize on the
     * map when iterating over any of its collection views:
     * <pre>
     *  Set s = diff.added.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(diff) {  // Synchronizing on m, not diff.added or s!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * <p>The returned map will be serializable if the specified map is
     * serializable.
	 */
	public final Map added;
	
	public int nextFID=0;
	private SpatialIndex spatialIndex;
	Object mutex;
	
	public Diff( ){
		modifiedFeatures=new ConcurrentHashMap();
		addedFeatures=new ConcurrentHashMap();
		modified2=Collections.unmodifiableMap(modifiedFeatures);
		added=Collections.unmodifiableMap(addedFeatures);
		spatialIndex=new Quadtree();
		mutex=this;
	}
	
	public boolean isEmpty() {
		synchronized (mutex) {
			return modifiedFeatures.isEmpty() && addedFeatures.isEmpty();
		}
	}

	public void clear() {
		synchronized (mutex) {
			nextFID=0;
			addedFeatures.clear();
			modifiedFeatures.clear();
			spatialIndex=new Quadtree();				
		}
	}

	public Diff(Diff other){
		modifiedFeatures=Collections.synchronizedMap(new HashMap(other.modifiedFeatures));
		addedFeatures=Collections.synchronizedMap(new HashMap(other.addedFeatures));
		modified2=Collections.unmodifiableMap(modifiedFeatures);
		added=Collections.unmodifiableMap(addedFeatures);
		spatialIndex=copySTRtreeFrom(other);
		nextFID=other.nextFID;
		mutex=this;
	}
	
	public void modify(String fid, SimpleFeature f) {
		synchronized (mutex) {
			SimpleFeature old;
            if( addedFeatures.containsKey(fid) ){
            	old=(SimpleFeature) addedFeatures.get(fid);
                addedFeatures.put(fid, f);
            }else{
            	old=(SimpleFeature) modifiedFeatures.get(fid);
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
			} else {
				old = (SimpleFeature) modifiedFeatures.get(fid);
				modifiedFeatures.put(fid, TransactionStateDiff.NULL);
			}
			if( old != null ) {
				spatialIndex.remove(ReferencedEnvelope.reference(old.getBounds()), old);
			}			
		}
	}
	
	public List queryIndex(Envelope env) {
		synchronized (mutex) {
			return spatialIndex.query(env);
		}
	}
	
	protected Quadtree copySTRtreeFrom(Diff diff) {
		Quadtree tree = new Quadtree();
		
		synchronized (diff) {
			Iterator i = diff.added.entrySet().iterator();
			while (i.hasNext()) {
				Entry e = (Map.Entry) i.next();
				SimpleFeature f = (SimpleFeature) e.getValue();
				if (!diff.modifiedFeatures.containsKey(f.getID())) {
					tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
				}
			}
			Iterator j = diff.modified2.entrySet().iterator();
			while( j.hasNext() ){
				Entry e = (Map.Entry) j.next();
				SimpleFeature f = (SimpleFeature) e.getValue();
				tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
			}
		}
		
		return tree;
	}
}
