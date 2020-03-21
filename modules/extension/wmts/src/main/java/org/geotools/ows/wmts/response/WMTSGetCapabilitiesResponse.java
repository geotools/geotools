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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.geotools.data.ows.GetCapabilitiesResponse;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.util.logging.Logging;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides a hook up to parse the capabilties document from inputstream.
 *
 * <p>(Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSGetCapabilitiesResponse extends GetCapabilitiesResponse {
    private static final Logger LOGGER = Logging.getLogger(WMTSGetCapabilitiesResponse.class);
    private static WMTSConfiguration WMTS_CONFIGURATION = new WMTSConfiguration();

    public WMTSGetCapabilitiesResponse(HTTPResponse response) throws ServiceException, IOException {
        this(response, null);
    }

    public WMTSGetCapabilitiesResponse(HTTPResponse response, Map<String, Object> hints)
            throws ServiceException, IOException {
        super(response);

        try {

            Object object;
            try (InputStream inputStream = response.getResponseStream()) {
                Parser parser = new Parser(WMTS_CONFIGURATION);
                if (LOGGER.isLoggable(Level.FINEST)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    try (BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line + "\n");
                        }
                    }
                    String string = stringBuilder.toString();
                    LOGGER.finest(string);

                    object =
                            parser.parse(
                                    new InputSource(new ByteArrayInputStream(string.getBytes())));
                } else {
                    object = parser.parse(inputStream);
                }
            } catch (SAXException | ParserConfigurationException e) {
                throw (ServiceException)
                        new ServiceException("Error while parsing XML.").initCause(e);
            }

            if (object instanceof ServiceException) {
                throw (ServiceException) object;
            }

            this.capabilities = new WMTSCapabilities((CapabilitiesType) object);
        } finally {
            response.dispose();
        }
    }
}
