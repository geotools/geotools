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
package org.geotools.factory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.geotools.util.PartiallyOrderedSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.geotools.util.Utilities.stream;

/**
 * The category registry holds multiple instances per category. Categories are
 * {@link Class classes} and instances are also accessible by the class they implement.
 * Note that instances have to implement/extend the category they are filed under.
 */
class CategoryRegistry {

	/**
	 * Heterogeneous map, where `Class<T>` is mapped to `InstanceRegistry<T>` for any `T`.
	 *
	 * Code depends on this map being immutable to avoid concurrent modification exceptions.
	 */
	private final ImmutableMap<Class<?>, InstanceRegistry<?>> categories;

	/**
	 * Creates a new registry with the specified Registers the specified category.
	 * If the same category is registered multiple times, all instances previously
	 * registered for that category are lost.
	 *
	 * @param factoryRegistry The {@link FactoryRegistry} this registry belongs to.
	 * @param categories The categories to register; must not be {@code null} but can contain {@code null}.
	 */
	public CategoryRegistry(final FactoryRegistry factoryRegistry, final Iterable<Class<?>> categories) {
		requireNonNull(factoryRegistry);
		requireNonNull(categories);
		this.categories = createCategoriesMap(factoryRegistry, categories);
	}

	private static ImmutableMap<Class<?>, InstanceRegistry<?>> createCategoriesMap(
			final FactoryRegistry factoryRegistry, final Iterable<Class<?>> categories) {
		Builder<Class<?>, InstanceRegistry<?>> categoriesBuilder = ImmutableMap.builder();
		categories.forEach(category ->
				categoriesBuilder.put(category, new InstanceRegistry<>(factoryRegistry, category)));
		return categoriesBuilder.build();
	}

	/**
	 * Registers the specified instance under all categories that are supertypes of the instance.
	 *
	 * @param instance The instance to register.
	 */
	@SuppressWarnings("unchecked")
	public <T> void registerInstance(final T instance) {
		requireNonNull(instance);
		streamCategories()
				.filter(category -> category.isAssignableFrom(instance.getClass()))
				// the cast is correct because the filter above only leaves categories that are supertypes of `instance`
				.map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
				.forEach(registry -> registry.register(instance));
	}

	/**
	 * Registers the specified instance under the specified category.
	 * The category itself must already be registered.
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
		requireNonNull(instance);
		streamCategories()
				.filter(category -> category.isAssignableFrom(instance.getClass()))
				// the cast is correct because the filter above only leaves categories that are supertypes of `instance`
				.map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
				.forEach(registry -> registry.deregister(instance));
	}

	/**
	 * Deregisters all instances with the same type as the specified one from the specified category.
	 * The category must be registered.
	 * If instances of the same type are registered under other categories they remain available under them.
	 *
	 * @param instance The instance to deregister.
	 * @param category The category, from which the instance should be removed.
	 * @return {true} if an instance of the same type was previously registered
	 */
	public <T> boolean deregisterInstance(final T instance, final Class<T> category) {
		return instanceRegistry(category).deregister(instance);
	}

	/**
	 * Deregisters all instances registered under the specified category.
	 * The category itself remain registered.
	 *
	 * @param category The category from which to deregister instances.
	 */
	public void deregisterInstances(Class<?> category) {
		instanceRegistry(category).clear();
	}

	/**
	 * Deregisters all instances. The categories themselves remain registered.
	 */
	public void deregisterInstances() {
		categories.values().forEach(InstanceRegistry::clear);
	}

	/**
	 * Finds the {@link InstanceRegistry} for the specified category.
	 * Throws an exception if that category was not registered.
	 */
	private <T> InstanceRegistry<T> instanceRegistry(final Class<T> category) {
		requireNonNull(category);
		@SuppressWarnings("unchecked")
		// during construction, we registered `InstanceRegistry<T>` for `Class<T>`, so this cast is save
		InstanceRegistry<T> registry = (InstanceRegistry<T>) categories.get(category);
		if (registry == null) {
			// TODO: do something fancy like in FactoryRegistry#getFactory(Class, Predicate, Hints, Hints.Key) ?
			throw new IllegalArgumentException("The category '" + category + "' is not registered");
		}
		return registry;
	}

	/**
	 * @return all registered categories
	 */
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
	public <T> Iterator<T> iterateInstances(final Class<T> category, final boolean useOrder) {
		return instanceRegistry(category).iterate(useOrder);
	}

