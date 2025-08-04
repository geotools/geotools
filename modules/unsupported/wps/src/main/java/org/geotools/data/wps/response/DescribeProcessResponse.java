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
package org.geotools.data.wps.response;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.ProcessDescriptionsType;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.xml.sax.SAXException;

/**
 * Represents the response from a server after a DescribeProcess request has been issued.
 *
 * @author gdavis
 */
public class DescribeProcessResponse extends Response {

    private ProcessDescriptionsType processDescs;
    private ExceptionReportType excepResponse;

    /** */
    public DescribeProcessResponse(HTTPResponse httpResponse) throws IOException, ServiceException {
        super(httpResponse);

        try (InputStream inputStream = httpResponse.getResponseStream()) {

            // Map hints = new HashMap();
            // hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
            Configuration config = new WPSConfiguration();
            Parser parser = new Parser(config);

            Object object;
            excepResponse = null;
            processDescs = null;
            try {
                // object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
                object = parser.parse(inputStream);
            } catch (SAXException | ParserConfigurationException e) {
                throw (IOException) new IOException().initCause(e);
            }

            // try casting the response
            if (object instanceof ProcessDescriptionsType type1) {
                processDescs = type1;
            }
            // exception caught on server and returned
            else if (object instanceof ExceptionReportType type) {
                excepResponse = type;
            }
        }
    }

    public ProcessDescriptionsType getProcessDesc() {
        return processDescs;
    }

    public ExceptionReportType getExceptionResponse() {
        return excepResponse;
    }
}
