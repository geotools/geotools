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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.ows.LayerDescription;
import org.geotools.data.ows.Response;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.ows.ServiceException;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * Represents the response from a server after a DescribeLayer request
 * has been issued.
 * 
 * @author Richard Gould
 * @source $URL$
 */
public class DescribeLayerResponse extends Response {

    private LayerDescription[] layerDescs;

    /**
     * @param contentType
     * @param inputStream
     * @throws ServiceException 
     * @throws SAXException
     */
    public DescribeLayerResponse( String contentType, InputStream inputStream ) throws IOException, ServiceException {
        super(contentType, inputStream);
        
        try {
	        Map hints = new HashMap();
	        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
	
	        Object object;
			try {
				object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
			} catch (SAXException e) {
				throw (IOException) new IOException().initCause(e);
			}
	        
	        layerDescs = (LayerDescription[]) object;
        } finally {
        	inputStream.close();
        }
    }

    public LayerDescription[] getLayerDescs() {
        return layerDescs;
    }

}
