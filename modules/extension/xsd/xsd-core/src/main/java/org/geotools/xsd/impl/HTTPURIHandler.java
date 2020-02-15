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
package org.geotools.xsd.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.geotools.util.logging.Logging;

/**
 * A URI handler that handles HTTP connections with connection timeouts and read timeouts. The
 * default timeouts are 10 seconds, they can be set interactively on an instance of the {@link
 * HTTPURIHandler}, and default values can be overridden setting the <code>
 * org.geotools.xsd.http.connection.timeout</code> and <code>org.geotools.xsd.http.read.timeout
 * </code> system variables.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class HTTPURIHandler extends URIHandlerImpl {

    static final int DEFAULT_CONNECTION_TIMEOUT =
            Integer.getInteger("org.geotools.xsd.http.connectionTimeout", 10000);

    static final int DEFAULT_READ_TIMEOUT =
            Integer.getInteger("org.geotools.xsd.http.readTimeout", 10000);

    static final Logger LOGGER = Logging.getLogger(HTTPURIHandler.class);

    int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    int readTimeout = DEFAULT_READ_TIMEOUT;

    @Override
    public boolean canHandle(URI uri) {
        return "http".equals(uri.scheme()) || "https".equals(uri.scheme());
    }

    /**
     * Creates an input stream for the URI, assuming it's a URL, and returns it.
     *
     * @return an open input stream.
     * @exception IOException if there is a problem obtaining an open input stream.
     */
    @Override
    public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
        try {

            final HttpURLConnection httpConnection = getConnection(uri);
            httpConnection.setConnectTimeout(connectionTimeout);
            httpConnection.setReadTimeout(readTimeout);

            InputStream result = httpConnection.getInputStream();
            Map<Object, Object> response = getResponse(options);
            if (response != null) {
                response.put(
                        URIConverter.RESPONSE_TIME_STAMP_PROPERTY,
                        httpConnection.getLastModified());
            }
            return result;
        } catch (RuntimeException exception) {
            throw new Resource.IOWrappedException(exception);
        }
    }

    protected HttpURLConnection getConnection(URI uri) throws IOException {
        String s = uri.toString();
        LOGGER.log(Level.INFO, s);
        URL url = new URL(s);
        return (HttpURLConnection) url.openConnection();
    }

    /** The current connection timeout */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the connection timeout, in milliseconds. See {@link
     * HttpURLConnection#setConnectTimeout(int)}
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /** The current read timeout */
    public int getReadTimeout() {
        return readTimeout;
    }

    /** Sets the read timeout, in milliseconds. See {@link HttpURLConnection#setReadTimeout(int)} */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
