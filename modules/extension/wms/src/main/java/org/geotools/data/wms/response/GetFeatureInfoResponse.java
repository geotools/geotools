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
 * Process GetFeatureInfoResponse.
 * 
 * <p>
 * FeatureInfoResponse is not well specified by any of the WMS specifications.
 * What this class decides to do with the response will largely depend on the
 * contentType.
 * 
 * <ul>
 * <li>
 * text/xml: could be GML
 * </li>
 * <li>
 * text/html: coudl be a description
 * </li>
 * <li>
 * Really this could be anything we will have to add to this class as different
 * responses are actually found in the wild.
 * </li>
 * </ul>
 * </p>
 *
 * @author Richard Gould
 *
 * @source $URL$
 */
public class GetFeatureInfoResponse extends Response {
    /**
     * DOCUMENT ME!
     *
     * @param contentType
     * @param inputStream
     * @throws SAXException 
     * @throws ServiceException 
     * @throws IOException 
     */
    public GetFeatureInfoResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
        super(contentType, inputStream);
    }
}
