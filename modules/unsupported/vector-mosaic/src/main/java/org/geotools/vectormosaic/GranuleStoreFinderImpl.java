/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Repository;
import org.geotools.data.store.DecoratingDataStore;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;

/**
 * Class used to find the DataStore for a VectorMosaicGranule. The lookup tries three possible avenues:
 *
 * <ul>
 *   <li>Using the store name, if set in the granule, to lookup the store in the provided {@link Repository}
 *   <li>Using the connection properties if they are set in the granule
 * </ul>
 */
public class GranuleStoreFinderImpl extends GranuleStoreFinder {
    static final Logger LOGGER = Logging.getLogger(GranuleStoreFinderImpl.class);
    protected final String preferredSPI;
    protected final Repository repository;

    /**
     * Constructor that accepts a nullable preferred SPI.
     *
     * @param preferredSPI the preferred SPI
     */
    public GranuleStoreFinderImpl(String preferredSPI, Repository repository) {
        this.preferredSPI = preferredSPI;
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<DataStore> findDataStore(VectorMosaicGranule granule, boolean isSampleForSchema) {
        DataStore dataStore = null;
        try {
            if (granule.getStoreName() != null) {
                DataStore ds = repository.dataStore(new NameImpl(granule.getStoreName()));
                if (ds != null) ds = new DisposeStopWrapper(ds);
                return Optional.ofNullable(ds);
            }
            if (granule.getConnProperties() != null) {
                Map params = propertiesToMap(granule.getConnProperties());
                if (preferredSPI != null) {
                    DataStoreFactorySpi dataStoreFactorySpi = getSPI(preferredSPI);
                    dataStore = dataStoreFactorySpi.createDataStore(params);
                } else {
                    dataStore = DataStoreFinder.getDataStore(params);
                }
                LOGGER.log(Level.FINE, "Found and set datastore for granule {0} with params {1}", new Object[] {
                    granule.getName(), granule.getConnProperties()
                });
                if (!isSampleForSchema && granuleTracker != null) {
                    granuleTracker.add(granule.getParams());
                }
            } else {
                LOGGER.log(
                        Level.WARNING,
                        "Connection properties not found for Vector Mosaic granule {0}",
                        granule.getName());
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not find data store", e);
        }
        return Optional.ofNullable(dataStore);
    }

    public String getPreferredSPI() {
        return preferredSPI;
    }

    /**
     * Simple store wrapper that prevents the store from being disposed. Used when the store comes from a
     * {@link Repository} that manages its lifecycle.
     */
    private static class DisposeStopWrapper extends DecoratingDataStore {
        public DisposeStopWrapper(DataStore ds) {
            super(ds);
        }

        @Override
        public void dispose() {
            // do not dispose the store, it's managed by the repository
        }
    }
}
