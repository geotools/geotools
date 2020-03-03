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
package org.geotools.feature;

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
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.FeatureIteratorImpl;
import org.geotools.feature.collection.SimpleFeatureIteratorImpl;
import org.geotools.feature.collection.SubFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;

/**
 * A basic implementation of SimpleFeatureCollection which use a {@link TreeMap} for its internal
 * storage.
 *
 * <p>This should be considered a MemoryFeatureCollection.
 *
 * @author Ian Schneider
 * @version $Id$
 */
public class DefaultFeatureCollection
        implements SimpleFeatureCollection, Collection<SimpleFeature> {
    protected static Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DefaultFeatureCollection.class);

    /**
     * Contents of collection, referenced by FeatureID.
     *
     * <p>This use will result in collections that are sorted by FID, in keeping with shapefile
     * etc...
     */
    private SortedMap<String, SimpleFeature> contents = new TreeMap<String, SimpleFeature>();

    /** Internal listener storage list */
    // private List listeners = new ArrayList(2);

    /** Internal envelope of bounds. */
    private ReferencedEnvelope bounds = null;

    // private String id; /// fid

    /**
     * Default implementation of Feature collection.
     *
     * <p>feature type determined by the first feature added.
     */
    public DefaultFeatureCollection() {
        this(null, null);
    }

    /**
     * Used to stage content in memory.
     *
     * <p>Client code is encouraged to use DataUtilities.collection( collection )
     *
     * @param collection SimpleFeatureCollection to copy into memory
     */
    public DefaultFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        this(collection.getID(), collection.getSchema());
        addAll(collection);
    }

    /**
     * Used to create a feature collection to stage content in memory.
     *
     * <p>The feature type will be determined by the first feature added.
     *
     * @param id may be null ... feature id
     */
    public DefaultFeatureCollection(String id) {
        this(id, null);
    }

    /**
     * Used to create a feature collection to stage content in memory.
     *
     * @param id may be null ... feature id
     * @param memberType optional, may be null
     */
    public DefaultFeatureCollection(String id, SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }

    /** id used when serialized to gml */
    protected String id;

    protected SimpleFeatureType schema;

    /**
     * Gets the bounding box for the features in this feature collection.
     *
     * @return the envelope of the geometries contained by this feature collection.
     */
    public ReferencedEnvelope getBounds() {
        if (bounds == null) {
            bounds = new ReferencedEnvelope();

            for (Iterator i = contents.values().iterator(); i.hasNext(); ) {
                BoundingBox geomBounds = ((SimpleFeature) i.next()).getBounds();
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
     * Ensures that this collection contains the specified element (optional operation). Returns
     * <tt>true</tt> if this collection changed as a result of the call. (Returns <tt>false</tt> if
     * this collection does not permit duplicates and already contains the specified element.)
     *
     * <p>Collections that support this operation may place limitations on what elements may be
     * added to this collection. In particular, some collections will refuse to add <tt>null</tt>
     * elements, and others will impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any restrictions on what
     * elements may be added.
     *
     * <p>If a collection refuses to add a particular element for any reason other than that it
     * already contains the element, it <i>must</i> throw an exception (rather than returning
     * <tt>false</tt>). This preserves the invariant that a collection always contains the specified
     * element after this call returns.
     *
     * @param o element whose presence in this collection is to be ensured.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    public boolean add(SimpleFeature o) {
        return add(o, true);
    }

    protected boolean add(SimpleFeature feature, boolean fire) {
        // This cast is necessary to keep with the contract of Set!
        if (feature == null) return false; // cannot add null!
        final String ID = feature.getID();
        if (ID == null) return false; // ID is required!
        if (contents.containsKey(ID)) return false; // feature all ready present

        if (this.schema == null) {
            this.schema = feature.getFeatureType();
        }
        SimpleFeatureType childType = (SimpleFeatureType) getSchema();
        if (!feature.getFeatureType().equals(childType)) {
            LOGGER.warning("Feature Collection contains a heterogeneous" + " mix of features");
        }
        // Check inheritance with FeatureType here?
        contents.put(ID, feature);
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection (optional operation).
     * The behavior of this operation is undefined if the specified collection is modified while the
     * operation is in progress. (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is nonempty.)
     *
     * @param collection elements to be inserted into this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends SimpleFeature> collection) {
        // TODO check inheritance with FeatureType here!!!
        boolean changed = false;

        Iterator<?> iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature f = (SimpleFeature) iterator.next();
                boolean added = add(f, false);
                changed |= added;
            }
            return changed;
        } finally {
            if (iterator instanceof FeatureIterator) {
                ((FeatureIterator<?>) iterator).close();
            }
        }
    }

    public boolean addAll(FeatureCollection<?, ?> collection) {
        // TODO check inheritance with FeatureType here!!!
        boolean changed = false;

        FeatureIterator<?> iterator = collection.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature f = (SimpleFeature) iterator.next();
                boolean added = add(f, false);
                changed |= added;
            }
            return changed;
        } finally {
            iterator.close();
        }
    }

    /**
     * Removes all of the elements from this collection (optional operation). This collection will
     * be empty after this method returns unless it throws an exception.
     */
    public void clear() {
        contents.clear();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element. More formally,
     * returns <tt>true</tt> if and only if this collection contains at least one element <tt>e</tt>
     * such that <tt>(o==null ? e==null : o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested.
     * @return <tt>true</tt> if this collection contains the specified element
     */
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
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                if (!contents.containsKey(feature.getID())) {
                    return false;
                }
            }
            return true;
        } finally {
            if (iterator instanceof FeatureIterator) {
                ((FeatureIterator<?>) iterator).close();
            }
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this collection. There are no guarantees concerning
     * the order in which the elements are returned (unless this collection is an instance of some
     * class that provides a guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    public Iterator<SimpleFeature> iterator() {
        // return contents.values().iterator();
        final Iterator<SimpleFeature> iterator = contents.values().iterator();
        return new Iterator<SimpleFeature>() {
            SimpleFeature currFeature = null;

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public SimpleFeature next() {
                currFeature = (SimpleFeature) iterator.next();
                return currFeature;
            }

            public void remove() {
                iterator.remove();
                bounds = null; // recalc
            }
        };
    }

    /**
     * Gets a SimpleFeatureIterator of this feature collection. This allows iteration without having
     * to cast.
     *
     * @return the SimpleFeatureIterator for this collection.
     */
    public SimpleFeatureIterator features() {
        return new SimpleFeatureIteratorImpl(contents.values());
    }

    /**
     * Removes a single instance of the specified element from this collection, if it is present
     * (optional operation). More formally, removes an element <tt>e</tt> such that <tt>(o==null ?
     * e==null : o.equals(e))</tt>, if this collection contains one or more such elements. Returns
     * true if this collection contained the specified element (or equivalently, if this collection
     * changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present.
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    public boolean remove(Object o) {
        if (!(o instanceof SimpleFeature)) return false;

        SimpleFeature f = (SimpleFeature) o;
        boolean changed = contents.values().remove(f);
        return changed;
    }

    /**
     * Removes all this collection's elements that are also contained in the specified collection
     * (optional operation). After this call returns, this collection will contain no elements in
     * common with the specified collection.
     *
     * @param collection elements to be removed from this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        Iterator<?> iterator = collection.iterator();
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
            if (iterator instanceof FeatureIterator) {
                ((FeatureIterator<?>) iterator).close();
            }
        }
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection
     * (optional operation). In other words, removes from this collection all of its elements that
     * are not contained in the specified collection.
     *
     * @param collection elements to be retained in this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        for (Iterator<?> it = contents.values().iterator(); it.hasNext(); ) {
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
    public int size() {
        return contents.size();
    }

    /**
     * Returns an array containing all of the elements in this collection. If the collection makes
     * any guarantees as to what order its elements are returned by its iterator, this method must
     * return the elements in the same order.
     *
     * <p>The returned array will be "safe" in that no references to it are maintained by this
     * collection. (In other words, this method must allocate a new array even if this collection is
     * backed by an array). The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    public Object[] toArray() {
        return contents.values().toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection; the runtime type of the
     * returned array is that of the specified array. If the collection fits in the specified array,
     * it is returned therein. Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare (i.e., the array has
     * more elements than this collection), the element in the array immediately following the end
     * of the collection is set to <tt>null</tt>. This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does not contain any
     * <tt>null</tt> elements.)
     *
     * <p>If this collection makes any guarantees as to what order its elements are returned by its
     * iterator, this method must return the elements in the same order.
     *
     * <p>Like the <tt>toArray</tt> method, this method acts as bridge between array-based and
     * collection-based APIs. Further, this method allows precise control over the runtime type of
     * the output array, and may, under certain circumstances, be used to save allocation costs
     *
     * <p>Suppose <tt>l</tt> is a <tt>List</tt> known to contain only strings. The following code
     * can be used to dump the list into a newly allocated array of <tt>String</tt>:
     *
     * <pre>
     *     String[] x = (String[]) v.toArray(new String[0]);
     * </pre>
     *
     * <p>Note that <tt>toArray(new Object[0])</tt> is identical in function to <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of this collection are to be stored, if it is big
     *     enough; otherwise, a new array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this collection
     */
    public <T> T[] toArray(T[] a) {
        return contents.values().toArray(a);
    }

    public void close(FeatureIterator<SimpleFeature> close) {
        if (close instanceof FeatureIteratorImpl) {
            FeatureIteratorImpl<SimpleFeature> wrapper = (FeatureIteratorImpl<SimpleFeature>) close;
            wrapper.close();
        }
    }

    //    public void close( Iterator close ) {
    //        // nop
    //    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        final SimpleFeatureIterator iterator = features();
        return new FeatureReader<SimpleFeatureType, SimpleFeature>() {
            public SimpleFeatureType getFeatureType() {
                return getSchema();
            }

            public SimpleFeature next()
                    throws IOException, IllegalAttributeException, NoSuchElementException {
                return iterator.next();
            }

            public boolean hasNext() throws IOException {
                return iterator.hasNext();
            }

            public void close() throws IOException {
                DefaultFeatureCollection.this.close(iterator);
            }
        };
    }

    public int getCount() throws IOException {
        return contents.size();
    }

    public SimpleFeatureCollection collection() throws IOException {
        DefaultFeatureCollection copy = new DefaultFeatureCollection(null, getSchema());
        List<SimpleFeature> list = new ArrayList<SimpleFeature>(contents.size());
        SimpleFeatureIterator iterator = features();
        try {
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
        } finally {
            iterator.close();
        }
        copy.addAll(list);
        return copy;
    }

    /**
     * Optimization time ... grab the fid set so other can quickly test membership during
     * removeAll/retainAll implementations.
     *
     * @return Set of fids.
     */
    public Set fids() {
        return Collections.unmodifiableSet(contents.keySet());
    }

    public void accepts(
            org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress)
            throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    /**
     * Will return an optimized subCollection based on access to the origional
     * MemoryFeatureCollection.
     *
     * <p>This method is intended in a manner similar to subList, example use: <code>
     * collection.subCollection( myFilter ).clear()
     * </code>
     *
     * @param filter Filter used to determine sub collection.
     * @since GeoTools 2.2, Filter 1.1
     */
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        return new SubFeatureCollection(this, filter);
    }

    /**
     * Construct a sorted view of this content.
     *
     * <p>Sorts may be combined togther in a stable fashion, in congruence with the Filter 1.1
     * specification.
     *
     * <p>This method should also be able to handle GeoTools specific sorting through detecting
     * order as a SortBy2 instance.
     *
     * @since GeoTools 2.2, Filter 1.1
     * @param order Filter 1.1 SortBy Construction of a Sort
     * @return FeatureList sorted according to provided order
     */
    public SimpleFeatureCollection sort(SortBy order) {
        if (order == SortBy.NATURAL_ORDER) {
            return this;
        }
        return null; // new OrderedFeatureList( order, compare );
    }

    public void purge() {
        // no resources were harmed in the making of this FeatureCollection
    }

    public void validate() {}

    public String getID() {
        return id;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }
}
