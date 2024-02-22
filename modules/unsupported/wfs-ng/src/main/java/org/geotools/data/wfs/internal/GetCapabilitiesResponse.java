/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_1_0_CAPABILITIES_CONFIGURATION;
import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_1_1_CONFIGURATION;
import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_2_0_CONFIGURATION;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.opengis.wfs.WFSCapabilitiesType;
import org.eclipse.emf.ecore.EObject;
import org.geotools.api.data.DataSourceException;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.util.Version;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

public class GetCapabilitiesResponse extends org.geotools.data.ows.GetCapabilitiesResponse {

    private WFSGetCapabilities capabilities;

    private static Logger LOGGER = Logging.getLogger(GetCapabilitiesResponse.class);

    public GetCapabilitiesResponse(HTTPResponse response, EntityResolver entityResolver)
            throws IOException, ServiceException {
        super(response);
        LOGGER.finer("Parsing GetCapabilities response");
        try {
            final Document rawDocument;
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                builderFactory.setNamespaceAware(true);
                builderFactory.setValidating(false);
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                if (entityResolver != null) {
                    documentBuilder.setEntityResolver(entityResolver);
                }
                rawDocument = documentBuilder.parse(response.getResponseStream());
            } catch (Exception e) {
                throw new IOException("Error parsing capabilities document: " + e.getMessage(), e);
            }

            List<Configuration> tryConfigs =
                    Arrays.asList(
                            WFS_2_0_CONFIGURATION,
                            WFS_1_1_CONFIGURATION,
                            WFS_1_0_CAPABILITIES_CONFIGURATION);

            final String versionAtt = rawDocument.getDocumentElement().getAttribute("version");
            Version version = null;
            if (null != versionAtt) {
                version = new Version(versionAtt);
                if (Versions.v1_0_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_1_0_CAPABILITIES_CONFIGURATION);
                } else if (Versions.v1_1_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_1_1_CONFIGURATION);
                } else if (Versions.v2_0_0.equals(version)) {
                    tryConfigs = Collections.singletonList(WFS_2_0_CONFIGURATION);
                }
            }
            EObject parsedCapabilities = null;

            for (Configuration wfsConfig : tryConfigs) {
                try {
                    parsedCapabilities = parseCapabilities(rawDocument, wfsConfig);
                    if (parsedCapabilities != null) {
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.log(
                            Level.INFO,
                            "Couldn't use configuration:" + wfsConfig.getClass().getName(),
                            e);
                }
            }

            if (null == parsedCapabilities) {
                throw new IllegalStateException("Unable to parse GetCapabilities document");
            }

            this.capabilities = WFSGetCapabilities.create(parsedCapabilities, rawDocument);
        } finally {
            response.dispose();
        }
    }

    private EObject parseCapabilities(final Document document, final Configuration wfsConfig)
            throws IOException {

        DOMParser parser = new DOMParser(wfsConfig, document);
        final Object parsed;
        try {
            parsed = parser.parse();
        } catch (Exception e) {
            throw new DataSourceException("Exception parsing WFS capabilities", e);
        }
        if (parsed == null) {
            throw new DataSourceException("WFS capabilities was not parsed");
        }
        if (!(parsed instanceof WFSCapabilitiesType)
                && !(parsed instanceof net.opengis.wfs20.WFSCapabilitiesType)) {
            throw new DataSourceException("Expected WFS Capabilities, got " + parsed);
        }
        EObject object = (EObject) parsed;
        return object;
    }

    @Override
    public WFSGetCapabilities getCapabilities() {
        return capabilities;
    }
}
