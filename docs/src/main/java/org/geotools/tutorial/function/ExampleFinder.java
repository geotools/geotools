package org.geotools.tutorial.function;

import java.util.Iterator;
import java.util.Set;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.filter.FunctionFactory;
import org.geotools.resources.LazySet;
import org.opengis.filter.FilterFactory;

public class ExampleFinder extends FactoryFinder {

    private static FactoryCreator registry;
    
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(ExampleFinder.class);
        if (registry == null) {
            Class<?> categories[] = new Class<?>[] { FunctionFactory.class };
            registry = new FactoryCreator( categories);
        }
        return registry;
    }
    
    /**
     * Returns a set of all available implementations for the {@link FilterFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available filter factory implementations.
     */
    public static synchronized Set<FunctionFactory> getFilterFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        Iterator<FunctionFactory> serviceProviders = getServiceRegistry().getServiceProviders(
                FunctionFactory.class, null, hints);
        return new LazySet<FunctionFactory>(serviceProviders);
    }
        
    /** Allow the classpath to be rescanned */
    public static synchronized void scanForPlugins() {
        if (registry != null) {
            registry.scanForPlugins();
        }
    }
}
