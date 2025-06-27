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
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.capability.Functions;

/**
 * Implementation of the Functions interface.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class FunctionsImpl implements Functions {

    Set<FunctionName> functionNames;

    public FunctionsImpl() {
        this(new ArrayList<>());
    }

    public FunctionsImpl(Collection<FunctionName> functionNames) {
        this.functionNames = new HashSet<>(functionNames);
    }

    public FunctionsImpl(FunctionName... functionNames) {
        if (functionNames == null) {
            functionNames = new FunctionName[] {};
        }

        this.functionNames = new HashSet<>(Arrays.asList(functionNames));
    }

    public FunctionsImpl(Functions copy) {
        this.functionNames = new HashSet<>();
        if (copy.getFunctionNames() != null) {
            for (FunctionName functionName : copy.getFunctionNames()) {
                this.functionNames.add(new FunctionNameImpl(functionName));
            }
        }
    }

    @Override
    public Collection<FunctionName> getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(Collection<FunctionName> functionNames) {
        this.functionNames = new HashSet<>(functionNames);
    }

    @Override
    public FunctionName getFunctionName(String name) {
        if (name == null || functionNames == null) {
            return null;
        }

        for (FunctionName functionName : functionNames) {
            if (name.equals(functionName.getName())) {
                return functionName;
            }
        }
        return null;
    }

    public void addAll(Functions copy) {
        if (copy == null) return;
        if (copy.getFunctionNames() != null) {
            for (FunctionName functionName : copy.getFunctionNames()) {
                this.functionNames.add(new FunctionNameImpl(functionName));
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("FunctionsImpl[");
        if (functionNames != null) {
            buf.append("with ");
            buf.append(functionNames.size());
            buf.append(" functions");
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (functionNames == null ? 0 : functionNames.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FunctionsImpl other = (FunctionsImpl) obj;
        if (functionNames == null) {
            if (other.functionNames != null) return false;
        } else if (!functionNames.equals(other.functionNames)) return false;
        return true;
    }
}
