/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.DescribeStoredQueriesRequest;
import org.geotools.data.wfs.internal.DescribeStoredQueriesResponse;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;

public class DescribeStoredQueriesResponseFactory implements WFSResponseFactory {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(WFSRequest originatingRequest, String contentType) {
        return originatingRequest instanceof DescribeStoredQueriesRequest
                && (contentType == null || contentType.startsWith("text/xml"));
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.DESCRIBE_STORED_QUERIES.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return Arrays.asList("text/xml");
    }

    @Override
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response)
            throws IOException {
        try {
            return new DescribeStoredQueriesResponse(
                    (DescribeStoredQueriesRequest) request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }
}
