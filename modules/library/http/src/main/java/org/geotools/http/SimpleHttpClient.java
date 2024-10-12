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
package org.geotools.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.Base64;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;

/**
 * A simple {@link HTTPClient} that creates a new {@link HttpURLConnection HTTP connection} for each
 * request.
 *
 * @author groldan
 */
public class SimpleHttpClient extends AbstractHttpClient implements HTTPProxy {

    private static final Logger LOGGER = Logging.getLogger(SimpleHttpClient.class);

    private static final int DEFAULT_TIMEOUT = 30; // 30 seconds

    /** Max number of attempts to follow a redirect to avoid redirect loop * */
    private static final int MAX_FOLLOW_REDIRECT =
            Integer.getInteger("org.geotools.http.simpleHttpClient.maxFollowRedirect", 16);

    /**
     * A SimpleHttpClient should be initiated by a call to
     * HTTPFactoryFinder.getHttpClientFactory().getClient();
     */
    public SimpleHttpClient() {
        this.connectTimeout = DEFAULT_TIMEOUT;
        this.readTimeout = DEFAULT_TIMEOUT;
    }

    /** @see org.geotools.http.HTTPClient#get(java.net.URL) */
    @Override
    public HTTPResponse get(final URL url) throws IOException {
        return this.get(url, null);
    }

    /** @see org.geotools.http.HTTPClient#get(URL, Map) */
    @Override
    public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "URL is " + url);

        if (isFile(url)) {
            return this.createFileResponse(url);
        }

        URLConnection connection = this.get(url, headers, 0);
        if (connection == null) {
            return null;
        }
        return new DefaultHttpResponse(connection);
    }

    /** @see org.geotools.http.HTTPClient#post(URL, InputStream, String, Map) */
    @Override
    public HTTPResponse post(URL url, InputStream content, String contentType) throws IOException {
        return post(url, content, contentType, null);
    }

    /** @see org.geotools.http.HTTPClient#post(URL, InputStream, String) */
    @Override
    public HTTPResponse post(
            final URL url, final InputStream postContent, final String postContentType, Map<String, String> headers)
            throws IOException {

        if (headers == null) {
            headers = new HashMap<>(1);
        } else {
            headers = new HashMap<>(headers);
        }
        if (postContentType != null) {
            headers.put("Content-type", postContentType);
        }
        URLConnection connection = openConnection(url, headers);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("POST");
        }
        connection.setDoOutput(true);

        connection.connect();

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] buff = new byte[512];
            int count;
            while ((count = postContent.read(buff)) > -1) {
                outputStream.write(buff, 0, count);
            }
            outputStream.flush();
        }

        return new DefaultHttpResponse(connection);
    }

    private URLConnection get(URL url, Map<String, String> headers, int redirectionCount) throws IOException {

        URLConnection connection = openConnection(url, headers);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("GET");
        }
        connection.connect();

        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            if (hasRedirect(httpConnection.getResponseCode())) {
                return this.followRedirect(httpConnection, headers, redirectionCount);
            }
        }

        return connection;
    }

    private URLConnection openConnection(URL finalURL, Map<String, String> headers) throws IOException {
        Map<String, String> extraParams = getExtraParams();
        if (!extraParams.isEmpty()) {
            finalURL = appendURL(finalURL, extraParams);
        }

        URLConnection connection = finalURL.openConnection();
        final boolean http = connection instanceof HttpURLConnection;
        if (headers == null) {
            headers = new HashMap<>();
        } else {
            headers = new HashMap<>(headers); // avoid parameter modification
        }

        if (http && tryGzip) {
            headers.put("Accept-Encoding", "gzip");
        }
        // mind, connect timeout is in seconds
        if (http && getConnectTimeout() > 0) {
            connection.setConnectTimeout(1000 * getConnectTimeout());
        }
        if (http && getReadTimeout() > 0) {
            connection.setReadTimeout(1000 * getReadTimeout());
        }

        final String username = getUser();
        final String password = getPassword();

        if (http && username != null && password != null) {
            String userpassword = username + ":" + password;
            String encodedAuthorization =
                    Base64.encodeBytes(userpassword.getBytes(StandardCharsets.UTF_8), Base64.DONT_BREAK_LINES);
            headers.put("Authorization", "Basic " + encodedAuthorization);
        }

        // Set User-Agent to a good default
        headers.put("User-Agent", "GeoTools HTTPClient (" + GeoTools.getVersion() + ")");

        for (Map.Entry<String, String> headerNameValue : headers.entrySet()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE, "Setting header " + headerNameValue.getKey() + " = " + headerNameValue.getValue());
            }
            connection.setRequestProperty(headerNameValue.getKey(), headerNameValue.getValue());
        }
        return connection;
    }

    private static boolean hasRedirect(int responseCode) {
        return responseCode == HttpURLConnection.HTTP_SEE_OTHER
                || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                || responseCode == HttpURLConnection.HTTP_MOVED_TEMP;
    }

    private URLConnection followRedirect(
            HttpURLConnection connection, Map<String, String> headers, int redirectionCount) throws IOException {
        String redirect = connection.getHeaderField("Location");
        if (redirect == null) {
            LOGGER.warning("Tried to follow redirect but no url was provided in Location header");
        } else if (redirectionCount < MAX_FOLLOW_REDIRECT) {
            redirectionCount++;
            try {
                URL redirectURL = new URI(redirect).toURL();
                LOGGER.fine("Following redirect to " + redirect);
                return this.get(redirectURL, headers, redirectionCount);
            } catch (URISyntaxException | MalformedURLException uri) {
                LOGGER.warning("Tried to follow redirect but invalid url was provided in Location header: " + redirect);
            }
        } else {
            LOGGER.warning(
                    "Max number of follow redirect attempts (" + MAX_FOLLOW_REDIRECT + ") reached. Returning null");
        }
        return null;
    }
}
