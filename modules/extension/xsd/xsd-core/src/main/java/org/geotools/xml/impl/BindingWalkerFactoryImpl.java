/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.eclipse.xsd.XSDFeature;
import org.picocontainer.MutablePicoContainer;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.impl.BindingWalker.Visitor;


public class BindingWalkerFactoryImpl implements BindingWalkerFactory {
    BindingLoader bindingLoader;
    MutablePicoContainer context;

    public BindingWalkerFactoryImpl(BindingLoader bindingLoader, MutablePicoContainer context) {
        this.bindingLoader = bindingLoader;
        this.context = context;
    }

    public void walk(XSDFeature component, Visitor visitor) {
        new BindingWalker(bindingLoader).walk(component, visitor, context);
    }

    public void setContext(MutablePicoContainer context) {
        this.context = context;
    }
}
