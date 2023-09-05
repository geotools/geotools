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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.geotools.api.temporal.DateAndTime;
import org.geotools.api.temporal.TemporalReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
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

    /** Test of getClockTime method, of class DefaultDateAndTime. */
    @Test
    public void testGetClockTime() {
        Number[] result = dateAndTime1.getClockTime();
        assertThat(dateAndTime2.getClockTime(), not(equalTo(result)));
    }

    /** Test of getCalendarEraName method, of class DefaultDateAndTime. */
    @Test
    public void testGetCalendarEraName() {
        InternationalString result = dateAndTime1.getCalendarEraName();
        assertEquals(dateAndTime2.getCalendarEraName(), result);
    }

    /** Test of getCalendarDate method, of class DefaultDateAndTime. */
    @Test
    public void testGetCalendarDate() {
        int[] result = dateAndTime1.getCalendarDate();
        assertThat(dateAndTime2.getCalendarDate(), not(equalTo(result)));
    }

    /** Test of setCalendarEraName method, of class DefaultDateAndTime. */
    @Test
    public void testSetCalendarEraName() {
        InternationalString result = dateAndTime1.getCalendarEraName();
        ((DefaultDateAndTime) dateAndTime1)
                .setCalendarEraName(new SimpleInternationalString("new Era"));
        assertNotEquals(dateAndTime1.getCalendarEraName(), result);
    }

    /** Test of setCalendarDate method, of class DefaultDateAndTime. */
    @Test
    public void testSetCalendarDate() {
        int[] result = dateAndTime1.getCalendarDate();
        int[] caldate = {1990, 3, 6};
        ((DefaultDateAndTime) dateAndTime1).setCalendarDate(caldate);
        assertThat(dateAndTime1.getCalendarDate(), not(equalTo(result)));
    }

    /** Test of setClockTime method, of class DefaultDateAndTime. */
    @Test
    public void testSetClockTime() {
        Number[] result = dateAndTime1.getClockTime();
        Number[] clk = {15, 23, 5.7};
        ((DefaultDateAndTime) dateAndTime1).setClockTime(clk);
        assertThat(dateAndTime1.getClockTime(), not(equalTo(result)));
    }

    /** Test of equals method, of class DefaultDateAndTime. */
    @Test
    public void testEquals() {
        assertNotEquals(null, dateAndTime1);
        assertEquals(dateAndTime1, dateAndTime1);
    }

    /** Test of hashCode method, of class DefaultDateAndTime. */
    @Test
    public void testHashCode() {
        int result = dateAndTime1.hashCode();
        assertNotEquals(dateAndTime2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultDateAndTime. */
    @Test
    public void testToString() {
        String result = dateAndTime1.toString();
        assertNotEquals(dateAndTime2.toString(), result);
    }
}
