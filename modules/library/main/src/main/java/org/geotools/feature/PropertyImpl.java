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
package org.geotools.feature;

import java.util.HashMap;
import java.util.Map;

import org.geotools.util.Utilities;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * Implementation of Property.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public abstract class PropertyImpl implements Property {
    /**
     * content of the property
     */
    protected Object value;
    /**
     * descriptor of the property
     */
    protected PropertyDescriptor descriptor;
    /**
     * user data
     */
    protected final Map<Object,Object> userData;

    protected PropertyImpl( Object value, PropertyDescriptor descriptor ) {
        this.value = value;
        this.descriptor = descriptor;
        userData = new HashMap<Object, Object>();
        
        if ( descriptor == null ) {
            throw new NullPointerException("descriptor");
        }
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public PropertyDescriptor getDescriptor() {
        return descriptor;
    }

    public Name getName() {
        return getDescriptor().getName();
    }

    public PropertyType getType() {
        return getDescriptor().getType();
    }

    public boolean isNillable() {
        return getDescriptor().isNillable();
    }
    
    public Map<Object, Object> getUserData() {
        return userData;
    }
    
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        
        if (!(obj instanceof PropertyImpl)) {
            return false;
        }

        PropertyImpl other = (PropertyImpl) obj;

        if (!Utilities.equals(descriptor, other.descriptor))
            return false;

        if (!Utilities.deepEquals(value, other.value))
            return false;   
    
        return true;
    }
    
    public int hashCode() {
        return 37 * descriptor.hashCode()
            + (37 * (value == null ? 0 : value.hashCode()));
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName()).append(":");
        sb.append(getDescriptor().getName().getLocalPart());
        sb.append("<");
        sb.append(getDescriptor().getType().getName().getLocalPart());
        sb.append(">=");
        sb.append(value);

        return sb.toString();
    }
    
}
