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

import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDTypeDefinition;


public interface InstanceComponent {
    /**
     * @return The object containing the type definiton of the instance.
     */
    XSDTypeDefinition getTypeDefinition();

    /**
     * @return The feature describing the component instance.
     */
    XSDNamedComponent getDeclaration();

    /**
     * @return The namespace of the element;
     */
    String getNamespace();

    /**
     * Sets the namespace of the element.
     *
     * @param namespace The new namespace.
     */
    void setNamespace(String namespace);

    /**
     * @return The name of the element.
     */
    String getName();

    /**
     * Sets the name of the element.
     *
     * @param name The new name.
     */
    void setName(String name);

    /**
     * @return The text inside of the component, or the empty string if the
     * component does not contain any text.
     */
    String getText();

    /**
     * Sets the text of the element.
     *
     * @param text The new text
     */
    void setText(String text);
}
