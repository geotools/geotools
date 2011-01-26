/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso;

/**
 * The UnsupportedDimensionException will be thrown when methods are called,
 * which are not capable to treat the dimension of the input data, or do not
 * always work correctly in that dimension.
 * 
 * @author Sanjay Dominik Jena
 * 
 *
 * @source $URL$
 */
public class UnsupportedDimensionException extends Exception {

	/**
	 * Creates a <code>UnsupportedDimensionException</code> with the given
	 * detail message.
	 * 
	 * @param message
	 *            a description of this
	 *            <code>UnsupportedDimensionException</code>
	 */
	public UnsupportedDimensionException(String message) {
		super(message);
	}

	/**
	 * Creates a <code>UnsupportedDimensionException</code> with
	 * <code>e</code>s detail message.
	 * 
	 * @param e
	 *            an exception that occurred while trying to operate a function
	 *            which is not operable for that coordiante dimension
	 */
	public UnsupportedDimensionException(Exception e) {
		this(e.toString());
	}

}
