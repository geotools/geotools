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
package org.geotools.data.directory;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Transaction;

/**
 * Locking manager that will delegate its work to the locking managers of the delegate data stores
 *
 * @author Andrea Aime - OpenGeo
 */
public class DirectoryLockingManager implements LockingManager {

    DirectoryTypeCache cache;

    public DirectoryLockingManager(DirectoryTypeCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean exists(String authID) {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if (store.getLockingManager() != null && store.getLockingManager().exists(authID)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean release(String authID, Transaction transaction) throws IOException {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if (store.getLockingManager() != null && store.getLockingManager().exists(authID)) {
                return store.getLockingManager().release(authID, transaction);
            }
        }

        return false;
    }

    @Override
    public boolean refresh(String authID, Transaction transaction) throws IOException {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if (store.getLockingManager() != null && store.getLockingManager().exists(authID)) {
                return store.getLockingManager().refresh(authID, transaction);
            }
        }

        return false;
    }

    @Override
    public void unLockFeatureID(String typeName, String authID, Transaction transaction, FeatureLock featureLock)
            throws IOException {
        DataStore store = cache.getDataStore(typeName, false);

        if (store != null && store.getLockingManager() != null) {
            store.getLockingManager().unLockFeatureID(typeName, authID, transaction, featureLock);
        }
    }

    @Override
    public void lockFeatureID(String typeName, String authID, Transaction transaction, FeatureLock featureLock)
            throws IOException {
        DataStore store = cache.getDataStore(typeName, false);

        if (store != null && store.getLockingManager() != null) {
            store.getLockingManager().lockFeatureID(typeName, authID, transaction, featureLock);
        }
    }
}
