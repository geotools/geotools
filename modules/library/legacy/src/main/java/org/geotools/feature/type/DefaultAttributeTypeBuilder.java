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
package org.geotools.feature.type;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultAttributeType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;

public class DefaultAttributeTypeBuilder extends AttributeTypeBuilder {

	public DefaultAttributeTypeBuilder() {
		super( new DefaultTypeFactory() );
	}

	private static class DefaultTypeFactory extends FeatureTypeFactoryImpl {
		public AttributeDescriptor createAttributeDescriptor(AttributeType type, Name name, int minOccurs, int maxOccurs, boolean isNillable, Object defaultValue) {
			return new DefaultAttributeType( type, name, minOccurs, maxOccurs, isNillable,defaultValue );
		}
		
		public GeometryDescriptor createGeometryDescriptor(GeometryType type,
		        Name name, int minOccurs, int maxOccurs, boolean isNillable,
		        Object defaultValue) {
		    return new GeometricAttributeType(type,name,minOccurs,maxOccurs,isNillable,defaultValue);
		}
	}
}
