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
package org.geotools.http.commons;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.geotools.data.ows.AbstractOpenWebService;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.http.HTTPProxy;
import org.geotools.http.HTTPResponse;
import org.geotools.util.logging.Logging;

/**
 * An Apache commons HTTP client based {@link HTTPClient} backed by a multithreaded connection
 * manager that allows to reuse connections to the backing server and to limit the {@link
 * #setMaxConnections(int) max number of concurrent connections}.
 *
 * <p>Java System properties {@code http.proxyHost}, {@code http.proxyPort}, {@code http.proxyUser},
 * and {@code http.proxyPassword} are respected.
 *
 * <p>Copied from gt-wms.
 *
 * @author groldan
 * @author awaterme
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 */
public class MultithreadedHttpClient implements HTTPClient, HTTPConnectionPooling, HTTPProxy {

    private static final Logger LOGGER = Logging.getLogger(MultithreadedHttpClient.class);

    private final PoolingHttpClientConnectionManager connectionManager;

    private HttpClient client;

    private String user;

    private String password;

    private boolean tryGzip = true;

    /** Available if a proxy was specified as system property */
    private RequestConfig hostConfigNoProxy;

    private Set<String> nonProxyHosts = new HashSet<>();

    private RequestConfig connectionConfig;

    public MultithreadedHttpClient() {
        connectionManager = new PoolingHttpClientConnectionManager();

        /*
         * params.setSoTimeout(30000); params.setConnectionTimeout(30000); params.setMaxTotalConnections(6);
         * params.setDefaultMaxConnectionsPerHost(6);
         */
        connectionManager.setMaxTotal(6);
        connectionManager.setDefaultMaxPerRoute(6);
        connectionConfig =
                RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.DEFAULT)
                        .setExpectContinueEnabled(true)
                        .setSocketTimeout(30000)
                        .setConnectTimeout(30000)
                        .build();

