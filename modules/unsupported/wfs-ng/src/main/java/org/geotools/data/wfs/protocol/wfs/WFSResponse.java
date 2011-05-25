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
package org.geotools.data.wfs.protocol.wfs;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.geotools.util.logging.Logging;

/**
 * A handle to a WFS response that contains the input stream to the actual contents and some well
 * known response information derived from the HTTP response headers.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: WFSResponse.java 31888 2008-11-20 13:34:53Z groldan $
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/protocol/wfs/WFSResponse.java $
 */
@SuppressWarnings("nls")
public class WFSResponse {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs.protocol.wfs");

    private Charset charset;

    private String contentType;

    private InputStream inputStream;

    private EObject request;

    private String targetUrl;

    /**
     * @param charset
     *            the response charset, {@code null} if unknown, utf-8 will be assumed then
     * @param contentType
     *            the response content type
     * @param in
     *            the response input stream ready to be consumed
     */
    public WFSResponse(String targetUrl, EObject originatingRequest, Charset charset,
            String contentType, InputStream in) {
        this.targetUrl = targetUrl;
        this.request = originatingRequest;
        if (charset == null) {
            this.charset = Charset.forName("UTF-8");
        } else {
            this.charset = charset;
        }
        this.contentType = contentType;
        this.inputStream = in;
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("WFS response: charset=" + charset + ", contentType=" + contentType);
        }
    }

    /**
     * Returns the character encoding if set by the server as an http header, if unknown assumes
     * {@code UTF-8}
     * 
     * @return the character set for the response if set, or {@code null}
     */
    public Charset getCharacterEncoding() {
        return charset;
    }

    /**
     * Returns the WFS response declared content type
     * 
     * @return the content type of the response
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * The open input stream for the response contents
     * 
     * @return the input stream for the response
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Allows to replace the input stream
     * 
     * @param in
     */
    public void setInputStream(InputStream in) {
        this.inputStream = in;
    }

    public EObject getOriginatingRequest() {
        return request;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    @Override
    public String toString() {
        return new StringBuilder("WFSResponse[charset=").append(charset).append(", contentType=")
                .append(contentType).append("]").toString();
    }
}
