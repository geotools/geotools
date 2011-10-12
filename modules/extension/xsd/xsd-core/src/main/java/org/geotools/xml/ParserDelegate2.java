/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.xml.impl.Handler;
import org.xml.sax.Attributes;

/**
 * Extension of {@link ParserDelegate} used to stage new interface methods.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public interface ParserDelegate2 {

    /**
     * Determines if this delegate can handle the specified element name.
     * <p>
     * A common check in this method would be to check the namespace of the element.
     * </p>
     * @param elementName The name of the element to potentially handle.
     * @param attributes The attributes of the element to potentially handle
     * @param handler The parse handler that would normally handle the element, possibly <code>null</code>
     * @param parent The parse handler for the parent element, possibly <code>null</code>.
     * @return True if this delegate handles elements of the specified name and should
     * take over parsing.
     */
    boolean canHandle(QName elementName, Attributes attributes, Handler handler, Handler parent);
}
