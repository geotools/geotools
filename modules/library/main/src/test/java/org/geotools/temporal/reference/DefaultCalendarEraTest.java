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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.geotools.api.temporal.CalendarDate;
import org.geotools.api.temporal.CalendarEra;
import org.geotools.api.temporal.IndeterminateValue;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.JulianDate;
import org.geotools.api.temporal.Period;
import org.geotools.api.temporal.TemporalReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultCalendarDate;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultJulianDate;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultCalendarEraTest {

    private CalendarEra calendarEra1;
    private CalendarEra calendarEra2;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Julian calendar");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Babylonian calendar");
        TemporalReferenceSystem frame1 = new DefaultTemporalReferenceSystem(name1, null);
        TemporalReferenceSystem frame2 = new DefaultTemporalReferenceSystem(name2, null);
        int[] calendarDate1 = {1900, 1, 1};
        int[] calendarDate2 = {400, 1, 1};
        CalendarDate referenceDate1 = new DefaultCalendarDate(
                frame1, IndeterminateValue.BEFORE, new SimpleInternationalString("Gregorian calendar"), calendarDate1);
        CalendarDate referenceDate2 = new DefaultCalendarDate(
                frame2, IndeterminateValue.NOW, new SimpleInternationalString("Babylonian calendar"), calendarDate2);
        JulianDate julianReference = new DefaultJulianDate(frame1, IndeterminateValue.NOW, 123456789);

        cal.set(1900, 0, 1);
        Instant begining1 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2000, 9, 17);
        Instant ending1 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2000, 1, 1);
        Instant begining2 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2012, 1, 1);
        Instant ending2 = new DefaultInstant(new DefaultPosition(cal.getTime()));

        Period epochOfUse1 = new DefaultPeriod(begining1, ending1);
        Period epochOfUse2 = new DefaultPeriod(begining2, ending2);

        calendarEra1 = new DefaultCalendarEra(
                new SimpleInternationalString("Cenozoic"),
                new SimpleInternationalString("no description"),
                referenceDate1,
                julianReference,
                epochOfUse1);
        calendarEra2 = new DefaultCalendarEra(
                new SimpleInternationalString("Mesozoic"),
                new SimpleInternationalString(""),
                referenceDate2,
                julianReference,
                epochOfUse2);
    }

    @After
    public void tearDown() {
        calendarEra1 = null;
        calendarEra2 = null;
    }

    /** Test of getName method, of class DefaultCalendarEra. */
    @Test
    public void testGetName() {
        InternationalString result = calendarEra1.getName();
        assertNotEquals(calendarEra2.getName(), result);
    }

    /** Test of getReferenceEvent method, of class DefaultCalendarEra. */
    @Test
    public void testGetReferenceEvent() {
        InternationalString result = calendarEra1.getReferenceEvent();
        assertNotEquals(calendarEra2.getReferenceEvent(), result);
    }

    /** Test of getReferenceDate method, of class DefaultCalendarEra. */
    @Test
    public void testGetReferenceDate() {
        CalendarDate result = calendarEra1.getReferenceDate();
        assertNotEquals(calendarEra2.getReferenceDate(), result);
    }

    /** Test of getJulianReference method, of class DefaultCalendarEra. */
    @Test
    public void testGetJulianReference() {
        JulianDate result = calendarEra1.getJulianReference();
        assertEquals(calendarEra2.getJulianReference(), result);
    }

    /** Test of getEpochOfUse method, of class DefaultCalendarEra. */
    @Test
    public void testGetEpochOfUse() {
        Period result = calendarEra1.getEpochOfUse();
        assertNotEquals(calendarEra2.getEpochOfUse(), result);
    }

    /** Test of setName method, of class DefaultCalendarEra. */
    @Test
    public void testSetName() {
        InternationalString result = calendarEra1.getName();
        ((DefaultCalendarEra) calendarEra1).setName(new SimpleInternationalString("new Era"));
        assertNotEquals(calendarEra1.getName(), result);
    }

    /** Test of setReferenceEvent method, of class DefaultCalendarEra. */
    @Test
    public void testSetReferenceEvent() {
        InternationalString result = calendarEra1.getReferenceEvent();
        ((DefaultCalendarEra) calendarEra1).setReferenceEvent(new SimpleInternationalString("new Era description"));
        assertNotEquals(calendarEra1.getReferenceEvent(), result);
    }

    /** Test of setReferenceDate method, of class DefaultCalendarEra. */
    @Test
    public void testSetReferenceDate() {
        CalendarDate result = calendarEra1.getReferenceDate();
        int[] date = {1950, 6, 10};
        ((DefaultCalendarEra) calendarEra1).setReferenceDate(new DefaultCalendarDate(null, null, null, date));
        assertNotEquals(calendarEra1.getReferenceDate(), result);
    }

    /** Test of setJulianReference method, of class DefaultCalendarEra. */
    @Test
    public void testSetJulianReference() {
        JulianDate result = calendarEra1.getJulianReference();
        ((DefaultCalendarEra) calendarEra1).setJulianReference(new DefaultJulianDate(null, null, 785410));
        assertNotEquals(calendarEra1.getJulianReference(), result);
    }

    /** Test of setEpochOfUse method, of class DefaultCalendarEra. */
    @Test
    public void testSetEpochOfUse() {
        Period result = calendarEra1.getEpochOfUse();
        cal.set(1900, 10, 10);
        ((DefaultCalendarEra) calendarEra1)
                .setEpochOfUse(new DefaultPeriod(
                        new DefaultInstant(new DefaultPosition(cal.getTime())),
                        new DefaultInstant(new DefaultPosition(new Date()))));
        assertNotEquals(calendarEra1.getEpochOfUse(), result);
    }

    /** Test of getDatingSystem method, of class DefaultCalendarEra. */
    @Test
    public void testGetDatingSystem() {
        Collection<org.geotools.api.temporal.Calendar> result = ((DefaultCalendarEra) calendarEra1).getDatingSystem();
        assertEquals(((DefaultCalendarEra) calendarEra2).getDatingSystem(), result);
    }

    /** Test of equals method, of class DefaultCalendarEra. */
    @Test
    public void testEquals() {
        assertNotEquals(null, calendarEra1);
        assertEquals(calendarEra1, calendarEra1);
        assertNotEquals(calendarEra1, calendarEra2);
    }

    /** Test of hashCode method, of class DefaultCalendarEra. */
    @Test
    public void testHashCode() {
        int result = calendarEra1.hashCode();
        assertNotEquals(calendarEra2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultCalendarEra. */
    @Test
    public void testToString() {
        String result = calendarEra1.toString();
        assertNotEquals(calendarEra2.toString(), result);
    }
}
