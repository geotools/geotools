/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * A wrapper around a HTTPClient that will send messages to log for each request and response.
 *
 * <p>To get full body content set Level=FINEST
 */
public class LoggingHTTPClient extends DelegateHTTPClient {

    private String charsetName;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final Logger LOGGER = Logging.getLogger(LoggingHTTPClient.class);

    private int counter = 0;

    public LoggingHTTPClient(HTTPClient delegate) {
        this(delegate, "UTF-8");
    }

    public LoggingHTTPClient(HTTPClient delegate, String charsetName) {
        super(delegate);
        this.charsetName = charsetName;
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
        final int myCount = ++counter;
        LOGGER.info(String.format("POST Request #%d URL: %s", myCount, url));
        if (LOGGER.isLoggable(Level.FINEST)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(postContent, out);
            LOGGER.finest("POST Request Body: \n" + out.toString(charsetName));

            postContent = new ByteArrayInputStream(out.toByteArray());
        }
        return new LoggingHTTPResponse(delegate.post(url, postContent, postContentType), charsetName, myCount);
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType, Map<String, String> headers)
            throws IOException {
        final int myCount = ++counter;
        LOGGER.info(String.format("POST Request #%d URL with additional headers %s : %s", myCount, headers, url));
        if (LOGGER.isLoggable(Level.FINEST)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(postContent, out);
            LOGGER.finest("POST Request Body: \n" + out.toString(charsetName));

            postContent = new ByteArrayInputStream(out.toByteArray());
        }
        return new LoggingHTTPResponse(delegate.post(url, postContent, postContentType, headers), charsetName, myCount);
    }

    @Override
    public HTTPResponse get(URL url) throws IOException {
        final int myCount = ++counter;
        LOGGER.info(String.format("GET Request #%d URL: %s", myCount, url));
        return new LoggingHTTPResponse(delegate.get(url), charsetName, myCount);
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        final int myCount = ++counter;
        LOGGER.info(String.format("GET Request #%d URL with additional headers %s : %s", myCount, headers, url));
        return new LoggingHTTPResponse(delegate.get(url, headers), charsetName, myCount);
    }

    static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

    private static class LoggingHTTPResponse extends DelegateHTTPResponse {

        private InputStream input;

        public LoggingHTTPResponse(HTTPResponse delegate, String charsetName, final int myCount) throws IOException {
            super(delegate);
            LOGGER.info(String.format("Response #%d received.", myCount));

            if (LOGGER.isLoggable(Level.FINEST)) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                LoggingHTTPClient.copy(delegate.getResponseStream(), output);

                LOGGER.finest("Body: \n" + output.toString(charsetName));
                input = new ByteArrayInputStream(output.toByteArray());
            } else {
                input = delegate.getResponseStream();
            }
        }

        @Override
        public InputStream getResponseStream() throws IOException {
            return input;
        }
    }
}
