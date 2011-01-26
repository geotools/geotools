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

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.coordinate.CurveSegmentImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.coordinate.TriangleImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;



/**
 * This implementation of the CollectionFactory generates collections holded in the system memory.
 * Hence, the lists are not backed by any type of persistence.
 * 
 * @author Sanjay Jena and Prof. Dr. Jackson Roehrig
 *
 *
 * @source $URL$
 */
public class CollectionFactoryMemoryImpl implements CollectionFactory {

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.io.CollectionFactory#getCurveList()
	 */
	public List<CurveImpl> getCurveList() {
		return new ArrayList<CurveImpl>();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.io.CollectionFactory#getCurveSegmentList()
	 */
	public List<CurveSegmentImpl> getCurveSegmentList() {
		return new ArrayList<CurveSegmentImpl>();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.io.CollectionFactory#getPositionList()
	 */
	public List<PositionImpl> getPositionList() {
		return new ArrayList<PositionImpl>();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.io.CollectionFactory#createTriangleList()
	 */
	public List<TriangleImpl> createTriangleList() {
		return new ArrayList<TriangleImpl>();
	}



}
