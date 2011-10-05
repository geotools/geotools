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
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.geotools.data.Base64;

/**
 * A simple {@link HTTPClient} that creates a new {@link HttpURLConnection HTTP connection} for each
 * request.
 * 
 * @author groldan
 * 
 */
public class SimpleHttpClient implements HTTPClient {

    private static final int DEFAULT_TIMEOUT = 30000;// 30 seconds

    private String user;

    private String password;

    private int connectTimeout = DEFAULT_TIMEOUT;

    private int readTimeout = DEFAULT_TIMEOUT;

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#get(java.net.URL)
     */
    public HTTPResponse get(final URL url) throws IOException {

        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod("GET");

        connection.connect();

        return new SimpleHTTPResponse(connection);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#post(java.net.URL, java.io.InputStream,
     *      java.lang.String)
     */
    public HTTPResponse post(final URL url, final InputStream postContent,
            final String postContentType) throws IOException {

        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        if (postContentType != null) {
            connection.setRequestProperty("Content-type", postContentType);
        }

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        try {
            byte[] buff = new byte[512];
            int count;
            while ((count = postContent.read(buff)) > -1) {
                outputStream.write(buff, 0, count);
            }
        } finally {
            outputStream.flush();
            outputStream.close();
        }

        return new SimpleHTTPResponse(connection);
    }

    private HttpURLConnection openConnection(URL finalURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setConnectTimeout(getConnectTimeout());
        connection.setReadTimeout(getReadTimeout());

        final String username = getUser();
        final String password = getPassword();

        if (username != null && password != null) {
            String userpassword = username + ":" + password;
            String encodedAuthorization = Base64.encodeBytes(userpassword.getBytes("UTF-8"));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        }
        return connection;
    }

    public static class SimpleHTTPResponse implements HTTPResponse {

        private HttpURLConnection connection;

        private InputStream responseStream;

        public SimpleHTTPResponse(final HttpURLConnection connection) {
            this.connection = connection;
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#dispose()
         */
        public void dispose() {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    // ignore
                }
                responseStream = null;
            }
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#getContentType()
         */
        @Override
        public String getContentType() {
            return connection.getContentType();
        }


        @Override
        public String getResponseHeader(String headerName) {
            return connection.getHeaderField(headerName);
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#getResponseStream()
         */
        public InputStream getResponseStream() throws IOException {

            if (responseStream == null) {
                InputStream inputStream = connection.getInputStream();

                final String contentEncoding = connection.getContentEncoding();

                if (contentEncoding != null
                        && connection.getContentEncoding().indexOf("gzip") != -1) {
                    inputStream = new GZIPInputStream(inputStream);
                }
                responseStream = inputStream;
            }

            return responseStream;
        }
    }
}
