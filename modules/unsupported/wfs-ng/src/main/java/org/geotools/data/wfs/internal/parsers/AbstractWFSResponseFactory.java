/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.emf.common.CommonPlugin.Implementation;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSException;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.xsd.Parser;
import org.xml.sax.EntityResolver;

/**
 * Potential base class for {@link WFSResponseFactory} implementations. Provides support for
 * detecting and parsing of exception reports received as {@link HTTPResponse}.
 *
 * <p>Subclasses have to {@link Implementation} {@link #isValidResponseHead(StringBuilder)} and
 * {@link #createResponseImpl(WFSRequest, HTTPResponse, InputStream)}.
 *
 * @author awaterme
 */
public abstract class AbstractWFSResponseFactory implements WFSResponseFactory {

    private static final Logger LOGGER = Loggers.MODULE;

    /**
     * Returns either a properly parsed response object or an exception depending on what the server
     * returned.
     *
     * <p>Ideally, the decision should only be taken based on the WFS response's content-type HTTP
     * header. Truth is, some WFS implementations does not set proper HTTP response headers so a bit
     * of an heuristic may be needed in order to identify the actual response.
     *
     * @see WFSResponseFactory#createParser(WFSResponse)
     * @see FeatureCollectionParser
     * @see ExceptionReportParser
     */
    @Override
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response)
            throws IOException {

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
        @SuppressWarnings("PMD.CloseResource") // closed in delegates it would seem... but unsure
        PushbackInputStream pushbackIn =
                new PushbackInputStream(response.getResponseStream(), buffSize);
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

        StringBuilder head = new StringBuilder();
        try (BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(new ByteArrayInputStream(buff), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                head.append(line).append('\n');
            }
        }
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Response head:\n" + head);
        }

        pushbackIn.unread(buff, 0, readCount);

        if (isValidResponseHead(head.toString())) {
            return createResponseImpl(request, response, pushbackIn);
        } else if (head.indexOf("ExceptionReport") > 0) {
            throw parseException(request, pushbackIn);
        } else {
            throw new IllegalStateException("Unkown server response: " + head);
        }
    }

    /**
     * Has to be implemented to turn the request and response into a proper {@link WFSResponse}.
     *
     * @param in The stream to read the response from. It is safe not to close this stream
     *     explicitly but to dispose the response instead.
     * @return The actual response
     */
    protected abstract WFSResponse createResponseImpl(
            WFSRequest request, HTTPResponse response, InputStream in) throws IOException;

    /**
     * @param head The first couple of characters from the response, typically the first 512
     * @return true, if head is an indicator for the expected XML result structure.
     */
    protected abstract boolean isValidResponseHead(String head);

    /**
     * @return An {@link WFSException}
     * @throws IOException in case the parsing of the exception report failed
     */
    public WFSException parseException(WFSRequest originatingRequest, InputStream inputStream)
            throws IOException {
        Parser parser = new Parser(originatingRequest.getStrategy().getWfsConfiguration());
        EntityResolver resolver = originatingRequest.getStrategy().getConfig().getEntityResolver();
        if (resolver != null) {
            parser.setEntityResolver(resolver);
        }
        Object parsed;
        try {
            parsed = parser.parse(inputStream);
            if (!(parsed instanceof net.opengis.ows10.ExceptionReportType
                    || parsed instanceof net.opengis.ows11.ExceptionReportType)) {
                String info = String.valueOf(parsed);
                throw new IOException("Unrecognized server error: " + info);
            }
        } catch (Exception e) {
            return new WFSException("Exception parsing server exception report", e);
        }
        if (parsed instanceof net.opengis.ows10.ExceptionReportType) {
            net.opengis.ows10.ExceptionReportType report =
                    (net.opengis.ows10.ExceptionReportType) parsed;
            @SuppressWarnings("unchecked")
            List<net.opengis.ows10.ExceptionType> exceptions = report.getException();

            StringBuilder msg = new StringBuilder("WFS returned an exception.");
            msg.append(" Originating Request: ");
            msg.append(originatingRequest.toString());
            WFSException result = new WFSException(msg.toString());
            for (net.opengis.ows10.ExceptionType ex : exceptions) {
                @SuppressWarnings("unchecked")
                List<String> texts = ex.getExceptionText();
                result.addExceptionDetails(ex.getExceptionCode(), ex.getLocator(), texts);
            }
            return result;
        } else if (parsed instanceof net.opengis.ows11.ExceptionReportType) {
            net.opengis.ows11.ExceptionReportType report =
                    (net.opengis.ows11.ExceptionReportType) parsed;
            @SuppressWarnings("unchecked")
            List<net.opengis.ows11.ExceptionType> exceptions = report.getException();

            StringBuilder msg = new StringBuilder("WFS returned an exception.");
            msg.append(" Originating Request: ");
            msg.append(originatingRequest.toString());
            WFSException result = new WFSException(msg.toString());
            for (net.opengis.ows11.ExceptionType ex : exceptions) {
                @SuppressWarnings("unchecked")
                List<String> texts = ex.getExceptionText();
                result.addExceptionDetails(ex.getExceptionCode(), ex.getLocator(), texts);
            }
            return result;
        } else {
            net.opengis.ows20.ExceptionReportType report =
                    (net.opengis.ows20.ExceptionReportType) parsed;
            List<net.opengis.ows20.ExceptionType> exceptions = report.getException();

            StringBuilder msg = new StringBuilder("WFS returned an exception.");
            msg.append(" Originating Request: ");
            msg.append(originatingRequest.toString());
            WFSException result = new WFSException(msg.toString());
            for (net.opengis.ows20.ExceptionType ex : exceptions) {
                List<String> text = ex.getExceptionText();
                result.addExceptionDetails(ex.getExceptionCode(), ex.getLocator(), text);
            }
            return result;
        }
    }
}
