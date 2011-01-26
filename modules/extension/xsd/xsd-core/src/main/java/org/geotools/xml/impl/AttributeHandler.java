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

import org.eclipse.xsd.XSDAttributeDeclaration;


/**
 * Classes implementing this interace serve as handlers for attributes in an
 * instance document as it is parsed.
 *
 * <p>
 * An attribute handler corresponds to a specific attribute in a schema.
 * </p>
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 * @source $URL$
 */
public interface AttributeHandler extends Handler {
    /**
     * @return The schema declaration of the attribute being handled.
     */
    XSDAttributeDeclaration getAttributeDeclaration();

    /**
     * Sets the attribute instance being handled by the handler.
     *
     * @param value The value of the attribute from an instance document.
     */
    void handleAttribute(String value);
}
