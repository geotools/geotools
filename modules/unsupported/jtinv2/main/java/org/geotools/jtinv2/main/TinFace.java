package org.geotools.jtinv2.main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

public class TinFace 
{

	// Private Member Variables
	private Envelope envelope;
	private TinPoint[] points;
	private int identifier;
	
	
	// Constructors
	public TinFace(int argIdentifier, TinPoint[] argPoints)
	{
		this.identifier = argIdentifier;
		
		// Do some error checking.
		if(argPoints.length != 3)
		{
			IllegalArgumentException error = new IllegalArgumentException
					("The array should only have 3 points");
			
			throw error;
		}
		
		this.points = argPoints;
		
		this.createEnvelope();
	}
	

	public TinFace(int argIdentifier, int[] argPointIdentifiers, Coordinate[] argCoordinates)
	{
		this.identifier = argIdentifier;
		
		// Do some error checking.
		if(argCoordinates.length != 3)
		{
			IllegalArgumentException error = new IllegalArgumentException
					("The array should only have 3 points");
					
			throw error;
		}
		
		this.points = new TinPoint[3];

		// Create dummy TinPoints.

		this.points[0] = new TinPoint(argPointIdentifiers[0], argCoordinates[0]);
		this.points[1] = new TinPoint(argPointIdentifiers[0], argCoordinates[1]);
		this.points[2] = new TinPoint(argPointIdentifiers[0], argCoordinates[2]);

		this.createEnvelope();
	}
	
	public double getLowestElevation()
	{
		// Find the coordinate with the lowest elevation.
		Coordinate[] coordinates = this.getCoordinates();
		
		double lowest = coordinates[1].z;
		
		if(coordinates[0].z < coordinates[1].z)
		{
			lowest = coordinates[0].z;
		}
		
		if(coordinates[2].z < coordinates[1].z)
		{
			if(coordinates[2].z < coordinates[0].z)
			{
				lowest = coordinates[2].z;
			}
		}
		
		return lowest;
	}

	public double getHighestElevation()
	{
		// Find the coordinate with the highest elevation.
		Coordinate[] coordinates = this.getCoordinates();
		
		double highest = coordinates[1].z;
		
		if(coordinates[0].z > coordinates[1].z)
		{
			highest = coordinates[0].z;
		}
		
		if(coordinates[2].z > coordinates[1].z)
		{
			if(coordinates[2].z > coordinates[0].z)
			{
				highest = coordinates[2].z;
			}
		}
		
		return highest;
	}

	public TinPoint[] getVertices()
	{
		return this.points;
	}
	
	public Coordinate[] getCoordinates()
	{
		Coordinate[] coordinates = new Coordinate[3];
		
		coordinates[0] = this.points[0].getCoordinate();
		coordinates[1] = this.points[1].getCoordinate();
		coordinates[2] = this.points[2].getCoordinate();
		return coordinates;
	}
	
	public Polygon getAsPolygon()
	{
		Coordinate[] coordinates = new Coordinate[4];

		Coordinate[] coordsForPoly = new Coordinate[4];
		
		coordsForPoly[0] = coordinates[0];
		coordsForPoly[1] = coordinates[1];
		coordsForPoly[2] = coordinates[2];
		coordsForPoly[3] = coordinates[0];

		GeometryFactory factory = new GeometryFactory();
		
		LinearRing outside = factory.createLinearRing(coordsForPoly);
		Polygon triangle = factory.createPolygon(outside, null);
		
		return triangle;
	}
	
	public boolean isLevel(double argTolerance)
	{
		Coordinate[] coordinates = this.getCoordinates();
		
		double firstDiff = coordinates[0].z - coordinates[1].z;
		
		double secondDiff = coordinates[1].z - coordinates[2].z;
				
		// We need absolute values.
		double firstDiffAbs = Math.abs(firstDiff);
		double secondDiffAbs = Math.abs(secondDiff);
		
		if(firstDiffAbs < argTolerance)
		{
			if(secondDiffAbs < argTolerance)
			{
				return true;
			}
		}

		return false;
	}
	
	public Envelope getEnvelope()
	{
		return this.envelope;
	}
	
	public int getIdentifier()
	{
		return this.identifier;
	}
	
	private void createEnvelope()
	{
		// Create the envelope.
		
		// Get the TinPoints as coordinates.
		Coordinate[] coordinates = this.getCoordinates();
				
		this.envelope = new Envelope(coordinates[0], coordinates[1]);
				
		// See if the third coordinate is in the envelope. If not,
		// expand the envelope.
		if(this.envelope.covers(coordinates[2]) == false)
		{
			this.envelope.expandToInclude(coordinates[2]);
		}
	}
}