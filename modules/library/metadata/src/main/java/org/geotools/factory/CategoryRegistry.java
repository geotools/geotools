package org.geotools.factory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.geotools.util.PartiallyOrderedSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.geotools.util.Utilities.stream;

/*
 * TODO: document all
 *
 *  - no null checks
 *  - no subclass relationships between categories and instances
 */

class CategoryRegistry {

	/**
	 * Heterogeneous map, where `Class<T>` is mapped to `InstanceRegistry<T>` for any `T`.
	 *
	 * Code depends on this map being immutable to avoid concurrent modification exceptions.
	 */
	private final ImmutableMap<Class<?>, InstanceRegistry<?>> categories;

	/**
	 * Creates a new registry with the specified Registers the specified category. If the same category is registered multiple times,
	 * all instances previously registered for that category are lost.
	 *
	 * @param factoryRegistry The {@link FactoryRegistry} this registry belongs to.
	 * @param categories The categories to register; must not be {@code null}.
	 */
	public CategoryRegistry(FactoryRegistry factoryRegistry, Iterable<Class<?>> categories) {
		this.categories = createCategoriesMap(factoryRegistry, categories);
	}

	private static ImmutableMap<Class<?>, InstanceRegistry<?>> createCategoriesMap(
			final FactoryRegistry factoryRegistry, final Iterable<Class<?>> categories) {
		Builder<Class<?>, InstanceRegistry<?>> categoriesBuilder = ImmutableMap.builder();
		categories.forEach(category ->
				categoriesBuilder.put(category, new InstanceRegistry<>(factoryRegistry, category)));
		return categoriesBuilder.build();
	}

	@SuppressWarnings("unchecked")
	public <T> void registerInstance(final T instance) {
		streamCategories()
				.filter(category -> category.isAssignableFrom(instance.getClass()))
				// the cast is correct because the filter above only leaves categories that are supertypes of `instance`
				.map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
				.forEach(registry -> registry.register(instance));
	}

	// documentation hint: returns true if this the first instance of its class
	public <T> boolean registerInstance(final T instance, final Class<T> category) {
		return instanceRegistry(category).register(instance);
	}

	// documentation hint: returns true if an instance of the same type was previously registered
	public <T> void deregisterInstance(final T instance) {
		streamCategories()
				.filter(category -> category.isAssignableFrom(instance.getClass()))
				// the cast is correct because the filter above only leaves categories that are supertypes of `instance`
				.map(category -> (InstanceRegistry<? super T>) instanceRegistry(category))
				.forEach(registry -> registry.deregister(instance));
	}

	// documentation hint: returns true if an instance of the same type was previously registered
	public <T> boolean deregisterInstance(final T instance, final Class<T> category) {
		return instanceRegistry(category).deregister(instance);
	}

	public void deregisterInstances() {
		categories.values().forEach(InstanceRegistry::clear);
	}

	public void deregisterInstances(Class<?> category) {
		instanceRegistry(category).clear();
	}

	private <T> InstanceRegistry<T> instanceRegistry(final Class<T> category) {
		@SuppressWarnings("unchecked")
		// during construction, we registered `InstanceRegistry<T>` for `Class<T>`, so this cast is save
		InstanceRegistry<T> registry = (InstanceRegistry<T>) categories.get(category);
		if (registry == null) {
			// TODO: do something fancy like in FactoryRegistry#getServiceProvider(Class, Predicate, Hints, Hints.Key) ?
			throw new IllegalArgumentException("The category '" + category + "' is not registered");
		}
		return registry;
	}

	public Stream<Class<?>> streamCategories() {
		return categories.keySet().stream();
	}

	public <T> Iterator<T> iterateInstances(final Class<T> category, final boolean useOrder) {
		return instanceRegistry(category).iterate(useOrder);
	}

	public <S> Optional<S> getInstanceOfType(Class<S> type) {
		return streamCategories()
				.filter(category -> category.isAssignableFrom(type))
				.map(this::instanceRegistry)
				.flatMap(registry -> stream(registry.getInstanceOfType(type)))
				.findFirst();
	}

	public <T> boolean setOrder(Class<T> category, T firstProvider, T secondProvider) {
		return instanceRegistry(category).setOrder(firstProvider, secondProvider);
	}

	public <T> boolean clearOrder(Class<T> category, T firstProvider, T secondProvider) {
		return instanceRegistry(category).clearOrder(firstProvider, secondProvider);
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

		// documentation hint: returns true if this the first instance of its class
		public boolean register(final T instance) {
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
			@SuppressWarnings("unchecked")
			S instance = (S) instancesByType.get(type);
			return Optional.ofNullable(instance);
		}

		// document hint: returns true if this establishes new order
		public boolean setOrder(T firstProvider, T secondProvider) {
			return instancesByType.containsKey(firstProvider.getClass())
					&& instancesByType.containsKey(secondProvider.getClass())
					// if both are contained, set the order
					&& instances.setOrder(firstProvider, secondProvider);
		}

		public boolean clearOrder(T firstProvider, T secondProvider) {
			return instancesByType.containsKey(firstProvider.getClass())
					&& instancesByType.containsKey(secondProvider.getClass())
					// if both are contained, set the order
					&& instances.clearOrder(firstProvider, secondProvider);
		}
	}

}

