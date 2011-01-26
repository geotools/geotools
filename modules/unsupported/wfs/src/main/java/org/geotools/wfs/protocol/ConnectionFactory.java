/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.protocol.http.HttpMethod;

/**
 * Interface to abstract out the plain connection and stream set up against the
 * target WFS
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL$
 * @deprecated in favour of {@link HTTPProtocol}
 */
public interface ConnectionFactory {

    String getAuthUsername();

    String getAuthPassword();

    boolean isTryGzip();

    /**
     * Returns the preferred character encoding name to encode requests in
     * 
     * @return
     */
    Charset getEncoding();

    /**
     * Creates and returns a connection object for the supplied URL, settled up
     * for the given HTTP method (GET or POST) and this connection factory
     * tryGzip and authentication settings.
     * 
     * @param query
     * @param method
     * @return
     * @throws IOException
     */
    HttpURLConnection getConnection(URL query, HttpMethod method) throws IOException;

    InputStream getInputStream(HttpURLConnection hc) throws IOException;

    /**
     * Shortcut for
     * {@code conn = getConnection(url, method); getInputStream(conn);}
     * 
     * @param query
     * @param method
     * @return
     * @throws IOException
     */
    InputStream getInputStream(URL query, HttpMethod method) throws IOException;
}
