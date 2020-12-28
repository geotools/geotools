/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.WFSTestData.MutableWFSConfig;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.ows.ServiceException;

public class TestWFSClient extends WFSClient {

    private URL describeFeatureTypeUrlOverride;

    private GetFeatureRequest request;

    public TestWFSClient(URL capabilitiesURL, HTTPClient http)
            throws IOException, ServiceException {
        super(capabilitiesURL, http, WFSTestData.getGmlCompatibleConfig());
    }

    /**
     * Allows to set an overriding url for the {@link #getDescribeFeatureTypeURLGet(String)}
     * operation, for test purposes so it is not actually needed to download the schema from the
     * internet but from a resource file
     */
    public void setDescribeFeatureTypeURLOverride(URL url) {
        this.describeFeatureTypeUrlOverride = url;
    }

    public void setAxisOrderOverride(String axisOrder, String axisOrderFilter) {
        ((MutableWFSConfig) config).setAxisOrder(axisOrder);
        ((MutableWFSConfig) config).setAxisOrderFilter(axisOrderFilter);
    }

    public void setOutputformatOverride(String outputformatOverride) {
        ((MutableWFSConfig) config).setOutputformatOverride(outputformatOverride);
    }

    public void setUseDefaultSrs(boolean useDefaultSrs) {
        ((MutableWFSConfig) config).setUseDefaultSrs(useDefaultSrs);
    }

    public void setProtocol(Boolean protocol) {
        ((MutableWFSConfig) config).setProtocol(protocol);
    }

    @Override
    public DescribeFeatureTypeResponse issueRequest(DescribeFeatureTypeRequest request)
            throws IOException {
        if (describeFeatureTypeUrlOverride == null) {
            return super.issueRequest(request);
        }
        HTTPResponse response =
                new TestHttpResponse(
                        request.getOutputFormat(), "UTF-8", describeFeatureTypeUrlOverride);
        try {
            return new DescribeFeatureTypeResponse(request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

    @Override
    public GetFeatureResponse issueRequest(GetFeatureRequest request) throws IOException {
        this.request = request;
        return super.issueRequest(request);
    }

    /** @return the request */
    public GetFeatureRequest getRequest() {
        return request;
    }
}
