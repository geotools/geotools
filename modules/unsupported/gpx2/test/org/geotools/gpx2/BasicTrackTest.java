package test.net.surveyos.sourceforge.gpx2;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.geotools.gpx2.gpxentities.BasicTrack;
import org.geotools.gpx2.gpxentities.BasicTrackSegment;
import org.geotools.gpx2.gpxentities.BasicWaypoint;
import org.geotools.gpx2.gpxentities.SimpleWaypoint;
import org.geotools.gpx2.gpxentities.TrackSegment;
import org.junit.Test;

public class BasicTrackTest 
{

	@Test
	public void testBasicTrackFromList()
	{
		TrackSegment segment = this.getBasicTrackSegmentForTest(1);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment);
		
		BasicTrack testSubject = new BasicTrack(segments);
		
		List<TrackSegment> returnedSegments = testSubject
		.getTrackSegments();
		
		TrackSegment firstSegment = returnedSegments.get(0);
		
		if(segment.equals(firstSegment) != true)
		{
			fail("The first track segment returned from the BasicTrack" +
					" did not match the track segment used to create the" +
					"BasicTrack object.");
		}
	}

	@Test
	public void testBasicTrackFromListAndString()
	{
		TrackSegment segment = this.getBasicTrackSegmentForTest(1);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment);
		
		BasicTrack testSubject = new BasicTrack(segments, "Test Track");
		
		List<TrackSegment> returnedSegments = testSubject
		.getTrackSegments();
		
		TrackSegment firstSegment = returnedSegments.get(0);
		
		if(segment.equals(firstSegment) != true)
		{
			fail("The first track segment returned from the BasicTrack" +
					" did not match the track segment used to create the" +
					"BasicTrack object.");
		}
		
		String name = testSubject.getName();
		
		if(name.equals("Test Track") != true)
		{
			fail("The name obtained from the BasicTrack did not match the" +
					" name that the BasicTrack was created with.");
		}
	}

	// Tests both the getName and setName methods.
	@Test
	public void testGetAndSetName() 
	{
		TrackSegment segment = this.getBasicTrackSegmentForTest(1);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment);
		
		BasicTrack testSubject = new BasicTrack(segments);
		
		testSubject.setName("Test Track");
		String name = testSubject.getName();
		
		if(name.equals("Test Track") != true)
		{
			fail("The name obtained from the BasicTrack did not match the" +
					" name that was set.");
		}
	}

	@Test
	public void testHasName() 
	{
		TrackSegment segment = this.getBasicTrackSegmentForTest(1);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment);
		
		BasicTrack testSubject = new BasicTrack(segments);
		
		testSubject.setName("Test Track");
		
		if(testSubject.hasName() != true)
		{
			fail("The hasName method did not return true after the name" +
					" had been set.");
		}
	}

	@Test
	public void testGetNumberOfSegments() 
	{
		TrackSegment segment1 = this.getBasicTrackSegmentForTest(1);
		TrackSegment segment2 = this.getBasicTrackSegmentForTest(2);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment1);
		segments.add(segment2);
		
		BasicTrack testSubject = new BasicTrack(segments, "Test Track");
		
		int numberOfSegments = testSubject.getNumberOfSegments();
		
		if(numberOfSegments != 2)
		{
			fail("The correct number of segments was not returned.");
		}
	}

	@Test
	public void testGetTrackSegments() 
	{
		TrackSegment segment1 = this.getBasicTrackSegmentForTest(1);
		TrackSegment segment2 = this.getBasicTrackSegmentForTest(2);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment1);
		segments.add(segment2);
		
		BasicTrack testSubject = new BasicTrack(segments, "Test Track");
		List<TrackSegment> returnedSegments = testSubject.getTrackSegments();
		
		TrackSegment returnedSegment1 = returnedSegments.get(0);
		TrackSegment returnedSegment2 = returnedSegments.get(1);
		
		if(returnedSegment1.equals(segment1) != true)
		{
			fail("The first segment did not match.");
		}
		
		if(returnedSegment2.equals(segment2) != true)
		{
			fail("The second segment did not match.");
		}
	}

	@Test
	public void testAddTrackSegment() 
	{
		TrackSegment segment1 = this.getBasicTrackSegmentForTest(1);
		TrackSegment segment2 = this.getBasicTrackSegmentForTest(2);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment1);
		
		BasicTrack testSubject = new BasicTrack(segments, "Test Track");
		testSubject.addTrackSegment(segment2);
		
		List<TrackSegment> returnedSegments = testSubject.getTrackSegments();
		
		TrackSegment returnedSegment1 = returnedSegments.get(0);
		
		if(returnedSegment1.equals(segment1) != true)
		{
			fail("The first segment did not match.");
		}
	}

	@Test
	public void testClearTrackSegments()
	{
		TrackSegment segment1 = this.getBasicTrackSegmentForTest(1);
		TrackSegment segment2 = this.getBasicTrackSegmentForTest(2);
		LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
		segments.add(segment1);
		segments.add(segment2);
		
		BasicTrack testSubject = new BasicTrack(segments, "Test Track");
		testSubject.clearTrackSegments();
		
		List<TrackSegment> returnedSegments = testSubject.getTrackSegments();
		
		if(returnedSegments.size() != 0)
		{
			fail("The track segments were not cleared.");
		}
	}
	
	private BasicTrackSegment getBasicTrackSegmentForTest(int argSegmentId)
	{
		LinkedList<SimpleWaypoint> waypoints = new LinkedList<SimpleWaypoint>();
		
		if(argSegmentId == 1)
		{
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
		
			return new BasicTrackSegment(waypoints);
		}

		if(argSegmentId == 2)
		{
			BasicWaypoint waypoint1 = BasicWaypoint
			.getBasicWaypoint(35.845, -11.565);
			waypoints.add(waypoint1);
		
			BasicWaypoint waypoint2 = BasicWaypoint
			.getBasicWaypoint(36.002, -12.888);
			waypoints.add(waypoint2);
		
			BasicWaypoint waypoint3 = BasicWaypoint
			.getBasicWaypoint(36.552, -11.652);
			waypoints.add(waypoint3);
		
			BasicWaypoint waypoint4 = BasicWaypoint
			.getBasicWaypoint(36.844, -12.005);
			waypoints.add(waypoint4);
		
			return new BasicTrackSegment(waypoints);
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			("The int passed to the helper method must have a value " +
					"of 1 or 2.");
			
			throw toThrow;
		}
	}
}
