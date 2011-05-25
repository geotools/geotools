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
import java.io.InputStream;

/**
 * A handle to an HTTP response headers and contents.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 *
 * @source $URL$
 */
public interface HTTPResponse {

    /**
     * @return the URL the request producing this response was sent to
     */
    public String getTargetUrl();

    /**
     * Returns the stream to the response contents ready to be consumed, whether gzip encoding is
     * being used or not.
     * 
     * @throws IOException
     *             the plain stream to the response contents
     */
    public InputStream getResponseStream() throws IOException;

    /**
     * Returns the value of the given HTTP response header.
     * 
     * @param headerName
     *            the response header name to get the value of
     * @return the value the HTTP server set for the given response header name, may be {@code null}
     */
    public String getResponseHeader(String headerName);

    /**
     * Returns the response charset identifier as extracted from the HTTP header, if any.
     * 
     * @return the charset or {@code null} if not indicated by the server.
     */
    public String getResponseCharset();

    /**
     * Shortcut method to get the response content-type header
     */
    public String getContentType();
}
