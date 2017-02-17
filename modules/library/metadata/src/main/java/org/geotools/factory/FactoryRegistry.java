/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.awt.RenderingHints;

import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;


/**
 * A registry for factories, organized by categories (usualy by <strong>interface</strong>). For
 * example <code>{@linkplain org.opengis.referencing.crs.CRSFactory}.class</code> is a category,
 * and <code>{@linkplain org.opengis.referencing.operation.MathTransformFactory}.class</code> is
 * an other category.
 * <p>
 * For each category, implementations are registered in a file placed in the
 * {@code META-INF/services/} directory, as specified in the {@link ServiceLoader}
 * javadoc. Those files are usually bundled into the JAR file distributed by the vendor.
 * If the same {@code META-INF/services/} file appears many time in different JARs,
 * they are processed as if their content were merged.
 * <p>
 * Example use:
 * <blockquote><code>
 * Set&lt;Class&lt;?&gt;&gt; categories =
 *     Collections.singleton(new Class&lt;?&gt;[] {<br>
 * &npsp;&npsp;&npsp;&npsp;MathTransformProvider.class<br>
 * });<br>
 * FactoryRegistry registry = new FactoryRegistry(categories);<br>
 * <br>
 * // get the providers<br>
 * Filter filter = null;<br>
 * Hints hints = null;<br>
 * Iterator&lt;MathTransform&gt; providers =
 *     registry.getServiceProviders(MathTransformProvider.class, filter, hints);<br>
 * </code></blockquote>
 * <p>
 * <strong>NOTE: This class is not thread safe</strong>. Users are responsable
 * for synchronisation. This is usually done in an utility class wrapping this
 * service registry (e.g. {@link org.geotools.referencing.ReferencingFactoryFinder}).
 *
 * @since 2.1
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Richard Gould
 * @author Jody Garnett
 *
 * @see org.geotools.referencing.ReferencingFactoryFinder
 * @see org.geotools.coverage.CoverageFactoryFinder
 */
public class FactoryRegistry {
    /**
     * The logger for all events related to factory registry.
     */
    protected static final Logger LOGGER = Logging.getLogger("org.geotools.factory");

    /**
     * The logger level for debug messages.
     */
    private static final Level DEBUG_LEVEL = Level.FINEST;

    /**
     * A copy of the global configuration defined through {@link FactoryIteratorProviders}
     * static methods. We keep a copy in every {@code FactoryRegistry} instance in order to
     * compare against the master {@link FactoryIteratorProviders#GLOBAL} and detect if the
     * configuration changed since the last time this registry was used.
     *
     * @see #synchronizeIteratorProviders
     */
    private final FactoryIteratorProviders globalConfiguration = new FactoryIteratorProviders();

    /**
     * The set of category that need to be scanned for plugins, or {@code null} if none.
     * On initialization, all categories need to be scanned for plugins. After a category
     * has been first used, it is removed from this set so we don't scan for plugins again.
     */
    private Set<Class<?>> needScanForPlugins;

    /**
     * Categories under scanning. This is used by {@link #scanForPlugins(Collection,Class)}
     * as a guard against infinite recursivity (i.e. when a factory to be scanned request
     * an other dependency of the same category).
     */
    private final RecursionCheckingHelper scanningCategories = new RecursionCheckingHelper();

    /**
     * Factories under testing for availability. This is used by
     * {@link #isAvailable} as a guard against infinite recursivity.
     */
    private final RecursionCheckingHelper testingAvailability = new RecursionCheckingHelper();

    /**
     * Factories under testing for hints compatibility. This is used by
     * {@link #usesAcceptableHints} as a guard against infinite recursivity.
     */
    private final RecursionCheckingHelper testingHints = new RecursionCheckingHelper();

    private final Map<Class<?>, SubRegistry> categoryMap = new HashMap<>();

    /**
     * A simple filter interface used by <code>FactoryRegistry.getServiceProviders</code> to select
     * providers matching an arbitrary criterion. Classes that implement this interface should be
     * defined in order to make use of the <code>getServiceProviders</code> method of
     * <code>FactoryRegistry</code> that takes a <code>Filter</code>.
     *
     * @see FactoryRegistry#getServiceProviders(Class, FactoryRegistry.Filter, boolean)
     */
    public interface Filter {

        /**
         * Returns <code>true</code> if the given <code>provider</code> object matches the criterion
         * defined by this <code>Filter</code>.
         *
         * @param provider a service provider <code>Object</code>.
         *
         * @return true if the provider matches the criterion.
         */
        boolean filter(Object provider);
    }
    
    /**
     * A class for wrapping <code>Iterators</code> with a filter function.
     * This provides an iterator for a subset without duplication.
     * @since 15.0
     */
    private static class FilterIterator<T> implements Iterator<T> {

        private final Iterator<T> iter;
        private final Filter filter;

        private T next = null;

        public FilterIterator(Iterator<T> iter, Filter filter) {
            this.iter = iter;
            this.filter = filter;
            advance();
        }

