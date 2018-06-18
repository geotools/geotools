/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Objects;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.NativeFilter;

/**
 * Default implementation of a native filter that works like an holder for the native expression.
 * This should be enough for most of the situations.
 */
public final class NativeFilterImpl implements NativeFilter {

    private final String nativeFilter;

    public NativeFilterImpl(String nativeFilter) {
        this.nativeFilter = nativeFilter;
    }

    @Override
    public String getNative() {
        return nativeFilter;
    }

    @Override
    public boolean evaluate(Object object) {
        throw new RuntimeException(
                String.format("Native filter '%s' can be executed in memory.", nativeFilter));
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        NativeFilterImpl that = (NativeFilterImpl) other;
        return Objects.equals(nativeFilter, that.nativeFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nativeFilter);
    }

    @Override
    public String toString() {
        return "[ " + nativeFilter + " ]";
    }
}
