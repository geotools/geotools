/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows;

import org.xml.sax.SAXException;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public class ServiceException extends SAXException {
	/*
	 * Constants used in communications with Web Map Servers
	 */
	/** Request contains a Format not offered by the service instance */
	public static final String INVALID_FORMAT = "InvalidFormat";
	/**
	 *  Request contains an SRS not offered by the service instance 
	 *  for one or more of the Layers in the request. 
	 */
	public static final String INVALID_SRS = "InvalidSRS";
	/**
	 * Request contains a CRS not offered by the server for one or 
	 * more of the Layers in the request.
	 */
	public static final String INVALID_CRS = "InvalidCRS";
	/** Request is for a Layer not offered by the service instance. */
	public static final String LAYER_NOT_DEFINED = "LayerNotDefined";
	/** Request is for a Layer in a Style not offered by the service instance. */
	public static final String STYLE_NOT_DEFINED = "StyleNotDefined";
	/** GetFeatureInfo request is applied to a Layer which is not declared queryable. */
	public static final String LAYER_NOT_QUERYABLE = "LayerNotQueryable";
	/**
	 * Value of (optional) UpdateSequence parameter in GetCapabilities request is
	 * equal to current value of Capabilities XML update sequence number.
	 */
	public static final String CURRENT_UPDATE_SEQUENCE ="CurrentUpdateSequence";
	/**
	 * Value of (optional) UpdateSequence parameter in GetCapabilities request is
	 * greater than current value of Capabilities XML update sequence number.
	 */
	public static final String INVALID_UPDATE_SEQUENCE ="InvalidUpdateSequence";
	/**
	 * Request does not include a sample dimension value, and the service instance
	 * did not declare a default value for that dimension.
	 */
	public static final String MISSING_DIMENSION_VALUE = "MissingDimensionValue";
	/** Request contains an invalid sample dimension value. */
	public static final String INVALID_DIMENSION_VALUE = "InvalidDimensionValue";
	/** Request is for an optional operation that is not supported by the server. */
	public static final String OPERATION_NOT_SUPPORTED = "OperationNotSupported";
	/*
	 * END WMS Constants
	 */
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = (("org.geotools.data.ows.ServiceException").hashCode());
	private String code = "";
    private String locator = null;
    private ServiceException next; //So they can be chained

    private ServiceException() {
        super("");
    	// should not be called
    }

    /**
     * @param msg Message
     * @see SAXException#SAXException(java.lang.String)
     */
    public ServiceException(String msg) {
        super(msg);
    }
    
    public ServiceException(String msg, String code) {
    	super(msg);
    	this.code = code;
    }

    /**
     * Passes the message to the parent, or the code if the message is null.
     * 
     * @param msg Message
     * @param code Error Code
     * @param locator Error Location
     * @see SAXException#SAXException(java.lang.String)
     */
    public ServiceException(String msg, String code, String locator) {
    	super((msg == null) ? code : msg);
        this.code = code;
        this.locator = locator;
    }

    /**
     * @return String the error code, such as 404-Not Found
     */
    public String getCode() {
        return code;
    }

    /**
     * @return String the location of the error, useful for parse errors
     */
    public String getLocator() {
        return locator;
    }
    
	public ServiceException getNext() {
		return next;
	}
	public void setNext(ServiceException next) {
		this.next = next;
	}
}
