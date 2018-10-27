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

package org.geotools.gce.imagemosaic.granulecollector;

import static org.geotools.util.Utilities.toInstanceByClassNameMap;

import java.util.Arrays;
import java.util.Map;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;

/** Helper finder for SubmosaicProducerFactories */
public class SubmosaicProducerFactoryFinder {

    private static FactoryCreator registry;

    public static synchronized Map<String, SubmosaicProducerFactory> getGranuleHandlersSPI() {
        // get all GranuleHandlerFactorySPI implementations
        FactoryRegistry serviceRegistry = getServiceRegistry();
        serviceRegistry.scanForPlugins();
        return serviceRegistry
                .getFactories(SubmosaicProducerFactory.class, true)
                .collect(toInstanceByClassNameMap());
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(new Class<?>[] {SubmosaicProducerFactory.class}));
        }
        return registry;
    }
}
