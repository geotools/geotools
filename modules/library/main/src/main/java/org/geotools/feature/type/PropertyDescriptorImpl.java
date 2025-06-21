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
package org.geotools.feature.type;

import java.util.HashMap;
import java.util.Map;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;

public class PropertyDescriptorImpl implements PropertyDescriptor {

    protected final PropertyType type;
    protected final Name name;
    protected final int minOccurs;
    protected final int maxOccurs;
    protected final boolean isNillable;
    final Map<Object, Object> userData;

    protected PropertyDescriptorImpl(PropertyType type, Name name, int min, int max, boolean isNillable) {
        this.type = type;
        this.name = name;
        this.minOccurs = min;
        this.maxOccurs = max;
        this.isNillable = isNillable;
        userData = new HashMap<>();

        if (type == null) {
            throw new NullPointerException("type");
        }

        if (name == null) {
            throw new NullPointerException("name");
        }

        if (max > 0 && max < min) {
            throw new IllegalArgumentException("max must be -1, or >= min");
        }
    }

    @Override
    public PropertyType getType() {
        return type;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public int getMinOccurs() {
        return minOccurs;
    }

    @Override
    public int getMaxOccurs() {
        return maxOccurs;
    }

    @Override
    public boolean isNillable() {
        return isNillable;
    }

    @Override
    public Map<Object, Object> getUserData() {
        return userData;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyDescriptorImpl)) {
            return false;
        }

        PropertyDescriptorImpl other = (PropertyDescriptorImpl) obj;
        return Utilities.equals(type, other.type)
                && Utilities.equals(name, other.name)
                && minOccurs == other.minOccurs
                && maxOccurs == other.maxOccurs
                && isNillable == other.isNillable;
    }

    @Override
    public int hashCode() {
        return 37 * minOccurs + 37 * maxOccurs ^ type.hashCode() ^ name.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
        sb.append(" ");
        sb.append(getName());
        if (type != null) {
            sb.append(" <");
            sb.append(type.getName().getLocalPart());
            sb.append(":");
            sb.append(Classes.getShortName(type.getBinding()));
            sb.append(">");
        }
        if (isNillable) {
            sb.append(" nillable");
        }
        if (minOccurs != 1 && maxOccurs != 1) {
            sb.append(" ");
            sb.append(minOccurs);
            sb.append(":");
            sb.append(maxOccurs);
        }
        if (userData != null && !userData.isEmpty()) {
            sb.append("\nuserData=(");
            for (Map.Entry entry : userData.entrySet()) {
                sb.append("\n\t");
                sb.append(entry.getKey());
                sb.append(" ==> ");
                sb.append(entry.getValue());
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
