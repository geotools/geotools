package test.org.geotools.jtinv2.main;

import static org.junit.Assert.*;

import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TinPoint;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class TinFaceTest
{
	// Private member variables.

	
	TinFace testSubject1;
	TinFace testSubject2;
	
	@Before
	public void setUp() throws Exception 
	{
		// Set up the coordinates for the first face.
		Coordinate[] coordinates1 = new Coordinate[3];
		coordinates1[0] = new Coordinate(3700.00, 1650.00, 10.0);
		coordinates1[1] = new Coordinate(4000.00, 1700.00, 20.0);
		coordinates1[2] = new Coordinate(3900.00, 1600.00, 15.0);
		
		// Set up the identifiers for the TinPoints of the first face.
		int[] identifiers1 = new int[3];
		identifiers1[0] = 100;
		identifiers1[1] = 101;
		identifiers1[2] = 102;
		
		// Set up the coordinates for the second face.
		Coordinate[] coordinates2 = new Coordinate[3];
		coordinates2[0] = new Coordinate(3700.00, 1650.00, 10.0);
		coordinates2[1] = new Coordinate(4000.00, 1700.00, 10.0);
		coordinates2[2] = new Coordinate(3900.00, 1600.00, 10.0);
		
		// Set up the identifiers for the TinPoints of the second face.
		int[] identifiers2 = new int[3];
		identifiers2[0] = 200;
		identifiers2[1] = 201;
		identifiers2[2] = 202;
		
		this.testSubject1 = new TinFace(100, identifiers1, coordinates1);
		this.testSubject2 = new TinFace(101, identifiers2, coordinates2);
	}

	@Test
	public void testGetLowestElevation()
	{
		double minElevation = this.testSubject1.getLowestElevation();
		
		if(minElevation != 10.0)
		{
			System.err.println(minElevation);
			fail("The method didn't return the lowest elevation.");
		}
	}

	@Test
	public void testGetHighestElevation()
	{
			double maxElevation = this.testSubject1.getHighestElevation();
			
			if(maxElevation != 20.0)
			{
				fail("The method didn't return the highest elevation.");
			}
	}

	@Test
	public void testGetVertices() 
	{
		TinPoint[] expectedVertices = new TinPoint[3];
		TinPoint[] actualVertices = this.testSubject1.getVertices();
		
		Coordinate[] coordinates1 = new Coordinate[3];
		coordinates1[0] = new Coordinate(3700.00, 1650.00, 10.0);
		coordinates1[1] = new Coordinate(4000.00, 1700.00, 20.0);
		coordinates1[2] = new Coordinate(3900.00, 1600.00, 15.0);
		
		expectedVertices[0] = new TinPoint(1, coordinates1[0]);
		expectedVertices[1] = new TinPoint(2, coordinates1[1]);
		expectedVertices[2] = new TinPoint(3, coordinates1[2]);
		
		if(expectedVertices[0].equals(actualVertices[0]) == false)
		{	
			System.err.println("Easting = ");
			System.err.println(actualVertices[0].getCoordinate().x);
			
			System.err.println("Northing = ");
			System.err.println(actualVertices[0].getCoordinate().y);
			
			System.err.println("Elevation = ");
			System.err.println(actualVertices[0].getCoordinate().z);
			
			fail("The first vertice did not match.");
		}
		
		if(expectedVertices[1].equals(actualVertices[1]) == false)
		{	
			fail("The first vertice did not match.");
		}
		
		if(expectedVertices[2].equals(actualVertices[2]) == false)
		{	
			fail("The first vertice did not match.");
		}
	}

	@Test
	public void testGetCoordinates() 
	{
		Coordinate expectedCoord1 = new Coordinate(3700.00, 1650.00, 10.0);
		Coordinate expectedCoord2 = new Coordinate(4000.00, 1700.00, 20.0);
		Coordinate expectedCoord3 = new Coordinate(3900.00, 1600.00, 15.0);
		
		Coordinate[] actualCoords = this.testSubject1.getCoordinates();
		
		if(expectedCoord1.equals(actualCoords[0]) == false)
		{
			fail("The first of the returned coordinates was not correct.");
		}
		
		if(expectedCoord2.equals(actualCoords[1]) == false)
		{
			fail("The first of the returned coordinates was not correct.");
		}
		
		if(expectedCoord3.equals(actualCoords[2]) == false)
		{
			fail("The first of the returned coordinates was not correct.");
		}
	}

	@Test
	public void testGetAsPolygon() 
	{
		Coordinate[] coords = new Coordinate[4];
		
		coords[0] = new Coordinate(3700.00, 1650.00, 10.0);
		coords[1] = new Coordinate(4000.00, 1700.00, 20.0);
		coords[2] = new Coordinate(3900.00, 1600.00, 15.0);
		coords[3] = new Coordinate(3700.00, 1650.00, 10.0);
		
		GeometryFactory factory = new GeometryFactory();
		
		LinearRing outside = factory.createLinearRing(coords);
		Polygon expectedTriangle = factory.createPolygon(outside, null);
		
		Polygon actualTriangle = this.testSubject1.getAsPolygon();
		
		if(expectedTriangle.equals(actualTriangle) == false)
		{
			fail("The triangles were not equal.");
		}
	}

	@Test
	public void testIsLevel() 
	{
		boolean shouldNotBeTrue = this.testSubject1.isLevel(0.10);
		boolean shouldBeTrue = this.testSubject2.isLevel(0.10);
		
		if(shouldNotBeTrue == true)
		{	
			fail("The method said the TinFace was level when it wasn't.");
		}
		
		if(shouldBeTrue == false)
		{
			fail("The method said the TinFace wasn't level when it was.");
		}
	}

	@Test
	public void testGetEnvelope() 
	{	
		Envelope expectedEnvelope = new Envelope(3700.00, 4000.00, 1600.00, 1700.00);
		
		Envelope actualEnvelope = this.testSubject1.getEnvelope();
		
		if(expectedEnvelope.equals(actualEnvelope) == false)
		{
			fail("The method didn't return the correct envelope.");
		}
	}
}
