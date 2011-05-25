/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_1_0.parsers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.util.logging.Logger;

import net.opengis.wfs.GetFeatureType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParser;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParserFactory;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.util.logging.Logging;

/**
 * A WFS response parser factory for GetFeature requests in {@code text/xml; subtype=gml/3.1.1}
 * output format.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 *
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/v1_1_0/parsers/Gml31GetFeatureResponseParserFactory.java $
 */
@SuppressWarnings("nls")
public class Gml31GetFeatureResponseParserFactory implements WFSResponseParserFactory {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private static final String SUPPORTED_OUTPUT_FORMAT1 = "text/xml; subtype=gml/3.1.1";

    private static final String SUPPORTED_OUTPUT_FORMAT2 = "GML3";

    /**
     * @see WFSResponseParserFactory#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Checks if this factory can create a parser for the potential responses of the given WFS
     * request.
     * <p>
     * For instance, this factory can create a parser as long as the request is a
     * {@link GetFeatureType GetFeature} request and the request output format matches {@code
     * "text/xml; subtype=gml/3.1.1"}.
     * </p>
     * 
     * @see WFSResponseParserFactory#canProcess(WFSOperationType, String)
     */
    public boolean canProcess(EObject request) {
        if (!(request instanceof GetFeatureType)) {
            return false;
        }
        String outputFormat = ((GetFeatureType) request).getOutputFormat();
        boolean matches = isSupportedOutputFormat(outputFormat);
        return matches;
    }

    protected boolean isSupportedOutputFormat(String outputFormat) {
        boolean matches = SUPPORTED_OUTPUT_FORMAT1.equals(outputFormat)
                || SUPPORTED_OUTPUT_FORMAT2.equals(outputFormat);
        return matches;
    }

    /**
     * Returns either a {@link FeatureCollectionParser} or an {@link ExceptionReportParser}
     * depending on what the server returned.
     * <p>
     * Ideally, the decision should only be taken based on the WFS response's content-type HTTP
     * header. Truth is, some WFS implementations does not set proper HTTP response headers so a bit
     * of an heuristic may be needed in order to identify the actual response.
     * </p>
     * 
     * @see WFSResponseParserFactory#createParser(WFS_1_1_0_DataStore, WFSResponse)
     * @see FeatureCollectionParser
     * @see ExceptionReportParser
     */
    public WFSResponseParser createParser(WFS_1_1_0_DataStore wfs, WFSResponse response)
            throws IOException {
        final WFSResponseParser parser;
        final String contentType = response.getContentType();
        if (isSupportedOutputFormat(contentType)) {
            parser = new FeatureCollectionParser();
        } else {
            // We can't rely on the server returning the correct output format. Some, for example
            // CubeWerx, upon a successful GetFeature request, set the response's content-type
            // header to plain "text/xml" instead of "text/xml;subtype=gml/3.1.1". So we'll do a bit
            // of heuristics to find out what it actually returned
            final int buffSize = 256;
            PushbackInputStream pushbackIn = new PushbackInputStream(response.getInputStream(),
                    buffSize);
            byte[] buff = new byte[buffSize];
            int readCount = 0;
            int r;
            while ((r = pushbackIn.read(buff, readCount, buffSize - readCount)) != -1) {
                readCount += r;
                if (readCount == buffSize) {
                    break;
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(buff), response.getCharacterEncoding()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            String head = sb.toString();
            LOGGER.fine("response head: " + head);

            pushbackIn.unread(buff, 0, readCount);
            response.setInputStream(pushbackIn);

            if (head.contains("FeatureCollection")) {
                parser = new FeatureCollectionParser();
            } else if (head.contains("ExceptionReport")) {
                parser = new ExceptionReportParser();
            } else {
                throw new IllegalStateException("Unkown server response: " + head);
            }
        }
        return parser;
    }
}
