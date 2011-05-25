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

package org.geotools.feature.xpath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.geotools.feature.Types;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;

/**
 * JXPath property handler that works on AttributeDescriptor/type
 * 
 * @author Gabriel Roldan
 *
 *
 * @source $URL$
 */
public class AttributeDescriptorPropertyHandler implements DynamicPropertyHandler {

	public String[] getPropertyNames(java.lang.Object o) {
		AttributeType att = (AttributeType) o;
		String[] propNames = null;
		if (att instanceof ComplexType) {
			ComplexType complexType = (ComplexType) att;
			List/*<AttributeDescriptor>*/ childTypes = 
				new ArrayList(complexType.getDescriptors());
			propNames = new String[childTypes.size()];
			for (int i = 0; i < propNames.length; i++) {
				propNames[i] = ((AttributeDescriptor)childTypes.get(i))
					.getName().getLocalPart();
			}
		}
		return propNames;
	}

	/**
	 * Returns the {@linkplain AttributeDescriptor} contained by the
	 * {@linkplain org.geotools.feature.iso.impl.ComplexAttributeImpl}
	 * <code>o</code>.
	 */
	public Object getProperty(Object o, String propName) {
	    ComplexType complex;
	    if (o instanceof AttributeDescriptor) {
	        AttributeDescriptor node = (AttributeDescriptor)o;
	        if(node.getName().getLocalPart().equals(propName)){
	            return node;
	        }
	        if(!(node.getType() instanceof ComplexType)){
	            throw new IllegalArgumentException("can't ask for property "
	                    + propName + " of a non complex type: " + node.getType());
	        }
	        complex = (ComplexType) node.getType();
	    } else if (o instanceof ComplexType) {
	        complex = (ComplexType) o;
	    } else {
	        throw new RuntimeException("Unexpected type passed to binding");
	    }
	    return Types.descriptor(complex, propName);
	}

	public void setProperty(Object feature, String propertyName, Object value) {
		throw new UnsupportedOperationException("not yet implemented");
		/*
		 * try { ((Feature) feature).setAttribute(propertyName, value); } catch
		 * (IllegalAttributeException e) { throw new JXPathException("Setting
		 * attribute " + propertyName + ": " + e.getMessage(), e); }
		 */
	}
}
