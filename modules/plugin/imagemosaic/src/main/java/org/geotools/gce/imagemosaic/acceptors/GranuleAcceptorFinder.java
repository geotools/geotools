/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic.acceptors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;

/**
 * Helper to find GranuleFactorySPI instances
 */
public class GranuleAcceptorFinder {

    private static FactoryCreator registry;

    public static synchronized Map<String, GranuleAcceptorFactorySPI> getPropertiesCollectorSPI() {
        // get all GridFormatFactorySpi implementations
        final Iterator<GranuleAcceptorFactorySPI> it = getServiceRegistry().getServiceProviders(GranuleAcceptorFactorySPI.class, true);
        Map<String, GranuleAcceptorFactorySPI> acceptorFactorySPIMap = new HashMap<>();
        while (it.hasNext()) {
            GranuleAcceptorFactorySPI granuleAcceptorFactorySPI = it.next();
            acceptorFactorySPIMap.put(granuleAcceptorFactorySPI.getClass().getName(),
                    granuleAcceptorFactorySPI);
        }
        return acceptorFactorySPIMap;
    }

    /**
     * Returns the service registry. The registry will be created the first time
     * this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { GranuleAcceptorFactorySPI.class }));
        }
        return registry;
    }
}
