/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.io.wkt;

/**
 * Thrown by a <code>WKTReader</code> when a parsing problem occurs.
 *
 * @source $URL$
 */
public class ParseException extends Exception {

	/**
	 * Creates a <code>ParseException</code> with the given detail message.
	 * 
	 * @param message
	 *            a description of this <code>ParseException</code>
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Creates a <code>ParseException</code> with <code>e</code>s detail
	 * message.
	 * 
	 * @param e
	 *            an exception that occurred while a <code>WKTReader</code>
	 *            was parsing a Well-known Text string
	 */
	public ParseException(Exception e) {
		this(e.toString());
	}
}
