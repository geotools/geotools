/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2012, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.FilteringFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;

/**
 * Implement a feature collection just based on provision of a {@link FeatureIterator}.
 * <p>
 * This implementation asks you to implement:
 * <ul>
 * <li>{@link #features()}</li>
 * <li>
 * This is the direct decentent of the origional {@link AbstractFeatureCollection}
 * and represents the easiest way to package your content as a FeatureCollection.
 * <p>
 * As this class provides no optimization, it is strongly recommended that you implement
 * the following methods (which require a whole collection traversal):
 * <ul>
 * <li>{@link #size()}</li>
 * <li>{@link #getBounds()</li>
 * </ul>
 *
 * @author Jody Garnett (LISAsoft)
 *
 */
public abstract class BaseFeatureCollection<T extends FeatureType, F extends Feature>
        implements FeatureCollection<T, F> {
    /** id used when serialized to gml */
    protected String id;

    protected T schema;

    protected BaseFeatureCollection() {
        this(null, null);
    }

    protected BaseFeatureCollection(T schema) {
        this(schema, null);
    }

    protected BaseFeatureCollection(T schema, String id) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = schema;
    }

    public String getID() {
        return id;
    }

    public T getSchema() {
        return schema;
    }

    //
    // FeatureCollection - Feature Access
    //
    /**
     * Subclasses required to implement this method to traverse FeatureCollection contents.
     *
     * <p>Note that {@link FeatureIterator<F>#close()} is available to clean up after any resource
     * use required during traversal.
     */
    public abstract FeatureIterator<F> features();

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
        FeatureIterator<F> e = features();
        try {
            if (o == null) {
                while (e.hasNext()) {
                    if (e.next() == null) {
                        return true;
                    }
                }
            } else {
                while (e.hasNext()) {
                    if (o.equals(e.next())) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            e.close();
        }
    }

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
    public boolean containsAll(Collection<?> c) {
        FeatureIterator<F> e = features();
        try {
            while (e.hasNext()) {
                Feature feature = e.next();
                if (!c.contains(feature)) {
                    return false;
                }
            }
            return true;
        } finally {
            e.close();
        }
    }

    /** @return <tt>true</tt> if this collection contains no elements. */
    public boolean isEmpty() {
        FeatureIterator<F> iterator = features();
        try {
            return !iterator.hasNext();
        } finally {
            iterator.close();
        }
    }

    /**
     * Array of all the elements.
     *
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        Object[] result = new Object[size()];
        FeatureIterator<F> e = null;
        try {
            e = features();
            for (int i = 0; e.hasNext(); i++) result[i] = e.next();
            return result;
        } finally {
            if (e != null) {
                e.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <O> O[] toArray(O[] a) {
        int size = size();
        if (a.length < size) {
            a = (O[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        FeatureIterator<F> it = features();
        try {
            Object[] result = a;
            for (int i = 0; i < size; i++) result[i] = it.next();
            if (a.length > size) a[size] = null;
            return a;
        } finally {
            it.close();
        }
    }

    public void accepts(
            org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress)
            throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    //
    // Feature Collections API
    //
    /**
     * Convenience implementation that just wraps this collection into a {@link
     * FilteringFeatureCollection}. Subclasses might want to override this in case the filter can be
     * cascaded to their data sources.
     */
    public FeatureCollection<T, F> subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        return new FilteringFeatureCollection<T, F>(this, filter);
    }
    /**
     * Obtained sorted contents, only implemented for SimpleFeature at present.
     *
     * <p>This method only supports SimpleFeature at present, consider use of
     * FeatureSource.features( Query ).
     *
     * @param order Sort order
     * @return FeatureCollection sorted in the indicated order
     */
    @SuppressWarnings("unchecked")
    public FeatureCollection<T, F> sort(SortBy order) {
        if (getSchema() instanceof SimpleFeatureType) {
            // go for the most efficient way if possible, otherwise rely on pure in memory
            // sorting...
            SimpleFeatureCollection simple =
                    DataUtilities.simple(
                            (FeatureCollection<SimpleFeatureType, SimpleFeature>) this);
            return (FeatureCollection<T, F>)
                    new SortedSimpleFeatureCollection(simple, new SortBy[] {order});
        } else {
            // hmm... we don't even have a basic non simple collection... need to implement one
            // before going here
            throw new UnsupportedOperationException(
                    "Cannot sort on complex features at the moment");
        }
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return Number of items, or Interger.MAX_VALUE
     */
    public int size() {
        int count = 0;
        FeatureIterator<F> it = features();
        try {
            while (it.hasNext()) {
                @SuppressWarnings("unused")
                Feature feature = it.next();
                count++;
            }
        } finally {
            it.close();
        }
        return count;
    }

    /**
     * Full collection traversal to obtain bounds of FeatureCollection. Subclasees are strong
     * encouraged to override this expensive method (even if just to implement caching).
     */
    public ReferencedEnvelope getBounds() {
        ReferencedEnvelope bounds = null;
        FeatureIterator<F> it = features();
        try {
            while (it.hasNext()) {
                Feature feature = it.next();
                BoundingBox bbox = feature.getBounds();
                if (bbox != null) {
                    if (bounds == null) {
                        bounds = new ReferencedEnvelope(bbox);
                    } else {
                        bounds.include(bbox);
                    }
                }
            }
        } finally {
            it.close();
        }
        return bounds;
    }
}
