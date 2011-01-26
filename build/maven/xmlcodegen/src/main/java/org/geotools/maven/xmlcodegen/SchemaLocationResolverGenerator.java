/*
 *    GeoTools - The Open Source Java GIS Tookit
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
package org.geotools.maven.xmlcodegen;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.Schemas;


/**
 * Generates an instance of {@link org.eclipse.xsd.util.XSDSchemaLocationResolver} for
 * a particular schema.
 * <p>
 * The schema supplied, and any included schemas ( not imported ), are added to
 * the set of schemas that the resulting class can resolve.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class SchemaLocationResolverGenerator extends AbstractGenerator {
    public void generate(XSDSchema schema)  {
        ArrayList includes = new ArrayList();
        ArrayList namespaces = new ArrayList();

        File file = null;
        try {
        	file = findSchemaFile( schema.getSchemaLocation() );	
        }
        catch( Exception e ) {
        	logger.log( Level.SEVERE, "", e );
        }
        
        if ( file != null ) {
        	includes.add(file);
        	namespaces.add(schema.getTargetNamespace());
        }
        else {
        	logger.log( Level.SEVERE, "Could not find: " + schema.getSchemaLocation() + " to copy." );
        }
        
        List included = Schemas.getIncludes(schema);

        for (Iterator i = included.iterator(); i.hasNext();) {
            XSDInclude include = (XSDInclude) i.next();
            
            file = null;
            try {
            	file = findSchemaFile( include.getSchemaLocation() );
            }
            catch( Exception e ) {
            	logger.log( Level.SEVERE, "", e );
            }
            
			if ( file != null ) {
				includes.add(file);
				if( include.getSchema() != null ) {
					namespaces.add(include.getSchema().getTargetNamespace());	
				}
				else {
					namespaces.add( schema.getTargetNamespace() );
				}
			}
			else {
				logger.log( Level.SEVERE, "Could not find: " + include.getSchemaLocation() + " to copy." );
			}
			
        }

        try {
//			String result = execute("SchemaLocationResolverTemplate",
//			        new Object[] { schema, includes, namespaces });
//			String prefix = Schemas.getTargetPrefix(schema).toUpperCase();
//			write(result, prefix + "SchemaLocationResolver");

			//copy over all the schemas
			for (Iterator i = includes.iterator(); i.hasNext();) {
			    File include = (File) i.next();
			    copy(include, resourceLocation);
			}
		}
        catch( Exception e ) {
        	logger.log( Level.SEVERE, "Error generating resolver", e );
        }
    }

}
