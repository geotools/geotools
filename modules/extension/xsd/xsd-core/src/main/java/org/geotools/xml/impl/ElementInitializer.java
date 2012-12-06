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

import org.geotools.xml.ComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Invokes the {@link ComplexBinding#initialize(ElementInstance, Node, MutablePicoContainer)} 
 * event/callback.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class ElementInitializer extends ComplexBindingCallback {

    public ElementInitializer(ElementInstance instance, Node node, MutablePicoContainer context) {
        super(instance, node, context);
    }

    @Override
    protected void doCallback(ComplexBinding binding) {
        binding.initialize(instance, node, context);
    }

}
