/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

/**
 * Perform basic SQL validation on input string.  This is to allow safe encoding
 * of parameters that must contain quotes, while still protecting users from SQL
 * injection.
 * 
 * We prevent SQL from breaking out of quotes by replacing any quotes in input 
 * stream with double quotes.  Backslashes are too risky to allow so are removed
 * completely 
 */
public class EscapeSql {
    public static String escapeSql(String str) {
        
        // ' --> ''
        str = str.replaceAll("'", "''");
        
        // " --> ""
        str = str.replaceAll("\"", "\"\"");
        
        // \ -->  (remove backslashes)
        str = str.replaceAll("\\\\", "");
        return str;
    }
}
