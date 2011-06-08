/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;

import org.geotools.factory.Hints;

public class TemporalConverterFactoryTest extends TestCase {

	TemporalConverterFactory factory;
	
	protected void setUp() throws Exception {
		factory = new TemporalConverterFactory();
	}

	/**
	 * When converting from Calendar to Date from ArcSDE we run into a problem
	 * where the Dates are out by a very small number. Basically we need to 
	 * look at the Calendar and see if it represents an *entire* day.
	 * 
	 * @throws Exception
	 */
    public void testStitchInTime() throws Exception {
        Converter converter = factory.createConverter( Calendar.class, Date.class, null );
        
        Calendar calendar = Calendar.getInstance();
        
        // Year, month, date, hour, minute, second.
        calendar.set(2004, 06, 1);
        for( int i=1; i<=12;i++){
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date date = converter.convert( calendar, Date.class );
            pause();
            assertNotNull( date );
            assertEquals( calendar.getTime(), date );            
        }        
        calendar.set(2004, 06, 1, 12, 30 );
        Date date = converter.convert( calendar, Date.class );
        pause();
        assertNotNull( date );
        assertEquals( calendar.getTime(), date );
    }
    
    /** Pause for one tick of the clock ... */
    public static void pause(){
        long pause = System.currentTimeMillis() + 15; // 15 is about the resolution of a system clock
        while( System.currentTimeMillis() < pause ){
            Thread.yield();
        }
    }
    
	public void testCalendarToDate() throws Exception {
		Calendar calendar = Calendar.getInstance();
		assertNotNull( factory.createConverter( Calendar.class, Date.class, null ) );
		
		Date date = (Date) factory.createConverter( Calendar.class, Date.class, null )
			.convert( calendar, Date.class );
		assertNotNull( date );
		assertEquals( calendar.getTime(), date );
	}
	
	
	/*
	 * Make sure that additional Milliseconds (cause an offset) do not appear
	 * after conversion
	 */
	public void testCalendarToDateWithMilliseconds() throws Exception {
		Calendar calendar = Calendar.getInstance();
		long offset = 123;
		calendar.set(Calendar.MILLISECOND, (int) offset);
		assertNotNull(factory.createConverter(Calendar.class, Date.class, null));

		Date date = (Date) factory.createConverter(Calendar.class, Date.class,
				null).convert(calendar, Date.class);
		assertNotNull(date);

		assertEquals(calendar.getTime(), date);
	}
	
	public void testCalendarToTime() throws Exception {
		Calendar calendar = Calendar.getInstance();
		assertNotNull( factory.createConverter( Calendar.class, Time.class, null ) );
		
		Time time = (Time) factory.createConverter( Calendar.class, Time.class, null )
			.convert( calendar, Time.class );
		assertNotNull( time );
		// need to remove the date part
		Calendar cal = (Calendar) calendar.clone();
    	cal.set(Calendar.YEAR, 0);
    	cal.set(Calendar.MONTH, 0);
    	cal.set(Calendar.DAY_OF_MONTH, 0);
		assertEquals(cal.getTimeInMillis(), time.getTime());
	}
	
	public void testCalendarToTimestamp() throws Exception {
		Calendar calendar = Calendar.getInstance();
		assertNotNull( factory.createConverter( Calendar.class, Timestamp.class, null ) );
		
		Timestamp timeStamp = (Timestamp) factory.createConverter( Calendar.class, Timestamp.class, null )
			.convert( calendar, Timestamp.class );
		assertNotNull( timeStamp );
		assertEquals( new Timestamp( calendar.getTime().getTime() ), timeStamp );
	}
	
	/**
	 * Make sure that milliseconds do not get lost after conversion
	 * 
	 * @throws Exception
	 */
	public void testCalendarToTimestampWithMilliseconds() throws Exception {
		Calendar calendar = Calendar.getInstance();
		long offset = 123;

		calendar.set(Calendar.MILLISECOND, (int) offset);
		assertNotNull(factory.createConverter(Calendar.class, Timestamp.class,
				null));
		Calendar calWithMs = (Calendar) calendar.clone();

		Timestamp timeStamp = (Timestamp) factory.createConverter(
				Calendar.class, Timestamp.class, null).convert(calendar,
				Timestamp.class);
		assertNotNull(timeStamp);
		assertEquals(new Timestamp(calWithMs.getTime().getTime()), timeStamp);
		assertEquals(new Timestamp(calendar.getTime().getTime()), timeStamp);
	}
	
	public void testDateToCalendar() throws Exception {
		Date date = new Date();
		assertNotNull( factory.createConverter(  Date.class, Calendar.class, null ) );
		
		Calendar calendar = (Calendar) factory.createConverter( Date.class, Calendar.class, null )
			.convert( date, Calendar.class );
		assertNotNull( calendar );
		assertEquals( date, calendar.getTime() );
	}
	
	public void testDateToTime() throws Exception {
		Date date = new Date();
		assertNotNull( factory.createConverter(  Date.class, Time.class, null ) );
		
		Time time = (Time) factory.createConverter( Date.class, Time.class, null )
			.convert( date, Time.class );
		assertNotNull( time );
		// need to remove the date part
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTime(date);
    	cal.set(Calendar.YEAR, 0);
    	cal.set(Calendar.MONTH, 0);
    	cal.set(Calendar.DAY_OF_MONTH, 0);
		assertEquals(cal.getTimeInMillis(), time.getTime());
	}
	
	
	
