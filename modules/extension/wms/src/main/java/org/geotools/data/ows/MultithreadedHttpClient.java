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
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * An Apache commons HTTP client based {@link HTTPClient} backed by a multithreaded connection
 * manager that allows to reuse connections to the backing server and to limit the
 * {@link #setMaxConnections(int) max number of concurrent connections}.
 * 
 * @author groldan
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 */
public class MultithreadedHttpClient implements HTTPClient {

    private MultiThreadedHttpConnectionManager connectionManager;

    private HttpClient client;

    private String user;

    private String password;

    public MultithreadedHttpClient() {
        connectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setSoTimeout(30000);
        params.setConnectionTimeout(30000);
        params.setMaxTotalConnections(6);
        params.setDefaultMaxConnectionsPerHost(6);

        connectionManager.setParams(params);

        client = new HttpClient(connectionManager);
    }

    // @Override
    public HTTPResponse post(final URL url, final InputStream postContent,
            final String postContentType) throws IOException {

        PostMethod postMethod = new PostMethod(url.toExternalForm());
        postMethod.setDoAuthentication(user != null && password != null);
        if (postContentType != null) {
            postMethod.setRequestHeader("Content-type", postContentType);
        }
        RequestEntity requestEntity = new InputStreamRequestEntity(postContent);
        postMethod.setRequestEntity(requestEntity);

        int responseCode = client.executeMethod(postMethod);
        if (200 != responseCode) {
            postMethod.releaseConnection();
            throw new IOException("Server returned HTTP error code " + responseCode + " for URL "
                    + url.toExternalForm());
        }

        return new HttpMethodResponse(postMethod);
    }

    // @Override
    public HTTPResponse get(final URL url) throws IOException {

        GetMethod getMethod = new GetMethod(url.toExternalForm());
        getMethod.setDoAuthentication(user != null && password != null);

        int responseCode = client.executeMethod(getMethod);
        if (200 != responseCode) {
            getMethod.releaseConnection();
            throw new IOException("Server returned HTTP error code " + responseCode + " for URL "
                    + url.toExternalForm());
        }
        return new HttpMethodResponse(getMethod);
    }

    // @Override
    public String getUser() {
        return user;
    }

    // @Override
    public void setUser(String user) {
        this.user = user;
        resetCredentials();
    }

    // @Override
    public String getPassword() {
        return password;
    }

    // @Override
    public void setPassword(String password) {
        this.password = password;
        resetCredentials();
    }

    private void resetCredentials() {
        client.getState().clearCredentials();
        if (user != null && password != null) {
            AuthScope authscope = AuthScope.ANY;
            Credentials credentials = new UsernamePasswordCredentials(user, password);

            client.getParams().setAuthenticationPreemptive(true);
            client.getState().setCredentials(authscope, credentials);
        } else {
            client.getParams().setAuthenticationPreemptive(false);
        }
    }

    // @Override
    public int getConnectTimeout() {
        return connectionManager.getParams().getConnectionTimeout() / 1000;
    }

    // @Override
    public void setConnectTimeout(int connectTimeout) {
        connectionManager.getParams().setConnectionTimeout(connectTimeout * 1000);
    }

    // @Override
    public int getReadTimeout() {
        return connectionManager.getParams().getSoTimeout() / 1000;
    }

    // @Override
    public void setReadTimeout(int readTimeout) {
        connectionManager.getParams().setSoTimeout(readTimeout * 1000);
    }

    public int getMaxConnections() {
        return connectionManager.getParams().getDefaultMaxConnectionsPerHost();
    }

    public void setMaxConnections(final int maxConnections) {
        connectionManager.getParams().setMaxTotalConnections(maxConnections);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnections);
    }

    private static class HttpMethodResponse implements HTTPResponse {

        private HttpMethod methodResponse;

        private InputStream responseBodyAsStream;

        public HttpMethodResponse(final HttpMethod methodResponse) {
            this.methodResponse = methodResponse;
        }

        // @Override
        public void dispose() {
            if (responseBodyAsStream != null) {
                try {
                    responseBodyAsStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (methodResponse != null) {
                methodResponse.releaseConnection();
                methodResponse = null;
            }
        }

        // @Override
        public String getContentType() {
            return getResponseHeader("Content-Type");
        }

        // @Override
        public String getResponseHeader(final String headerName) {
            Header responseHeader = methodResponse.getResponseHeader(headerName);
            return responseHeader == null ? null : responseHeader.getValue();
        }

        // @Override
        public InputStream getResponseStream() throws IOException {
            if (responseBodyAsStream == null) {
                responseBodyAsStream = methodResponse.getResponseBodyAsStream();
            }
            return responseBodyAsStream;
        }

    }
}
