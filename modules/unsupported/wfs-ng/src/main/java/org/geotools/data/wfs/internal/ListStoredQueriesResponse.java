/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_2_0_CONFIGURATION;
import static org.geotools.data.wfs.internal.Loggers.MODULE;
import static org.geotools.data.wfs.internal.Loggers.RESPONSES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.opengis.wfs20.ListStoredQueriesResponseType;
import org.apache.commons.io.IOUtils;
import org.geotools.data.DataSourceException;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.DOMParser;
import org.w3c.dom.Document;

public class ListStoredQueriesResponse extends WFSResponse {

    private ListStoredQueriesResponseType listStoredQueriesResponse;

    public ListStoredQueriesResponse(WFSRequest originatingRequest, HTTPResponse response)
            throws IOException, ServiceException {
        super(originatingRequest, response);
        MODULE.finer("Parsing ListStoredQueries response");
        try {
            final Document rawDocument;
            final byte[] rawResponse;
            {
                ByteArrayOutputStream buff = new ByteArrayOutputStream();
                InputStream inputStream = response.getResponseStream();
                try {
                    IOUtils.copy(inputStream, buff);
                } finally {
                    inputStream.close();
                }
                rawResponse = buff.toByteArray();
            }
            if (RESPONSES.isLoggable(Level.FINE)) {
                RESPONSES.fine("Full ListStoredQueries response: " + new String(rawResponse));
            }
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                builderFactory.setNamespaceAware(true);
                builderFactory.setValidating(false);
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                rawDocument = documentBuilder.parse(new ByteArrayInputStream(rawResponse));
            } catch (Exception e) {
                throw new IOException("Error parsing capabilities document: " + e.getMessage(), e);
            }

            listStoredQueriesResponse = parseStoredQueries(rawDocument, WFS_2_0_CONFIGURATION);

            if (null == listStoredQueriesResponse) {
                throw new IllegalStateException("Unable to parse ListStoredQueries document");
            }

        } finally {
            response.dispose();
        }
    }

    private ListStoredQueriesResponseType parseStoredQueries(
            Document document, Configuration wfsConfig) throws DataSourceException {
        DOMParser parser = new DOMParser(wfsConfig, document);
        final Object parsed;
        try {
            parsed = parser.parse();
        } catch (Exception e) {
            throw new DataSourceException("Exception parsing ListStoredQueriesResponse", e);
        }

        return (ListStoredQueriesResponseType) parsed;
    }

    public ListStoredQueriesResponseType getListStoredQueriesResponse() {
        return listStoredQueriesResponse;
    }
}
