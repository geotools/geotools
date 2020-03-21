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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to mock HTTP responses
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MockHttpResponse implements HTTPResponse {

    String contentType;

    Map<String, String> headers;

    byte[] response;

    String responseCharset;

    public MockHttpResponse(String response, String contentType, String... headers) {
        this(response.getBytes(), contentType, headers);
    }

    public MockHttpResponse(byte[] response, String contentType, String... headers) {
        this.response = response;
        this.contentType = contentType;
        this.headers = new HashMap<String, String>();

        if (headers != null) {
            if (headers.length % 2 != 0) {
                throw new IllegalArgumentException(
                        "The headers must be a alternated sequence of keys "
                                + "and values, should have an even number of entries");
            }

            for (int i = 0; i < headers.length; i += 2) {
                String key = headers[i];
                String value = headers[i++];
                this.headers.put(key, value);
            }
        }
    }

    public void dispose() {
        // nothing to do
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getResponseHeader(String headerName) {
        return headers.get(headerName);
    }

    public InputStream getResponseStream() throws IOException {
        return new ByteArrayInputStream(response);
    }

    /**
     * @return {@code null}
     * @see HTTPResponse#getResponseCharset()
     */
    @Override
    public String getResponseCharset() {
        return responseCharset;
    }

    @Override
    public String toString() {
        String contents = null;
        if (responseCharset != null) {
            contents = new String(response, Charset.forName(responseCharset));
        } else {
            contents = new String(response);
        }
        return contentType + " - " + contents;
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }
}
