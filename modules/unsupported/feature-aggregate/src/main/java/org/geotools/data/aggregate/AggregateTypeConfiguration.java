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

import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * Maps a set of source datastores and type names into an aggregated feature type
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class AggregateTypeConfiguration {

    /**
     * The feature type name
     */
    String name;

    /**
     * The store to native type name map
     */
    LinkedHashMap<Name, String> storeMap = new LinkedHashMap<Name, String>();

    /**
     * The store that drives the feature type
     */
    Name primaryStore;

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
                addStore(storeName, name);
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
                addStore(new NameImpl(storeName), name);
            }
        }
    }

    public AggregateTypeConfiguration(AggregateTypeConfiguration other) {
        this.name = other.name;
        this.storeMap.putAll(other.getStoreMap());
    }

    /**
     * Adds a source store/type for this aggregated feature type
     * @param storeName
     * @param typeName
     */
    public void addStore(Name storeName, String typeName) {
        if (storeMap.isEmpty()) {
            primaryStore = storeName;
        }
        storeMap.put(storeName, typeName);
    }

    /**
     * Adds a source store/type for this aggregated feature type
     * @param storeName
     * @param typeName
     */
    public void addStore(String localName, String typeName) {
        Name storeName = buildName(localName);
        if (storeMap.isEmpty()) {
            primaryStore = storeName;
        }
        storeMap.put(storeName, typeName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Name, String> getStoreMap() {
        return storeMap;
    }

    public Name getPrimaryStore() {
        return primaryStore;
    }

    @Override
    public String toString() {
        return "AggregateTypeConfiguration [name=" + name + ", storeMap=" + storeMap + "]";
    }

    public int getStoreIndex(Name storeName) {
        int i = 0;
        for (Name name : storeMap.keySet()) {
            if (name.equals(storeName)) {
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
}
