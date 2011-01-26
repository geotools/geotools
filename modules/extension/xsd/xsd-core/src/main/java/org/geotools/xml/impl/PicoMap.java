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
package org.geotools.xml.impl;

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
 * A {@link Map} dectorator which implements the {@link MutablePicoContainer} 
 * interface.
 * <p>
 * This class is used internally to help transitition from pico container to a 
 * map based system for registering bindings.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public class PicoMap implements Map, MutablePicoContainer {

    Map delegate;
    
    public PicoMap( Map delegate ) {
        this.delegate = delegate;
    }
    
    public void clear() {
        delegate.clear();
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Set entrySet() {
        return delegate.entrySet();
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public Object get(Object key) {
        return delegate.get(key);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Set keySet() {
        return delegate.keySet();
    }

    public Object put(Object key, Object value) {
        return delegate.put(key, value);
    }

    public void putAll(Map t) {
        delegate.putAll(t);
    }

    public Object remove(Object key) {
        return delegate.remove(key);
    }

    public int size() {
        return delegate.size();
    }

    public Collection values() {
        return delegate.values();
    }

    public boolean addChildContainer(PicoContainer child) {
        return false;
    }

    public MutablePicoContainer makeChildContainer() {
        return null;
    }

    public ComponentAdapter registerComponent(ComponentAdapter componentAdapter) {
        if ( componentAdapter instanceof DecoratingComponentAdapter ) {
            componentAdapter = ((DecoratingComponentAdapter)componentAdapter).getDelegate();
        }
        
        Object key = componentAdapter.getComponentKey();
        if ( componentAdapter instanceof InstanceComponentAdapter ) {
            Object instance = ((InstanceComponentAdapter)componentAdapter).getComponentInstance(null);
            put( key, instance );
        }
        else {
            Class implementation = componentAdapter.getComponentImplementation();
            put( key, implementation );
        }
        
        return componentAdapter;
    }

    public ComponentAdapter registerComponentImplementation(
            Class componentImplementation) {
        put( componentImplementation, componentImplementation );
        return null;
    }

    public ComponentAdapter registerComponentImplementation(
            Object componentKey, Class componentImplementation) {
        put( componentKey, componentImplementation );
        return null;
    }

    public ComponentAdapter registerComponentImplementation(
            Object componentKey, Class componentImplementation,
            Parameter[] parameters) {
        put( componentKey, componentImplementation );
        return null;
    }

    public ComponentAdapter registerComponentInstance(Object componentInstance) {
        put( componentInstance, componentInstance );
        return null;
    }

    public ComponentAdapter registerComponentInstance(Object componentKey,
            Object componentInstance) {
        put( componentKey, componentInstance );
        return null;
    }

    public boolean removeChildContainer(PicoContainer child) {
        return false;
    }

    public ComponentAdapter unregisterComponent(Object componentKey) {
        remove(componentKey);
        return null;
    }

    public ComponentAdapter unregisterComponentByInstance(
            Object componentInstance) {
        if ( componentInstance == null ) {
            return null;
        }
        
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
            
            if ( entry.getValue() instanceof Class ) {
                continue;
            }
            
            if ( componentInstance.equals( entry.getValue() ) ) {
                e.remove();
            }
        }
        
        return null;
    }

    public void accept(PicoVisitor visitor) {
    }

    public ComponentAdapter getComponentAdapter(Object componentKey) {
        if ( componentKey == null ) {
            return null;
        }
        
        Object o = get( componentKey );
        if ( o == null ) {
            return null;
        }
        
        if ( o instanceof Class ) {
            //TODO: determine which form of injection to use
            return new ConstructorInjectionComponentAdapter( componentKey, (Class) o );
        }
        
        return new InstanceComponentAdapter( componentKey, o );
    }

    public ComponentAdapter getComponentAdapterOfType(Class componentType) {
        List adapters = getComponentAdaptersOfType(componentType);
        if ( adapters.isEmpty() ) {
            return null;
        }
        
        return (ComponentAdapter) adapters.iterator().next();
    }

    public Collection getComponentAdapters() {
        List adapters = new ArrayList();
        
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
            adapters.add( getComponentAdapter(entry.getKey() ) );
        }
        
        return adapters;
    }

    public List getComponentAdaptersOfType(Class componentType) {
        if ( componentType == null ) {
            return Collections.EMPTY_LIST;
        }
        
        List adapters = new ArrayList();
        
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
            
            if ( entry.getValue() instanceof Class ) {
                Class clazz = (Class)entry.getValue();
                if ( componentType.isAssignableFrom(clazz) ) {
                    adapters.add( getComponentAdapter(entry.getKey()) );
                }
            }
            else {
                if ( componentType.isInstance( entry.getValue() ) ) {
                    adapters.add( getComponentAdapter(entry.getKey()) );
                }
            }
        }
        
        return adapters;
    }

    public Object getComponentInstance(Object componentKey) {
        if ( componentKey == null ) {
            return null;
        }
        
        Object o = get( componentKey );
        if ( o instanceof Class ) {
            //TODO: instantiate
            Class clazz = (Class) o;
            try {
                return clazz.newInstance();
            } 
            catch( Exception e ) {
                throw new RuntimeException( e );
            }
        }
        else {
            return o;
        }
    }

    public Object getComponentInstanceOfType(Class componentType) {
        if ( componentType == null ) {
            return Collections.EMPTY_LIST;
        }
        
        //first look for instance
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
         
            if ( entry.getValue() instanceof Class ) {
                continue;
            }
            
            if ( componentType.isInstance( entry.getValue() ) ) {
                return entry.getValue();
            }
        }
        
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
         
            if ( entry.getValue() instanceof Class ) {
                Class clazz = (Class) entry.getValue();
                if ( componentType.isAssignableFrom( clazz )  ) {
                    return getComponentInstance( entry.getKey() );
                }
            }
        }
        
        return null;
        
    }

    public List getComponentInstances() {
        ArrayList instances = new ArrayList();
        
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
            instances.add( getComponentInstance(entry.getKey()) );
        }
     
        return instances;
    }

    public List getComponentInstancesOfType(Class componentType) {
        if ( componentType == null ) {
            return Collections.EMPTY_LIST;
        }
        
        ArrayList instances = new ArrayList();
        for ( Iterator e = entrySet().iterator(); e.hasNext(); ) {
            Entry entry = (Entry) e.next();
         
            if ( entry.getValue() instanceof Class ) {
                Class clazz = (Class) entry.getValue();
                if ( componentType.isAssignableFrom(clazz)) {
                    instances.add( getComponentInstance(entry.getKey()) );
                }
            }
            
            if ( componentType.isInstance( entry.getValue() ) ) {
                instances.add( getComponentInstance(entry.getKey()) );
            }
        }
     
        return instances;
    }

    public PicoContainer getParent() {
        return null;
    }

    public void verify() throws PicoVerificationException {
    }

    public void start() {
    }

    public void stop() {
    }

    public void dispose() {
    }

}
