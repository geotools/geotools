/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.factory;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.geotools.util.Utilities.ensureArgumentNonNull;
import static org.geotools.util.Utilities.stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.geotools.util.PartiallyOrderedSet;
import org.geotools.util.logging.Logging;

/**
 * The category registry holds multiple instances per category. Categories are {@link Class classes} and instances are
 * also accessible by the class they implement. Note that instances have to implement/extend the category they are filed
 * under.
 *
 * <p>This class is not thread-safe and {@code null}-intolerant (throws an {@link IllegalArgumentException} if an
 * argument is {@code null}).
 */
class CategoryRegistry {

    /**
     * Heterogeneous map, where `Class<T>` is mapped to `InstanceRegistry<T>` for any `T`.
     *
     * <p>Code depends on this map never being mutated to avoid concurrent modification exceptions.
     */
    private final Map<Class<?>, InstanceRegistry<?>> categories;

    /**
     * Creates a new registry with the specified Registers the specified category. If the same category is registered
     * multiple times, all instances previously registered for that category are lost.
     *
     * @param factoryRegistry The {@link FactoryRegistry} this registry belongs to.
     * @param categories The categories to register; must not be {@code null} but can contain {@code null}.
     */
    public CategoryRegistry(final FactoryRegistry factoryRegistry, final Iterable<Class<?>> categories) {
        ensureArgumentNonNull("factoryRegistry", factoryRegistry);
        ensureArgumentNonNull("categories", categories);
        // use an unmodifiable map to guarantee immutability
        this.categories = stream(categories)
                .collect(collectingAndThen(
                        toMap(
                                category -> category,
                                category -> new InstanceRegistry<>(factoryRegistry, category),
                                (firstRegistry, secondRegistry) -> secondRegistry),
                        Collections::unmodifiableMap));
    }

    /**
     * Registers the specified instance under all categories that are supertypes of the instance.
     *
     * @param instance The instance to register.
     */
    @SuppressWarnings("unchecked")
    public <T> void registerInstance(final T instance) {
        ensureArgumentNonNull("instance", instance);
        streamCategories()
                .filter(category -> category.isAssignableFrom(instance.getClass()))
                // the cast is correct because the filter above only leaves categories that are
                // supertypes of `instance`
                .map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
                .forEach(registry -> registry.register(instance));
    }

    /**
     * Registers the specified instance under the specified category. The category itself must already be registered.
     *
     * @param instance The instance to register.
     * @param category The category to register the instance under.
     * @return {@code true} if this the first instance of its class.
     */
    public <T> boolean registerInstance(final T instance, final Class<T> category) {
        return instanceRegistry(category).register(instance);
    }

    /**
     * Deregisters all instances with the same type as the specified one from all categories.
     *
     * @param instance The instance to deregister.
     */
    @SuppressWarnings("unchecked")
    public <T> void deregisterInstance(final T instance) {
        ensureArgumentNonNull("instance", instance);
        streamCategories()
                .filter(category -> category.isAssignableFrom(instance.getClass()))
                // the cast is correct because the filter above only leaves categories that are
                // supertypes of `instance`
                .map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
                .forEach(registry -> registry.deregister(instance));
    }

    /**
     * Deregisters all instances with the same type as the specified one from the specified category. The category must
     * be registered. If instances of the same type are registered under other categories they remain available under
     * them.
     *
     * @param instance The instance to deregister.
     * @param category The category, from which the instance should be removed.
     * @return {true} if an instance of the same type was previously registered
     */
    public <T> boolean deregisterInstance(final T instance, final Class<T> category) {
        return instanceRegistry(category).deregister(instance);
    }

    /**
     * Deregisters all instances registered under the specified category. The category itself remain registered.
     *
     * @param category The category from which to deregister instances.
     */
    public void deregisterInstances(Class<?> category) {
        instanceRegistry(category).clear();
    }

    /** Deregisters all instances. The categories themselves remain registered. */
    public void deregisterInstances() {
        categories.values().forEach(InstanceRegistry::clear);
    }

    /**
     * Finds the {@link InstanceRegistry} for the specified category. Throws an exception if that category was not
     * registered.
     */
    private <T> InstanceRegistry<T> instanceRegistry(final Class<T> category) {
        ensureArgumentNonNull("category", category);
        @SuppressWarnings("unchecked")
        // during construction, we registered `InstanceRegistry<T>` for `Class<T>`, so this cast is
        // save
        InstanceRegistry<T> registry = (InstanceRegistry<T>) categories.get(category);
        if (registry == null) {
            throw new IllegalArgumentException("The category '" + category + "' is not registered");
        }
        return registry;
    }

    /** @return all registered categories */
    public Stream<Class<?>> streamCategories() {
        return categories.keySet().stream();
    }

    /**
     * Returns all instances that were registered with the specified category
     *
     * @param category The category for which instances are.
     * @param useOrder whether to return instances in topological order as specified by {@link #setOrder}
     * @return The instances registered for the specified category.
     */
    public <T> Stream<T> streamInstances(final Class<T> category, final boolean useOrder) {
        return instanceRegistry(category).stream(useOrder);
    }

    /**
     * Returns an arbitrary instance that extends/implements the specified type that is filed under a category that also
     * extends/implements that type.
     *
     * @param type The type to look up.
     * @return An instance if one was found.
     */
    public <S> Optional<S> getInstanceOfType(Class<S> type) {
        ensureArgumentNonNull("type", type);
        return streamCategories()
                .filter(category -> category.isAssignableFrom(type))
                .map(this::instanceRegistry)
                .flatMap(registry -> stream(registry.getInstanceOfType(type)))
                .findFirst();
    }

