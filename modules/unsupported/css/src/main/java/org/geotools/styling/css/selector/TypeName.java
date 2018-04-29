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

import java.util.List;

public class TypeName extends Selector {

    public static final TypeName DEFAULT = new TypeName(null);

    public static Selector combineAnd(List<TypeName> selectors, Object context) {
        TypeName firstNonDefault = null;
        for (TypeName selector : selectors) {
            if (!DEFAULT.equals(selector)) {
                if (firstNonDefault == null) {
                    firstNonDefault = selector;
                } else if (!firstNonDefault.name.equals(selector.name)) {
                    return REJECT;
                }
            }
        }

        if (firstNonDefault != null) {
            return firstNonDefault;
        } else {
            return DEFAULT;
        }
    }

    public String name;

    public TypeName(String typename) {
        this.name = typename;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TypeName other = (TypeName) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public Specificity getSpecificity() {
        return Specificity.ELEMENT_1;
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "TypeName [name=" + name + "]";
    }
}
