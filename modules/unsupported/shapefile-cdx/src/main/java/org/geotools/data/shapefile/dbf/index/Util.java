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

/**
 * Helper methods to manage byte-streams of data.
 * 
 * @author Alvaro Huarte
 */
class Util {
    
    private static boolean x86Architecture = (System.getProperty("os.arch").indexOf("86")==0 && System.getProperty("os.arch").compareTo("vax")!=0);
    
    /**
     * Gets the string from the specified null-terminated byte stream.
     */
    public static String getString(byte bytes[], int startIndex, int byteCount, Charset charset, boolean trimRight) {
        
        while (byteCount>0) if (bytes[startIndex+byteCount-1]==0) byteCount--; else break;
        if (trimRight) while (byteCount>0) if (bytes[startIndex+byteCount-1]==32) byteCount--; else break;
        
        return byteCount>0 ? new String(bytes, startIndex, byteCount, charset) : "";
    }
    
    /**
     * Gets the integer from the specified byte stream in Big Endian byte-order ('first byte is the least significant byte').
     */
    public static int getIntegerBE(byte[] bytes, int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            value += ((long)bytes[startIndex+i] & 0xffL) << (8 * i);
        }
        return (int)value;
    }
    /**
     * Gets the integer from the specified byte stream in Little Endian byte-order ('first byte the most significant byte').
     */
    public static int getIntegerLE(byte[] bytes, int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            value = (value << 8) | (bytes[startIndex+i] & 0xFF);
        }
        return (int)value;
    }

    /**
     * Gets the integer from the specified byte stream (applying NOT bitwise operator) in Big Endian byte-order ('first byte is the least significant byte').
     */
    private static int getIntegerNotBE(byte[] bytes, int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            value += ((long)~bytes[startIndex+i] & 0xffL) << (8 * i);
        }
        return (int)value;
    }
    /**
     * Gets the integer from the specified byte stream (applying NOT bitwise operator) in Little Endian byte-order ('first byte the most significant byte').
     */
    private static int getIntegerNotLE(byte[] bytes, int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            value = (value << 8) | (~bytes[startIndex+i] & 0xFF);
        }
        return (int)value;
    }

    /**
     * Gets the Double from the specified byte stream in Big Endian byte-order ('first byte is the least significant byte').
     */
    public static double getDoubleBE(byte[] bytes, int startIndex, int byteCount) {
        
        boolean negativeVal = (bytes[startIndex]&0x80)!=0x80;
        double value;
        long upper, lower;
        
        if (byteCount < 8) {
            byte[] b = new byte[8];
            for (int i = 0; i < byteCount; i++) b[i] = bytes[startIndex+i];
            startIndex = 0;
            byteCount = 8;
            bytes = b;
        }
        
        if (negativeVal) {
            upper = Util.getIntegerNotBE(bytes, startIndex, 4);
            startIndex += 4;
            lower = Util.getIntegerNotBE(bytes, startIndex, 4);
            
            value = Double.longBitsToDouble((upper << 32) + (lower & 0xffffffffl));
        }
        else {
            upper = Util.getIntegerBE(bytes, startIndex, 4);
            startIndex += 4;
            lower = Util.getIntegerBE(bytes, startIndex, 4);
            
            value =-Double.longBitsToDouble((upper << 32) + (lower & 0xffffffffl));
        }
        return value;
    }
    /**
     * Gets the double from the specified byte stream in Little Endian byte-order ('first byte the most significant byte').
     */
    public static double getDoubleLE(byte[] bytes, int startIndex, int byteCount) {
        
        boolean negativeVal = (bytes[startIndex]&0x80)!=0x80;
        double value;
        long upper, lower;
        
        if (byteCount < 8) {
            byte[] b = new byte[8];
            for (int i = 0; i < byteCount; i++) b[i] = bytes[startIndex+i];
            startIndex = 0;
            byteCount = 8;
            bytes = b;
        }
        
        if (negativeVal) {
            upper = Util.getIntegerNotLE(bytes, startIndex, 4);
            startIndex += 4;
            lower = Util.getIntegerNotLE(bytes, startIndex, 4);
            
            value = Double.longBitsToDouble((upper << 32) + (lower & 0xffffffffl));
        }
        else {
            upper = Util.getIntegerLE(bytes, startIndex, 4);
            startIndex += 4;
            lower = Util.getIntegerLE(bytes, startIndex, 4);
            
            value =-Double.longBitsToDouble((upper << 32) + (lower & 0xffffffffl));
        }
        return value;
    }
    
    /**
     * Converts the specified value to x86 architecture.
     */
    public static short x86(short in) {
        
        if (x86Architecture)
            return in;
        
        short is, save = in;
        boolean negative = false;
        int first, second;
        if (in < 0) {
            negative = true;
            in &= 0x7fff;
        }
        first = in >>> 8;
        if (negative) first |= 0x80;
        second = save & 0x00ff;
        is = (short) ((second << 8) + first);
        return is;
    }
    
    /**
     * Converts the specified value to x86 architecture. 
     */
    public static int x86(int in) {
        
        if (x86Architecture)
            return in;
        
        boolean negative = false;
        int is;
        int first, second, third, fourth, save;
        save = in;
        if (in < 0) {
            negative = true;
            in &= 0x7fffffff;
        }
        first = in >>> 24;
        if (negative) first |= 0x80;
        in = save & 0x00ff0000;
        second = in >>> 16;
        in = save & 0x0000ff00;
        third = in >>> 8;
        fourth = save & 0x000000ff;
        is = (fourth << 24) + (third << 16) + (second << 8) + first;
        return is;
    }
}
