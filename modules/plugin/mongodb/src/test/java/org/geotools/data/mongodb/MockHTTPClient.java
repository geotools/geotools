/* (c) 2019 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;

/** @author ImranR */
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUser(String user) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPassword(String password) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getConnectTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getReadTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTryGzip(boolean tryGZIP) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTryGzip() {
        // TODO Auto-generated method stub
        return false;
    }
}
