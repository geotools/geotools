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

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.ExecuteResponseType;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.xml.sax.SAXException;

/**
 * Represents the response from a server after an ExecuteProcess request has been issued.
 *
 * @author gdavis
 */
public class ExecuteProcessResponse extends Response {

    private ExecuteResponseType exeResponse;
    private ExceptionReportType excepResponse;
    private InputStream rawResponseStream;
    private String rawContentType;

    /** */
    @SuppressWarnings("PMD.UseTryWithResources") // multiple init points for InputStream
    public ExecuteProcessResponse(HTTPResponse httpResponse, boolean raw)
            throws IOException, ServiceException {
        super(httpResponse);

        InputStream inputStream = null;
        try {
            if (!raw) {
                inputStream = httpResponse.getResponseStream();
                parseDocumentResponse(inputStream);
            } else {
                // we need to know if the response was an exception, unfortunately we cannot
                // make that determination just by looking at the mime type ...

                // could be gml or other stuff, not necessarily a service exception, we need to
                // check if it's an exception or not
                rawContentType = httpResponse.getContentType();
                if (rawContentType.matches(".*/xml.*")) {
                    // make sure we don't throw away info
                    inputStream = new BufferedInputStream(httpResponse.getResponseStream());
                    inputStream.mark(8192);

                    try {
                        XMLInputFactory factory = XMLInputFactory.newFactory();
                        // disable DTDs
                        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
                        // disable external entities
                        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
                        XMLStreamReader parser =
                                factory.createXMLStreamReader(inputStream, "UTF-8");
                        // position at root element
                        while (parser.hasNext()) {
                            if (START_ELEMENT == parser.next()) {
                                break;
                            }
                        }

                        // get the first tag name
                        String name = parser.getName().getLocalPart();
                        inputStream.reset();
                        if ("ServiceException".equals(name)
                                || "ExceptionReport".equals(name)
                                || "ExecuteResponse".equals(name)) {
                            parseDocumentResponse(inputStream);
                            return;
                        }
                    } catch (XMLStreamException e) {
                        throw new IOException("Failed to parse the response", e);
                    }
                } else {
                    inputStream = httpResponse.getResponseStream();
                }

                // ok, it's really the raw response, store it and avoid closing it
                rawResponseStream = inputStream;
                inputStream = null;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void parseDocumentResponse(InputStream inputStream) throws IOException {
        // Map hints = new HashMap();
        // hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
        Configuration config = new WPSConfiguration();
        Parser parser = new Parser(config);

        Object object;
        excepResponse = null;
        exeResponse = null;
        try {
            // object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
            object = parser.parse(inputStream);
        } catch (SAXException | ParserConfigurationException e) {
            throw (IOException) new IOException().initCause(e);
        }

        // try casting the response
        if (object instanceof ExecuteResponseType) {
            exeResponse = (ExecuteResponseType) object;
            // in case of exceptions let's be explicit about them
            if (exeResponse.getStatus() != null
                    && exeResponse.getStatus().getProcessFailed() != null) {
                excepResponse = exeResponse.getStatus().getProcessFailed().getExceptionReport();
            }
        }
        // exception caught on server and returned
        else if (object instanceof ExceptionReportType) {
            excepResponse = (ExceptionReportType) object;
        }
    }

    public ExecuteResponseType getExecuteResponse() {
        return exeResponse;
    }

    public ExceptionReportType getExceptionResponse() {
        return excepResponse;
    }

    /**
     * If a raw response was requested, and no service exception has been sent, we should get the
     * raw response stream here
     */
    public InputStream getRawResponseStream() {
        return rawResponseStream;
    }

    /** The raw response stream content type */
    public String getRawContentType() {
        return rawContentType;
    }
}
