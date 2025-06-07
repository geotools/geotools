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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    byte[] response;

    String contentType;

    Map<String, String> headers = new HashMap<>();

    Charset charset = StandardCharsets.UTF_8;

    public MockHttpResponse(InputStream stream, String contentType, String... headers) {
        this.response = toByteArray(stream);
        this.contentType = contentType;

        if (headers != null) {
            if (headers.length % 2 != 0) {
                throw new IllegalArgumentException("The headers must be a alternated sequence of keys "
                        + "and values, should have an even number of entries");
            }

            for (int i = 0; i < headers.length; i += 2) {
                String key = headers[i];
                String value = headers[i++];
                this.headers.put(key, value);
            }
        }
    }

    private byte[] toByteArray(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[16384];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the response into a reusable byte array", e);
        }
    }

    public MockHttpResponse(InputStream stream, String contentType, Charset charset, String... headers) {
        this(stream, contentType, headers);
        this.charset = charset;
    }

    public MockHttpResponse(File responseFile, String contentType, String... headers) throws FileNotFoundException {
        this(new FileInputStream(responseFile), contentType, headers);
    }

    public MockHttpResponse(File responseFile, String contentType, Charset charset, String... headers)
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
    public MockHttpResponse(URL response, String contentType, String... headers) throws IOException {
        this(response.openStream(), contentType, headers);
    }

    public MockHttpResponse(URL response, String contentType, Charset charset, String... headers) throws IOException {
        this(response, contentType, headers);
        this.charset = charset;
    }

    public MockHttpResponse(String response, String contentType, String... headers) {
        this(response.getBytes(StandardCharsets.UTF_8), contentType, headers);
    }

    public MockHttpResponse(String response, String contentType, Charset charset, String... headers) {
        this(response.getBytes(charset), contentType);
        this.charset = charset;
    }

    public MockHttpResponse(byte[] response, String contentType, String... headers) {
        this(new ByteArrayInputStream(response), contentType, headers);
    }

    @Override
    public void dispose() {
        // nothing to do
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
        return new ByteArrayInputStream(response);
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
        return new String(response, charset);
    }

    @Deprecated
    public void setResponseCharset(String responseCharset) {
        this.charset = Charset.forName(responseCharset);
    }
}
