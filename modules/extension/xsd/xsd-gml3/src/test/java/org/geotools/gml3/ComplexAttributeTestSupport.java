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
package org.geotools.gml3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Create ComplexAttributes to assist in testing bindings
 * 
 * @author Rob Atkinson, CSIRO Land and Water
 *
 * @source $URL$
 */

public abstract class ComplexAttributeTestSupport extends GML3TestSupport {

    /*
     * can extend this later to generate more generate complex attributes - lets start with
     * something concrete
     */
    public ComplexAttribute gmlCodeType(QName typeName, String value, String codeSpace) {
        Name myType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<Property>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

        Name attName = new NameImpl("codeSpace");
        // Name name, Class<?> binding, boolean isAbstract, List<Filter> restrictions,
        // PropertyType superType, InternationalString description
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null,
                null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, false, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(codeSpace, pd, null));

        p = new AttributeTypeImpl(new NameImpl("simpleContent"), String.class, false, false, null,
                null, null);
        AttributeDescriptor pd2 = new AttributeDescriptorImpl(p, new NameImpl("simpleContent"), 0,
                0, false, null);

        properties.add(new AttributeImpl(value, pd2, null));
        propertyDescriptors.add(pd2);

        ComplexTypeImpl at = new ComplexTypeImpl(myType, propertyDescriptors, false, false,
                Collections.EMPTY_LIST, null, null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, myType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }

    public ComplexAttribute gmlMeasureType(QName typeName, String value, String uom) {
        Name myType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<Property>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

        // assume attributes from same namespace as typename

        Name attName = new NameImpl("uom");
        // Name name, Class<?> binding, boolean isAbstract, List<Filter> restrictions,
        // PropertyType superType, InternationalString description
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null,
                null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, false, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(uom, pd, null));

        p = new AttributeTypeImpl(new NameImpl("simpleContent"), String.class, false, false, null,
                null, null);
        AttributeDescriptor pd2 = new AttributeDescriptorImpl(p, new NameImpl("simpleContent"), 0,
                0, false, null);

        properties.add(new AttributeImpl(value, pd2, null));
        propertyDescriptors.add(pd2);

        ComplexTypeImpl at = new ComplexTypeImpl(myType, propertyDescriptors, false, false,
                Collections.EMPTY_LIST, null, null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, myType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }

}
