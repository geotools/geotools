/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.type;

import org.geotools.feature.AttributeType;
import org.opengis.filter.Filter;

/**
 * This level of inheritance is intended for documentation purposes ... 
 * so read carefully :). 
 * 
 * A nested Attribute is equivalent to a weak feature, or a feature without a Fid.
 * This distinction is important because there is very little difference between 
 * a Java Bean and a nested Attribute, except the existance of this class. 
 * This class is a simplified version of the XML schema which would be used to 
 * represent this object as XML. 
 * 
 * The other subtle divergence between a Nested Attribute, and a Feature is the 
 * idea that a Feature can be translated into and out of GML, while a Nested 
 * Attribute is really just a typed Object. Much of what is represented within 
 * the Nested Attribute and NestedAttributeType can, and is duplicated in many 
 * bean2xml and xml2bean libraries (they use introspection ... we declare it in 
 * a human readable form).
 * 
 * @author dzwiers
 *
 * @source $URL$
 * 
 * @deprecated Will be removed in geotools 2.6.
 */
public class NestedAttributeType extends ListAttributeType {

	/**
	 * super(copy)
	 * @param copy
	 */
	public NestedAttributeType(NestedAttributeType copy) {
		super(copy);
	}

	/**
	 * @param name
	 * @param nillable
	 * @param min
	 * @param max
	 * @param children
	 */
	public NestedAttributeType(String name, boolean nillable, int min, int max,
			AttributeType[] children, Filter restriction) {
		super(name, nillable, min, max, children, restriction);
	}

	/**
	 * @param name
	 * @param nillable
	 * @param children
	 */
	public NestedAttributeType(String name, boolean nillable,
			AttributeType[] children) {
		super(name, nillable, children);
	}

}
