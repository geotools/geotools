/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.shapefile.ng.index.CloseableIterator;

/**
 * Wraps a plain iterator into a closeable one.
 */
public class CloseableIteratorWrapper<E> implements CloseableIterator<E> {
    Iterator<E> delegate;

    public CloseableIteratorWrapper(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public E next() {
        return delegate.next();
    }

    public void remove() {
        delegate.remove();
    }

    public void close() throws IOException {
        // Just makes the API happy, the delegate does not really have a close method
    }

}
