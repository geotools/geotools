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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.ClockTime;
import org.opengis.temporal.TemporalReferenceSystem;
import static org.junit.Assert.*;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultClockTimeTest {

    private ClockTime clockTime1;
    private ClockTime clockTime2;

    @Before
    public void setUp() {
        NamedIdentifier name = new NamedIdentifier(Citations.CRS, "Gregorian calendar");
        TemporalReferenceSystem frame = new DefaultTemporalReferenceSystem(name, null);
        Number[] clck1 = {8, 16, 25};
        Number[] clck2 = {15, 55, 1};
        clockTime1 = new DefaultClockTime(frame, null, clck1);
        clockTime2 = new DefaultClockTime(frame, null, clck2);
    }

    @After
    public void tearDown() {
        clockTime1 = null;
        clockTime2 = null;
    }

    /**
     * Test of getClockTime method, of class DefaultClockTime.
     */
    @Test
    public void testGetClockTime() {
        Number[] result = clockTime1.getClockTime();
        assertFalse(clockTime2.getClockTime().equals(result));
    }

    /**
     * Test of setClockTime method, of class DefaultClockTime.
     */
    @Test
    public void testSetClockTime() {
        Number[] result = clockTime1.getClockTime();
        Number[] clcktime = {14, 15, 0};
        ((DefaultClockTime) clockTime1).setClockTime(clcktime);
        assertFalse(clockTime1.getClockTime().equals(result));
    }

    /**
     * Test of equals method, of class DefaultClockTime.
     */
    @Test
    public void testEquals() {
        assertFalse(clockTime1.equals(null));
        assertEquals(clockTime1, clockTime1);
    }

    /**
     * Test of hashCode method, of class DefaultClockTime.
     */
    @Test
    public void testHashCode() {
        int result = clockTime1.hashCode();
        assertFalse(clockTime2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultClockTime.
     */
    @Test
    public void testToString() {
        String result = clockTime1.toString();
        assertFalse(clockTime2.equals(result));
    }
}
