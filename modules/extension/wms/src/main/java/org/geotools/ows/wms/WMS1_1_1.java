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
package org.geotools.ows.wms;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.request.AbstractGetStylesRequest;
import org.geotools.ows.wms.request.AbstractPutStylesRequest;
import org.geotools.ows.wms.request.GetStylesRequest;
import org.geotools.ows.wms.request.PutStylesRequest;
import org.geotools.ows.wms.response.GetStylesResponse;
import org.geotools.ows.wms.response.PutStylesResponse;

/**
 * Provides support for the Web Map Server 1.1.1 Specificaiton.
 *
 * <p>WMS1_1_1 provides both name and version information that may be checked against a
 * GetCapabilities document during version negotiation.
 *
 * @author Jody Garnett, Refractions Research
 * @author rgould
 */
public class WMS1_1_1 extends WMS1_1_0 {
    public WMS1_1_1() {}

    /** Expected version attribute for root element. */
    @Override
    public String getVersion() {
        return "1.1.1";
    }

    /** Factory method to create WMS 1.1.1 GetCapabilities Request */
    @Override
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    public static class GetCapsRequest extends WMS1_1_0.GetCapsRequest {
        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        @Override
        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    public static class GetMapRequest extends WMS1_1_0.GetMapRequest {

        public GetMapRequest(URL onlineResource) {
            super(onlineResource);
        }

        @Override
        protected void initVersion() {
            setVersion("1.1.1");
        }
    }

    public static class GetFeatureInfoRequest extends WMS1_1_0.GetFeatureInfoRequest {

        public GetFeatureInfoRequest(
                URL onlineResource, org.geotools.ows.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        @Override
        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    @Override
    public org.geotools.ows.wms.request.GetMapRequest createGetMapRequest(URL get) {
        return new GetMapRequest(get);
    }

    @Override
    public org.geotools.ows.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest(
            URL onlineResource, org.geotools.ows.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }

    @Override
    public GetStylesRequest createGetStylesRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalGetStylesRequest(onlineResource, null);
    }

    /** @see WMS1_0_0#createPutStylesRequest(java.net.URL) */
    @Override
    public PutStylesRequest createPutStylesRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalPutStylesRequest(onlineResource);
    }

    public static class InternalGetStylesRequest extends AbstractGetStylesRequest {

        /** */
        public InternalGetStylesRequest(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
        }
        /* (non-Javadoc)
         * @see AbstractGetStylesRequest#initVersion()
         */
        @Override
        protected void initVersion() {
            setProperty(VERSION, "1.1.1");
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new GetStylesResponse(httpResponse);
        }
    }

    public static class InternalPutStylesRequest extends AbstractPutStylesRequest {

        public InternalPutStylesRequest(URL onlineResource) {
            super(onlineResource, null);
        }

        @Override
        protected void initVersion() {
            setProperty(VERSION, "1.1.1");
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new PutStylesResponse(httpResponse);
        }
    }
}
