/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;

public class DescribeFeatureTypeResponseFactory implements WFSResponseFactory {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(WFSRequest originatingRequest, String contentType) {
        return originatingRequest instanceof DescribeFeatureTypeRequest
                && (contentType == null
                        || contentType.startsWith("text/xml")
                        || contentType.startsWith("application/gml+xml")
                        || contentType.startsWith("application/xml"));
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.DESCRIBE_FEATURETYPE.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return Arrays.asList(
                "text/xml",
                "text/xml; subtype=gml/3.1.1",
                "text/xml; subtype=gml/3.2",
                "XMLSCHEMA",
                "text/gml; subtype=gml/3.1.1",
                "text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0",
                "application/gml+xml",
                "application/gml+xml; version=3.2",
                "application/gml+xml; version=3.2;charset=UTF-8");
    }

    @Override
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response)
            throws IOException {
        try {
            return new DescribeFeatureTypeResponse((DescribeFeatureTypeRequest) request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }
}
