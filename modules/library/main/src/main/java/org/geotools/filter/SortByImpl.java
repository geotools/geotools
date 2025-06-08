/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;

public class SortByImpl implements SortBy {

    PropertyName propertyName;

    SortOrder sortOrder;

    public SortByImpl(PropertyName propertyName, SortOrder sortOrder) {
        this.propertyName = propertyName;
        this.sortOrder = sortOrder;
    }

    @Override
    public PropertyName getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(PropertyName propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (propertyName == null ? 0 : propertyName.hashCode());
        result = prime * result + (sortOrder == null ? 0 : sortOrder.hashCode());
        return result;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SortByImpl other = (SortByImpl) obj;
        if (propertyName == null) {
            if (other.propertyName != null) return false;
        } else if (!propertyName.equals(other.propertyName)) return false;
        if (sortOrder == null) {
            if (other.sortOrder != null) return false;
        } else if (!sortOrder.equals(other.sortOrder)) return false;
        return true;
    }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "SortByImpl [propertyName=" + propertyName + ", sortOrder=" + sortOrder + "]";
    }
}
