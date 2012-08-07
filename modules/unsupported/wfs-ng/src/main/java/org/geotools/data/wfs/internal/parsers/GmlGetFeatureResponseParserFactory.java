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
package org.geotools.data.wfs.internal.parsers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 * A WFS response parser factory for GetFeature requests in GML output formats.
 * <p>
 * At this time supports GML 2 and GML 3.1
 */
@SuppressWarnings("nls")
public class GmlGetFeatureResponseParserFactory implements WFSResponseFactory {

    private static final Logger LOGGER = Loggers.MODULE;

    private static final List<String> SUPPORTED_FORMATS = Collections.unmodifiableList(Arrays
            .asList(//
            "text/xml; subtype=gml/3.1.1",//
                    "text/xml;subtype=gml/3.1.1",//
                    "text/xml; subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "text/xml;subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "application/gml+xml; subtype=gml/3.1.1",//
                    "application/gml+xml;subtype=gml/3.1.1",//
                    "application/gml+xml; subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "application/gml+xml;subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "GML3", //
                    "GML3L0", //
                    "text/xml", // oddly, GeoServer returns plain 'text/xml' instead of the propper
                                // subtype when resultType=hits. Guess we should make this something
                                // the specific strategy can hanlde?
                    "text/gml; subtype=gml/3.1.1",// the incorrectly advertised GeoServer format
                    "GML2",//
                    "text/xml; subtype=gml/2.1.2"//
            ));

    /**
     * @see WFSResponseFactory#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Checks if this factory can create a parser for the potential responses of the given WFS
     * request.
     * <p>
     * For instance, this factory can create a parser as long as the request is a
     * {@link GetFeatureType GetFeature} request and the request output format matches
     * {@code "text/xml; subtype=gml/3.1.1"}.
     * </p>
     * 
     * @see WFSResponseFactory#canProcess(WFSOperationType, String)
     */
    public boolean canProcess(final WFSRequest request, final String contentType) {
        if (!WFSOperationType.GET_FEATURE.equals(request.getOperation())) {
            return false;
        }
        // String outputFormat = ((GetFeatureRequest) request).getOutputFormat();
        boolean matches = SUPPORTED_FORMATS.contains(contentType);
        if (!matches) {
            // fuzy search, "
            for (String supported : SUPPORTED_FORMATS) {
                if (supported.startsWith(contentType) || contentType.startsWith(supported)) {
                    matches = true;
                    break;
                }
            }
        }
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
     * @see WFSResponseFactory#createParser(WFSResponse)
     * @see FeatureCollectionParser
     * @see ExceptionReportParser
     */
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response) throws IOException {

        final GetFeatureRequest getFeature = (GetFeatureRequest) request;

        final GetFeatureParser parser;
        final String contentType = response.getContentType();
        if (SUPPORTED_FORMATS.contains(contentType)) {
            parser = parser(getFeature, response.getResponseStream());
        } else {
            // We can't rely on the server returning the correct output format. Some, for example
            // CubeWerx, upon a successful GetFeature request, set the response's content-type
            // header to plain "text/xml" instead of "text/xml;subtype=gml/3.1.1". So we'll do a bit
            // of heuristics to find out what it actually returned
            final int buffSize;
            if (LOGGER.isLoggable(Level.FINER)) {
                buffSize = 4096;
            } else {
                buffSize = 512;
            }
            PushbackInputStream pushbackIn = new PushbackInputStream(response.getResponseStream(),
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

            String charset = response.getResponseHeader("Charset");
            try {
                Charset.forName(charset);
            } catch (Exception e) {
                charset = "UTF-8";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(buff), charset));
            StringBuilder head = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                head.append(line).append('\n');
            }
            if(LOGGER.isLoggable(Level.FINER)){
                System.err.println("Response head:");
                System.err.println(head);
            }
            
            pushbackIn.unread(buff, 0, readCount);

            if (head.indexOf("FeatureCollection") > 0) {
                parser = parser(getFeature, pushbackIn);
            } else if (head.indexOf("ExceptionReport") > 0) {
                // parser = new ExceptionReportParser();
                // TODO: return ExceptionResponse or so
                throw new UnsupportedOperationException("implement!");
            } else {
                throw new IllegalStateException("Unkown server response: " + head);
            }
        }

        try {
            return new GetFeatureResponse(request, response, parser);
        } catch (ServiceException e) {
            throw new IOException(e);
        }

    }

    private GetFeatureParser parser(GetFeatureRequest request, InputStream in) throws IOException {

        final QName remoteFeatureName = request.getTypeName();

        FeatureType queryType = request.getQueryType();
        if (queryType == null) {
            queryType = request.getFullType();
        }
        if (!(queryType instanceof SimpleFeatureType)) {
            throw new UnsupportedOperationException();
        }

        SimpleFeatureType schema = (SimpleFeatureType) queryType;

        GetFeatureParser featureReader = new XmlSimpleFeatureParser(in, schema, remoteFeatureName);
        return featureReader;
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.GET_FEATURE.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return SUPPORTED_FORMATS;
    }
}
