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

    /** Replaces first occurence of {dimName} with dimValue within the baseUrl */
    public static String replaceToken(String baseUrl, String dimName, String dimValue) {
        String token = "{" + dimName.toLowerCase() + "}";
        int index = baseUrl.toLowerCase().indexOf(token);
        try {
            if (index != -1) {
                return baseUrl.substring(0, index)
                        + (dimValue == null ? "" : URLEncoder.encode(dimValue, "UTF-8"))
                        + baseUrl.substring(index + dimName.length() + 2);
            } else {
                return baseUrl;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Doesn't support UTF-8", e);
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
        String separator = (!baseUrl.contains("?") ? "?" : "&");

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
                                .append(
                                        valString.startsWith("{")
                                                ? valString
                                                : URLEncoder.encode(valString, "UTF-8"));
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
     * Fixes a problem when spaces within url's are replaced with +. In parts of the url we should
     * instead use %20. A fix for GEOT-4317
     */
    static String usePercentEncodingForSpace(String name) {
        try {
            return URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Doesn't support UTF-8", e);
        }
    }
}
