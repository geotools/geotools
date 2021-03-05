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
package org.geotools.xsd.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DecoratingComponentAdapter;
import org.picocontainer.defaults.InstanceComponentAdapter;

/**
 * A {@link Map} decorator which implements the {@link MutablePicoContainer} interface.
 *
 * <p>This class is used internally to help transition from pico container to a map based system for
 * registering bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
// MutablePicoContainer has no generics, and is dead as a library, no point trying to make this
// one better
@SuppressWarnings("unchecked")
public class PicoMap implements Map, MutablePicoContainer {

    Map<Object, Object> delegate;

    public PicoMap(Map delegate) {
        this.delegate = delegate;
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Set entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public Object get(Object key) {
        return delegate.get(key);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Set keySet() {
        return delegate.keySet();
    }

    @Override
    public Object put(Object key, Object value) {
        return delegate.put(key, value);
    }

    @Override
    public void putAll(Map t) {
        delegate.putAll(t);
    }

    @Override
    public Object remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Collection values() {
        return delegate.values();
    }

    @Override
    public boolean addChildContainer(PicoContainer child) {
        return false;
    }

    @Override
    public MutablePicoContainer makeChildContainer() {
        return null;
    }

    @Override
    public ComponentAdapter registerComponent(ComponentAdapter componentAdapter) {
        if (componentAdapter instanceof DecoratingComponentAdapter) {
            componentAdapter = ((DecoratingComponentAdapter) componentAdapter).getDelegate();
        }

        Object key = componentAdapter.getComponentKey();
        if (componentAdapter instanceof InstanceComponentAdapter) {
            Object instance =
                    ((InstanceComponentAdapter) componentAdapter).getComponentInstance(null);
            put(key, instance);
        } else {
            Class implementation = componentAdapter.getComponentImplementation();
            put(key, implementation);
        }

        return componentAdapter;
    }

    @Override
    public ComponentAdapter registerComponentImplementation(Class componentImplementation) {
        put(componentImplementation, componentImplementation);
        return null;
    }

    @Override
    public ComponentAdapter registerComponentImplementation(
            Object componentKey, Class componentImplementation) {
        put(componentKey, componentImplementation);
        return null;
    }

    @Override
    public ComponentAdapter registerComponentImplementation(
            Object componentKey, Class componentImplementation, Parameter[] parameters) {
        put(componentKey, componentImplementation);
        return null;
    }

    @Override
    public ComponentAdapter registerComponentInstance(Object componentInstance) {
        put(componentInstance, componentInstance);
        return null;
    }

    @Override
    public ComponentAdapter registerComponentInstance(
            Object componentKey, Object componentInstance) {
        put(componentKey, componentInstance);
        return null;
    }

    @Override
    public boolean removeChildContainer(PicoContainer child) {
        return false;
    }

    @Override
    public ComponentAdapter unregisterComponent(Object componentKey) {
        remove(componentKey);
        return null;
    }

    @Override
    public ComponentAdapter unregisterComponentByInstance(Object componentInstance) {
        if (componentInstance == null) {
            return null;
        }

        for (Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();

            if (entry.getValue() instanceof Class) {
                continue;
            }

            if (componentInstance.equals(entry.getValue())) {
                e.remove();
            }
        }

        return null;
    }

    @Override
    public void accept(PicoVisitor visitor) {}

    @Override
    public ComponentAdapter getComponentAdapter(Object componentKey) {
        if (componentKey == null) {
            return null;
        }

        Object o = get(componentKey);
        if (o == null) {
            return null;
        }

        if (o instanceof Class) {
            // TODO: determine which form of injection to use
            return new ConstructorInjectionComponentAdapter(componentKey, (Class) o);
        }

        return new InstanceComponentAdapter(componentKey, o);
    }

    @Override
    public ComponentAdapter getComponentAdapterOfType(Class componentType) {
        List adapters = getComponentAdaptersOfType(componentType);
        if (adapters.isEmpty()) {
            return null;
        }

        return (ComponentAdapter) adapters.iterator().next();
    }

    @Override
    public Collection getComponentAdapters() {
        List adapters = new ArrayList();

        for (Object o : entrySet()) {
            Entry entry = (Entry) o;
            adapters.add(getComponentAdapter(entry.getKey()));
        }

        return adapters;
    }

    @Override
    public List getComponentAdaptersOfType(Class componentType) {
        if (componentType == null) {
            return Collections.emptyList();
        }

        List adapters = new ArrayList();

        for (Object o : entrySet()) {
            Entry entry = (Entry) o;

            if (entry.getValue() instanceof Class) {
                Class clazz = (Class) entry.getValue();
                if (componentType.isAssignableFrom(clazz)) {
                    adapters.add(getComponentAdapter(entry.getKey()));
                }
            } else {
                if (componentType.isInstance(entry.getValue())) {
                    adapters.add(getComponentAdapter(entry.getKey()));
                }
            }
        }

        return adapters;
    }

    @Override
    public Object getComponentInstance(Object componentKey) {
        if (componentKey == null) {
            return null;
        }

        Object o = get(componentKey);
        if (o instanceof Class) {
            // TODO: instantiate
            Class clazz = (Class) o;
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return o;
        }
    }

    @Override
    public Object getComponentInstanceOfType(Class componentType) {
        if (componentType == null) {
            return Collections.emptyList();
        }

        // first look for instance
        for (Object value : entrySet()) {
            Entry entry = (Entry) value;

            if (entry.getValue() instanceof Class) {
                continue;
            }

            if (componentType.isInstance(entry.getValue())) {
                return entry.getValue();
            }
        }

        for (Object o : entrySet()) {
            Entry entry = (Entry) o;

            if (entry.getValue() instanceof Class) {
                Class clazz = (Class) entry.getValue();
                if (componentType.isAssignableFrom(clazz)) {
                    return getComponentInstance(entry.getKey());
                }
            }
        }

        return null;
    }

    @Override
    public List getComponentInstances() {
        ArrayList instances = new ArrayList();

        for (Object o : entrySet()) {
            Entry entry = (Entry) o;
            instances.add(getComponentInstance(entry.getKey()));
        }

        return instances;
    }

    @Override
    public List getComponentInstancesOfType(Class componentType) {
        if (componentType == null) {
            return Collections.emptyList();
        }

        ArrayList instances = new ArrayList();
        for (Object o : entrySet()) {
            Entry entry = (Entry) o;

            if (entry.getValue() instanceof Class) {
                Class clazz = (Class) entry.getValue();
                if (componentType.isAssignableFrom(clazz)) {
                    instances.add(getComponentInstance(entry.getKey()));
                }
            }

            if (componentType.isInstance(entry.getValue())) {
                instances.add(getComponentInstance(entry.getKey()));
            }
        }

        return instances;
    }

    @Override
    public PicoContainer getParent() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void verify() throws PicoVerificationException {}

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void dispose() {}
}
