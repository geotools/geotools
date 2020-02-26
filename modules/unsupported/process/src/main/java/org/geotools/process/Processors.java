/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Stream;
import org.geotools.data.Parameter;
import org.geotools.data.util.NullProgressListener;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.opengis.feature.type.Name;

/**
 * Factory and utility methods for {@link ProcessExecutor}, and {@link Process} classes defined in
 * this package.
 *
 * <p>Defines static methods used to access the application's default process factory
 * implementations.
 *
 * @author gdavis
 */
public class Processors extends FactoryFinder {
    /** The service registry for this manager. Will be initialized only when first needed. */
    private static FactoryRegistry registry;

    /** Do not allow any instantiation of this class. */
    private Processors() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        synchronized (Processors.class) {
            if (registry == null) {
                registry = new FactoryCreator(ProcessFactory.class);
            }
        }
        return registry;
    }

    /** Dynamically register a new process factory into SPI */
    public static void addProcessFactory(ProcessFactory factory) {
        getServiceRegistry().registerFactory(factory);
    }

    /**
     * Dynamically removes a process factory from SPI. Normally the factory has been added before
     * via {@link #addProcessFactory(ProcessFactory)}
     */
    public static void removeProcessFactory(ProcessFactory factory) {
        if (lastFactory == factory) {
            lastFactory = null;
        }
        getServiceRegistry().deregisterFactory(factory);
    }

    /**
     * Set of available ProcessFactory; each of which is responsible for one or more processes.
     *
     * @return Set of ProcessFactory
     */
    public static Set<ProcessFactory> getProcessFactories() {
        Stream<ProcessFactory> serviceProviders =
                getServiceRegistry().getFactories(ProcessFactory.class, null, null);
        return new LazySet<ProcessFactory>(serviceProviders);
    }

    /** Cache of last factory found */
    static ProcessFactory lastFactory;

    /**
     * Look up a Factory by name of a process it supports.
     *
     * @param name Name of the Process you wish to work with
     * @return ProcessFactory capable of creating an instanceof the named process
     */
    public static synchronized ProcessFactory createProcessFactory(Name name) {
        // store a local reference to last factory, since it could change if this method is called
        // within the factories getNames() method, which could happen if a factory delegates in some
        // way
        ProcessFactory last = lastFactory;
        if (last != null && last.getNames().contains(name)) {
            return last;
        }
        for (ProcessFactory factory : getProcessFactories()) {
            if (factory.getNames().contains(name)) {
                lastFactory = factory;
                return factory;
            }
        }
        return null; // go fish
    }

    /**
     * Look up an implementation of the named process on the classpath.
     *
     * @param name Name of the Process to create
     * @return created process
     */
    public static synchronized Process createProcess(Name name) {
        ProcessFactory factory = createProcessFactory(name);
        if (factory == null) return null;

        return factory.create(name);
    }

    /**
     * Look up an implementation of the named process on the classpath and describe the input
     * parameter required.
     *
     * @param name Name of the Process
     * @return Description of the parameters required
     */
    public static synchronized Map<String, Parameter<?>> getParameterInfo(Name name) {
        ProcessFactory factory = createProcessFactory(name);
        if (factory == null) return null;

        return factory.getParameterInfo(name);
    }

    /**
     * Look up an implementation of the named process on the classpath and describe the expected
     * results.
     *
     * <p>Note the expected results are generated in part by the input parameters provided; this is
     * to allow for processes where the output is controlled by the parameters (such as choosing a
     * greyscale or color raster product; or choosing the version of GML produced etc...).
     *
     * @param name Name of the Process
     * @return Description of the parameters required
     */
    public static synchronized Map<String, Parameter<?>> getResultInfo(
            Name name, Map<String, Object> parameters) {
        ProcessFactory factory = createProcessFactory(name);
        if (factory == null) return null;

        return factory.getResultInfo(name, parameters);
    }

    /** Used to wrap a Process up as a Callable for use with an existing ExecutorService */
    public static Callable<Map<String, Object>> createCallable(
            final Process process, final Map<String, Object> input) {
        return new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return process.execute(input, new CallableProgressListener());
            }
        };
    }

    public static ProcessExecutor newProcessExecutor(int nThreads) {
        return newProcessExecutor(nThreads, Executors.defaultThreadFactory());
    }

    public static ProcessExecutor newProcessExecutor(int nThreads, ThreadFactory threadFactory) {
        if (threadFactory == null) threadFactory = Executors.defaultThreadFactory();

        return new ThreadPoolProcessExecutor(nThreads, threadFactory);
    }

    /**
     * Reinitializes all static state, including the ProcessFactory service registry and reference
     * to the last used ProcessFactory
     */
    public static synchronized void reset() {
        if (registry == null) {
            // nothing to do
            return;
        }
        registry.deregisterAll();
        registry.scanForPlugins();
        lastFactory = null;
    }

    /**
     * This progress listener checks if the current Thread is interrupted, it acts as a bridge
     * between Future and ProgressListener code.
     *
     * @author Jody
     */
    static class CallableProgressListener extends NullProgressListener {
        @Override
        public boolean isCanceled() {
            return Thread.currentThread().isInterrupted();
        }
    }
}
