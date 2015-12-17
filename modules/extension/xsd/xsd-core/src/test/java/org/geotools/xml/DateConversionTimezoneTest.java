/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.geotools.xs.bindings.XSDateBinding;

import junit.framework.TestCase;

/**
 * Tests for timezone aspects of date conversion in {@link XmlConverterFactory} and {@link XSDateBinding}.
 * 
 * @author awaterme
 *
 */
public class DateConversionTimezoneTest extends TestCase {

    private XmlConverterFactory sut1 = new XmlConverterFactory();
    private XSDateBinding sut2 = new XSDateBinding();

    private TimeZone systemTimeZone;

    /**
     * Save & restore system time zone, so later tests are not affected.
     */
    public void setupSaveSystemTimeZone() {
        systemTimeZone = TimeZone.getDefault();
    }

    public void tearDownRestoreSystemTimeZone() {
        TimeZone.setDefault(systemTimeZone);
    }

    /**
     * {@link DateBinding#encode(Object, String)} test, simulates behavior of most common case: Date with default time zone, as coming from DB, XML,
     * date format parser.
     * 
     * @throws Exception
     */
    public void testEncodeWithDefaultTimezone() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(format.parse("2015-09-02").getTime());
        assertEquals("2015-09-02Z", sut1Convert(date));
        assertEquals("2015-09-02Z", sut2Convert(date));
    }

    private String sut1Convert(Date date) throws Exception {
        return sut1.createConverter(Date.class, String.class, null).convert(date, String.class);
    }
    
    private String sut2Convert(Date date) throws Exception {
        return sut2.encode(date, null);
    }

    /**
     * {@link DateBinding#encode(Object, String)} test, simulates behavior for zone-offset == 0 for certain hours.
     * 
     * @throws Exception
     */
    public void testEncodeWithTimezoneUTC() throws Exception {
        testEncodeWithTimezone("UTC");
        testEncodeWithTimezone("GMT");
    }

    /**
     * {@link DateBinding#encode(Object, String)} test, simulates behavior for zone-offset greater than 0 for certain hours.
     * 
     * @throws Exception
     */
    public void testEncodeWithTimezoneCET() throws Exception {
        testEncodeWithTimezone("CET");
    }

    /**
     * {@link DateBinding#encode(Object, String)} test, simulates behavior for zone-offset less than 0 for certain hours.
     * 
     * @throws Exception
     */
    public void testEncodeWithTimezoneEST() throws Exception {
        testEncodeWithTimezone("EST");
    }

    private void testEncodeWithTimezone(String timezoneId) throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
        TimeZone.setDefault(timeZone);
        int[] hours = new int[] { 0, 1, 12, 23 };
        for (int hour : hours) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, 2015);
            calendar.set(Calendar.DAY_OF_MONTH, 2);
            calendar.set(Calendar.MONTH, 8);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            Date date = new Date(calendar.getTime().getTime());
            assertEquals("2015-09-02Z", sut1Convert(date));
            assertEquals("2015-09-02Z", sut2Convert(date));
        }
    }

}
