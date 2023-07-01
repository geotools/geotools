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

import java.util.Collection;
import org.geotools.api.temporal.Calendar;
import org.geotools.api.temporal.Clock;
import org.geotools.api.temporal.ClockTime;
import org.geotools.api.temporal.IndeterminateValue;
import org.geotools.api.temporal.TemporalReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultClockTime;
import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultClockTest {

    private Clock clock1;
    private Clock clock2;

    @Before
    public void setUp() {
        NamedIdentifier name1 = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        NamedIdentifier name2 = new NamedIdentifier(Citations.CRS, "Julian calendar");
        TemporalReferenceSystem frame1 = new DefaultTemporalReferenceSystem(name1, null);
        TemporalReferenceSystem frame2 = new DefaultTemporalReferenceSystem(name2, null);
        Number[] clockTime1 = {0, 0, 0};
        Number[] clockTime2 = {12, 0, 0.0};
        ClockTime clocktime1 = new DefaultClockTime(frame1, null, clockTime1);
        ClockTime clocktime2 = new DefaultClockTime(frame2, null, clockTime2);
        ClockTime utcReference1 = new DefaultClockTime(frame1, null, clockTime1);
        ClockTime utcReference2 = new DefaultClockTime(frame2, null, clockTime2);
        clock1 =
                new DefaultClock(
                        name1,
                        null,
                        new SimpleInternationalString("reference event"),
                        clocktime1,
                        utcReference1);
        clock2 =
                new DefaultClock(
                        name2,
                        null,
                        new SimpleInternationalString("description"),
                        clocktime2,
                        utcReference2);
    }

    @After
    public void tearDown() {
        clock1 = null;
        clock2 = null;
    }

    /** Test of getReferenceEvent method, of class DefaultClock. */
    @Test
    public void testGetReferenceEvent() {
        InternationalString result = clock1.getReferenceEvent();
        assertNotEquals(clock2.getReferenceEvent(), result);
    }

    /** Test of getReferenceTime method, of class DefaultClock. */
    @Test
    public void testGetReferenceTime() {
        ClockTime result = clock1.getReferenceTime();
        assertNotEquals(clock2.getReferenceTime(), result);
    }

    /** Test of getUTCReference method, of class DefaultClock. */
    @Test
    public void testGetUTCReference() {
        ClockTime result = clock1.getUTCReference();
        assertNotEquals(clock2.getUTCReference(), result);
    }

    /** Test of clkTrans method, of class DefaultClock. */
    @Test
    public void testClkTrans() {
        // @TODO this method is not implemented yet!
    }

    /** Test of utcTrans method, of class DefaultClock. */
    @Test
    public void testUtcTrans() {
        // @TODO this method is not implemented yet!
    }

    /** Test of setReferenceEvent method, of class DefaultClock. */
    @Test
    public void testSetReferenceEvent() {
        InternationalString result = clock1.getReferenceEvent();
        ((DefaultClock) clock1).setReferenceEvent(new SimpleInternationalString(""));
        assertNotEquals(clock1.getReferenceEvent(), result);
    }

    /** Test of setReferenceTime method, of class DefaultClock. */
    @Test
    public void testSetReferenceTime() {
        ClockTime result = clock1.getReferenceTime();
        ((DefaultClock) clock1)
                .setReferenceTime(new DefaultClockTime(clock1, IndeterminateValue.UNKNOWN, null));
        assertNotEquals(clock1.getReferenceTime(), result);
    }

    /** Test of setUtcReference method, of class DefaultClock. */
    @Test
    public void testSetUtcReference() {
        ClockTime result = clock1.getUTCReference();
        ((DefaultClock) clock1)
                .setUtcReference(new DefaultClockTime(clock1, IndeterminateValue.UNKNOWN, null));
        assertNotEquals(clock1.getUTCReference(), result);
    }

    /** Test of getDateBasis method, of class DefaultClock. */
    @Test
    public void testGetDateBasis() {
        Collection<Calendar> result = ((DefaultClock) clock1).getDateBasis();
        assertEquals(((DefaultClock) clock2).getDateBasis(), result);
    }

    /** Test of equals method, of class DefaultClock. */
    @Test
    public void testEquals() {
        assertNotEquals(null, clock1);
        assertEquals(clock1, clock1);
        assertNotEquals(clock1, clock2);
    }

    /** Test of hashCode method, of class DefaultClock. */
    @Test
    public void testHashCode() {
        int result = clock1.hashCode();
        assertNotEquals(clock2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultClock. */
    @Test
    public void testToString() {
        String result = clock1.toString();
        assertNotEquals(clock2.toString(), result);
    }
}
