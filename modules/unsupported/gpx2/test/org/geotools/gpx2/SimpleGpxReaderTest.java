/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: Jun 20, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package test.net.surveyos.sourceforge.gpx2;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.geotools.gpx2.gpxentities.*;
import org.geotools.gpx2.io.SimpleGpxReader;

public class SimpleGpxReaderTest 
{

	@Before
	public void setUp() throws Exception 
	{
		//Stub
	}

	@After
	public void tearDown() throws Exception 
	{
		//Stub
	}
	
	@Test
	public void testGetWaypoints() 
	{
		SimpleGpxReader reader = new SimpleGpxReader();
		
		File targetFile = new File("H:/My_Programming/Eclipse_Workspace/" +
				"GeoTools GPX Support (2008-11-11 Release)/src/test/net/" +
				"surveyos/sourceforge/gpx2/" +
				"resources/sample_gpx_file_01.gpx");
		
		reader.setUpForReading(targetFile);
		
		List<SimpleWaypoint> waypoints = reader.getWaypoints();
		
		if(waypoints.isEmpty() == true)
		{
			fail("No waypoints were parsed by the reader.");
		}
		
		SimpleWaypoint waypoint1 = waypoints.get(0);
		double lat = waypoint1.getLatitude();
		double lon = waypoint1.getLongitude();
		double ele = waypoint1.getElevation();
		String name = waypoint1.getName();
		
		if(lat != 37.95708231)
		{
			String latAsString = Double.toString(lat);
			fail("The latitude of the first waypoint was not parsed correctly." +
					"The value of the latitude provided by the reader was" +
					latAsString);
		}
		
		if(lon != -121.31492345)
		{
			String lonAsString = Double.toString(lon);
			fail("The longitude of the first waypoint was not parsed correctly." +
					"The value of the longitude provided by the reader was" +
					lonAsString);
		}
		
		if(ele != -3.960)
		{
			String eleAsString = Double.toString(ele);
			fail("The elevation of the first waypoint was not parsed correctly." +
					"The value of the elevation provided by the reader was" +
					eleAsString);
		}
		
		if( name.equals("040") == false)
		{
			fail("The name of the first waypoint was not parsed correctly." +
			"The value of the name provided by the reader was" +
			name);
		}
	}
	
	@Test
	public void testGetTracks()
	{
		SimpleGpxReader reader = new SimpleGpxReader();
		
		File targetFile = new File("H:/My_Programming/Eclipse_Workspace/" +
				"GeoTools GPX Support (2008-11-11 Release)/src/test/net/" +
				"surveyos/sourceforge/gpx2/" +
				"resources/sample_gpx_file_02.gpx");
		
		reader.setUpForReading(targetFile);
		
		List<Track> tracks = reader.getTracks();
		
		if(tracks.isEmpty() == true)
		{
			fail("No tracks were parsed by the reader.");
		}
		
		Track firstParsedTrack = tracks.get(0);
		List<TrackSegment> segments = firstParsedTrack.getTrackSegments();
		TrackSegment firstSegment = segments.get(0);
		
		List<SimpleWaypoint> waypoints = firstSegment.getTrackPoints();
		
		SimpleWaypoint waypoint1 = waypoints.get(0);
		double lat1 = waypoint1.getLatitude();
		double lon1 = waypoint1.getLongitude();
		double ele1 = waypoint1.getElevation();
		
		if(lat1 != 37.95711995)
		{
			String latAsString = Double.toString(lat1);
			fail("The latitude of the first track point was not " +
					"parsed correctly." +
					"The value of the latitude provided by the " +
					"reader was" +
					latAsString);
		}
		
		if(lon1 != -121.31200076)
		{
			String lonAsString = Double.toString(lon1);
			fail("The longitude of the first track point was not parsed " +
					"correctly." +
					"The value of the longitude provided by the reader was" +
					lonAsString);
		}
		
		if(ele1 != -7.553)
		{
			String eleAsString = Double.toString(ele1);
			fail("The elevation of the first track point was not parsed " +
					"correctly." +
					"The value of the elevation provided by the reader was" +
					eleAsString);
		}
		
		SimpleWaypoint waypoint2 = waypoints.get(0);
		double lat2 = waypoint1.getLatitude();
		double lon2 = waypoint1.getLongitude();
		double ele2 = waypoint1.getElevation();
		String name2 = waypoint1.getName();
		
		if(lat2 != 37.95711995)
		{
			String latAsString = Double.toString(lat2);
			fail("The latitude of the second track point was not" +
					" parsed correctly." +
					"The value of the latitude provided by the reader was" +
					latAsString);
		}
		
		if(lon2 != -121.31200076)
		{
			String lonAsString = Double.toString(lon2);
			fail("The longitude of the second track point was not " +
					"parsed correctly." +
					"The value of the longitude provided by the reader was" +
					lonAsString);
		}
		
		if(ele2 != -7.553)
		{
			String eleAsString = Double.toString(ele2);
			fail("The elevation of the second track point was not " +
					"parsed correctly." +
					"The value of the elevation provided by the reader was" +
					eleAsString);
		}
	}
}
