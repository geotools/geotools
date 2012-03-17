/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import java.net.URL;

import org.opengis.referencing.Factory;


/**
 * Provides a hook to locate grid shift files, such as NTv1, NTv2 and NADCON ones
 * 
 * Andrea Aime - Geosolutions
 */
public interface GridShiftLocator extends Factory {
	
	/**
	 * Locate the specified resource.
	 * 
	 * @param grid the grid name/location
	 * @return the fully resolved URL of the grid or null, if the resource cannot be located.
	 */
	URL locateGrid(String grid);
}
