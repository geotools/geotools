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
package org.geotools.data.ws.protocol.ws;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

/**
 * A handle to a WFS response that contains the input stream to the actual contents and some well
 * known response information derived from the HTTP response headers.
 * 
 * @author rpetty
 * @version $Id$
 * @since 2.6
 * @source $URL$
 */
@SuppressWarnings("nls")
public class WSResponse {

    private InputStream inputStream;

    /**
     * @param charset
     *            the response charset, {@code null} if unknown, utf-8 will be assumed then
     * @param contentType
     *            the response content type
     * @param in
     *            the response input stream ready to be consumed
     */
    public WSResponse(InputStream in) {
        this.inputStream = in;
    }

    /**
     * The open input stream for the response contents
     * 
     * @return the input stream for the response
     */
    public InputStream getInputStream() {
        return inputStream;
    }
}
