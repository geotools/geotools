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
 * Provides the default implementation of the Track interface.
 *
 * @source $URL$
 */
public class BasicTrack implements Track
{
	private String name;
	private LinkedList<TrackSegment> segments;
	private boolean hasName;

	/**
	 * Constructs a BasicTract from the list of TrackSegments provided as an
	 * argument.
	 */
	public BasicTrack(List<TrackSegment> argTrackSegments)
	{
		this.segments = new LinkedList<TrackSegment>();
		this.segments.addAll(argTrackSegments);
	}
	
	/**
	 * Constructs a BasicTrack from the list of TrackSegments provided as the
	 * first argument and with the name provided as the second argument.
	 */
	public BasicTrack(List<TrackSegment> argTrackSegments, String argName)
	{
		this.segments = new LinkedList<TrackSegment>();
		this.segments.addAll(argTrackSegments);
		this.setName(argName);
		this.hasName = true;
	}
	
	/**
	 * Constructs a BasicTrack with an empty list of TrackSegments.
	 */
	public BasicTrack()
	{
		this.segments = new LinkedList<TrackSegment>();
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public void setName(String argName) 
	{
		this.name = argName;
		this.hasName = true;
	}
		
	/**
	 * Indicates if this BasicTrack has a valid name value.
	 */
	public boolean hasName()
	{
		return this.hasName;
	}

	@Override
	public int getNumberOfSegments() 
	{
		return this.segments.size();
	}

	@Override
	public List<TrackSegment> getTrackSegments() 
	{
		return this.segments;
	}

	@Override
	public void addTrackSegment(TrackSegment argTrackSegment)
	{
		this.segments.add(argTrackSegment);
		
	}

	@Override
	public void clearTrackSegments() 
	{
		this.segments.clear();		
	}
}
