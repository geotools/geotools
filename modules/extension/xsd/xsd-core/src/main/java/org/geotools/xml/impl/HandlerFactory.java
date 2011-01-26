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
import org.eclipse.xsd.XSDElementDeclaration;
import javax.xml.namespace.QName;


/**
 * Factory used to create element handler objects during the processing of an
 * instance document.
 *
 * @author Justin Deoliveira,Refractions Reserach Inc.,jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface HandlerFactory {
    /**
     * Creates a handler for the root element of a document.
     */
    DocumentHandler createDocumentHandler(ParserHandler parser);

    /**
     * Creates an element hander for a global or top level element in a document.
     *
     * @param qName The qualified name identifying the element.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new element handler, or null if one could not be created.
     */
    ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser);

    /**
     * Creates a handler for a particular element in a document.
     *
     * @param element The schema component which represents the declaration
     * of the element.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new element handler, or null if one could not be created.
     */
    ElementHandler createElementHandler(XSDElementDeclaration element, Handler parent,
        ParserHandler parser);

    /**
     * Creates a handler for a particular element in a document.
     *
     * @param attribute The schema component which represents the declaration
     * of the attribute.
     * @param parent The parent handler.
     * @param parser The content handler driving the parser.
     *
     * @return A new attribute handler, or null if one could not be created.
     */

    //AttributeHandler createAttributeHandler(XSDAttributeDeclaration attribute, Handler parent, ParserHandler parser );
}
