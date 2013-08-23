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

import com.vividsolutions.jts.geom.LineString;

/**
 * A LineString that represents a boundary for a TIN with a numeric identifier, a category,
 * and a short description.
 * 
 * @author Landon Blake
 *
 */
public class TinBoundary implements TinBuildingBlock
{
	// Private member variables.
	private int identifier;
	private String category;
	private String description;
	private LineString linestring;
	private boolean hasCategory;
	private boolean hasDescription;
	private boolean isOuterBoundary;
	private boolean isInnerBoundary;
			
			
	// Constructors
	public TinBoundary(int argIdentifier, LineString argLineString, boolean argIsOuterBoundary)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.hasCategory = false;
		this.hasDescription = false;
		this.isOuterBoundary = argIsOuterBoundary;
			
		if(argIsOuterBoundary = true)
		{
			this.isInnerBoundary = false;
		}
			
		else
		{
			this.isInnerBoundary = true;
		}
	}
			
	public TinBoundary(int argIdentifier, LineString argLineString, String argCategory, boolean argIsOuterBoundary)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.hasCategory = true;
		this.hasDescription = false;
		this.isOuterBoundary = argIsOuterBoundary;
		
		if(argIsOuterBoundary = true)
		{
			this.isInnerBoundary = false;
		}
		
		else
		{
			this.isInnerBoundary = true;
		}
	}
			
	public TinBoundary(int argIdentifier, LineString argLineString, String argCategory, String argDescription, boolean argIsOuterBoundary)
	{
		this.identifier = argIdentifier;
		this.linestring = argLineString;
		this.description = argDescription;
		this.category = argCategory;
		this.hasCategory = true;
		this.hasDescription = true;
		this.isOuterBoundary = argIsOuterBoundary;
		
		if(argIsOuterBoundary = true)
		{
			this.isInnerBoundary = false;
		}
		
		else
		{
			this.isInnerBoundary = true;
		}
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
			
	public TinBoundary clone(int argIdentifier)
	{
		if(this.hasCategory() == true)
		{
			// This TinPoint has both a category and a description.
			if(this.hasDescription() == true)
			{
				TinBoundary toReturn = new TinBoundary(argIdentifier, this.linestring, this.category, this.description, this.isOuterBoundary);
				return toReturn;
			}
					
			// This TinPoint only has a category.
			else
			{
				TinBoundary toReturn = new TinBoundary(argIdentifier, this.linestring, this.category, this.isOuterBoundary);
				return toReturn;
			}
		}
				
		// This TinPoint only has a coordinate.
		TinBoundary toReturn = new TinBoundary(argIdentifier, this.linestring, this.isOuterBoundary);
		return toReturn;

	}
			
	public boolean equals(TinBoundary argCompareMe)
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
			
	public int howEqual(TinBoundary argCompareMe)
	{
		int equalityCounter = 0;
			
		// Check the linestring for equality.
		LineString otherLine = argCompareMe.getLineString();
		
		if(this.linestring.equals(otherLine) == true)
		{
			equalityCounter++;
		}
				
		// Check for the same category.
		if(this.category.equals(argCompareMe.getCategory()) == true)
		{
			equalityCounter++;
		}
				
		// Check for the same description.
		if(this.description.equals(argCompareMe.getDescription()) == true)
		{
			equalityCounter++;
		}
				
		return equalityCounter;
	}
			
	public String toString()
	{
		String linestringAsString = this.linestring.toString();
				
		StringBuilder builder = new StringBuilder();
	
		builder.append("[TIN Boundary = ");
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
	
	public boolean isOuterBoundary()
	{
		return this.isOuterBoundary;
	}
	
	public boolean isInnerBoundary()
	{
		return this.isInnerBoundary;
	}

}

