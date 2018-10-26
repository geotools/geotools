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
import org.geotools.data.ows.Capabilities;
import org.geotools.data.ows.GetCapabilitiesResponse;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * Provides a hook up to parse the capabilties document from inputstream.
 *
 * @author Richard Gould
 */
public class WMSGetCapabilitiesResponse extends GetCapabilitiesResponse {

    public WMSGetCapabilitiesResponse(HTTPResponse response) throws ServiceException, IOException {
        this(response, null);
    }

    public WMSGetCapabilitiesResponse(HTTPResponse response, Map<String, Object> hints)
            throws ServiceException, IOException {
        super(response);

        try {
            hints = hints == null ? new HashMap<>() : new HashMap<>(hints);
            hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
            if (!hints.containsKey(DocumentFactory.VALIDATION_HINT)) {
                hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
            }
            Object object;
            InputStream inputStream = null;
            try {
                inputStream = response.getResponseStream();
                object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
            } catch (SAXException e) {
                throw (ServiceException)
                        new ServiceException("Error while parsing XML.").initCause(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            if (object instanceof ServiceException) {
                throw (ServiceException) object;
            }

            this.capabilities = (Capabilities) object;
        } finally {
            response.dispose();
        }
    }
}
