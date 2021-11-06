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

import org.geotools.xsd.Binding;

public class MismatchedBindingFinder implements BindingWalker.Visitor {

    private Object object;

    private boolean mismatched = false;

    public MismatchedBindingFinder(Object object) {
        this.object = object;
    }

    @Override
    public void visit(Binding binding) {
        if (!binding.getType().isAssignableFrom(object.getClass())) {
            mismatched = true;
        }
    }

    public boolean foundMismatchedBinding() {
        return mismatched;
    }
}
