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

package org.geotools.jtinv2.pointutils;

import org.geotools.jtinv2.main.TinPoint;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Creates a BasicCoordinateSpatialIndexFactory. To use this factory, add points
 * with the "addCoordinate" method. When you are done, call the "build" method.
 */
public class BasicTinPointSpatialIndexFactory 
{
	private BasicTinPointSpatialIndex index;
	
	public BasicTinPointSpatialIndexFactory(double argDimension)
	{
		this.index = new BasicTinPointSpatialIndex(argDimension);
	}
	
	/**
	 * Indexes the coordinate passed as an argument.
	 */
	public void indexTinPoint(TinPoint argTinPoint)
	{
		this.index.indexTinPoint(argTinPoint);
	}
	
	/**
	 * Returns the BasicCoordinateSpatialIndex just built by this factory.
	 */
	public BasicTinPointSpatialIndex build()
	{
		return this.index;
	}
}
