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
import org.geotools.xml.SchemaIndex;


public class HandlerFactoryImpl implements HandlerFactory {
    public DocumentHandler createDocumentHandler(ParserHandler parser) {
        return new DocumentHandlerImpl(this, parser);
    }

    public ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser) {
        SchemaIndex index = parser.getSchemaIndex();

        //look up the element in the schema
        XSDElementDeclaration element = index.getElementDeclaration(qName);

        if (element != null) {
            return createElementHandler(element, parent, parser);
        }

        return null;
    }

    public ElementHandler createElementHandler(XSDElementDeclaration element, Handler parent,
        ParserHandler parser) {
        return new ElementHandlerImpl(element, parent, parser);
    }
}
