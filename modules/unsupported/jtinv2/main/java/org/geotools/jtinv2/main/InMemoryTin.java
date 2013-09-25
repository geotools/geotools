/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
 *    @author Julian Padilla
 */

package org.geotools.jtinv2.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * An in memory representation of a TIN. These classes are typically built by
 * an InMemoryTinFactory.
 *
 */
public class InMemoryTin implements TriangulatedIrregularNetwork 
{
	// Private member variables.
	private ArrayList<TinPoint> tinPoints;
	private ArrayList<TinBreakline> tinBreaklines;
	private ArrayList<TinBoundary> tinBoundaries;
	private ArrayList<TinFace> tinFaces;
	private ArrayList<TinBoundary> innerBoundaries;
	private TinBoundary outerBoundary;
	private Envelope envelope;

	// Constructors.
	public InMemoryTin(ArrayList<TinPoint> argTinPoints, ArrayList<TinBreakline> argTinBreaklines,
			ArrayList<TinBoundary> argTinBoundaries, ArrayList<TinFace> argTinFaces, ArrayList<TinBoundary> argInnerBoundaries,
			Envelope argEnvelope, TinBoundary argOuterBoundary)
	{
		this.tinPoints = argTinPoints;
		this.tinBreaklines = argTinBreaklines;
		this.tinBoundaries = argTinBoundaries;
		this.tinFaces = argTinFaces;
		this.innerBoundaries = argInnerBoundaries;
		this.outerBoundary = argOuterBoundary;
		this.envelope = argEnvelope;
	}
	
	
	
	@Override
	public int getNumberOfTinPoints()
	{
		return this.tinPoints.size();
	}

	@Override
	public int getNumberOfTriangles()
	{
		return this.tinFaces.size();
	}

	@Override
	public int getNumberOfBreaklines() 
	{
		return this.tinBreaklines.size();
	}

	@Override
	public int getNumberOfBoundaries() 
	{
		return this.tinBoundaries.size();
	}

	@Override
	public Iterator<TinPoint> getTinPointsIterator()
	{
		return this.tinPoints.iterator();
	}

	@Override
	public Iterator<TinBreakline> getTinBreaklinesIterator()
	{
		return this.tinBreaklines.iterator();
	}

	@Override
	public Iterator<TinBoundary> getTinBoundariesIterator() 
	{
		return this.tinBoundaries.iterator();
	}

	@Override
	public TinBoundary getOuterBoundary() 
	{
		return this.outerBoundary;
	}

	@Override
	public Iterator<TinBoundary> getHoleIterator() 
	{
		return this.innerBoundaries.iterator();
	}

	@Override
	public Envelope getEnvelope() 
	{
		return this.envelope;
	}
	
	public TinPoint getTinPointAtLocation(Coordinate argCoordinate, double argRadius)
	{
		// TODO We need to speed this up by using a point spatial index.
		Iterator<TinPoint> goOverEach = this.tinPoints.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			Coordinate currentCoordinate = currentPoint.getCoordinate();
			
			double distance = currentCoordinate.distance(argCoordinate);
			
			if(distance < argRadius)
			{
				return currentPoint;
			}
		}
		
		// No TinPoint found.
		IllegalStateException error = new IllegalStateException("No TinPoint found at that location.");
		throw error;
	}
	
	public boolean hasTinPointAtLocation(Coordinate argCoordinate, double argRadius)
	{
		// TODO We need to speed this up by using a point spatial index.
		Iterator<TinPoint> goOverEach = this.tinPoints.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			Coordinate currentCoordinate = currentPoint.getCoordinate();
			
			double distance = currentCoordinate.distance(argCoordinate);
			
			if(distance < argRadius)
			{
				return true;
			}
		}
		
		return false;
	}
}
