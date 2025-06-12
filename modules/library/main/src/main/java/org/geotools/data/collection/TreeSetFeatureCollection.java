/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

// J2SE interfaces

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.FeatureIteratorImpl;
import org.geotools.feature.collection.SimpleFeatureIteratorImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Origional implementation of FeatureCollection using a TreeMap for internal storage.
 *
 * <p>The features are maintained in an internal TreeMap resuling in a collection that is sorted by feature id mimicking
 * the contents of a shapefile.
 *
 * <p>This implementation of FeatureCollection is painfully correct.
 *
 * <ul>
 *   <li>To better mimic an actual data file each feature that is returned is a copy
 *   <li>The pain comes if you were expecting performance - the overhead associated with copying is significant
 *   <li>Since a TreeSet (and not a spatial index) is used to store contents their this feature collection does not
 *       support fast spatial operations.
 * </ul>
 *
 * With this in mind this implementation is recommended for being careful or you are encountering problems between
 * threads when debugging. It is excellent for its intended purpose of test cases.
 *
 * @author Ian Schneider
 */
public class TreeSetFeatureCollection implements SimpleFeatureCollection {
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TreeSetFeatureCollection.class);

    /**
     * Contents of collection, referenced by FeatureID.
     *
     * <p>This use will result in collections that are sorted by FID, in keeping with shapefile etc...
     */
    private SortedMap<String, SimpleFeature> contents = new TreeMap<>();

    /** Internal envelope of bounds. */
    private ReferencedEnvelope bounds = null;

    /** listeners */
    // protected List<CollectionListener> listeners = new ArrayList<CollectionListener>();

    /** id used when serialized to gml */
    protected String id;

    /** FeatureType of contents. */
    protected SimpleFeatureType schema;

    /** FeatureCollection schema will be defined by the first added feature. */
    public TreeSetFeatureCollection() {
        this(null, null);
    }

    /**
     * This constructor should not be used by client code.
     *
     * @param collection SimpleFeatureCollection to copy into memory
     */
    public TreeSetFeatureCollection(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        this(collection.getID(), collection.getSchema());
        addAll(collection);
    }

    /**
     * This constructor should not be used by client code.
     *
     * <p>Opportunistic reuse is encouraged, but only for the purposes of testing or other specialized uses. Normal
     * creation should occur through <code>
     * org.geotools.core.FeatureCollections.newCollection()</code> allowing applications to customize any generated
     * collections.
     *
     * @param id may be null ... feature id
     * @param memberType optional, may be null
     */
    public TreeSetFeatureCollection(String id, SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }

    /**
     * Gets the bounding box for the features in this feature collection.
     *
     * @return the envelope of the geometries contained by this feature collection.
     */
    @Override
    public ReferencedEnvelope getBounds() {
        if (bounds == null) {
            bounds = new ReferencedEnvelope();

            for (SimpleFeature simpleFeature : contents.values()) {
                BoundingBox geomBounds = simpleFeature.getBounds();
                // IanS - as of 1.3, JTS expandToInclude ignores "null" Envelope
                // and simply adds the new bounds...
                // This check ensures this behavior does not occur.
                if (!geomBounds.isEmpty()) {
                    bounds.include(geomBounds);
                }
            }
        }
        return bounds;
    }

    /**
     * Ensures that this collection contains the specified element (optional operation). Returns <tt>true</tt> if this
     * collection changed as a result of the call. (Returns <tt>false</tt> if this collection does not permit duplicates
     * and already contains the specified element.)
     *
     * <p>Collections that support this operation may place limitations on what elements may be added to this
     * collection. In particular, some collections will refuse to add <tt>null</tt> elements, and others will impose
     * restrictions on the type of elements that may be added. Collection classes should clearly specify in their
     * documentation any restrictions on what elements may be added.
     *
     * <p>If a collection refuses to add a particular element for any reason other than that it already contains the
     * element, it <i>must</i> throw an exception (rather than returning <tt>false</tt> ). This preserves the invariant
     * that a collection always contains the specified element after this call returns.
     *
     * @param feature element whose presence in this collection is to be ensured.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    public boolean add(SimpleFeature feature) {
        // This cast is neccessary to keep with the contract of Set!
        if (feature == null) return false; // cannot add null!
        final String ID = feature.getID();
        if (ID == null) return false; // ID is required!
        if (contents.containsKey(ID)) return false; // feature all ready present

        if (this.schema == null) {
            this.schema = feature.getFeatureType();
        }
        SimpleFeatureType childType = getSchema();
        // if ( childType==null ){
        // //this.childType=
        // }else{
        if (!feature.getFeatureType().equals(childType))
            LOGGER.warning("Feature Collection contains a heterogeneous" + " mix of features");

        // }
        // TODO check inheritance with FeatureType here!!!
        contents.put(ID, feature);
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection (optional operation). The behavior of
     * this operation is undefined if the specified collection is modified while the operation is in progress. (This
     * implies that the behavior of this call is undefined if the specified collection is this collection, and this
     * collection is nonempty.)
     *
     * @param collection elements to be inserted into this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #add(Object)
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection collection) {
        // TODO check inheritance with FeatureType here!!!
        boolean changed = false;

        Iterator<SimpleFeature> iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature f = iterator.next();
                boolean added = add(f);
                changed |= added;
            }
            return changed;
        } finally {
            DataUtilities.close(iterator);
        }
    }

    public boolean addAll(FeatureCollection<?, ?> collection) {
        // TODO check inheritance with FeatureType here!!!
        boolean changed = false;

        try (FeatureIterator<?> iterator = collection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature f = (SimpleFeature) iterator.next();
                boolean added = add(f);
                changed |= added;
            }
            return changed;
        }
    }

    /**
     * Removes all of the elements from this collection (optional operation). This collection will be empty after this
     * method returns unless it throws an exception.
     */
    public void clear() {
        if (contents.isEmpty()) return;
        contents.clear();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element. More formally, returns <tt>true</tt> if
     * and only if this collection contains at least one element <tt>e</tt> such that <tt>(o==null ? e==null :
     * o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested.
     * @return <tt>true</tt> if this collection contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        // The contract of Set doesn't say we have to cast here, but I think its
        // useful for client sanity to get a ClassCastException and not just a
        // false.
        if (!(o instanceof SimpleFeature)) return false;

        SimpleFeature feature = (SimpleFeature) o;
        final String ID = feature.getID();

        return contents.containsKey(ID); // || contents.containsValue( feature );
    }

    /**
     * Test for collection membership.
     *
     * @return true if collection is completly covered
     */
    @Override
    public boolean containsAll(Collection collection) {
        Iterator iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                if (!contents.containsKey(feature.getID())) {
                    return false;
                }
            }
            return true;
        } finally {
            DataUtilities.close(iterator);
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this collection. There are no guarantees concerning the order in which
     * the elements are returned (unless this collection is an instance of some class that provides a guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    public Iterator<SimpleFeature> iterator() {
        final Iterator<SimpleFeature> iterator = contents.values().iterator();

        return new Iterator<>() {
            SimpleFeature currFeature = null;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public SimpleFeature next() {
                currFeature = iterator.next();
                return currFeature;
            }

            @Override
            public void remove() {
                iterator.remove();
                bounds = null; // recalc
            }
        };
    }

    /**
     * Gets a SimpleFeatureIterator of this feature collection. This allows iteration without having to cast.
     *
     * @return the SimpleFeatureIterator for this collection.
     */
    @Override
    public SimpleFeatureIterator features() {
        return new SimpleFeatureIteratorImpl(contents.values());
    }

    /**
     * Removes a single instance of the specified element from this collection, if it is present (optional operation).
     * More formally, removes an element <tt>e</tt> such that <tt>(o==null ? e==null : o.equals(e))</tt>, if this
     * collection contains one or more such elements. Returns true if this collection contained the specified element
     * (or equivalently, if this collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    public boolean remove(Object o) {
        if (!(o instanceof SimpleFeature)) return false;

        SimpleFeature f = (SimpleFeature) o;
        boolean changed = contents.values().remove(f);

        //        if (changed) {
        //            fireChange(f, CollectionEvent.FEATURES_REMOVED);
        //        }
        return changed;
    }

    /**
     * Removes all this collection's elements that are also contained in the specified collection (optional operation).
     * After this call returns, this collection will contain no elements in common with the specified collection.
     *
     * @param collection elements to be removed from this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection collection) {
        boolean changed = false;
        Iterator iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature f = (SimpleFeature) iterator.next();
                boolean removed = contents.values().remove(f);

                if (removed) {
                    changed = true;
                }
            }
            return changed;
        } finally {
            DataUtilities.close(iterator);
        }
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection (optional operation).
     * In other words, removes from this collection all of its elements that are not contained in the specified
     * collection.
     *
     * @param collection elements to be retained in this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection collection) {
        boolean modified = false;

        for (Iterator it = contents.values().iterator(); it.hasNext(); ) {
            SimpleFeature f = (SimpleFeature) it.next();
            if (!collection.contains(f)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Returns the number of elements in this collection. If this collection contains more than
     * <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return contents.size();
    }

    /**
     * Returns an array containing all of the elements in this collection. If the collection makes any guarantees as to
     * what order its elements are returned by its iterator, this method must return the elements in the same order.
     *
     * <p>The returned array will be "safe" in that no references to it are maintained by this collection. (In other
     * words, this method must allocate a new array even if this collection is backed by an array). The caller is thus
     * free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    @Override
    public Object[] toArray() {
        return contents.values().toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection; the runtime type of the returned array is
     * that of the specified array. If the collection fits in the specified array, it is returned therein. Otherwise, a
     * new array is allocated with the runtime type of the specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare (i.e., the array has more elements than this
     * collection), the element in the array immediately following the end of the collection is set to <tt>null</tt>.
     * This is useful in determining the length of this collection <i>only</i> if the caller knows that this collection
     * does not contain any <tt>null</tt> elements.)
     *
     * <p>If this collection makes any guarantees as to what order its elements are returned by its iterator, this
     * method must return the elements in the same order.
     *
     * <p>Like the <tt>toArray</tt> method, this method acts as bridge between array-based and collection-based APIs.
     * Further, this method allows precise control over the runtime type of the output array, and may, under certain
     * circumstances, be used to save allocation costs
     *
     * <p>Suppose <tt>l</tt> is a <tt>List</tt> known to contain only strings. The following code can be used to dump
     * the list into a newly allocated array of <tt>String</tt>:
     *
     * <pre>
     * String[] x = (String[]) v.toArray(new String[0]);
     * </pre>
     *
     * <p>Note that <tt>toArray(new Object[0])</tt> is identical in function to <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of this collection are to be stored, if it is big enough; otherwise, a
     *     new array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this collection
     */
    @Override
    public <O> O[] toArray(O[] a) {
        @SuppressWarnings("unchecked")
        O[] cast = (O[]) new Object[contents.size()];
        return contents.values().toArray(a != null ? a : cast);
    }

    public void close(FeatureIterator<SimpleFeature> close) {
        if (close instanceof FeatureIteratorImpl) {
            FeatureIteratorImpl<SimpleFeature> wrapper = (FeatureIteratorImpl<SimpleFeature>) close;
            wrapper.close();
        }
    }

    public void close(Iterator close) {
        // nop
    }

    @SuppressWarnings("PMD.CloseResource") // closed in the wrapper
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        final SimpleFeatureIterator iterator = features();
        return new FeatureReader<>() {
            @Override
            public SimpleFeatureType getFeatureType() {
                return getSchema();
            }

            @Override
            public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
                return iterator.next();
            }

            @Override
            public boolean hasNext() throws IOException {
                return iterator.hasNext();
            }

            @Override
            public void close() throws IOException {
                TreeSetFeatureCollection.this.close(iterator);
            }
        };
    }

    public int getCount() throws IOException {
        return contents.size();
    }

    public SimpleFeatureCollection collection() throws IOException {
        TreeSetFeatureCollection copy = new TreeSetFeatureCollection(null, getSchema());
        List<SimpleFeature> list = new ArrayList<>(contents.size());
        try (SimpleFeatureIterator iterator = features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeature duplicate;
                try {
                    duplicate = SimpleFeatureBuilder.copy(feature);
                } catch (IllegalAttributeException e) {
                    throw new DataSourceException("Unable to copy " + feature.getID(), e);
                }
                list.add(duplicate);
            }
        }
        copy.addAll(list);
        return copy;
    }

    /**
     * Optimization time ... grab the fid set so other can quickly test membership during removeAll/retainAll
     * implementations.
     *
     * @return Set of fids.
     */
    public Set<String> fids() {
        return Collections.unmodifiableSet(contents.keySet());
    }

    @Override
    public void accepts(
            org.geotools.api.feature.FeatureVisitor visitor, org.geotools.api.util.ProgressListener progress)
            throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    /**
     * Will return an optimized subCollection based on access to the origional MemoryFeatureCollection.
     *
     * <p>This method is intended in a manner similar to subList, example use: <code>
     * collection.subCollection( myFilter ).clear()
     * </code>
     *
     * @param filter Filter used to determine sub collection.
     * @since GeoTools 2.2, Filter 1.1
     */
    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        CollectionFeatureSource temp = new CollectionFeatureSource(this);
        return temp.getFeatures(filter);
    }

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        Query subQuery = new Query(getSchema().getTypeName());
        subQuery.setSortBy(order);

        CollectionFeatureSource temp = new CollectionFeatureSource(this);
        return temp.getFeatures(subQuery);
    }

    public void purge() {
        // no resources were harmed in the making of this FeatureCollection
    }

    @Override
    public String getID() {
        return id;
    }

    //    public final void addListener(CollectionListener listener) throws NullPointerException {
    //        listeners.add(listener);
    //    }
    //
    //    public final void removeListener(CollectionListener listener) throws NullPointerException
    // {
    //        listeners.remove(listener);
    //    }

    @Override
    public SimpleFeatureType getSchema() {
        return schema;
    }
}
