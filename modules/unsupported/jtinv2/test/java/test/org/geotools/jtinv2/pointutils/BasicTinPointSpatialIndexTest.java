package test.org.geotools.jtinv2.pointutils;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.geotools.jtinv2.main.TinPoint;
import org.geotools.jtinv2.pointutils.BasicTinPointSpatialIndex;
import org.geotools.jtinv2.pointutils.BasicTinPointSpatialIndexFactory;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class BasicTinPointSpatialIndexTest 
{
	private BasicTinPointSpatialIndex testSubject;
	private TinPoint point9;

	@Before
	public void setUp() throws Exception 
	{
		BasicTinPointSpatialIndexFactory factory = new BasicTinPointSpatialIndexFactory(200.0);
		
		Coordinate coord1 = new Coordinate(8900.0, 2200.0, 20.0);
		TinPoint point1 = new TinPoint(1, coord1);
		
		Coordinate coord2 = new Coordinate(9300.0, 2200.0);
		TinPoint point2 = new TinPoint(2, coord2);
		
		Coordinate coord3 = new Coordinate(9000.0, 2300.0, 10.0);
		TinPoint point3 = new TinPoint(3, coord3);
		
		Coordinate coord4 = new Coordinate(9200, 2300.0);
		TinPoint point4 = new TinPoint(4, coord4);
		
		Coordinate coord5 = new Coordinate(9200.0, 2400.0, 35.0);
		TinPoint point5 = new TinPoint(5, coord5);
		
		Coordinate coord6 = new Coordinate(9100.0, 2400.0, 10.0);
		TinPoint point6 = new TinPoint(6, coord6);
		
		Coordinate coord7 = new Coordinate(9100.0, 2500.0, 20.0);
		TinPoint point7 = new TinPoint(7, coord7);
		
		Coordinate coord8 = new Coordinate(9000.0, 2500.0);
		TinPoint point8 = new TinPoint(8, coord8);
		
		Coordinate coord9 = new Coordinate(9200.0, 2600.0);
		this.point9 = new TinPoint(9, coord9);
		
		// Add points for radius test near Point 9.
		Coordinate coord11 = new Coordinate(9210.0, 2600.0);
		TinPoint point11 = new TinPoint(11, coord11);
		
		Coordinate coord10 = new Coordinate(9200.0, 2610.0);
		TinPoint point10 = new TinPoint(10, coord10);
		
		factory.indexTinPoint(point1);
		factory.indexTinPoint(point2);
		factory.indexTinPoint(point3);
		factory.indexTinPoint(point4);
		factory.indexTinPoint(point5);
		factory.indexTinPoint(point6);
		factory.indexTinPoint(point7);
		factory.indexTinPoint(point8);
		factory.indexTinPoint(point9);
		factory.indexTinPoint(point10);
		factory.indexTinPoint(point11);
		
		this.testSubject = factory.build();
	}

	@Test
	public void testGetTinPointsInEnvelope1() 
	{
		Coordinate bottomLeft = new Coordinate(8800.0, 2100.0, 0.0);
		Coordinate topRight = new Coordinate(9900.0, 3000.0, 0.0);
		Envelope envelope = new Envelope(bottomLeft, topRight);
		
		Iterator<TinPoint> goOverEach = this.testSubject.getTinPointsInEnvelope(envelope);
		
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			int id = currentPoint.getIdentifier();
			
			if(id == 1)
			{
				successCounter++;
			}
			
			if(id == 2)
			{
				successCounter++;
			}
			
			if(id == 3)
			{
				successCounter++;
			}
			
			if(id == 4)
			{
				successCounter++;
			}
		}
		
		if(successCounter != 4)
		{
			System.err.println("Success count = ");
			System.err.println(successCounter);
			
			fail("The method did not return all the points in the envelope.");
		}
	}

	@Test
	public void testGetTinPointsInRangeOfTinPoint() 
	{
		Iterator<TinPoint> goOverEach = this.testSubject.getTinPointsInRangeOfTinPoint(this.point9, 12.0);
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			int id = currentPoint.getIdentifier();
			System.err.println(id);
			
			if(id == 10)
			{
				successCounter++;
			}
			
			if(id == 11)
			{
				successCounter++;
			}
		}
		
		if(successCounter != 2)
		{
			fail("The method did not return both points within range of the subject TinPoint.");
		}
	}
	
	@Test
	public void testGetTinPointsInEnvelope2() 
	{
		Coordinate bottomLeft = new Coordinate(9188.0, 2588.0, 0.0);
		Coordinate topRight = new Coordinate(9212.0, 2612.0, 0.0);
		Envelope envelope = new Envelope(bottomLeft, topRight);
		
		Iterator<TinPoint> goOverEach = this.testSubject.getTinPointsInEnvelope(envelope);
		
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			int id = currentPoint.getIdentifier();
			
			if(id == 10)
			{
				System.err.println("Found point 12!");
				successCounter++;
			}
			
			if(id == 11)
			{
				System.err.println("Found point 11!");
				successCounter++;
			}
		}
		
		if(successCounter != 2)
		{
			System.err.println("Success count = ");
			System.err.println(successCounter);
			
			fail("The method did not return all the points in the envelope.");
		}
	}
}