	/**
	 * Returns an arbitrary instance that extends/implements the specified type that is filed
	 * under a category that also extends/implements that type.
	 *
	 * @param type The type to look up.
	 * @return An instance if one was found.
	 */
	public <S> Optional<S> getInstanceOfType(Class<S> type) {
		requireNonNull(type);
		return streamCategories()
				.filter(category -> category.isAssignableFrom(type))
				.map(this::instanceRegistry)
				.flatMap(registry -> stream(registry.getInstanceOfType(type)))
				.findFirst();
	}

	/**
	 * Orders the specified instances, so that the first appears before the second when
	 * {@link #iterateInstances(Class, boolean) iterateInstances} is called with
	 * {@code useOrder = true}.
	 *
	 * @param category The category to order instances for.
	 * @return {@code true} if this establishes a new order
	 */
	public <T> boolean setOrder(Class<T> category, T firstInstance, T secondInstance) {
		requireNonNull(firstInstance);
		requireNonNull(secondInstance);
		return instanceRegistry(category).setOrder(firstInstance, secondInstance);
	}

	/**
	 * Removes the ordering between the specified instances, so that the first no longer appears
	 * before the second when {@link #iterateInstances(Class, boolean) iterateInstances} is
	 * called with {@code useOrder = true}.
	 *
	 * @param category The category to clear instance order for.
	 * @return {@code true} if that ordering existed before
	 */
	public <T> boolean clearOrder(Class<T> category, T firstInstance, T secondInstance) {
		requireNonNull(firstInstance);
		requireNonNull(secondInstance);
		return instanceRegistry(category).clearOrder(firstInstance, secondInstance);
	}

	private static class InstanceRegistry<T> {

		private final FactoryRegistry factoryRegistry;
		private final Class<?> category;

		private final Map<Class<?>, T> instancesByType = new HashMap<>();
		private final PartiallyOrderedSet<T> instances = new PartiallyOrderedSet<>(false);

		private InstanceRegistry(FactoryRegistry factoryRegistry, Class<?> category) {
			this.factoryRegistry = factoryRegistry;
			this.category = category;
		}

		public boolean register(final T instance) {
			requireNonNull(instance);
			boolean deregistered = deregisterByType(instance);
			registerInternal(instance);
			notifyRegistered(instance);
			return !deregistered;
		}

		private void notifyRegistered(final T instance) {
			if (instance instanceof RegistrableFactory) {
				((RegistrableFactory) instance).onRegistration(factoryRegistry, category);
			}
		}

		private void registerInternal(final T instance) {
			instancesByType.put(instance.getClass(), instance);
			instances.add(instance);
		}

		public boolean deregister(final T instance) {
			requireNonNull(instance);
			if (instancesByType.containsKey(instance.getClass())) {
				deregisterByType(instance);
				return true;
			} else {
				return false;
			}
		}

		// documentation hint: returns true if an instance of the same type was previously registered
		private boolean deregisterByType(final T instance) {
			T removed = instancesByType.remove(instance.getClass());
			instances.remove(instance);
			notifyDeregistered(instance);
			return removed != null;
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
				instances.remove(instance);
				notifyDeregistered(instance);
			}
		}

		public Iterator<T> iterate(final boolean useOrder) {
			if (useOrder) {
				return instances.iterator();
			} else {
				return instancesByType.values().iterator();
			}
		}

		public <S> Optional<S> getInstanceOfType(Class<S> type) {
			requireNonNull(type);
			@SuppressWarnings("unchecked")
			S instance = (S) instancesByType.get(type);
			return Optional.ofNullable(instance);
		}

		public boolean setOrder(T firstInstance, T secondInstance) {
			return instancesByType.containsKey(firstInstance.getClass())
					&& instancesByType.containsKey(secondInstance.getClass())
					// if both are contained, set the order
					&& instances.setOrder(firstInstance, secondInstance);
		}

		public boolean clearOrder(T firstInstance, T secondInstance) {
			requireNonNull(firstInstance);
			requireNonNull(secondInstance);
			return instancesByType.containsKey(firstInstance.getClass())
					&& instancesByType.containsKey(secondInstance.getClass())
					// if both are contained, set the order
					&& instances.clearOrder(firstInstance, secondInstance);
		}
	}

}

