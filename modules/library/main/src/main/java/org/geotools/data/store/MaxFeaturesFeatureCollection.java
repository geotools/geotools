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
package org.geotools.data.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * SimpleFeatureCollection wrapper which limits the number of features returned.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class MaxFeaturesFeatureCollection<T extends FeatureType, F extends Feature>
        extends DecoratingFeatureCollection<T, F> {

    FeatureCollection<T, F> delegate;
    long max;

    public MaxFeaturesFeatureCollection(FeatureCollection<T, F> delegate, long max) {
        super(delegate);
        this.delegate = delegate;
        this.max = max;
    }

    public FeatureReader<T, F> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    @Override
    public FeatureIterator<F> features() {
        return new MaxFeaturesIterator<>(delegate.features(), max);
    }

    @Override
    public FeatureCollection<T, F> subCollection(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureCollection<T, F> sort(SortBy order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return (int) Math.min(delegate.size(), max);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty() || max == 0;
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override
    public <O> O[] toArray(O[] a) {
        List<F> list = new ArrayList<>();
        try (FeatureIterator<F> i = features()) {
            while (i.hasNext()) {
                list.add(i.next());
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
