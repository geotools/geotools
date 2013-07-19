package test.org.geotools.jtinv2.main;

import static org.junit.Assert.*;

import org.geotools.jtinv2.main.ThreeDimensionalLine;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class ThreeDimensionalLineTest 
{
	// Private member variables.
	ThreeDimensionalLine testSubject;

	@Before
	public void setUp() throws Exception 
	{
		Coordinate firstEndPoint = new Coordinate(10.0, 10.0, 10.0);
		Coordinate secondEndPoint = new Coordinate(10.0, 20.0, 20.0);
		
		this.testSubject = new ThreeDimensionalLine(firstEndPoint, secondEndPoint);
	}

	@Test
	public void testGetFirstEndPoint() 
	{
		Coordinate expectedResult = new Coordinate(10.0, 10.0, 10.0);
		
		Coordinate firstCoordinate = this.testSubject.getFirstEndPoint();
		
		if(expectedResult.equals(firstCoordinate) == false)
		{
			fail("The coordinate returned by the method was not the correct coordinate.");
		}
	}

	@Test
	public void testGetSecondEndPoint() 
	{
		Coordinate expectedResult = new Coordinate(10.0, 20.0, 20.0);
		
		Coordinate secondCoordinate = this.testSubject.getSecondEndPoint();
		
		if(expectedResult.equals(secondCoordinate) == false)
		{
			fail("The coordinate returned by the method was not the correct coordinate.");
		}
	}

	@Test
	public void testGetLowerEndPoint() 
	{
		Coordinate higherEndPoint = testSubject.getHigherEndPoint();
		
		Coordinate secondCoordinate = this.testSubject.getSecondEndPoint();
		
		if(higherEndPoint.equals(secondCoordinate) == false)
		{
			fail("The coordinate returned by the method was not the correct coordinate.");
		}
	}

	@Test
	public void testGetHigherEndPoint() 
	{
		Coordinate lowerEndPoint = testSubject.getLowerEndPoint();
		
		Coordinate secondCoordinate = this.testSubject.getFirstEndPoint();
		
		if(lowerEndPoint.equals(secondCoordinate) == false)
		{
			fail("The coordinate returned by the method was not the correct coordinate.");
		}
	}

	@Test
	public void testAreEndPointsAtTheSameElevation() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetSlopeLength() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGet2DLength() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetChangeInElevation() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetSlope()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDistanceToElevationFromStartPoint() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testIsElevationInBetweenEndPoints() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetElevationAtDistance() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetHorizontalDirection() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetZenithAngle() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetNormals() 
	{
		fail("Not yet implemented");
	}

}
