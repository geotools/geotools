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

package org.geotools.jtinv2.main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class TinFaceUtilities 
{
	private TinFace subject;
	
	public boolean isCoordinateOnTinFace(Coordinate argCoordinate)
	{
		Polygon triangle = this.subject.getAsPolygon();
		GeometryFactory factory = new GeometryFactory();
		
		Point coordinateAsGeometry = factory.createPoint(argCoordinate);
		
		return triangle.contains(coordinateAsGeometry);
	}
	
	public void setSubject(TinFace argSubject)
	{
		this.subject = argSubject;
	}
}
