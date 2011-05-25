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
 *
 *    Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

import com.vividsolutions.jts.geom.CoordinateSequence;

/**
 * A Builder for JTS CoordinateSequences.
 * 
 * @author wolf
 *
 *
 * @source $URL$
 */
public interface CSBuilder {
	/**
	 * Starts the building of a new coordinate sequence 
	 * @param size - the number of coordinates in the coordinate sequence
	 * @param dimensions - the dimension of the coordinates in the coordinate sequence,
	 * may be ignored if the coordinate sequence does not support variabile dimensions
	 */
	public void start(int size, int dimensions);
	
	/**
	 * Stops the coordinate sequence building and returns the result
	 */
	public CoordinateSequence end();

	
	/**
	 * Sets and ordinate in the specified coordinate
	 * @param value
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public void setOrdinate(double value, int ordinateIndex, int coordinateIndex);
	
	/**
	 * Utility method that allows to set an ordinate in an already built coordinate sequence
	 * Needed because the CoordinateSequence interface does not expose it
	 * @param sequence
	 * @param value
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public void setOrdinate(CoordinateSequence sequence, double value, int ordinateIndex, int coordinateIndex);
	
	/**
	 * Gets an ordinate in the specified coordinate
	 * 
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public double getOrdinate(int ordinateIndex, int coordinateIndex);
	
	/**
	 * Returns the size of the coordinate sequence we are building, -1 if there is none
	 */
	public int getSize();
	
	/**
	 * Returns the dimension of the coordinate sequence we are building, -1 if there is none
	 */
	public int getDimension();
}


