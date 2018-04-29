/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.selector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Id extends Selector {

    public static Selector combineAnd(List<Id> selectors, Object ctx) {
        Set<String> identifiers = new HashSet<>(selectors.get(0).ids);
        for (Id selector : selectors) {
            identifiers.retainAll(selector.ids);
        }
        if (identifiers.isEmpty()) {
            return REJECT;
        } else {
            return new Id(identifiers);
        }
    }

    public Set<String> ids;

    public Id(String... ids) {
        this.ids = new HashSet<>(Arrays.asList(ids));
    }

    public Id(Set<String> ids) {
        this.ids = ids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ids == null) ? 0 : ids.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Id other = (Id) obj;
        if (ids == null) {
            if (other.ids != null) return false;
        } else if (!ids.equals(other.ids)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Id [" + ids + "]";
    }

    @Override
    public Specificity getSpecificity() {
        if (ids.size() == 1) {
            return Specificity.ID_1;
        } else {
            return new Specificity(ids.size(), 0, 0);
        }
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }
}
