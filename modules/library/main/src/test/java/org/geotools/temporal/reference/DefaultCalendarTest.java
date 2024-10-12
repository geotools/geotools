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
package org.geotools.temporal.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import org.geotools.api.temporal.Calendar;
import org.geotools.api.temporal.CalendarDate;
import org.geotools.api.temporal.CalendarEra;
import org.geotools.api.temporal.Clock;
import org.geotools.api.temporal.ClockTime;
import org.geotools.api.temporal.DateAndTime;
import org.geotools.api.temporal.IndeterminateValue;
import org.geotools.api.temporal.JulianDate;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultCalendarDate;
import org.geotools.temporal.object.DefaultClockTime;
import org.geotools.temporal.object.DefaultDateAndTime;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultJulianDate;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultCalendarTest {

    private Calendar calendar1;
    private Calendar calendar2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Julian calendar");
        calendar1 = new DefaultCalendar(name1, null);
        calendar2 = new DefaultCalendar(name2, null);
    }

    @After
    public void tearDown() {
        calendar1 = null;
        calendar2 = null;
    }

    /** Test of dateTrans method, of class DefaultCalendar. */
    @Test
    public void testDateTrans_CalendarDate_ClockTime() {
        int[] cal = {2012, 9, 10};
        CalendarDate calendarDate = new DefaultCalendarDate(
                calendar1, IndeterminateValue.NOW, new SimpleInternationalString("new Era"), cal);
        Number[] clock = {12, 10, 5.488};
        ClockTime clockTime = new DefaultClockTime(calendar1, IndeterminateValue.NOW, clock);
        JulianDate result = calendar1.dateTrans(calendarDate, clockTime);
        assertEquals(calendar2.dateTrans(calendarDate, clockTime), result);
    }

    /** Test of dateTrans method, of class DefaultCalendar. */
    @Test
    public void testDateTrans_DateAndTime() {
        int[] cal = {2012, 9, 10};
        Number[] clock = {12, 10, 5.488};
        DateAndTime dateAndTime = new DefaultDateAndTime(calendar1, null, null, cal, clock);
        JulianDate result = ((DefaultCalendar) calendar1).dateTrans(dateAndTime);
        assertEquals(((DefaultCalendar) calendar1).dateTrans(dateAndTime), result);
    }

    /** Test of julTrans method, of class DefaultCalendar. */
    @Test
    public void testJulTrans() {
        // @TODO this method is not supported yet!
    }

    /** Test of getBasis method, of class DefaultCalendar. */
    @Test
    public void testGetBasis() {
        Collection<CalendarEra> result = calendar1.getBasis();
        assertEquals(calendar2.getBasis(), result);
    }

    /** Test of getClock method, of class DefaultCalendar. */
    @Test
    public void testGetClock() {
        Clock result = calendar1.getClock();
        assertEquals(calendar2.getClock(), result);
    }

    /** Test of setBasis method, of class DefaultCalendar. */
    @Test
    public void testSetBasis() throws ParseException {
        Collection<CalendarEra> result = calendar1.getBasis();
        int[] calendarDate = {1, 1, 1};
        CalendarEra calendarEra = new DefaultCalendarEra(
                new SimpleInternationalString("Babylonian calendar"),
                new SimpleInternationalString("Ascension of Nebuchadnezzar II to the throne of Babylon"),
                new DefaultCalendarDate(calendar1, null, null, calendarDate),
                new DefaultJulianDate(calendar1, null, 1721423.25),
                new DefaultPeriod(
                        new DefaultInstant(new DefaultPosition(new DefaultJulianDate(calendar1, null, 2087769))),
                        new DefaultInstant(new DefaultPosition(new DefaultJulianDate(calendar1, null, 2299160)))));
        Collection<CalendarEra> collection = new ArrayList<>();
        collection.add(calendarEra);
        ((DefaultCalendar) calendar1).setBasis(collection);
        assertNotEquals(calendar1.getBasis(), result);
    }

    /** Test of setClock method, of class DefaultCalendar. */
    @Test
    public void testSetClock() {
        Clock result = calendar1.getClock();
        ((DefaultCalendar) calendar1).setClock(null);
        assertEquals(calendar1.getClock(), result);
    }

    /** Test of equals method, of class DefaultCalendar. */
    @Test
    public void testEquals() {
        assertNotEquals(null, calendar1);
        assertEquals(calendar1, calendar1);
        assertNotEquals(calendar1, calendar2);
    }

    /** Test of hashCode method, of class DefaultCalendar. */
    @Test
    public void testHashCode() {
        int result = calendar1.hashCode();
        assertNotEquals(calendar2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultCalendar. */
    @Test
    public void testToString() {
        String result = calendar1.toString();
        assertNotEquals(calendar2.toString(), result);
    }
}
