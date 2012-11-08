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
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class DelegateHTTPClient implements HTTPClient {

    protected HTTPClient delegate;

    
    public DelegateHTTPClient(HTTPClient delegate)  {
        this.delegate = delegate;
    }

    
    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
        return delegate.post(url, postContent, postContentType);
    }

    @Override    
    public HTTPResponse get(URL url) throws IOException {
        return delegate.get(url);
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
}