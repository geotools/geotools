/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.geotools.factory.Hints;
import org.geotools.xs.bindings.XSDateBinding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for time zone aspects of date conversion in {@link XmlConverterFactory} and {@link
 * XSDateBinding}.
 *
 * @author awaterme
 */
public class DateConversionTimezoneTest extends TestCase {

    // "Systems under Test"
    private XmlConverterFactory sut1 = new XmlConverterFactory();

    private XSDateBinding sut2 = new XSDateBinding();

    private TimeZone systemTimeZone;

    /**
     * Tests date encoding having {@link Hints#LOCAL_DATE_TIME_HANDLING} activated
     *
     * @throws Exception
     */
    @Test
    public void testLocalEncode() throws Exception {
        Hints.putSystemDefault(Hints.LOCAL_DATE_TIME_HANDLING, true);

        // UTC
        assertDateEquals("2015-09-02", 2015, 9, 2, 0, "UTC");
        assertDateEquals("2015-09-02", 2015, 9, 2, 1, "UTC");
        assertDateEquals("2015-09-02", 2015, 9, 2, 23, "UTC");
        // GMT
        assertDateEquals("2015-09-02", 2015, 9, 2, 0, "GMT");
        assertDateEquals("2015-09-02", 2015, 9, 2, 1, "GMT");
        assertDateEquals("2015-09-02", 2015, 9, 2, 23, "GMT");
        // CET
        assertDateEquals("2015-09-02", 2015, 9, 2, 0, "CET");
        assertDateEquals("2015-09-02", 2015, 9, 2, 1, "CET");
        assertDateEquals("2015-09-02", 2015, 9, 2, 2, "CET");
        assertDateEquals("2015-09-02", 2015, 9, 2, 23, "CET");
        // EST
        assertDateEquals("2015-09-02", 2015, 9, 2, 0, "EST");
        assertDateEquals("2015-09-02", 2015, 9, 2, 1, "EST");
        assertDateEquals("2015-09-02", 2015, 9, 2, 23, "EST");
    }

    /**
     * Tests date encoding having {@link Hints#LOCAL_DATE_TIME_HANDLING} activated
     *
     * @throws Exception
     */
    @Test
    public void testTimezoneAwareEncode() throws Exception {
        // UTC: zone offset == 0 -> no shifting
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 0, "UTC");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 1, "UTC");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 23, "UTC");
        // GMT: zone offset == 0 -> no shifting
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 0, "GMT");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 1, "GMT");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 23, "GMT");
        // CET: zone offset > 0 -> shifting backwards
        assertDateEquals("2015-09-01Z", 2015, 9, 2, 0, "CET");
        assertDateEquals("2015-09-01Z", 2015, 9, 2, 1, "CET");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 2, "CET");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 23, "CET");
        // EST: zone offset < 0 -> shifting forward
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 0, "EST");
        assertDateEquals("2015-09-02Z", 2015, 9, 2, 1, "EST");
        assertDateEquals("2015-09-03Z", 2015, 9, 2, 23, "EST");
    }

    private void assertDateEquals(
            String expected, int year, int month, int day, int hour, String timezoneId)
            throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
        TimeZone.setDefault(timeZone);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        java.util.Date utilDate = calendar.getTime();
        Date date = new Date(utilDate.getTime());
        assertEquals(expected, sut1Convert(date));
        assertEquals(expected, sut2Convert(date));
    }

    /** Save & restore system time zone, so later tests are not affected. */
    @Before
    public void setUp() {
        systemTimeZone = TimeZone.getDefault();
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(systemTimeZone);
        Hints.removeSystemDefault(Hints.LOCAL_DATE_TIME_HANDLING);
    }

    private String sut1Convert(Date date) throws Exception {
        return sut1.createConverter(Date.class, String.class, null).convert(date, String.class);
    }

    private String sut2Convert(Date date) throws Exception {
        return sut2.encode(date, null);
    }
}
