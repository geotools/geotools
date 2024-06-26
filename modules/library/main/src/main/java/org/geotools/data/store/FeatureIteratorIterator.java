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
package org.geotools.data.store;

import java.util.Iterator;
import org.geotools.api.feature.Feature;
import org.geotools.feature.FeatureIterator;

/**
 * An {@link Iterator} which delegates to a {@link FeatureIterator}.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @since 2.4
 */
public class FeatureIteratorIterator<F extends Feature> implements Iterator<F> {

    /** delegate feature iterator. */
    FeatureIterator<F> delegate;

    /**
     * Creates the new iterator.
     *
     * @param delegate The iterator to delegate to.
     */
    public FeatureIteratorIterator(FeatureIterator<F> delegate) {
        this.delegate = delegate;
    }

    /** Calls through to {@link FeatureIterator#hasNext()} */
    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    /** Calls through to {@link FeatureIterator#next()} */
    @Override
    public F next() {
        return delegate.next();
    }

    /** @throws UnsupportedOperationException */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove is not supported");
    }

    /** @return The underlying delegate. */
    public FeatureIterator<F> getDelegate() {
        return delegate;
    }
}
