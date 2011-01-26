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

import java.io.IOException;
import java.io.InputStream;

import org.geotools.ows.ServiceException;

/**
 * Base class for GetCapabilities responses. They should typically attempt to
 * parse the Capabilities document in inputStream in the constructor. 
 * 
 * @author Richard Gould
 *
 * @source $URL$
 */
public abstract class GetCapabilitiesResponse extends Response {

	protected Capabilities capabilities;

	public GetCapabilitiesResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
		super(contentType, inputStream);
	}
 
	/**
	 * Returns the capabilities object parsed during the response
	 */
	public Capabilities getCapabilities() {
		return capabilities;
	}
}
