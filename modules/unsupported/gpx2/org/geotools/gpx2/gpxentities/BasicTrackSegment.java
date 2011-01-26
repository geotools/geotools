/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: May 19, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package org.geotools.gpx2.gpxentities;

import java.util.*;

/**
 * Provides a basic implementation of the TrackSegment interface.
 *
 * @source $URL$
 */
public class BasicTrackSegment implements TrackSegment 
{

	private LinkedList<SimpleWaypoint> waypoints = new 
	LinkedList<SimpleWaypoint>();
	
	/**
	 * Constructs a BasicTrackSegment using the provided list of 
	 * SimpleWaypoints representing the "track points" contained in
	 * in the TrackSegment.
	 */
	public BasicTrackSegment(List<SimpleWaypoint> argWaypoints)
	{
		this.waypoints.addAll(argWaypoints);
	}
	
	@Override
	public List<SimpleWaypoint> getTrackPoints() 
	{
		return this.waypoints;
	}

	@Override
	public int getNumberOfTrackPoints()
	{
		return this.waypoints.size();
	}
}
