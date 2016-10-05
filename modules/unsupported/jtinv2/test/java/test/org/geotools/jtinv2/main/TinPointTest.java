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
 */

@author Julian Padilla

package test.org.geotools.jtinv2.main;

import static org.junit.Assert.*;

import org.geotools.jtinv2.main.TinPoint;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class TinPointTest 
{
	private TinPoint testSubject1;
	private TinPoint testSubject2;
	private TinPoint testSubject3;	
	private TinPoint testSubject4;
	private TinPoint testSubject5;

	@Before
	public void setUp() throws Exception 
	{
		Coordinate coord1 = new Coordinate(3700.00, 1650.00, 10.0);
		Coordinate coord2 = new Coordinate(3700.00, 1650.00, 10.0);
		Coordinate coord3 = new Coordinate(3900.00, 1600.00, 15.0);
		Coordinate coord4 = new Coordinate(3900.00, 1600.00, 15.0);
		Coordinate coord5 = new Coordinate(3700.00, 1650.00, 10.0);
		
		this.testSubject1 = new TinPoint(1, coord1, "Ground", "West Top of Bank");
		this.testSubject2 = new TinPoint(1, coord2, "Ground", "West Top of Bank");
		this.testSubject3 = new TinPoint(2, coord3, "Ground", "West Top of Bank");
		this.testSubject4 = new TinPoint(3, coord4);
		this.testSubject5 = new TinPoint(4, coord5);
	}

	@Test
	public void testGetIdentifier() 
	{
		if(this.testSubject1.getIdentifier() != 1)
		{
			System.err.println("Actual identifier = ");
			System.err.println(this.testSubject1.getIdentifier());
			fail("The propert TinPoint identifier was not returned.");
		}
	}

	@Test
	public void testGetCategory()
	{
		String category = this.testSubject1.getCategory();
		
		if(category.equals("Ground") == false)
		{
			System.err.println("Actual category = ");
			System.err.println(category);
			fail("The proper category was not returned.");
		}
	}

	@Test
	public void testGetDescription() 
	{
		String description = this.testSubject1.getDescription();
		
		if(description.equals("West Top of Bank") == false)
		{
			System.err.println("Actual description = ");
			System.err.println(description);
			fail("The proper category was not returned.");
		}
	}

	@Test
	public void testGetCoordinate() 
	{
		Coordinate expectedCoordinate = new Coordinate(3700.00, 1650.00, 10.0);
		Coordinate actualCoordinate = this.testSubject1.getCoordinate();
		
		boolean shouldBeEqual = expectedCoordinate.equals(actualCoordinate);
		
		if(shouldBeEqual == false)
		{
			fail("The proper coordinate was not returned.");
		}
	}

	@Test
	public void testHasCategory() 
	{
		boolean shouldBeTrue = this.testSubject1.hasCategory();
		boolean shouldNotBeTrue = this.testSubject4.hasCategory();
		
		if(shouldBeTrue == false)
		{
			fail("The method detected no category when there was one.");
		}
		
		if(shouldNotBeTrue == true)
		{
			fail("The method detected a category when there was none.");
		}
	}

	@Test
	public void testHasDescription()
	{
		boolean shouldBeTrue = this.testSubject1.hasDescription();
		boolean shouldNotBeTrue = this.testSubject4.hasDescription();
		
		if(shouldBeTrue == false)
		{
			fail("The method detected no description when there was one.");
		}
		
		if(shouldNotBeTrue == true)
		{
			fail("The method detected a description when there was none.");
		}
	}

	@Test
	public void testGetNorthing() 
	{
		double expectedNorthing = 1650.00;
		double actualNorthing = testSubject1.getNorthing();
		
		if(expectedNorthing != actualNorthing)
		{
			fail("The proper northing was not returned.");
		}
	}

	@Test
	public void testGetEasting() 
	{
		double expectedNEasting = 3700.00;
		double actualEasting = testSubject1.getEasting();
		
		if(expectedNEasting != actualEasting)
		{
			fail("The proper easting was not returned.");
		}
	}

	@Test
	public void testGetElevation() 
	{
		double expectedElevation = 10.00;
		double actualElevation = testSubject1.getElevation();
		
		if(expectedElevation != actualElevation)
		{
			fail("The proper elevation was not returned.");
		}
	}

	@Test
	public void testClone() 
	{
		TinPoint clone = this.testSubject1.clone(1);
		
		if(clone.equals(this.testSubject1) == false)
		{		
			fail("A proper clone was not returned, or the equals method is broken.");
		}
	}

	@Test
	public void testEquals() 
	{
		boolean shouldBeEqual = this.testSubject1.equals(this.testSubject2);
		boolean shouldNotBeEqual = this.testSubject1.equals(this.testSubject3);
		
		if(shouldBeEqual == false)
		{
			int equalityCount = this.testSubject1.howEqual(this.testSubject2);
			System.err.println("Equality count = ");
			System.err.println(equalityCount);
			fail("The method found equal TinPoints to be unequal.");
		}
		
		if(shouldNotBeEqual == true)
		{
			fail("The method found unequal TinPoints to be equal.");
		}
	}

	@Test
	public void testHowEqual() 
	{
		int equalityCount1 = this.testSubject1.howEqual(this.testSubject2);
		int equalityCount2 = this.testSubject1.howEqual(this.testSubject4);
		int equalityCount3 = this.testSubject1.howEqual(this.testSubject5);
		
		if(equalityCount1 != 3)
		{
			System.err.println("Equality count 1 = ");
			System.err.println(equalityCount1);
			fail("Proper equality comparison was not performed.");
		}
		
		if(equalityCount2 != 0)
		{
			System.err.println("Equality count 2 = ");
			System.err.println(equalityCount2);
			fail("Proper equality comparison was not performed.");
		}
		
		if(equalityCount3 != 1)
		{
			System.err.println("Equality count 3 = ");
			System.err.println(equalityCount2);
			fail("Proper equality comparison was not performed.");
		}
	}

	@Test
	public void testHasSameHorizontalPosition() 
	{
		boolean shouldBeTrue = this.testSubject1.hasSameHorizontalPosition(this.testSubject2, 0.10);
		boolean shouldNotBeTrue = this.testSubject1.hasSameHorizontalPosition(this.testSubject3, 0.10);
		
		if(shouldBeTrue == false)
		{
			fail("The method did not properly detect two TinPoints in the same horizontal position.");
		}
		
		if(shouldNotBeTrue == true)
		{
			fail("The method found two TinPoints in the same horizontal position when it shouldn't have.");
		}
	}

	@Test
	public void testHasSameElevation() 
	{
		boolean shouldBeTrue = this.testSubject1.hasSameElevation(this.testSubject2, 0.10);
		boolean shouldNotBeTrue = this.testSubject1.hasSameElevation(this.testSubject3, 0.10);
		
		if(shouldBeTrue == false)
		{
			fail("The method did not properly detect two TinPoints with the same elevation.");
		}
		
		if(shouldNotBeTrue == true)
		{
			System.err.println("Elevation 1 = ");
			System.err.println(this.testSubject1.getElevation());
			System.err.println("Elevation 2 = ");
			System.err.println(this.testSubject3.getElevation());
			
			fail("The method found two TinPoints at the same elevation when it shouldn't have.");
		}
	}

	@Test
	public void testToString() 
	{
		String result = this.testSubject1.toString();
		String expectedResult = "[TIN Point = Northing: 1650.0 |  Easting: 3700.0 |  Elevation: 3700.0 |  Category: Ground |  Description: West Top of Bank]";
		
		if(result.equals(expectedResult) == false)
		{
			System.err.println("toString() result: ");
			System.err.println(result);
			fail("A proper string was not returned by the method.");
		}
	}

}
