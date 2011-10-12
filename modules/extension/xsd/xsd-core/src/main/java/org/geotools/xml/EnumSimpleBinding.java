package org.geotools.xml;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

/**
 * Parses a simple type into an exiting enum.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class EnumSimpleBinding extends AbstractSimpleBinding {

    Class enumClass;
    QName target;
    
    Method get;
    Method valueOf;
    
    public EnumSimpleBinding(Class enumClass, QName target) {
        this.enumClass = enumClass;
        this.target = target;
        
        try {
            get = enumClass.getMethod("get", String.class);
        } 
        catch(Exception e) {}
        
        try {
            valueOf = enumClass.getMethod("valueOf", String.class);
        } 
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public QName getTarget() {
        return target;
    }

    public Class getType() {
        return enumClass;
    }

    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        Object result = get(value.toString());
        if (result == null) {
            //try converting to uppercase
            result = get(value.toString().toUpperCase());
        }
        return result;
    }
    
    Object get(String value) throws Exception {
        if (get != null){
            return get.invoke(null, value);
        }
        
        return valueOf.invoke(null, value);
    }
}
