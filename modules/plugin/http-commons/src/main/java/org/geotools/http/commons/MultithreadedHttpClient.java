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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.routing.RoutingSupport;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.util.Timeout;
import org.geotools.data.ows.AbstractOpenWebService;
import org.geotools.http.AbstractHttpClient;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.http.HTTPProxy;
import org.geotools.http.HTTPResponse;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;

/**
 * An Apache commons HTTP client based {@link HTTPClient} backed by a multithreaded connection manager that allows to
 * reuse connections to the backing server and to limit the {@link #setMaxConnections(int) max number of concurrent
 * connections}.
 *
 * <p>Java System properties {@code http.proxyHost}, {@code http.proxyPort}, {@code http.proxyUser}, and
 * {@code http.proxyPassword} are respected.
 *
 * <p>Copied from gt-wms.
 *
 * @author groldan
 * @author awaterme
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 */
public class MultithreadedHttpClient extends AbstractHttpClient implements HTTPConnectionPooling, HTTPProxy {

    private static final Logger LOGGER = Logging.getLogger(MultithreadedHttpClient.class);

    private final PoolingHttpClientConnectionManager connectionManager;

    private HttpClient client;

    private RequestConfig connectionConfig;

    private AuthScope authScope;

    public MultithreadedHttpClient() {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(6);
        connectionManager.setDefaultMaxPerRoute(6);
        connectionConfig = RequestConfig.custom()
                .setCookieSpec(StandardCookieSpec.RELAXED)
                .setExpectContinueEnabled(true)
                .setResponseTimeout(Timeout.ofSeconds(30))
                .setConnectTimeout(Timeout.ofSeconds(30))
                .build();

        client = builder().build();
    }

    private BasicCredentialsProvider credsProvider = null;

    private HttpClientBuilder builder() {
        HttpClientBuilder builder = HttpClientBuilder.create()
                .setUserAgent("GeoTools/%s (%s)"
                        .formatted(GeoTools.getVersion(), this.getClass().getSimpleName()))
                .useSystemProperties()
                .setConnectionManager(connectionManager);
        if (credsProvider != null) {
            builder.setDefaultCredentialsProvider(credsProvider);
        }
        return builder;
    }

    @Override
    public HttpMethodResponse post(final URL url, final InputStream postContent, final String postContentType)
            throws IOException {
        return post(url, postContent, postContentType, null);
    }

