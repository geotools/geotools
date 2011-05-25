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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;


/**
 * Gets properties from a parent object by visiting bindings in the hierachy.
 * The object properties are stored as name, object tuples.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class GetPropertiesExecutor implements BindingWalker.Visitor {
    /** the parent object */
    Object parent;

    /** the parent element */
    XSDElementDeclaration element;
    
    /** the properties */
    List properties;

    public GetPropertiesExecutor(Object parent, XSDElementDeclaration element) {
        this.parent = parent;
        this.element = element;
        properties = new ArrayList();
    }

    public List getProperties() {
        return properties;
    }

    public void visit(Binding binding) {
        if (binding instanceof ComplexBinding) {
            ComplexBinding complex = (ComplexBinding) binding;

            try {
                List properties = complex.getProperties(parent);
                if ( properties == null || properties.isEmpty() ) {
                    properties = complex.getProperties(parent, element);
                }
                if (properties != null) {
                    this.properties.addAll(properties);
                }
            } catch (Exception e) {
                String msg = "Failed to get properties. Binding for " + complex.getTarget();
                throw new RuntimeException(msg, e);
            }
        }
    }
}
