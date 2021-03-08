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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * Default implementation of {@link FeatureCollection#sort(SortBy)}.
 *
 * <p>This implementation is not suitable for working with large content as it makes use of memory
 * both when eastablishing an initial sort order, and subsequently to hold a list of FeatureId.
 */
public class SubFeatureList extends SubFeatureCollection implements RandomFeatureAccess {
    /** Order by which content should be sorted */
    List<SortBy> sort;

    /** List of FeatureIDs in sorted order */
    List<FeatureId> index;

    public SubFeatureList(SimpleFeatureCollection list, Filter filter) {
        this(list, filter, SortBy.NATURAL_ORDER);
    }

    public SubFeatureList(SimpleFeatureCollection list, SortBy sort) {
        this(list, Filter.INCLUDE, sort);
    }

    /** Create a simple SubFeatureList with the provided filter. */
    public SubFeatureList(SimpleFeatureCollection list, Filter filter, SortBy subSort) {
        super(list, filter);

        if (subSort == null || subSort.equals(SortBy.NATURAL_ORDER)) {
            sort = Collections.emptyList();
        } else {
            sort = new ArrayList<>();
            if (collection instanceof SubFeatureList) {
                SubFeatureList sorted = (SubFeatureList) collection;
                sort.addAll(sorted.sort);
            }
            sort.add(subSort);
        }
        index = createIndex();
    }

    public SubFeatureList(SimpleFeatureCollection list, List<FeatureId> order) {
        super(list);

        index = order;
        filter = null;
    }

    /**
     * item at the specified index.
     *
     * @param position index of item
     * @return the item at the specified index.
     * @throws IndexOutOfBoundsException if index is not between 0 and size
     */
    public SimpleFeature get(int position) {
        FeatureId fid = index.get(position);
        if (collection instanceof RandomFeatureAccess) {
            RandomFeatureAccess random = (RandomFeatureAccess) collection;
            return random.getFeatureMember(fid.getID());
        }
        try (SimpleFeatureIterator it = collection.features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (id.equals(feature.getID())) {
                    return feature;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    /** Lazy create a filter based on index */
    @Override
    protected Filter createFilter() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Set<FeatureId> featureIds = new HashSet<>();
        for (FeatureId fid : index) {
            featureIds.add(ff.featureId(fid.getID()));
        }
        Id fids = ff.id(featureIds);

        return fids;
    }

    /** Put this SubFeatureList in touch with its inner index */
    protected List<FeatureId> createIndex() {
        List<FeatureId> fids = new ArrayList<>();
        try (SimpleFeatureIterator it = collection.features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (filter.evaluate(feature)) {
                    fids.add(feature.getIdentifier());
                }
            }
            if (sort != null && !sort.isEmpty()) {
                final SortBy initialOrder = sort.get(sort.size() - 1);
                final FeatureIdAccessor idAccessor = new FeatureIdAccessor(true);
                Collections.sort(
                        fids,
                        new Comparator<FeatureId>() {
                            @Override
                            public int compare(FeatureId key1, FeatureId key2) {
                                SimpleFeature feature1 = idAccessor.getFeature(key1.getID());
                                SimpleFeature feature2 = idAccessor.getFeature(key2.getID());

                                int compare = compare(feature1, feature2, initialOrder);
                                if (compare == 0 && sort.size() > 1) {
                                    for (int i = sort.size() - 1; compare == 0 && i >= 0; i--) {
                                        compare = compare(feature1, feature2, sort.get(i));
                                    }
                                }
                                return compare;
                            }

                            @SuppressWarnings("unchecked")
                            protected int compare(
                                    SimpleFeature feature1, SimpleFeature feature2, SortBy order) {
                                PropertyName name = order.getPropertyName();
                                Comparable value1 = (Comparable) name.evaluate(feature1);
                                Comparable value2 = (Comparable) name.evaluate(feature2);

                                if (value1 == value2) {
                                    return 0;
                                }
                                if (order.getSortOrder() == SortOrder.ASCENDING) {
                                    if (value1 == null) {
                                        return -1;
                                    }
                                    return value1.compareTo(value2);
                                } else {
                                    if (value2 == null) {
                                        return -1;
                                    }
                                    return value2.compareTo(value1);
                                }
                            }
                        });
            }
        }
        return fids;
    }

    public int indexOf(SimpleFeature feature) {
        return index.indexOf(feature.getIdentifier());
    }

    public int lastIndexOf(SimpleFeature feature) {
        return index.lastIndexOf(feature.getIdentifier());
    }

    //
    // Feature Collection methods
    //
    /**
     * Sublist of this sublist!
     *
     * <p>Implementation will ensure this does not get out of hand, order is maintained and only
     * indexed once.
     */
    public SimpleFeatureCollection subList(Filter subfilter) {
        if (filter.equals(Filter.INCLUDE)) {
            return this;
        }
        if (filter.equals(Filter.EXCLUDE)) {
            return new EmptyFeatureCollection(schema);
        }
        return new SubFeatureList(collection, ff.and(filter, subfilter), sort.get(0));
    }

    //
    // RandomFeatureAccess
    //

    @Override
    public SimpleFeature getFeatureMember(String id) throws NoSuchElementException {
        int position = index.indexOf(ff.featureId(id));
        if (position == -1) {
            throw new NoSuchElementException(id);
        }
        return new FeatureIdAccessor(false).getFeature(id);
    }

    /**
     * Returns a quick iterator that uses get and size methods.
     *
     * <p>As with all resource collections it is assumed that the iterator will be closed after use.
     *
     * @return an iterator over the elements in this list in proper sequence.
     * @see #modCount
     */
    public Iterator<SimpleFeature> openIterator() {
        return new SortedIteratory();
    }

    private class SortedIteratory implements Iterator<SimpleFeature> {
        Iterator<FeatureId> iterator = index.iterator();
        String id;
        FeatureIdAccessor idAccessor = new FeatureIdAccessor(true);

        @Override
        public boolean hasNext() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public SimpleFeature next() {
            FeatureId fid = iterator.next();
            id = fid.getID();
            return idAccessor.getFeature(id);
        }

        @Override
        public void remove() {
            removeFeatureMember(id);
        }
    }

    private class FeatureIdAccessor {
        SoftValueHashMap<String, SimpleFeature> featureCache;
        private boolean cacheFeatures;

        public FeatureIdAccessor(boolean cacheFeatures) {
            this.cacheFeatures = cacheFeatures;
            if (cacheFeatures) {
                featureCache = new SoftValueHashMap<>();
            }
        }

        protected SimpleFeature getFeature(String id) {
            if (collection instanceof RandomFeatureAccess) {
                RandomFeatureAccess random = (RandomFeatureAccess) collection;
                return random.getFeatureMember(id);
            } else if (cacheFeatures) {
                // check in the cache
                SimpleFeature result = featureCache.get(id);
                if (result != null) {
                    return result;
                }

                // sigh, full scan needed
                try (SimpleFeatureIterator it = collection.features()) {
                    while (it.hasNext()) {
                        SimpleFeature feature = it.next();
                        featureCache.put(id, feature);
                        if (id.equals(feature.getID())) {
                            return feature;
                        }
                    }
                }

                throw new RuntimeException("Could not find feature with id " + id);
            } else {
                // full scan...
                try (SimpleFeatureIterator it = collection.features()) {
                    while (it.hasNext()) {
                        SimpleFeature feature = it.next();
                        if (id.equals(feature.getID())) {
                            return feature;
                        }
                    }
                }

                throw new RuntimeException("Could not find feature with id " + id);
            }
        }
    }

    @Override
    public SimpleFeature removeFeatureMember(String id) {
        throw new UnsupportedOperationException();
    }
}
