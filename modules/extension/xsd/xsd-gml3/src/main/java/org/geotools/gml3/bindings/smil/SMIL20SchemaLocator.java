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
package org.geotools.gml3.bindings.smil;

import java.io.IOException;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.gml3.smil.SMIL20;
import org.geotools.gml3.smil.SMIL20LANG;
import org.geotools.xml.Schemas;


public class SMIL20SchemaLocator implements XSDSchemaLocator {
    public XSDSchema locateSchema(XSDSchema schema, String namespaceURI,
        String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
        if (SMIL20.NAMESPACE.equals(namespaceURI)) {
            String location = getClass().getResource("smil20.xsd").toString();

            XSDSchemaLocationResolver[] locators = new XSDSchemaLocationResolver[] {
                    new SMIL20SchemaLocationResolver()
                };

            try {
                return Schemas.parse(location, null, locators);
            } catch (IOException e) {
                //TODO:  log this
            }
        }

        if (SMIL20LANG.NAMESPACE.equals(namespaceURI)) {
            String location = getClass().getResource("smil20-language.xsd").toString();

            XSDSchemaLocationResolver[] locators = new XSDSchemaLocationResolver[] {
                    new SMIL20SchemaLocationResolver()
                };

            try {
                return Schemas.parse(location, null, locators);
            } catch (IOException e) {
                //TODO:  log this
            }
        }

        return null;
    }
}
