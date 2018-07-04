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

package org.geotools.gce.imagemosaic.namecollector;

import static org.geotools.util.Utilities.toInstanceByClassNameMap;

import java.util.Arrays;
import java.util.Map;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;

/** Access the {@link CoverageNameCollectorSPI}s */
public class CoverageNameCollectorSpiFinder {

    private static FactoryCreator registry;

    public static synchronized Map<String, CoverageNameCollectorSPI> getCoverageNameCollectorSPI() {
        // get all CoverageNameCollectorSPI implementations
        FactoryRegistry serviceRegistry = getServiceRegistry();
        serviceRegistry.scanForPlugins();
        return serviceRegistry
                .getFactories(CoverageNameCollectorSPI.class, true)
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
                            Arrays.asList(new Class<?>[] {CoverageNameCollectorSPI.class}));
        }
        return registry;
    }
}
