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
package org.geotools.geometry.iso.io;

import java.util.List;

import org.geotools.geometry.iso.coordinate.CurveSegmentImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.coordinate.TriangleImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;


/**
 * A CollectionFactory creates lists of different GM_Objects or Coordinates.
 * The original objective of this encapsulating is the possibility of persistence support,
 * that is that the returned lists are backed by a type of database to relieve the system
 * memory in cases of large geometries.
 * 
 * @author Sanjay Jena and Prof. Dr. Jackson Roehrig
 *
 * @source $URL$
 */
public interface CollectionFactory {
	
	/**
	 * Creates a list of Curves
	 * 
	 * @return List
	 */
	List<CurveImpl> getCurveList();

	/**
	 * Creates a list of CurveSegments
	 * 
	 * @return List
	 */
	List<CurveSegmentImpl> getCurveSegmentList();

	
	/**
	 * Creates a list of Positions
	 * 
	 * @return List
	 */
	List<PositionImpl> getPositionList();

	/**
	 * Creates a list of Triangles
	 * 
	 * @return List
	 */
	List<TriangleImpl> createTriangleList();



}
