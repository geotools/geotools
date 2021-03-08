/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.filter.visitor.BindingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Decorates a feature collection with one that filters content.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class FilteringFeatureCollection<T extends FeatureType, F extends Feature>
        extends DecoratingFeatureCollection<T, F> {

    /** The original feature collection. */
    FeatureCollection<T, F> delegate;
    /** the filter */
    Filter filter;

    public FilteringFeatureCollection(FeatureCollection<T, F> delegate, Filter filter) {
        super(delegate);
        this.delegate = delegate;
        this.filter = (Filter) filter.accept(new BindingFilterVisitor(delegate.getSchema()), null);
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
        int count = 0;
        try (FeatureIterator<F> i = features()) {
            while (i.hasNext()) {
                count++;
                i.next();
            }

            return count;
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
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
    public boolean contains(Object o) {
        return delegate.contains(o) && filter.evaluate(o);
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
    public FeatureIterator<F> features() {
        return new FilteringFeatureIterator<>(delegate.features(), filter);
    }

    public FeatureReader<T, F> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    @Override
    public ReferencedEnvelope getBounds() {
        // calculate manually
        return DataUtilities.bounds(this);
    }
}
