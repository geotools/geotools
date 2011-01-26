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
package org.geotools.data.ows;

/**
 * Represents a base object for a WMS getCapabilities response.
 *
 * @author gdavis
 *
 * @source $URL$
 */
public class WPSCapabilities extends Capabilities {
    private WPSRequest request;
    
	private String[] exceptions;


    /**
     * The request contains information about possible Requests that can be 
     * made against this server, including URLs and formats.
     *
     * @return Returns the request.
     */
    public WPSRequest getRequest() {
        return request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(WPSRequest request) {
        this.request = request;
    }
    

	/**
	 * Exceptions declare what kind of formats this server can return exceptions
	 * in. They are used during subsequent requests.
	 */
	public String[] getExceptions() {
	    return exceptions;
	}

	public void setExceptions(String[] exceptions) {
	    this.exceptions = exceptions;
	}
}
