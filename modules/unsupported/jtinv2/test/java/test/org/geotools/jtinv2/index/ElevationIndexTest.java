package test.org.geotools.jtinv2.index;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.geotools.jtinv2.index.ElevationIndex;
import org.geotools.jtinv2.index.HasElevation;
import org.geotools.jtinv2.main.TinPoint;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class ElevationIndexTest 
{
	private ElevationIndex testSubject;

	@Before
	public void setUp() throws Exception 
	{
		this.testSubject = new ElevationIndex(25.0);
		
		// Create the TinPoints for our test subject.
		Coordinate coordinate1 = new Coordinate(8900.00, 2200.00, 20.00);
		Coordinate coordinate2 = new Coordinate(9300.00, 2200.00, 40.00);
		Coordinate coordinate3 = new Coordinate(9000.00, 2300.00, 80.00);
		Coordinate coordinate4 = new Coordinate(9200.00, 2300.00, 120.00);
		Coordinate coordinate5 = new Coordinate(9200.00, 2400.00, 35.00);
		Coordinate coordinate6 = new Coordinate(9100.00, 2400.00, 40.00);
		Coordinate coordinate7 = new Coordinate(9100.00, 2500.00, 150.00);
		Coordinate coordinate8 = new Coordinate(9000.00, 2500.00, 125.00);
		Coordinate coordinate9 = new Coordinate(9200.00, 2600.00, 90.00);
		
		TinPoint tinPoint1 = new TinPoint(1, coordinate1, "Ground", "Survey Limit");
		TinPoint tinPoint2 = new TinPoint(2, coordinate2, "Ground", "Ridge");
		TinPoint tinPoint3 = new TinPoint(3, coordinate3, "Ground", "Edge of Water");
		TinPoint tinPoint4 = new TinPoint(4, coordinate4, "Ground", "Edge of Water");
		TinPoint tinPoint5 = new TinPoint(5, coordinate5, "Ground", "Ridge");
		TinPoint tinPoint6 = new TinPoint(6, coordinate6, "Ground", "Edge of Water");
		TinPoint tinPoint7 = new TinPoint(7, coordinate7, "Ground", "Ridge");
		TinPoint tinPoint8 = new TinPoint(8, coordinate8, "Ground", "Ridge");
		TinPoint tinPoint9 = new TinPoint(9, coordinate9, "Ground", "Survey Limit");
		
		this.testSubject.indexByPrimaryElevation(tinPoint1);
		this.testSubject.indexByPrimaryElevation(tinPoint2);
		this.testSubject.indexByPrimaryElevation(tinPoint3);
		this.testSubject.indexByPrimaryElevation(tinPoint4);
		this.testSubject.indexByPrimaryElevation(tinPoint5);
		this.testSubject.indexByPrimaryElevation(tinPoint6);
		this.testSubject.indexByPrimaryElevation(tinPoint7);
		this.testSubject.indexByPrimaryElevation(tinPoint8);
		this.testSubject.indexByPrimaryElevation(tinPoint9);
	}

	@Test
	public void testIndexByElevationGivenRange() 
	{
		boolean pointFound = false;
		
		Coordinate coordinate1 = new Coordinate(8900.00, 2200.00, 20.00);
		TinPoint tinPoint1 = new TinPoint(101, coordinate1, "Ground", "Survey Limit");
		
		this.testSubject.indexByPrimaryElevation(tinPoint1);
		
		Iterator<HasElevation> goOverEach = this.testSubject.getElementsInElevationRange(10.0, 30.0);
		
		while(goOverEach.hasNext() == true)
		{			
			TinPoint currentPoint = (TinPoint) goOverEach.next();
			int id = currentPoint.getIdentifier();
			
			if(id == 101)
			{
				pointFound = true;
			}
		}
		
		if(pointFound != true)
		{
			fail("Not yet implemented");
		}
	}

	@Test
	public void testIndexByElevation() 
	{
		Iterator<HasElevation> goOverEach = this.testSubject.getElementsAtElevation(40.0);
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = (TinPoint) goOverEach.next();
			int id = currentPoint.getIdentifier();
			
			if(id == 2)
			{
				successCounter++;
			}
			
			if(id == 6)
			{
				successCounter++;
			}
		}
		
		if(successCounter != 2)
		{
			fail("The method did not return all of the points.");
		}
	}

	@Test
	public void testGetElementsBelowElevation() 
	{
		Iterator<HasElevation> goOverEach = this.testSubject.getElementsBelowElevation(41.0);
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentElement = (TinPoint) goOverEach.next();
			int id = currentElement.getIdentifier();
			
			if(id == 2)
			{
				successCounter++;
			}
			
			if(id == 5)
			{
				successCounter++;
			}
			
			if(id == 6)
			{
				successCounter++;
			}
		}
		
		if(successCounter != 3)
		{
			fail("The method did not properly return the elements below the elevation.");
		}
	}

	@Test
	public void testGetElementsAboveElevation() 
	{
		{
			Iterator<HasElevation> goOverEach = this.testSubject.getElementsAboveElevation(119.0);
			int successCounter = 0;
			
			while(goOverEach.hasNext() == true)
			{
				TinPoint currentElement = (TinPoint) goOverEach.next();
				int id = currentElement.getIdentifier();
				
				if(id == 4)
				{
					successCounter++;
				}
				
				if(id == 7)
				{
					successCounter++;
				}
				
				if(id == 8)
				{
					successCounter++;
				}
			}
			
			if(successCounter != 3)
			{
				fail("The method did not properly return the elements below the elevation.");
			}
		}
	}

	@Test
	public void testGetNumberOfItemsIndexed() 
	{
		int numberOfItemsIndexed = this.testSubject.getNumberOfItemsIndexed();
		
		if(numberOfItemsIndexed != 9)
		{
			fail("The method did not return the proper number of points indexed.");
		}
	}

	@Test
	public void testGetIterator() 
	{
		Iterator<HasElevation> goOverEach = this.testSubject.getIterator();
		int successCounter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = (TinPoint) goOverEach.next();
			int id = currentPoint.getIdentifier();
			
			if(id > 0)
			{
				if(id < 10)
				{
					successCounter++;
				}
			}
		}
		
		if(successCounter != 9)
		{
			fail("The iterator didn't return all points indexed.");
		}
	}

}
