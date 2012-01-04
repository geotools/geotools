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
package org.geotools.data.wps;

import java.net.URL;
import java.util.List;

import net.opengis.wps10.DataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;

import org.geotools.data.ows.Specification;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.request.GetExecutionStatusRequest;


/**
 *
 *
 * @source $URL$
 */
public abstract class WPSSpecification extends Specification
{

    /**
     * Creates a DescribeProcess request which can be used to retrieve
     * information about a specific process on the WPS Server.
     *
     * @param onlineResource the location where the request can be made
     * @return a DescribeProcessRequest to be configured and then passed to the WPS Server
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract DescribeProcessRequest createDescribeProcessRequest(URL onlineResource)
        throws UnsupportedOperationException;

    /**
     * Creates a Execute request which can be used to execute
     * a specific process on the WPS Server.
     *
     * @param onlineResource the location where the request can be made
     * @return an ExecuteProcessRequest to be configured and then passed to the WPS Server
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract ExecuteProcessRequest createExecuteProcessRequest(URL onlineResource)
        throws UnsupportedOperationException;

    public abstract GetExecutionStatusRequest createGetExecutionStatusRequest(URL onlineResource)
        throws UnsupportedOperationException;

    public abstract DataType createLiteralInputValue(String literalValue);

    public abstract DataType createBoundingBoxInputValue(String crs, int dimensions, List<Double> lowerCorner,
        List<Double> upperCorner);

    public abstract ResponseFormType createResponseForm(ResponseDocumentType responseDoc,
        OutputDefinitionType rawOutput);

    public abstract ResponseDocumentType createResponseDocumentType(boolean lineage, boolean status,
        boolean storeExecuteResponse, String outputType);

    public abstract OutputDefinitionType createOutputDefinitionType(String identifier);

}
