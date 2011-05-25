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

import org.eclipse.xsd.XSDElementDeclaration;


/**
 * Represents an element in an instance document.
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface ElementInstance extends InstanceComponent {
    /**
     * @return The declaration of the element in the schema.
     */
    XSDElementDeclaration getElementDeclaration();

    /**
     * @return The attributes of the element.
     */
    AttributeInstance[] getAttributes();

    /**
     * Sets the attributes of the element.
     *
     * @param atts The new attributes.
     */
    void setAttributes(AttributeInstance[] atts);
}
