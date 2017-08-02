package org.geotools.factory;

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

	private final Map<Class<?>, InstanceRegistry<?>> categories = new HashMap<>();

	public CategoryRegistry() { }

	public void registerCategory(final Class<?> category) {
		// TODO: should anything happen if the category is already registered?
		categories.put(category, new InstanceRegistry<>());
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

		private final Map<Class<?>, T> instancesByType = new HashMap<>();
		private final PartiallyOrderedSet<T> instances = new PartiallyOrderedSet<>();

		// documentation hint: returns true if this the first instance of its class
		public boolean register(final T instance) {
			boolean deregistered = deregisterByType(instance);
			registerInternal(instance);
			// TODO: call onRegistration
			return !deregistered;
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

		public void clear() {
			Iterator values = instancesByType.values().iterator();
			while (values.hasNext()) {
				Object instance = values.next();
				values.remove();
				instances.remove(instance);
				// TODO: if `removed != null`, call onDeregistration with `removed`
			}
		}

		// documentation hint: returns true if an instance of the same type was previously registered
		private boolean deregisterByType(final T instance) {
			T removed = instancesByType.remove(instance.getClass());
			instances.remove(instance);
			// TODO: if `removed != null`, call onDeregistration with `removed`
			return removed != null;
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

