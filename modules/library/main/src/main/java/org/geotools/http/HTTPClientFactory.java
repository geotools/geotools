/*    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import org.geotools.data.ows.HTTPClient;
import org.geotools.util.factory.Hints;

/**
 * Factory class to create a HTTP client.
 * Default implementation is within library gt-http.
 * 
 * @author Roar Brænden
 */
public interface HTTPClientFactory {

	/**
	 * Main method to get the client
	 * @return default http client
	 */
    HTTPClient getClient();
    
    /**
     * Get a client by the given hints.
     * 
     * @param hints The hints for the http client
     * @return
     */
    HTTPClient getClient(Hints hints);
    
    /**
     * Turn logging of all requests on or off.
     * Supersedes any setting given by hints.
     * 
     * @param logging
     */
    void logging(boolean logging);
    
    /**
     * Turn logging on by the given charset.
     * 
     * @param charset
     */
    void logging(String charset);

}
