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
package org.geotools.data.wms;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.data.wms.request.AbstractGetStylesRequest;
import org.geotools.data.wms.request.AbstractPutStylesRequest;
import org.geotools.data.wms.request.GetStylesRequest;
import org.geotools.data.wms.request.PutStylesRequest;
import org.geotools.data.wms.response.GetStylesResponse;
import org.geotools.data.wms.response.PutStylesResponse;
import org.geotools.ows.ServiceException;

/**
 * Provides support for the Web Map Server 1.1.1 Specificaiton.
 *
 * <p>WMS1_1_1 provides both name and version information that may be checked against a
 * GetCapabilities document during version negotiation.
 *
 * @author Jody Garnett, Refractions Research
 * @author rgould
 * @source $URL$
 */
public class WMS1_1_1 extends WMS1_1_0 {
    public WMS1_1_1() {}

    /**
     * Expected version attribute for root element.
     *
     * @return DOCUMENT ME!
     */
    public String getVersion() {
        return "1.1.1";
    }

    /**
     * Factory method to create WMS 1.1.1 GetCapabilities Request
     *
     * @param server DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    public static class GetCapsRequest extends WMS1_1_0.GetCapsRequest {
        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    public static class GetMapRequest extends WMS1_1_0.GetMapRequest {

        public GetMapRequest(URL onlineResource) {
            super(onlineResource);
        }

        protected void initVersion() {
            setVersion("1.1.1");
        }
    }

    public static class GetFeatureInfoRequest extends WMS1_1_0.GetFeatureInfoRequest {

        public GetFeatureInfoRequest(
                URL onlineResource, org.geotools.data.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    public org.geotools.data.wms.request.GetMapRequest createGetMapRequest(URL get) {
        return new GetMapRequest(get);
    }

    public org.geotools.data.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest(
            URL onlineResource, org.geotools.data.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }

    public GetStylesRequest createGetStylesRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalGetStylesRequest(onlineResource, null);
    }

    /** @see org.geotools.data.wms.WMS1_0_0#createPutStylesRequest(java.net.URL) */
    public PutStylesRequest createPutStylesRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalPutStylesRequest(onlineResource);
    }

    public static class InternalGetStylesRequest extends AbstractGetStylesRequest {

        /**
         * @param onlineResource
         * @param properties
         */
        public InternalGetStylesRequest(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
        }
        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetStylesRequest#initVersion()
         */
        protected void initVersion() {
            setProperty(VERSION, "1.1.1");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new GetStylesResponse(httpResponse);
        }
    }

    public static class InternalPutStylesRequest extends AbstractPutStylesRequest {

        public InternalPutStylesRequest(URL onlineResource) {
            super(onlineResource, null);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.1.1");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new PutStylesResponse(httpResponse);
        }
    }
}
