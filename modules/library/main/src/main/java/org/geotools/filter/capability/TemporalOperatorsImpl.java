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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.geotools.api.filter.capability.TemporalOperator;
import org.geotools.api.filter.capability.TemporalOperators;

public class TemporalOperatorsImpl implements TemporalOperators {

    Set<TemporalOperator> operators;

    public TemporalOperatorsImpl() {
        this(new ArrayList<>());
    }

    public TemporalOperatorsImpl(Collection<TemporalOperator> operators) {
        this.operators = new LinkedHashSet<>();
        this.operators.addAll(operators);
    }

    @Override
    public Collection<TemporalOperator> getOperators() {
        return operators;
    }

    @Override
    public TemporalOperator getOperator(String name) {
        for (TemporalOperator op : operators) {
            if (op.getName().equals(name)) {
                return op;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (operators == null ? 0 : operators.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TemporalOperatorsImpl other = (TemporalOperatorsImpl) obj;
        if (operators == null) {
            if (other.operators != null) return false;
        } else if (!operators.equals(other.operators)) return false;
        return true;
    }
}
