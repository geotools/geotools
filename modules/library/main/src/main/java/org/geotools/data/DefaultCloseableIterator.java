/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Default implementation of CloseableIterator
 *
 * @param <T>
 */
public class DefaultCloseableIterator<T> implements CloseableIterator<T> {

    private static final Logger LOGGER = Logging.getLogger(DefaultCloseableIterator.class);

    protected final Iterator<T> wrapped;

    protected Closeable closeableItem;

    public DefaultCloseableIterator(Iterator<T> wrapped) {
        this.wrapped = wrapped;
        if (wrapped instanceof Closeable) {
            this.closeableItem = (Closeable) wrapped;
        } else {
            this.closeableItem = null;
        }
    }

    public DefaultCloseableIterator(Iterator<T> wrapped, Closeable closeable) {
        this.wrapped = wrapped;
        this.closeableItem = closeable;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = wrapped.hasNext();
        if (!hasNext) {
            // auto close
            close();
        }
        return hasNext;
    }

    @Override
    public T next() {
        return wrapped.next();
    }

    @Override
    public void remove() {
        wrapped.remove();
    }

    /** Closes the underlying iterator in case it implements {@code CloseableIterator}. */
    @Override
    public void close() {
        try {
            if (closeableItem != null) {
                closeableItem.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeableItem = null;
        }
    }

    @Override
    protected void finalize() {
        if (closeableItem != null) {
            try {
                close();
            } finally {
                LOGGER.warning(
                        "CloseableIterator need to be closed by the client. "
                                + "There is code not closing it."
                                + "\nAuto closing at finalize().");
            }
        }
    }
}
