/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: Jun 16, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package org.geotools.gpx2.utils;

/**
 * Provides a set of utility methods for working with GPX data and GPS objects
 * that can be accessed from a static context. This includes methods for 
 * working with latitude and longitude values represented as doubles in 
 * decimal degrees format.
 *
 *
 * @source $URL$
 */
public final class GpxUtils 
{
	/**
	 * Indicates if the double value passed as an argument to this method is
	 * a valid latitude in decimal degrees format. To be a valid latitude 
	 * the double value must be greater than or equal to -90 and lesser than
	 * or equal to +90.
	 */
	public static final boolean isValidLatitudeValue(Double argValue)
	{
		boolean toReturn = false;
			
		if(argValue < 90.0)
		{
			if(argValue > -90.0)
			{
				toReturn = true;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Indicates if the double value passed as an argument to this method is
	 * a valid longitude in decimal degrees format. To be a valid longitude
	 * the double value must be greater than or equal to -180 and lesser than
	 * or equal to +180.
	 */
	public static final boolean isValidLongitudeValue(Double argValue)
	{
		boolean toReturn = false;
		
		if(argValue < 180.0)
		{
			if(argValue > -180.0)
			{
				toReturn = true;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Indicates if the latitude value passed as an argument to the method
	 * is in the Northern Hemisphere.
	 * <br><br>
	 * Note: If the latitude value is on the equator this method
	 * will return a value of false.
	 * 
	 * @param argValue The latitude of being tested in decimal degrees in
	 * decimal degrees format.
	 */
	public static final boolean isNorthLatitude(Double argValue)
	{
		if(GpxUtils.isValidLatitudeValue(argValue) == true)
		{
			if(argValue > 0.0)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The latitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
	
	/**
	 * Indicates if the latitude value passed as an argument to the method
	 * is in the Southern Hemisphere.
	 * <br><br>
	 * Note: If the latitude value is on the equator this method
	 * will return a value of false.
	 * 
	 * @param argValue The latitude of being tested in decimal degrees in
	 * decimal degrees format.
	 */
	public static final boolean isSouthLatitude(Double argValue)
	{
		if(GpxUtils.isValidLatitudeValue(argValue) == true)
		{
			if(argValue < 0.0)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The latitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
	
	/**
	 * Indicates if the longitude value passed as an argument to the method
	 * is in the Western Hemisphere.
	 * <br><br>
	 * Note: If the longitude value is on the prime meridian this method
	 * will return a value of false.
	 * 
	 * @param argValue The longitude of being tested in decimal degrees in
	 * decimal degrees format.
	 */
	public static final boolean isWestLongitude(Double argValue)
	{
		if(GpxUtils.isValidLongitudeValue(argValue) == true)
		{
			if(argValue < 0.0)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The longitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
	
	/**
	 * Indicates if the longitude value passed as an argument to the method
	 * is in the Southern Hemisphere.
	 * <br><br>
	 * Note: If the longitude value is on the prime meridian this method
	 * will return a value of false.
	 * 
	 * @param argValue The longitude of being tested in decimal degrees in
	 * decimal degrees format.
	 */
	public static final boolean isEastLongitude(Double argValue)
	{
		if(GpxUtils.isValidLongitudeValue(argValue) == true)
		{
			if(argValue > 0.0)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The longitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
	
	/**
	 * Indicates if the latitude value provided as an argument is on the 
	 * equator. A latitude value is considered to be on the equator if
	 * the difference between it and a latitude of 0 is within the
	 * tolerance passed to the method as an argument.
	 * 
	 * @param argValue - The latitude value being tested in decimal degrees
	 * format.
	 * @param argTolerance - The error tolerance for the test in decimal 
	 * degrees format.
	 * @return A boolean value indicating if the latitude is on the equator.
	 */
	public static final boolean isOnEquater(Double argValue, Double argTolerance)
	{
		if(GpxUtils.isValidLatitudeValue(argValue) == true)
		{
			if((argValue - 0.0) < argTolerance)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The latitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
	
	/**
	 * Indicates if the longitude value provided as an argument is on the 
	 * prime meridian. A latitude value is considered to be on the prime 
	 * meridian if the difference between it and a longitude of 0 is within 
	 * the tolerance passed to the method as an argument.
	 * 
	 * @param argValue - The longitude value being tested in decimal degrees
	 * format.
	 * @param argTolerance - The error tolerance for the test in decimal 
	 * degrees format.
	 * @return A boolean value indicating if the longitude is on the 
	 * prime meridian.
	 */
	public static final boolean isOnPrimeMeridian(Double argValue, 
			Double argTolerance)
	{
		if(GpxUtils.isValidLongitudeValue(argValue) == true)
		{
			if((argValue - 0.0) < argTolerance)
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		
		else
		{
			IllegalArgumentException toThrow = new IllegalArgumentException
			(" The longitude value being tested was not valid.");
			
			throw toThrow;
		}
	}
}
