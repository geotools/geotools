/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Some useful functions that was used within gt-wmts.
 *
 * <p>Might have their substitution elsewhere
 *
 * @author Roar Brænden
 */
public class WMTSHelper {

    /**
     * Performs a case-insensitive replacement of the first occurrence of {dimName} with dimValue within the baseUrl.
     * The curly braces are added by this function so dimName should not contain them. dimValue should be encoded using
     * {@link #encodeParameter(String)} before passing into this function
     *
     * @param baseUrl the string which contains {dimName}
     * @param dimName the value in baseurl that needs to be replaced. Should NOT have the curly braces around it
     * @param dimValue the encoded value to replace {dimName} with. If this is null then a blank string is used
     * @return baseUrl with {dimName} substituted with dimValue
     */
    public static String replaceToken(String baseUrl, String dimName, String dimValue) {
        String token = "{" + dimName.toLowerCase() + "}";
        int index = baseUrl.toLowerCase().indexOf(token);
        if (index != -1) {
            return baseUrl.substring(0, index)
                    + (dimValue == null ? "" : dimValue)
                    + baseUrl.substring(index + dimName.length() + 2);
        } else {
            return baseUrl;
        }
    }

    /**
     * Append the parameters to the baseUrl.
     *
     * <p>Add a ? mark if baseUrl doesn't have it.
     *
     * <p>Does not add existing parameters
     *
     * <p>If a value starts with {, it will avoid url-encoding
     */
    public static String appendQueryString(String baseUrl, Map<String, String> params) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("baseUrl cannot be null");
        }
        StringBuilder arguments = new StringBuilder();
        String separator = !baseUrl.contains("?") ? "?" : "&";

        String lowerBase = baseUrl.toLowerCase();
        try {
            for (String key : params.keySet()) {
                if (!lowerBase.contains(key.toLowerCase() + "=")) {
                    Object val = params.get(key);
                    if (val != null) {
                        String valString = val.toString();
                        arguments
                                .append(separator)
                                .append(key)
                                .append("=")
                                .append(valString.startsWith("{") ? valString : URLEncoder.encode(valString, "UTF-8"));
                        separator = "&";
                    }
                }
            }
            return baseUrl + arguments.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Doesn't support UTF-8", e);
        }
    }

    /**
     * Encodes a parameter using UTF-8 encoding (suitable for URLs). It will also replace spaces with %20 instead of +
     * (as raised in GEOT-4317) so that the encoded parameter can be used in any part of a URL
     *
     * @param name The parameter to be encoded
     * @return the encoded parameter
     */
    public static String encodeParameter(String name) {
        try {
            return URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Doesn't support UTF-8", e);
        }
    }
}
