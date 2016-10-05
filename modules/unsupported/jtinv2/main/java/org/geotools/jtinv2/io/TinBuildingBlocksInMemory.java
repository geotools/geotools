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

package org.geotools.jtinv2.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public class TinBuildingBlocksInMemory implements TinBuildingBlocks
{

	private ArrayList<Polygon> holes;
	private ArrayList<LineString> breaklines;
	private ArrayList<Coordinate> vertices;
	private Polygon outerBoundary;
	private boolean hasOuterBoundary;
	private boolean hasHoles;
	private boolean hasBreaklines;
	
	public TinBuildingBlocksInMemory()
	{
		this.holes = new ArrayList<Polygon>();
		this.breaklines = new ArrayList<LineString>();
		this.vertices = new ArrayList<Coordinate>();
		
		this.hasBreaklines = false;
		this.hasHoles = false;
		this.hasOuterBoundary = false;
	}
	
	@Override
	public Iterator<Polygon> getHoles() 
	{
		return this.holes.iterator();
	}

	@Override
	public Polygon getOuterBoundary() 
	{
		return this.outerBoundary;
	}

	@Override
	public Iterator<LineString> getBreaklines() 
	{
		return this.breaklines.iterator();
	}

	@Override
	public Iterator<Coordinate> getVertices()
	{
		return this.vertices.iterator();
	}
	
	public void addVertex(Coordinate argCoordinate)
	{
		this.vertices.add(argCoordinate);
	}
	
	public void addBreakline(LineString argBreakline)
	{
		this.breaklines.add(argBreakline);
		this.hasBreaklines = true;
	}
	
	public void setOuterBoundary(Polygon argOuterBoundary)
	{
		this.outerBoundary = argOuterBoundary;
		this.hasOuterBoundary = true;
	}
	
	public void addHole(Polygon argHole)
	{
		this.holes.add(argHole);
		this.hasHoles = true;
	}

}
