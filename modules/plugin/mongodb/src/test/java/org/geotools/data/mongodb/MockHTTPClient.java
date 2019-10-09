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
package org.geotools.data.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;

/**
 * @author ImranR
 *     <p>Mock HTTP Class used in Unit Tests to Mock Schema Downloads
 */
public class MockHTTPClient implements HTTPClient {

    /** Mocks HTTP calls */
    Map<String, HTTPResponse> expectGet = new HashMap<String, HTTPResponse>();

    public MockHTTPClient() {}

    public MockHTTPClient(Map<String, HTTPResponse> expectGet) {
        this.expectGet = expectGet;
    }

    public void expectGet(String url, HTTPResponse response) {
        expectGet.put(url, response);
    }

    @Override
    public HTTPResponse post(URL url, InputStream postContent, String postContentType)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HTTPResponse get(URL url) throws IOException {
        return expectGet.get(url.toExternalForm());
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public void setUser(String user) {}

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {}

    @Override
    public int getConnectTimeout() {
        return 0;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {}

    @Override
    public int getReadTimeout() {
        return 0;
    }

    @Override
    public void setReadTimeout(int readTimeout) {}

    @Override
    public void setTryGzip(boolean tryGZIP) {}

    @Override
    public boolean isTryGzip() {
        return false;
    }
}
