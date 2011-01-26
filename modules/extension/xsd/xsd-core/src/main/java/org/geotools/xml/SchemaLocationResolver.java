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
package org.geotools.xml;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;


/**
 * Resolves a physical schema location from a namespace uri.
 * <p>
 * This class works from a {@link org.geotools.xml.XSD} instance from which it
 * resolves location on disk relative to.
 * </p>
 * <p>
 * Example usage:
 *
 * <code>
 *         <pre>
 *         XSD xsd = ...
 *         String namespaceURI = xsd.getNamesapceURI();
 *
 *         SchemaLocationResolver resolver = new SchemaLocationResolver( xsd );
 *         String schemaLocation = locator.resolveSchemaLocation( null, namespaceURI, "mySchema.xsd" );
 *         </pre>
 * </code>
 *
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class SchemaLocationResolver implements XSDSchemaLocationResolver {
    /**
     * the xsd instance
     */
    protected XSD xsd;
    
    /**
     * A list of locations to use as prefixes when looking up schema files.
     * <p>
     * This value should be set in cases where an xml schema imports or includes
     * schema files from sub directories. 
     * </p>
     */
    protected String[] lookupDirectories;

    /**
     * Creates the new schema location resolver.
     *
     * @param xsd The xsd to resolve filenames relative to.
     */
    public SchemaLocationResolver(XSD xsd) {
        this(xsd,new String[]{});
    }
    
    /**
     * Creates the new schema location resolver specifying additional directories to locate
     * schema files in.
     * <p>
     * The <tt>lookupDirectories</tt> parameter should be used in cases where a main schema imports
     * or includes files from sub directories. Consider the following schema file structure:
     * <pre>
     *   main.xsd
     *   dir1/
     *      include1.xsd
     *   dir2/
     *      include2.xsd
     * </pre>
     * 
     * The constructor would be called with:
     * <pre>
     * new SchemaLocationResolver(this,"include1","include2");
     * </pre>
     * 
     * </p>
     * @param xsd The xsd to resolve files relative to.
     * @param lookupDirectories Additional lookup directories relative to the xsd to lookup files in.
     */
    public SchemaLocationResolver(XSD xsd, String... lookupDirectories) {
        this.xsd = xsd;
        this.lookupDirectories = lookupDirectories;
    }

    /**
     * Determines if the locator can resolve the schema location for a particular 
     * namespace uri and schema location.
     * 
     * @return true if it can handle, otherwise false.
     */
    public boolean canHandle( XSDSchema schema, String uri, String location ) {
        if ( xsd.getNamespaceURI().equals(uri) ) {
            //try resolving directly
            URL xsdLocation = resolveLocationToResource( location );
            return xsdLocation != null;
        }
        
        return false;
    }
    
    private URL resolveLocationToResource( String location ) {
        //try to resolve it directly
        URL url = xsd.getClass().getResource( location );
        
        if ( url == null ) {
            //strip off the filename and do a resource lookup
            String fileName = new File(location).getName();
            url = xsd.getClass().getResource(fileName);
        }
        
        if ( url == null ) {
            //try resolving relative to lookupDirectories
            if ( lookupDirectories != null ) {
                for ( String lookup : lookupDirectories ) {
                    if ( lookup.endsWith( "/" ) ) {
                        lookup = lookup.substring(0,lookup.length()-1);
                    }
                    url = xsd.getClass().getResource( lookup + "/" + location );
                }
            }
        }
        
        return url;
    
    }
    
    /**
     * Resolves <param>location<param> to a physical location.
     * <p>
     * Resolution is performed by stripping the filename off of <param>location</param>
     * and looking up a resource located in the same package as the xsd.
     * </p>
     */
    public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
        if (location == null) {
            return null;
        }

        //if no namespace given, assume default for the current schema
        if (((uri == null) || "".equals(uri)) && (schema != null)) {
            uri = schema.getTargetNamespace();
        }

        //namespace match?
        if (canHandle(schema, uri, location)) {
            return resolveLocationToResource( location ).toString();
        }

        return null;
    }

    public String toString() {
        return xsd.toString();
    }
}
