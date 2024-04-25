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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

/**
 * A base class for HTTPClient, that implements everything except the get and post methods.
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class AbstractHttpClient implements HTTPClient {

    protected String user;

    protected String password;

    protected Map<String, String> extraParams = Collections.emptyMap();

    protected int connectTimeout;

    protected int readTimeout;

    protected boolean tryGzip;

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
        return this.password;
    }

    @Override
    public void setExtraParams(Map<String, String> extraParams) {
        this.extraParams = extraParams;
    }

    @Override
    public Map<String, String> getExtraParams() {
        return this.extraParams;
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
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /** @see HTTPClient#setTryGzip(boolean) */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        this.tryGzip = tryGZIP;
    }

    /** @see HTTPClient#isTryGzip() */
    @Override
    public boolean isTryGzip() {
        return tryGzip;
    }

+    /**
+     * Appends query parameters to an existing URL.
+     *
+     * @param oldUrl The original URL to which parameters will be appended.
+     * @param appendQuery A map containing key-value pairs to be appended as query parameters.
+     * @return A new URL with the appended query parameters.
+     * @throws MalformedURLException If the resulting URL is malformed.
+     */
    protected static URL appendURL(URL oldUrl, Map<String, String> appendQuery)
            throws MalformedURLException {
        String oldQuery = oldUrl.getQuery();

        StringJoiner stringJoiner = new StringJoiner("&");
        appendQuery.forEach(
                (key, value) -> {
                    try {
                        stringJoiner.add(
                                URLEncoder.encode(key, "UTF-8")
                                        + "="
                                        + URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        String query = stringJoiner.toString();

        String newQuery = oldQuery != null ? oldQuery + "&" + query : query;

        return new URL(
                oldUrl.getProtocol(),
                oldUrl.getHost(),
                oldUrl.getPort(),
                oldUrl.getPath() + "?" + newQuery);
    }

    protected boolean isFile(URL url) {
        return "file".equalsIgnoreCase(url.getProtocol());
    }

    protected HTTPResponse createFileResponse(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        return new DefaultHttpResponse(connection);
    }

    @Override
    public abstract HTTPResponse post(URL url, InputStream content, String contentType)
            throws IOException;
}
