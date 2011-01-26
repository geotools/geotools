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
package org.geotools.xml.gml;

import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

/**
 * A temporary measure representing a choice between several bindings.
 * <p> 
 * Created for GML generated FeatureTypes.  Represents a Choice type.  This is a
 * pale, weak imitation of the real thing and as soon as the new FeatureModel
 * comes around it will be thrown away.
 * 
 * This is temporary and only for use by the parser.  It should never be public or in common use.
 * 
 * @author Jesse
 */
interface ChoiceAttributeType extends AttributeDescriptor, AttributeType {
    
	/**
	 * Gets the choices
	 * @return the potential types
	 */
	public Class[] getChoices() ;
	
	/**
	 * Yet another hack in order to use our FeatureType.  Converts object to the "real" attribute type.  
	 * 
	 * Best to explain by Example.
	 * <p>
	 *  Consider a choice between Polygon and MultiPolygon.  It doesn't make sense
	 *  to be a Geometry Attribute Type since normal attribute type inspection will allow
	 *  users/clients to set Points and lines as legal attribute.  For parsing purposes,
	 *  this make the FeatureType a little more accurate.  It is not perfect of course since
	 *  The choice Line, Polygon and MultiPolygon still has to be of Type Geometry but it is a little better.
	 *  </p>
	 */
	public Object convert(Object obj);
}
