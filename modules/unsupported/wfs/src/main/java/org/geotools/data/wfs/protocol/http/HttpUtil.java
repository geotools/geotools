/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.geotools.util.logging.Logging;

/**
 * 
 */
public class HttpUtil {

    protected static final Logger LOGGER = Logging.getLogger(HttpUtil.class);

    /**
     * @see HTTPProtocol#createUrl(URL, Map)
     */
    public static URL createUrl(final URL baseUrl, final Map<String, String> queryStringKvp)
            throws MalformedURLException {
        final String finalUrlString = createUri(baseUrl, queryStringKvp);
        URL queryUrl = new URL(finalUrlString);
        return queryUrl;
    }

    public static String createUri(final URL baseUrl, final Map<String, String> queryStringKvp) {
        final String query = baseUrl.getQuery();
        Map<String, String> finalKvpMap = new HashMap<String, String>(queryStringKvp);
        if (query != null && query.length() > 0) {
            Map<String, String> userParams = new CaseInsensitiveMap(queryStringKvp);
            String[] rawUrlKvpSet = query.split("&");
            for (String rawUrlKvp : rawUrlKvpSet) {
                int eqIdx = rawUrlKvp.indexOf('=');
                String key, value;
                if (eqIdx > 0) {
                    key = rawUrlKvp.substring(0, eqIdx);
                    value = rawUrlKvp.substring(eqIdx + 1);
                } else {
                    key = rawUrlKvp;
                    value = null;
                }
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                if (userParams.containsKey(key)) {
                    LOGGER.fine("user supplied value for query string argument " + key
                            + " overrides the one in the base url");
                } else {
                    finalKvpMap.put(key, value);
                }
            }
        }

        String protocol = baseUrl.getProtocol();
        String host = baseUrl.getHost();
        int port = baseUrl.getPort();
        String path = baseUrl.getPath();

        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://").append(host);
        if (port != -1 && port != baseUrl.getDefaultPort()) {
            sb.append(':');
            sb.append(port);
        }
        if (!"".equals(path) && !path.startsWith("/")) {
            sb.append('/');
        }
        sb.append(path).append('?');

        String key, value;
        try {
            Entry<String, String> kvp;
            for (Iterator<Map.Entry<String, String>> it = finalKvpMap.entrySet().iterator(); it
                    .hasNext();) {
                kvp = it.next();
                key = kvp.getKey();
                value = kvp.getValue();
                if (value == null) {
                    value = "";
                } else {
                    value = URLEncoder.encode(value, "UTF-8");
                }
                sb.append(key);
                sb.append('=');
                sb.append(value);
                if (it.hasNext()) {
                    sb.append('&');
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final String finalUrlString = sb.toString();
        return finalUrlString;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> requestKvp(URL url) {
        String queryString = url.getQuery();
        String[] split = queryString.split("&");
        Map<String, String> kvp = new LinkedHashMap<String, String>();
        for (String encodedKvp : split) {
            String[] splittedKvp = encodedKvp.split("=");
            final String key = splittedKvp[0];
            String value = splittedKvp.length == 2 ? splittedKvp[1] : null;
            if (value != null) {
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            kvp.put(key, value);
        }
        return new CaseInsensitiveMap(kvp);
    }

}
