/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Repository;
import org.geotools.feature.NameImpl;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.opengis.feature.type.Name;

/**
 * A catalog fetching the backing GeoTools data store from a {@link Repository} on demand
 *
 * @author Andrea Aime - GeoSolutions
 */
public class RepositoryDataStoreCatalog extends AbstractGTDataStoreGranuleCatalog {

    private Repository repository;

    private String flatStoreName;

    private Set<String> validTypeNames;

    private Name storeName;

    private DataAccessStoreWrapper cachedWrapped;

    public RepositoryDataStoreCatalog(
            Properties params,
            boolean create,
            Repository repository,
            String dataStoreName,
            DataStoreFactorySpi spi,
            Hints hints) {
        super(params, create, spi, hints);
        Utilities.ensureNonNull("repository", repository);
        Utilities.ensureNonNull("dataStoreName", repository);
        this.repository = repository;
        this.flatStoreName = dataStoreName;
        this.storeName = buildName(dataStoreName);

        // important, do not get the store here, as its lifecycle is externally managed
        // e.g., in GeoServer the ResourcePool can dispose the store and re-create it later
        // based on a LRU cache behavior. Also, the store might not yet be available at this moment
        // for the same reason
    }

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
    protected void handleInitializationException(Throwable t) {
        // nothing to do here
    }

    @Override
    protected void initTileIndexStore(Properties params, boolean create, DataStoreFactorySpi spi)
            throws IOException, MalformedURLException {
        // nothing to do here, the store is provided on demand
        if (create) {
            // don't go looking for feature types, there are none
            validTypeNames = new HashSet<String>();
        }
    }

    @Override
    protected void disposeTileIndexStore() {
        // the store is externally managed, nothing to dispose here
    }

    @Override
    protected DataStore getTileIndexStore() {
        DataStore dataStore = null;
        try {
            // try getting the store, the specific repository implementation might throw
            // an exception if the store is a DataAccess (DefaultRepository does for example)
            dataStore = repository.dataStore(storeName);
        } catch (Exception e) {
            // ignore
        }
        if (dataStore == null) {
            // see if we can fall back on a data access exposing simple feature types
            DataAccess access = repository.access(storeName);
            if (access != null) {
                if (cachedWrapped != null && cachedWrapped.wraps(access)) {
                    return cachedWrapped;
                } else {
                    DataAccessStoreWrapper wrapper = new DataAccessStoreWrapper(access);
                    cachedWrapped = wrapper;
                    dataStore = wrapper;
                }
            } else {
                throw new IllegalStateException(
                        "Could not find a data store with name " + flatStoreName);
            }
        }

        return dataStore;
    }

    @Override
    protected Set<String> getValidTypeNames() {
        if (validTypeNames == null) {
            validTypeNames = new HashSet<String>();
            try {
                initializeTypeNames(params);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return validTypeNames;
    }
}