        client = createHttpClient();
    }

    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    // package private to support testing || JNH Public for refactoring
    public HttpClient createHttpClient() {
        clientBuilder.useSystemProperties();
        clientBuilder.setConnectionManager(connectionManager);
        return clientBuilder.build();
    }

    @Override
    public HttpMethodResponse post(
            final URL url, final InputStream postContent, final String postContentType)
            throws IOException {

        HttpPost postMethod = new HttpPost(url.toExternalForm());
        postMethod.setConfig(connectionConfig);
        if (user != null && password != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", user));
            nvps.add(new BasicNameValuePair("password", password));
            postMethod.setEntity(new UrlEncodedFormEntity(nvps));
        }
        if (tryGzip) {
            postMethod.setHeader("Accept-Encoding", "gzip");
        }
        if (postContentType != null) {
            postMethod.setHeader("Content-type", postContentType);
        }
        HttpEntity requestEntity = new InputStreamEntity(postContent);
        postMethod.setEntity(requestEntity);

        HttpMethodResponse response = null;
        try {
            response = executeMethod(postMethod);
        } catch (HttpException e) {
            throw new IOException(e);
        }
        if (200 != response.getStatusCode()) {
            postMethod.releaseConnection();
            throw new IOException(
                    "Server returned HTTP error code "
                            + response.getStatusCode()
                            + " for URL "
                            + url.toExternalForm());
        }

        return response;
    }

    /** @return the http status code of the execution */
    private HttpMethodResponse executeMethod(HttpRequestBase method)
            throws IOException, HttpException {

        HttpResponse resp = client.execute(method);
        HttpMethodResponse response = new HttpMethodResponse(resp);
        return response;
    }

    @Override
    public HTTPResponse get(final URL url) throws IOException {
        return this.get(url, null);
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        HttpGet getMethod = new HttpGet(url.toExternalForm());
        getMethod.setConfig(connectionConfig);
        if (user != null && password != null) {
            resetCredentials();
            String encoding =
                    Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
            getMethod.setHeader("Authorization", "Basic " + encoding);
        }
        if (tryGzip) {
            getMethod.setHeader("Accept-Encoding", "gzip");
        }

        if (headers != null) {
            for (Map.Entry<String, String> headerNameValue : headers.entrySet()) {
                getMethod.setHeader(headerNameValue.getKey(), headerNameValue.getValue());
            }
        }

        HttpMethodResponse response = null;
        try {
            response = executeMethod(getMethod);
        } catch (HttpException e) {
            throw new IOException(e);
        }
        if (200 != response.getStatusCode()) {
            getMethod.releaseConnection();
            throw new IOException(
                    "Server returned HTTP error code "
                            + response.getStatusCode()
                            + " for URL "
                            + url.toExternalForm());
        }
        return response;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
        resetCredentials();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        resetCredentials();
    }

    private void resetCredentials() {
        if (user != null && password != null) {
            AuthScope authscope = AuthScope.ANY;
            Credentials credentials = new UsernamePasswordCredentials(user, password);
            // TODO - check if this works for all types of auth or do we need to look it up?
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(authscope, credentials);
            clientBuilder.setDefaultCredentialsProvider(credsProvider);
            client = createHttpClient();
        }
    }

    @Override
    public int getConnectTimeout() {
        return connectionConfig.getConnectionRequestTimeout() / 1000;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        connectionConfig =
                RequestConfig.copy(connectionConfig)
                        .setConnectionRequestTimeout(connectTimeout * 1000)
                        .build();
    }

    @Override
    public int getReadTimeout() {
        return connectionConfig.getSocketTimeout() / 1000;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        connectionConfig =
                RequestConfig.copy(connectionConfig).setSocketTimeout(readTimeout * 1000).build();
    }

    @Override
    public int getMaxConnections() {
        return connectionManager.getDefaultMaxPerRoute();
    }

    @Override
    public void setMaxConnections(final int maxConnections) {
        connectionManager.setDefaultMaxPerRoute(maxConnections);
        connectionManager.setMaxTotal(maxConnections);
    }

    static class HttpMethodResponse implements HTTPResponse {

        private org.apache.http.HttpResponse methodResponse;

        private InputStream responseBodyAsStream;

        public HttpMethodResponse(final org.apache.http.HttpResponse methodResponse) {
            this.methodResponse = methodResponse;
        }

        /** @return */
        public int getStatusCode() {
            StatusLine statusLine = methodResponse.getStatusLine();
            return statusLine.getStatusCode();
        }

        @Override
        public void dispose() {
            if (responseBodyAsStream != null) {
                try {
                    responseBodyAsStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }

            if (methodResponse != null) {
                methodResponse = null;
            }
        }

        @Override
        public String getContentType() {
            return getResponseHeader("Content-Type");
        }

        @Override
        public String getResponseHeader(final String headerName) {
            Header responseHeader = methodResponse.getFirstHeader(headerName);
            return responseHeader == null ? null : responseHeader.getValue();
        }

        @Override
        public InputStream getResponseStream() throws IOException {
            if (responseBodyAsStream == null) {
                responseBodyAsStream = methodResponse.getEntity().getContent();
                // commons httpclient does not handle gzip encoding automatically, we have to check
                // ourselves: https://issues.apache.org/jira/browse/HTTPCLIENT-816
                Header header = methodResponse.getFirstHeader("Content-Encoding");
                if (header != null && "gzip".equals(header.getValue())) {
                    responseBodyAsStream = new GZIPInputStream(responseBodyAsStream);
                }
            }
            return responseBodyAsStream;
        }

        /** @see org.geotools.data.ows.HTTPResponse#getResponseCharset() */
        @Override
        public String getResponseCharset() {

            return methodResponse.getEntity().getContentEncoding().getValue();
        }
    }

    /** @see org.geotools.data.ows.HTTPClient#setTryGzip(boolean) */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        this.tryGzip = tryGZIP;
    }

    /** @see org.geotools.data.ows.HTTPClient#isTryGzip() */
    @Override
    public boolean isTryGzip() {
        return tryGzip;
    }

    @Override
    public void close() {
        this.connectionManager.shutdown();
    }
}
