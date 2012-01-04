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
import net.opengis.wps10.ExecuteResponseType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.xml.sax.SAXException;


/**
 * Represents the response from a server after a DescribeProcess request
 * has been issued.
 *
 * @author gdavis
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wps/src/main/java/org/geotools/data/wps/response/DescribeProcessResponse.java $
 */
public class GetExecutionStatusResponse extends Response
{

    private ExecuteResponseType exeResponse;
    private ExceptionReportType excepResponse;

    /**
     * @param contentType
     * @param inputStream
     * @throws ServiceException
     * @throws SAXException
     */
    public GetExecutionStatusResponse(HTTPResponse httpResponse) throws IOException, ServiceException
    {
        super(httpResponse);

        InputStream inputStream = null;
        try
        {
            inputStream = httpResponse.getResponseStream();

            // Map hints = new HashMap();
            // hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
            Configuration config = new WPSConfiguration();
            Parser parser = new Parser(config);

            Object object;
            exeResponse = null;
            excepResponse = null;
            try
            {
                // object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
                object = parser.parse(inputStream);
            }
            catch (SAXException e)
            {
                throw (IOException) new IOException().initCause(e);
            }
            catch (ParserConfigurationException e)
            {
                throw (IOException) new IOException().initCause(e);
            }

            // try casting the response
            if (object instanceof ExecuteResponseType)
            {
                exeResponse = (ExecuteResponseType) object;
            }
            // exception caught on server and returned
            else if (object instanceof ExceptionReportType)
            {
                excepResponse = (ExceptionReportType) object;
            }

        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

    public ExecuteResponseType getExecuteResponse()
    {
        return exeResponse;
    }

    public ExceptionReportType getExceptionResponse()
    {
        return excepResponse;
    }

}
