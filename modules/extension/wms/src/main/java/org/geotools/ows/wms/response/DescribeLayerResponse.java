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
package org.geotools.ows.wms.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.LayerDescription;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * Represents the response from a server after a DescribeLayer request has been issued.
 *
 * @author Richard Gould
 */
public class DescribeLayerResponse extends Response {

    private LayerDescription[] layerDescs;

    public DescribeLayerResponse(HTTPResponse httpResponse) throws IOException, ServiceException {
        this(httpResponse, null);
    }

    public DescribeLayerResponse(HTTPResponse httpResponse, Map<String, Object> hints)
            throws IOException, ServiceException {
        super(httpResponse);

        try {
            hints = hints == null ? new HashMap<>() : new HashMap<>(hints);
            hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());

            Object object;
            InputStream inputStream = null;
            try {
                inputStream = getInputStream();
                object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
            } catch (SAXException e) {
                throw (IOException) new IOException().initCause(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            layerDescs = (LayerDescription[]) object;
        } finally {
            dispose();
        }
    }

    public LayerDescription[] getLayerDescs() {
        return layerDescs;
    }
}
