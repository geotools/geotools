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
package org.geotools.xsd;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.xml.sax.SAXException;

/**
 * Helper class which ensures that the xsd schema parser uses pre-build schema objects.
 *
 * <p>This class works from a {@link XSD} which contains a reference to the schema.
 *
 * <p>Example usage: <code>
 *         <pre>
 *         XSD xsd = ...;
 *         String namespaceURI = xsd.getNamesapceURI();
 *
 *         SchemaLocator locator = new SchemaLocator( xsd );
 *         XSDSchema schema = locator.locateSchema( null, namespaceURI, null, null);
 *         </pre>
 * </code>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class SchemaLocator implements XSDSchemaLocator {
    /** logging instance */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SchemaLocator.class);

    /** The xsd instance. */
    protected XSD xsd;

    /**
     * Creates a new instance of the schema locator.
     *
     * @param xsd The XSD instance that references the schema to be "located".
     */
    public SchemaLocator(XSD xsd) {
        this.xsd = xsd;
    }

    /**
     * Determines if the locator can locate a schema for the specified namespace and location.
     *
     * @return true if it can handle, otherwise false.
     */
    public boolean canHandle(
            XSDSchema schema, String namespaceURI, String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
        return xsd.getNamespaceURI().equals(namespaceURI);
    }

    /**
     * Creates the schema, returning <code>null</code> if the schema could not be created. <code>
     * namespaceURI</code> should not be <code>null</code>. All other parameters are ignored.
     *
     * @see XSDSchemaLocator#locateSchema(org.eclipse.xsd.XSDSchema, java.lang.String, java.lang.String,
     *     java.lang.String)
     */
    @Override
    public XSDSchema locateSchema(
            XSDSchema schema, String namespaceURI, String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
        if (canHandle(schema, namespaceURI, rawSchemaLocationURI, resolvedSchemaLocationURI)) {
            try {
                return xsd.getSchema();
            } catch (IOException e) {
                if (e.getCause() instanceof SAXException) throw new SAXExceptionWrapper((SAXException) e.getCause());
                LOGGER.log(Level.WARNING, "Error occured getting schema", e);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return xsd.toString();
    }
}
