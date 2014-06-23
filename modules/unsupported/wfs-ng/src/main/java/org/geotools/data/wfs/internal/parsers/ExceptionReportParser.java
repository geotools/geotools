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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.ows10.ExceptionReportType;
import net.opengis.ows10.ExceptionType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.internal.WFSException;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseParser;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.geotools.xml.Parser;

/**
 * A WFS response parser that parses server exception reports into {@link WFSException} objects.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 * 
 * 
 * 
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/v1_1_0/parsers/ExceptionReportParser.java $
 */
@SuppressWarnings({ "nls", "unchecked" })
public class ExceptionReportParser implements WFSResponseParser {

    /**
     * @param response
     *            a response handle to a service exception report
     * @return a {@link WFSException} containing the server returned exception report messages
     * @see WFSResponseParser#parse(WFSStrategy, WFSResponse)
     */
    public Object parse(WFSResponse response, String axisOrder) {
        throw new UnsupportedOperationException("implement!");
//        WFSConfiguration configuration = new WFSConfiguration();
//        Parser parser = new Parser(configuration);
//        InputStream responseStream = response.getInputStream();
//        Charset responseCharset = response.getCharacterEncoding();
//        Reader reader = new InputStreamReader(responseStream, responseCharset);
//        Object parsed;
//        try {
//            parsed = parser.parse(reader);
//            if (!(parsed instanceof net.opengis.ows10.ExceptionReportType)) {
//                return new IOException("Unrecognized server error");
//            }
//        } catch (Exception e) {
//            return new WFSException("Exception parsing server exception report", e);
//        }
//        net.opengis.ows10.ExceptionReportType report = (ExceptionReportType) parsed;
//        List<ExceptionType> exceptions = report.getException();
//
//        EObject originatingRequest = response.getOriginatingRequest();
//        StringBuilder msg = new StringBuilder("WFS returned an exception.");
//        msg.append(" Target URL: " + response.getTargetUrl());
//        if (originatingRequest != null) {
//            try {
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                StrictWFS_1_1_Strategy.encode(originatingRequest, configuration, out,
//                        Charset.forName("UTF-8"));
//                String requestStr = out.toString("UTF-8");
//
//                msg.append(". Originating request is: \n").append(requestStr).append("\n");
//            } catch (Exception e) {
//                LOGGER.log(Level.FINE, "Error encoding request for exception report", e);
//            }
//        }
//        WFSException result = new WFSException(msg.toString());
//        for (ExceptionType ex : exceptions) {
//            result.addExceptionReport(String.valueOf(ex.getExceptionText()));
//        }
//        return result;
    }
}
