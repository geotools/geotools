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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * Decorates a feature collection with one that filters content.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class FilteringSimpleFeatureCollection extends DecoratingSimpleFeatureCollection {

    /** The original feature collection. */
    SimpleFeatureCollection delegate;
    /** the filter */
    Filter filter;

    public FilteringSimpleFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> delegate, Filter filter) {
        this(DataUtilities.simple(delegate), filter);
    }

    public FilteringSimpleFeatureCollection(SimpleFeatureCollection delegate, Filter filter) {
        super(delegate);
        this.delegate = delegate;
        this.filter = filter;
    }

    @Override
    public SimpleFeatureIterator features() {
        return new FilteringSimpleFeatureIterator(delegate.features(), filter);
    }

    public void close(SimpleFeatureIterator close) {
        close.close();
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter subFilter = ff.and(this.filter, filter);

        return new FilteringSimpleFeatureCollection(delegate, subFilter);
    }

    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        // the delegate might have an optimized way to handle filters and still apply a
        // visitor (e.g., ContentFeatureCollection), but avoid self-looping
        final SimpleFeatureCollection sc = delegate.subCollection(filter);
        if (sc instanceof FilteringSimpleFeatureCollection) {
            super.accepts(visitor, progress);
        } else {
            sc.accepts(visitor, progress);
        }
    }

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int count = 0;
        try (SimpleFeatureIterator i = features()) {
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
    public <T> T[] toArray(T[] a) {
        List<SimpleFeature> list = new ArrayList<>();
        try (SimpleFeatureIterator i = features()) {
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

    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    @Override
    public ReferencedEnvelope getBounds() {
        // calculate manually
        return DataUtilities.bounds(this);
    }
}
