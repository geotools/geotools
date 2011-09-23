/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * Maps a set of source datastores and type names into an aggregated feature type
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class AggregateTypeConfiguration implements Serializable {

    /**
     * The feature type name
     */
    String name;

    /**
     * The store to native type name map
     */
    List<SourceType> sourceTypes = new ArrayList<SourceType>();

    /**
     * The store that drives the feature type
     */
    SourceType primarySource;

    /**
     * Builds a new aggreate type configuration
     * 
     * @param name the name of the aggregate type
     */
    public AggregateTypeConfiguration(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Feature type name cannot be null");
        }

        this.name = name;
    }

    /**
     * Builds a new aggregate type configuration, with a store list, assuming the feature type in
     * the store has always the same name
     * 
     * @param name the name of the aggregate type
     * @param storeNames the name of the various stores
     */
    public AggregateTypeConfiguration(String name, Name... storeNames) {
        this(name);
        if (storeNames != null && storeNames.length > 0) {
            for (Name storeName : storeNames) {
                addSourceType(storeName, name);
            }
        }
    }

    /**
     * Builds a new aggregate type configuration, with a store list, assuming the feature type in
     * the store has always the same name
     * 
     * @param name the name of the aggregate type
     * @param storeNames the name of the various stores
     */
    public AggregateTypeConfiguration(String name, String... storeNames) {
        this(name);
        if (storeNames != null && storeNames.length > 0) {
            for (String storeName : storeNames) {
                addSourceType(new NameImpl(storeName), name);
            }
        }
    }

    public AggregateTypeConfiguration(AggregateTypeConfiguration other) {
        copyFrom(other);
    }

    public void copyFrom(AggregateTypeConfiguration other) {
        this.name = other.name;
        this.sourceTypes.clear();
        this.sourceTypes.addAll(other.getSourceTypes());
        this.primarySource = other.primarySource;
    }

    /**
     * Adds a source store/type for this aggregated feature type
     * 
     * @param storeName
     * @param typeName
     */
    public void addSourceType(Name storeName, String typeName) {
        SourceType sourceType = new SourceType(storeName, typeName);
        if (sourceTypes.isEmpty()) {
            primarySource = sourceType;
        }
        sourceTypes.add(sourceType);
    }

    /**
     * Adds a source store/type for this aggregated feature type
     * 
     * @param storeName
     * @param typeName
     */
    public void addSourceType(String localName, String typeName) {
        Name storeName = buildName(localName);
        this.addSourceType(storeName, typeName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SourceType> getSourceTypes() {
        return sourceTypes;
    }

    public List<SourceType> getSourceTypes(Name store) {
        List<SourceType> result = new ArrayList<SourceType>();
        for (SourceType st : sourceTypes) {
            if (st.getStoreName().equals(store)) {
                result.add(st);
            }
        }
        return result;
    }

    public SourceType getPrimarySourceType() {
        return primarySource;
    }

    public void setPrimarySourceType(SourceType primary) {
        if (!sourceTypes.contains(primary)) {
            sourceTypes.add(0, primary);
        }
        this.primarySource = primary;
    }

    @Override
    public String toString() {
        return "AggregateTypeConfiguration [name=" + name + ", storeMap=" + sourceTypes
                + ", primarySource=" + primarySource + "]";
    }

    int getStoreIndex(Name storeName) {
        int i = 0;
        for (SourceType st : sourceTypes) {
            if (st.getStoreName().equals(storeName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Builds a qualified name from a name containing the ":" separator, otherwise the given name
     * will be used as the local part
     * 
     * @param name
     * @return
     */
    static Name buildName(String name) {
        int idx = name.indexOf(":");
        if (idx == -1) {
            return new NameImpl(name);
        } else {
            String ns = name.substring(0, idx);
            String local = name.substring(idx + 1);
            return new NameImpl(ns, local);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((primarySource == null) ? 0 : primarySource.hashCode());
        result = prime * result + ((sourceTypes == null) ? 0 : sourceTypes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AggregateTypeConfiguration other = (AggregateTypeConfiguration) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (primarySource == null) {
            if (other.primarySource != null)
                return false;
        } else if (!primarySource.equals(other.primarySource))
            return false;
        if (sourceTypes == null) {
            if (other.sourceTypes != null)
                return false;
        } else if (!sourceTypes.equals(other.sourceTypes))
            return false;
        return true;
    }
}
