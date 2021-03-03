/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TemporalConverterFactoryTest {

    TemporalConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new TemporalConverterFactory();
    }

    /**
     * When converting from Calendar to Date we run into a problem where the Dates are out by a very
     * small number. Basically we need to look at the Calendar and see if it represents an *entire*
     * day.
     */
    @Test
    public void testStitchInTime() throws Exception {
        Converter converter = factory.createConverter(Calendar.class, Date.class, null);

        Calendar calendar = Calendar.getInstance();

        // Year, month, date, hour, minute, second.
        calendar.set(2004, 06, 1);
        for (int i = 1; i <= 12; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date date = converter.convert(calendar, Date.class);
            pause();
            Assert.assertNotNull(date);
            Assert.assertEquals(calendar.getTime(), date);
        }
        calendar.set(2004, 06, 1, 12, 30);
        Date date = converter.convert(calendar, Date.class);
        pause();
        Assert.assertNotNull(date);
        Assert.assertEquals(calendar.getTime(), date);
    }

    /** Pause for one tick of the clock ... */
    public static void pause() {
        long pause =
                System.currentTimeMillis() + 15; // 15 is about the resolution of a system clock
        while (System.currentTimeMillis() < pause) {
            Thread.yield();
        }
    }

    @Test
    public void testCalendarToDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Assert.assertNotNull(factory.createConverter(Calendar.class, Date.class, null));

        Date date =
                factory.createConverter(Calendar.class, Date.class, null)
                        .convert(calendar, Date.class);
        Assert.assertNotNull(date);
        Assert.assertEquals(calendar.getTime(), date);
    }

    /*
     * Make sure that additional Milliseconds (cause an offset) do not appear
     * after conversion
     */
    @Test
    public void testCalendarToDateWithMilliseconds() throws Exception {
        Calendar calendar = Calendar.getInstance();
        long offset = 123;
        calendar.set(Calendar.MILLISECOND, (int) offset);
        Assert.assertNotNull(factory.createConverter(Calendar.class, Date.class, null));

        Date date =
                factory.createConverter(Calendar.class, Date.class, null)
                        .convert(calendar, Date.class);
        Assert.assertNotNull(date);

        Assert.assertEquals(calendar.getTime(), date);
    }

    @Test
    public void testCalendarToTime() throws Exception {
        Calendar calendar = Calendar.getInstance(/*TimeZone.getTimeZone("GMT")*/ );
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        Assert.assertNotNull(factory.createConverter(Calendar.class, Time.class, null));

        Time time =
                factory.createConverter(Calendar.class, Time.class, null)
                        .convert(calendar, Time.class);
        Assert.assertNotNull(time);
        // need to remove the date part
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        Assert.assertEquals(cal.getTimeInMillis(), time.getTime());
    }

    @Test
    public void testCalendarToTimestamp() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Assert.assertNotNull(factory.createConverter(Calendar.class, Timestamp.class, null));

        Timestamp timeStamp =
                factory.createConverter(Calendar.class, Timestamp.class, null)
                        .convert(calendar, Timestamp.class);
        Assert.assertNotNull(timeStamp);
        Assert.assertEquals(new Timestamp(calendar.getTime().getTime()), timeStamp);
    }

    /** Make sure that milliseconds do not get lost after conversion */
    @Test
    public void testCalendarToTimestampWithMilliseconds() throws Exception {
        Calendar calendar = Calendar.getInstance();
        long offset = 123;

        calendar.set(Calendar.MILLISECOND, (int) offset);
        Assert.assertNotNull(factory.createConverter(Calendar.class, Timestamp.class, null));
        Calendar calWithMs = (Calendar) calendar.clone();

        Timestamp timeStamp =
                factory.createConverter(Calendar.class, Timestamp.class, null)
                        .convert(calendar, Timestamp.class);
        Assert.assertNotNull(timeStamp);
        Assert.assertEquals(new Timestamp(calWithMs.getTime().getTime()), timeStamp);
        Assert.assertEquals(new Timestamp(calendar.getTime().getTime()), timeStamp);
    }

    @Test
    public void testDateToCalendar() throws Exception {
        Date date = new Date();
        Assert.assertNotNull(factory.createConverter(Date.class, Calendar.class, null));

        Calendar calendar =
                factory.createConverter(Date.class, Calendar.class, null)
                        .convert(date, Calendar.class);
        Assert.assertNotNull(calendar);
        Assert.assertEquals(date, calendar.getTime());
    }

    @Test
    public void testDateToTime() throws Exception {
        Date date = new Date();
        Assert.assertNotNull(factory.createConverter(Date.class, Time.class, null));

        Time time = factory.createConverter(Date.class, Time.class, null).convert(date, Time.class);
        Assert.assertNotNull(time);
        // need to remove the date part
        Calendar cal = Calendar.getInstance(/*TimeZone.getTimeZone("GMT")*/ );
        cal.setTime(date);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        Assert.assertEquals(cal.getTimeInMillis(), time.getTime());
    }

    @Test
    public void testDateToTimestamp() throws Exception {
        Date date = new Date();
        Assert.assertNotNull(factory.createConverter(Date.class, Timestamp.class, null));

        Timestamp timeStamp =
                factory.createConverter(Date.class, Timestamp.class, null)
                        .convert(date, Timestamp.class);
        Assert.assertNotNull(timeStamp);
        Assert.assertEquals(new Timestamp(date.getTime()), timeStamp);

        // check safe conversion
        Hints h = new Hints();
        h.put(ConverterFactory.SAFE_CONVERSION, Boolean.TRUE);
        Assert.assertNull(factory.createConverter(Timestamp.class, Calendar.class, h));
        h.put(ConverterFactory.SAFE_CONVERSION, Boolean.FALSE);
        Assert.assertNotNull(factory.createConverter(Timestamp.class, Calendar.class, h));
    }

    @Test
    public void testTimeToCalendar() throws Exception {
        Time time = new Time(new Date().getTime());
        Assert.assertNotNull(factory.createConverter(Time.class, Calendar.class, null));

        Calendar calendar =
                factory.createConverter(Time.class, Calendar.class, null)
                        .convert(time, Calendar.class);
        Assert.assertNotNull(calendar);
        Assert.assertEquals(time, new Time(calendar.getTime().getTime()));
    }

    @Test
    public void testTimestampToCalendar() throws Exception {
        Timestamp timeStamp = new Timestamp(new Date().getTime());
        Assert.assertNotNull(factory.createConverter(Timestamp.class, Calendar.class, null));

        Calendar calendar =
                factory.createConverter(Timestamp.class, Calendar.class, null)
                        .convert(timeStamp, Calendar.class);
        Assert.assertNotNull(calendar);
        Assert.assertEquals(timeStamp, new Timestamp(calendar.getTime().getTime()));
    }

    @Test
    public void testXMLGregorianCalendarToCalendar() throws Exception {
        XMLGregorianCalendar gc =
                DatatypeFactory.newInstance().newXMLGregorianCalendar("1981-06-20T12:00:00");
        Assert.assertNotNull(
                factory.createConverter(XMLGregorianCalendar.class, Calendar.class, null));

        Calendar calendar =
                factory.createConverter(XMLGregorianCalendar.class, Calendar.class, null)
                        .convert(gc, Calendar.class);
        Assert.assertNotNull(calendar);

        Assert.assertEquals(1981, calendar.get(Calendar.YEAR));
        Assert.assertEquals(5, calendar.get(Calendar.MONTH));
        Assert.assertEquals(20, calendar.get(Calendar.DATE));
        Assert.assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, calendar.get(Calendar.SECOND));
    }

    @Test
    public void testCalendarToXMLGregorianCalendar() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Assert.assertNotNull(
                factory.createConverter(Calendar.class, XMLGregorianCalendar.class, null));

        XMLGregorianCalendar gc =
                factory.createConverter(Calendar.class, XMLGregorianCalendar.class, null)
                        .convert(calendar, XMLGregorianCalendar.class);
        Assert.assertNotNull(gc);

        Calendar actual = gc.toGregorianCalendar();
        Assert.assertEquals(calendar.get(Calendar.YEAR), actual.get(Calendar.YEAR));
        Assert.assertEquals(calendar.get(Calendar.MONTH), actual.get(Calendar.MONTH));
        Assert.assertEquals(calendar.get(Calendar.DATE), actual.get(Calendar.DATE));
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), actual.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(calendar.get(Calendar.MINUTE), actual.get(Calendar.MINUTE));
        Assert.assertEquals(calendar.get(Calendar.SECOND), actual.get(Calendar.SECOND));
        Assert.assertEquals(calendar.get(Calendar.MILLISECOND), actual.get(Calendar.MILLISECOND));
    }

    @Test
    public void testXMLGregorianCalendarToDate() throws Exception {
        XMLGregorianCalendar gc =
                DatatypeFactory.newInstance().newXMLGregorianCalendar("1981-06-20T12:00:00");
        Assert.assertNotNull(factory.createConverter(XMLGregorianCalendar.class, Date.class, null));

        Date date =
                factory.createConverter(XMLGregorianCalendar.class, Date.class, null)
                        .convert(gc, Date.class);
        Assert.assertNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Assert.assertEquals(1981, calendar.get(Calendar.YEAR));
        Assert.assertEquals(5, calendar.get(Calendar.MONTH));
        Assert.assertEquals(20, calendar.get(Calendar.DATE));
        Assert.assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, calendar.get(Calendar.SECOND));
    }

    @Test
    public void testDateToXMLGregorianCalendar() throws Exception {
        Date date = new Date();
        Assert.assertNotNull(factory.createConverter(Date.class, XMLGregorianCalendar.class, null));

        XMLGregorianCalendar gc =
                factory.createConverter(Date.class, XMLGregorianCalendar.class, null)
                        .convert(date, XMLGregorianCalendar.class);

        Assert.assertNotNull(gc);

        Assert.assertEquals(date, gc.toGregorianCalendar().getTime());
    }

    @Test
    public void testTimeZoneToString() throws Exception {
        Converter converter = factory.createConverter(TimeZone.class, String.class, null);
        Assert.assertNotNull(converter);

        Assert.assertEquals(
                TimeZone.getDefault().getID(),
                converter.convert(TimeZone.getDefault(), String.class));
        Assert.assertNull(converter.convert(null, String.class));
    }

    @Test
    public void testLongtoDate() throws Exception {
        Converter converter = factory.createConverter(Long.class, Date.class, null);
        Assert.assertNotNull(converter);

        Assert.assertNull(converter.convert(null, Date.class));

        // Thu, 21 May 2015 00:00:00 GMT
        Long esriFieldTypeDateVal = 1432166400000L;
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTimeInMillis(esriFieldTypeDateVal);
        Date expected = c.getTime();
        Date actual = converter.convert(esriFieldTypeDateVal, Date.class);
        Assert.assertEquals(expected, actual);
    }
}
