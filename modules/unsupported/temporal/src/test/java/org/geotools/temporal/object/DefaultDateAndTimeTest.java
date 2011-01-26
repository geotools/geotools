/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.DateAndTime;
import org.opengis.temporal.TemporalReferenceSystem;
import static org.junit.Assert.*;
import org.opengis.util.InternationalString;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultDateAndTimeTest {

    private DateAndTime dateAndTime1;
    private DateAndTime dateAndTime2;

    @Before
    public void setUp() {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        TemporalReferenceSystem frame = new DefaultTemporalReferenceSystem(name, null);
        int[] cal1 = {1981, 6, 25};
        int[] cal2 = {2000, 1, 1};
        Number[] clck1 = {8, 16, 25};
        Number[] clck2 = {15, 55, 1};
        InternationalString calendarEraName = new SimpleInternationalString("Cenozoic");
        dateAndTime1 = new DefaultDateAndTime(frame, null, calendarEraName, cal1, clck1);
        dateAndTime2 = new DefaultDateAndTime(frame, null, calendarEraName, cal2, clck2);
    }

    @After
    public void tearDown() {
        dateAndTime1 = null;
        dateAndTime2 = null;
    }

    /**
     * Test of getClockTime method, of class DefaultDateAndTime.
     */
    @Test
    public void testGetClockTime() {
        Number[] result = dateAndTime1.getClockTime();
        assertFalse(dateAndTime2.getClockTime().equals(result));
    }

    /**
     * Test of getCalendarEraName method, of class DefaultDateAndTime.
     */
    @Test
    public void testGetCalendarEraName() {
        InternationalString result = dateAndTime1.getCalendarEraName();
        assertTrue(dateAndTime2.getCalendarEraName().equals(result));
    }

    /**
     * Test of getCalendarDate method, of class DefaultDateAndTime.
     */
    @Test
    public void testGetCalendarDate() {
        int[] result = dateAndTime1.getCalendarDate();
        assertFalse(dateAndTime2.getCalendarDate().equals(result));
    }

    /**
     * Test of setCalendarEraName method, of class DefaultDateAndTime.
     */
    @Test
    public void testSetCalendarEraName() {
        InternationalString result = dateAndTime1.getCalendarEraName();
        ((DefaultDateAndTime) dateAndTime1).setCalendarEraName(new SimpleInternationalString("new Era"));
        assertFalse(dateAndTime1.getCalendarEraName().equals(result));
    }

    /**
     * Test of setCalendarDate method, of class DefaultDateAndTime.
     */
    @Test
    public void testSetCalendarDate() {
        int[] result = dateAndTime1.getCalendarDate();
        int[] caldate = {1990, 3, 6};
        ((DefaultDateAndTime) dateAndTime1).setCalendarDate(caldate);
        assertFalse(dateAndTime1.getCalendarDate().equals(result));
    }

    /**
     * Test of setClockTime method, of class DefaultDateAndTime.
     */
    @Test
    public void testSetClockTime() {
        Number[] result = dateAndTime1.getClockTime();
        Number[] clk = {15, 23, 5.7};
        ((DefaultDateAndTime) dateAndTime1).setClockTime(clk);
        assertFalse(dateAndTime1.getClockTime().equals(result));
    }

    /**
     * Test of equals method, of class DefaultDateAndTime.
     */
    @Test
    public void testEquals() {
        assertFalse(dateAndTime1.equals(null));
        assertEquals(dateAndTime1, dateAndTime1);
    }

    /**
     * Test of hashCode method, of class DefaultDateAndTime.
     */
    @Test
    public void testHashCode() {
        int result = dateAndTime1.hashCode();
        assertFalse(dateAndTime2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultDateAndTime.
     */
    @Test
    public void testToString() {
        String result = dateAndTime1.toString();
        assertFalse(dateAndTime2.toString().equals(result));
    }
}
