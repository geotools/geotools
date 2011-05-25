/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.io;

import org.geotools.data.vpf.ifc.DataTypesDefinition;


/**
 * RowField.java Created: Mon Jan 27 13:58:34 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * @version $Id$
 */
public class RowField extends Number implements DataTypesDefinition {
    /**
     * Describe variable <code>value</code> here.
     *
     */
    private Object value = null;

    /**
     * Describe variable <code>type</code> here.
     *
     */
    private char type = CHAR_NULL_VALUE;

    /**
     * Creates a new <code><code>RowField</code></code> instance.
     *
     * @param value an <code><code>Object</code></code> value
     * @param type a <code><code>char</code></code> value
     */
    public RowField(Object value, char type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Method <code>toString</code> is used to perform 
     *
     * @return a <code><code>String</code></code> value
     */
    public String toString() {
        if (value != null) {
            return value.toString().trim();
        } else {
            return null;
        }
    }

    /**
     * Method <code>equals</code> is used to perform 
     *
     * @param obj an <code><code>Object</code></code> value
     * @return a <code><code>boolean</code></code> value
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof RowField)) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Method <code>getType</code> is used to perform 
     *
     * @return a <code><code>char</code></code> value
     */
    public char getType() {
        return type;
    }

    /**
     * Method <code>getValue</code> is used to perform 
     *
     * @return an <code><code>Object</code></code> value
     */
    public Object getValue() {
        return value;
    }

    

    /* (non-Javadoc)
     * @see java.lang.Number#intValue()
     */
    public int intValue() {
        return ((Number) value).intValue();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#longValue()
     */
    public long longValue() {
        return ((Number) value).longValue();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#byteValue()
     */
    public byte byteValue() {
        return ((Number) value).byteValue();
    }
    /* (non-Javadoc)
     * @see java.lang.Number#shortValue()
     */
    public short shortValue() {
        return ((Number) value).shortValue();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#floatValue()
     */
    public float floatValue() {
        return ((Number) value).floatValue();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#doubleValue()
     */
    public double doubleValue() {
        return ((Number) value).doubleValue();
    }
    
}
