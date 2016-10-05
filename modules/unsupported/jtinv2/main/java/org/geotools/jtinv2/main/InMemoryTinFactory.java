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

import org.geotools.jtinv2.index.InMemoryTinIndex;
import org.geotools.jtinv2.index.TinIndex;
import org.geotools.jtinv2.tinbuilding.TriangleCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Creates an InMemoryTin using the data provided.
 * 
 * To use this factory, first add the TinPoints, TinBreaklines, and TinBoundaries.
 *
 */
public class InMemoryTinFactory 
{
	// Private member variables.
	private ArrayList<TinPoint> tinPoints;
	private ArrayList<TinBreakline> tinBreaklines;
	private ArrayList<TinBoundary> tinBoundaries;
	private ArrayList<TinFace> tinFaces;
	private ArrayList<TinBoundary> innerBoundaries;
	private TinBoundary outerBoundary;
	private Envelope envelope;
	
	private InMemoryTinIndex tinIndex;
	
	public InMemoryTinFactory(double argPointSpatialIndexDim)
	{
		this.tinPoints = new ArrayList<TinPoint>();
		this.tinBreaklines = new ArrayList<TinBreakline>();
		this.tinFaces = new ArrayList<TinFace>();
		this.tinBoundaries = new ArrayList<TinBoundary>();
		this.innerBoundaries = new ArrayList<TinBoundary>();
		this.tinIndex = new InMemoryTinIndex(argPointSpatialIndexDim);
		this.envelope = new Envelope();
	}
	
	/**
	 * Adds a TinPoint to the TIN being built.
	 */
	public void addTinPoint(TinPoint argTinPoint)
	{
		this.tinPoints.add(argTinPoint);
		Coordinate coord = argTinPoint.getCoordinate();
		
		// Expand the envelope.
		this.envelope.expandToInclude(coord);
		
		// Index the point.
		this.tinIndex.indexTinPoint(argTinPoint);
	}
	
	/**
	 * Adds a TinBreakline to the Tin being built.
	 */
	public void addTinBreakline(TinBreakline argTinBreakline)
	{
		this.tinBreaklines.add(argTinBreakline);
		
		// Index the breakline.
		this.tinIndex.indexTinBreakline(argTinBreakline);
	}
	
	/**
	 * Adds a TinBoundary to the TIN being built.
	 */
	public void addTinBoundary(TinBoundary argTinBoundary, boolean argIsOuterBoundary)
	{
		this.tinBoundaries.add(argTinBoundary);
		
		if(argIsOuterBoundary == true)
		{
			this.outerBoundary = argTinBoundary;
		}
		
		// Index the boundary.
		this.tinIndex.indexTinBoundary(argTinBoundary);
	}
	
	/**
	 * Adds a TinFace to the TIN being built. This is not typically called directly,
	 * but is used by TriangleCalculator objects passed to this classes "calculateTriangles"
	 * method.
	 */
	public void addTinFace(TinFace argTinFace)
	{
		this.tinFaces.add(argTinFace);
		
		// Index tinFace.
		this.tinIndex.indexTinFace(argTinFace);
	}
	
	/**
	 * Calculates the triangles for this TIN using the points, breaklines, and boundaries
	 * that were added.
	 */
	public void calculateTriangles(TriangleCalculator argCalculator)
	{
		argCalculator.calculateTriangles(this);
	}
	
	/**
	 * Returns the InMemoryTin using the data provided to this factory.
	 */
	public InMemoryTin build()
	{
		// Returns the InMemoryTin that was just built.
		InMemoryTin tin = new InMemoryTin(this.tinPoints, this.tinBreaklines, this.tinBoundaries, this.tinFaces, this.innerBoundaries, this.envelope, this.outerBoundary);
		return tin;
	}
	
	/**
	 * Returns the InMemoryTinIndex for the tin that was just built.
	 */
	public InMemoryTinIndex getTinIndex()
	{
		return this.tinIndex;
	}
	
	public void reset(double argPointSpatialIndexDim)
	{
		this.tinPoints.clear();
		this.tinBreaklines.clear();
		this.tinFaces.clear();
		this.tinBoundaries.clear();
		this.innerBoundaries.clear();
		this.tinIndex = new InMemoryTinIndex(argPointSpatialIndexDim);
	}
	
	public Iterator<TinPoint> getTinPoints()
	{
		return this.tinPoints.iterator();
	}
	
	public Iterator<TinBreakline> getTinBreaklines()
	{
		return this.tinBreaklines.iterator();
	}
	
	public Iterator<TinBoundary> getInnerBoundaries()
	{
		return this.innerBoundaries.iterator();
	}
	
	public TinBoundary getOuterBoundary()
	{
		return this.outerBoundary;
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
 