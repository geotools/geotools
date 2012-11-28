/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.impl.BindingWalker.Visitor;
import org.picocontainer.MutablePicoContainer;

/**
 * Base class for visitors that invoke a callback on a complex binding.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public abstract class ComplexBindingCallback implements Visitor {

    protected final ElementInstance instance;
    protected final Node node;
    protected final MutablePicoContainer context;

    public ComplexBindingCallback(ElementInstance instance, Node node, MutablePicoContainer context) {
        this.instance = instance;
        this.node = node;
        this.context = context;
    }

    @Override
    public void visit(Binding binding) {
        if (binding instanceof ComplexBinding) {
            doCallback((ComplexBinding) binding);
        }
    }

    protected abstract void doCallback(ComplexBinding binding);
}
