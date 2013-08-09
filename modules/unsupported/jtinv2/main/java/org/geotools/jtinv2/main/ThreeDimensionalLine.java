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

import net.surveyos.sourceforge.jtswarped.angles.BasicSurveyorsDirection;

import com.vividsolutions.jts.geom.Coordinate;

public class ThreeDimensionalLine 
{
	// Private Member Variables
	private Coordinate firstEndPoint;
	private Coordinate secondEndPoint;
	
	// Constructors
	public ThreeDimensionalLine(Coordinate argFirstEndPoint, Coordinate argSecondEndPoint)
	{
		this.firstEndPoint = argFirstEndPoint;
		this.secondEndPoint = argSecondEndPoint;
	}
	
	// Public Method Definitions
	public Coordinate getFirstEndPoint()
	{
		return this.firstEndPoint;
	}
	
	public Coordinate getSecondEndPoint()
	{
		return this.secondEndPoint;
	}
	
	public Coordinate getLowerEndPoint()
	{
		double firstEndPointElevation = this.firstEndPoint.z;
		double secondEndPointElevation = this.secondEndPoint.z;
		
		if(firstEndPointElevation < secondEndPointElevation)
		{
			return this.firstEndPoint;
		}
		
		if(secondEndPointElevation < firstEndPointElevation)
		{
			return this.secondEndPoint;
		}
		
		else
		{
			IllegalStateException error = new IllegalStateException("Both end points have the same elevation.");
			throw error;
		}
	}
	
	public Coordinate getHigherEndPoint()
	{
		double firstEndPointElevation = this.firstEndPoint.z;
		double secondEndPointElevation = this.secondEndPoint.z;
		
		if(firstEndPointElevation > secondEndPointElevation)
		{
			return this.firstEndPoint;
		}
		
		if(secondEndPointElevation > firstEndPointElevation)
		{
			return this.secondEndPoint;
		}
		
		else
		{
			IllegalStateException error = new IllegalStateException("Both end points have the same elevation.");
			throw error;
		}
	}
	
	public boolean areEndPointsAtTheSameElevation()
	{
		double firstEndPointElevation = this.firstEndPoint.z;
		double secondEndPointElevation = this.secondEndPoint.z;
		
		if(firstEndPointElevation == secondEndPointElevation)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public double getSlopeLength()
	{
		double length = this.get2DLength();
		double changeInElevation = this.getChangeInElevation();
		
		return this.getHypotenuse(length, changeInElevation);
	}
	
	public double get2DLength()
	{
		double firstEndPointX = this.firstEndPoint.x;
		double firstEndPointY = this.secondEndPoint.y;
		
		double secondEndPointX = this.secondEndPoint.x;
		double secondEndPointY = this.secondEndPoint.y;
		
		double changeInX = firstEndPointX - secondEndPointX;
		double changeInY = firstEndPointY - secondEndPointY;
		
		double changeInXSquared = changeInX * changeInX;
		double changeInYSquared = changeInY * changeInY;
		
		double sumOfSquares = changeInXSquared + changeInYSquared;
		
		double length = Math.sqrt(sumOfSquares);
		
		return length;
	}
	
	public double getChangeInElevation()
	{
		double firstEndPointElevation = this.firstEndPoint.z;
		double secondEndPointElevation = this.secondEndPoint.z;
		
		return firstEndPointElevation - secondEndPointElevation;
	}
	
	public double getSlope()
	{
		double length = this.get2DLength();
		double changeInElevation = this.getChangeInElevation();
		
		double slope = changeInElevation / length;
		
		return slope;
	}
	
	public double getDistanceToElevationFromStartPoint(double argElevation)
	{
		// Get the change in elevation between the target elevation and the lowest point.
		double changeInElevation = argElevation - this.getLowestElevation();
		double slope = this.getSlope();
				
		double distance = changeInElevation / slope;
				
		return distance;
	}
	
	public boolean isElevationInBetweenEndPoints(double argElevation)
	{
		double highestElevation = this.getHigherElevation();
		double lowestElevation = this.getLowestElevation();
		
		if(argElevation < highestElevation)
		{
			if(argElevation > lowestElevation)
			{
				return true;
			}
		}
		
		// If we make it to this point in the method, both conditions above
		// were false.
		return false;
	}
	
	public double getElevationAt2dDistance(double argDistance)
	{
		double lowerElevation = this.getLowestElevation();
		double slope = this.getSlope();
		
		double changeInElevation = argDistance * slope;
		
		double elevationAtDistance = lowerElevation + changeInElevation;
		
		return elevationAtDistance;
	}
	
	public double getElevationAtSlopeDistance(double argSlopeDistance)
	{
		double slope = this.getSlope();
		
		double distance = argSlopeDistance / slope;
		
		double elevationAtDistance = this.getElevationAt2dDistance(distance);
		
		return elevationAtDistance;
	}
	
	public BasicSurveyorsDirection getHorizontalDirection()
	{
		
	}
	
	public BasicSurveyorsDirection getHorizontalDirectionAsDouble()
	{
		
	}
	
	public double getZenithAngleAsDouble()
	{
		
	}
	
	public List<ThreeDimensionalLine> getNormals()
	{
		// Stub
	}
	
	public double getLowestElevation()
	{
		Coordinate lowest = this.getLowerEndPoint();
		return lowest.z;
	}
	
	public double getHigherElevation()
	{
		Coordinate highest = this.getHigherEndPoint();
		return highest.z;
	}
		
	
	// Private Method Definitions
	
	private double getHypotenuse(double firstSideLength, double secondSideLength)
	{
		double firstSquare = firstSideLength * firstSideLength;
		double secondSquare = secondSideLength * secondSideLength;
		
		double sumOfSquares = firstSquare + secondSquare;
		
		return Math.sqrt(sumOfSquares);
	}
}
