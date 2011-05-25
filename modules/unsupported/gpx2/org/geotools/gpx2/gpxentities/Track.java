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
 * Represents a GPX track. A track is made up of a TrackSegments. Each 
 * TrackSegment in a Track object contains SimpleWaypoints that represent the
 * "track points" in the Track.
 * 
 * @see TrackSegment
 * @see SimpleWaypoint
 * @see BasicTrack
 * @see BasicTrackSegment
 *
 *
 * @source $URL$
 */
public interface Track 
{
	/**
	 * Returns the name of this TrackSegment.
	 */
	public abstract String getName();
	
	/**
	 * Sets the name of the TrackSegment.
	 */
	public abstract void setName(String argName);

	/**
	 * Adds a TrackSegment to this Track.
	 */
	public abstract void addTrackSegment(TrackSegment argTrackSegment);
	
	/**
	 * Clears all of the TrackSegments within this Track.
	 */
	public abstract void clearTrackSegments();
	
	/**
	 * Returns the TrackSegments contained in this Track.
	 */
	public abstract List<TrackSegment> getTrackSegments();
	
	/**
	 * Returns the number of TrackSegments contained in this Track.
	 */
	public abstract int getNumberOfSegments();
}
