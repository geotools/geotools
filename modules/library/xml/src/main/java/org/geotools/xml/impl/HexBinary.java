/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*
 * Copyright 2003, 2004  The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.geotools.xml.impl;

/** Utility class for xs:hexbinary. */
public class HexBinary {
    /** Creates a clone of the given byte array. */
    public static byte[] getClone(byte[] pHexBinary) {
        byte[] result = new byte[pHexBinary.length];
        System.arraycopy(pHexBinary, 0, result, 0, pHexBinary.length);
        return result;
    }

    /** Converts the string <code>pValue</code> into an array of hex bytes. */
    public static byte[] decode(String pValue) {
        if (pValue.length() % 2 != 0) {
            throw new IllegalArgumentException("A HexBinary string must have even length.");
        }
        byte[] result = new byte[pValue.length() / 2];
        int j = 0;
        for (int i = 0; i < pValue.length(); ) {
            byte b;
            char c = pValue.charAt(i++);
            char d = pValue.charAt(i++);
            if (c >= '0' && c <= '9') {
                b = (byte) (c - '0' << 4);
            } else if (c >= 'A' && c <= 'F') {
                b = (byte) (c - 'A' + 10 << 4);
            } else if (c >= 'a' && c <= 'f') {
                b = (byte) (c - 'a' + 10 << 4);
            } else {
                throw new IllegalArgumentException("Invalid hex digit: " + c);
            }
            if (d >= '0' && d <= '9') {
                b += (byte) (d - '0');
            } else if (d >= 'A' && d <= 'F') {
                b += (byte) (d - 'A' + 10);
            } else if (d >= 'a' && d <= 'f') {
                b += (byte) (d - 'a' + 10);
            } else {
                throw new IllegalArgumentException("Invalid hex digit: " + d);
            }
            result[j++] = b;
        }
        return result;
    }

    /** Converts the byte array <code>pHexBinary</code> into a string. */
    public static String encode(byte[] pHexBinary) {
        StringBuffer result = new StringBuffer();
        for (byte b : pHexBinary) {
            byte c = (byte) ((b & 0xf0) >> 4);
            if (c <= 9) {
                result.append((char) ('0' + c));
            } else {
                result.append((char) ('A' + c - 10));
            }
            c = (byte) (b & 0x0f);
            if (c <= 9) {
                result.append((char) ('0' + c));
            } else {
                result.append((char) ('A' + c - 10));
            }
        }
        return result.toString();
    }
}
