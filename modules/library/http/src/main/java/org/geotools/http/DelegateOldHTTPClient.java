/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
import java.net.URL;
import java.util.Map;

/**
 * @deprecated Used in conjunction with the namespace change from org.geotools.data.ows to
 *     org.geotools.http
 * @author Roar Brænden
 */
@Deprecated
public class DelegateOldHTTPClient implements HTTPClient {

    protected org.geotools.data.ows.HTTPClient delegate;

    public DelegateOldHTTPClient(org.geotools.data.ows.HTTPClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        return new DelegateOldHTTPResponse(delegate.post(url, postContent, postContentType));
    }

    @Override
    public HTTPResponse get(URL url) throws IOException {
        return new DelegateOldHTTPResponse(delegate.get(url));
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        return new DelegateOldHTTPResponse(delegate.get(url, headers));
    }

    @Override
    public String getUser() {
        return delegate.getUser();
    }

    @Override
    public void setUser(String user) {
        delegate.setUser(user);
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public void setPassword(String password) {
        delegate.setPassword(password);
    }

    @Override
    public int getConnectTimeout() {
        return delegate.getConnectTimeout();
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        delegate.setConnectTimeout(connectTimeout);
    }

    @Override
    public int getReadTimeout() {
        return delegate.getReadTimeout();
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        delegate.setReadTimeout(readTimeout);
    }

    @Override
    public void setTryGzip(boolean tryGZIP) {
        delegate.setTryGzip(tryGZIP);
    }

    @Override
    public boolean isTryGzip() {
        return delegate.isTryGzip();
    }
}
