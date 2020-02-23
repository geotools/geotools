/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class URIs {

    public static URL buildURL(URL baseURL, Map<String, String> kvp) {
        String uri = buildURL(baseURL.toExternalForm(), null, kvp);
        try {
            return new URL(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildURL(String baseURL, Map<String, String> kvp) {
        return buildURL(baseURL, null, kvp);
    }

    public static String buildURL(final String baseURL, String path, Map<String, String> kvp) {

        // prepare modifiable parameters
        StringBuilder baseURLBuffer = new StringBuilder(baseURL);

        Map<String, String> kvpBuffer = new LinkedHashMap<String, String>();
        if (kvp != null) {
            kvpBuffer.putAll(kvp);
        }

        // compose the final URL
        String result;
        if (path != null) {
            result = appendContextPath(baseURLBuffer.toString(), path);
        } else {
            result = baseURLBuffer.toString();
        }

        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : kvpBuffer.entrySet()) {
            params.append(entry.getKey());
            params.append("=");
            String value = entry.getValue();
            if (value != null) {
                String encoded = urlEncode(value);
                params.append(encoded);
            }
            params.append("&");
        }
        if (params.length() > 1) {
            params.setLength(params.length() - 1);
            result = appendQueryString(result, params.toString());
        }

        return result;
    }

    /**
     * Appends a context path to a base url.
     *
     * @param url The base url.
     * @param contextPath The context path to be appended.
     * @return A full url with the context path appended.
     */
    public static String appendContextPath(String url, String contextPath) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        if (contextPath.startsWith("/")) {
            contextPath = contextPath.substring(1);
        }

        return url + (contextPath.isEmpty() ? "" : "/" + contextPath);
    }

    /** URL encodes the value towards the ISO-8859-1 charset */
    public static String urlEncode(String value) {
        try {
            // TODO: URLEncoder also encodes ( and ) which are considered safe chars,
            // see also http://www.w3.org/International/O-URL-code.html
            return URLEncoder.encode(new String(value.getBytes(), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This is unexpected", e);
        }
    }

    public static String urlDecode(String value) {
        try {
            // TODO: URLEncoder also encodes ( and ) which are considered safe chars,
            // see also http://www.w3.org/International/O-URL-code.html
            return URLDecoder.decode(new String(value.getBytes(), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This is unexpected", e);
        }
    }

    /**
     * Appends a query string to a url.
     *
     * <p>This method checks <code>url</code> to see if the appended query string requires a '?' or
     * '&' to be prepended.
     *
     * <p>This code can be used to make sure the url ends with ? or & by calling
     * appendQueryString(url, "")
     *
     * @param url The base url.
     * @param queryString The query string to be appended, should not contain the '?' character.
     * @return A full url with the query string appended.
     *     <p>TODO: remove this and replace with Requetss.appendQueryString
     */
    public static String appendQueryString(String url, String queryString) {
        if (url.endsWith("?") || url.endsWith("&")) {
            return url + queryString;
        }

        if (url.indexOf('?') != -1) {
            return url + "&" + queryString;
        }

        return url + "?" + queryString;
    }

    public static Map<String, String> parseQueryString(final String queryString) {
        if (queryString == null || queryString.length() == 0) {
            return Collections.emptyMap();
        }
        String[] params = queryString.split("&");
        @SuppressWarnings("unchecked")
        Map<String, String> kvpMap = new CaseInsensitiveMap();
        for (String kvp : params) {
            String[] split = kvp.split("=");
            if (split[0].length() > 0) {
                String key = split[0];
                String value = split.length > 1 ? urlDecode(split[1]) : null;

                kvpMap.put(key, value);
            }
        }
        return kvpMap;
    }
}
