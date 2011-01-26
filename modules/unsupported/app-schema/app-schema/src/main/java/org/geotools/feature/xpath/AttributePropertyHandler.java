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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.geotools.feature.Types;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.xml.sax.Attributes;

/**
 * JXPath property handler that works on Attribute.
 * 
 * @author Gabriel Roldan
 * @author Justin Deoliveira
 *
 * @source $URL$
 */
public class AttributePropertyHandler implements DynamicPropertyHandler {

    public String[] getPropertyNames(Object o) {
        Attribute att = (Attribute) o;

        // we only work on complex attributes
        if (att instanceof ComplexAttribute) {

            ComplexType type = (ComplexType) att.getType();
            Collection attributes = type.getDescriptors();

            String[] propNames = new String[attributes.size()];
            int i = 0;
            for (Iterator itr = attributes.iterator(); itr.hasNext(); i++) {
                AttributeDescriptor descriptor = (AttributeDescriptor) itr.next();

                // JD: this ignores namespaces
                propNames[i] = descriptor.getName().getLocalPart();
            }

            return propNames;
        }

        return null;
    }

    public Object getProperty(Object o, String propName) {
        Object value = null;

        Attribute att = (Attribute)o;
        
        // the Filter spec says the xpath expresion may or may not
        // start with the Feature name. If it does, it is the self
        // location path
        AttributeDescriptor descriptor = att.getDescriptor();
        String attName;
        if (descriptor == null) {
            attName = att.getType().getName().getLocalPart();
        } else {
            attName = descriptor.getName().getLocalPart();
        }
        if (propName.equals(attName) || propName.startsWith(attName + "/")) {
            return o;
        }

        if (o instanceof ComplexAttribute) {
            ComplexAttribute attribute = (ComplexAttribute) o;
            Name name = Types.typeName(propName);
            Collection<Property> found;
            try {
                found = attribute.getProperties(name);
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw e;
            }
            value = found.size() == 0 ? null : (found.size() == 1 ? found.iterator().next() : found);

            // FIXME HACK: this is due to the Filter subsystem not dealing with
            // PropertyHandler returning attribute, hence can't, for example,
            // compare
            // an Attribute with a Literal
            /*
            if (value instanceof Attribute && !(value instanceof ComplexAttribute)) {
                value = ((Attribute) value).get();
            }
            */
        }
        
         if (value == null && descriptor != null) {
            if ("id".equals(propName)) {
                value = att.getIdentifier();
            } else {
                String[] scopedAttName = propName.split(":");
                attName = scopedAttName[scopedAttName.length - 1];

                Map attributes = (Map) att.getUserData().get(Attributes.class);
                if (attributes != null) {
                    for (Iterator it = attributes.entrySet().iterator(); it.hasNext();) {
                        Map.Entry entry = (Entry) it.next();
                        Name key = (Name) entry.getKey();
                        if (attName.equals(key.getLocalPart())) {
                            value = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return value;
    }

    public void setProperty(Object att, String name, Object value) {
        // Attribute attribute = (Attribute)att;
        // if (att instanceof ComplexAttribute) {
        //			
        // }
        // else {
        // //just set the value
        //			
        // }
        //		
        // if(!(attribute instanceof ComplexAttribute)){
        // if(!propertyName.equals(attribute.getType().getName().getLocalPart())){
        // throw new IllegalArgumentException("only self reference to type
        // allowed for simple attributes");
        // }
        // attribute.set(value);
        // }else{
        // ComplexAttribute complex = (ComplexAttribute)attribute;
        // List/*<Attribute>*/atts = complex.get(new
        // org.geotools.util.AttributeName(propertyName));
        // if(atts.size() == 0){
        // throw new IllegalArgumentException("No attributes of type " +
        // propertyName + " found");
        // }
        // ((Attribute)atts.get(0)).set(value);
        // }
    }
}
