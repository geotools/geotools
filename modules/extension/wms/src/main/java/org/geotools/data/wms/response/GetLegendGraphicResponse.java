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
import org.xml.sax.SAXException;

/**
 * 
 * @author Richard Gould
 *
 * @source $URL$
 */
public class GetLegendGraphicResponse extends Response {

    /**
     * @param contentType
     * @param inputStream
     * @throws SAXException 
     * @throws ServiceException 
     */
    public GetLegendGraphicResponse( String contentType, InputStream inputStream ) throws ServiceException, IOException {
        super(contentType, inputStream);
        
        if (contentType.toLowerCase().indexOf("text/xml") != -1) {
        	throw parseException(inputStream);
        }
    }

}
