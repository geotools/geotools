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

import java.io.Closeable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.feature.Feature;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureIterator;

/**
 * A feature iterator that completely delegates to a normal Iterator, simply allowing Java 1.4 code to escape the caste
 * (sic) system.
 *
 * <p>This implementation checks the iterator to see if it implements {@link Closeable} in order to allow for
 * collections that make use of system resources.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 */
public class DelegateFeatureIterator<F extends Feature> implements FeatureIterator<F> {
    Iterator<F> delegate;
    /**
     * Wrap the provided iterator up as a FeatureIterator.
     *
     * @param iterator Iterator to be used as a delegate.
     */
    public DelegateFeatureIterator(Iterator<F> iterator) {
        delegate = iterator;
    }

    @Override
    public boolean hasNext() {
        return delegate != null && delegate.hasNext();
    }

    @Override
    public F next() throws NoSuchElementException {
        if (delegate == null) throw new NoSuchElementException();
        return delegate.next();
    }

    @Override
    public void close() {
        DataUtilities.close(delegate);
        delegate = null;
    }
}
