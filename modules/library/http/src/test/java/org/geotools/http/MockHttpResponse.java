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
package org.geotools.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to mock HTTP responses.
 *
 * <p>Taking anything that could be turned into an InputStream as an argument for the constructor.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class MockHttpResponse implements HTTPResponse {

    InputStream stream;

    String contentType;

    Map<String, String> headers = new HashMap<>();

    Charset charset = StandardCharsets.UTF_8;

    public MockHttpResponse(InputStream stream, String contentType, String... headers) {
        this.stream = stream;
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

    public MockHttpResponse(
            InputStream stream, String contentType, Charset charset, String... headers) {
        this(stream, contentType, headers);
        this.charset = charset;
    }

    public MockHttpResponse(File responseFile, String contentType, String... headers)
            throws FileNotFoundException {
        this(new FileInputStream(responseFile), contentType, headers);
    }

    public MockHttpResponse(
            File responseFile, String contentType, Charset charset, String... headers)
            throws FileNotFoundException {
        this(responseFile, contentType, headers);
        this.charset = charset;
    }

    /**
     * MockHttpResponse with a URL as the response.
     *
     * @param response Pointing at a file resource
     * @param contentType
     * @param headers
     * @throws IOException
     */
    public MockHttpResponse(URL response, String contentType, String... headers)
            throws IOException {
        this(response.openStream(), contentType, headers);
    }

    public MockHttpResponse(URL response, String contentType, Charset charset, String... headers)
            throws IOException {
        this(response, contentType, headers);
        this.charset = charset;
    }

    public MockHttpResponse(String response, String contentType, String... headers) {
        this(response.getBytes(), contentType, headers);
    }

    public MockHttpResponse(
            String response, String contentType, Charset charset, String... headers) {
        this(response.getBytes(charset), contentType);
        this.charset = charset;
    }

    public MockHttpResponse(byte[] response, String contentType, String... headers) {
        this(new ByteArrayInputStream(response), contentType, headers);
    }

    @Override
    public void dispose() {
        try {
            this.stream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public String getResponseHeader(String headerName) {
        return headers.get(headerName);
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        return stream;
    }

    /**
     * @return {@code null}
     * @see HTTPResponse#getResponseCharset()
     */
    @Override
    public String getResponseCharset() {
        return charset.name();
    }

    @Override
    public String toString() {

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

    @Deprecated
    public void setResponseCharset(String responseCharset) {
        this.charset = Charset.forName(responseCharset);
    }
}
