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

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;

/** Utility class to find a DataStoreFactorySpi for a given set of connection parameters. */
public abstract class GranuleStoreFinder {
    protected GranuleTracker granuleTracker;
    /**
     * Find the DataStore for the given connection parameters.
     *
     * @param granule the granule
     * @param isSampleForSchema true if the granule is a sample for schema
     */
    public abstract Optional<DataStore> findDataStore(
            VectorMosaicGranule granule, boolean isSampleForSchema);

    /**
     * Converts properties to a Map of key/value pairs
     *
     * @param connProps the properties to convert
     * @return a Map of key/value pairs
     */
    @SuppressWarnings("unchecked")
    protected Map propertiesToMap(Properties connProps) {
        Map params = new java.util.HashMap();
        for (Object key : connProps.keySet()) {
            params.put(key, connProps.get(key));
        }
        return params;
    }
    /**
     * Returns the data store factory SPI for the given name
     *
     * @param preferredSPI the name of the SPI to find
     * @return the SPI
     */
    protected DataStoreFactorySpi getSPI(String preferredSPI) {
        DataStoreFactorySpi dataStoreFactorySpi = null;
        for (Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
                it.hasNext(); ) {
            DataStoreFactorySpi spi = it.next();
            if (spi.getClass().getTypeName().equals(preferredSPI)) {
                dataStoreFactorySpi = spi;
                break;
            }
        }
        return dataStoreFactorySpi;
    }
}
