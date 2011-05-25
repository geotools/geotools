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

import java.net.URL;

import org.geotools.data.ows.GetCapabilitiesRequest;

/**
 * Provides support for the Web Map Server 1.1.1 Specificaiton.
 * <p>
 * WMS1_1_1 provides both name and version information that may be checked against a GetCapabilities document during
 * version negotiation.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 * @author rgould
 *
 * @source $URL$
 */
public class WMS1_1_1 extends WMS1_1_0 {
    public WMS1_1_1() {
    }

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
    public GetCapabilitiesRequest createGetCapabilitiesRequest( URL server ) {
        return new GetCapsRequest(server);
    }

    static public class GetCapsRequest extends WMS1_1_0.GetCapsRequest {
        public GetCapsRequest( URL urlGetCapabilities ) {
            super(urlGetCapabilities);
        }

        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    static public class GetMapRequest extends WMS1_1_0.GetMapRequest {

        public GetMapRequest( URL onlineResource ) {
            super(onlineResource);
        }

        protected void initVersion() {
            setVersion("1.1.1");
        }
    }

    static public class GetFeatureInfoRequest extends WMS1_1_0.GetFeatureInfoRequest {

        public GetFeatureInfoRequest( URL onlineResource, org.geotools.data.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }
    }

    public org.geotools.data.wms.request.GetMapRequest createGetMapRequest( URL get ) {
        return new GetMapRequest(get);
    }

    public org.geotools.data.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest( URL onlineResource, org.geotools.data.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }
}
