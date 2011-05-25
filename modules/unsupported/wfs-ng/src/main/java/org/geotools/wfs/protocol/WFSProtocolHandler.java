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

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;
import static org.geotools.data.wfs.protocol.wfs.WFSOperationType.DESCRIBE_FEATURETYPE;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.util.logging.Logging;

/**
 * Handles setting up connections to a WFS based on a WFS capabilities document,
 * taking care of GZIP and authentication.
 * 
 * @author Gabriel Roldan
 * @version $Id: WFSProtocolHandler.java 31823 2008-11-11 16:11:49Z groldan $
 * @since 2.5.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/wfs/protocol/WFSProtocolHandler.java $
 * @deprecated use {@link WFSProtocol}
 */
public abstract class WFSProtocolHandler {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private Version wfsVersion;

    protected final ConnectionFactory connectionFac;

    public WFSProtocolHandler(final Version version, final ConnectionFactory connectionFac) {
        this.wfsVersion = version;
        this.connectionFac = connectionFac;
    }

    public Version getVersion() {
        return wfsVersion;
    }

    /**
     * Returns whether the service supports the given operation for the given
     * HTTP method.
     * 
     * @param operation
     * @param method
     * @return
     */
    public abstract boolean supports(WFSOperationType operation, HttpMethod method);

    /**
     * 
     * @param operation
     * @param method
     * @return The URL access point for the given operation and method
     * @throws UnsupportedOperationException
     *             if the combination operation/method is not supported by the
     *             service
     * @see #supports(WFSOperationType, HttpMethod)
     */
    public abstract URL getOperationURL(WFSOperationType operation, HttpMethod method)
            throws UnsupportedOperationException;

    /**
     * Returns the preferred character encoding name to encode requests in
     * 
     * @return
     */
    public String getEncoding() {
        return connectionFac.getEncoding().name();
    }

    public HttpURLConnection createDescribeFeatureTypeConnection(final String typeName,
            HttpMethod method) throws IOException, IllegalArgumentException {
        URL query;
        if (HttpMethod.POST == method) {
            query = getOperationURL(WFSOperationType.DESCRIBE_FEATURETYPE, HttpMethod.POST);
        } else {
            query = getDescribeFeatureTypeURLGet(typeName);
        }
        if (query == null) {
            return null;
        }
        return connectionFac.getConnection(query, method);
    }

    public URL getDescribeFeatureTypeURLGet(final String typeName) throws MalformedURLException {
        URL getUrl = getOperationURL(DESCRIBE_FEATURETYPE, GET);
        Logging.getLogger("org.geotools.data.communication").fine("Output: " + getUrl);

        String query = getUrl.getQuery();
        query = query == null ? null : query.toUpperCase();
        String url = getUrl.toString();

        if ((query == null) || "".equals(query)) {
            if ((url == null) || !url.endsWith("?")) {
                url += "?";
            }

            url += "SERVICE=WFS";
        } else {
            if (query.indexOf("SERVICE=WFS") == -1) {
                url += "&SERVICE=WFS";
            }
        }

        if ((query == null) || (query.indexOf("VERSION") == -1)) {
            url += "&VERSION=" + getVersion();
        }

        if ((query == null) || (query.indexOf("REQUEST") == -1)) {
            url += "&REQUEST=DescribeFeatureType";
        }

        url += ("&TYPENAME=" + typeName);

        getUrl = new URL(url);
        return getUrl;
    }
}
