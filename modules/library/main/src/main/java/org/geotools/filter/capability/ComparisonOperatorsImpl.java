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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.filter.capability.ComparisonOperators;
import org.geotools.api.filter.capability.Operator;

/**
 * Implementation of the ComparisonOperators interface.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ComparisonOperatorsImpl implements ComparisonOperators {

    Set<Operator> operators;

    public ComparisonOperatorsImpl() {
        this(new ArrayList<>());
    }

    /** Copy the provided ComparisonOperator */
    public ComparisonOperatorsImpl(ComparisonOperators copy) {
        this.operators = new HashSet<>(copy.getOperators());
    }

    public ComparisonOperatorsImpl(Collection<Operator> operators) {
        this.operators = new HashSet<>(operators);
    }

    public ComparisonOperatorsImpl(Operator... operators) {
        if (operators == null) {
            operators = new Operator[] {};
        }
        this.operators = new HashSet<>(Arrays.asList(operators));
    }

    @Override
    public Collection<Operator> getOperators() {
        if (operators == null) {
            operators = new HashSet<>();
        }
        return operators;
    }

    public void setOperators(Collection<Operator> operators) {
        this.operators = new HashSet<>(operators);
    }
    /** @return Operator with the provided name, or null if not supported */
    @Override
    public Operator getOperator(String name) {
        if (name == null || operators == null) {
            return null;
        }
        for (Operator operator : operators) {
            if (name.equals(operator.getName())) {
                return operator;
            }
        }
        return null;
    }

    public void addAll(ComparisonOperators copy) {
        if (copy.getOperators() != null) {
            getOperators().addAll(copy.getOperators());
        }
    }

    @Override
    public String toString() {
        if (operators == null) {
            return "ComparisonOperators: none";
        }
        return "ComparisonOperators:" + operators;
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
        ComparisonOperatorsImpl other = (ComparisonOperatorsImpl) obj;
        if (operators == null) {
            if (other.operators != null) return false;
        } else if (!operators.equals(other.operators)) return false;
        return true;
    }
}
