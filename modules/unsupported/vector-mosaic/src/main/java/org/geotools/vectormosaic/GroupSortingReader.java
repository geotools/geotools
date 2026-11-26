/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.sort.SortedFeatureReader;

/**
 * Completes the sort of a reader already sorted on the index attributes. Consecutive features with the same index
 * values form a group, and without index sort keys the whole mosaic is one group. A group from one granule passes
 * through when the granule store sorted it. Any other group is sorted again, on the full sort. The reader stops after
 * startIndex + maxFeatures features, so it never reads the groups after that.
 */
class GroupSortingReader implements SimpleFeatureReader {

    private final SimpleFeatureReader indexSorted;
    private final GranuleSourceProvider granules;
    private final Query groupQuery;
    private long remaining;

    /** The first feature of the next group, read while looking for the end of the current one */
    private SimpleFeature pending;

    private SimpleFeatureReader group;

    GroupSortingReader(SimpleFeatureReader indexSorted, Query query, GranuleSourceProvider granules) {
        this.indexSorted = indexSorted;
        this.granules = granules;
        this.remaining = SortedFeatureReader.getTopN(query);
        // the offset applies to the whole result, not to each group
        this.groupQuery = new Query(query);
        this.groupQuery.setStartIndex(null);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return indexSorted.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (remaining <= 0) {
            return false;
        }
        while (group == null || !group.hasNext()) {
            if (group != null) {
                group.close();
                group = null;
            }
            if (!readPending()) {
                return false;
            }
            group = sortGroup();
        }
        return true;
    }

    /** Reads the next feature into pending, unless one is already waiting. Returns false at the end of the input. */
    private boolean readPending() throws IOException {
        if (pending == null && indexSorted.hasNext()) {
            pending = indexSorted.next();
        }
        return pending != null;
    }

    /** Sorts the group that starts with the pending feature. The sort keeps only the features the query still needs. */
    private SimpleFeatureReader sortGroup() throws IOException {
        // the provider opened the granule of pending last, so isGranuleSorted is about that granule
        SimpleFeatureReader groupReader = new GroupReader(granules.getGroupValues(pending));
        if (granules.isGranuleSorted()) {
            return groupReader;
        }
        if (remaining != Long.MAX_VALUE) {
            groupQuery.setMaxFeatures((int) Math.min(remaining, Integer.MAX_VALUE));
        }
        return new SortedFeatureReader(groupReader, groupQuery);
    }

    @Override
    public SimpleFeature next() throws IOException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more features");
        }
        remaining--;
        return group.next();
    }

    @Override
    public void close() throws IOException {
        try (indexSorted) {
            // a group sorted on disk deletes its files on close
            if (group != null) {
                group.close();
            }
        }
    }

    /** Reads the features sharing the given index values. Stops at the first feature of the next group. */
    private class GroupReader implements SimpleFeatureReader {

        private final List<Object> indexValues;

        GroupReader(List<Object> indexValues) {
            this.indexValues = indexValues;
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return indexSorted.getFeatureType();
        }

        @Override
        public boolean hasNext() throws IOException {
            // Java equals decides the groups. A database collation that sorts "a" and "A" as equal can split a group.
            return readPending() && indexValues.equals(granules.getGroupValues(pending));
        }

        @Override
        public SimpleFeature next() throws IOException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more features in the group");
            }
            SimpleFeature feature = pending;
            pending = null;
            return feature;
        }

        @Override
        public void close() {
            // the sorter closes its input, but the outer reader still needs indexSorted
        }
    }
}
