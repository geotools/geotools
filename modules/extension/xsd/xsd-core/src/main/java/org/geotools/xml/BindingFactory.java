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
package org.geotools.xml;

import javax.xml.namespace.QName;


/**
 * Creates the binding for a qualified name.
 * <p>
 * An instance of this factory is placed in the context and available to
 * bindings via constructor injection.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public interface BindingFactory {
    /**
     * Creates the binding from a qualified name.
     * <p>
     * Example usage.
     * <pre>
     * //Load the binding for xs int
     * QName name = new QName( "http://www.w3.org/2001/XMLSchema", "int" );
     * Binding binding = bindingFactory.createBinding( name );
     * </pre>
     * </p>
     *
     * @param name The qualified name of a schema type, element, or attribute.
     *
     * @return The binding for <code>name</code>, or <code>null</code>.
     */
    Binding createBinding(QName name);
}
