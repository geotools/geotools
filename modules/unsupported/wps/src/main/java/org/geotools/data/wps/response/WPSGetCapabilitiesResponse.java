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
package org.geotools.data.wps.response;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.WPSCapabilitiesType;

import org.geotools.data.ows.AbstractWPSGetCapabilitiesResponse;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.xml.sax.SAXException;

/**
 * Provides a hook up to parse the capabilities document from inputstream.
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class WPSGetCapabilitiesResponse extends AbstractWPSGetCapabilitiesResponse {

	public WPSGetCapabilitiesResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
		super(contentType, inputStream);
		
		try {
	        //Map hints = new HashMap();
	        //hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
	        //hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
        	Configuration config = new WPSConfiguration();
        	Parser parser = new Parser(config);
	
	        Object object;
	        excepResponse = null;
	        capabilities = null;		        
			try {
				//object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
				object = parser.parse(inputStream);
			} catch (SAXException e) {
				throw (ServiceException) new ServiceException("Error while parsing XML.").initCause(e);
			} catch (ParserConfigurationException e) {
				throw (ServiceException) new ServiceException("Error while parsing XML.").initCause(e);
			}
	        
	        //if (object instanceof ServiceException) {
	        //	throw (ServiceException) object;
	        //}
	        
			// try casting the response
			if (object instanceof WPSCapabilitiesType) {
				capabilities = (WPSCapabilitiesType) object;
			}
			// exception caught on server and returned
			else if (object instanceof ExceptionReportType) {
				excepResponse = (ExceptionReportType) object;
			}				

		} finally {
			inputStream.close();
		}
	}

}