    /**
     * Orders the specified instances, so that the first appears before the second when {@link #streamInstances(Class,
     * boolean) iterateInstances} is called with {@code useOrder = true}.
     *
     * @param category The category to order instances for.
     * @return {@code true} if this establishes a new order
     */
    public <T> boolean setOrder(Class<T> category, T firstInstance, T secondInstance) {
        ensureArgumentNonNull("firstInstance", firstInstance);
        ensureArgumentNonNull("secondInstance", secondInstance);
        return instanceRegistry(category).setOrder(firstInstance, secondInstance);
    }

    /**
     * Removes the ordering between the specified instances, so that the first no longer appears before the second when
     * {@link #streamInstances(Class, boolean) iterateInstances} is called with {@code useOrder = true}.
     *
     * @param category The category to clear instance order for.
     * @return {@code true} if that ordering existed before
     */
    public <T> boolean clearOrder(Class<T> category, T firstInstance, T secondInstance) {
        ensureArgumentNonNull("firstInstance", firstInstance);
        ensureArgumentNonNull("secondInstance", secondInstance);
        return instanceRegistry(category).clearOrder(firstInstance, secondInstance);
    }

    @Override
    public String toString() {
        return "CategoryRegistry [categories=" + categories.keySet() + "]";
    }

    /**
     * Registry tracking instances of a single category.
     *
     * @param <T> category
     */
    private static class InstanceRegistry<T> {
        /** The logger for all events related to factory registry. */
        protected static final Logger LOGGER = Logging.getLogger(CategoryRegistry.class);

        private static Class<?> REGISTERABLE_SERVICE = null;

        static {
            try {
                REGISTERABLE_SERVICE = Class.forName("javax.imageio.spi.RegisterableService");
            } catch (ClassNotFoundException ignore) {
                // RegisterableService not available in all Java versions
            }
        }

        private final FactoryRegistry factoryRegistry;

        private final Class<?> category;

        private final Map<Class<?>, T> instancesByType = new HashMap<>();

        private final PartiallyOrderedSet<T> orderedInstances = new PartiallyOrderedSet<>(false);

        private InstanceRegistry(FactoryRegistry factoryRegistry, Class<?> category) {
            this.factoryRegistry = factoryRegistry;
            this.category = category;
        }

        /** @return {@code true} if this the first instance of its class. */
        public boolean register(final T instance) {
            ensureArgumentNonNull("instance", instance);
            boolean deregistered = deregisterByType(instance);
            registerInternal(instance);
            notifyRegistered(instance);
            return !deregistered;
        }

        private void notifyRegistered(final T instance) {
            if (instance instanceof RegistrableFactory) {
                ((RegistrableFactory) instance).onRegistration(factoryRegistry, category);
            }
            if (REGISTERABLE_SERVICE != null && REGISTERABLE_SERVICE.isInstance(instance)) {
                LOGGER.warning("Migrate instances from RegisterableService to RegistrableFactory: " + instance);
            }
        }

        private void registerInternal(final T instance) {
            instancesByType.put(instance.getClass(), instance);
            orderedInstances.add(instance);
        }

        /** @return {true} if an instance of the same type was previously registered */
        public boolean deregister(final T instance) {
            ensureArgumentNonNull("instance", instance);
            if (instancesByType.containsKey(instance.getClass())) {
                deregisterByType(instance);
                return true;
            } else {
                return false;
            }
        }

        /** @return {true} if an instance of the same type was previously registered */
        private boolean deregisterByType(final T instance) {
            T removed = instancesByType.remove(instance.getClass());
            boolean instanceWasRemoved = removed != null;

            if (instanceWasRemoved) {
                orderedInstances.remove(removed);
                notifyDeregistered(removed);
            }

            return instanceWasRemoved;
        }

        private void notifyDeregistered(final T instance) {
            if (instance instanceof RegistrableFactory) {
                ((RegistrableFactory) instance).onDeregistration(factoryRegistry, category);
            }
        }

        public void clear() {
            Iterator<T> values = instancesByType.values().iterator();
            while (values.hasNext()) {
                T instance = values.next();
                values.remove();
                orderedInstances.remove(instance);
                notifyDeregistered(instance);
            }
        }

        public Stream<T> stream(final boolean useOrder) {
            if (useOrder) {
                return orderedInstances.stream();
            } else {
                return instancesByType.values().stream();
            }
        }

        public <S> Optional<S> getInstanceOfType(Class<S> type) {
            ensureArgumentNonNull("type", type);
            @SuppressWarnings("unchecked")
            S instance = (S) instancesByType.get(type);
            return Optional.ofNullable(instance);
        }

        /** @see CategoryRegistry#setOrder(Class, Object, Object) */
        public boolean setOrder(T firstInstance, T secondInstance) {
            return instancesByType.containsKey(firstInstance.getClass())
                    && instancesByType.containsKey(secondInstance.getClass())
                    // if both are contained, set the order
                    && orderedInstances.setOrder(firstInstance, secondInstance);
        }

        /** @see CategoryRegistry#clearOrder(Class, Object, Object) */
        public boolean clearOrder(T firstInstance, T secondInstance) {
            ensureArgumentNonNull("firstInstance", firstInstance);
            ensureArgumentNonNull("secondInstance", secondInstance);
            return instancesByType.containsKey(firstInstance.getClass())
                    && instancesByType.containsKey(secondInstance.getClass())
                    // if both are contained, set the order
                    && orderedInstances.clearOrder(firstInstance, secondInstance);
        }

        @Override
        public String toString() {
            return "InstanceRegistry [category=" + category.getSimpleName() + "]";
        }
    }
}
