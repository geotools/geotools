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
import java.util.Map;

/**
 * Defines the inteface by which an {@link AbstractOpenWebService} executes HTTP requests.
 *
 * @author groldan
 * @see HTTPResponse
 * @see SimpleHttpClient
 * @see AbstractOpenWebService#setHttpClient(HTTPClient)
 */
public interface HTTPClient {

    /**
     * Executes an HTTP POST request against the provided URL, sending the contents of {@code
     * postContent} as the POST method body and setting the Content-Type request header to {@code
     * postContentType} if given, and returns the server response.
     *
     * <p>If an HTTP authentication {@link #getUser() user} and {@link #getPassword() password} is
     * set, the appropriate authentication HTTP header will be sent with the request.
     *
     * <p>If a {@link #getConnectTimeout() connection timeout} is set, the http connection will be
     * set to respect that timeout.
     *
     * <p>If a {@link #getReadTimeout() read timeout} is set, the http connection will be set to
     * respect it.
     *
     * @param url the URL against which to execute the POST request
     * @param postContent an input stream with the contents of the POST body
     * @param postContentType the MIME type of the contents sent as the request POST body, can be
     *     {@code null}
     * @return the {@link HTTPResponse} encapsulating the response to the HTTP POST request
     */
    HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException;

    /**
     * Executes an HTTP GET request against the provided URL and returns the server response.
     *
     * <p>If an HTTP authentication {@link #getUser() user} and {@link #getPassword() password} is
     * set, the appropriate authentication HTTP header will be sent with the request.
     *
     * <p>If a {@link #getConnectTimeout() connection timeout} is set, the http connection will be
     * set to respect that timeout.
     *
     * <p>If a {@link #getReadTimeout() read timeout} is set, the http connection will be set to
     * respect it.
     *
     * @param url the URL to retrieve
     * @return an {@link HTTPResponse} encapsulating the response to the HTTP GET request
     */
    HTTPResponse get(URL url) throws IOException;

    /**
     * Executes an HTTP GET request against the provided URL and returns the server response.
     *
     * <p>If an HTTP authentication {@link #getUser() user} and {@link #getPassword() password} is
     * set, the appropriate authentication HTTP header will be sent with the request.
     *
     * <p>If a {@link #getConnectTimeout() connection timeout} is set, the http connection will be
     * set to respect that timeout.
     *
     * <p>If a {@link #getReadTimeout() read timeout} is set, the http connection will be set to
     * respect it.
     *
     * <p>header parameter contains additional headers to add to the request.
     *
     * @param url the URL to retrieve
     * @param headers a list of custom headers to add to the request.
     * @return an {@link HTTPResponse} encapsulating the response to the HTTP GET request
     */
    default HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
        return get(url);
    }

    /** @return the HTTP BASIC Authentication user name, or {@code null} if not set */
    public String getUser();

    /** @param user the HTTP BASIC Authentication user name */
    public void setUser(String user);

    /** @return the HTTP BASIC Authentication password, or {@code null} if not set */
    public String getPassword();

    /** @param password the HTTP BASIC Authentication password */
    public void setPassword(String password);

    /** @return the tcp/ip connect timeout in seconds */
    public int getConnectTimeout();

    /** @param connectTimeout tcp/ip connect timeout in seconds */
    public void setConnectTimeout(int connectTimeout);

    /** @return the socket read timeout in seconds */
    public int getReadTimeout();

    /** @param readTimeout socket read timeout in seconds */
    public void setReadTimeout(int readTimeout);

    /** @param tryGZIP */
    void setTryGzip(boolean tryGZIP);

    /** @return whether gzip content encoding will be attempted; defaults to {@code false} */
    boolean isTryGzip();
}
