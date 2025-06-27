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

import org.geotools.api.temporal.CalendarDate;
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
public class DefaultCalendarDateTest {

    private CalendarDate calendarDate1;
    private CalendarDate calendarDate2;

    @Before
    public void setUp() {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        TemporalReferenceSystem frame = new DefaultTemporalReferenceSystem(name, null);
        int[] cal1 = {1981, 6, 25};
        int[] cal2 = {2000, 1, 1};
        InternationalString cal_era = new SimpleInternationalString("Cenozoic");
        calendarDate1 = new DefaultCalendarDate(frame, null, cal_era, cal1);
        calendarDate2 = new DefaultCalendarDate(frame, null, cal_era, cal2);
    }

    @After
    public void tearDown() {
        calendarDate1 = null;
        calendarDate2 = null;
    }

    /** Test of getCalendarEraName method, of class DefaultCalendarDate. */
    @Test
    public void testGetCalendarEraName() {
        InternationalString result = calendarDate1.getCalendarEraName();
        assertEquals(calendarDate2.getCalendarEraName(), result);
    }

    /** Test of getCalendarDate method, of class DefaultCalendarDate. */
    @Test
    public void testGetCalendarDate() {
        int[] result = calendarDate1.getCalendarDate();
        assertThat(calendarDate2.getCalendarDate(), not(equalTo(result)));
    }

    /** Test of setCalendarEraName method, of class DefaultCalendarDate. */
    @Test
    public void testSetCalendarEraName() {
        InternationalString result = calendarDate1.getCalendarEraName();
        ((DefaultCalendarDate) calendarDate1).setCalendarEraName(new SimpleInternationalString("new Era"));
        assertNotEquals(calendarDate1.getCalendarEraName(), result);
    }

    /** Test of setCalendarDate method, of class DefaultCalendarDate. */
    @Test
    public void testSetCalendarDate() {
        int[] result = calendarDate1.getCalendarDate();
        int[] caldate = {1995, 5, 5};
        ((DefaultCalendarDate) calendarDate1).setCalendarDate(caldate);
        assertThat(calendarDate1.getCalendarDate(), not(equalTo(result)));
    }

    /** Test of equals method, of class DefaultCalendarDate. */
    @Test
    public void testEquals() {
        assertNotEquals(null, calendarDate1);
        assertEquals(calendarDate1, calendarDate1);
    }

    /** Test of hashCode method, of class DefaultCalendarDate. */
    @Test
    public void testHashCode() {
        int result = calendarDate1.hashCode();
        assertNotEquals(calendarDate2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultCalendarDate. */
    @Test
    public void testToString() {
        String result = calendarDate1.toString();
        assertNotEquals(calendarDate2.toString(), result);
    }
}
