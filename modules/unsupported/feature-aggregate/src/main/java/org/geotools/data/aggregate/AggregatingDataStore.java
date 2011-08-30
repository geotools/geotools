package org.geotools.data.aggregate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.Repository;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

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
public class AggregatingDataStore extends ContentDataStore {

    static final Logger LOGGER = Logging.getLogger(AggregatingDataStore.class);

    Repository repository;

    boolean tolerant;

    Map<String, AggregateTypeConfiguration> typeMap = new LinkedHashMap<String, AggregateTypeConfiguration>();

    ExecutorService executor;

    public AggregatingDataStore(Repository repository, ExecutorService executor) {
        this.repository = repository;
        this.executor = executor;
    }

    public boolean isTolerant() {
        return tolerant;
    }

    public void setTolerant(boolean tolerant) {
        this.tolerant = tolerant;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        List<Name> result = new ArrayList<Name>();
        for (String name : typeMap.keySet()) {
            result.add(new NameImpl(namespaceURI, name));
        }
        return result;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        String name = entry.getName().getLocalPart();
        AggregateTypeConfiguration config = typeMap.get(name);
        if (config == null) {
            throw new IOException("Aggregating configuration for type " + name + " was not found");
        }
        return new AggregatingFeatureSource(entry, this, config);
    }

    /**
     * Adds all feature types found in the specified stores, aggregating the ones with the same
     * names (the structure will be picked form the first store having that type in the list)
     * 
     * @param storeNames
     * @throws IOException
     */
    public void autoConfigureStores(List<String> storeNames) throws IOException {
        if (storeNames == null || storeNames.size() == 0) {
            return;
        }

        // collect all type names
        Map<String, Set<String>> allNames = new LinkedHashMap<String, Set<String>>();
        for (String storeName : storeNames) {
            DataStore store = getStore(storeName, tolerant);
            if (store != null) {
                Set<String> typeNames = new LinkedHashSet<String>(Arrays.asList(store
                        .getTypeNames()));
                allNames.put(storeName, typeNames);
            } else {
                allNames.put(storeName, (Set<String>) Collections.EMPTY_SET);
            }
        }

        // build the feature type maps
        for (int i = 0; i < storeNames.size(); i++) {
            String storeName = storeNames.get(i);
            Set<String> baseNames = allNames.get(storeName);
            for (String baseName : baseNames) {
                AggregateTypeConfiguration config = new AggregateTypeConfiguration(baseName);
                config.addStore(storeName, baseName);
                for (int j = i + 1; j < storeNames.size(); j++) {
                    String otherStore = storeNames.get(j);
                    if (allNames.get(otherStore).remove(baseName)) {
                        config.addStore(otherStore, baseName);
                    }
                }

                addType(config);
            }
        }

    }

    /**
     * Adds a new aggregate type to the store
     */
    public void addType(AggregateTypeConfiguration config) throws IOException {
        try {
            typeMap.put(config.getName(), config);
            // force the feature type configuration to check the config is ok
            getSchema(config.getName());
            // the new vtable might be overriding a previous definition
            entries.remove(new NameImpl(namespaceURI, config.getName()));
        } catch (IOException e) {
            typeMap.remove(config.getName());
        }
    }

    /**
     * Returns the configuration for the specified type, or null if not found
     * 
     * @param name
     * @return
     */
    public AggregateTypeConfiguration getConfiguration(String name) {
        return typeMap.get(name);
    }

    /**
     * Removes and returns the configuration of specified aggregate type
     * 
     * @param name
     * @return
     */
    public AggregateTypeConfiguration removeType(String name) {
        // the new vtable might be overriding a previous definition
        AggregateTypeConfiguration config = typeMap.remove(name);
        if (config != null) {
            entries.remove(new NameImpl(namespaceURI, name));
        }
        return config;
    }

    /**
     * Resets the store configuration, leaving no feature types
     */
    public void resetConfiguration() {
        typeMap.clear();
        entries.clear();
    }

    /**
     * Returns a immutable view of the aggregate type configurations
     * 
     * @return
     */
    public Map<String, AggregateTypeConfiguration> getConfigurations() {
        Map<String, AggregateTypeConfiguration> result = new HashMap<String, AggregateTypeConfiguration>();
        for (String key : typeMap.keySet()) {
            result.put(key, new AggregateTypeConfiguration(typeMap.get(key)));
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Gets a store from the repository given its name
     * 
     * @param storeName
     * @return
     * @throws IOException
     */
    DataStore getStore(String storeName, boolean tolerant) throws IOException {
        Name name = AggregateTypeConfiguration.buildName(storeName);
        return getStore(name, tolerant);
    }

    DataStore getStore(Name name, boolean tolerant) throws IOException {
        DataStore store = null;
        Exception e = null;
        try {
            store = repository.dataStore(name);
        } catch (Exception ex) {
            e = ex;
        }
        if (store != null || tolerant) {
            return store;
        } else {
            throw new IOException("Could not locate store " + name, e);
        }
    }

    <V> Future<V> submit(Callable<V> callable) {
        return executor.submit(callable);
    }

}
