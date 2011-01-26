package test.net.surveyos.sourceforge.gpx2;

import static org.junit.Assert.*;

import org.geotools.gpx2.gpxentities.BasicWaypoint;
import org.joda.time.DateTime;
import org.junit.Test;

public class BasicWaypointTest 
{

	@Test
	public void testGetBasicWaypointDoubleDouble() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123);
		
		if(testSubject1.getLatitude() != 84.566)
		{
			fail("The BasicWaypoint object was constructed with" +
					"an incorrect latitude value.");
		}
		
		if(testSubject1.getLongitude() != 120.123)
		{
			fail("The BasicWaypoint object was constructed with" +
					" an incorrect longitude value.");
		}
		
		// Pass illegal latitude and longitude values and see
		// if an IllegalArgumentException is thrown.
		boolean exceptionWasThrown = false;
		
		try
		{
			BasicWaypoint testSubject2 = BasicWaypoint
			.getBasicWaypoint(-500.223, 8090.123);
		}
		
		catch(IllegalArgumentException caught)
		{
			exceptionWasThrown = true;
		}
		
		if(exceptionWasThrown == false)
		{
			fail("The constructor should have caught " +
					"an IllegalArgumentException");
		}
	}

	@Test
	public void testGetBasicWaypointDoubleDoubleString() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		if(testSubject1.getLatitude() != 84.566)
		{
			fail("The BasicWaypoint object was constructed with" +
					"an incorrect latitude value.");
		}
		
		if(testSubject1.getLongitude() != 120.123)
		{
			fail("The BasicWaypoint object was constructed with" +
					" an incorrect longitude value.");
		}
		
		String name = testSubject1.getName();
		
		if(name.equals("Waypoint 001") == false)
		{
			fail("The BasicWaypoint object was constructed with the" +
					" wrong name.");
		}
		
		// Pass illegal latitude and longitude values and see
		// if an IllegalArgumentException is thrown.
		boolean exceptionWasThrown = false;
		
		try
		{
			BasicWaypoint testSubject2 = BasicWaypoint
			.getBasicWaypoint(-500.223, 8090.123);
		}
		
		catch(IllegalArgumentException caught)
		{
			exceptionWasThrown = true;
		}
		
		if(exceptionWasThrown == false)
		{
			fail("The constructor should have thrown " +
					"an IllegalArgumentException");
		}
	}

	@Test
	public void testGetLatitude() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		if(testSubject1.getLatitude() != 84.566)
		{
			fail("The getLatitude method failed.");
		}
	}

	@Test
	public void testSetLatitude() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		testSubject1.setLatitude(45.255698);
		
		if(testSubject1.getLatitude() != 45.255698)
		{
			fail("The setLatitude method failed.");
		}
		
		boolean exceptionWasThrown = false;
		
		try
		{
			testSubject1.setLatitude(-8556.253);
		}
		
		catch(IllegalArgumentException caught)
		{
			exceptionWasThrown = true;
		}
		
		if(exceptionWasThrown == false)
		{
			fail("The setLatitude method should have thrown " +
					"an IllegalArgumentException");
		}
	}

	@Test
	public void testGetLongitude() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		if(testSubject1.getLongitude() != 120.123)
		{
			fail("The getLongitude method failed.");
		}
	}

	@Test
	public void testSetLongitude()
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		testSubject1.setLongitude(165.255698);
		
		if(testSubject1.getLongitude() != 165.255698)
		{
			fail("The setLongitude method failed.");
		}
		
		boolean exceptionWasThrown = false;
		
		try
		{
			testSubject1.setLatitude(-8556.253);
		}
		
		catch(IllegalArgumentException caught)
		{
			exceptionWasThrown = true;
		}
		
		if(exceptionWasThrown == false)
		{
			fail("The setLongitude method should have thrown " +
					"an IllegalArgumentException");
		}
	}

	@Test
	public void testGetElevation() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		testSubject1.setElevation(-10.526);
		
		if(testSubject1.getElevation() != -10.526)
		{
			fail("The setElevation method failed. The elevation" +
					" values did not match.");
		}
	}

	@Test
	public void testSetElevation() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		testSubject1.setElevation(-10.526);
		
		if(testSubject1.getElevation() != -10.526)
		{
			fail("The setElevation method failed.");
		}
		
		if(testSubject1.hasElevation() == false)
		{
			fail("The setElevation method failed. The hasElevation" +
					" method did not return true.");
		}
	}

	@Test
	public void testGetTime()
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		DateTime now = new DateTime();
		testSubject1.setTime(now);
		
		DateTime timeReturned = testSubject1.getTime();
		
		if(timeReturned.equals(now) != true)
		{
			fail("The getTime method failed. The date and time" +
					" values did not match.");
		}
	}

	@Test
	public void testSetTime() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		DateTime now = new DateTime();
		testSubject1.setTime(now);
		
		DateTime timeReturned = testSubject1.getTime();
		
		if(timeReturned.equals(now) != true)
		{
			fail("The getTime method failed. The date and time" +
					" values did not match.");
		}
		
		if(testSubject1.hasDateAndTimeCollected() != true)
		{
			fail("The getTime method failed. The hasDateAndTimeCollected" +
					" method did not return true.");
		}
	}

	@Test
	public void testGetName() 
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		String name = testSubject1.getName();
		
		if(name.equals("Waypoint 001") != true)
		{
			fail("The getName method failed. The name values did not match.");
		}
	}

	@Test
	public void testSetName()
	{
		BasicWaypoint testSubject1 = BasicWaypoint
		.getBasicWaypoint(84.566, 120.123, "Waypoint 001");
		
		String name = testSubject1.getName();
		
		if(name.equals("Waypoint 001") != true)
		{
			fail("The getName method failed. The name values did not match.");
		}
		
		if(testSubject1.hasName() != true)
		{
			fail("The setName method failed. The hasName" +
					" method did not return true.");
		}
	}
}
