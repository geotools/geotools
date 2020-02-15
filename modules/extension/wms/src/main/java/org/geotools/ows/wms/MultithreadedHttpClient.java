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
package org.geotools.ows.wms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.geotools.data.ows.AbstractOpenWebService;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.util.logging.Logging;

/**
 * An Apache commons HTTP client based {@link HTTPClient} backed by a multithreaded connection
 * manager that allows to reuse connections to the backing server and to limit the {@link
 * #setMaxConnections(int) max number of concurrent connections}.
 *
 * <p>Java System properties {@code http.proxyHost}, {@code http.proxyPort}, {@code http.proxyUser},
 * and {@code http.proxyPassword} are respected.
 *
 * @author groldan
 * @author awaterme
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 */
public class MultithreadedHttpClient implements HTTPClient {

    private static final Logger LOGGER = Logging.getLogger(MultithreadedHttpClient.class);

    private MultiThreadedHttpConnectionManager connectionManager;

    private HttpClient client;

    private String user;

    private String password;

    private boolean tryGzip = true;

    /** Available if a proxy was specified as system property */
    private HostConfiguration hostConfigNoProxy;

    private Set<String> nonProxyHosts = new HashSet<String>();

    public MultithreadedHttpClient() {
        connectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setSoTimeout(30000);
        params.setConnectionTimeout(30000);
        params.setMaxTotalConnections(6);
        params.setDefaultMaxConnectionsPerHost(6);

        connectionManager.setParams(params);

        client = createHttpClient();

        applySystemProxySettings();
    }

    // package private to support testing || JNH Public for refactoring
    public HttpClient createHttpClient() {
        return new HttpClient(connectionManager);
    }

    private void applySystemProxySettings() {
        final String proxyHost = System.getProperty("http.proxyHost");
        final int proxyPort = Integer.parseInt(System.getProperty("http.proxyPort", "80"));
        String nonProxyHostProp = System.getProperty("http.nonProxyHosts");

        if (proxyHost != null) {
            LOGGER.fine(
                    "Found 'http.proxyHost' Java System property. Using it as proxy server. Port: "
                            + proxyPort);
            HostConfiguration hostConfig = client.getHostConfiguration();
            if (nonProxyHostProp != null) {
                if (nonProxyHostProp.startsWith("\"")) {
                    nonProxyHostProp = nonProxyHostProp.substring(1);
                }
                if (nonProxyHostProp.endsWith("\"")) {
                    nonProxyHostProp = nonProxyHostProp.substring(0, nonProxyHostProp.length() - 1);
                }
                hostConfigNoProxy = (HostConfiguration) hostConfig.clone();
                StringTokenizer tokenizer = new StringTokenizer(nonProxyHostProp, "|");
                while (tokenizer.hasMoreTokens()) {
                    nonProxyHosts.add(tokenizer.nextToken().trim().toLowerCase());
                }
                LOGGER.fine("Initialized with nonProxyHosts: " + nonProxyHosts);
            }
            hostConfig.setProxy(proxyHost, proxyPort);
        }

        final String proxyUser = System.getProperty("http.proxyUser");
        final String proxyPassword = System.getProperty("http.proxyPassword");
        if (proxyUser != null) {
            if (proxyPassword == null || proxyPassword.length() == 0) {
                LOGGER.warning(
                        "System property http.proxyUser provided but http.proxyPassword "
                                + "not provided or empty. Proxy auth credentials will be passed as is anyway.");
            } else {
                LOGGER.fine(
                        "System property http.proxyUser and http.proxyPassword found,"
                                + " setting proxy auth credentials");
            }
            HttpState state = client.getState();
            if (state == null) {
                state = new HttpState();
                client.setState(state);
            }
            AuthScope authscope = AuthScope.ANY;
            Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPassword);
            state.setProxyCredentials(authscope, credentials);
        }
    }

    @Override
    public HTTPResponse post(
            final URL url, final InputStream postContent, final String postContentType)
            throws IOException {

        PostMethod postMethod = new PostMethod(url.toExternalForm());
        postMethod.setDoAuthentication(user != null && password != null);
        if (tryGzip) {
            postMethod.setRequestHeader("Accept-Encoding", "gzip");
        }
        if (postContentType != null) {
            postMethod.setRequestHeader("Content-type", postContentType);
        }
        RequestEntity requestEntity = new InputStreamRequestEntity(postContent);
        postMethod.setRequestEntity(requestEntity);

        int responseCode = executeMethod(postMethod);
        if (200 != responseCode) {
            postMethod.releaseConnection();
            throw new IOException(
                    "Server returned HTTP error code "
                            + responseCode
                            + " for URL "
                            + url.toExternalForm());
        }

        return new HttpMethodResponse(postMethod);
    }

    /** @return the http status code of the execution */
    private int executeMethod(HttpMethod method) throws IOException, HttpException {
        String host = method.getURI().getHost();
        if (host != null && nonProxyHosts.contains(host.toLowerCase())) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Bypassing proxy config due to nonProxyHosts for "
                                + method.getURI().toString());
            }
            return client.executeMethod(hostConfigNoProxy, method);
        }
        return client.executeMethod(method);
    }

    @Override
    public HTTPResponse get(final URL url) throws IOException {
        return this.get(url, null);
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        GetMethod getMethod = new GetMethod(url.toExternalForm());
        getMethod.setDoAuthentication(user != null && password != null);
        if (tryGzip) {
            getMethod.setRequestHeader("Accept-Encoding", "gzip");
        }

        if (headers != null) {
            for (Map.Entry<String, String> headerNameValue : headers.entrySet()) {
                getMethod.setRequestHeader(headerNameValue.getKey(), headerNameValue.getValue());
            }
        }

        int responseCode = executeMethod(getMethod);
        if (200 != responseCode) {
            getMethod.releaseConnection();
            throw new IOException(
                    "Server returned HTTP error code "
                            + responseCode
                            + " for URL "
                            + url.toExternalForm());
        }
        return new HttpMethodResponse(getMethod);
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

    @Override
    public int getConnectTimeout() {
        return connectionManager.getParams().getConnectionTimeout() / 1000;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        connectionManager.getParams().setConnectionTimeout(connectTimeout * 1000);
    }

    @Override
    public int getReadTimeout() {
        return connectionManager.getParams().getSoTimeout() / 1000;
    }

    @Override
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
                methodResponse.releaseConnection();
                methodResponse = null;
            }
        }

        @Override
        public String getContentType() {
            return getResponseHeader("Content-Type");
        }

        @Override
        public String getResponseHeader(final String headerName) {
            Header responseHeader = methodResponse.getResponseHeader(headerName);
            return responseHeader == null ? null : responseHeader.getValue();
        }

        @Override
        public InputStream getResponseStream() throws IOException {
            if (responseBodyAsStream == null) {
                responseBodyAsStream = methodResponse.getResponseBodyAsStream();
                // commons httpclient does not handle gzip encoding automatically, we have to check
                // ourselves: https://issues.apache.org/jira/browse/HTTPCLIENT-816
                Header header = methodResponse.getResponseHeader("Content-Encoding");
                if (header != null && "gzip".equals(header.getValue())) {
                    responseBodyAsStream = new GZIPInputStream(responseBodyAsStream);
                }
            }
            return responseBodyAsStream;
        }

        /** @see org.geotools.data.ows.HTTPResponse#getResponseCharset() */
        @Override
        public String getResponseCharset() {
            String responseCharSet = null;
            if (methodResponse instanceof HttpMethodBase) {
                responseCharSet = ((HttpMethodBase) methodResponse).getResponseCharSet();
            }
            return responseCharSet;
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
}
