package test.net.surveyos.sourceforge.gpx2;

import java.util.*;

import static org.junit.Assert.*;

import org.geotools.gpx2.gpxentities.BasicTrackSegment;
import org.geotools.gpx2.gpxentities.BasicWaypoint;
import org.geotools.gpx2.gpxentities.SimpleWaypoint;
import org.junit.Test;

public class BasicTrackSegmentTest 
{

	/**
	 * This method tests the BasicTrackSegment constructor and the
	 * getTrackPoints method. Failure in either of these methods will
	 * cause the test to fail.
	 */
	@Test
	public void testBasicTrackSegmentConstructor() 
	{
		LinkedList<SimpleWaypoint> waypoints = new LinkedList<SimpleWaypoint>();
		
		BasicWaypoint waypoint1 = BasicWaypoint
		.getBasicWaypoint(52.326, -120.522);
		waypoints.add(waypoint1);
		
		BasicWaypoint waypoint2 = BasicWaypoint
		.getBasicWaypoint(54.856, -120.876);
		waypoints.add(waypoint2);
		
		BasicWaypoint waypoint3 = BasicWaypoint
		.getBasicWaypoint(49.325, -120.874);
		waypoints.add(waypoint3);
		
		BasicWaypoint waypoint4 = BasicWaypoint
		.getBasicWaypoint(49.218, -121.253);
		waypoints.add(waypoint4);
		
		BasicTrackSegment segment = new BasicTrackSegment(waypoints);
		
		List<SimpleWaypoint> returnedWaypoints = segment.getTrackPoints();
		
		SimpleWaypoint returnedWaypoint1 = returnedWaypoints.get(0);
		double firstLat = returnedWaypoint1.getLatitude();
		
		if(firstLat != 52.326)
		{
			fail("The BasicTrackSegment constructor failed: " +
					"The latitude of the first track point returned from the " +
					"BasicTrackSegment did not have the correct value.");
		}
		
		SimpleWaypoint returnedWaypoint3 = returnedWaypoints.get(2);
		double thirdLat = returnedWaypoint3.getLatitude();
		
		if(thirdLat != 49.325)
		{
			fail("The BasicTrackSegment constructor failed: " +
					"The latitude of the third track point returned from the " +
					"BasicTrackSegment did not have the correct value.");
		}
	}

	@Test
	public void testGetNumberOfTrackPoints()
	{
LinkedList<SimpleWaypoint> waypoints = new LinkedList<SimpleWaypoint>();
		
		BasicWaypoint waypoint1 = BasicWaypoint
		.getBasicWaypoint(52.326, -120.522);
		waypoints.add(waypoint1);
		
		BasicWaypoint waypoint2 = BasicWaypoint
		.getBasicWaypoint(54.856, -120.876);
		waypoints.add(waypoint2);
		
		BasicWaypoint waypoint3 = BasicWaypoint
		.getBasicWaypoint(49.325, -120.874);
		waypoints.add(waypoint3);
		
		BasicWaypoint waypoint4 = BasicWaypoint
		.getBasicWaypoint(49.218, -121.253);
		waypoints.add(waypoint4);
		
		BasicTrackSegment segment = new BasicTrackSegment(waypoints);
		
		if(segment.getNumberOfTrackPoints() != 4)
		{
			fail("getNumberOfTrackPoints method failed: The method did not" +
					"return the correct number of track points that were" +
					"contained in the BasicTrackSegment.");
		}
	}

}
