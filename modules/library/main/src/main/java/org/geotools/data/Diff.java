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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.quadtree.Quadtree;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

/**
 * Captures changes made to a FeatureStore prior to being committed.
 *
 * <p>This is used to simulate the functionality of a database including transaction independence.
 *
 * @author Jody Garnett
 */
public class Diff {
    /** Map of modified features; by feature id */
    private final Map<String, SimpleFeature> modifiedFeatures;

    /**
     * Map of added features; by feature id.
     *
     * <p>Note deleted features are represented as a null value recorded against their feature id
     */
    private final Map<String, SimpleFeature> addedFeatures;

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
     * <p>The returned map will be serializable if the specified map is serializable.
     */
    private final Map<String, SimpleFeature> modified2;

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
     * <p>The returned map will be serializable if the specified map is serializable.
     */
    private final Map<String, SimpleFeature> added;

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
        addedFeatures = new ConcurrentHashMap<String, SimpleFeature>();
        addedFidList = new CopyOnWriteArrayList<String>();

        // public "views" requiring synchronised( mutex )
        modified2 = Collections.unmodifiableMap(modifiedFeatures);
        added = Collections.unmodifiableMap(addedFeatures);

        spatialIndex = new Quadtree();
        mutex = this;
    }

    /** Diff copy. */
    public Diff(Diff other) {
        // copy data
        modifiedFeatures = new ConcurrentHashMap<String, SimpleFeature>(other.modifiedFeatures);
        addedFeatures = new ConcurrentHashMap<String, SimpleFeature>(other.addedFeatures);
        addedFidList = new CopyOnWriteArrayList<String>(other.addedFidList);

        // create public "views"
        modified2 = Collections.unmodifiableMap(modifiedFeatures);
        added = Collections.unmodifiableMap(addedFeatures);

        spatialIndex = copySTRtreeFrom(other);
        nextFID = other.nextFID;
        mutex = this;
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
    /** Clear diff - called during rollback. */
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
     * @param f replacement feature; null to indicate remove
     */
    public void modify(String fid, SimpleFeature f) {
        synchronized (mutex) {
            SimpleFeature old;
            if (addedFeatures.containsKey(fid)) {
                old = addedFeatures.get(fid);
                if (f == null) {
                    addedFeatures.remove(fid);
                    addedFidList.remove(fid);
                } else {
                    addedFeatures.put(fid, f);
                }
            } else {
                old = modifiedFeatures.get(fid);
                modifiedFeatures.put(fid, f);
            }
            if (old != null) {
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
        if (f != null && f.getDefaultGeometry() != null) {
            BoundingBox bounds = f.getBounds();
            if (!bounds.isEmpty()) spatialIndex.insert(ReferencedEnvelope.reference(bounds), f);
        }
    }

    public void remove(String fid) {
        synchronized (mutex) {
            SimpleFeature old = null;

            if (addedFeatures.containsKey(fid)) {
                old = (SimpleFeature) addedFeatures.get(fid);
                addedFeatures.remove(fid);
                addedFidList.remove(fid);
            } else {
                old = (SimpleFeature) modifiedFeatures.get(fid);
                modifiedFeatures.put(fid, Diff.NULL);
            }
            if (old != null) {
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
     * <p>The returned map will be serializable if the specified map is serializable.
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
     * <p>The returned map will be serializable if the specified map is serializable.
     *
     * @return Map of added features
     */
    public Map<String, SimpleFeature> getAdded() {
        return added;
    }

    protected Quadtree copySTRtreeFrom(Diff diff) {
        Quadtree tree = new Quadtree();

        synchronized (diff) {
            Iterator<Entry<String, SimpleFeature>> i = diff.added.entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, SimpleFeature> e = i.next();
                SimpleFeature f = (SimpleFeature) e.getValue();
                if (!diff.modifiedFeatures.containsKey(f.getID())) {
                    tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
                }
            }
            Iterator<Entry<String, SimpleFeature>> j = diff.getModified().entrySet().iterator();
            while (j.hasNext()) {
                Entry<String, SimpleFeature> e = j.next();
                SimpleFeature f = (SimpleFeature) e.getValue();
                tree.insert(ReferencedEnvelope.reference(f.getBounds()), f);
            }
        }

        return tree;
    }

    /**
     * A NullObject used to represent the absence of a SimpleFeature.
     *
     * <p>This class is used by TransactionStateDiff as a placeholder to represent features that
     * have been removed. The concept is generally useful and may wish to be taken out as a separate
     * class (used for example to represent deleted rows in a shapefile).
     */
    public static final SimpleFeature NULL =
            new SimpleFeature() {
                public Object getAttribute(String path) {
                    return null;
                }

                public Object getAttribute(int index) {
                    return null;
                }

                // public Object[] getAttributes(Object[] attributes) {
                // return null;
                // }

                public ReferencedEnvelope getBounds() {
                    return null;
                }

                public Geometry getDefaultGeometry() {
                    return null;
                }

                public SimpleFeatureType getFeatureType() {
                    return null;
                }

                public String getID() {
                    return null;
                }

                public FeatureId getIdentifier() {
                    return null;
                }

                // public int getNumberOfAttributes() {
                // return 0;
                // }

                public void setAttribute(int position, Object val) {}

                public void setAttribute(String path, Object attribute)
                        throws IllegalAttributeException {}

                // public void setDefaultGeometry(Geometry geometry)
                // throws IllegalAttributeException {
                // }

                public Object getAttribute(Name name) {
                    return null;
                }

                public int getAttributeCount() {
                    return 0;
                }

                public List<Object> getAttributes() {
                    return null;
                }

                public SimpleFeatureType getType() {
                    return null;
                }

                public void setAttribute(Name name, Object value) {}

                public void setAttributes(List<Object> values) {}

                public void setAttributes(Object[] values) {}

                public void setDefaultGeometry(Object geometry) {}

                public GeometryAttribute getDefaultGeometryProperty() {
                    return null;
                }

                public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {}

                public Collection<Property> getProperties(Name name) {
                    return null;
                }

                public Collection<Property> getProperties() {
                    return null;
                }

                public Collection<Property> getProperties(String name) {
                    return null;
                }

                public Property getProperty(Name name) {
                    return null;
                }

                public Property getProperty(String name) {
                    return null;
                }

                public Collection<? extends Property> getValue() {
                    return null;
                }

                public void setValue(Collection<Property> values) {}

                public AttributeDescriptor getDescriptor() {
                    return null;
                }

                public Name getName() {
                    return null;
                }

                public Map<Object, Object> getUserData() {
                    return null;
                }

                public boolean isNillable() {
                    return false;
                }

                public void setValue(Object newValue) {}

                public String toString() {
                    return "<NullFeature>";
                }

                public int hashCode() {
                    return 0;
                }

                public boolean equals(Object arg0) {
                    return arg0 == this;
                }

                public void validate() {}
            };
}
