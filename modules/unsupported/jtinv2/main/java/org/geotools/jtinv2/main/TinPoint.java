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

import org.geotools.jtinv2.index.HasElevation;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * A point used to build a TIN that has a numeric identifier, category, and a
 * short description.
 *
 */
public class TinPoint implements TinBuildingBlock, HasElevation
{

	// Private member variables.
	private int identifier;
	private String category;
	private String description;
	private Coordinate coordinate;
	private boolean hasCategory;
	private boolean hasDescription;
	
	
	// Constructors
	public TinPoint(int argIdentifier, Coordinate argCoordinate)
	{
		this.identifier = argIdentifier;
		this.coordinate = argCoordinate;
		this.hasCategory = false;
		this.hasDescription = false;
	}
	
	public TinPoint(int argIdentifier, Coordinate argCoordinate, String argCategory)
	{
		this.identifier = argIdentifier;
		this.coordinate = argCoordinate;
		this.category = argCategory;
		this.hasCategory = true;
		this.hasDescription = false;
	}
	
	public TinPoint(int argIdentifier, Coordinate argCoordinate, String argCategory, String argDescription)
	{
		this.identifier = argIdentifier;
		this.coordinate = argCoordinate;
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
	
	public Coordinate getCoordinate()
	{
		return this.coordinate;
	}
	
	public boolean hasCategory()
	{
		return this.hasCategory;
	}
	
	public boolean hasDescription()
	{
		return this.hasDescription;
	}
	
	public double getNorthing()
	{
		return this.coordinate.y;
	}
	
	public double getEasting()
	{
		return this.coordinate.x;
	}
	
	public double getElevation()
	{
		return this.coordinate.z;
	}
	
	public TinPoint clone(int argIdentifier)
	{
		if(this.hasCategory() == true)
		{
			// This TinPoint has both a category and a description.
			if(this.hasDescription() == true)
			{
				TinPoint toReturn = new TinPoint(argIdentifier, this.coordinate, this.category, this.description);
				return toReturn;
			}
			
			// This TinPoint only has a category.
			else
			{
				TinPoint toReturn = new TinPoint(argIdentifier, this.coordinate, this.category);
				return toReturn;
			}
		}
		
		// This TinPoint only has a coordinate.
		TinPoint toReturn = new TinPoint(argIdentifier, this.coordinate);
		
		return toReturn;
	}
	
	public boolean equals(TinPoint argCompareMe)
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
	
	public int howEqual(TinPoint argCompareMe)
	{
		int equalityCounter = 0;
		
		// Check the coordinates for equality.
		double easting = this.coordinate.x;
		double northing = this.coordinate.y;
		double elevation = this.coordinate.z;
		
		if(easting == argCompareMe.getEasting() == true)
		{
			if(northing == argCompareMe.getNorthing() == true)
			{
				if(elevation == argCompareMe.getElevation() == true)
				{
					equalityCounter++;
				}
			}
		}
		
		// Check for the same category.
		if(this.hasCategory() == true)
		{
			// Does the other TinPoint have a category?
			if(argCompareMe.hasCategory() == true)
			{
				if(this.category.equals(argCompareMe.getCategory()) == true)
				{
					equalityCounter++;
				}
			}
		}
		
		else
		{
			equalityCounter++;
		}
		
		// Check for the same description.
		if(this.hasDescription() == true)
		{
			// Does the other TinPoint have a description?
			if(argCompareMe.hasDescription() == true)
			{
				if(this.description.equals(argCompareMe.getDescription()) == true)
				{
					equalityCounter++;
				}
			}
		}
		
		else
		{
			equalityCounter++;
		}
		
		return equalityCounter;
	}
	
	public boolean hasSameHorizontalPosition(TinPoint argCompareMe, double argTolerance)
	{
		// Check the horizontal position for equality.
		double easting = this.coordinate.x;
		double northing = this.coordinate.y;
		
		double otherEasting = argCompareMe.getEasting();
		double otherNorthing = argCompareMe.getNorthing();
		
		double eastingDiff = Math.abs(easting - otherEasting);
		double northingDiff = Math.abs(northing - otherNorthing);
		
		if(eastingDiff < argTolerance)
		{
			return true;
		}
		
		if(northingDiff < argTolerance)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean hasSameElevation(TinPoint argCompareMe, double argTolerance)
	{
		double elevation = this.coordinate.z;
		double otherElevation = argCompareMe.getElevation();
		
		double elevationDiff = elevation - otherElevation;
		double elevationDiffAbs = Math.abs(elevationDiff);
		
		if(elevationDiffAbs < argTolerance)
		{
			return true;
		}
		
		return false;
	}
	
	public String toString()
	{
		Double northingAsDouble = new Double(this.coordinate.y);
		String northingAsString = northingAsDouble.toString();
		
		Double eastingAsDouble = new Double(this.coordinate.x);
		String eastingAsString = eastingAsDouble.toString();
		
		Double elevationAsDouble = new Double(this.coordinate.z);
		String elevationAsString = eastingAsDouble.toString();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("[TIN Point = Northing: ");
		builder.append(northingAsString);
		builder.append(" | ");
		
		builder.append(" Easting: ");
		builder.append(eastingAsString);
		builder.append(" | ");
		
		builder.append(" Elevation: ");
		builder.append(elevationAsString);

		
		if(this.hasCategory() == true)
		{
			builder.append(" | ");
			builder.append(" Category: ");
			builder.append(this.category);
		}
		
		if(this.hasDescription() == true)
		{
			builder.append(" | ");
			builder.append(" Description: ");
			builder.append(this.description);
		}
		
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	public double getPrimaryElevation() 
	{
		return this.getElevation();
	}

	@Override
	public double getLowestElevation() 
	{
		UnsupportedOperationException error = new UnsupportedOperationException
				("This method is not implemented.");
		
		throw error;
	}

	@Override
	public double getHighestElevation() 
	{
		UnsupportedOperationException error = new UnsupportedOperationException
				("This method is not implemented.");
		
		throw error;
	}

	@Override
	public boolean hasPrimaryElevation() 
	{
		return true;
	}

	@Override
	public boolean hasLowestElevation() 
	{
		return false;
	}

	@Override
	public boolean hasHighestElevation() 
	{
		return false;
	}
	
	@Override
	public String getElevationIdentifier()
	{
		Integer idAsInteger = new Integer(this.identifier);
		return idAsInteger.toString();
	}
}
