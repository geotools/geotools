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

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.geotools.data.shapefile.dbf.index.NodeKey;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultJulianDate;
import org.geotools.temporal.reference.DefaultCalendar;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.opengis.temporal.CalendarDate;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.JulianDate;
import org.opengis.temporal.TemporalReferenceSystem;

/**
 * Key entry of a node in a Dbase CDX-index file.
 * 
 * @author Alvaro Huarte
 */
class CdxNodeKey implements NodeKey {
    
    static {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Julian calendar");
        julianReferenceSystem = new DefaultTemporalReferenceSystem(name, null);
        julianCalendar = new DefaultCalendar(name, null);
    }
    private static TemporalReferenceSystem julianReferenceSystem;
    private static DefaultCalendar julianCalendar;
    
    private Class<?> bindingValue;
    private Object keyValue;
    private int keyLength;
    
    /**
     * Creates a new CdxNodeKey object.
     */
    public CdxNodeKey(Class<?> bindingValue, int keyLength, Object keyValue) {
        this.bindingValue = bindingValue;
        this.keyLength = keyLength;
        this.keyValue = keyValue;
    }
    
    /**
     * Returns the binding value of the NodeKey.
     */
    public Class<?> getBinding() {
        return bindingValue;
    }
    /**
     * Returns the length of the NodeKey (Maximum number of bytes of the key).
     */
    public int getKeyLength() {
        return keyLength;
    }
    /**
     * Returns the key value of the NodeKey.
     */
    public Object getValue() {
        return keyValue;
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
     * Creates a new CdxNodeKey from the specified byte-stream.
     */
    public static CdxNodeKey createKey(Class<?> binding, Charset charset, Calendar calendar, byte[] bytes, int startIndex, int byteCount) {
        
        if (binding==String.class) {
            String value = Util.getString(bytes, startIndex, byteCount, charset, true);
            return new CdxNodeKey(binding, byteCount, value);
        }
        else
        if (binding==Integer.class) {
            double value = Util.getDoubleLE(bytes, startIndex, byteCount);
            long longVal = (long)value;
            if (longVal>Integer.MIN_VALUE && longVal<Integer.MAX_VALUE) return new CdxNodeKey(binding, byteCount, (int)longVal); else return new CdxNodeKey(binding, byteCount, longVal);
        }
        else
        if (binding==Long.class) {
            double value = Util.getDoubleLE(bytes, startIndex, byteCount);
            return new CdxNodeKey(binding, byteCount, (long)value);
        }
        else
        if (binding==Double.class) {
            double value = Util.getDoubleLE(bytes, startIndex, byteCount);
            return new CdxNodeKey(binding, byteCount, value);
        }
        else
        if (binding==Date.class) {
            double value = Util.getDoubleLE(bytes, startIndex, byteCount);
            JulianDate jdt = new DefaultJulianDate(julianReferenceSystem, IndeterminateValue.UNKNOWN, value);
            CalendarDate cdt = julianCalendar.julTrans(jdt);
            int[] tvalue = cdt.getCalendarDate();
            calendar.clear(Calendar.MILLISECOND);
            calendar.set(tvalue[0], tvalue[1]-1, tvalue[2], 0, 0, 0);
            return new CdxNodeKey(binding, byteCount, calendar.getTime());
        }
        else
        if (binding==Boolean.class) {
            String value = Util.getString(bytes, startIndex, byteCount, charset, true);
            return new CdxNodeKey(binding, byteCount, !(value.length()==0 || value.equalsIgnoreCase("F")));
        }
        else {
            String value = Util.getString(bytes, startIndex, byteCount, charset, true);
            return new CdxNodeKey(binding, byteCount, value);
        }
    }
    /**
     * Creates a new CdxNodeKey from the specified byte-stream.
     */
    public static CdxNodeKey createKey(Class<?> binding, Charset charset, TimeZone timeZone, byte[] bytes, int startIndex, int byteCount) {
        return createKey(binding, charset, Calendar.getInstance(timeZone, Locale.US), bytes, startIndex, byteCount);
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "Value='" + (keyValue!=null ? keyValue.toString() : "<null>") + "', Binding=" + bindingValue.getSimpleName();
    }
}
