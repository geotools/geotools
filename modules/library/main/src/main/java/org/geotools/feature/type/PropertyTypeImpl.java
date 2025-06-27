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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.api.filter.Filter;
import org.geotools.api.util.InternationalString;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;

public abstract class PropertyTypeImpl implements PropertyType {

    private static final List<Filter> NO_RESTRICTIONS = Collections.emptyList();

    protected final Name name;
    protected final Class<?> binding;
    protected final boolean isAbstract;
    protected final PropertyType superType;
    protected final List<Filter> restrictions;
    protected final InternationalString description;
    protected final Map<Object, Object> userData;

    public PropertyTypeImpl(
            Name name,
            Class<?> binding,
            boolean isAbstract,
            List<Filter> restrictions,
            PropertyType superType,
            InternationalString description) {
        if (name == null) {
            throw new NullPointerException("Name is required for PropertyType");
        }
        if (binding == null) {
            if (superType != null && superType.getBinding() != null) {
                // FIXME: This should be optional as the superType may have the required
                // information?
                throw new NullPointerException(
                        "Binding to a Java class, did you mean to bind to " + superType.getBinding());
            }
            throw new NullPointerException("Binding to a Java class is required");
        }
        this.name = name;
        this.binding = binding;
        this.isAbstract = isAbstract;

        if (restrictions == null) {
            this.restrictions = NO_RESTRICTIONS;
        } else {
            this.restrictions = Collections.unmodifiableList(restrictions);
        }

        this.superType = superType;
        this.description = description;
        this.userData = new HashMap<>();
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Class<?> getBinding() {
        return binding;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public List<Filter> getRestrictions() {
        return restrictions;
    }

    @Override
    public PropertyType getSuper() {
        return superType;
    }

    @Override
    public InternationalString getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return getName().hashCode()
                ^ getBinding().hashCode()
                ^ (getDescription() != null ? getDescription().hashCode() : 17);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof PropertyType)) {
            return false;
        }

        PropertyType prop = (PropertyType) other;

        if (!Utilities.equals(name, prop.getName())) {
            return false;
        }

        if (!Utilities.equals(binding, prop.getBinding())) {
            return false;
        }

        if (isAbstract != prop.isAbstract()) {
            return false;
        }

        if (!equals(getRestrictions(), prop.getRestrictions())) {
            return false;
        }

        if (!Utilities.equals(superType, prop.getSuper())) {
            return false;
        }

        if (!Utilities.equals(description, prop.getDescription())) {
            return false;
        }

        return true;
    }

    /**
     * Convenience method for testing two lists for equality. One or both objects may be null, and considers null and
     * empty list as equal
     */
    private boolean equals(final List object1, final List object2) {
        if (object1 == object2 || object1 != null && object1.equals(object2)) return true;
        if (object1 == null && object2.isEmpty()) return true;
        if (object2 == null && object1.isEmpty()) return true;
        return false;
    }

    @Override
    public Map<Object, Object> getUserData() {
        return userData;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
        sb.append(" ");
        sb.append(getName());
        if (isAbstract()) {
            sb.append(" abstract");
        }
        if (superType != null) {
            sb.append(" extends ");
            sb.append(superType.getName().getLocalPart());
        }
        if (binding != null) {
            sb.append("<");
            sb.append(Classes.getShortName(binding));
            sb.append(">");
        }
        if (description != null) {
            sb.append("\n\tdescription=");
            sb.append(description);
        }
        if (restrictions != null && !restrictions.isEmpty()) {
            sb.append("\nrestrictions=");
            boolean first = true;
            for (Filter filter : restrictions) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" AND ");
                }
                sb.append(filter);
            }
        }
        return sb.toString();
    }
}
