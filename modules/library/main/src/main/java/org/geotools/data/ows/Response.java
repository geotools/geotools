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
import org.jdom.JDOMException;


/**
 * Provides a base class for Responses from an OWS. Checks the incoming content
 * for a ServiceException and parses it if it encounters one.
 *
 * @author rgould
 *
 *
 * @source $URL$
 */
public abstract class Response {
    protected HTTPResponse httpResponse;

    public Response(HTTPResponse httpResponse) throws ServiceException, IOException {
    	if( httpResponse.getResponseStream() == null ){
    		throw new NullPointerException("An inputStream is required for "+getClass().getName());
        }
    	if( httpResponse.getContentType() == null ){
    		// should missing content type be fatal? Or could we make an assumption?
        	throw new NullPointerException("Content type is required for "+getClass().getName());
        }    	
        this.httpResponse = httpResponse;
        /*
         * Intercept XML ServiceExceptions and throw them
         */
        if (httpResponse.getContentType().toLowerCase().equals("application/vnd.ogc.se_xml")) {
            try {
                throw parseException(httpResponse.getResponseStream());
            } finally {
                dispose();
            }
        }
    }

    public void dispose(){
        httpResponse.dispose();
    }
    
    public String getContentType() {
        return httpResponse.getContentType();
    }

    /**
     * Returns the InputStream that contains the response from the server. 
     * The contents of this stream vary according to the type of request
     * that was made, and whether it was successful or not.
     * 
     * <B>NOTE:</B>
     * Note that clients using this code are responsible for closing the
     * InputStream when they are finished with it.
     * 
     * @return the input stream containing the response from the server
     */
    public InputStream getInputStream() {
        try {
            return httpResponse.getResponseStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected ServiceException parseException(InputStream inputStream) throws IOException {
    	try {
			return ServiceExceptionParser.parse(inputStream);
		} catch (JDOMException e) {
			throw (IOException) new IOException().initCause(e);
		} finally {
			inputStream.close();
		}
    }
}
