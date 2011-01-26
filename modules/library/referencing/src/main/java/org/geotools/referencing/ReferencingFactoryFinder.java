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
package org.geotools.referencing;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.Locale;
import java.util.Collections;
import java.util.LinkedHashSet;
import javax.imageio.spi.ServiceRegistry;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.Factory;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.referencing.operation.MathTransformFactory;

import org.geotools.factory.Hints;
import org.geotools.factory.GeoTools;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.resources.Arguments;
import org.geotools.resources.LazySet;


/**
 * Defines static methods used to access the application's default {@linkplain Factory
 * factory} implementation.
 *
 * <P>To declare a factory implementation, a services subdirectory is placed within the
 * {@code META-INF} directory that is present in every JAR file. This directory
 * contains a file for each factory interface that has one or more implementation classes
 * present in the JAR file. For example, if the JAR file contained a class named
 * {@code com.mycompany.DatumFactoryImpl} which implements the {@link DatumFactory}
 * interface, the JAR file would contain a file named:</P>
 *
 * <blockquote><pre>META-INF/services/org.opengis.referencing.datum.DatumFactory</pre></blockquote>
 *
 * <P>containing the line:</P>
 *
 * <blockquote><pre>com.mycompany.DatumFactoryImpl</pre></blockquote>
 *
 * <P>If the factory classes implements {@link javax.imageio.spi.RegisterableService}, it will
 * be notified upon registration and deregistration. Note that the factory classes should be
 * lightweight and quick to load. Implementations of these interfaces should avoid complex
 * dependencies on other classes and on native code. The usual pattern for more complex services
 * is to register a lightweight proxy for the heavyweight service.</P>
 *
 * <H2>Note on factory ordering</H2>
 * <P>This class is thread-safe. However, calls to any {@link #setAuthorityOrdering} or
 * {@link #setVendorOrdering} methods have a system-wide effect. If two threads or two
 * applications need a different ordering, they shall manage their own instance of
 * {@link FactoryRegistry}. This {@code FactoryFinder} class is simply a convenience
 * wrapper around a {@code FactoryRegistry} instance.</P>
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class ReferencingFactoryFinder extends FactoryFinder {
    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * The authority names. Will be created only when first needed.
     */
    private static Set<String> authorityNames;

    /**
     * Do not allows any instantiation of this class.
     */
    private ReferencingFactoryFinder() {
    }

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(ReferencingFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(new Class<?>[] {
                    DatumFactory.class,
                    CSFactory.class,
                    CRSFactory.class,
                    DatumAuthorityFactory.class,
                    CSAuthorityFactory.class,
                    CRSAuthorityFactory.class,
                    MathTransformFactory.class,
                    CoordinateOperationFactory.class,
                    CoordinateOperationAuthorityFactory.class});
        }
        return registry;
    }

    /**
     * Returns the names of all currently registered authorities.
     */
    public static synchronized Set<String> getAuthorityNames() {
        /*
         * IMPORTANT: Return the same Set instance (unmodifiable) as long as there is no change
         * in the list of registered factories, and create a new instance in case of changes.
         * 'add/removeAuthorityFactory(...)' and 'scanForPlugins()' methods reset 'authorityNames'
         * to null, which will cause the creation of a new Set instance. Some implementations like
         * AllAuthoritiesFactory rely on this behavior as a way to be notified of registration
         * changes for clearing their cache.
         */
        if (authorityNames == null) {
            authorityNames = new LinkedHashSet<String>();
            final Hints hints = EMPTY_HINTS;
loop:       for (int i=0; ; i++) {
                final Set<? extends AuthorityFactory> factories;
                switch (i) {
                    case 0:  factories = getCRSAuthorityFactories(hints);                 break;
                    case 1:  factories = getCSAuthorityFactories(hints);                  break;
                    case 2:  factories = getDatumAuthorityFactories(hints);               break;
                    case 3:  factories = getCoordinateOperationAuthorityFactories(hints); break;
                    default: break loop;
                }
                for (final AuthorityFactory factory : factories) {
                    final Citation authority = factory.getAuthority();
                    if (authority != null) {
                        authorityNames.add(Citations.getIdentifier(authority));
                        for (final Identifier id : authority.getIdentifiers()) {
                            authorityNames.add(id.getCode());
                        }
                    }
                }
            }
            authorityNames = Collections.unmodifiableSet(authorityNames);
        }
        return authorityNames;
    }

    /**
     * Returns all providers of the specified category.
     *
     * @param  category The factory category.
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available factory implementations.
     */
    private static synchronized <T extends Factory>
            Set<T> getFactories(final Class<T> category, Hints hints)
    {
        hints = mergeSystemHints(hints);
        return new LazySet<T>(getServiceRegistry().getServiceProviders(category, null, hints));
    }

    /**
     * Returns a provider of the specified category.
     *
     * @param  category The factory category.
     * @param  hints An optional map of hints, or {@code null} if none.
     * @param  key The hint key to use for searching an implementation.
     * @return The first factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         specified interface.
     */
    private static synchronized <T extends Factory> T getFactory(final Class<T> category,
            Hints hints, final Hints.Key key) throws FactoryRegistryException
    {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider(category, null, hints, key);
    }

    /**
     * Returns the first implementation of a factory matching the specified hints. If no
     * implementation matches, a new one is created if possible or an exception is thrown
     * otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  category  The authority factory type.
     * @param  authority The desired authority (e.g. "EPSG").
     * @param  hints     An optional map of hints, or {@code null} if none.
     * @param  key       The hint key to use for searching an implementation.
     * @return The first authority factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         specfied interface.
     */
    private static synchronized <T extends AuthorityFactory> T getAuthorityFactory(
            final Class<T> category, final String authority, Hints hints, final Hints.Key key)
            throws FactoryRegistryException
    {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider(category, new AuthorityFilter(authority), hints, key);
    }

    /**
     * Returns the first implementation of {@link DatumFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first datum factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link DatumFactory} interface.
     */
    public static DatumFactory getDatumFactory(final Hints hints) throws FactoryRegistryException {
        return getFactory(DatumFactory.class, hints, Hints.DATUM_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link DatumFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available datum factory implementations.
     */
    public static Set<DatumFactory> getDatumFactories(final Hints hints) {
        return getFactories(DatumFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CSFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate system factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CSFactory} interface.
     */
    public static CSFactory getCSFactory(final Hints hints) throws FactoryRegistryException {
        return getFactory(CSFactory.class, hints, Hints.CS_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link CSFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate system factory implementations.
     */
    public static Set<CSFactory> getCSFactories(final Hints hints) {
        return getFactories(CSFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CRSFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate reference system factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CRSFactory} interface.
     */
    public static CRSFactory getCRSFactory(final Hints hints) throws FactoryRegistryException {
        return getFactory(CRSFactory.class, hints, Hints.CRS_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link CRSFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate reference system factory implementations.
     */
    public static Set<CRSFactory> getCRSFactories(final Hints hints) {
        return getFactories(CRSFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CoordinateOperationFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     * <p>
     * Hints that may be understood includes
     * {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM_FACTORY},
     * {@link Hints#DATUM_SHIFT_METHOD     DATUM_SHIFT_METHOD},
     * {@link Hints#LENIENT_DATUM_SHIFT    LENIENT_DATUM_SHIFT} and
     * {@link Hints#VERSION                VERSION}.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate operation factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CoordinateOperationFactory} interface.
     */
    public static CoordinateOperationFactory getCoordinateOperationFactory(final Hints hints)
            throws FactoryRegistryException
    {
        return getFactory(CoordinateOperationFactory.class, hints,
                Hints.COORDINATE_OPERATION_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the
     * {@link CoordinateOperationFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate operation factory implementations.
     */
    public static Set<CoordinateOperationFactory> getCoordinateOperationFactories(final Hints hints) {
        return getFactories(CoordinateOperationFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link DatumAuthorityFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  authority The desired authority (e.g. "EPSG").
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first datum authority factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link DatumAuthorityFactory} interface.
     */
    public static DatumAuthorityFactory getDatumAuthorityFactory(final String authority,
                                                                 final Hints  hints)
            throws FactoryRegistryException
    {
        return getAuthorityFactory(DatumAuthorityFactory.class, authority, hints,
                Hints.DATUM_AUTHORITY_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link DatumAuthorityFactory}
     * interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available datum authority factory implementations.
     */
    public static Set<DatumAuthorityFactory> getDatumAuthorityFactories(final Hints hints) {
        return getFactories(DatumAuthorityFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CSAuthorityFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     * <p>
     * Hints that may be understood includes
     * {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER FORCE_LONGITUDE_FIRST_AXIS_ORDER},
     * {@link Hints#FORCE_STANDARD_AXIS_UNITS        FORCE_STANDARD_AXIS_UNITS} and
     * {@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS   FORCE_STANDARD_AXIS_DIRECTIONS} and
     * {@link Hints#VERSION                          VERSION}.
     *
     * @param  authority The desired authority (e.g. "EPSG").
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate system authority factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CSAuthorityFactory} interface.
     */
    public static CSAuthorityFactory getCSAuthorityFactory(final String authority,
                                                           final Hints  hints)
            throws FactoryRegistryException
    {
        return getAuthorityFactory(CSAuthorityFactory.class, authority, hints,
                Hints.CS_AUTHORITY_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link CSAuthorityFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate system authority factory implementations.
     */
    public static Set<CSAuthorityFactory> getCSAuthorityFactories(final Hints hints) {
        return getFactories(CSAuthorityFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CRSAuthorityFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     * <p>
     * Hints that may be understood includes
     * {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER FORCE_LONGITUDE_FIRST_AXIS_ORDER},
     * {@link Hints#FORCE_STANDARD_AXIS_UNITS        FORCE_STANDARD_AXIS_UNITS},
     * {@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS   FORCE_STANDARD_AXIS_DIRECTIONS} and
     * {@link Hints#VERSION                          VERSION}.
     * <p>
     * <b>TIP:</b> The EPSG official factory and the EPSG extensions (additional CRS provided by
     * ESRI and others) are two distinct factories. Call to {@code getCRSAuthorityFactory("EPSG",
     * null)} returns only one of those, usually the official EPSG factory. If the union of those
     * two factories is wanted, then a chain of fallbacks is wanted. Consider using something like:
     *
     * <blockquote><code>
     * {@linkplain org.geotools.referencing.factory.FallbackAuthorityFactory#create(Class,
     * java.util.Collection) FallbackAuthorityFactory.create}(CRSAuthorityFactory.class,
     * {@linkplain #getCRSAuthorityFactories getCRSAuthorityFactories}(hints));
     * </code></blockquote>
     *
     * @param  authority The desired authority (e.g. "EPSG").
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate reference system authority factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CRSAuthorityFactory} interface.
     */
    public static CRSAuthorityFactory getCRSAuthorityFactory(final String authority,
                                                             final Hints  hints)
            throws FactoryRegistryException
    {
        return getAuthorityFactory(CRSAuthorityFactory.class, authority, hints,
                Hints.CRS_AUTHORITY_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link CRSAuthorityFactory} interface.
     * This set can be used to list the available codes known to all authorities.
     * In the event that the same code is understood by more then one authority
     * you will need to assume both are close enough, or make use of this set directly
     * rather than use the {@link CRS#decode} convenience method.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate reference system authority factory implementations.
     */
    public static Set<CRSAuthorityFactory> getCRSAuthorityFactories(final Hints hints) {
        return getFactories(CRSAuthorityFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link CoordinateOperationAuthorityFactory} matching
     * the specified hints. If no implementation matches, a new one is created if possible or an
     * exception is thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  authority The desired authority (e.g. "EPSG").
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first coordinate operation authority factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link CoordinateOperationAuthorityFactory} interface.
     */
    public static CoordinateOperationAuthorityFactory getCoordinateOperationAuthorityFactory(
            final String authority, final Hints hints)
            throws FactoryRegistryException
    {
        return getAuthorityFactory(CoordinateOperationAuthorityFactory.class, authority, hints,
                Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the
     * {@link CoordinateOperationAuthorityFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available coordinate operation authority factory implementations.
     */
    public static Set<CoordinateOperationAuthorityFactory> getCoordinateOperationAuthorityFactories(
            final Hints hints)
    {
        return getFactories(CoordinateOperationAuthorityFactory.class, hints);
    }

    /**
     * Returns the first implementation of {@link MathTransformFactory} matching the specified
     * hints. If no implementation matches, a new one is created if possible or an exception is
     * thrown otherwise. If more than one implementation is registered and an
     * {@linkplain #setVendorOrdering ordering is set}, then the preferred
     * implementation is returned. Otherwise an arbitrary one is selected.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first math transform factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link MathTransformFactory} interface.
     */
    public static MathTransformFactory getMathTransformFactory(final Hints hints)
            throws FactoryRegistryException
    {
        return getFactory(MathTransformFactory.class, hints, Hints.MATH_TRANSFORM_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the
     * {@link MathTransformFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available math transform factory implementations.
     */
    public static Set<MathTransformFactory> getMathTransformFactories(final Hints hints) {
        return getFactories(MathTransformFactory.class, hints);
    }

    /**
     * Sets a pairwise ordering between two vendors. If one or both vendors are not
     * currently registered, or if the desired ordering is already set, nothing happens
     * and {@code false} is returned.
     * <p>
     * The example below said that an ESRI implementation (if available) is
     * preferred over the Geotools one:
     *
     * <blockquote><code>FactoryFinder.setVendorOrdering("ESRI", "Geotools");</code></blockquote>
     *
     * @param  vendor1 The preferred vendor.
     * @param  vendor2 The vendor to which {@code vendor1} is preferred.
     * @return {@code true} if the ordering was set for at least one category.
     */
    public static synchronized boolean setVendorOrdering(final String vendor1,
                                                         final String vendor2)
    {
        return getServiceRegistry().setOrdering(Factory.class, true,
                                                new VendorFilter(vendor1),
                                                new VendorFilter(vendor2));
    }

    /**
     * Unsets a pairwise ordering between two vendors. If one or both vendors are not
     * currently registered, or if the desired ordering is already unset, nothing happens
     * and {@code false} is returned.
     *
     * @param  vendor1 The preferred vendor.
     * @param  vendor2 The vendor to which {@code vendor1} is preferred.
     * @return {@code true} if the ordering was unset for at least one category.
     */
    public static synchronized boolean unsetVendorOrdering(final String vendor1,
                                                           final String vendor2)
    {
        return getServiceRegistry().setOrdering(Factory.class, false,
                                                new VendorFilter(vendor1),
                                                new VendorFilter(vendor2));
    }

    /**
     * A filter for factories provided by a given vendor.
     */
    private static final class VendorFilter implements ServiceRegistry.Filter {
        /** The vendor to filter. */
        private final String vendor;

        /** Constructs a filter for the given vendor. */
        public VendorFilter(final String vendor) {
            this.vendor = vendor;
        }

        /** Returns {@code true} if the specified provider is built by the vendor. */
        public boolean filter(final Object provider) {
            return Citations.titleMatches(((Factory)provider).getVendor(), vendor);
        }
    }

    /**
     * Sets a pairwise ordering between two authorities. If one or both authorities are not
     * currently registered, or if the desired ordering is already set, nothing happens
     * and {@code false} is returned.
     * <p>
     * The example below said that EPSG {@linkplain AuthorityFactory authority factories}
     * are preferred over ESRI ones:
     *
     * <blockquote><code>FactoryFinder.setAuthorityOrdering("EPSG", "ESRI");</code></blockquote>
     *
     * @param  authority1 The preferred authority.
     * @param  authority2 The authority to which {@code authority1} is preferred.
     * @return {@code true} if the ordering was set for at least one category.
     */
    public static synchronized boolean setAuthorityOrdering(final String authority1,
                                                            final String authority2)
    {
        return getServiceRegistry().setOrdering(AuthorityFactory.class, true,
                                                new AuthorityFilter(authority1),
                                                new AuthorityFilter(authority2));
    }

    /**
     * Unsets a pairwise ordering between two authorities. If one or both authorities are not
     * currently registered, or if the desired ordering is already unset, nothing happens
     * and {@code false} is returned.
     *
     * @param  authority1 The preferred authority.
     * @param  authority2 The vendor to which {@code authority1} is preferred.
     * @return {@code true} if the ordering was unset for at least one category.
     */
    public static synchronized boolean unsetAuthorityOrdering(final String authority1,
                                                              final String authority2)
    {
        return getServiceRegistry().setOrdering(AuthorityFactory.class, false,
                                                new AuthorityFilter(authority1),
                                                new AuthorityFilter(authority2));
    }

    /**
     * A filter for factories provided for a given authority.
     */
    private static final class AuthorityFilter implements ServiceRegistry.Filter {
        /** The authority to filter. */
        private final String authority;

        /** Constructs a filter for the given authority. */
        public AuthorityFilter(final String authority) {
            this.authority = authority;
        }

        /** Returns {@code true} if the specified provider is for the authority. */
        public boolean filter(final Object provider) {
            if (authority == null) {
                // If the user didn't specified an authority name, then the factory to use must
                // be specified explicitly through a hint (e.g. Hints.CRS_AUTHORITY_FACTORY).
                return false;
            }
            return Citations.identifierMatches(((AuthorityFactory)provider).getAuthority(), authority);
        }
    }

    /**
     * Programmatic management of authority factories.
     * Needed for user managed, not plug-in managed, authority factory.
     * Also useful for test cases.
     *
     * @param authority The authority factory to add.
     */
    public static synchronized void addAuthorityFactory(final AuthorityFactory authority) {
        if (registry == null) {
            scanForPlugins();
        }
        getServiceRegistry().registerServiceProvider(authority);
        authorityNames = null;
    }

    /**
     * Programmatic management of authority factories.
     * Needed for user managed, not plug-in managed, authority factory.
     * Also useful for test cases.
     *
     * @param authority The authority factory to remove.
     */
    public static synchronized void removeAuthorityFactory(final AuthorityFactory authority) {
        getServiceRegistry().deregisterServiceProvider(authority);
        authorityNames = null;
    }

    /**
     * Returns {@code true} if the specified factory is registered. A factory may have been
     * registered by {@link #scanForPlugins()} if it was declared in a {@code META-INF/services}
     * file, or it may have been {@linkplain #addAuthorityFactory added programmatically}.
     *
     * @since 2.4
     */
    public static synchronized boolean isRegistered(final Factory factory) {
        return factory.equals(getServiceRegistry().getServiceProviderByClass(factory.getClass()));
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the
     * application class path can theoretically change, or additional plug-ins may become available.
     * Rather than re-scanning the classpath on every invocation of the API, the class path is
     * scanned automatically only on the first invocation. Clients can call this method to prompt
     * a re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public static void scanForPlugins() {
        synchronized (ReferencingFactoryFinder.class) {
            authorityNames = null;
            if (registry != null) {
                registry.scanForPlugins();
            }
        }
        GeoTools.fireConfigurationChanged();
    }

    /**
     * List all available factory implementations in a tabular format. For each factory interface,
     * the first implementation listed is the default one. This method provides a way to check the
     * state of a system, usually for debugging purpose.
     *
     * @param  out The output stream where to format the list.
     * @param  locale The locale for the list, or {@code null}.
     * @throws IOException if an error occurs while writting to {@code out}.
     */
    public static synchronized void listProviders(final Writer out, final Locale locale)
            throws IOException
    {
        final FactoryRegistry registry = getServiceRegistry();
        new FactoryPrinter().list(registry, out, locale);
    }
    
    /**
     * Resets the factory finder and prepares for a new full scan of the SPI subsystems
     */
    public static void reset() {
        FactoryRegistry copy = registry;
        registry = null;
        if(copy != null) {
            copy.deregisterAll();
        }
    }

    /**
     * Dump to the standard output stream a list of available factory implementations.
     * This method can be invoked from the command line. It provides a mean to verify
     * if some implementations were found in the classpath. The syntax is:
     * <BR>
     * <BLOCKQUOTE><CODE>
     * java org.geotools.referencing.FactoryFinder <VAR>&lt;options&gt;</VAR>
     * </CODE></BLOCKQUOTE>
     *
     * <P>where options are:</P>
     *
     * <TABLE CELLPADDING='0' CELLSPACING='0'>
     *   <TR><TD NOWRAP><CODE>-encoding</CODE> <VAR>&lt;code&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the character encoding</TD></TR>
     *   <TR><TD NOWRAP><CODE>-locale</CODE> <VAR>&lt;language&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the language for the output (e.g. "fr" for French)</TD></TR>
     * </TABLE>
     *
     * <P><strong>Note for Windows users:</strong> If the output contains strange
     * symbols, try to supply an "{@code -encoding}" argument. Example:</P>
     *
     * <blockquote><code>
     * java org.geotools.referencing.FactoryFinder -encoding Cp850
     * </code></blockquote>
     *
     * <P>The codepage number (850 in the previous example) can be obtained from the DOS
     * commande line using the "{@code chcp}" command with no arguments.</P>
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        args = arguments.getRemainingArguments(0);
        try {
            listProviders(arguments.out, arguments.locale);
        } catch (Exception exception) {
            exception.printStackTrace(arguments.err);
        }
    }
}
