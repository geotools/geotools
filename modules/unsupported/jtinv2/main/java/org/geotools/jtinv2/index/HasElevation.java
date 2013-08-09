/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jtinv2.index;

/**
 * A class that implements this interface can provide information
 * about its location above or below a datum. Objects that implement
 * this interface could have a single elevation, or an elevation range.
 * 
 * Objects that implement this interface can be indexed using the 
 * ElevationIndex class defined in this same Java package.
 * 
 * @author Landon Blake
 * @see ElevationIndex
 */
public interface HasElevation 
{
	/**
	 * The object will return a double value from this method
	 * if its position above or below a datum can be reprsented by
	 * a single elevation. If this object is implemented by an 
	 * elevation range instead, this method should throw and
	 * IllegalStateException.
	 * 
	 * You should call make sure the hasPrimaryElevation() method of this
	 * interface returns true before calling this method to avoid
	 * the exception.
	 */
	public abstract double getPrimaryElevation();
	
	/**
	 * The object will return a double value from this method
	 * if its position above or below a datum can be represented by
	 * an elevation range. The value returned is the bottom of this
	 * elevation range. If this object is implemented by a single
	 * elevation instead, this method should throw and
	 * IllegalStateException.
	 * 
	 * You should call make sure the hasLowestElevation() method of this
	 * interface returns true before calling this method to avoid
	 * the exception.
	 */
	public abstract double getLowestElevation();
	
	/**
	 * The object will return a double value from this method
	 * if its position above or below a datum can be represented by
	 * an elevation range. The value returned is the top of this
	 * elevation range. If this object is implemented by a single
	 * elevation instead, this method should throw and
	 * IllegalStateException.
	 * 
	 * You should call make sure the hasHighestElevation() method of this
	 * interface returns true before calling this method to avoid
	 * the exception.
	 */
	public abstract double getHighestElevation();
	
	/**
	 * Returns a string that uniquely identifies this elevation or
	 * elevation range. This allows objects of this type to be used
	 * in a HashMap, among other things.
	 */
	public abstract String getElevationIdentifier();
	
	/**
	 * Indicates if this object has a primary elevation. If this method
	 * returns true, the getPrimaryElevation() method should return a valid
	 * double value.
	 */
	public abstract boolean hasPrimaryElevation();
	
	/**
	 * Indicates if this object can be represented by an elevation range and
	 * has a lowest or lower elevation. If this method
	 * returns true, the getLowestElevation() method should return a valid
	 * double value.
	 */
	public abstract boolean hasLowestElevation();
	
	/**
	 * Indicates if this object can be represented by an elevation range and
	 * has a highest or higher elevation. If this method
	 * returns true, the getHighestElevation() method should return a valid
	 * double value.
	 */
	public abstract boolean hasHighestElevation();
}
