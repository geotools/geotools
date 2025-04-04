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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * SimpleFeatureCollection wrapper which limits the number of features returned.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class MaxSimpleFeatureCollection extends DecoratingSimpleFeatureCollection {

    SimpleFeatureCollection delegate;
    long start;
    long max;

    public MaxSimpleFeatureCollection(FeatureCollection<SimpleFeatureType, SimpleFeature> delegate, long max) {
        this(DataUtilities.simple(delegate), 0, max);
    }

    public MaxSimpleFeatureCollection(SimpleFeatureCollection delegate, long max) {
        this(delegate, 0, max);
    }

    public MaxSimpleFeatureCollection(SimpleFeatureCollection delegate, long start, long max) {
        super(delegate);
        this.delegate = delegate;
        this.start = start;
        this.max = max;
    }

    FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    @Override
    public SimpleFeatureIterator features() {
        return new MaxFeaturesSimpleFeatureIterator(delegate.features(), start, max);
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int size = delegate.size();
        if (size < start) {
            return 0;
        } else {
            return (int) Math.min(size - start, max);
        }
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty() || max == 0 || delegate.size() - start < 1;
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        List<T> list = new ArrayList<>();
        try (SimpleFeatureIterator i = features()) {
            while (i.hasNext()) {
                list.add((T) i.next());
            }
            return list.toArray(a);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ReferencedEnvelope getBounds() {
        // calculate manually
        return DataUtilities.bounds(this);
    }
}
