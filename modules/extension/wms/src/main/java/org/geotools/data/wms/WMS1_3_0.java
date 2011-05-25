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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.geotools.data.ows.GetCapabilitiesRequest;

/**
 * @author rgould
 *
 *
 * @source $URL$
 */
public class WMS1_3_0 extends WMS1_1_1 {
	
	public WMS1_3_0() {

	}
	/* (non-Javadoc)
	 * @see org.geotools.data.wms.Specification#getVersion()
	 */
	public String getVersion() {
		return "1.3.0";
	}

	/* (non-Javadoc)
	 * @see org.geotools.data.wms.Specification#createGetCapabilitiesRequest(java.net.URL)
	 */
	public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
		return new GetCapsRequest(server);
	}
	
    public org.geotools.data.wms.request.GetMapRequest createGetMapRequest( URL get) {
        return new GetMapRequest(get);
    }
    
	public static class GetCapsRequest extends WMS1_1_1.GetCapsRequest {

		public GetCapsRequest(URL urlGetCapabilities) {
			super(urlGetCapabilities);
		}
		
		/* (non-Javadoc)
		 * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initVersion()
		 */
		protected void initVersion() {
			setProperty("VERSION", "1.3.0");
		}
	}
	
	public static class GetMapRequest extends WMS1_1_1.GetMapRequest {

        public GetMapRequest( URL onlineResource) {
            super(onlineResource);
        }
	    
        protected void initVersion() {
            setVersion("1.3.0");
        }
        
        
		public void setFormat(String value) {
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			super.setFormat(value);
		}
	}
	
	public static class GetFeatureInfoRequest extends WMS1_1_1.GetFeatureInfoRequest {
	    
        public GetFeatureInfoRequest( URL onlineResource, org.geotools.data.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }
        
        protected void initVersion() {
            setProperty("VERSION", "1.3.0");
        }
        
        protected String getQueryX() {
            return "I";
        }
        
        protected String getQueryY() {
            return "J";
        }
	}
	
    public org.geotools.data.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest( URL onlineResource, org.geotools.data.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }
}
