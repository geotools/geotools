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
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.util.logging.Logging;

/** Class used to find the DataStore for a VectorMosaicGranule. */
public class GranuleStoreFinderImpl extends GranuleStoreFinder {
    static final Logger LOGGER = Logging.getLogger(GranuleStoreFinderImpl.class);
    protected final String preferredSPI;

    /**
     * Constructor that accepts a nullable preferred SPI.
     *
     * @param preferredSPI the preferred SPI
     */
    public GranuleStoreFinderImpl(String preferredSPI) {
        this.preferredSPI = preferredSPI;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<DataStore> findDataStore(
            VectorMosaicGranule granule, boolean isSampleForSchema) {
        DataStore dataStore = null;
        try {
            if (granule.getConnProperties() != null) {
                Map params = propertiesToMap(granule.getConnProperties());
                if (preferredSPI != null) {
                    DataStoreFactorySpi dataStoreFactorySpi = getSPI(preferredSPI);
                    dataStore = dataStoreFactorySpi.createDataStore(params);
                } else {
                    dataStore = DataStoreFinder.getDataStore(params);
                }
                LOGGER.log(
                        Level.FINE,
                        "Found and set datastore for granule {0} with params {1}",
                        new Object[] {granule.getName(), granule.getConnProperties()});
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
}
