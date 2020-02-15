/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * Implement a feature collection just based on provision of iterator.
 *
 * <p>You will need to implement the following methods:
 *
 * <ul>
 *   <li>{@link #getBounds()}
 *   <li>{@link #size()}
 *   <li>{@link #closeIterator(Iterator)}
 *   <li>{@link #openIterator()}
 * </ul>
 *
 * @author Jody Garnett (LISAsoft)
 */
public abstract class AdaptorFeatureCollection implements SimpleFeatureCollection {

    public AdaptorFeatureCollection(String id, SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }

    //
    // SimpleFeatureCollection - Feature Access
    //
    public SimpleFeatureIterator features() {
        SimpleFeatureIterator iter = new DelegateSimpleFeatureIterator(openIterator());
        return iter;
    }

    public void close(FeatureIterator<SimpleFeature> close) {
        if (close != null) {
            close.close();
        }
    }

    public void close(SimpleFeatureIterator close) {
        if (close != null) {
            closeIterator(close);
        }
    }

    public void closeIterator(SimpleFeatureIterator close) {
        DelegateSimpleFeatureIterator iter = (DelegateSimpleFeatureIterator) close;
        closeIterator(iter.delegate);
        iter.close();
    }

    /** Accepts a visitor, which then visits each feature in the collection. */
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    //
    // Feature Collections API
    //
    public SimpleFeatureCollection subList(Filter filter) {
        return new SubFeatureList(this, filter);
    }

    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        return new SubFeatureCollection(this, filter);
    }

    public SimpleFeatureCollection sort(SortBy order) {
        return new SubFeatureList(this, order);
    }

    //
    // Resource Collection management
    //
    /** @return <tt>true</tt> if this collection contains no elements. */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element. <tt></tt>.
     *
     * <p>This implementation iterates over the elements in the collection, checking each element in
     * turn for equality with the specified element.
     *
     * @param o object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contains(Object o) {
        Iterator e = null;
        try {
            e = iterator();
            if (o == null) {
                while (e.hasNext()) if (e.next() == null) return true;
            } else {
                while (e.hasNext()) if (o.equals(e.next())) return true;
            }
            return false;
        } finally {
            close(e);
        }
    }

    /**
     * Array of all the elements.
     *
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator e = null;
        try {
            e = iterator();
            for (int i = 0; e.hasNext(); i++) result[i] = e.next();
            return result;
        } finally {
            close(e);
        }
    }

    public Object[] toArray(Object[] a) {
        int size = size();
        if (a.length < size)
            a =
                    (Object[])
                            java.lang.reflect.Array.newInstance(
                                    a.getClass().getComponentType(), size);

        Iterator it = iterator();
        try {

            Object[] result = a;
            for (int i = 0; i < size; i++) result[i] = it.next();
            if (a.length > size) a[size] = null;
            return a;
        } finally {
            close(it);
        }
    }

    // Bulk Operations

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements in the specified
     * collection.
     *
     * <p>
     *
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements in the specified
     *     collection.
     * @throws NullPointerException if the specified collection is null.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection c) {
        Iterator e = c.iterator();
        try {
            while (e.hasNext()) if (!contains(e.next())) return false;
            return true;
        } finally {
            close(e);
        }
    }
    //  String conversion

    /**
     * Returns a string representation of this collection.
     *
     * @return a string representation of this collection.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        Iterator i = iterator();
        try {
            boolean hasNext = i.hasNext();
            while (hasNext) {
                Object o = i.next();
                buf.append(o == this ? "(this Collection)" : String.valueOf(o));
                hasNext = i.hasNext();
                if (hasNext) buf.append(", ");
            }
            buf.append("]");
            return buf.toString();
        } finally {
            close(i);
        }
    }

    //
    // Contents
    //
    //
    /** id used when serialized to gml */
    protected String id;

    protected SimpleFeatureType schema;

    /**
     * Please implement!
     *
     * <p>Note: If you return a ResourceIterator, the default implemntation of close( Iterator )
     * will know what to do.
     */
    public final Iterator<SimpleFeature> iterator() {
        Iterator<SimpleFeature> iterator = openIterator();
        return iterator;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return Number of items, or Interger.MAX_VALUE
     */
    public abstract int size();

    /**
     * Clean up after any resources assocaited with this iteartor in a manner similar to JDO
     * collections. Example (safe) use:
     *
     * <pre><code>
     * Iterator iterator = collection.iterator();
     * try {
     *     for( Iterator i=collection.iterator(); i.hasNext();){
     *          Feature feature = (Feature) i.hasNext();
     *          System.out.println( feature.getID() );
     *     }
     * }
     * finally {
     *     collection.close( iterator );
     * }
     * </code></pre>
     */
    public final void close(Iterator close) {
        if (close == null) return;
        try {
            closeIterator(close);
        } catch (Throwable e) {
            // TODO Log e = ln
        }
    }
    /**
     * Open an Iterator, we will call close( iterator ).
     *
     * <p>Please subclass to provide your own iterator - note <code>iterator()</code> is implemented
     * to call <code>open()</code> and track the results in for later <code>purge()</code>.
     *
     * @return Iterator based on resource use
     */
    protected abstract Iterator<SimpleFeature> openIterator();

    /**
     * Please override to cleanup after your own iterators, and any used resources.
     *
     * <p>As an example if the iterator was working off a File then the inputstream should be
     * closed.
     *
     * <p>Subclass must call super.close( close ) to allow the list of open iterators to be
     * adjusted.
     *
     * @param close Iterator, will not be <code>null</code>
     */
    protected abstract void closeIterator(Iterator<SimpleFeature> close);

    public String getID() {
        return id;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    /** Subclasses need to override this. */
    public ReferencedEnvelope getBounds() {
        throw new UnsupportedOperationException(
                "Subclasses " + getClass().getSimpleName() + " should override");
    }
}
