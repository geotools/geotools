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
package org.geotools.data.wms.response;

import java.io.IOException;
import java.io.InputStream;

import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;


/**
 * Process GetMapResponse.
 * 
 * <p>
 * Assume this is a placeholder allowing other code access to the InputStream?
 * It would be nice if this class actually provided a real object either
 * returned Image, SVG XML Document or a resolved GridCoverage in a manner
 * similar to GetCapabilities.
 * </p>
 *
 * @author Richard Gould, Refractions Research
 * @source $URL$
 */
public class GetMapResponse extends Response {
    public GetMapResponse(String contentType, InputStream response) throws ServiceException, IOException {
        super(contentType, response);
        
        if (contentType.toLowerCase().indexOf("application/vnd.ogc.se_xml") != -1) {
        	throw parseException(response);
        }
    }
}
