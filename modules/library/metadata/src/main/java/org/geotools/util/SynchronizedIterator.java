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
package org.geotools.util;

import java.util.Iterator;


/**
 * An iterator synchronized on the given lock. The functionality is equivalent to the one provided
 * by {@link java.util.Collections#synchronizedSet}'s iterator, except that the synchronization is
 * performed on an arbitrary lock.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class SynchronizedIterator<E> implements Iterator<E> {
    /**
     * The wrapped iterator.
     */
    private final Iterator<E> iterator;

    /**
     * The lock.
     */
    private final Object lock;

    SynchronizedIterator(final Iterator<E> iterator, final Object lock) {
        this.iterator = iterator;
        this.lock = lock;
    }

    /**
     * Returns {@code true} if there is more elements to iterate over.
     */
    public boolean hasNext() {
        synchronized (lock) {
            return iterator.hasNext();
        }
    }

    /**
     * Returns the next element in iteratior order.
     */
    public E next() {
        synchronized (lock) {
            return iterator.next();
        }
    }

    /**
     * Removes the last iterated element.
     */
    public void remove() {
        synchronized (lock) {
            iterator.remove();
        }
    }

}
