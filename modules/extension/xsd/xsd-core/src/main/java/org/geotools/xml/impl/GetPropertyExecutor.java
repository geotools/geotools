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

import org.eclipse.xsd.XSDNamedComponent;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.util.Converters;
import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;


/**
 * Gets children from a parent object, visiting bindings in teh
 * hierachy until one is found.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class GetPropertyExecutor implements BindingWalker.Visitor {
    /**
     * logger
     */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.xml");

    /** parent + child objects **/
    Object parent;
    Object child;

    /** declaration (element or attribute) + qualified name**/
    QName name;

    public GetPropertyExecutor(Object parent, XSDNamedComponent content) {
        this.parent = parent;

        name = new QName(content.getTargetNamespace(), content.getName());
    }

    public Object getChildObject() {
        return child;
    }

    public void visit(Binding binding) {
        //TODO: visit should return a boolena to signify wether to continue
        if (child != null) {
            return;
        }

        if (binding instanceof ComplexBinding) {
            ComplexBinding complex = (ComplexBinding) binding;

            if (binding.getType() == null) {
                LOGGER.warning("Binding for: " + binding.getTarget() + " does not declare type");
            }

            Object parent = this.parent;

            if ((binding.getType() != null)
                    && !binding.getType().isAssignableFrom(parent.getClass())) {
                LOGGER.fine(parent + " (" + parent.getClass().getName() + ") "
                    + " is not of type " + binding.getType().getName());

                //try to convert
                Object converted = Converters.convert(parent, binding.getType());

                if (converted != null) {
                    parent = converted;
                } else {
                    LOGGER.fine("Could not convert " + parent + " to "
                        + binding.getType().getName());
                    // For complex feature, if the feature can't be converted to the binding type,
                    // exit the route to avoid ClassCastException raised in
                    // child = complex.getProperty(parent, name);
                    // For example, MeasureTypeBinding.getProperty(parent, name) throws
                    // ClassCastException when 'uom' is not set.
                    if (parent instanceof ComplexAttributeImpl) {
                        return;
                    }
                }
            }

            try {
                child = complex.getProperty(parent, name);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get property: " + name, e);
            }
        }
    }
}
