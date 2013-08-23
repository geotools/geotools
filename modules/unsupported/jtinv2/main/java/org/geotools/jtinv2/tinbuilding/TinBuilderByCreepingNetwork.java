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

package org.geotools.jtinv2.tinbuilding;

import java.util.Iterator;
import java.util.Set;

import net.surveyos.sourceforge.javautilities.collections.trees.BasicTreeWithLevels;
import net.surveyos.sourceforge.javautilities.collections.trees.BasicTreeWithLevelsFactory;

import org.geotools.jtinv2.main.InMemoryTinFactory;
import org.geotools.jtinv2.main.TinPoint;

public class TinBuilderByCreepingNetwork implements TriangleCalculator 
{
	// Private member variables.
	private InMemoryTinFactory tinFactory;
	
	// Data element containers used during TinBuilding.
	private Set<TinPoint> unusedPoints;
	private BasicTreeWithLevelsFactory<TinPoint> treeFactory;
	private BasicTreeWithLevels<TinPoint> usedPoints;

	
	// Store the max and preferred edge lengths used to calculate
	// the triangles.
	private double maxEdgeLength;
	private double prefferedEdgeLength;
	
	public TinBuilderByCreepingNetwork(double argMaxEdgeLength, double argPreferredEdgeLength)
	{
		this.maxEdgeLength = argMaxEdgeLength;
		this.prefferedEdgeLength = argPreferredEdgeLength;
		
		// Get all of the TinPoints;
		Iterator<TinPoint> goOverEach = this.tinFactory.getTinPoints();
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			this.unusedPoints.add(currentPoint);
		}
	}
	
	
	@Override
	public String getName() 
	{
		return "TIN Builder By Creeping Network";
	}

	@Override
	public boolean supportsAnOuterBoundary() 
	{
		return true;
	}

	@Override
	public boolean supportsHoles() 
	{
		return true;
	}

	@Override
	public boolean supportsBreaklines() 
	{
		return true;
	}

	@Override
	public void calculateTriangles(InMemoryTinFactory argFactory) 
	{
		// Get the first point and create our tree.
		Iterator<TinPoint> goOverEach = this.unusedPoints.iterator();
		
		TinPoint firstPoint = goOverEach.next();
		
		this.treeFactory = new BasicTreeWithLevelsFactory<TinPoint>(firstPoint);

		// Start loop to build triangles.
	}

}
