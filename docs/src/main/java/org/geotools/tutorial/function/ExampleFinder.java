/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.tutorial.function;

import java.util.Set;
import java.util.stream.Stream;
import org.geotools.filter.FunctionFactory;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;
import org.opengis.filter.FilterFactory;

public class ExampleFinder extends FactoryFinder {

    private static volatile FactoryCreator registry;

    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(ExampleFinder.class);
        if (registry == null) {
            Class<?> categories[] = new Class<?>[] {FunctionFactory.class};
            registry = new FactoryCreator(categories);
        }
        return registry;
    }

    /**
     * Returns a set of all available implementations for the {@link FilterFactory} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available filter factory implementations.
     */
    public static synchronized Set<FunctionFactory> getFilterFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        Stream<FunctionFactory> serviceProviders =
                getServiceRegistry().getFactories(FunctionFactory.class, null, hints);
        return new LazySet<>(serviceProviders);
    }

    /** Allow the classpath to be rescanned */
    public static synchronized void scanForPlugins() {
        if (registry != null) {
            registry.scanForPlugins();
        }
    }
}
