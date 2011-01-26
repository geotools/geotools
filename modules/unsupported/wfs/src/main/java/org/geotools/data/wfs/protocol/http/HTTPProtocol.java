/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * A facade interface to interact with a plain HTTP server.
 * <p>
 * This interface provides a mean for plain HTTP conversation. That is, issueing HTTP requests both
 * through GET and POST methods, handling gzip encoding/decoding if supported by the server, URL
 * encoding for query string parameters, and handling HTTP authentication.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 * @source $URL:
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/protocol/http/HTTPProtocol.java $
 */
public interface HTTPProtocol {

    public interface POSTCallBack {
        
        public String getContentType();
        
        public long getContentLength();

        public void writeBody(OutputStream out) throws IOException;
    }

    /**
     * Sets whether the server should be asked to return responses encoded in GZIP.
     * 
     * @param tryGzip
     *            {@code true} to ask the server to encode responses in GZIP.
     */
    public void setTryGzip(boolean tryGzip);

    /**
     * Returns whether gzip encoding is attempted when interacting with the HTTP server; default is
     * {@code false}
     * 
     * @return {@code true} if gzip is being attempted.
     */
    public boolean isTryGzip();

    /**
     * Sets the HTTP authentication realms (not required/used so far, but intended to be)
     * 
     * @param username
     * @param password
     */
    public void setAuth(String username, String password);

    /**
     * Sets the request timeout in milliseconds.
     * 
     * @param milliseconds
     */
    public void setTimeoutMillis(int milliseconds);

    /**
     * Returns the request timeout in milliseconds, defaults to -1 meaning no timeout
     * 
     * @return
     */
    public int getTimeoutMillis();

    /**
     * Issues an HTTP request over the {@code baseUrl} with a query string defined by the {@code
     * kvp} key/value pair of parameters.
     * <p>
     * If the base url query is not empty and already contains a parameter named as one of the
     * parameters in {@code kvp}, the original parameter value in the baseUrl query is overriden by
     * the one in the {@code kvp} map. For this purpose, the parameter name matching comparison is
     * made case insensitively.
     * </p>
     * 
     * @param baseUrl
     *            the URL where to fetch the contents from
     * @param kvp
     *            the set of key/value pairs to create the actual URL query string, may be empty
     * @return the server response of issuing the HTTP request through GET method
     * @throws IOException
     *             if a communication error of some sort occurs
     * @see #createUrl(URL, Map)
     */
    public HTTPResponse issueGet(URL baseUrl, Map<String, String> kvp) throws IOException;

    public HTTPResponse issuePost(final URL targetUrl, final POSTCallBack callback)
            throws IOException;

    /**
     * Creates an URL with {@code baseUrl} and a query string defined by the {@code kvp} key/value
     * pair of parameters.
     * <p>
     * If the base url query is not empty and already contains a parameter named as one of the
     * parameters in {@code kvp}, the original parameter value in the baseUrl query is overriden by
     * the one in the {@code kvp} map. For this purpose, the parameter name matching comparison is
     * made case insensitively.
     * </p>
     * 
     * @param baseUrl
     *            the original URL to create the new one from
     * @param kvp
     *            the set of key/value pairs to create the actual URL query string, may be empty
     * @return the new URL with {@code baseUrl} and the query string from {@code queryStringKvp}
     * @throws MalformedURLException
     *             if the resulting URL is not valid
     */
    public URL createUrl(URL baseUrl, Map<String, String> queryStringKvp)
            throws MalformedURLException;

}
