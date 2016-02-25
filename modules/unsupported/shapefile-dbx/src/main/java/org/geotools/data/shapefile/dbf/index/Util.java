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
    
    /**
     * Gets the string from the specified null-terminated byte stream.
     */
    public static String getString(byte bytes[], int startIndex, int byteCount, Charset charset, boolean trimRight) {
        
        while (byteCount>0) if (bytes[startIndex+byteCount-1]==0) byteCount--; else break;
        if (trimRight) while (byteCount>0) if (bytes[startIndex+byteCount-1]==32) byteCount--; else break;
        
        return byteCount>0 ? new String(bytes, startIndex, byteCount, charset) : "";
    }
}
