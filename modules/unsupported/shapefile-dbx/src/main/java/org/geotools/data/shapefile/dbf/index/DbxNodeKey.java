/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Date;

import org.geotools.data.shapefile.dbf.index.NodeKey;

/**
 * Key entry of a node in a Dbase DBX-index file.
 * 
 * @author Alvaro Huarte
 */
class DbxNodeKey implements NodeKey {
    
    private Class<?> bindingValue;
    private Object keyValue;
    
    /**
     * Creates a new CdxNodeKey object.
     */
    public DbxNodeKey(Class<?> bindingValue, Object keyValue) {
        this.bindingValue = bindingValue;
        this.keyValue = keyValue;
    }
    
    /**
     * Returns the binding value of the NodeKey.
     */
    public Class<?> getBinding() {
        return bindingValue;
    }
    /**
     * Returns the key value of the NodeKey.
     */
    public Object getValue() {
        return keyValue;
    }
    /**
     * Sets the key value of the NodeKey.
     */
    public void setValue(Object value) {
        keyValue = value;
    }

    /**
     * Write a string to the specified file.
     * @throws IOException
     */
    public static int writeString(RandomAccessFile file, String value, int fitToLength, Charset charset) throws IOException {
        byte[] bytes = value.getBytes(charset);
        file.write(bytes);
        int size = bytes.length;
        for (int i = 0; i < fitToLength-bytes.length; i++) { file.writeByte(0); size++; }
        return size;
    }
    /**
     * Write a value to the specified file.
     * @throws IOException
     */
    public static int writeKey(RandomAccessFile file, Class<?> binding, Object value, Charset charset) throws IOException {
        if (binding==Integer.class) {
            file.writeInt(((Number)value).intValue());
            return 4;
        }
        else
        if (binding==Long.class) {
            file.writeLong(((Number)value).longValue());
            return 8;
        }
        else
        if (binding==Double.class) {
            file.writeDouble(((Number)value).doubleValue());
            return 8;
        }
        else
        if (binding==Date.class) {
            file.writeLong(((java.util.Date)value).getTime());
            return 8;
        }
        else
        if (binding==Boolean.class) {
            file.writeBoolean((boolean)value);
            return 1;
        }
        else
        if (value!=null) {
            byte[] bytes = value.toString().getBytes(charset);
            file.writeShort(bytes.length);
            if (bytes.length > 0) file.write(bytes);
            return 2 + bytes.length;
        }
        return 0;
    }
    
    /**
     * Returns a hash code value for the object. 
     * This method is supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     */
    @Override
    public int hashCode() {
        return keyValue!=null ? keyValue.hashCode() : super.hashCode();
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object otherObject) {
        return (otherObject instanceof NodeKey) && equals((NodeKey)otherObject);
    }
    /**
     * Indicates whether some other NodeKey is "equal to" this one.
     */
    public boolean equals(NodeKey otherKey) {
        Object otherValue = otherKey.getValue();
        if (keyValue!=null && otherValue==null) return false;
        if (keyValue==null && otherValue!=null) return false;
        if (keyValue==null && otherValue==null) return true;
        return keyValue.equals(otherValue);
    }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "Value='" + (keyValue!=null ? keyValue.toString() : "<null>") + "', Binding=" + bindingValue.getSimpleName();
    }
}
