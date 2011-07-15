/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.properties.time;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class TimeParserTest extends Assert {

	@Test
	public void testParserOnCurrentTime() throws ParseException, InterruptedException {
		TimeParser parser = new TimeParser();
		long now = System.currentTimeMillis();
		Thread.sleep(20);
		final String timeInstant = "current";
		List<Date> time = parser.parse(timeInstant);
		assertEquals(1, time.size());
		assertTrue(now < time.get(0).getTime());
	}
	
	@Test
	public void testParserOnNullTime() throws ParseException{
		TimeParser parser = new TimeParser();
		List<Date> time = parser.parse(null);
		assertTrue(time.isEmpty());
	}
	
	@Test
	public void testParserOnTimeInstant() throws ParseException {
		TimeParser parser = new TimeParser();
		final String timeInstant = "2011-10-10T10:11:12.000Z";
		List<Date> time = parser.parse(timeInstant);
		assertEquals(1, time.size());
		assertEquals(1318241472000l, time.get(0).getTime());
	}

	@Test
	public void testParserOnTimePeriod() throws ParseException {
		TimeParser parser = new TimeParser();
		final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-10T14:11:12.000Z/PT1H";
		List<Date> time = parser.parse(timeInterval);
		assertEquals(5, time.size());
		assertEquals(1318241472000l, time.get(0).getTime());
		assertEquals(1318241472000l + (3600 * 1000 * 4), time.get(time.size()-1).getTime());
	}
	
	@Test
	public void testParserOnDayPeriod() throws ParseException {
		TimeParser parser = new TimeParser();
		final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-14T10:11:12.000Z/P2D";
		List<Date> time = parser.parse(timeInterval);
		assertEquals(3, time.size());
		assertEquals(1318241472000l, time.get(0).getTime());
		assertEquals(1318241472000l + (3600 * 1000 * 48), time.get(1).getTime());
	}

}
