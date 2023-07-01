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

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.Period;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultPeriodTest {

    private Period period1;
    private Period period2;

    @Before
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, 1, 1);
        Instant begining1 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2000, 1, 1);
        Instant ending1 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2000, 1, 1);
        Instant begining2 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        cal.set(2012, 1, 1);
        Instant ending2 = new DefaultInstant(new DefaultPosition(cal.getTime()));
        period1 = new DefaultPeriod(begining1, ending1);
        period2 = new DefaultPeriod(begining2, ending2);
    }

    @After
    public void tearDown() {
        period1 = null;
        period2 = null;
    }

    /** Test of getBeginning method, of class DefaultPeriod. */
    @Test
    public void testGetBeginning() {
        Instant result = period1.getBeginning();
        assertNotEquals(period2.getBeginning(), result);
    }

    /** Test of setBegining method, of class DefaultPeriod. */
    @Test
    public void testSetBegining_Instant() {
        Instant result = period1.getBeginning();
        Instant newInstant = new DefaultInstant(new DefaultPosition(new Date()));
        ((DefaultPeriod) period1).setBegining(newInstant);
        assertNotEquals(period1.getBeginning(), result);
    }

    /** Test of setBegining method, of class DefaultPeriod. */
    @Test
    public void testSetBegining_Date() {
        Date result = period1.getBeginning().getPosition().getDate();
        ((DefaultPeriod) period1).setBegining(new Date());
        assertNotEquals(period1.getBeginning().getPosition().getDate(), result);
    }

    /** Test of getEnding method, of class DefaultPeriod. */
    @Test
    public void testGetEnding() {
        Instant result = period1.getEnding();
        assertNotEquals(period2.getEnding(), result);
    }

    /** Test of setEnding method, of class DefaultPeriod. */
    @Test
    public void testSetEnding_Instant() {
        Instant result = period1.getEnding();
        Instant newInstant = new DefaultInstant(new DefaultPosition(new Date()));
        ((DefaultPeriod) period1).setEnding(newInstant);
        assertNotEquals(period1.getEnding(), result);
    }

    /** Test of setEnding method, of class DefaultPeriod. */
    @Test
    public void testSetEnding_Date() {
        Date result = period1.getEnding().getPosition().getDate();
        ((DefaultPeriod) period1).setEnding(new Date());
        assertNotEquals(period1.getEnding().getPosition().getDate(), result);
    }

    /** Test of equals method, of class DefaultPeriod. */
    @Test
    public void testEquals() {
        assertNotEquals(null, period1);
        assertEquals(period1, period1);
        assertNotEquals(period1, period2);
    }

    /** Test of hashCode method, of class DefaultPeriod. */
    @Test
    public void testHashCode() {
        int result = period1.hashCode();
        assertNotEquals(period2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultPeriod. */
    @Test
    public void testToString() {
        String result = period1.toString();
        assertNotEquals(period2.toString(), result);
    }
}
