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
 *
 */

package org.geotools.kml.v22;

import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.impl.ElementHandler;
import org.geotools.xsd.impl.Handler;
import org.geotools.xsd.impl.HandlerFactory;
import org.geotools.xsd.impl.HandlerFactoryImpl;
import org.geotools.xsd.impl.ParserHandler;

public class KMLCustomSchemaHandlerFactory extends HandlerFactoryImpl implements HandlerFactory {

    private final SchemaRegistry schemaRegistry;

    public KMLCustomSchemaHandlerFactory(SchemaRegistry schemaRegistry) {
        super();
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser) {
        String name = qName.getLocalPart();
        if (schemaRegistry.get(name) != null) {
            // we found a custom schema element
            // let's treat it as if we've found a placemark
            SchemaIndex schemaIndex = parser.getSchemaIndex();
            XSDElementDeclaration element = schemaIndex.getElementDeclaration(KML.Placemark);
            if (element != null) {
                return createElementHandler(element, parent, parser);
            }
        }
        return super.createElementHandler(qName, parent, parser);
    }
}
