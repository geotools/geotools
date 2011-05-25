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
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.geotools.ows.ServiceException;

/**
 * This represents a Request to be made against a Open Web Service.
 * 
 * @author rgould
 *
 * @source $URL$
 */
public interface Request {
    
    /** Represents the REQUEST parameter */
    public static final String REQUEST = "REQUEST"; //$NON-NLS-1$
    /** Represents the VERSION parameter */
    public static final String VERSION = "VERSION"; //$NON-NLS-1$
    /** Represents the WMTVER parameter */
    public static final String WMTVER = "WMTVER"; //$NON-NLS-1$
    public static final String SERVICE = "SERVICE";
    
    /**
     * Once the properties of the request are configured, this will return
     * the URL that points to the server and contains all of the appropriate
     * name/value parameters. 
     * 
     * @return a URL that can be used to issue the request
     */
    public URL getFinalURL();
    
    /**
     * Sets the name/value property for this request.
     * 
     * Note that when using this method, it is up to the programmer to
     * provide their own encoding of <code>value</code> according to the
     * OWS specifications! The code will not do this for you. 
     * 
     * Different OWS specifications define different ways to do this. There are 
     * notorious differences between WMS 1.1.1 (section 6.2.1) and 
     * WMS 1.3.0 (section 6.3.2) for example.
     * 
     * If value is null, "name" is removed from the properties table.
     * 
     * @param name the name of the property
     * @param value the value of the property
     */
    public void setProperty(String name, String value);
    
    /**
     * @return a copy of this request's properties
     */
    public Properties getProperties();
    
    /**
     * Each Request must know how to create it's counterpart Response. 
     * Given the content type and input stream (containin the response data), 
     * this method must return an appropriate Response object.
     * 
     * @param contentType the MIME type of the data in the inputStream
     * @param inputStream contains the data from the response
     * @throws ServiceException
     * @throws IOException
     */
    Response createResponse(String contentType, InputStream inputStream) throws ServiceException, IOException;
    
    /**
     * This method indicates whether this request needs to transmit some data
     * to the server using POST. If this returns true, performPostOutput() will be called
     * during the connection, allowing the data to be written out to the server.
     * 
     * @return true if this request needs POST support, false otherwise.
     */
    boolean requiresPost();
    
    /**
     * If this request uses POST, it must specify the content type of the data
     * that is to be written out during performPostOutput().
     * 
     * For open web services, this is usually "application/xml".
     * 
     * @return the MIME type of the data to be sent during the request
     */
    String getPostContentType();
    
    /**
     * This is called during the connection to the server, allowing this
     * request to write out data to the server by using the provided
     * outputStream.
     * 
     * Implementors of this method do not need to call outputStream.flush() or 
     * outputStream.close(). The framework will call them immediately after
     * calling this method.
     * 
     * @param outputStream
     */
    void performPostOutput(OutputStream outputStream) throws IOException;
}
