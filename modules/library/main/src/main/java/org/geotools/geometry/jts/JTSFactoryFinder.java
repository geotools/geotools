/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import org.geotools.factory.Hints;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.resources.LazySet;


/**
 * Defines static methods used to access {@linkplain GeometryFactory geometry},
 * {@linkplain CoordinateSequenceFactory coordinate sequence} or
 * {@linkplain PrecisionModel precision model} factories.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class JTSFactoryFinder extends FactoryFinder {
    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private JTSFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(JTSFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { GeometryFactory.class } ));
            registry.registerServiceProvider( new GeometryFactory(), GeometryFactory.class );
        }
        return registry;
    }

    /**
     * Returns the first implementation of {@link GeometryFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     * <p>
     * Hints that may be understood includes
     * {@link Hints#JTS_COORDINATE_SEQUENCE_FACTORY JTS_COORDINATE_SEQUENCE_FACTORY},
     * {@link Hints#JTS_PRECISION_MODEL             JTS_PRECISION_MODEL} and
     * {@link Hints#JTS_SRID                        JTS_SRID}.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first geometry factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link GeometryFactory} category and the given hints.
     */
    public static synchronized GeometryFactory getGeometryFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return (GeometryFactory) getServiceRegistry().getServiceProvider(
                GeometryFactory.class, null, hints, Hints.JTS_GEOMETRY_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link GeometryFactory} category.
     *
     * @return Set of available geometry factory implementations.
     */
    public static synchronized Set getGeometryFactories() {
        return new LazySet(getServiceRegistry().getServiceProviders(GeometryFactory.class, null, null));
    }

    /**
     * Returns the first implementation of {@link PrecisionModel} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first precision model that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link PrecisionModel} category and the given hints.
     */
    public static synchronized PrecisionModel getPrecisionModel(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return (PrecisionModel) getServiceRegistry().getServiceProvider(
                PrecisionModel.class, null, hints, Hints.JTS_PRECISION_MODEL);
    }

    /**
     * Returns a set of all available implementations for the {@link PrecisionModel} category.
     *
     * @return Set of available precision model implementations.
     */
    public static synchronized Set getPrecisionModels() {
        return new LazySet(getServiceRegistry().getServiceProviders(PrecisionModel.class, null, null));
    }

    /**
     * Returns the first implementation of {@link CoordinateSequenceFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate sequence factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CoordinateSequenceFactory} interface and the given hints.
     */
    public static synchronized CoordinateSequenceFactory getCoordinateSequenceFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return (CoordinateSequenceFactory) getServiceRegistry().getServiceProvider(
                CoordinateSequenceFactory.class, null, hints, Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link CoordinateSequenceFactory}
     * interface.
     *
     * @return Set of available coordinate sequence factory implementations.
     */
    public static synchronized Set getCoordinateSequenceFactories() {
        return new LazySet(getServiceRegistry().getServiceProviders(CoordinateSequenceFactory.class, null, null));
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is
     * needed because the application class path can theoretically change, or
     * additional plug-ins may become available. Rather than re-scanning the
     * classpath on every invocation of the API, the class path is scanned
     * automatically only on the first invocation. Clients can call this
     * method to prompt a re-scan. Thus this method need only be invoked by
     * sophisticated applications which dynamically make new plug-ins
     * available at runtime.
     */
    public static void scanForPlugins() {
        if (registry != null) {
            registry.scanForPlugins();
        }
    }

    /**
     * A custom registry for JTS factories. There is usually no need for custom implementation of
     * {@link ServiceRegistry} for geotools object. However, JTS factories are an other story
     * because they don't know anything about the Geotools's factory plugin system. Consequently
     * we need to process JTS factories in a special way.
     */
    private static final class Registry extends FactoryCreator {
        /**
         * Creates a registry for JTS factories.
         */
        public Registry() {
            super(Arrays.asList(new Class<?>[] {
                    GeometryFactory.class,
                    PrecisionModel.class,
                    CoordinateSequenceFactory.class}));
        }

        /**
         * Creates a new instance of the specified factory using the specified hints.
         *
         * @param  category The category to instantiate.
         * @param  implementation The factory class to instantiate.
         * @param  hints The implementation hints.
         * @return The factory.
         * @throws FactoryRegistryException if the factory creation failed.
         */
        protected Object createServiceProvider(final Class category,
                                               final Class implementation,
                                               final Hints hints)
                throws FactoryRegistryException
        {
            if (GeometryFactory.class.isAssignableFrom(category) &&
                GeometryFactory.class.equals(implementation))
            {
                return new GeometryFactory(getPrecisionModel(hints), getSRID(hints),
                                           getCoordinateSequenceFactory(hints));
            }
            return super.createServiceProvider(category, implementation, hints);
        }

        /**
         * Extracts the SRID from the hints, or returns {@code 0} if none.
         */
        private static int getSRID(final Hints hints) {
            if (hints != null) {
                final Integer SRID = (Integer) hints.get(Hints.JTS_SRID);
                if (SRID != null) {
                    return SRID.intValue();
                }
            }
            return 0;
        }

        /**
         * Returns {@code true} if the specified {@code provider} meets the requirements specified
         * by a map of {@code hints}. This method is invoked automatically when the {@code provider}
         * is known to meets standard Geotools requirements.
         * <p>
         * This implementation add JTS-specific checks. More specifically, we checks if
         * {@link GeometryFactory} uses the required {@link CoordinateSequenceFactory}
         * and {@link PrecisionModel}.
         *
         * @param provider The provider to checks.
         * @param category The factory's category.
         * @param hints    The user requirements.
         * @return {@code true} if the {@code provider} meets the user requirements.
         */
        protected boolean isAcceptable(final Object provider, final Class category, final Hints hints) {
            if (GeometryFactory.class.isAssignableFrom(category)) {
                final GeometryFactory           factory   = (GeometryFactory) provider;
                final CoordinateSequenceFactory sequence  = factory.getCoordinateSequenceFactory();
                final PrecisionModel            precision = factory.getPrecisionModel();
                if (!isAcceptable(sequence,  hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY)) ||
                    !isAcceptable(precision, hints.get(Hints.JTS_PRECISION_MODEL)))
                {
                    return false;
                }
                final int SRID = getSRID(hints);
                if (SRID!=0 && SRID!=factory.getSRID()) {
                    return false;
                }
            }
            return super.isAcceptable(provider, category, hints);
        }

        /**
         * Checks if an actual {@link GeometryFactory} property matches the given hint.
         *
         * @param actual The geometry factory property, either a {@link CoordinateSequenceFactory}
         *               or a {@link PrecisionModel} concrete implementation.
         * @param requested The user's hint, either a concrete implementation of the same class
         *                  than {@code actual}, a {@link Class} or an array of them.
         * @return {@code true} if the {@code actual} value matches the {@code requested}
         *         value, or {@code false} otherwise.
         */
        private static boolean isAcceptable(final Object actual,
                                            final Object requested)
        {
            /*
             * If the user didn't provided any hint, or if the factory
             * already uses the requested object, accepts.
             */
            if (requested==null || requested.equals(actual)) {
                return true;
            }
            /*
             * If hint is an array (either Class object or concrete
             * implementations), iterates over all array's elements.
             */
            if (requested.getClass().isArray()) {
                final int length = Array.getLength(requested);
                for (int i=0; i<length; i++) {
                    if (!isAcceptable(actual, Array.get(requested, i))) {
                        return false;
                    }
                }
                return true;
            }
            /*
             * If hint is only a class instead of an actual implementation,
             * accepts instances of this class or any subclasses.
             */
            if (actual!=null && requested instanceof Class) {
                return ((Class) requested).isAssignableFrom(actual.getClass());
            }
            return false;
        }
    }
}