	public void testDateToTimestamp() throws Exception {
		Date date = new Date();
		assertNotNull( factory.createConverter(  Date.class, Timestamp.class, null ) );
		
		Timestamp timeStamp = (Timestamp) factory.createConverter( Date.class, Timestamp.class, null )
			.convert( date, Timestamp.class );
		assertNotNull( timeStamp );
		assertEquals( new Timestamp( date.getTime() ), timeStamp );
		
		      
		//check safe conversion
		Hints h = new Hints();
		h.put(ConverterFactory.SAFE_CONVERSION, new Boolean(true));
		assertNull( factory.createConverter( Timestamp.class, Calendar.class, h ) );
		h.put(ConverterFactory.SAFE_CONVERSION, new Boolean(false));
		assertNotNull( factory.createConverter( Timestamp.class, Calendar.class, h ) );
	}
	
	public void testTimeToCalendar() throws Exception {
		Time time = new Time(new Date().getTime());
		assertNotNull( factory.createConverter(  Time.class, Calendar.class, null ) );
		
		Calendar calendar = (Calendar) factory.createConverter( Time.class, Calendar.class, null )
			.convert( time, Calendar.class );
		assertNotNull( calendar );
		assertEquals( time, new Time( calendar.getTime().getTime() ) );
	}
	
	public void testTimestampToCalendar() throws Exception {
		Timestamp timeStamp = new Timestamp( new Date().getTime() );
		assertNotNull( factory.createConverter(  Timestamp.class, Calendar.class, null ) );
		
		Calendar calendar = (Calendar) factory.createConverter( Timestamp.class, Calendar.class, null )
			.convert( timeStamp, Calendar.class );
		assertNotNull( calendar );
		assertEquals( timeStamp, new Timestamp( calendar.getTime().getTime() ) );
	}
	
	public void testXMLGregorianCalendarToCalendar() throws Exception {
		XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar("1981-06-20T12:00:00");
	    assertNotNull( factory.createConverter( XMLGregorianCalendar.class, Calendar.class, null));
	    
	    Calendar calendar = (Calendar) factory.createConverter( XMLGregorianCalendar.class, Calendar.class, null)
	        .convert( gc, Calendar.class );
	    assertNotNull(calendar);
	    
	    assertEquals( 1981, calendar.get( Calendar.YEAR ) );
	    assertEquals( 5, calendar.get( Calendar.MONTH ) );
	    assertEquals( 20, calendar.get( Calendar.DATE ) );
	    assertEquals( 12, calendar.get( Calendar.HOUR_OF_DAY ) );
	    assertEquals( 0, calendar.get( Calendar.MINUTE ) );
	    assertEquals( 0, calendar.get( Calendar.SECOND ) );
	}
	
	public void testCalendarToXMLGregorianCalendar() throws Exception {
	    Calendar calendar = Calendar.getInstance();
	    assertNotNull( factory.createConverter( Calendar.class, XMLGregorianCalendar.class, null));
            
            XMLGregorianCalendar gc = factory.createConverter(Calendar.class, XMLGregorianCalendar.class, null)
                .convert( calendar, XMLGregorianCalendar.class );
            assertNotNull(gc);
            
            Calendar actual = gc.toGregorianCalendar();
            assertEquals( calendar.get( Calendar.YEAR ), actual.get( Calendar.YEAR) );
            assertEquals( calendar.get( Calendar.MONTH ), actual.get( Calendar.MONTH) );
            assertEquals( calendar.get( Calendar.DATE ), actual.get( Calendar.DATE) );
            assertEquals( calendar.get( Calendar.HOUR_OF_DAY ), actual.get( Calendar.HOUR_OF_DAY) );
            assertEquals( calendar.get( Calendar.MINUTE ), actual.get( Calendar.MINUTE) );
            assertEquals( calendar.get( Calendar.SECOND ), actual.get( Calendar.SECOND) );
            assertEquals( calendar.get( Calendar.MILLISECOND ), actual.get( Calendar.MILLISECOND) );
        }
	
	public void testXMLGregorianCalendarToDate() throws Exception {
	    XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar("1981-06-20T12:00:00");
	    assertNotNull( factory.createConverter( XMLGregorianCalendar.class, Date.class, null));
            
            Date date =  factory.createConverter( XMLGregorianCalendar.class, Date.class, null)
                .convert( gc, Date.class );
            assertNotNull(date);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( date );
            
            assertEquals( 1981, calendar.get( Calendar.YEAR ) );
            assertEquals( 5, calendar.get( Calendar.MONTH ) );
            assertEquals( 20, calendar.get( Calendar.DATE ) );
            assertEquals( 12, calendar.get( Calendar.HOUR_OF_DAY ) );
            assertEquals( 0, calendar.get( Calendar.MINUTE ) );
            assertEquals( 0, calendar.get( Calendar.SECOND ) );
	}
	
	public void testDateToXMLGregorianCalendar() throws Exception {
	    Date date = new Date();
            assertNotNull( factory.createConverter(  Date.class, XMLGregorianCalendar.class, null ) );
            
            XMLGregorianCalendar gc = factory.createConverter( Date.class, XMLGregorianCalendar.class, null )
                    .convert( date, XMLGregorianCalendar.class );
            
            assertNotNull( gc );
            
            assertEquals( date, gc.toGregorianCalendar().getTime() );
	}
}
