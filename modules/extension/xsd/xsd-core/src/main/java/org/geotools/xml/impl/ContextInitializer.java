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

import org.picocontainer.MutablePicoContainer;
import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.impl.BindingWalker.Visitor;


public class ContextInitializer implements Visitor {
    ElementInstance childInstance;
    Node node;
    MutablePicoContainer context;

    public ContextInitializer(ElementInstance childInstance, Node node, MutablePicoContainer context) {
        this.childInstance = childInstance;
        this.node = node;
        this.context = context;
    }

    public void visit(Binding binding) {
        if (binding instanceof ComplexBinding) {
            ComplexBinding cStrategy = (ComplexBinding) binding;
            cStrategy.initializeChildContext(childInstance, node, context);
        }
    }
}
