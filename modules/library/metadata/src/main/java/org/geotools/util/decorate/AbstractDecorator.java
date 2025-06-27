/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.decorate;

import java.io.Serializable;

/**
 * Generic delegating base class. Provides the following features:
 *
 * <ul>
 *   <li>null check for the delegate object
 *   <li>direct forwarding of {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()} to the delegate
 *   <li>implements the Wrapper interface for programmatic extraction
 * </ul>
 */
public class AbstractDecorator<D> implements Wrapper, Serializable {

    protected D delegate;

    public AbstractDecorator(D delegate) {
        if (delegate == null) throw new NullPointerException("Cannot delegate to a null object");
        this.delegate = delegate;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        // first drill down to the latest wrapper, then check if the last delegate actually
        // implements the required interface
        if (delegate instanceof Wrapper) return ((Wrapper) delegate).isWrapperFor(iface);
        else if (iface.isInstance(delegate)) return true;
        else return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws IllegalArgumentException {
        // first drill down to the latest wrapper, then check if the last delegate actually
        // implements the required interface and return it
        if (delegate instanceof Wrapper) return ((Wrapper) delegate).unwrap(iface);
        else if (iface.isInstance(delegate)) return (T) delegate;
        else throw new IllegalArgumentException("Cannot unwrap to the requested interface " + iface);
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + delegate + ']';
    }
}
