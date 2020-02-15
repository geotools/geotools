/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * Default Repository implementation allows GeoTools to manage your DataStores.
 *
 * <p>To use this implementation you can "registery" new DataStores:
 *
 * <ul>
 *   <li>load( File )
 *   <li>This also serves as a reminder that we need CrossDataStore functionality - at least for
 *       Locks. And possibly for "Query".
 *
 * @author Jody Garnett DefaultRepository.java $
 */
public class DefaultRepository implements Repository {

    /** Holds the DataStores so we can clean up when closed */
    protected Map<Name, DataAccess<?, ?>> repository = new ConcurrentHashMap<>();

    //
    // lookup methods provided by Repository interface
    //
    public DataAccess<?, ?> access(String name) {
        return access(new NameImpl(name));
    }

    public DataAccess<?, ?> access(Name name) {
        return repository.get(name);
    }

    public DataStore dataStore(String name) {
        return (DataStore) access(name);
    }

    public DataStore dataStore(Name name) {
        return (DataStore) access(name);
    }

    //
    // DefaultRepository methods used to maintain datastores
    //

    /**
     * Load a quick repository from a properties file.
     *
     * <p>This is useful for test cases; the format is:
     *
     * <pre><code>
     * nameA=param=value,param2=value2,...
     * nameB=param=value,param2=value2,...
     * </code></pre>
     */
    public void load(File propertiesFile) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));

        for (Iterator i = properties.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();
            String definition = (String) entry.getValue();
            Map<String, Serializable> params = definition(definition);

            DataStore dataStore = DataStoreFinder.getDataStore(params);

            register(name, dataStore);
        }
    }

    /** Check if a lock exists in any of the DataStores. */
    public boolean lockExists(String lockID) {
        if (lockID == null) {
            return false;
        }
        LockingManager lockManager;
        for (DataAccess<?, ?> access : repository.values()) {
            DataStore store = (DataStore) access;
            lockManager = store.getLockingManager();
            if (lockManager == null) {
                continue; // did not support locking
            }
            if (lockManager.exists(lockID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implement lockRefresh.
     *
     * <p>Currently it is an error if the lockID is not found. Because if we can't find it we cannot
     * refresh it.
     *
     * <p>Since locks are time sensitive it is impossible to check if a lockExists and then be sure
     * it will still exist when you try to refresh it. Nothing we do can protect client code from
     * this fact, they will need to do with the IOException when (not if) this situation occurs.
     *
     * @see org.geotools.data.Catalog#lockRefresh(java.lang.String, org.geotools.data.Transaction)
     * @param lockID Authorizataion of lock to refresh
     * @param transaction Transaction used to authorize refresh
     * @throws IOException If opperation encounters problems, or lock not found
     * @throws IllegalArgumentException if lockID is <code>null</code>
     */
    public boolean lockRefresh(String lockID, Transaction transaction) throws IOException {
        if (lockID == null) {
            throw new IllegalArgumentException("lockID required");
        }
        if (transaction == null || transaction == Transaction.AUTO_COMMIT) {
            throw new IllegalArgumentException(
                    "Tansaction required (with authorization for " + lockID + ")");
        }
        LockingManager lockManager;

        boolean refresh = false;
        for (DataAccess<?, ?> access : repository.values()) {
            DataStore store = (DataStore) access;
            lockManager = store.getLockingManager();
            if (lockManager == null) {
                continue; // did not support locking
            }
            if (lockManager.release(lockID, transaction)) {
                refresh = true;
            }
        }
        return refresh;
    }

    /**
     * Implement lockRelease.
     *
     * <p>Currently it is <b>not</b> and error if the lockID is not found, it may have expired.
     * Since locks are time sensitive it is impossible to check if a lockExists and then be sure it
     * will still exist when you try to release it.
     *
     * @see org.geotools.data.Catalog#lockRefresh(java.lang.String, org.geotools.data.Transaction)
     * @param lockID Authorizataion of lock to refresh
     * @param transaction Transaction used to authorize refresh
     * @throws IOException If opperation encounters problems
     * @throws IllegalArgumentException if lockID is <code>null</code>
     */
    public boolean lockRelease(String lockID, Transaction transaction) throws IOException {
        if (lockID == null) {
            throw new IllegalArgumentException("lockID required");
        }
        if (transaction == null || transaction == Transaction.AUTO_COMMIT) {
            throw new IllegalArgumentException(
                    "Tansaction required (with authorization for " + lockID + ")");
        }

        LockingManager lockManager;

        boolean release = false;
        for (DataAccess<?, ?> access : repository.values()) {
            DataStore store = (DataStore) access;
            lockManager = store.getLockingManager();
            if (lockManager == null) continue; // did not support locking

            if (lockManager.release(lockID, transaction)) {
                release = true;
            }
        }
        return release;
    }

    /**
     * Register a new DataStore with this registery
     *
     * <p>Description ...
     *
     * @see org.geotools.data.Catalog#registerDataStore(org.geotools.data.DataStore)
     */
    public void register(String name, DataAccess<?, ?> dataStore) throws IOException {
        register(new NameImpl(name), dataStore);
    }

    public void register(Name name, DataAccess<?, ?> dataStore) throws IOException {
        if (repository.containsKey(name)) {
            throw new IOException("Name " + name + " already registered");
        }
        if (repository.containsValue(dataStore)) {
            throw new IOException("The dataStore already registered");
        }
        repository.put(name, dataStore);
    }

    public DataStore datastore(String id) {
        return dataStore(new NameImpl(id));
    }

    public SimpleFeatureSource source(String dataStoreId, String typeName) throws IOException {
        DataStore ds = datastore(dataStoreId);
        return ds.getFeatureSource(typeName);
    }

    /** Internal method parsing parameters from a string */
    private static final Map<String, Serializable> definition(String definition)
            throws ParseException {
        Map<String, Serializable> map = new HashMap<String, Serializable>();

        String[] params = definition.split(",");
        int offset = 0;
        for (int i = 0; i < params.length; i++) {
            String[] vals = params[i].split("=");
            if (vals.length == 2) {
                map.put(vals[0].trim(), vals[1].trim());
            } else {
                throw new ParseException("Could not interpret " + params[i], offset);
            }
            offset += params[i].length();
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public Set<Name> getNames() {
        Set names = new HashSet(repository.keySet());
        return (Set<Name>) names;
    }

    @SuppressWarnings("unchecked")
    public List<DataStore> getDataStores() {
        List list = new ArrayList(repository.values());
        return (List<DataStore>) list;
    }
}
