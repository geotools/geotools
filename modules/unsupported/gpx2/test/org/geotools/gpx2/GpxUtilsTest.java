/*
 * Project Name: GeoTools GPX Support
 * Original Organization Name: The SurveyOs Project
 * Original Programmer Name: The Sunburned Surveyor
 * Current Maintainer Name: The SurveyOS Project
 * Current Maintainer Contact Information
 *    E-Mail Address: The Sunburned Surveyor
 * Copyright Holder: The SurveyOS Project
 * Date Last Modified: Jun 17, 2008
 * Current Version Number: 00.00.01
 * IDE Name: Eclipse
 * IDE Version: 3.2.1
 * Type: Java Class
 */
package test.net.surveyos.sourceforge.gpx2;

import static org.junit.Assert.*;

import org.geotools.gpx2.utils.GpxUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GpxUtilsTest
{

	@Before
	public void setUp() throws Exception 
	{
		// Stub
	}

	@After
	public void tearDown() throws Exception
	{
		// Stub
	}

	@Test
	public void testIsValidLatitudeValue() 
	{
		boolean result1 = GpxUtils.isValidLatitudeValue(-92.362);
		boolean result2 = GpxUtils.isValidLatitudeValue(98.236);
		boolean result3 = GpxUtils.isValidLatitudeValue(42.6522);
		boolean result4 = GpxUtils.isValidLatitudeValue(-42.6522);
		
		if(-92.362 < -90.0)
		{
			System.out.println("-92.362 is less than -90.");
		}
		
		if(result1 == true)
		{		
			fail("Method failed to detect latitude value less than -90" +
					" degrees.");
		}
		
		if(result2 == true)
		{
			fail("Method failed to detect latitude value greater than 90" +
					" degrees.");
		}
		
		if(result3 == false)
		{
			fail("Method marked a positive latitude value as invalid when" +
					"it was valid.");
		}
		
		if(result4 == false)
		{
			fail("Method marked a negative latitude value as invalid when" +
					"it was valid.");
		}
	}

	@Test
	public void testIsValidLongitudeValue() 
	{
		boolean result1 = GpxUtils.isValidLongitudeValue(-182.362);
		boolean result2 = GpxUtils.isValidLongitudeValue(192.236);
		boolean result3 = GpxUtils.isValidLongitudeValue(42.6522);
		boolean result4 = GpxUtils.isValidLongitudeValue(-42.6522);
		
		if(result1 == true)
		{		
			fail("Method failed to detect longitude value less than 180" +
					" degrees.");
		}
		
		if(result2 == true)
		{
			fail("Method failed to detect longitude value greater than 180" +
					" degrees.");
		}
		
		if(result3 == false)
		{
			fail("Method marked a positive longitude value as invalid when" +
					"it was valid.");
		}
		
		if(result4 == false)
		{
			fail("Method marked a negative llongitude value as invalid when" +
					"it was valid.");
		}
	}

	@Test
	public void testIsNorthLatitude() 
	{
		boolean result1 = GpxUtils.isNorthLatitude(52.3662);
		boolean result2 = GpxUtils.isNorthLatitude(-45.3662);
		
		if(result1 == false)
		{
			fail("Method did not properly recognize a latitide in the " +
					"northern hemisphere.");
		}
		
		if(result2 == true)
		{
			fail("Method recognized a latitide in the " +
					"southern hemisphere as being in the northern " +
					"hemisphere.");
		}
	}

	@Test
	public void testIsSouthLatitude() 
	{
		boolean result1 = GpxUtils.isSouthLatitude(52.3662);
		boolean result2 = GpxUtils.isSouthLatitude(-45.3662);
		
		if(result1 == true)
		{
			fail("Method did not properly recognize a latitide in the " +
					"southern hemisphere.");
		}
		
		if(result2 == false)
		{
			fail("Method recognized a latitide in the " +
					"northern hemisphere as being in the southern " +
					"hemisphere.");
		}
	}

	@Test
	public void testIsWestLongitude() 
	{
		boolean result1 = GpxUtils.isWestLongitude(52.3662);
		boolean result2 = GpxUtils.isWestLongitude(-45.3662);
		
		if(result1 == true)
		{
			fail("Method recognized a longitude in the " +
					"eastern hemisphere as being in the western " +
					"hemisphere.");
		}
		
		if(result2 == false)
		{
			fail("Method failed to recognize a longitude in the " +
					"western hemisphere.");
		}
	}

	@Test
	public void testIsEastLongitude() 
	{
		boolean result1 = GpxUtils.isEastLongitude(52.3662);
		boolean result2 = GpxUtils.isEastLongitude(-45.3662);
		
		if(result1 == false)
		{
			fail("Method failed to recognize a longitude in the " +
			"eastern hemisphere.");
		}
		
		if(result2 == true)
		{
			fail("Method recognized a longitude in the " +
					"western hemisphere as being in the eastern " +
					"hemisphere.");
		}
	}

	@Test
	public void testIsOnEquater() 
	{
		boolean result1 = GpxUtils.isOnEquater(0.255, 0.30);
		boolean result2 = GpxUtils.isOnEquater(0.012, 0.008);
		
		if(result1 == false)
		{
			fail("Method failed to recognize a latitude on the equator.");
		}
		
		if(result2 == true)
		{
			fail("Method recognized a latitude not on the equator as" +
					"being on the equator.");
		}
	}
	@Test
	public void testIsOnPrimeMeridian() 
	{
		boolean result1 = GpxUtils.isOnPrimeMeridian(0.255, 0.30);
		boolean result2 = GpxUtils.isOnPrimeMeridian(0.012, 0.008);
		
		if(result1 == false)
		{
			fail("Method failed to recognize a longitude on the prime " +
					"meridian.");
		}
		
		if(result2 == true)
		{
			fail("Method recognized a longitude not on the primae meridian as" +
					"being on the prime meridian.");
		}
	}

}
