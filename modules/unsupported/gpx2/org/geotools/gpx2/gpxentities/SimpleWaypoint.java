/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: May 14, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package org.geotools.gpx2.gpxentities;

import org.joda.time.*;

public interface SimpleWaypoint
{
	/**
	 * Returns the latitude of this SimpleWaypoint object. The latitude is in
	 * decimal degrees format.
	 */
	public abstract double getLatitude();
		
	/**
	 * Sets the latitude of this SimpleWaypoint object. The double value 
	 * passed to this method should represent a valid latitude in decimal
	 * degrees format.
	 * 
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLatitude(double argLatitude);
		
	/**
	 * Returns the longitude of this SimpleWaypoint object. The longitude is in
	 * decimal degrees format.
	 */
	public abstract double getLongitude();
		
	/**
	 * Sets the longitude of this SimpleWaypoint object. The double value 
	 * passed to this method should represent a valid longitude in decimal
	 * degrees format.
	 * 
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLongitude(double argLongitude);
		
	/**
	 * Returns the elevation of this SimpleWaypoint object. The elevation 
	 * returned is usually in feet. However, this may change depending on the
	 * implementation of this interface.
	 */
	public abstract double getElevation();
		
	/**
	 * Sets the elevation of this SimpleWaypoint object. This value is 
	 * typically in feet, but this may vary depending on the implementation
	 * of this interface. Negative elevation values are allowed and would
	 * represent elevations below sea level or some other datum.

	 */
	public abstract void setElevation(double argElevation);
		
	/**
	 * Returns the DateTime object that represents the date and time this
	 * this SimpleWaypoint object was collected. The calendar (time unit system)
	 * that this DateTime is referenced to will very depending on the
	 * implementation of this interface.
	 */
	public abstract DateTime getTime();
		
	/**
	 * Sets the DateTime object representing the date and time this 
	 * SimpleWaypoint was collected.
	 */
	public abstract void setTime(DateTime argTime);
		
	/**
	 * Sets the name of this Waypoint. This name is not guaranteed to be
	 * unique.
	 */
	public abstract void setName(String argName);
		
	/**
	 * Returns the name of this Waypoint as a String.
	 */
	public abstract String getName();
		
	/**
	 * Indicates if this SimpleWaypoint object has a name.
	 */
	public abstract boolean hasName();
		
	/**
	 * Indicates if this SimpleWaypoint object has an elevation.
	 */
	public abstract boolean hasElevation();
		
	/**
	 * Indicates if this SimpleWaypoint has a DateTime object that reprensets
	 * the data and time it was collected.
	 */
	public abstract boolean hasDateAndTimeCollected();
}
