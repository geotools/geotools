/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

import org.geotools.util.logging.Logging;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A {@linkplain FactoryRegistry factory registry} capable to creates factories if no appropriate
 * instance was found in the registry.
 * <p>
 * This class maintains a cache of previously created factories, as {@linkplain WeakReference
 * weak references}. Calls to {@link #getServiceProvider getServiceProvider} first check if a
 * previously created factory can fit.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Jody Garnett
 */
public class FactoryCreator extends FactoryRegistry {
    /**
     * The array of classes for searching the one-argument constructor.
     */
    private static final Class[] HINTS_ARGUMENT = new Class[] {Hints.class};

    /**
     * List of factories already created. Used as a cache.
     */
    private final Map<Class<?>, List<Reference<?>>> cache = new HashMap<Class<?>, List<Reference<?>>>();

    /**
     * Objects under construction for each implementation class.
     * Used by {@link #safeCreate} as a guard against infinite recursivity.
     */
    private final Set<Class<?>> underConstruction = new HashSet<Class<?>>();

    /**
     * Constructs a new registry for the specified category.
     *
     * @param category The single category.
     *
     * @since 2.4
     */
    public FactoryCreator(final Class<?> category) {
        super(category);
    }

    /**
     * Constructs a new registry for the specified categories.
     *
     * @param categories The categories.
     *
     * @since 2.4
     */
    public FactoryCreator(final Class<?>[] categories) {
        super(categories);
    }

    /**
     * Constructs a new registry for the specified categories.
     *
     * @param categories The categories.
     */
    public FactoryCreator(final Collection<Class<?>> categories) {
        super(categories);
    }

    /**
     * Returns the providers available in the cache. To be used by {@link FactoryRegistry}.
     */
    @Override
    final <T> List<Reference<T>> getCachedProviders(final Class<T> category) {
        List<Reference<?>> c = cache.get(category);
        if (c == null) {
            c = new LinkedList<Reference<?>>();
            cache.put(category, c);
        }
        @SuppressWarnings("unchecked")
        final List<Reference<T>> cheat = (List) c;
        /*
         * Should be safe because we created an empty list, there is no other place where this
         * list is created and from this point we can only insert elements restricted to type T.
         */
        return cheat;
    }

    /**
     * Caches the specified factory under the specified category.
     */
    private <T> void cache(final Class<T> category, final T factory) {
        getCachedProviders(category).add(new WeakReference<T>(factory));
    }

    /**
     * Returns a provider for the specified category, using the specified map of hints (if any).
     * If a provider matching the requirements is found in the registry, it is returned. Otherwise,
     * a new provider is created and returned. This creation step is the only difference between
     * this method and the {@linkplain FactoryRegistry#getServiceProvider super-class method}.
     *
     * @param  category The category to look for.
     * @param  filter   An optional filter, or {@code null} if none.
     * @param  hints    A {@linkplain Hints map of hints}, or {@code null} if none.
     * @param  key      The key to use for looking for a user-provided instance in the hints, or
     *                  {@code null} if none.
     * @return A factory for the specified category and hints (never {@code null}).
     * @throws FactoryNotFoundException if no factory was found, and the specified hints don't
     *         provide suffisient information for creating a new factory.
     * @throws FactoryRegistryException if the factory can't be created for some other reason.
     */
    @Override
    public <T> T getServiceProvider(final Class<T>  category,
                                    final Filter    filter,
                                    final Hints     hints,
                                    final Hints.Key key)
            throws FactoryRegistryException
    {
        final FactoryNotFoundException notFound;
        try {
            return super.getServiceProvider(category, filter, hints, key);
        } catch (FactoryNotFoundException exception) {
            // Will be rethrown later in case of failure to create the factory.
            notFound = exception;
        }
        /*
         * No existing factory found. Creates one using reflection. First, we
         * check if an implementation class was explicitly specified by the user.
         */
        final Class<?>[] types;
        if (hints==null || key==null) {
            types = null;
        } else {
            final Object hint = hints.get(key);
            if (hint == null) {
                types = null;
            } else {
                if (hint instanceof Class<?>[]) {
                    types = (Class<?>[]) hint;
                } else {
                    types = new Class<?>[] {(Class<?>) hint};
                    // Should not fails, since non-class argument should
                    // have been accepted by 'getServiceProvider(...)'.
                }
                final int length = types.length;
                for (int i=0; i<length; i++) {
                    final Class<?> type = types[i];
                    if (type!=null && category.isAssignableFrom(type)) {
                        final int modifiers = type.getModifiers();
                        if (!Modifier.isAbstract(modifiers)) {
                            final T candidate = createSafe(category, type, hints);
                            if (candidate == null) {
                                continue;
                            }
                            if (isAcceptable(candidate, category, hints, filter)) {
                                cache(category, candidate);
                                return candidate;
                            }
                            dispose(candidate);
                        }
                    }
                }
            }
        }
        /*
         * No implementation hint provided. Search the first implementation
         * accepting a Hints argument. No-args constructor will be ignored.
         * Note: all Factory objects should be fully constructed by now,
         * since the super-class has already iterated over all factories.
         */
        for (final Iterator<T> it=getUnfilteredProviders(category); it.hasNext();) {
            final T factory = it.next();
            final Class<?> implementation = factory.getClass();
            if (types!=null && !isTypeOf(types, implementation)) {
                continue;
            }
            if (filter!=null && !filter.filter(factory)) {
                continue;
            }
            final T candidate;
            try {
                candidate = createSafe(category, implementation, hints);
            } catch (FactoryNotFoundException exception) {
                // The factory has a dependency which has not been found.
                // Be tolerant to that kind of error.
                Logging.recoverableException(LOGGER, FactoryCreator.class, "getServiceProvider", exception);
                continue;
            } catch (FactoryRegistryException exception) {
                if (exception.getCause() instanceof NoSuchMethodException) {
                    // No public constructor with the expected argument.
                    // Try an other implementation.
                    continue;
                } else {
                    // Other kind of error, probably unexpected.
                    // Let the exception propagates.
                    throw exception;
                }
            }
            if (candidate == null) {
                continue;
            }
            if (isAcceptable(candidate, category, hints, filter)) {
                cache(category, candidate);
                return candidate;
            }
            dispose(candidate);
        }
        throw notFound;
    }

    /**
     * Returns {@code true} if the specified implementation is one of the specified types.
     */
    private static boolean isTypeOf(final Class<?>[] types, final Class<?> implementation) {
        for (int i=0; i<types.length; i++) {
            if (types[i].isAssignableFrom(implementation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Invokes {@link #createServiceProvider}, but checks against recursive calls. If the specified
     * implementation is already under construction, returns {@code null} in order to tell to
     * {@link #getServiceProvider} that it need to search for an other implementation. This is
     * needed for avoiding infinite recursivity when a factory is a wrapper around an ther factory
     * of the same category. For example this is the case of
     * {@link org.geotools.referencing.operation.BufferedCoordinateOperationFactory}.
     */
    private <T> T createSafe(final Class<T> category, final Class<?> implementation, final Hints hints) {
        if (!underConstruction.add(implementation)) {
            return null;
        }
        try {
            return createServiceProvider(category, implementation, hints);
        } finally {
            if (!underConstruction.remove(implementation)) {
                throw new AssertionError();
            }
        }
    }

    /**
     * Creates a new instance of the specified factory using the specified hints.
     * The default implementation tries to instantiate the given implementation class
     * using the first of the following constructor found:
     * <p>
     * <ul>
     *   <li>Constructor with a single {@link Hints} argument.</li>
     *   <li>No-argument constructor.</li>
     * </ul>
     *
     * @param  category The category to instantiate.
     * @param  implementation The factory class to instantiate.
     * @param  hints The implementation hints.
     * @return The factory.
     * @throws FactoryRegistryException if the factory creation failed.
     */
    protected <T> T createServiceProvider(final Class<T> category,
                                          final Class<?> implementation,
                                          final Hints hints)
            throws FactoryRegistryException
    {
        Throwable cause;
        try {
            try {
                return category.cast(implementation.getConstructor(HINTS_ARGUMENT)
                        .newInstance(new Object[] {hints}));
            } catch (NoSuchMethodException exception) {
                // Constructor do not exists or is not public. We will fallback on the no-arg one.
                cause = exception;
            }
            try {
                return category.cast(implementation.getConstructor((Class[]) null)
                        .newInstance((Object[]) null));
            } catch (NoSuchMethodException exception) {
                // No constructor accessible. Do not store the cause (we keep the one above).
            }
        } catch (IllegalAccessException exception) {
            cause = exception; // constructor is not public (should not happen)
        } catch (InstantiationException exception) {
            cause = exception; // The class is abstract
        } catch (InvocationTargetException exception) {
            cause = exception.getCause(); // Exception in constructor
            if (cause instanceof FactoryRegistryException) {
                throw (FactoryRegistryException) cause;
            }
        }
        throw new FactoryRegistryException(Errors.format(
                ErrorKeys.CANT_CREATE_FACTORY_$1, implementation), cause);
    }

    /**
     * Dispose the specified factory after. This method is invoked when a factory has been
     * created, and then {@code FactoryCreator} determined that the factory doesn't meet
     * user's requirements.
     */
    private static void dispose(final Object factory) {
        // Empty for now. This method is merely a reminder for disposal in future Geotools versions.
    }
}
