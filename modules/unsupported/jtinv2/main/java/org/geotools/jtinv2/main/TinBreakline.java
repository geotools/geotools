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
import com.vividsolutions.jts.geom.LineString;

/**
 * A LineString that represents a breakline for a TIN with a numeric identifier, a category,
 * and a short description.
 * 
 * @author Landon Blake
 *
 */
public class TinBreakline implements TinBuildingBlock
{
	// Private member variables.
	private int identifier;
	private String category;
	private String description;
	private LineString linestring;
	private boolean hasCategory;
	private boolean hasDescription;
		
		
	// Constructors
	public TinBreakline(int argIdentifier, LineString argLineString)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.hasCategory = false;
		this.hasDescription = false;
	}
		
	public TinBreakline(int argIdentifier, LineString argLineString, String argCategory)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.category = argCategory;
		this.hasCategory = true;
		this.hasDescription = false;
	}
		
	public TinBreakline(int argIdentifier, LineString argLineString, String argCategory, String argDescription)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.category = argCategory;
		this.description = argDescription;
		this.hasCategory = true;
		this.hasDescription = true;
	}
		
		
	// Public Methods
	@Override
	public int getIdentifier() 
	{
		return this.identifier;
	}

	@Override
	public String getCategory() 
	{
		if(this.hasCategory() == false)
		{
			IllegalStateException error = new IllegalStateException("The TinPoint has no category.");
			throw error;
		}
			
		return this.category;
	}

	@Override
	public String getDescription() 
	{
		if(this.hasDescription() == false)
		{
			IllegalStateException error = new IllegalStateException("The TinPoint has no description.");
			throw error;
		}
			
		return this.description;
	}
		
	public LineString getLineString()
	{
		return this.linestring;
	}
		
	public boolean hasCategory()
	{
		return this.hasCategory;
	}
		
	public boolean hasDescription()
	{
		return this.hasDescription;
	}
		
	public TinBreakline clone(int argIdentifier)
	{
		if(this.hasCategory() == true)
		{
			// This TinPoint has both a category and a description.
			if(this.hasDescription() == true)
			{
				TinBreakline toReturn = new TinBreakline(argIdentifier, this.linestring, this.category, this.description);
				return toReturn;
			}
				
			// This TinPoint only has a category.
			else
			{
				TinBreakline toReturn = new TinBreakline(argIdentifier, this.linestring, this.category);
				return toReturn;
			}
		}
			
		// This TinPoint only has a coordinate.
		TinBreakline toReturn = new TinBreakline(argIdentifier, this.linestring);
		return toReturn;
	}
		
	public boolean equals(TinBreakline argCompareMe)
	{
		int equalityCounter = this.howEqual(argCompareMe);
			
		if(equalityCounter == 3)
		{
			return true;
		}
			
		else
		{
			return false;
		}
	}
		
	public int howEqual(TinBreakline argCompareMe)
	{
		int equalityCounter = 0;
			
		// Check the linestring for equality.
			
		boolean areEqual = this.areLineStringsEqual(this.linestring, argCompareMe.getLineString(), 0.1);
		
		if(areEqual == true)
		{
			System.err.println("Marker 001");
			equalityCounter++;
		}
		
		// Check for the same category.
		if(this.category.equals(argCompareMe.getCategory()) == true)
		{
			System.err.println("Marker 002");
			equalityCounter++;
		}
			
		// Check for the same description.
		if(this.description.equals(argCompareMe.getDescription()) == true)
		{
			System.err.println("Marker 003");
			equalityCounter++;
		}
		
		return equalityCounter;
	}
		
	public String toString()
	{
		String linestringAsString = this.linestring.toString();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("[TIN Breakline = ");
		builder.append(linestringAsString);
			
		if(this.hasCategory() == true)
		{
			builder.append(" | Category: ");
			builder.append(this.category);
		}
			
		if(this.hasDescription() == true)
		{
			builder.append(" | Description: ");
			builder.append(this.description);
		}
			
		builder.append("]");
		
		return builder.toString();
	}
	
	private boolean areLineStringsEqual(LineString first, LineString second, double tolerance)
	{
		// Get the coordinates from each LineString.
		Coordinate[] firstCoords = first.getCoordinates();
		Coordinate[] secondCoords = second.getCoordinates();
		
		if(firstCoords.length != secondCoords.length)
		{
			return false;
		}
		
		int numberOfCoords = firstCoords.length;
		
		int counter = 0;
		
		while(counter < numberOfCoords)
		{
			double firstX = firstCoords[counter].x;
			double firstY = firstCoords[counter].y;
			double firstZ = firstCoords[counter].z;
			
			double secondX = secondCoords[counter].x;
			double secondY = secondCoords[counter].y;
			double secondZ = secondCoords[counter].z;
			
			double xDiff = firstX - secondX;
			double xDiffAbs = Math.abs(xDiff);
			
			double yDiff = firstY - secondY;
			double yDiffAbs = Math.abs(yDiff);
			
			double zDiff = firstZ - secondZ;
			double zDiffAbs = Math.abs(zDiff);
			
			if(zDiffAbs > tolerance)
			{
				return false;
			}
			
			if(yDiffAbs > tolerance)
			{
				return false;
			}
			
			if(zDiffAbs > tolerance)
			{
				return false;
			}
			
			counter++;
		}
		
		return true;
	}
	
}
