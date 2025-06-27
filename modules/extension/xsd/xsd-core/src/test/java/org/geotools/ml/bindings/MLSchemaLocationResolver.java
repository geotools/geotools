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
package org.geotools.ml.bindings;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;

public class MLSchemaLocationResolver implements XSDSchemaLocationResolver {
    @Override
    public String resolveSchemaLocation(XSDSchema xsdSchema, String namespaceURI, String schemaLocationURI) {
        if (schemaLocationURI == null) {
            return null;
        }

        // if no namespace given, assume default for the current schema
        if ((namespaceURI == null || "".equals(namespaceURI)) && xsdSchema != null) {
            namespaceURI = xsdSchema.getTargetNamespace();
        }

        if (ML.NAMESPACE.equals(namespaceURI)) {
            if (schemaLocationURI.endsWith("mails.xsd")) {
                return getClass().getResource("mails.xsd").toString();
            }
        }

        return null;
    }
}
