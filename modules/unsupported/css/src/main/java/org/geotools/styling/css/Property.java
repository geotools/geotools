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
package org.geotools.styling.css;

import java.util.List;

/**
 * A CSS property, having a name and a list of {@link Value} associated to it
 *
 * @author Andrea Aime - GeoSolutions
 */
public class Property {

    boolean used;

    String name;

    List<Value> values;

    public Property(String name, List<Value> values) {
        super();
        this.setName(name);
        this.setValues(values);
    }

    @Override
    public String toString() {
        return "Property [name=" + getName() + ", values=" + getValues() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getValues() == null) ? 0 : getValues().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Property other = (Property) obj;
        if (getName() == null) {
            if (other.getName() != null) return false;
        } else if (!getName().equals(other.getName())) return false;
        if (getValues() == null) {
            if (other.getValues() != null) return false;
        } else if (!getValues().equals(other.getValues())) return false;
        return true;
    }

    /** True if the property has been used during the conversion */
    boolean isUsed() {
        return used;
    }

    /** The property name */
    public String getName() {
        return name;
    }

    /** The list of property values */
    public List<Value> getValues() {
        return values;
    }

    void setUsed(boolean used) {
        this.used = used;
    }

    void setName(String name) {
        this.name = name;
    }

    void setValues(List<Value> values) {
        this.values = values;
    }

    /**
     * Returns true if any of the property values is other than null or None (will return true also
     * if there is no value)
     */
    public boolean hasValues() {
        return values.stream().anyMatch(v -> v != null && v != Value.NONE);
    }
}