        private void advance() {
            while (iter.hasNext()) {
                T elt = iter.next();
                if (filter.filter(elt)) {
                    next = elt;
                    return;
                }
            }

            next = null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            T o = next;
            advance();
            return o;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A portion of a registry dealing with a single superclass or interface.
     * @since 15.0
     */
    private static class SubRegistry {

        private final FactoryRegistry registry;

        private final Class<?> category;

        // Provider Objects organized by partial ordering
        private final Class<?> cPartiallyOrderedSet;
        private final Object poset;

        // Class -> Provider Object of that class
        private final Map<Class<?>,Object> map = new HashMap<>();

        public SubRegistry(FactoryRegistry registry, Class<?> category) {
            this.registry = registry;
            this.category = category;
            try {
                this.cPartiallyOrderedSet = Class.forName("javax.imageio.spi.PartiallyOrderedSet");
                Constructor<?> constructor = cPartiallyOrderedSet.getDeclaredConstructor();
                constructor.setAccessible(true);
                this.poset = constructor.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                    NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
                throw new FactoryRegistryException("Cannot instantiate PartiallyOrderedSet", e);
            }
        }
        
        private Object callReflection0(String name) {
            try {
                Method m = cPartiallyOrderedSet.getDeclaredMethod(name);
                m.setAccessible(true);
                return m.invoke(poset);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new FactoryRegistryException("Cannot invoke PartiallyOrderedSet#" + name, e);
            }
        }

        private Object callReflection1(String name, Object arg) {
            try {
                Method m = cPartiallyOrderedSet.getDeclaredMethod(name, Object.class);
                m.setAccessible(true);
                return m.invoke(poset, arg);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new FactoryRegistryException("Cannot invoke PartiallyOrderedSet#" + name, e);
            }
        }

        private boolean callReflection2(String name, Object arg1, Object arg2) {
            try {
                Method m = cPartiallyOrderedSet.getDeclaredMethod(name, Object.class, Object.class);
                m.setAccessible(true);
                return (boolean) m.invoke(poset, arg1, arg2);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new FactoryRegistryException("Cannot invoke PartiallyOrderedSet#" + name, e);
            }
        }

        public boolean registerServiceProvider(Object provider) {
            Object oprovider = map.get(provider.getClass());
            boolean present = oprovider != null;

            if (present) {
                deregisterServiceProvider(oprovider);
            }
            map.put(provider.getClass(), provider);
            callReflection1("add", provider);
            if (provider instanceof RegisterableService) {
                ((RegisterableService)provider).onRegistration(registry, category);
            }

            return !present;
        }

        /**
         * If the provider was not previously registered, do nothing.
         *
         * @return true if the provider was previously registered.
         */
        public boolean deregisterServiceProvider(Object provider) {
            Object oprovider = map.get(provider.getClass());

            if (provider == oprovider) {
                map.remove(provider.getClass());
                callReflection1("remove", provider);
                if (provider instanceof RegisterableService) {
                    ((RegisterableService)provider).onDeregistration(registry, category);
                }

                return true;
            }
            return false;
        }

        public boolean contains(Object provider) {
            return map.get(provider.getClass()) == provider;
        }

        public boolean setOrdering(Object firstProvider, Object secondProvider) {
            return callReflection2("setOrdering", firstProvider, secondProvider);
        }

        public boolean unsetOrdering(Object firstProvider, Object secondProvider) {
            return callReflection2("unsetOrdering", firstProvider, secondProvider);
        }

        public Iterator getServiceProviders(boolean useOrdering) {
            if (useOrdering) {
                return (Iterator) callReflection0("iterator");
            } else {
                return map.values().iterator();
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T getServiceProviderByClass(Class<T> providerClass) {
            return (T)map.get(providerClass);
        }

        public void clear() {
            Iterator<?> iter = map.values().iterator();
            while (iter.hasNext()) {
                Object provider = iter.next();
                iter.remove();

                if (provider instanceof RegisterableService) {
                    ((RegisterableService)provider).onDeregistration(registry, category);
                }
            }
            callReflection0("clear");
        }

        public void finalize() {
            clear();
        }
    }

    /**
     * Constructs a new registry for the specified category.
     *
     * @param category The single category.
     *
     * @since 2.4
     */
    @SuppressWarnings("unchecked")
    public FactoryRegistry(final Class<?> category) {
        this((Collection) Collections.singleton(category));
    }

    /**
     * Constructs a new registry for the specified categories.
     *
     * @param categories The categories.
     *
     * @since 2.4
     */
    public FactoryRegistry(final Class<?>[] categories) {
        this(Arrays.asList(categories));
    }

    /**
     * Constructs a new registry for the specified categories.
     *
     * @param categories The categories.
     */
    public FactoryRegistry(final Collection<Class<?>> categories) {
        this(categories.iterator());
        for (final Iterator<Class<?>> it=getCategories(); it.hasNext();) {
            if (needScanForPlugins == null) {
                needScanForPlugins = new HashSet<Class<?>>();
            }
            needScanForPlugins.add(it.next());
        }
    }

    /**
     * Constructs a new registry for the specified categories.
     *
     * @param categories an <code>Iterator</code> containing
     * <code>Class</code> objects to be used to define categories.
     *
     * @exception IllegalArgumentException if
     * <code>categories</code> is <code>null</code>.
     */
    private FactoryRegistry(Iterator<Class<?>> categories) {
        if (categories == null) {
            throw new IllegalArgumentException("categories == null!");
        }
        while (categories.hasNext()) {
            Class<?> category = (Class<?>)categories.next();
            categoryMap.put(category, new SubRegistry(this, category));
        }
    }

    /**
     * Returns the providers in the registry for the specified category, filter and hints.
     * Providers that are not {@linkplain OptionalFactory#isAvailable available} will be
     * ignored. This method will {@linkplain #scanForPlugins() scan for plugins} the first
     * time it is invoked for the given category.
     *
     * @param <T>      The class represented by the {@code category} argument.
     * @param category The category to look for. Usually an interface class
     *                 (not the actual implementation class).
     * @param filter   The optional filter, or {@code null}.
     * @param hints    The optional user requirements, or {@code null}.
     * @return Factories ready to use for the specified category, filter and hints.
     *
     * @since 2.3
     */
    public synchronized <T> Iterator<T> getServiceProviders(final Class<T> category, final Filter filter, final Hints hints) {
        /*
         * The implementation of this method is very similar to the 'getUnfilteredProviders'
         * one except for filter handling. See the comments in 'getUnfilteredProviders' for
         * more implementation details.
         */
        if (scanningCategories.contains(category)) {
            // Please note you will get errors here if you accidentally allow
            // more than one thread to use your FactoryRegistry at a time.
            throw new RecursiveSearchException(category);
        }
        final Filter hintsFilter = new Filter() {
            public boolean filter(final Object provider) {
                return isAcceptable(category.cast(provider), category, hints, filter);
            }
        };
        synchronizeIteratorProviders();
        scanForPluginsIfNeeded(category);
        return getServiceProviders(category, hintsFilter, true);
    }

    /**
     * Returns an <code>Iterator</code> containing service provider objects within a given category
     * that satisfy a criterion imposed by the supplied <code>FactoryRegistry.Filter</code>
     * object's <code>filter</code> method.
     *
     * <p> The <code>useOrdering</code> argument controls the ordering of the results using the
     * same rules as <code>getServiceProviders(Class, boolean)</code>.
     *
     * @param category the category to be retrieved from.
     * @param filter an instance of <code>FactoryRegistry.Filter</code> whose <code>filter</code>
     *        method will be invoked.
     * @param order <code>true</code> if pairwise orderings should be taken account in ordering the
     *        returned objects.
     *
     * @return an <code>Iterator</code> containing service provider objects from the given
     *         category, possibly in order.
     *
     * @throws IllegalArgumentException if there is no category corresponding to <code>category</code>.
     */
    public <T> Iterator<T> getServiceProviders(Class<T> category, Filter filter, boolean order) {
        SubRegistry reg = categoryMap.get(category);
        if (reg == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        return new FilterIterator<T>(getServiceProviders(category, order), filter);
    }

    /**
     * Returns an <code>Iterator</code> containing all registered service providers in the given
     * category. If <code>order</code> is <code>false</code>, the iterator will return all of the
     * server provider objects in an arbitrary order. Otherwise, the ordering will respect any
     * pairwise orderings that have been set.  If the graph of pairwise orderings contains cycles,
     * any providers that belong to a cycle will not be returned.
     *
     * @param category the category to be retrieved from.
     * @param order <code>true</code> if pairwise orderings should be taken account in ordering the
     *        returned objects.
     *
     * @return an <code>Iterator</code> containing service provider objects from the given
     *         category, possibly in order.
     *
     * @throws IllegalArgumentException if there is no category corresponding to <code>category</code>.
     */
    public <T> Iterator<T> getServiceProviders(Class<T> category, boolean order) {
        SubRegistry reg = (SubRegistry)categoryMap.get(category);
        if (reg == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        return reg.getServiceProviders(order);
    }

    /**
     * Returns the currently registered service provider object that is of the given class type.
     * At most one object of a given class is allowed to be registered at any given time. If no
     * registered object has the desired class type, <code>null</code> is returned.
     *
     * @param providerClass the <code>Class</code> of the desired service provider object.
     *
     * @return a currently registered service provider object with the desired 
     * <code>Class</code>type, or <code>null</code> is none is present.
     *
     * @throws IllegalArgumentException if <code>providerClass</code> is <code>null</code>.
     */
    public <T> T getServiceProviderByClass(Class<T> providerClass) {
        if (providerClass == null) {
            throw new IllegalArgumentException("providerClass == null!");
        }
        Iterator<Class<?>> iter = categoryMap.keySet().iterator();
        while (iter.hasNext()) {
            Class<?> c = iter.next();
            if (c.isAssignableFrom(providerClass)) {
                T provider = categoryMap.get(c).getServiceProviderByClass(providerClass);
                if (provider != null) {
                    return provider;
                }
            }
        }
        return null;
    }

    /**
     * Implementation of {@link #getServiceProviders(Class, Filter, Hints)} without the filtering
     * applied by the {@link #isAcceptable(Object, Class, Hints, Filter)} method. If this filtering
     * is not already presents in the filter given to this method, then it must be applied on the
     * elements returned by the iterator. The later is preferrable when:
     * <p>
     * <ul>
     *   <li>There is some cheaper tests to perform before {@code isAcceptable}.</li>
     *   <li>We don't want a restrictive filter in order to avoid trigging a classpath
     *       scan if this method doesn't found any element to iterate.</li>
     * </ul>
     * <p>
     * <b>Note:</b>
     * {@link #synchronizeIteratorProviders} should also be invoked once before this method.
     *
     * @todo Use Hints to match Constructor.
     */
    final <T> Iterator<T> getUnfilteredProviders(final Class<T> category) {
        /*
         * If the map is not empty, then this mean that a scanning is under progress, i.e.
         * 'scanForPlugins' is currently being executed. This is okay as long as the user
         * is not asking for one of the categories under scanning. Otherwise, the answer
         * returned by 'getServiceProviders' would be incomplete because not all plugins
         * have been found yet. This can lead to some bugs hard to spot because this methoud
         * could complete normally but return the wrong plugin. It is safer to thrown an
         * exception so the user is advised that something is wrong.
         */
        if (scanningCategories.contains(category)) {
            throw new RecursiveSearchException(category);
        }
        scanForPluginsIfNeeded(category);
        return getServiceProviders(category, true);
    }

    /**
     * Returns the first provider in the registry for the specified category, using the specified
     * map of hints (if any). This method may {@linkplain #scanForPlugins scan for plugins} the
     * first time it is invoked. Except as a result of this scan, no new provider instance is
     * created by the default implementation of this method. The {@link FactoryCreator} class
     * change this behavior however.
     *
     * @param  <T>      The class represented by the {@code category} argument.
     * @param  category The category to look for. Must be one of the categories declared to the
     *                  constructor. Usually an interface class (not the actual implementation
     *                  class).
     * @param  filter   An optional filter, or {@code null} if none.
     *                  This is used for example in order to select the first factory for some
     *                  {@linkplain org.opengis.referencing.AuthorityFactory#getAuthority authority}.
     * @param  hints    A {@linkplain Hints map of hints}, or {@code null} if none.
     * @param  key      The key to use for looking for a user-provided instance in the hints, or
     *                  {@code null} if none.
     * @return A factory {@linkplain OptionalFactory#isAvailable available} for use for the
     *         specified category and hints. The returns type is {@code Object} instead of
     *         {@link Factory} because the factory implementation doesn't need to be a Geotools one.
     * @throws FactoryNotFoundException if no factory was found for the specified category, filter
     *         and hints.
     * @throws FactoryRegistryException if a factory can't be returned for some other reason.
     *
     * @see #getServiceProviders(Class, Filter, Hints)
     * @see FactoryCreator#getServiceProvider
     */
    public <T> T getServiceProvider(final Class<T> category, final Filter filter,
                                    Hints hints, final Hints.Key key)
            throws FactoryRegistryException
    {
        synchronizeIteratorProviders();
        final boolean debug = LOGGER.isLoggable(DEBUG_LEVEL);
        if (debug) {
            /*
             * We are not required to insert the method name ("GetServiceProvider") in the
             * message because it is part of the informations already stored by LogRecord,
             * and formatted by the default java.util.logging.SimpleFormatter.
             *
             * Conventions for the message part according java.util.logging.Logger javadoc:
             * - "ENTRY"  at the begining of a method.
             * - "RETURN" at the end of a method, if successful.
             * - "THROW"  in case of failure.
             * - "CHECK"  ... is our own addition to Sun's convention for this method ...
             */
            debug("ENTRY", category, key, null, null);
        }
        Class<?> implementation = null;
        if (key != null) {
            /*
             * Sanity check: make sure that the key class is appropriate for the category.
             */
            final Class<?> valueClass = key.getValueClass();
            if (!category.isAssignableFrom(valueClass)) {
                if (debug) {
                    debug("THROW", category, key, "unexpected type:", valueClass);
                }
                throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_KEY_$1, key));
            }
            if (hints != null) {
                final Object hint = hints.get(key);
                if (hint != null) {
                    if (debug) {
                        debug("CHECK", category, key, "user provided a", hint.getClass());
                    }
                    if (category.isInstance(hint)) {
                        /*
                         * The factory implementation was given explicitly by the user.
                         * Nothing to do; we are done.
                         */
                        if (debug) {
                            debug("RETURN", category, key, "return hint as provided.", null);
                        }
                        return category.cast(hint);
                    }
                    /*
                     * Before to pass the hints to the private 'getServiceImplementation' method,
                     * remove the hint for the user-supplied key.  This is because this hint has
                     * been processed by this public 'getServiceProvider' method, and the policy
                     * is to remove the processed hints before to pass them to child dependencies
                     * (see the "Check recursively in factory dependencies" comment elswhere in
                     * this class).
                     *
                     * Use case: DefaultDataSourceTest invokes indirectly 'getServiceProvider'
                     * with a "CRS_AUTHORITY_FACTORY = ThreadedEpsgFactory.class" hint. However
                     * ThreadedEpsgFactory (in the org.geotools.referencing.factory.epsg package)
                     * is a wrapper around DirectEpsgFactory, and defines this dependency through
                     * a "CRS_AUTHORITY_FACTORY = DirectEpsgFactory.class" hint. There is no way
                     * to match this hint for both factories in same time. Since we must choose
                     * one, we assume that the user is interrested in the most top level one and
                     * discart this particular hint for the dependencies.
                     */
                    hints = new Hints(hints);
                    if (hints.remove(key) != hint) {
                        // Should never happen except on concurrent modification in an other thread.
                        throw new AssertionError(key);
                    }
                    /*
                     * If the user accepts many implementation classes, then try all of them in
                     * the preference order given by the user. The last class (or the singleton
                     * if the hint was not an array) will be tried using the "normal" path
                     * (oustide the loop) in order to get the error message in case of failure.
                     */
                    if (hint instanceof Class<?>[]) {
                        final Class<?>[] types = (Class<?>[]) hint;
                        final int length = types.length;
                        for (int i=0; i<length-1; i++) {
                            final Class<?> type = types[i];
                            if (debug) {
                                debug("CHECK", category, key, "consider hint[" + i + ']', type);
                            }
                            final T candidate = getServiceImplementation(category, type, filter, hints);
                            if (candidate != null) {
                                if (debug) {
                                    debug("RETURN", category, key, "found implementation", candidate.getClass());
                                }
                                return candidate;
                            }
                        }
                        if (length != 0) {
                            implementation = types[length-1]; // Last try to be done below.
                        }
                    } else {
                        implementation = (Class<?>) hint;
                    }
                }
            }
        }
        if (debug && implementation != null) {
            debug("CHECK", category, key, "consider hint[last]", implementation);
        }
        final T candidate = getServiceImplementation(category, implementation, filter, hints);
        if (candidate != null) {
            if (debug) {
                debug("RETURN", category, key, "found implementation", candidate.getClass());
            }
            return candidate;
        }
        if (debug) {
            debug("THROW", category, key, "could not find implementation.", null);
        }
        throw new FactoryNotFoundException(Errors.format(ErrorKeys.FACTORY_NOT_FOUND_$1,
                  implementation!=null ? implementation : category));
    }

    /**
     * Logs a debug message for {@link #getServiceProvider} method. Note: we are not required
     * to insert the method name ({@code "GetServiceProvider"}) in the message because it is
     * part of the informations already stored by {@link LogRecord}, and formatted by the
     * default {@link java.util.logging.SimpleFormatter}.
     *
     * @param status   {@code "ENTRY"}, {@code "RETURN"} or {@code "THROW"},
     *                 according {@link Logger} conventions.
     * @param category The category given to the {@link #getServiceProvider} method.
     * @param key      The key being examined, or {@code null}.
     * @param message  Optional message, or {@code null} if none.
     * @param type     Optional class to format after the message, or {@code null}.
     */
    private static void debug(final String status, final Class<?> category,
                              final Hints.Key key, final String message, final Class type)
    {
        final StringBuilder buffer = new StringBuilder(status);
        buffer.append(Utilities.spaces(Math.max(1, 7-status.length())))
              .append('(').append(Classes.getShortName(category));
        if (key != null) {
            buffer.append(", ").append(key);
        }
        buffer.append(')');
        if (message != null) {
            buffer.append(": ").append(message);
        }
        if (type != null) {
            buffer.append(' ').append(Classes.getShortName(type)).append('.');
        }
        final LogRecord record = new LogRecord(DEBUG_LEVEL, buffer.toString());
        record.setSourceClassName(FactoryRegistry.class.getName());
        record.setSourceMethodName("getServiceProvider");
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Searchs the first implementation in the registery matching the specified conditions.
     * This method is invoked only by the {@link #getServiceProvider(Class, Filter, Hints,
     * Hints.Key)} public method above; there is no recursivity there. This method do not
     * creates new instance if no matching factory is found.
     *
     * @param  category       The category to look for. Usually an interface class.
     * @param  implementation The desired class for the implementation, or {@code null} if none.
     * @param  filter         An optional filter, or {@code null} if none.
     * @param  hints          A {@linkplain Hints map of hints}, or {@code null} if none.
     * @return A factory for the specified category and hints, or {@code null} if none.
     */
    private <T> T getServiceImplementation(final Class<T> category, final Class<?> implementation,
                                           final Filter filter, final Hints hints)
    {
        for (final Iterator<T> it=getUnfilteredProviders(category); it.hasNext();) {
            final T candidate = it.next();
            // Implementation class must be tested before 'isAcceptable'
            // in order to avoid StackOverflowError in some situations.
            if (implementation!=null && !implementation.isInstance(candidate)) {
                continue;
            }
            if (!isAcceptable(candidate, category, hints, filter)) {
                continue;
            }
            return candidate;
        }
        final List<Reference<T>> cached = getCachedProviders(category);
        if (cached != null) {
            /*
             * Checks if a factory previously created by FactoryCreator could fit. This
             * block should never be executed if this instance is not a FactoryCreator.
             */
            for (final Iterator<Reference<T>> it=cached.iterator(); it.hasNext();) {
                final T candidate = it.next().get();
                if (candidate == null) {
                    it.remove();
                    continue;
                }
                if (implementation!=null && !implementation.isInstance(candidate)) {
                    continue;
                }
                if (!isAcceptable(candidate, category, hints, filter)) {
                    continue;
                }
                return candidate;
            }
        }
        return null;
    }

    /**
     * Returns the providers available in the cache, or {@code null} if none.
     * To be overridden by {@link FactoryCreator} only.
     */
    <T> List<Reference<T>> getCachedProviders(final Class<T> category) {
        return null;
    }

    /**
     * Returns {@code true} is the specified {@code factory} meets the requirements specified by
     * a map of {@code hints} and the filter. This method is the entry point for the following
     * public methods:
     * <ul>
     *   <li>Singleton {@link #getServiceProvider (Class category, Filter, Hints, Hints.Key)}</li>
     *   <li>Iterator  {@link #getServiceProviders(Class category, Filter, Hints)}</li>
     * </ul>
     *
     * @param candidate The factory to checks.
     * @param category  The factory category. Usually an interface.
     * @param hints     The optional user requirements, or {@code null}.
     * @param filter    The optional filter, or {@code null}.
     * @return {@code true} if the {@code factory} meets the user requirements.
     */
    final <T> boolean isAcceptable(final T candidate, final Class<T> category,
                                   final Hints hints, final Filter filter)
    {
        if (filter!=null && !filter.filter(candidate)) {
            return false;
        }
        /*
         * Note: isAvailable(...) must be tested before checking the hints, because in current
         * Geotools implementation (especially DeferredAuthorityFactory), some hints computation
         * are deferred until a connection to the database is etablished (which 'isAvailable'
         * does in order to test the connection).
         */
        if (!isAvailable(candidate)) {
            return false;
        }
        if (hints != null) {
            if (candidate instanceof Factory) {
                if (!usesAcceptableHints((Factory) candidate, category, hints, (Set<Factory>) null)) {
                    return false;
                }
            }
        }
        /*
         * Checks for optional user conditions supplied in FactoryRegistry subclasses.
         */
        return isAcceptable(candidate, category, hints);
    }

    /**
     * Returns {@code true} is the specified {@code factory} meets the requirements specified
     * by a map of {@code hints}. This method checks only the hints; it doesn't check the
     * {@link Filter}, the {@linkplain OptionalFactory#isAvailable availability} or the
     * user-overrideable {@link #isAcceptable(Object, Class, Hints)} method. This method
     * invokes itself recursively.
     *
     * @param factory     The factory to checks.
     * @param category    The factory category. Usually an interface.
     * @param hints       The user requirements ({@code null} not allowed).
     * @param alreadyDone Should be {@code null} except on recursive calls (for internal use only).
     * @return {@code true} if the {@code factory} meets the hints requirements.
     */
    private boolean usesAcceptableHints(final Factory  factory,
                                        final Class<?> category,
                                        final Hints    hints,
                                        Set<Factory>   alreadyDone)
    {
        /*
         * Ask for implementation hints with special care against infinite recursivity.
         * Some implementations use deferred algorithms fetching dependencies only when
         * first needed. The call to getImplementationHints() is sometime a trigger for
         * fetching dependencies (in order to return accurate hints).   For example the
         * BufferedCoordinateOperationFactory implementation asks for an other instance
         * of CoordinateOperationFactory, the instance to cache behind a buffer,  which
         * should not be itself. Of course BufferedCoordinateOperation will checks that
         * it is not caching itself, but its test happen too late for preventing a never-
         * ending loop if we don't put a 'testingHints' guard here. It is also a safety
         * against broken factory implementations.
         */
        if (!testingHints.addAndCheck(factory)) {
            return false;
        }
        final Map<RenderingHints.Key, ?> implementationHints;
        try {
            implementationHints = Hints.stripNonKeys(factory.getImplementationHints());
        } finally {
            testingHints.removeAndCheck(factory);
        }
        if (implementationHints == null) {
            // factory was bad and did not meet contract - assume it used no Hints
            return true;
        }
        /*
         * We got the implementation hints. Now tests their compatibility.
         */
        Hints remaining = null;
        for (final Map.Entry<?,?> entry : implementationHints.entrySet()) {
            final Object    key   = entry.getKey();
            final Object    value = entry.getValue();
            final Object expected = hints.get(key);
            if (expected != null) {
                /*
                 * We have found a hint that matter. Check if the
                 * available factory meets the user's criterions.
                 */
                if (expected instanceof Class<?>) {
                    if (!((Class<?>) expected).isInstance(value)) {
                        return false;
                    }
                } else if (expected instanceof Class<?>[]) {
                    final Class<?>[] types = (Class<?>[]) expected;
                    int i = 0;
                    do if (i >= types.length) return false;
                    while (!types[i++].isInstance(value));
                } else if (!expected.equals(value)) {
                    return false;
                }
            }
            /*
             * Checks recursively in factory dependencies, if any. Note that the dependencies
             * will be checked against a subset of user's hints. More specifically, all hints
             * processed by the current pass will NOT be passed to the factories dependencies.
             * This is because the same hint may appears in the "parent" factory and a "child"
             * dependency with different value. For example the FORCE_LONGITUDE_FIRST_AXIS_ORDER
             * hint has the value TRUE in OrderedAxisAuthorityFactory, but the later is basically
             * a wrapper around the ThreadedEpsgFactory (typically), which has the value FALSE
             * for the same hint.
             *
             * Additional note: The 'alreadyDone' set is a safety against cyclic dependencies,
             * in order to protect ourself against never-ending loops. This is not the same
             * kind of dependencies than 'testingHints'. It is a "factory A depends on factory
             * B which depends on factory A" loop, which is legal.
             */
            if (value instanceof Factory) {
                final Factory dependency = (Factory) value;
                if (alreadyDone == null) {
                    alreadyDone = new HashSet<Factory>();
                }
                if (!alreadyDone.contains(dependency)) {
                    alreadyDone.add(factory);
                    if (remaining == null) {
                        remaining = new Hints(hints);
                        remaining.keySet().removeAll(implementationHints.keySet());
                    }
                    final Class<?> type;
                    if (key instanceof Hints.Key) {
                        type = ((Hints.Key) key).getValueClass();
                    } else {
                        type = Factory.class; // Kind of unknown factory type...
                    }
                    // Recursive call to this method for scanning dependencies.
                    if (!usesAcceptableHints(dependency, type, remaining, alreadyDone)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if the specified {@code provider} meets the requirements specified by
     * a map of {@code hints}. The default implementation always returns {@code true}. There is no
     * need to override this method for {@link AbstractFactory} implementations, since their hints
     * are automatically checked. Override this method for non-Geotools implementations.
     * For example a JTS geometry factory finder may overrides this method in order to check
     * if a {@link com.vividsolutions.jts.geom.GeometryFactory} uses the required
     * {@link com.vividsolutions.jts.geom.CoordinateSequenceFactory}. Such method should be
     * implemented as below, since this method may be invoked for various kind of objects:
     *
     * <blockquote><pre>
     * if (provider instanceof GeometryFactory) {
     *     // ... Check the GeometryFactory state here.
     * }
     * </pre></blockquote>
     *
     * @param <T>      The class represented by the {@code category} argument.
     * @param provider The provider to checks.
     * @param category The factory category. Usually an interface.
     * @param hints    The user requirements, or {@code null} if none.
     * @return {@code true} if the {@code provider} meets the user requirements.
     */
    protected <T> boolean isAcceptable(final T provider, final Class<T> category, final Hints hints) {
        return true;
    }

    /**
     * Returns {@code true} if the specified factory is available.
     */
    private boolean isAvailable(final Object provider) {
        if (!(provider instanceof OptionalFactory)) {
            return true;
        }
        final OptionalFactory factory = (OptionalFactory) provider;
        final Class<? extends OptionalFactory> type = factory.getClass();
        if (!testingAvailability.addAndCheck(type)) {
            throw new RecursiveSearchException(type);
        }
        try {
            return factory.isAvailable();
        } finally {
            testingAvailability.removeAndCheck(type);
        }
    }
    
    /**
     * Returns all class loaders to be used for scanning plugins. Current implementation
     * returns the following class loaders:
     * <p>
     * <ul>
     *   <li>{@linkplain Class#getClassLoader This object class loader}</li>
     *   <li>{@linkplain Thread#getContextClassLoader The thread context class loader}</li>
     *   <li>{@linkplain ClassLoader#getSystemClassLoader The system class loader}</li>
     * </ul>
     *
     * The actual number of class loaders may be smaller if redundancies was found.
     * If some more classloaders should be scanned, they shall be added into the code
     * of this method.
     *
     * @return All classloaders to be used for scanning plugins.
     */
    public final Set<ClassLoader> getClassLoaders() {
        final Set<ClassLoader> loaders = new HashSet<ClassLoader>();
        for (int i=0; i<4; i++) {
            final ClassLoader loader;
            try {
                switch (i) {
                    case 0:  loader = getClass().getClassLoader();                    break;
                    case 1:  loader = FactoryRegistry.class.getClassLoader();         break;
                    case 2:  loader = Thread.currentThread().getContextClassLoader(); break;
                    case 3:  loader = ClassLoader.getSystemClassLoader();             break;
                    // Add any supplementary class loaders here, if needed.
                    default: throw new AssertionError(i); // Should never happen.
                }
            } catch (SecurityException exception) {
                // We are not allowed to get a class loader.
                // Continue; some other class loader may be available.
                continue;
            }
            loaders.add(loader);
        }
        loaders.remove(null);
        loaders.addAll(GeoTools.getClassLoaders());

        /*
         * We now have a set of class loaders with duplicated object already removed
         * (e.g. system classloader == context classloader). However, we may still
         * have an other form of redundancie. A class loader may be the parent of an
         * other one. Try to remove those dependencies.
         */
        final ClassLoader[] asArray = loaders.toArray(new ClassLoader[loaders.size()]);
        for (int i=0; i<asArray.length; i++) {
            ClassLoader loader = asArray[i];
            try {
                while ((loader=loader.getParent()) != null) {
                    loaders.remove(loader);
                }
            } catch (SecurityException exception) {
                // We are not allowed to fetch the parent class loader.
                // Ignore (some redundancies may remains).
            }
        }
        if (loaders.isEmpty()) {
            LOGGER.warning("No class loaders available.");
        }
        return loaders;
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
    public void scanForPlugins() {
        final Set<ClassLoader> loaders = getClassLoaders();
        for (final Iterator<Class<?>> categories=getCategories(); categories.hasNext();) {
            final Class<?> category = categories.next();
            scanForPlugins(loaders, category);
        }
    }

    /**
     * Scans for factory plug-ins of the given category, with guard against recursivities.
     * The recursivity check make debugging easier than inspecting a {@link StackOverflowError}.
     *
     * @param loader The class loader to use.
     * @param category The category to scan for plug-ins.
     */
    private <T> void scanForPlugins(final Collection<ClassLoader> loaders, final Class<T> category) {
        if (!scanningCategories.addAndCheck(category)) {
            throw new RecursiveSearchException(category);
        }
        try {
            final StringBuilder message = getLogHeader(category);
            boolean newServices = false;
            /*
             * First, scan META-INF/services directories (the default mechanism).
             */
            for (final ClassLoader loader : loaders) {
                newServices |= register(ServiceLoader.load(category, loader).iterator(), category, message);
                newServices |= registerFromSystemProperty(loader, category, message);
            }
            /*
             * Next, query the user-provider iterators, if any.
             */
            final FactoryIteratorProvider[] fip = FactoryIteratorProviders.getIteratorProviders();
            for (int i=0; i<fip.length; i++) {
                final Iterator<T> it = fip[i].iterator(category);
                if (it != null) {
                    newServices |= register(it, category, message);
                }
            }
            /*
             * Finally, log the list of registered factories.
             */
            if (newServices) {
                log("scanForPlugins", message);
            }
        } finally {
            scanningCategories.removeAndCheck(category);
        }
    }

    /**
     * Scans the given category for plugins only if needed. After this method has been
     * invoked once for a given category, it will no longer scan for that category.
     */
    private <T> void scanForPluginsIfNeeded(final Class<?> category) {
        if (needScanForPlugins != null && needScanForPlugins.remove(category)) {
            if (needScanForPlugins.isEmpty()) {
                needScanForPlugins = null;
            }
            scanForPlugins(getClassLoaders(), category);
        }
    }

    /**
     * {@linkplain #registerServiceProvider Registers} all factories given by the
     * supplied iterator.
     *
     * @param factories The factories (or "service providers") to register.
     * @param category  the category under which to register the providers.
     * @param message   A buffer where to write the logging message.
     * @return {@code true} if at least one factory has been registered.
     */
    private <T> boolean register(final Iterator<T> factories, final Class<T> category,
                                 final StringBuilder message)
    {
        boolean newServices = false;
        final String lineSeparator = System.getProperty("line.separator", "\n");
        while (factories.hasNext()) {
            T factory;
            try {
                factory = factories.next();
            } catch (OutOfMemoryError error) {
                // Makes sure that we don't try to handle this error.
                throw error;
            } catch (NoClassDefFoundError error) {
                /*
                 * A provider can't be registered because of some missing dependencies.
                 * This occurs for example when trying to register the WarpTransform2D
                 * math transform on a machine without JAI installation. Since the factory
                 * may not be essential (this is the case of WarpTransform2D), just skip it.
                 */
                loadingFailure(category, error, false);
                continue;
            } catch (ExceptionInInitializerError error) {
                /*
                 * If an exception occured during class initialization, log the cause.
                 * The ExceptionInInitializerError alone doesn't help enough.
                 */
                final Throwable cause = error.getCause();
                if (cause != null) {
                    loadingFailure(category, cause, true);
                }
                throw error;
            } catch (Error error) {
                if (!Classes.getShortClassName(error).equals("ServiceConfigurationError")) {
                    // We want to handle sun.misc.ServiceConfigurationError only. Unfortunatly, we
                    // need to rely on reflection because this error class is not a commited API.
                    // TODO: Check if the error is catchable with JSE 6.
                    throw error;
                }
                /*
                 * Failed to register a factory for a reason probably related to the plugin
                 * initialisation. It may be some factory-dependent missing resources.
                 */
                loadingFailure(category, error, true);
                continue;
            }
            if (category.isAssignableFrom(factory.getClass())) {
                final Class<? extends T> factoryClass = factory.getClass().asSubclass(category);
                /*
                 * If the factory implements more than one interface and an
                 * instance were already registered, reuse the same instance
                 * instead of duplicating it.
                 */
                final T replacement = getServiceProviderByClass(factoryClass);
                if (replacement != null) {
                    factory = replacement;
                    // Need to register anyway, because the category may not be
                    // the same.
                }
                if (registerServiceProvider(factory, category)) {
                    /*
                     * The factory is now registered. Add it to the message to
                     * be logged. We will log all factories together in a single
                     * log event because some registration (e.g.
                     * MathTransformProviders) would be otherwise quite verbose.
                     */
                    message.append(lineSeparator);
                    message.append("  ");
                    message.append(factoryClass.getName());
                    newServices = true;
                }
            }
        }
        return newServices;
    }

    /**
     * Adds a service provider object to the registry.
     * The provider is associated with the given category.
     *
     * <p> If <code>provider</code> implements the <code>RegisterableService</code> interface, its
     * <code>onRegistration</code> method will be called. Its <code>onDeregistration</code> method
     * will be called each time it is deregistered from a category, for example if a category is
     * removed or the registry is garbage collected.
     *
     * @param provider the service provide object to be registered.
     * @param category the category under which to register the provider.
     *
     * @return true if no provider of the same class was previously registered in the same category
     *
     * @throws IllegalArgumentException if <code>provider</code> is <code>null</code> 
     *         or if there is no category corresponding to <code>category</code>.
     * @throws ClassCastException if provider does not implement the <code>Class</code> defined by
     *         <code>category</code>.
     */
    public <T> boolean registerServiceProvider(T provider, Class<T> category) {
        if (provider == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        SubRegistry reg = categoryMap.get(category);
        if (reg == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (!category.isAssignableFrom(provider.getClass())) {
            throw new ClassCastException();
        }

        return reg.registerServiceProvider(provider);
    }

    /**
     * Adds a service provider object to the registry.
     * The provider is associated within each category present in the registry whose
     * <code>Class</code> it implements.
     *
     * <p> If <code>provider</code> implements the <code>RegisterableService</code> interface, its
     * <code>onRegistration</code> method will be called once for each category it is registered
     * under. Its <code>onDeregistration</code> method will be called each time it is deregistered
     * from a category or when the registry is finalized.
     *
     * @param provider the service provider object to be registered.
     *
     * @throws IllegalArgumentException if <code>provider</code> is <code>null</code>.
     */
    public void registerServiceProvider(Object provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        Iterator<SubRegistry> regs = getSubRegistries(provider);
        while (regs.hasNext()) {
            regs.next().registerServiceProvider(provider);
        }
    }

    /**
     * Removes a service provider object from all categories that contain it.
     *
     * @param provider the service provider object to be deregistered.
     *
     * @throws IllegalArgumentException if <code>provider</code> is <code>null</code>.
     */
    public void deregisterServiceProvider(Object provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        Iterator<SubRegistry> regs = getSubRegistries(provider);
        while (regs.hasNext()) {
            regs.next().deregisterServiceProvider(provider);
        }
    }

    /**
     * If a system property was setup, load the class (if not already registered)
     * and move it in front of any other factory. This is done for compatibility
     * with legacy {@code FactoryFinder} implementation.
     *
     * @param loader   The class loader to use.
     * @param category The category to scan for plug-ins.
     * @param message  A buffer where to write the logging message.
     * @return {@code true} if at least one factory has been registered.
     */
    private <T> boolean registerFromSystemProperty(final ClassLoader loader,
            final Class<T> category, final StringBuilder message)
    {
        boolean newServices = false;
        try {
            final String classname = System.getProperty(category.getName());
            if (classname != null) try {
                final Class<?> candidate = loader.loadClass(classname);
                if (category.isAssignableFrom(candidate)) {
                    final Class<? extends T> factoryClass = candidate.asSubclass(category);
                    T factory = getServiceProviderByClass(factoryClass);
                    if (factory == null) try {
                        factory = factoryClass.newInstance();
                        if (registerServiceProvider(factory, category)) {
                            message.append(System.getProperty("line.separator", "\n"));
                            message.append("  ");
                            message.append(factoryClass.getName());
                            newServices = true;
                        }
                    } catch (IllegalAccessException exception) {
                        throw new FactoryRegistryException(Errors.format(
                                ErrorKeys.CANT_CREATE_FACTORY_$1, classname), exception);
                    } catch (InstantiationException exception) {
                        throw new FactoryRegistryException(Errors.format(
                                ErrorKeys.CANT_CREATE_FACTORY_$1, classname), exception);
                    }
                    /*
                     * Put this factory in front of every other factories (including the ones loaded
                     * in previous class loaders, which is why we don't inline this ordering in the
                     * 'register' loop). Note: if some factories were not yet registered, they will
                     * not be properly ordered. Since this code exists more for compatibility reasons
                     * than as a commited API, we ignore this short comming for now.
                     */
                    for (final Iterator<T> it=getServiceProviders(category, false); it.hasNext();) {
                        final T other = it.next();
                        if (other != factory) {
                            setOrdering(category, factory, other);
                        }
                    }
                }
            } catch (ClassNotFoundException exception) {
                // The class has not been found, maybe because we are not using the appropriate
                // class loader. Ignore (do not thrown an exception), in order to give a chance
                // to the caller to invokes this method again with a different class loader.
            }
        } catch (SecurityException exception) {
            // We are not allowed to read property, probably
            // because we are running in an applet. Ignore...
        }
        return newServices;
    }

    /**
     * Invoked when a factory can't be loaded. Log a warning, but do not stop the process.
     */
    private static void loadingFailure(final Class<?> category, final Throwable error,
                                       final boolean showStackTrace)
    {
        final String         name = Classes.getShortName(category);
        final StringBuilder cause = new StringBuilder(Classes.getShortClassName(error));
        final String      message = error.getLocalizedMessage();
        if (message != null) {
            cause.append(": ");
            cause.append(message);
        }
        final LogRecord record = Loggings.format(Level.WARNING,
                LoggingKeys.CANT_LOAD_SERVICE_$2, name, cause.toString());
        if (showStackTrace) {
            record.setThrown(error);
        }
        record.setSourceClassName(FactoryRegistry.class.getName());
        record.setSourceMethodName("scanForPlugins");
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Prepares a message to be logged if any provider has been registered.
     */
    private static StringBuilder getLogHeader(final Class<?> category) {
        return new StringBuilder(Loggings.getResources(null).getString(
                LoggingKeys.FACTORY_IMPLEMENTATIONS_$1, category));
    }

    /**
     * Log the specified message after all provider for a given category have been registered.
     */
    private static void log(final String method, final StringBuilder message) {
        final LogRecord record = new LogRecord(Level.CONFIG, message.toString());
        record.setSourceClassName(FactoryRegistry.class.getName());
        record.setSourceMethodName(method);
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Synchronizes the content of the {@link #globalConfiguration} with
     * {@link FactoryIteratorProviders#GLOBAL}. New providers are {@linkplain #register registered}
     * immediately. Note that this method is typically invoked in a different thread than
     * {@link FactoryIteratorProviders} method calls.
     *
     * @see FactoryIteratorProviders#addFactoryIteratorProvider
     */
    private void synchronizeIteratorProviders() {
        final FactoryIteratorProvider[] newProviders =
                globalConfiguration.synchronizeIteratorProviders();
        if (newProviders == null) {
            return;
        }
        for (final Iterator<Class<?>> categories=getCategories(); categories.hasNext();) {
            final Class<?> category = categories.next();
            if (needScanForPlugins == null || !needScanForPlugins.contains(category)) {
                /*
                 * Register immediately the factories only if some other factories were already
                 * registered for this category,  because in such case scanForPlugin() will not
                 * be invoked automatically. If no factory are registered for this category, do
                 * nothing - we will rely on the lazy invocation of scanForPlugins() when first
                 * needed. We perform this check because getServiceProviders(category).hasNext()
                 * is the criterion used by FactoryRegistry in order to decide if it should invoke
                 * automatically scanForPlugins().
                 */
                for (int i=0; i<newProviders.length; i++) {
                    register(newProviders[i], category);
                }
            }
        }
    }

    /**
     * Registers every factories from the specified provider for the given category.
     */
    private <T> void register(final FactoryIteratorProvider provider, final Class<T> category) {
        final Iterator<T> it = provider.iterator(category);
        if (it != null) {
            final StringBuilder message = getLogHeader(category);
            if (register(it, category, message)) {
                log("synchronizeIteratorProviders", message);
            }
        }
    }

    /**
     * Set pairwise ordering between all factories according a comparator. Calls to
     * <code>{@linkplain Comparator#compare compare}(factory1, factory2)</code> should returns:
     * <ul>
     *   <li>{@code -1} if {@code factory1} is preferred to {@code factory2}</li>
     *   <li>{@code +1} if {@code factory2} is preferred to {@code factory1}</li>
     *   <li>{@code 0} if there is no preferred order between {@code factory1} and
     *       {@code factory2}</li>
     * </ul>
     *
     * @param  <T>        The class represented by the {@code category} argument.
     * @param  category   The category to set ordering.
     * @param  comparator The comparator to use for ordering.
     * @return {@code true} if at least one ordering setting has been modified as a consequence
     *         of this call.
     */
    public <T> boolean setOrdering(final Class<T> category, final Comparator<T> comparator) {
        boolean set = false;
        final List<T> previous = new ArrayList<T>();
        for (final Iterator<T> it=getServiceProviders(category, false); it.hasNext();) {
            final T f1 = it.next();
            for (int i=previous.size(); --i>=0;) {
                final T f2 = previous.get(i);
                final int c;
                try {
                    c = comparator.compare(f1, f2);
                } catch (ClassCastException exception) {
                    /*
                     * This exception is expected if the user-supplied comparator follows strictly
                     * the java.util.Comparator specification and has determined that it can't
                     * compare the supplied factories. From FactoryRegistry point of view, it just
                     * means that the ordering between those factories will stay undeterminated.
                     */
                    continue;
                }
                if (c > 0) {
                    set |= setOrdering(category, f1, f2);
                } else if (c < 0) {
                    set |= setOrdering(category, f2, f1);
                }
            }
            previous.add(f1);
        }
        return set;
    }

    /**
     * Sets or unsets a pairwise ordering between all factories meeting a criterion. For example
     * in the CRS framework ({@link org.geotools.referencing.FactoryFinder}), this is used for
     * setting ordering between all factories provided by two vendors, or for two authorities.
     * If one or both factories are not currently registered, or if the desired ordering is
     * already set/unset, nothing happens and false is returned.
     *
     * @param <T>      The class represented by the {@code base} argument.
     * @param base     The base category. Only categories {@linkplain Class#isAssignableFrom
     *                 assignable} to {@code base} will be processed.
     * @param set      {@code true} for setting the ordering, or {@code false} for unsetting.
     * @param service1 Filter for the preferred factory.
     * @param service2 Filter for the factory to which {@code service1} is preferred.
     * @return {@code true} if the ordering changed as a result of this call.
     */
    public <T> boolean setOrdering(final Class<T> base, final boolean set,
                                   final Filter service1, final Filter service2)
    {
        boolean done = false;
        for (final Iterator<Class<?>> categories=getCategories(); categories.hasNext();) {
            final Class<?> candidate = categories.next();
            if (base.isAssignableFrom(candidate)) {
                final Class<? extends T> category = candidate.asSubclass(base);
                done |= setOrUnsetOrdering(category, set, service1, service2);
            }
        }
        return done;
    }

    /**
     * Sets a pairwise ordering between two service provider objects
     * within a given category.  If one or both objects are not
     * currently registered within the given category, or if the
     * desired ordering is already set, nothing happens and
     * <code>false</code> is returned.  If the providers previously
     * were ordered in the reverse direction, that ordering is
     * removed.
     *
     * <p> The ordering will be used by the
     * <code>getServiceProviders</code> methods when their
     * <code>useOrdering</code> argument is <code>true</code>.
     *
     * @param category a <code>Class</code> object indicating the
     * category under which the preference is to be established.
     * @param firstProvider the preferred provider.
     * @param secondProvider the provider to which
     * <code>firstProvider</code> is preferred.
     *
     * @return <code>true</code> if a previously unset ordering
     * was established.
     *
     * @exception IllegalArgumentException if either provider is
     * <code>null</code> or they are the same object.
     * @exception IllegalArgumentException if there is no category
     * corresponding to <code>category</code>.
     */
    public <T> boolean setOrdering(Class<T> category,
                                   T firstProvider,
                                   T secondProvider) {
        if (firstProvider == null || secondProvider == null) {
            throw new IllegalArgumentException("provider is null!");
        }
        if (firstProvider == secondProvider) {
            throw new IllegalArgumentException("providers are the same!");
        }
        SubRegistry reg = categoryMap.get(category);
        if (reg == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (reg.contains(firstProvider) &&
            reg.contains(secondProvider)) {
            return reg.setOrdering(firstProvider, secondProvider);
        }
        return false;
    }

    /**
     * Sets a pairwise ordering between two service provider objects
     * within a given category.  If one or both objects are not
     * currently registered within the given category, or if no
     * ordering is currently set between them, nothing happens
     * and <code>false</code> is returned.
     *
     * <p> The ordering will be used by the
     * <code>getServiceProviders</code> methods when their
     * <code>useOrdering</code> argument is <code>true</code>.
     *
     * @param category a <code>Class</code> object indicating the
     * category under which the preference is to be disestablished.
     * @param firstProvider the formerly preferred provider.
     * @param secondProvider the provider to which
     * <code>firstProvider</code> was formerly preferred.
     *
     * @return <code>true</code> if a previously set ordering was
     * disestablished.
     *
     * @exception IllegalArgumentException if either provider is
     * <code>null</code> or they are the same object.
     * @exception IllegalArgumentException if there is no category
     * corresponding to <code>category</code>.
     */
    public <T> boolean unsetOrdering(Class<T> category,
                                     T firstProvider,
                                     T secondProvider) {
        if (firstProvider == null || secondProvider == null) {
            throw new IllegalArgumentException("provider is null!");
        }
        if (firstProvider == secondProvider) {
            throw new IllegalArgumentException("providers are the same!");
        }
        SubRegistry reg = categoryMap.get(category);
        if (reg == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (reg.contains(firstProvider) &&
            reg.contains(secondProvider)) {
            return reg.unsetOrdering(firstProvider, secondProvider);
        }
        return false;
    }

    /**
     * Helper method for the above.
     */
    private <T> boolean setOrUnsetOrdering(final Class<T> category, final boolean set,
                                           final Filter service1, final Filter service2)
    {
        boolean done = false;
        T impl1 = null;
        T impl2 = null;
        for (final Iterator<? extends T> it=getServiceProviders(category, false); it.hasNext();) {
            final T factory = it.next();
            if (service1.filter(factory)) impl1 = factory;
            if (service2.filter(factory)) impl2 = factory;
            if (impl1!=null && impl2!=null && impl1!=impl2) {
                if (set) done |=   setOrdering(category, impl1, impl2);
                else     done |= unsetOrdering(category, impl1, impl2);
            }
        }
        return done;
    }

    /**
     * Returns an <code>Iterator</code> of <code>Class</code> objects indicating the current
     * set of categories. The iterator will be empty if no categories exist.
     *
     * @return an <code>Iterator</code> containing <code>Class</code>objects.
     */
    public Iterator<Class<?>> getCategories() {
        return categoryMap.keySet().iterator();
    }

    /**
     * Returns an Iterator containing the subregistries to which the provider belongs.
     */
    private Iterator<SubRegistry> getSubRegistries(Object provider) {
        List<SubRegistry> l = new ArrayList<>();
        Iterator<Class<?>> iter = categoryMap.keySet().iterator();
        while (iter.hasNext()) {
            Class<?> c = iter.next();
            if (c.isAssignableFrom(provider.getClass())) {
                l.add(categoryMap.get(c));
            }
        }
        return l.iterator();
    }

    /**
     * Deregisters all currently registered service providers from all categories.
     */
    public void deregisterAll() {
        Iterator<SubRegistry> iter = categoryMap.values().iterator();
        while (iter.hasNext()) {
            ((SubRegistry)iter.next()).clear();
        }
    }
}
