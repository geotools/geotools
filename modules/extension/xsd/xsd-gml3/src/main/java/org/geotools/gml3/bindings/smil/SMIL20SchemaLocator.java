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
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.gml3.smil.SMIL20;
import org.geotools.gml3.smil.SMIL20LANG;
import org.geotools.util.InternalEntityResolver;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Schemas;

public class SMIL20SchemaLocator implements XSDSchemaLocator {

    public static final Logger LOGGER = Logging.getLogger(SMIL20SchemaLocator.class);

    @Override
    public XSDSchema locateSchema(
            XSDSchema schema, String namespaceURI, String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
        if (SMIL20.NAMESPACE.equals(namespaceURI)) {
            String location = getClass().getResource("smil20.xsd").toString();

            try {
                // Internal: Use of NullEntityResolver to trust internal location
                return Schemas.parse(
                        location,
                        null,
                        Collections.singletonList(new SMIL20SchemaLocationResolver()),
                        Collections.emptyList(),
                        InternalEntityResolver.INSTANCE);
            } catch (IOException e) {
                LOGGER.log(Level.FINE, "Failed to load SMIL20 schema: " + e.getMessage(), e);
            }
        }

        if (SMIL20LANG.NAMESPACE.equals(namespaceURI)) {
            String location = getClass().getResource("smil20-language.xsd").toString();

            try {
                // Internal: Use of NullEntityResolver to trust internal location
                return Schemas.parse(
                        location,
                        null,
                        Collections.singletonList(new SMIL20SchemaLocationResolver()),
                        Collections.emptyList(),
                        InternalEntityResolver.INSTANCE);
            } catch (IOException e) {
                LOGGER.log(Level.FINE, "Failed to load SMIL20 schema: " + e.getMessage(), e);
            }
        }

        return null;
    }
}
