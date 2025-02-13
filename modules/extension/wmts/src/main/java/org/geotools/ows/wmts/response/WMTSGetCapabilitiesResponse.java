/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.wmts.response;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.geotools.data.ows.GetCapabilitiesResponse;
import org.geotools.data.ows.ServiceExceptionParser;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.util.logging.Logging;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.xml.sax.SAXException;

/**
 * Provides a hook up to parse the capabilities document from input stream.
 *
 * <p>(Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSGetCapabilitiesResponse extends GetCapabilitiesResponse {
    private static final Logger LOGGER = Logging.getLogger(WMTSGetCapabilitiesResponse.class);
    private static WMTSConfiguration WMTS_CONFIGURATION = new WMTSConfiguration();

    private static final int MAX_BUFFER_SIZE = 2048; // Should be enough to support the size of a ServiceException.

    public WMTSGetCapabilitiesResponse(HTTPResponse response) throws ServiceException, IOException {
        this(response, null);
    }

    /**
     * Constructor that sets capabilities property. Input stream of response is closed immediately.
     *
     * @param response the httpResponse from the server
     * @param hints not used
     * @throws ServiceException thrown if server responds with ServiceException
     * @throws IOException thrown if xml or input stream is wrong
     */
    public WMTSGetCapabilitiesResponse(HTTPResponse response, Map<String, Object> hints)
            throws ServiceException, IOException {
        super(response);
        Object object;
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        try (InputStream inputStream = new BufferedInputStream(response.getResponseStream(), MAX_BUFFER_SIZE)) {
            inputStream.mark(MAX_BUFFER_SIZE);
            int bytesRead = inputStream.read(buffer, 0, MAX_BUFFER_SIZE);
            inputStream.reset();

            try {
                Parser parser = new Parser(WMTS_CONFIGURATION);
                object = parser.parse(inputStream);
            } catch (SAXException | ParserConfigurationException e) {
                throw new IOException("Error while parsing XML.", e);
            }
            if (object instanceof CapabilitiesType) {
                this.capabilities = new WMTSCapabilities((CapabilitiesType) object);
            } else {
                try (InputStream bufferedStream = new ByteArrayInputStream(buffer, 0, bytesRead)) {
                    object = ServiceExceptionParser.parse(bufferedStream);
                    if (object instanceof ServiceException) {
                        LOGGER.log(Level.SEVERE, "Server returned ServiceException.", (Throwable) object);
                        throw (ServiceException) object;
                    }
                } catch (IOException e) {
                    LOGGER.log(
                            Level.SEVERE, "Server returned xml that couldn't be parsed by ServiceExceptionParser.", e);
                }
                throw new IOException("Server returned wrong xml.");
            }
        }
    }
}
