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
package org.geotools.data.ws.protocol.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

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

    public HTTPResponse issuePost(final URL targetUrl, final POSTCallBack callback)
            throws IOException;

}
