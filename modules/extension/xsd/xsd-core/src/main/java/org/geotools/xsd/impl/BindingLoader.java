/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.xsd.Binding;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.InstanceComponentAdapter;

/**
 * Stores a list of binding classes and provides ways to load or get a specific {@link Binding}
 * object based on {@link QName}. It can handle storage of classes that support the {@link
 * ComponentAdapter} interface or other classes but will attempt to instantiate and return a {@link
 * Binding} object.
 *
 * <p>It uses a Context (otherwise known as PicoContainer) to instantiate the relevant object and
 * understand the dependencies that should also be loaded. The Context is required to be passed in
 * by the caller
 */
public class BindingLoader {

    Map bindings;

    public BindingLoader(Map bindings) {
        this.bindings = bindings;
    }

    /**
     * Loads a binding with a specific QName into a context.
     *
     * @param qName The qualified name of the type of the binding object.
     * @param context The context which is to contain the binding.
     * @return The binding object of the associated type, otherwise null if no such binding could be
     *     created.
     */
    public Binding loadBinding(QName qName, PicoContainer context) {
        Object o = bindings.get(qName);
        if (o == null) {
            return null;
        }
        if (o instanceof ComponentAdapter) {
            return (Binding) ((ComponentAdapter) o).getComponentInstance(context);
        }

        if (o instanceof Class) {
            return loadBinding(qName, (Class) o, context);
        }

        return (Binding) o;
    }

    /**
     * Loads a binding with a specific class into a context.
     *
     * @param bindingClass The class of the binding.
     * @param context The context which is to contain the binding.
     * @return The binding object of the associated type, otherwise null if no such binding could be
     *     created.
     */
    public Binding loadBinding(QName qName, Class bindingClass, PicoContainer context) {
        // instantiate within the given context
        ComponentAdapter adapter = new ConstructorInjectionComponentAdapter(qName, bindingClass);
        return (Binding) adapter.getComponentInstance(context);
    }

    /**
     * Returns the component adapter for a binding with the specified name.
     *
     * @param type The qualified name of the type of the binding.
     * @return The binding class, or null if no such class exists.
     */
    protected ComponentAdapter getBinding(QName type) {
        Object o = bindings.get(type);
        if (o == null) {
            return null;
        }

        if (o instanceof ComponentAdapter) {
            return (ComponentAdapter) o;
        }

        if (o instanceof Class) {
            return new ConstructorInjectionComponentAdapter(null, (Class) o);
        }

        return new InstanceComponentAdapter(null, o);
    }
}
