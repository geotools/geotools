/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: May 16, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package org.geotools.gpx2.gpxentities;

import java.util.*;

/**
 * Represents a TrackSegment. A TrackSegment is part of a Track. It contains 
 * two (2) or more "track points", which are represented by SimpleWaypoints.
 *
 *
 * @source $URL$
 */
public interface TrackSegment 
{
	/**
	 * Returns a list of the SimpleWaypoint objects that are the "track points"
	 * for this TrackSegment.
	 */
	public abstract List<SimpleWaypoint> getTrackPoints();
	
	/**
	 * Returns the number of "track points" stored in this TrackSegment.
	 */
	public abstract int getNumberOfTrackPoints();
}
