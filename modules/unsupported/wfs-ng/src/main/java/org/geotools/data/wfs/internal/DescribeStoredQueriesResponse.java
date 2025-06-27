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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.opengis.wfs20.DescribeStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import org.apache.commons.io.IOUtils;
import org.geotools.api.data.DataSourceException;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.DOMParser;
import org.w3c.dom.Document;

public class DescribeStoredQueriesResponse extends WFSResponse {

    private DescribeStoredQueriesResponseType describeStoredQueriesResponse;

    public DescribeStoredQueriesResponse(WFSRequest originatingRequest, HTTPResponse response)
            throws IOException, ServiceException {
        super(originatingRequest, response);

        MODULE.finer("Parsing DescribeStoredQueries response");
        try {
            final Document rawDocument;
            final byte[] rawResponse;
            {
                ByteArrayOutputStream buff = new ByteArrayOutputStream();
                try (InputStream inputStream = response.getResponseStream()) {
                    IOUtils.copy(inputStream, buff);
                }
                rawResponse = buff.toByteArray();
            }
            if (RESPONSES.isLoggable(Level.FINE)) {
                RESPONSES.fine("Full ListStoredQueries response: " + new String(rawResponse, StandardCharsets.UTF_8));
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

            describeStoredQueriesResponse = parseStoredQueries(rawDocument, WFS_2_0_CONFIGURATION);

            if (null == describeStoredQueriesResponse) {
                throw new IllegalStateException("Unable to parse DescribeStoredQueriesResponse document");
            }

        } finally {
            response.dispose();
        }
    }

    private DescribeStoredQueriesResponseType parseStoredQueries(Document document, Configuration wfsConfig)
            throws DataSourceException {
        DOMParser parser = new DOMParser(wfsConfig, document);
        final Object parsed;
        try {
            parsed = parser.parse();
        } catch (Exception e) {
            throw new DataSourceException("Exception parsing DescribeStoredQueriesResponse", e);
        }

        if (!(parsed instanceof DescribeStoredQueriesResponseType)) {
            throw new DataSourceException(
                    "DescribedStoredQueries response is not a DescribeStoredQueriesResponseType but a "
                            + parsed.getClass());
        }

        return (DescribeStoredQueriesResponseType) parsed;
    }

    public List<StoredQueryDescriptionType> getStoredQueryDescriptions() {
        return describeStoredQueriesResponse.getStoredQueryDescription();
    }
}
