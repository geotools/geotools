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

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;


/**
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SMIL20SchemaLocationResolver implements XSDSchemaLocationResolver {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     *        @generated modifiable
     */
    public String resolveSchemaLocation(XSDSchema xsdSchema, String namespaceURI,
        String schemaLocationURI) {
        if (schemaLocationURI == null) {
            return null;
        }

        //if no namespace given, assume default for the current schema
        if (((namespaceURI == null) || "".equals(namespaceURI)) && (xsdSchema != null)) {
            namespaceURI = xsdSchema.getTargetNamespace();
        }

        if ("http://www.w3.org/2001/SMIL20/".equals(namespaceURI)) {
            if (schemaLocationURI.endsWith("smil20.xsd")) {
                return getClass().getResource("smil20.xsd").toString();
            }
        }

        if ("http://www.w3.org/XML/1998/namespace".equals(namespaceURI)) {
            if (schemaLocationURI.endsWith("xml-mod.xsd")) {
                return getClass().getResource("xml-mod.xsd").toString();
            }
        }

        if ("http://www.w3.org/2001/SMIL20/Language".equals(namespaceURI)) {
            if (schemaLocationURI.endsWith("smil20-language.xsd")) {
                return getClass().getResource("smil20-language.xsd").toString();
            }
        }

        return null;
    }
}
