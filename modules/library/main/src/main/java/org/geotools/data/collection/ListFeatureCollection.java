/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * FeatureCollection implementation wrapping around a java.util.List.
 *
 * <p>This implementation wraps around a java.util.List and is suitable for quickly getting something on screen.
 *
 * <p>Usage notes:
 *
 * <ul>
 *   <li>This implementation does not use a spatial index, please do not expect spatial operations to be fast.
 *   <li>FeatureCollections are not allowed to have duplicates
 * </ul>
 *
 * <p>This implementation is intended to quickly wrap up a list of features and get them on screen; as such it respects
 * various hints about the copying of internal content as provided by the renderer.
 *
 * @see Hints#FEATURE_DETACHED
 * @author Oliver Gottwald
 * @author Jody
 */
public class ListFeatureCollection extends AbstractFeatureCollection implements Collection<SimpleFeature> {
    /** wrapped list of features containing the contents */
    protected List<SimpleFeature> list;

    /** Cached bounds */
    protected ReferencedEnvelope bounds = null;

    /** Create a ListFeatureCollection for the provided schema An ArrayList is used internally. */
    public ListFeatureCollection(SimpleFeatureType schema) {
        this(schema, new ArrayList<>());
    }
    /**
     * Create a ListFeatureCollection around the provided list. The contents of the list should all be of the provided
     * schema for this to make sense. Please keep in mind the feature collection constraints, no two Features in the
     * list should have the same feature id, and you should not insert the same feature more then once.
     *
     * <p>The provided list is directly used for storage, most feature collection operations just use a simple iterator
     * so there is no performance advantaged to be gained over using an ArrayList vs a LinkedList (other then for the
     * size() method of course).
     */
    public ListFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> list) {
        super(schema);
        this.list = list;
    }
    /**
     * Create a ListFeatureCollection around the provided array. The contents of the array should all be of the provided
     * schema for this to make sense. Please keep in mind the feature collection constraints, no two Features in the
     * list should have the same feature id, and you should not insert the same feature more then once.
     *
     * <p>The provided array is directly used with a {@link CopyOnWriteArrayList} for storage.
     */
    public ListFeatureCollection(SimpleFeatureType schema, SimpleFeature... array) {
        super(schema);
        this.list = new CopyOnWriteArrayList<>(array);
    }
    /**
     * Create a ListFeatureCollection around the provided list. The contents of the list should all be of the provided
     * schema for this to make sense. Please keep in mind the feature collection control, no two Features in the list
     * should have the same feature id, and you should not insert the same feature more then once.
     *
     * <p>The provided list is directly used for storage, most feature collection operations just use a simple iterator
     * so there is no performance advantaged to be gained over using an ArrayList vs a LinkedList (other then for the
     * size() method of course).
     */
    public ListFeatureCollection(SimpleFeatureCollection copy) throws IOException {
        this(copy.getSchema());
        copy.accepts(feature -> list.add((SimpleFeature) feature), null);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    protected Iterator<SimpleFeature> openIterator() {
        Iterator<SimpleFeature> it = list.iterator();
        return it;
    }

    @Override
    public boolean add(SimpleFeature f) {
        bounds = null; // reset
        return list.add(f);
    }

    @Override
    public void clear() {
        list.clear();
        // maintain the bounds
        bounds = null;
    }

    @Override
    public SimpleFeatureIterator features() {
        return new ListFeatureIterator(list);
    }

    @Override
    public synchronized ReferencedEnvelope getBounds() {
        if (bounds == null) {
            bounds = calculateBounds();
        }
        return bounds;
    }
    /** Calculate bounds from features */
    protected ReferencedEnvelope calculateBounds() {
        ReferencedEnvelope extent = ReferencedEnvelope.create(getSchema().getCoordinateReferenceSystem());
        for (SimpleFeature feature : list) {
            if (feature == null) {
                continue;
            }
            ReferencedEnvelope bbox = ReferencedEnvelope.reference(feature.getBounds());
            if (bbox == null || bbox.isEmpty() || bbox.isNull()) {
                continue;
            }
            extent.expandToInclude(bbox);
        }
        return extent;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
    /**
     * SimpleFeatureIterator that will use collection close method.
     *
     * @author Jody
     */
    private static class ListFeatureIterator implements SimpleFeatureIterator {
        private Iterator<SimpleFeature> iter;

        public ListFeatureIterator(List<SimpleFeature> features) {
            iter = features.iterator();
        }

        @Override
        public void close() {
            if (iter instanceof FeatureIterator) {
                ((FeatureIterator<?>) iter).close();
            }
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            return iter.next();
        }
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
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

    @Override
    public boolean remove(Object o) {
        boolean removed = list.remove(o);
        if (removed) {
            bounds = null; // maintain the bounds
        }
        return removed;
    }

    @Override
    public boolean addAll(Collection<? extends SimpleFeature> c) {
        boolean changed = false;
        for (SimpleFeature feature : c) {
            boolean added = add(feature);
            if (!changed && added) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        bounds = null;
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        bounds = null;
        return list.retainAll(c);
    }
}
