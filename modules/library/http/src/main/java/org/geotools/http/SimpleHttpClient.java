/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.Base64;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;

/**
 * A simple {@link HTTPClient} that creates a new {@link HttpURLConnection HTTP connection} for each
 * request.
 *
 * @author groldan
 */
public class SimpleHttpClient extends AbstractHttpClient implements HTTPProxy {

    private static final Logger LOGGER = Logging.getLogger(SimpleHttpClient.class);

    private static final int DEFAULT_TIMEOUT = 30; // 30 seconds

    /**
     * A SimpleHttpClient should be initiated by a call to
     * HTTPFactoryFinder.getHttpClientFactory().getClient();
     */
    public SimpleHttpClient() {
        this.connectTimeout = DEFAULT_TIMEOUT;
        this.readTimeout = DEFAULT_TIMEOUT;
    }

    /** @see org.geotools.data.ows.HTTPClient#get(java.net.URL) */
    @Override
    public HTTPResponse get(final URL url) throws IOException {
        return this.get(url, null);
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "URL is " + url);

        if (isFile(url)) {
            return this.createFileResponse(url);
        }
        URLConnection connection = openConnection(url);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("GET");
        }

        // Set User-Agent to a good default
        connection.addRequestProperty(
                "User-Agent", "GeoTools HTTPClient (" + GeoTools.getVersion() + ")");
        if (headers != null) {
            for (Map.Entry<String, String> headerNameValue : headers.entrySet()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            "Adding header "
                                    + headerNameValue.getKey()
                                    + " = "
                                    + headerNameValue.getValue());
                }
                connection.addRequestProperty(headerNameValue.getKey(), headerNameValue.getValue());
            }
        }

        connection.connect();

        return new DefaultHttpResponse(connection);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#post(java.net.URL, java.io.InputStream,
     *     java.lang.String)
     */
    @Override
    public HTTPResponse post(
            final URL url, final InputStream postContent, final String postContentType)
            throws IOException {

        URLConnection connection = openConnection(url);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("POST");
        }
        connection.setDoOutput(true);
        if (postContentType != null) {
            connection.setRequestProperty("Content-type", postContentType);
        }

        connection.connect();

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] buff = new byte[512];
            int count;
            while ((count = postContent.read(buff)) > -1) {
                outputStream.write(buff, 0, count);
            }
            outputStream.flush();
        }

        return new DefaultHttpResponse(connection);
    }

    private URLConnection openConnection(URL finalURL) throws IOException {
        URLConnection connection = finalURL.openConnection();
        final boolean http = connection instanceof HttpURLConnection;
        if (http && tryGzip) {
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }
        // mind, connect timeout is in seconds
        if (http && getConnectTimeout() > 0) {
            connection.setConnectTimeout(1000 * getConnectTimeout());
        }
        if (http && getReadTimeout() > 0) {
            connection.setReadTimeout(1000 * getReadTimeout());
        }

        final String username = getUser();
        final String password = getPassword();

        if (http && username != null && password != null) {
            String userpassword = username + ":" + password;
            String encodedAuthorization =
                    Base64.encodeBytes(
                            userpassword.getBytes(StandardCharsets.UTF_8), Base64.DONT_BREAK_LINES);
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        }
        return connection;
    }

    @Deprecated
    public static class SimpleHTTPResponse extends DefaultHttpResponse {

        public SimpleHTTPResponse(final URLConnection connection) throws IOException {
            super(connection);
        }
    }
}
