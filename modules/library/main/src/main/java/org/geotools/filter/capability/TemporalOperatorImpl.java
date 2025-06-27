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
package org.geotools.filter.capability;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.capability.TemporalOperator;

public class TemporalOperatorImpl implements TemporalOperator {

    String name;
    Set<Name> operands;

    public TemporalOperatorImpl(String name) {
        this.name = name;
        operands = new LinkedHashSet<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Name> getTemporalOperands() {
        return operands;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (operands == null ? 0 : operands.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TemporalOperatorImpl other = (TemporalOperatorImpl) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (operands == null) {
            if (other.operands != null) return false;
        } else if (!operands.equals(other.operands)) return false;
        return true;
    }
}
