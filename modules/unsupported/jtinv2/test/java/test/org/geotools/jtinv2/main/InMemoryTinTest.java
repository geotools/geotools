package test.org.geotools.jtinv2.main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.geotools.jtinv2.main.InMemoryTin;
import org.geotools.jtinv2.main.InMemoryTinFactory;
import org.geotools.jtinv2.main.TinBoundary;
import org.geotools.jtinv2.main.TinBreakline;
import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TinPoint;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class InMemoryTinTest 
{
	// Private member variables.
	private InMemoryTin testSubject;
	private TinPoint tinPoint1;
	
	@Before
	public void setUp() throws Exception 
	{
		InMemoryTinFactory factory = new InMemoryTinFactory(200.00);
		
		this.addTinPoints(factory);
		this.addTinBreaklines(factory);
		this.addTinBoundaries(factory);
		this.addTinFaces(factory);
		
		this.testSubject = factory.build();
	}

	@Test
	public void testGetNumberOfTinPoints()
	{
		int numberOfPoints = this.testSubject.getNumberOfTinPoints();
		
		if(numberOfPoints != 9)
		{
			fail("The method did not return the proper number of TinPoints.");
		}
	}

	@Test
	public void testGetNumberOfTriangles() 
	{
		int numberOfTriangles = this.testSubject.getNumberOfTriangles();
		
		if(numberOfTriangles != 8)
		{
			fail("The method did not return the proper number of triangles.");
		}
	}

	@Test
	public void testGetNumberOfBreaklines() 
	{
		int numberOfBreaklines = this.testSubject.getNumberOfBreaklines();
				
		if(numberOfBreaklines != 1)
		{
			fail("The method did not return the proper number of breaklines.");
		}
	}

	@Test
	public void testGetNumberOfBoundaries() 
	{
		int numberOfBoundaries = this.testSubject.getNumberOfBoundaries();
		
		if(numberOfBoundaries != 2)
		{
			fail("The method did not return the proper number of boundaries.");
		}
	}

	@Test
	public void testGetTinPointsIterator() 
	{
		Iterator<TinPoint> goOverEach = this.testSubject.getTinPointsIterator();
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			int currentId = currentPoint.getIdentifier();
			
			if(counter != currentId)
			{
				fail("The iterator didn't correctly return all of the TinPoints.");
			}
			
			counter++;
		}
	}

	@Test
	public void testGetBreaklinesIterator() 
	{
		Iterator<TinBreakline> goOverEach = this.testSubject.getTinBreaklinesIterator();
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			TinBreakline currentBreakline = goOverEach.next();
			int currentId = currentBreakline.getIdentifier();
			
			if(counter != currentId)
			{
				fail("The iterator didn't correctly return all of the breaklines.");
			}
			
			counter++;
		}
	}

	@Test
	public void testGetBoundariesIterator() 
	{
		Iterator<TinBoundary> goOverEach = this.testSubject.getTinBoundariesIterator();
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			TinBoundary currentBoundary = goOverEach.next();
			int currentId = currentBoundary.getIdentifier();
			
			if(counter != currentId)
			{
				fail("The iterator didn't correctly return all of the boundaries.");
			}
			
			counter++;
		}
	}

	@Test
	public void testGetOuterBoundary() 
	{
		TinBoundary outerBoundary = this.testSubject.getOuterBoundary();
		int id = outerBoundary.getIdentifier();
		
		if(id != 1)
		{
			fail("The method did not return the outer boundary.");
		}
	}

	@Test
	public void testGetHoleIterator() 
	{
		Iterator<TinBoundary> goOverEach = this.testSubject.getHoleIterator();
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			TinBoundary currentBoundary = goOverEach.next();
			int currentId = currentBoundary.getIdentifier();
			
			if(counter != currentId)
			{
				fail("The iterator didn't correctly return all of the boundaries.");
			}
			
			counter++;
		}
	}

	@Test
	public void testGetEnvelope() 
	{
		Envelope expectedEnvelope = new Envelope(8900.00, 9300.00, 2200.00, 2600.00);
		
		Envelope actualEnvelope = this.testSubject.getEnvelope();
		
		if(expectedEnvelope.equals(actualEnvelope) == false)
		{
			fail("The method did not return the correct envelope.");
		}
	}
	
	@Test
	public void testGetTinPointAtLocation()
	{
		Coordinate location = this.tinPoint1.getCoordinate();
		
		TinPoint returnedTinPoint = this.testSubject.getTinPointAtLocation(location, 0.10);
		
		int id = returnedTinPoint.getIdentifier();
		
		if(id != 1)
		{
			fail("The proper TinPoint was not returned.");
		}
	}
	
	
	// Private helper methods.
	private void addTinFaces(InMemoryTinFactory factory)
	{
		// Coordinates for TinPoints.
		Coordinate coordinate1 = new Coordinate(8900.00, 2200.00, 20.00);
		Coordinate coordinate2 = new Coordinate(9300.00, 2200.00, 40.00);
		Coordinate coordinate3 = new Coordinate(9000.00, 2300.00, 10.00);
		Coordinate coordinate4 = new Coordinate(9200.00, 2300.00, 10.00);
		Coordinate coordinate5 = new Coordinate(9200.00, 2400.00, 35.00);
		Coordinate coordinate6 = new Coordinate(9100.00, 2400.00, 10.00);
		Coordinate coordinate7 = new Coordinate(9100.00, 2500.00, 35.00);
		Coordinate coordinate8 = new Coordinate(9000.00, 2500.00, 25.00);
		Coordinate coordinate9 = new Coordinate(9200.00, 2600.00, 20.00);
	
		// TinPoints for TinFaces.
		TinPoint tinPoint1 = new TinPoint(1, coordinate1, "Ground", "Survey Limit");
		TinPoint tinPoint2 = new TinPoint(2, coordinate2, "Ground", "Ridge");
		TinPoint tinPoint3 = new TinPoint(3, coordinate3, "Ground", "Edge of Water");
		TinPoint tinPoint4 = new TinPoint(4, coordinate4, "Ground", "Edge of Water");
		TinPoint tinPoint5 = new TinPoint(5, coordinate5, "Ground", "Ridge");
		TinPoint tinPoint6 = new TinPoint(6, coordinate6, "Ground", "Edge of Water");
		TinPoint tinPoint7 = new TinPoint(7, coordinate7, "Ground", "Ridge");
		TinPoint tinPoint8 = new TinPoint(8, coordinate8, "Ground", "Ridge");
		TinPoint tinPoint9 = new TinPoint(9, coordinate9, "Ground", "Survey Limit");

		// Create TinFaces.
		TinPoint[] faceCoords1 = new TinPoint[3];
		faceCoords1[0] = tinPoint1;
		faceCoords1[1] = tinPoint2;
		faceCoords1[2] = tinPoint3;
		TinFace face1 = new TinFace(1, faceCoords1);
		
		TinPoint[] faceCoords2 = new TinPoint[3];
		faceCoords2[0] = tinPoint1;
		faceCoords2[1] = tinPoint3;
		faceCoords2[2] = tinPoint8;
		TinFace face2 = new TinFace(2, faceCoords2);
		
		TinPoint[] faceCoords3 = new TinPoint[3];
		faceCoords3[0] = tinPoint3;
		faceCoords3[1] = tinPoint8;
		faceCoords3[2] = tinPoint6;
		TinFace face3 = new TinFace(3, faceCoords3);
		
		TinPoint[] faceCoords4 = new TinPoint[3];
		faceCoords4[0] = tinPoint6;
		faceCoords4[1] = tinPoint7;
		faceCoords4[2] = tinPoint8;
		TinFace face4 = new TinFace(4, faceCoords4);
		
		TinPoint[] faceCoords5 = new TinPoint[3];
		faceCoords5[0] = tinPoint4;
		faceCoords5[1] = tinPoint5;
		faceCoords5[2] = tinPoint6;
		TinFace face5 = new TinFace(5, faceCoords5);
		
		TinPoint[] faceCoords6 = new TinPoint[3];
		faceCoords6[0] = tinPoint5;
		faceCoords6[1] = tinPoint6;
		faceCoords6[2] = tinPoint7;
		TinFace face6 = new TinFace(6, faceCoords6);
		
		TinPoint[] faceCoords7 = new TinPoint[3];
		faceCoords7[0] = tinPoint5;
		faceCoords7[1] = tinPoint6;
		faceCoords7[2] = tinPoint9;
		TinFace face7 = new TinFace(7, faceCoords7);
		
		TinPoint[] faceCoords8 = new TinPoint[3];
		faceCoords8[0] = tinPoint7;
		faceCoords8[1] = tinPoint8;
		faceCoords8[2] = tinPoint9;
		TinFace face8 = new TinFace(8, faceCoords8);
		
		factory.addTinFace(face1);
		factory.addTinFace(face2);
		factory.addTinFace(face3);
		factory.addTinFace(face4);
		factory.addTinFace(face5);
		factory.addTinFace(face6);
		factory.addTinFace(face7);
		factory.addTinFace(face8);
	}

	private void addTinBoundaries(InMemoryTinFactory factory) 
	{
		Coordinate coordinate1 = new Coordinate(8900.00, 2200.00, 20.00);
		Coordinate coordinate2 = new Coordinate(9300.00, 2200.00, 40.00);
		Coordinate coordinate3 = new Coordinate(9000.00, 2300.00, 10.00);
		Coordinate coordinate4 = new Coordinate(9200.00, 2300.00, 10.00);
		Coordinate coordinate5 = new Coordinate(9200.00, 2400.00, 35.00);
		Coordinate coordinate6 = new Coordinate(9100.00, 2400.00, 10.00);
		Coordinate coordinate8 = new Coordinate(9000.00, 2500.00, 25.00);
		Coordinate coordinate9 = new Coordinate(9200.00, 2600.00, 20.00);
		
		// Add outer boundary.
		GeometryFactory geometryFactory = new GeometryFactory();
		
		Coordinate[] coords1 = new Coordinate[5];
		
		coords1[0] = coordinate1;
		coords1[1] = coordinate2;
		coords1[2] = coordinate5;
		coords1[3] = coordinate9;
		coords1[4] = coordinate8;
		
		LineString lineString1 = geometryFactory.createLineString(coords1);
		
		TinBoundary outerBoundary = new TinBoundary(1, lineString1, "Boundaries", "Survey Limit", true);
	
		factory.addTinBoundary(outerBoundary, true);
		
		// Add the hole boundary.
		Coordinate[] coords2 = new Coordinate[3];
		
		coords2[0] = coordinate3;
		coords2[1] = coordinate4;
		coords2[2] = coordinate6;

		LineString lineString2 = geometryFactory.createLineString(coords2);
		
		TinBoundary holeBoundary = new TinBoundary(2, lineString2, "Boundaries", "Edge of Water", false);
	
		factory.addTinBoundary(holeBoundary, false);
	}

	private void addTinBreaklines(InMemoryTinFactory factory) 
	{
		// Create the TinPoints for our test subject.
		Coordinate coordinate2 = new Coordinate(9300.00, 2200.00, 40.00);
		Coordinate coordinate5 = new Coordinate(9200.00, 2400.00, 35.00);
		Coordinate coordinate7 = new Coordinate(9100.00, 2500.00, 35.00);
		Coordinate coordinate8 = new Coordinate(9000.00, 2500.00, 25.00);
		
		GeometryFactory geometryFactory = new GeometryFactory();
		
		Coordinate[] coords = new Coordinate[4];
		
		coords[0] = coordinate2;
		coords[1] = coordinate5;
		coords[2] = coordinate7;
		coords[3] = coordinate8;
		
		LineString lineString = geometryFactory.createLineString(coords);
	
		TinBreakline breakline = new TinBreakline(1, lineString, "Ridges", "Main Ridge");
	
		factory.addTinBreakline(breakline);
	}

	private void addTinPoints(InMemoryTinFactory factory) 
	{
		// Create the TinPoints for our test subject.
		Coordinate coordinate1 = new Coordinate(8900.00, 2200.00, 20.00);
		Coordinate coordinate2 = new Coordinate(9300.00, 2200.00, 40.00);
		Coordinate coordinate3 = new Coordinate(9000.00, 2300.00, 10.00);
		Coordinate coordinate4 = new Coordinate(9200.00, 2300.00, 10.00);
		Coordinate coordinate5 = new Coordinate(9200.00, 2400.00, 35.00);
		Coordinate coordinate6 = new Coordinate(9100.00, 2400.00, 10.00);
		Coordinate coordinate7 = new Coordinate(9100.00, 2500.00, 35.00);
		Coordinate coordinate8 = new Coordinate(9000.00, 2500.00, 25.00);
		Coordinate coordinate9 = new Coordinate(9200.00, 2600.00, 20.00);
		
		tinPoint1 = new TinPoint(1, coordinate1, "Ground", "Survey Limit");
		TinPoint tinPoint2 = new TinPoint(2, coordinate2, "Ground", "Ridge");
		TinPoint tinPoint3 = new TinPoint(3, coordinate3, "Ground", "Edge of Water");
		TinPoint tinPoint4 = new TinPoint(4, coordinate4, "Ground", "Edge of Water");
		TinPoint tinPoint5 = new TinPoint(5, coordinate5, "Ground", "Ridge");
		TinPoint tinPoint6 = new TinPoint(6, coordinate6, "Ground", "Edge of Water");
		TinPoint tinPoint7 = new TinPoint(7, coordinate7, "Ground", "Ridge");
		TinPoint tinPoint8 = new TinPoint(8, coordinate8, "Ground", "Ridge");
		TinPoint tinPoint9 = new TinPoint(9, coordinate9, "Ground", "Survey Limit");
	
		factory.addTinPoint(tinPoint1);
		factory.addTinPoint(tinPoint2);
		factory.addTinPoint(tinPoint3);
		factory.addTinPoint(tinPoint4);
		factory.addTinPoint(tinPoint5);
		factory.addTinPoint(tinPoint6);
		factory.addTinPoint(tinPoint7);
		factory.addTinPoint(tinPoint8);
		factory.addTinPoint(tinPoint9);
	}


}
