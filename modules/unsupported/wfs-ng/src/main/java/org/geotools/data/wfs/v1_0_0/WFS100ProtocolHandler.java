/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.protocol.ConnectionFactory;
import org.geotools.wfs.protocol.WFSProtocolHandler;
import org.geotools.xml.DocumentFactory;
import org.xml.sax.SAXException;

public class WFS100ProtocolHandler extends WFSProtocolHandler {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private WFSCapabilities capabilities;

    public WFS100ProtocolHandler(InputStream capabilitiesReader, ConnectionFactory connectionFac) throws IOException {
        super(Version.v1_0_0, connectionFac);
        capabilities = parseCapabilities(capabilitiesReader);
    }

    public WFSCapabilities getCapabilities() {
        return capabilities;
    }

    public ConnectionFactory getConnectionFactory(){
        return super.connectionFac;
    }
    
    @SuppressWarnings("unchecked")
    private WFSCapabilities parseCapabilities(InputStream capabilitiesReader) throws IOException {
        // TODO: move to some 1.0.0 specific class
        Map hints = new HashMap();
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);

        Object parsed;
        try {
            parsed = DocumentFactory.getInstance(capabilitiesReader, hints, LOGGER.getLevel());
        } catch (SAXException e) {
            throw new DataSourceException("Error parsing WFS 1.0.0 capabilities", e);
        }

        if (parsed instanceof WFSCapabilities) {
            return (WFSCapabilities) parsed;
        } else {
            throw new DataSourceException(
                    "The specified URL Should have returned a 'WFSCapabilities' object. Returned a "
                            + ((parsed == null) ? "null value."
                                    : (parsed.getClass().getName() + " instance.")));
        }
    }

    @Override
    public URL getOperationURL(WFSOperationType operation, HttpMethod method)
            throws UnsupportedOperationException {
        OperationType operationType;
        switch (operation) {
        case DESCRIBE_FEATURETYPE:
            operationType = capabilities.getDescribeFeatureType();
            break;
        case GET_CAPABILITIES:
            operationType = capabilities.getGetCapabilities();
            break;
        case GET_FEATURE:
            operationType = capabilities.getGetFeature();
            break;
        case GET_FEATURE_WITH_LOCK:
            operationType = capabilities.getGetFeatureWithLock();
            break;
        case LOCK_FEATURE:
            operationType = capabilities.getLockFeature();
            break;
        case TRANSACTION:
            operationType = capabilities.getTransaction();
            break;
        default:
            throw new IllegalArgumentException("Unknown operation type " + operation);
        }
        if (operationType == null) {
            throw new UnsupportedOperationException(operation + " not supported by the server");
        }
        URL url;
        if (GET == method) {
            url = operationType.getGet();
        } else {
            url = operationType.getPost();
        }
        if (url == null) {
            throw new UnsupportedOperationException("Method " + method + " for " + operation
                    + " is not supported by the server");
        }
        return url;
    }

    @Override
    public boolean supports(WFSOperationType operation, HttpMethod method) {
        try {
            getOperationURL(operation, method);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

}
