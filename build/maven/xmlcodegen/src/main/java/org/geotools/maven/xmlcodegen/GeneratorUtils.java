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

package org.geotools.maven.xmlcodegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xsd.Schemas;

/**
 * Utility class used for generating.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeneratorUtils {

	/**
	 * Returns all the types defined in <param>schema</param>, including anonymous complex
	 * types.
	 * 
	 * @param schema the schema.
	 * 
	 * @return A list of types, including anonymous complex types.
	 */
	public static List allTypes( XSDSchema schema ) {
		//get the named types
		List<XSDTypeDefinition> types = new ArrayList<>( schema.getTypeDefinitions() );
    	
		//get anonymous types
		List<XSDTypeDefinition> anonymous = new ArrayList<>();
		
		//add any anonymous types of global elements + attributes
        for (XSDElementDeclaration element : schema.getElementDeclarations()) {
            if (element.getAnonymousTypeDefinition() != null) {
                element.getAnonymousTypeDefinition().setName("_" + element.getName());
                anonymous.add(element.getAnonymousTypeDefinition());
            }
        }
        for (XSDAttributeDeclaration attribute : schema.getAttributeDeclarations()) {
            if (attribute.getAnonymousTypeDefinition() != null) {
                attribute.getAnonymousTypeDefinition().setName("_" + attribute.getName());
                anonymous.add(attribute.getAnonymousTypeDefinition());
            }
        }
    	//add any anonymous types foudn with type definitions
        for (XSDTypeDefinition type : types) {
            List particles = Schemas.getChildElementParticles(type, false);
            for (Object o : particles) {
                XSDParticle particle = (XSDParticle) o;
                XSDElementDeclaration element = (XSDElementDeclaration) particle.getContent();

                //ignore element references, caught in the above loop
                if (element.isElementDeclarationReference())
                    continue;

                if (element.getAnonymousTypeDefinition() != null) {
                    element.getAnonymousTypeDefinition().setName(type.getName() + "_" + element.getName());
                    anonymous.add(element.getAnonymousTypeDefinition());
                }
            }
        }
    	
    	types.addAll( anonymous );
    	return types;
	}
	
	/**
     * Returns all the types defined in <param>schema</param> whose names are 
     * included in the specified set of names.
     * <p>
     * If <tt>names</tt> is null or empty all types are returned.
     * </p>
     * @param schema the schema.
     * @param names the names of types to include.
     * 
     * @return A list of types, including anonymous complex types.
     */
	public static List types( XSDSchema schema, Set names ) {
	    List allTypes = allTypes( schema );
	    if ( names != null && !names.isEmpty() ) {
	        for (Iterator t = allTypes.iterator(); t.hasNext(); ) {
	            XSDTypeDefinition type = (XSDTypeDefinition) t.next();
	            if ( type.getName() != null && names.contains(type.getName())) {
	                continue;
	            }
	            
	            t.remove();
	        }
	    }
	    
	    return allTypes;
	}
}
