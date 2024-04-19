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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * A base class for HTTPClient, that implements everything except the get and post methods.
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class AbstractHttpClient implements HTTPClient {

    protected String user;

    protected String password;

    protected String authKey;

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
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public String getAuthKey() {
        return this.authKey;
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

    protected static URL appendURL(URL oldUrl, String appendQuery) throws MalformedURLException {
        String oldQuery = oldUrl.getQuery();
        String newQuery = oldQuery != null ? oldQuery + "&" + appendQuery : appendQuery;

        return new URL(
                oldUrl.getProtocol(),
                oldUrl.getAuthority(),
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
