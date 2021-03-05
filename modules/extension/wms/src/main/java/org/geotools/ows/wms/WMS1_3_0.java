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

import java.net.URL;
import org.geotools.data.ows.GetCapabilitiesRequest;

/** @author rgould */
public class WMS1_3_0 extends WMS1_1_1 {

    public WMS1_3_0() {}

    /* (non-Javadoc)
     * @see org.geotools.data.wms.Specification#getVersion()
     */
    @Override
    public String getVersion() {
        return "1.3.0";
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.Specification#createGetCapabilitiesRequest(java.net.URL)
     */
    @Override
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    @Override
    public org.geotools.ows.wms.request.GetMapRequest createGetMapRequest(URL get) {
        return new GetMapRequest(get);
    }

    public static class GetCapsRequest extends WMS1_1_1.GetCapsRequest {

        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initVersion()
         */
        @Override
        protected void initVersion() {
            setProperty("VERSION", "1.3.0");
        }
    }

    public static class GetMapRequest extends WMS1_1_1.GetMapRequest {

        public GetMapRequest(URL onlineResource) {
            super(onlineResource);
        }

        @Override
        protected void initVersion() {
            setVersion("1.3.0");
        }

        @Override
        public void setSRS(String srs) {
            // in wms 1.3 it's called CRS
            properties.setProperty("CRS", srs);
        }
    }

    public static class GetFeatureInfoRequest extends WMS1_1_1.GetFeatureInfoRequest {

        public GetFeatureInfoRequest(
                URL onlineResource, org.geotools.ows.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        @Override
        protected void initVersion() {
            setProperty("VERSION", "1.3.0");
        }

        @Override
        protected String getQueryX() {
            return "I";
        }

        @Override
        protected String getQueryY() {
            return "J";
        }
    }

    @Override
    public org.geotools.ows.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest(
            URL onlineResource, org.geotools.ows.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }
}
