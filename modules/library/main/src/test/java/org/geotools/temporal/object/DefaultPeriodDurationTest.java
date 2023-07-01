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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.geotools.util.SimpleInternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.temporal.PeriodDuration;
import org.geotools.api.util.InternationalString;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultPeriodDurationTest {

    private PeriodDuration periodDuration1;
    private PeriodDuration periodDuration2;

    @Before
    public void setUp() {
        periodDuration1 =
                new DefaultPeriodDuration(
                        new SimpleInternationalString("5"),
                        new SimpleInternationalString("2"),
                        new SimpleInternationalString("1"),
                        new SimpleInternationalString("12"),
                        new SimpleInternationalString("15"),
                        new SimpleInternationalString("5"),
                        new SimpleInternationalString("23"));
        periodDuration2 = new DefaultPeriodDuration(1535148449548L);
    }

    @After
    public void tearDown() {
        periodDuration1 = null;
        periodDuration2 = null;
    }

    /** Test of getDesignator method, of class DefaultPeriodDuration. */
    @Test
    public void testGetDesignator() {
        InternationalString result = periodDuration1.getDesignator();
        assertEquals(periodDuration2.getDesignator(), result);
    }

    /** Test of getYears method, of class DefaultPeriodDuration. */
    @Test
    public void testGetYears() {
        InternationalString result = periodDuration1.getYears();
        assertNotEquals(periodDuration2.getYears(), result);
    }

    /** Test of getMonths method, of class DefaultPeriodDuration. */
    @Test
    public void testGetMonths() {
        InternationalString result = periodDuration1.getMonths();
        assertNotEquals(periodDuration2.getMonths(), result);
    }

    /** Test of getDays method, of class DefaultPeriodDuration. */
    @Test
    public void testGetDays() {
        InternationalString result = periodDuration1.getDays();
        assertNotEquals(periodDuration2.getDays(), result);
    }

    /** Test of getTimeIndicator method, of class DefaultPeriodDuration. */
    @Test
    public void testGetTimeIndicator() {
        InternationalString result = periodDuration1.getTimeIndicator();
        assertEquals(periodDuration2.getTimeIndicator(), result);
    }

    /** Test of getHours method, of class DefaultPeriodDuration. */
    @Test
    public void testGetHours() {
        InternationalString result = periodDuration1.getHours();
        assertNotEquals(periodDuration2.getHours(), result);
    }

    /** Test of getMinutes method, of class DefaultPeriodDuration. */
    @Test
    public void testGetMinutes() {
        InternationalString result = periodDuration1.getMinutes();
        assertNotEquals(periodDuration2.getMinutes(), result);
    }

    /** Test of getSeconds method, of class DefaultPeriodDuration. */
    @Test
    public void testGetSeconds() {
        InternationalString result = periodDuration1.getSeconds();
        assertNotEquals(periodDuration2.getSeconds(), result);
    }

    /** Test of setYears method, of class DefaultPeriodDuration. */
    @Test
    public void testSetYears() {
        InternationalString result = periodDuration1.getYears();
        ((DefaultPeriodDuration) periodDuration1).setYears(new SimpleInternationalString("12"));
        assertNotEquals(periodDuration1.getYears(), result);
    }

    /** Test of setMonths method, of class DefaultPeriodDuration. */
    @Test
    public void testSetMonths() {
        InternationalString result = periodDuration1.getMonths();
        ((DefaultPeriodDuration) periodDuration1).setMonths(new SimpleInternationalString("13"));
        assertNotEquals(periodDuration1.getMonths(), result);
    }

    /** Test of setDays method, of class DefaultPeriodDuration. */
    @Test
    public void testSetDays() {
        InternationalString result = periodDuration1.getDays();
        ((DefaultPeriodDuration) periodDuration1).setDays(new SimpleInternationalString("14"));
        assertNotEquals(periodDuration1.getDays(), result);
    }

    /** Test of setHours method, of class DefaultPeriodDuration. */
    @Test
    public void testSetHours() {
        InternationalString result = periodDuration1.getHours();
        ((DefaultPeriodDuration) periodDuration1).setHours(new SimpleInternationalString("1"));
        assertNotEquals(periodDuration1.getHours(), result);
    }

    /** Test of setMinutes method, of class DefaultPeriodDuration. */
    @Test
    public void testSetMinutes() {
        InternationalString result = periodDuration1.getMinutes();
        ((DefaultPeriodDuration) periodDuration1).setMinutes(new SimpleInternationalString("4"));
        assertNotEquals(periodDuration1.getMinutes(), result);
    }

    /** Test of setSeconds method, of class DefaultPeriodDuration. */
    @Test
    public void testSetSeconds() {
        InternationalString result = periodDuration1.getSeconds();
        ((DefaultPeriodDuration) periodDuration1).setSeconds(new SimpleInternationalString("3"));
        assertNotEquals(periodDuration1.getSeconds(), result);
    }

    /** Test of getTimeInMillis method, of class DefaultPeriodDuration. */
    @Test
    public void testGetTimeInMillis() {
        long result = ((DefaultPeriodDuration) periodDuration1).getTimeInMillis();
        assertNotEquals(((DefaultPeriodDuration) periodDuration2).getTimeInMillis(), result);
    }

    /** Test of equals method, of class DefaultPeriodDuration. */
    @Test
    public void testEquals() {
        assertNotEquals(null, periodDuration1);
        assertEquals(periodDuration1, periodDuration1);
        assertNotEquals(periodDuration1, periodDuration2);
    }

    /** Test of hashCode method, of class DefaultPeriodDuration. */
    @Test
    public void testHashCode() {
        int result = periodDuration1.hashCode();
        assertNotEquals(periodDuration2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultPeriodDuration. */
    @Test
    public void testToString() {
        String result = periodDuration1.toString();
        assertNotEquals(periodDuration2.toString(), result);
    }
}
