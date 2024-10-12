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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.api.filter.Filter;
import org.geotools.data.wfs.WFSTestData.MutableWFSConfig;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.ows.ServiceException;
import org.geotools.util.logging.Logging;

/**
 * Test client for WFS
 *
 * <p>Gives the possibility to override config settings, and mock up http responses.
 */
public class TestWFSClient extends WFSClient {

    private Logger LOGGER = Logging.getLogger(TestWFSClient.class);

    private GetFeatureRequest request;

    public TestWFSClient(URL capabilitiesUrl, TestHttpClient http) throws IOException, ServiceException {
        super(capabilitiesUrl, http, WFSTestData.getGmlCompatibleConfig());
    }

    /** Allows to set a response when calling DescribeFeatureType with a certain typeName */
    public void mockDescribeFeatureTypeRequest(URL responseUrl, QName typeName) {
        DescribeFeatureTypeRequest request = this.createDescribeFeatureTypeRequest();
        request.setTypeName(typeName);

        try {
            mockRequest(responseUrl, request);
            LOGGER.fine("Added mock response for DescribeFeatureType");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Couldn't create mock response for DescribeFeatureType", ex);
        }
    }

    /** Allows to set a response for a given typeName and filter */
    public void mockGetFeatureRequest(URL responseUrl, QName typeName, Filter filter) {
        GetFeatureRequest request = this.createGetFeatureRequest();
        request.setTypeName(typeName);
        request.setFilter(filter);

        try {
            mockRequest(responseUrl, request);
            LOGGER.fine("Added mock response for GetFeature");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Couldn't create mock response for GetFeature", ex);
        }
    }

    private void mockRequest(URL responseUrl, WFSRequest request) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        request.performPostOutput(outputStream);
        ((TestHttpClient) this.getHTTPClient())
                .expectPost(
                        request.getFinalURL(),
                        outputStream.toString(),
                        request.getPostContentType(),
                        new TestHttpResponse(responseUrl, "text/xml"));
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
    public GetFeatureResponse issueRequest(GetFeatureRequest request) throws IOException {
        this.request = request;
        return super.issueRequest(request);
    }

    /** @return the request */
    public GetFeatureRequest getRequest() {
        return request;
    }
}