    @Override
    public HttpMethodResponse post(
            URL url, InputStream postContent, String postContentType, Map<String, String> headers) throws IOException {

        if (headers == null) {
            headers = new HashMap<>();
        } else {
            headers = new HashMap<>(headers); // avoid parameter modification
        }
        HttpPost postMethod = new HttpPost(url.toExternalForm());
        postMethod.setConfig(connectionConfig);
        HttpEntity requestEntity;
        if (credsProvider != null) {
            // we can't read the input stream twice as would be needed if the server asks us to
            // authenticate
            String input = new BufferedReader(new InputStreamReader(postContent, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            requestEntity = new StringEntity(input);
        } else {
            requestEntity = new InputStreamEntity(postContent, ContentType.create(postContentType));
        }
        if (tryGzip) {
            headers.put("Accept-Encoding", "gzip");
        }
        if (postContentType != null) {
            headers.put("Content-type", postContentType);
        }

        setHeadersOn(headers, postMethod);

        postMethod.setEntity(requestEntity);

        HttpMethodResponse response = null;
        try {
            response = executeMethod(postMethod);
        } catch (HttpException | URISyntaxException e) {
            throw new IOException(e);
        }
        if (200 != response.getStatusCode()) {
            throw new IOException(
                    "Server returned HTTP error code " + response.getStatusCode() + " for URL " + url.toExternalForm());
        }

        return response;
    }

    /** @return the http status code of the execution */
    private HttpMethodResponse executeMethod(org.apache.hc.client5.http.classic.methods.HttpUriRequestBase method)
            throws IOException, HttpException, URISyntaxException {

        HttpClientContext localContext = HttpClientContext.create();
        ClassicHttpResponse resp;
        if (credsProvider != null) {
            localContext.setCredentialsProvider(credsProvider);
            // see https://stackoverflow.com/a/21592593
            AuthCache authCache = new BasicAuthCache();
            URI target = method.getUri();
            BasicScheme basicScheme = new BasicScheme();
            basicScheme.initPreemptive(credsProvider.getCredentials(authScope, localContext));
            authCache.put(new HttpHost(target.getScheme(), target.getHost(), target.getPort()), basicScheme);
            localContext.setAuthCache(authCache);
        }
        resp = client.executeOpen(RoutingSupport.determineHost(method), method, localContext);

        HttpMethodResponse response = new HttpMethodResponse(resp);

        return response;
    }

    @Override
    public HTTPResponse get(final URL url) throws IOException {
        return this.get(url, null);
    }

    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {

        if (isFile(url)) {
            return createFileResponse(url);
        }
        if (headers == null) {
            headers = new HashMap<>();
        } else {
            headers = new HashMap<>(headers); // avoid parameter modification
        }

        Map<String, String> extraParams = getExtraParams();
        if (!extraParams.isEmpty()) {
            url = appendURL(url, extraParams);
        }

        HttpGet getMethod = new HttpGet(url.toExternalForm());
        getMethod.setConfig(connectionConfig);

        if (tryGzip) {
            headers.put("Accept-Encoding", "gzip");
        }

        setHeadersOn(headers, getMethod);

        HttpMethodResponse response = null;
        try {
            response = executeMethod(getMethod);
        } catch (HttpException e) {
            throw new IOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (200 != response.getStatusCode()) {
            throw new IOException(
                    "Server returned HTTP error code " + response.getStatusCode() + " for URL " + url.toExternalForm());
        }
        return response;
    }

    private void setHeadersOn(
            Map<String, String> headers, org.apache.hc.client5.http.classic.methods.HttpUriRequestBase request) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Setting header " + header.getKey() + " = " + header.getValue());
            }
            request.setHeader(header.getKey(), header.getValue());
        }
    }

    @Override
    public void setUser(String user) {
        super.setUser(user);
        resetCredentials();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        resetCredentials();
    }

    private void resetCredentials() {
        if (user != null && password != null) {
            authScope = new AuthScope(null, -1);
            Credentials credentials = new UsernamePasswordCredentials(user, password.toCharArray());
            // TODO - check if this works for all types of auth or do we need to look it up?
            credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(authScope, credentials);
            client = builder().build();
        } else if (credsProvider != null) {
            credsProvider = null;
            client = builder().build();
        }
    }

    @Override
    public int getConnectTimeout() {
        return (int) connectionConfig.getConnectTimeout().toSeconds();
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        connectionConfig = RequestConfig.copy(connectionConfig)
                .setConnectTimeout(Timeout.ofSeconds(connectTimeout))
                .build();
    }

    @Override
    public int getReadTimeout() {
        return (int) connectionConfig.getResponseTimeout().toSeconds();
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        connectionConfig = RequestConfig.copy(connectionConfig)
                .setResponseTimeout(Timeout.ofSeconds(readTimeout))
                .build();
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

    @Override
    public void close() {
        this.connectionManager.close();
    }

    static class HttpMethodResponse implements HTTPResponse {

        private ClassicHttpResponse methodResponse;

        private InputStream responseBodyAsStream;

        public HttpMethodResponse(final ClassicHttpResponse methodResponse) {
            this.methodResponse = methodResponse;
        }

        /** @return */
        public int getStatusCode() {
            if (methodResponse != null) {
                return methodResponse.getCode();
            } else {
                return -1;
            }
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
                try {
                    methodResponse.close();
                } catch (IOException e) {
                    // ignore
                }
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
            final Header encoding = new BasicHeader(
                    HttpHeaders.CONTENT_ENCODING, methodResponse.getEntity().getContentEncoding());
            return encoding == null ? null : encoding.getValue();
        }
    }
}
