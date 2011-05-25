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
package org.geotools.gml3.v3_2;

import java.io.IOException;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geotools.xml.SchemaLocationResolver;
import org.geotools.xml.XSD;


/**
 * This is a base class which hacks around the circular dependency 
 * in the gml 3.2 schema.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 * @source $URL$
 */
public abstract class StubbedGMLXSD extends XSD {

   
    protected static void loadSchema( XSD _this ) {
        try {
            _this.getSchema();
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
    
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        if ( schema == null ) {
            return new GMLStubSchemaLocationResolver(super.createSchemaLocationResolver(),this);    
        }
        else {
            return super.createSchemaLocationResolver();
        }
        
    }
    
    private static class GMLStubSchemaLocationResolver extends SchemaLocationResolver {

        XSDSchemaLocationResolver resolver;
        public GMLStubSchemaLocationResolver( XSDSchemaLocationResolver resolver, XSD xsd ) {
            super( xsd );
            this.resolver = resolver;
        }
        
        @Override
        public boolean canHandle(XSDSchema schema, String uri, String location) {
            return GML.NAMESPACE.equals( uri ) || super.canHandle(schema, uri, location);
        }
        
        public String resolveSchemaLocation(XSDSchema xsdSchema,
                String namespaceURI, String schemaLocationURI) {
            if ( GML.NAMESPACE.equals( namespaceURI ) ) {
                return "";
            }
            else {
                return resolver.resolveSchemaLocation(xsdSchema, namespaceURI, schemaLocationURI);
            }
        }
    }

}
