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
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A simple mock http client, allows to set expectations on requests and provide canned responses on
 * them
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MockHttpClient extends AbstractHttpClient {

    Map<Request, HTTPResponse> expectedRequests = new LinkedHashMap<Request, HTTPResponse>();

    /**
     * Binds a certain URL to a response. The order of the query string parameters is not relevant,
     * the code will match the same set of KVP params regardless of their sequence and case of their
     * keys (from OGC specs, keys are case insensitive, values are case sensitive)
     */
    public void expectGet(URL url, HTTPResponse response) {
        expectedRequests.put(new Request(url), response);
    }

    /** Binds a certain POST request to a response. */
    public void expectPost(
            URL url, String postContent, String postContentType, HTTPResponse response) {
        expectPOST(url, postContent.getBytes(), postContentType, response);
    }

    public void expectPOST(
            URL url, byte[] postContent, String postContentType, HTTPResponse response) {
        expectedRequests.put(new Request(url, postContent, postContentType), response);
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        return getResponse(new Request(url, toByteArray(postContent), postContentType));
    }

    private byte[] toByteArray(InputStream is) throws IOException {
        byte[] result;
        try {
            result = new byte[is.available()];
            is.read(result);
        } finally {
            is.close();
        }
        return result;
    }

    private HTTPResponse getResponse(Request request) {
        HTTPResponse response = expectedRequests.get(request);
        if (response == null) {
            StringBuilder sb =
                    new StringBuilder(
                            "Unexpected request \n"
                                    + request
                                    + "\nNo response is bound to it. Bound urls are: ");
            for (Request r : expectedRequests.keySet()) {
                sb.append("\n").append(r);
            }
            throw new IllegalArgumentException(sb.toString());
        }
        return response;
    }

    @Override
    public HTTPResponse get(URL url) throws IOException {
        return getResponse(new Request(url));
    }

    private static class Request {
        String path;

        Map<String, Object> kvp;

        String contentType;

        boolean isGetRequest;

        byte[] postContent;

        public Request(URL url) {
            this.path = url.getProtocol() + "://" + url.getHost() + url.getPath();
            Map<String, String> parsedQueryString = parseQueryString(url);
            // we use a treemap as it makes it easier to see what's missing when no bound url is
            // found
            this.kvp = new TreeMap<String, Object>();
            for (Entry<String, String> entry : parsedQueryString.entrySet()) {
                this.kvp.put(entry.getKey().toUpperCase(), entry.getValue());
            }
            this.isGetRequest = true;
        }

        public Map<String, String> parseQueryString(URL url) {
            if (url.getQuery() == null || url.getQuery().isEmpty()) {
                return Collections.emptyMap();
            }
            return Arrays.stream(url.getQuery().split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(o -> decode(o[0]), o -> decode(o[1])));
        }

        private static String decode(final String encoded) {
            try {
                return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public Request(URL url, byte[] postContent, String postContentType) {
            this(url);
            this.isGetRequest = false;
            this.postContent = postContent;
            this.contentType = postContentType;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
            result = prime * result + (isGetRequest ? 1231 : 1237);
            result = prime * result + ((kvp == null) ? 0 : kvp.hashCode());
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            result = prime * result + Arrays.hashCode(postContent);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Request other = (Request) obj;
            if (contentType == null) {
                if (other.contentType != null) return false;
            } else if (!contentType.equals(other.contentType)) return false;
            if (isGetRequest != other.isGetRequest) return false;
            if (kvp == null) {
                if (other.kvp != null) return false;
            } else if (!kvp.equals(other.kvp)) return false;
            if (path == null) {
                if (other.path != null) return false;
            } else if (!path.equals(other.path)) return false;
            if (!Arrays.equals(postContent, other.postContent)) return false;
            return true;
        }

        @Override
        public String toString() {
            if (isGetRequest) {
                return "GET " + path + ", " + kvp;
            } else {
                return "POST "
                        + path
                        + ", "
                        + kvp
                        + ", content type "
                        + contentType
                        + ", content "
                        + Arrays.toString(postContent);
            }
        }
    }
}
