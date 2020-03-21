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
package org.geotools.filter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** @author jdeolive */
public abstract class BinaryLogicAbstract extends AbstractFilter {
    protected List<org.opengis.filter.Filter> children;

    protected BinaryLogicAbstract(List<org.opengis.filter.Filter> children) {
        this.children = children;
    }
    /** Returned list is unmodifieable. For a cheaper access option use visitor */
    public List<org.opengis.filter.Filter> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void setChildren(List<org.opengis.filter.Filter> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryLogicAbstract that = (BinaryLogicAbstract) o;
        if (children == null) return that.children == null;
        if (children.size() != that.children.size()) return false;
        return children.containsAll(that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
