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
package org.geotools.data.wps;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.data.wps.request.AbstractDescribeProcessRequest;
import org.geotools.data.wps.request.AbstractExecuteProcessRequest;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.data.wps.response.WPSGetCapabilitiesResponse;
import org.geotools.ows.ServiceException;

/**
 * Provides support for the Web Processing Service 1.0.0 Specification.
 * <p>
 * WPS1_0_0 provides both name and version information that may be checked against 
 * a GetCapabilities document during version negotiation.
 * </p>
 * 
 * @author gdavis
 *
 * @source $URL$
 */
public class WPS1_0_0 extends WPSSpecification {

    /**
     * Public constructor creates the WMS1_0_0 object.
     */
    public WPS1_0_0() {

    }

    /**
     * Expected version attribute for root element.
     * 
     * @return the expect version value for this specification
     */
    public String getVersion() {
        return "1.0.0"; //$NON-NLS-1$
    }
    
    /**
     * Create a request for performing GetCapabilities requests on a 1.0.0 server.
     * 
     * @see org.geotools.data.wps.WPSSpecification#createGetCapabilitiesRequest(java.net.URL)
     * @param server a URL that points to the 1.0.0 server
     * @return a AbstractGetCapabilitiesRequest object that can provide a valid request
     */
    public GetCapabilitiesRequest createGetCapabilitiesRequest( URL server ) {
        return new GetCapsRequest(server);
    }
    
    private static String processKey(String key) {
        return key.trim().toLowerCase();
    }

    /**
     * We need a custom request object.
     */
    static public class GetCapsRequest extends AbstractGetCapabilitiesRequest {
        /**
         * Construct a Request compatible with a 1.0.0 Web Process Server.
         * 
         * @param urlGetCapabilities URL of GetCapabilities document.
         */
        public GetCapsRequest( URL urlGetCapabilities ) {
            super(urlGetCapabilities);
        }

        protected void initVersion() {
        	properties.setProperty(VERSION, "1.0.0");
        }

        protected void initRequest() {
            setProperty("REQUEST", "getCapabilities");
        }

        protected void initService() {
            //The 1.0.0 specification does not use the service property
        }

        protected String processKey( String key ) {
            return WPS1_0_0.processKey(key);
        }
        
        public Response createResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
			return new WPSGetCapabilitiesResponse(contentType, inputStream);
		}
    }

	@Override
	public DescribeProcessRequest createDescribeProcessRequest(
			URL onlineResource) throws UnsupportedOperationException {
		return new InternalDescribeProcessRequest(onlineResource, null);
	}
	
	public static class InternalDescribeProcessRequest extends AbstractDescribeProcessRequest {

        /**
         * @param onlineResource
         * @param properties
         */
        public InternalDescribeProcessRequest( URL onlineResource, Properties properties ) {
            super(onlineResource, properties);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.0.0");
        }
        
        public Response createResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
			return new DescribeProcessResponse(contentType, inputStream);
		}
	}

	@Override
	public ExecuteProcessRequest createExecuteProcessRequest(URL onlineResource)
			throws UnsupportedOperationException {
		return new InternalExecuteProcessRequest(onlineResource, null);
	}	
	
	public static class InternalExecuteProcessRequest extends AbstractExecuteProcessRequest {
		
        /**
         * @param onlineResource
         * @param properties
         */
        public InternalExecuteProcessRequest( URL onlineResource, Properties properties ) {
            super(onlineResource, properties);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.0.0");
        }
        
        public Response createResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
			return new ExecuteProcessResponse(contentType, inputStream);
		}
         
	}	
}
