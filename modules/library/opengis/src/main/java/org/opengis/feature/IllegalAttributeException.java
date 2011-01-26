/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

import org.opengis.feature.type.AttributeDescriptor;

/**
 * Indicates a validation check has failed; the provided descriptor and value are available via this
 * exception.
 * 
 * @author Jody Garnett (Refractions Research, Inc.)
 * @since GeoAPI 2.2
 */
public class IllegalAttributeException extends IllegalArgumentException {
    private static final long serialVersionUID = 3373066465585246605L;

    /**
     * AttributeDescriptor being used to validate against.
     */
    final private AttributeDescriptor descriptor;

    /**
     * Object that failed validation.
     */
    final private Object value;

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value) {
        super();
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message) {
        super(message);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message,
            Throwable t) {
        super(message, t);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, Throwable t) {
        super(t);
        this.descriptor = descriptor;
        this.value = value;
    }

    /**
     * AttribtueDescriptor being checked against.
     * 
     * @return AttributeDescriptor being checked.
     */
    public AttributeDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Attribute value that failed validation.
     * 
     * @return Attribute value
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        
        StringBuffer buf = new StringBuffer();
        buf.append(s);
        if( message != null){
            buf.append(":");
            buf.append(message);
        }
        if( descriptor != null ){
            buf.append(":");
            buf.append( descriptor.getName() );
        }
        buf.append(" value:");
        buf.append( value );
        
        return buf.toString();
    }
}
