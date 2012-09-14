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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL$
 */
public class TimeParserTest extends Assert {
	
	private final static TimeParser PARSER = new TimeParser();
	
	@Test
	public void testParserOnCurrentTime() throws ParseException, InterruptedException {
		long now = System.currentTimeMillis();
		Thread.sleep(20);
		final String timeInstant = "current";
		List<Date> time = PARSER.parse(timeInstant);
		assertEquals(1, time.size());
		assertTrue(now < time.get(0).getTime());
	}
	
	@Test
	public void testParserOnNullTime() throws ParseException{
		List<Date> time = PARSER.parse(null);
		assertTrue(time.isEmpty());
	}
	
	@Test
        public void testParserOnTimeInstant1() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyy
                String timeInstant = "2011";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals(timeInstant+"-01-01T00:00:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant34() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyyMM
                String timeInstant = "201110";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-01T00:00:00.000Z",df.format(time.get(0)));
	}
        
	@Test
        public void testParserOnTimeInstant3() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyy-MM
	        String timeInstant = "2011-10";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-01T00:00:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant4() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyyMMdd
                String timeInstant = "20111010";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T00:00:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant5() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyy-MM-dd
                String timeInstant = "2011-10-10";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals(timeInstant+"T00:00:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant6() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyyMMdd'T'HH
                String timeInstant = "20111010T10";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:00:00.000Z",df.format(time.get(0)));
	}
        
	@Test
        public void testParserOnTimeInstant7() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyy-MM-dd'T'HH
                String timeInstant = "2011-10-10T10";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals(timeInstant+":00:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant8() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH'Z'
                String timeInstant = "20111010T10Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:00:00.000Z",df.format(time.get(0)));
                
	}
	
	@Test
        public void testParserOnTimeInstant9() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH'Z'
                String timeInstant = "2011-10-10T10Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:00:00.000Z",df.format(time.get(0)));
	}
        
	@Test
        public void testParserOnTimeInstant10() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyyMMdd'T'HHmm
                String timeInstant = "20111010T1011";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant11() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                //test format yyyyMMdd'T'HH:mm
                String timeInstant = "20111010T10:11";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant12() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmm
                String timeInstant = "2011-10-10T1011";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant13() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm
                String timeInstant = "2011-10-10T10:11";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant14() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));

                
                //test format yyyyMMdd'T'HHmm'Z'
                String timeInstant = "20111010T1011Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant15() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH:mm'Z'
                String timeInstant = "20111010T10:11Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant16() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmm'Z'
                String timeInstant = "2011-10-10T1011Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant17() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm'Z'
                String timeInstant = "2011-10-10T10:11Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:00.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant18() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HHmmss
                String timeInstant = "20111010T101120";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant19() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH:mm:ss
                String timeInstant = "20111010T10:11:20";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant20() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmmss
                String timeInstant = "2011-10-10T101120";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant21() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm:ss
                String timeInstant = "2011-10-10T10:11:20";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant22() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HHmmss'Z'
                String timeInstant = "20111010T101120Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant23() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH:mm:ss'Z'
                String timeInstant = "20111010T10:11:20Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant24() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmmss'Z'
                String timeInstant = "2011-10-10T101120Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant25() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm:ss'Z'
                String timeInstant = "2011-10-10T10:11:20Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.000Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant26() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HHmmssSSS
                String timeInstant = "20111010T101120666";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant27() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH:mm:ss.SSS
                String timeInstant = "20111010T10:11:20.666";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant28() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmmssSSS
                String timeInstant = "2011-10-10T101120666";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant29() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm:ss.SSS
                String timeInstant = "2011-10-10T10:11:20.666";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant30() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HHmmssSSS'Z'
                String timeInstant = "20111010T101120666Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant31() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyyMMdd'T'HH:mm:ss.SSS'Z'
                String timeInstant = "20111010T10:11:20.666Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant32() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HHmmssSSS'Z'
                String timeInstant = "2011-10-10T101120666Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}
	
	@Test
        public void testParserOnTimeInstant33() throws ParseException {
                
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                
                //test format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
                String timeInstant = "2011-10-10T10:11:20.666Z";
                List<Date> time = PARSER.parse(timeInstant);
                assertEquals(1, time.size());
                assertEquals("2011-10-10T10:11:20.666Z",df.format(time.get(0)));
	}

	@Test
	public void testParserOnTimePeriod() throws ParseException {
		final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-10T14:11:12.000Z/PT1H";
		List<Date> time = PARSER.parse(timeInterval);
		assertEquals(5, time.size());
		assertEquals(1318241472000l, time.get(0).getTime());
		assertEquals(1318241472000l + (3600 * 1000 * 4), time.get(time.size()-1).getTime());
	}
	
	@Test
	public void testParserOnDayPeriod() throws ParseException {
		final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-14T10:11:12.000Z/P2D";
		List<Date> time = PARSER.parse(timeInterval);
		assertEquals(3, time.size());
		assertEquals(1318241472000l, time.get(0).getTime());
		assertEquals(1318241472000l + (3600 * 1000 * 48), time.get(1).getTime());
	}

}
