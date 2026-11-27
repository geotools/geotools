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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;

/** A property store that sorts the natural order natively. It records the sort of every query it reads. */
class SortingPropertyDataStore extends PropertyDataStore {

    final List<List<SortBy>> sorts = new ArrayList<>();

    SortingPropertyDataStore(File dir) {
        super(dir);
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) {
        return new SortingFeatureSource(entry);
    }

    private class SortingFeatureSource extends PropertyFeatureSource {

        SortingFeatureSource(ContentEntry entry) {
            super(entry, Query.ALL);
        }

        @Override
        protected QueryCapabilities buildQueryCapabilities() {
            return new QueryCapabilities() {
                @Override
                public boolean supportsSorting(SortBy... sortBy) {
                    return isNaturalOrder(sortBy);
                }
            };
        }

        @Override
        protected boolean canSort(Query query) {
            return isNaturalOrder(query.getSortBy());
        }

        private static boolean isNaturalOrder(SortBy... sortBy) {
            return sortBy != null && sortBy.length == 1 && sortBy[0] == SortBy.NATURAL_ORDER;
        }

        @Override
        protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
            List<SortBy> sort = query.getSortBy() == null ? List.of() : List.of(query.getSortBy());
            sorts.add(sort);
            return super.getReaderInternal(query);
        }
    }
}
