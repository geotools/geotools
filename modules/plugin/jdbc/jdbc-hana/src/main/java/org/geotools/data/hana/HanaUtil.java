/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

/**
 * Various utility methods for SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public final class HanaUtil {

    /**
     * Encodes an identifier.
     *
     * @param id The identifier to encode. Must not be null.
     * @return Returns the encoded identifier.
     */
    public static String encodeIdentifier(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < id.length(); ++i) {
            char c = id.charAt(i);
            if (c != '"') {
                sb.append(c);
            } else {
                sb.append("\"\"");
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * Converts a string to a SQL string literal by quoting it with single quotes.
     *
     * @param s The string to convert.
     * @return Returns the string literal.
     */
    public static String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c != '\'') {
                sb.append(c);
            } else {
                sb.append("''");
            }
        }
        sb.append('\'');
        return sb.toString();
    }

    private HanaUtil() {}
}
