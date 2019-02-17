/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import org.eclipse.xsd.XSDFeature;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xsd.impl.BindingWalker.Visitor;
import org.opengis.feature.ComplexAttribute;
import org.picocontainer.MutablePicoContainer;

/**
 * Methods for the dispatch of binding visitors that first check for type mismatches between binding
 * Java types and instance types.
 *
 * <p>If a mismatched biding is found for a complex attribute, the binding for xs:anyType is
 * visited.
 *
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class BindingVisitorDispatch {

    /** This is a static method class, not to be instantiated. */
    private BindingVisitorDispatch() {}

    public static void walk(
            Object object,
            BindingWalker bindingWalker,
            XSDFeature component,
            Visitor visitor,
            MutablePicoContainer context) {
        walk(object, bindingWalker, component, visitor, null, context);
    }

    public static void walk(
            Object object,
            BindingWalker bindingWalker,
            XSDFeature component,
            Visitor visitor,
            XSDTypeDefinition container,
            MutablePicoContainer context) {
        // do not test simple bindings as they are often mismatched and rely on converters
        if (object instanceof ComplexAttribute) {
            MismatchedBindingFinder finder = new MismatchedBindingFinder(object);
            bindingWalker.walk(component, finder, container, context);
            if (finder.foundMismatchedBinding()) {
                // if a mismatched binding is found, just visit xs:anyType binding
                visitor.visit(bindingWalker.getAnyTypeBinding());
                return;
            }
        }
        bindingWalker.walk(component, visitor, container, context);
    }
}
