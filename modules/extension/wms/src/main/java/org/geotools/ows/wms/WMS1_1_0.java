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
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.request.*;
import org.geotools.ows.wms.response.DescribeLayerResponse;
import org.geotools.ows.wms.response.GetLegendGraphicResponse;

/** @author Richard Gould */
public class WMS1_1_0 extends WMS1_0_0 {

    public WMS1_1_0() {}

    public org.geotools.ows.wms.request.GetMapRequest createGetMapRequest(URL get) {
        return new GetMapRequest(get);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.Specification#getVersion()
     */
    public String getVersion() {
        return "1.1.0";
    }

    /** @see org.geotools.data.wms.Specification#createGetCapabilitiesRequest(java.net.URL) */
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    /**
     * @see WMS1_0_0#createGetFeatureInfoRequest(java.net.URL,
     *     org.geotools.ows.wms.request.GetMapRequest, java.util.Set, java.lang.String[])
     */
    public org.geotools.ows.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest(
            URL onlineResource, org.geotools.ows.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }

    /** @see WMS1_0_0#createDescribeLayerRequest(java.net.URL) */
    public DescribeLayerRequest createDescribeLayerRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalDescribeLayerRequest(onlineResource, null);
    }

    public GetLegendGraphicRequest createGetLegendGraphicRequest(URL onlineResource) {
        return new InternalGetLegendGraphicRequest(onlineResource, this);
    }

    public static class GetCapsRequest extends WMS1_0_0.GetCapsRequest {

        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
            // TODO Auto-generated constructor stub
        }

        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initRequest()
         */
        protected void initRequest() {
            setProperty("REQUEST", "GetCapabilities");
        }
        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initService()
         */
        protected void initService() {
            setProperty("SERVICE", "WMS");
        }
        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initVersion()
         */
        protected void initVersion() {
            setProperty("VERSION", "1.1.0");
        }

        protected String processKey(String key) {
            return key.trim().toUpperCase();
        }
    }

    public static class GetMapRequest extends WMS1_0_0.GetMapRequest {

        public GetMapRequest(URL onlineResource) {
            super(onlineResource);
        }

        protected void initRequest() {
            setProperty(REQUEST, "GetMap");
        }

        protected void initVersion() {
            setProperty(VERSION, "1.1.0");
        }

        protected String getRequestFormat(String format) {
            return format;
        }

        protected String getRequestException(String exception) {
            return exception;
        }

        protected String processKey(String key) {
            return key.trim().toUpperCase();
        }
    }

    public static class GetFeatureInfoRequest extends WMS1_0_0.GetFeatureInfoRequest {

        /** */
        public GetFeatureInfoRequest(
                URL onlineResource, org.geotools.ows.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        protected void initRequest() {
            setProperty("REQUEST", "GetFeatureInfo");
        }

        protected void initVersion() {
            setProperty("VERSION", "1.1.0");
        }

        protected String processKey(String key) {
            return key.trim().toUpperCase();
        }
    }

    public static class InternalDescribeLayerRequest extends AbstractDescribeLayerRequest {

        /** */
        public InternalDescribeLayerRequest(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.1.0");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new DescribeLayerResponse(httpResponse, hints);
        }
    }

    public static class InternalGetLegendGraphicRequest extends AbstractGetLegendGraphicRequest {

        public InternalGetLegendGraphicRequest(URL onlineResource, WMS1_1_0 creator) {
            super(onlineResource);
            // Apparently getLegend graphic is only supported for 1.1.0 SLD WMS.
            // Only 1.1.1 supports GetLegendGraphic as a main query.  As far as I can tell
            // So this allows The WMS 1.1.1 strategy to put its version number.
            // I think this should be done for all cases my self but I don't
            // want to make such a big change.
            setProperty(VERSION, creator.getVersion());
        }

        protected void initVersion() {
            setProperty(VERSION, "1.1.0");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new GetLegendGraphicResponse(httpResponse);
        }
    }
}
