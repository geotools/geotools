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
package org.geotools.xml.handlers;

import java.net.URI;

import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Type;

/**
 * Utility class with methods to help implement Element and Type Handlers 
 * @author Jesse
 *
 * @source $URL$
 */
public class XMLTypeHelper {

	public static Element findChildElement(Type type, String localName, URI namespaceURI) {
		if( type instanceof ComplexType ){
			ComplexType complexType=(ComplexType) type;
			ElementGrouping child = complexType.getChild();
			if( child!=null){
				Element found=child.findChildElement(localName, namespaceURI);
				if( found!=null )
					return found;
			}
			Element[] children = complexType.getChildElements();
			if( children==null || children.length==0)
				return null;
			for (int i = 0; i < children.length; i++) {
				Element element = children[i];
				if( localName.equals(element.getName()) && namespaceURI.equals(element.getNamespace()) )
					return element;
			}
			if( complexType.getParent()!=null ){
				Type parent = complexType.getParent();
				return findChildElement((ComplexType) parent, localName, namespaceURI);
			}
		}else{
			return type.findChildElement(localName);
		}
		return null;
	}

}
