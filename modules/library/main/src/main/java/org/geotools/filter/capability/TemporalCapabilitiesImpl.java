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
import org.geotools.api.filter.capability.TemporalCapabilities;
import org.geotools.api.filter.capability.TemporalOperator;
import org.geotools.api.filter.capability.TemporalOperators;

public class TemporalCapabilitiesImpl implements TemporalCapabilities {

    TemporalOperators temporalOperators;

    public TemporalCapabilitiesImpl() {
        this((TemporalOperators) null);
    }

    public TemporalCapabilitiesImpl(Collection<TemporalOperator> operators) {
        this(new TemporalOperatorsImpl(operators));
    }

    public TemporalCapabilitiesImpl(TemporalOperators operators) {
        temporalOperators = toTemporalOperatorsImpl(operators);
    }

    public TemporalCapabilitiesImpl(TemporalCapabilities capabilities) {
        temporalOperators = toTemporalOperatorsImpl(capabilities.getTemporalOperators());
    }

    TemporalOperators toTemporalOperatorsImpl(TemporalOperators operators) {
        if (operators == null) {
            return new TemporalOperatorsImpl();
        }
        if (operators instanceof TemporalOperatorsImpl) {
            return operators;
        }
        return new TemporalOperatorsImpl(operators.getOperators());
    }

    @Override
    public TemporalOperators getTemporalOperators() {
        return temporalOperators;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (temporalOperators == null ? 0 : temporalOperators.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TemporalCapabilitiesImpl other = (TemporalCapabilitiesImpl) obj;
        if (temporalOperators == null) {
            if (other.temporalOperators != null) return false;
        } else if (!temporalOperators.equals(other.temporalOperators)) return false;
        return true;
    }
}
