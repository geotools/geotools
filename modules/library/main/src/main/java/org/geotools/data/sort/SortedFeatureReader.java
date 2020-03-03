/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sort;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * FeatureReader used to sort contents.
 * <p>
 * The implementation makes use of {@link MergeSortDumper).
 *
 *
 */
public class SortedFeatureReader implements SimpleFeatureReader {

    SimpleFeatureReader delegate;

    /**
     * Checks if the schema and the sortBy are suitable for merge/sort. All attributes need to be
     * {@link Serializable}, all sorting attributes need to be {@link Comparable}
     */
    public static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        return MergeSortDumper.canSort(schema, sortBy);
    }

    /** Gets the max amount amount of features to keep in memory from the query and system hints */
    public static int getMaxFeaturesInMemory(Query q) {
        return MergeSortDumper.getMaxFeatures(q);
    }

    /**
     * Builds a new sorting feature reader
     *
     * @param reader The reader to be sorted
     * @param query The query holding the SortBy directives, and the eventual max features in memory
     *     hint {@link Hints#MAX_MEMORY_SORT}
     */
    public SortedFeatureReader(SimpleFeatureReader reader, Query query) throws IOException {
        this.delegate = MergeSortDumper.getDelegateReader(reader, query);
    }

    /**
     * Builds a new sorting feature reader
     *
     * @param reader The reader to be sorted
     * @param sortBy The sorting directives
     * @param maxFeatures The maximum number of features to keep in memory
     */
    public SortedFeatureReader(SimpleFeatureReader reader, SortBy[] sortBy, int maxFeatures)
            throws IOException {
        this.delegate = MergeSortDumper.getDelegateReader(reader, sortBy, maxFeatures);
    }

    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        return delegate.next();
    }

    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    public void close() throws IOException {
        delegate.close();
    }

    /** Builds a comparator that can be used to sort SimpleFeature instances in memory */
    public static Comparator<SimpleFeature> getComparator(SortBy[] sortBy) {
        // handle the easy cases, no sorting or natural sorting
        if (sortBy == SortBy.UNSORTED || sortBy == null) {
            return null;
        }

        // build a list of comparators
        List<Comparator<SimpleFeature>> comparators = new ArrayList<Comparator<SimpleFeature>>();
        for (SortBy sb : sortBy) {
            if (sb == SortBy.NATURAL_ORDER) {
                comparators.add(new FidComparator(true));
            } else if (sb == SortBy.REVERSE_ORDER) {
                comparators.add(new FidComparator(false));
            } else {
                String name = sb.getPropertyName().getPropertyName();
                boolean ascending = sb.getSortOrder() == SortOrder.ASCENDING;
                comparators.add(new PropertyComparator(name, ascending));
            }
        }

        // return the final comparator
        if (comparators.size() == 1) {
            return comparators.get(0);
        } else {
            return new CompositeComparator(comparators);
        }
    }
}
