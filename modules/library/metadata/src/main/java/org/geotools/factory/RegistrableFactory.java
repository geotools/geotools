package org.geotools.factory;

// TODO: document
public interface RegistrableFactory {

	void onRegistration(FactoryRegistry registry, Class<?> category);

	void onDeregistration(FactoryRegistry registry, Class<?> category);

}
