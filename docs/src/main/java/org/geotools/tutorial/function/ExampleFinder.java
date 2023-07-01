/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
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
import org.geotools.api.filter.FilterFactory;

public class ExampleFinder extends FactoryFinder {

    private static volatile FactoryCreator registry;

    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(ExampleFinder.class);
        if (registry == null) {
            Class<?>[] categories = new Class<?>[] {FunctionFactory.class};
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
