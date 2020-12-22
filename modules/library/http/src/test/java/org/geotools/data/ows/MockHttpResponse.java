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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to mock HTTP responses
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MockHttpResponse implements HTTPResponse {

    InputStream stream;

    String contentType;

    Map<String, String> headers = new HashMap<>();

    String responseCharset;

    public MockHttpResponse(File responseFile, String contentType) throws FileNotFoundException {
        this.stream = new FileInputStream(responseFile);
        this.contentType = contentType;
    }

    public MockHttpResponse(String response, String contentType, String... headers) {
        this(response.getBytes(), contentType, headers);
    }

    public MockHttpResponse(byte[] response, String contentType, String... headers) {
        this.stream = new ByteArrayInputStream(response);
        this.contentType = contentType;

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
        return stream;
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
        Charset charset =
                (responseCharset != null
                        ? Charset.forName(responseCharset)
                        : StandardCharsets.UTF_8);

        StringBuilder textBuilder = new StringBuilder();
        try {
            try (Reader reader = new BufferedReader(new InputStreamReader(stream, charset))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }
            return contentType + " - " + textBuilder.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                stream.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }
}