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

import org.geotools.data.ows.Capabilities;
import org.geotools.data.ows.GetCapabilitiesResponse;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.ows.ServiceException;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * Provides a hook up to parse the capabilties document from inputstream.
 * 
 * @author Richard Gould
 *
 *
 * @source $URL$
 */
public class WMSGetCapabilitiesResponse extends GetCapabilitiesResponse {

	public WMSGetCapabilitiesResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
		super(contentType, inputStream);
		
		try {
	        Map hints = new HashMap();
	        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
	        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
	
	        Object object;
			try {
				object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
			} catch (SAXException e) {
				throw (ServiceException) new ServiceException("Error while parsing XML.").initCause(e);
			}
	        
	        if (object instanceof ServiceException) {
	        	throw (ServiceException) object;
	        }
	        
	        this.capabilities = (Capabilities)object;
		} finally {
			inputStream.close();
		}
	}

}
