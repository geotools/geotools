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

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
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

    /**
     * Test of getBeginning method, of class DefaultPeriod.
     */
    @Test
    public void testGetBeginning() {
        Instant result = period1.getBeginning();
        assertFalse(period2.getBeginning().equals(result));
    }

    /**
     * Test of setBegining method, of class DefaultPeriod.
     */
    @Test
    public void testSetBegining_Instant() {
        Instant result = period1.getBeginning();
        Instant newInstant = new DefaultInstant(new DefaultPosition(new Date()));
        ((DefaultPeriod) period1).setBegining(newInstant);
        assertFalse(period1.getBeginning().equals(result));
    }

    /**
     * Test of setBegining method, of class DefaultPeriod.
     */
    @Test
    public void testSetBegining_Date() {
        Date result = period1.getBeginning().getPosition().getDate();
        ((DefaultPeriod) period1).setBegining(new Date());
        assertFalse(period1.getBeginning().getPosition().getDate().equals(result));
    }

    /**
     * Test of getEnding method, of class DefaultPeriod.
     */
    @Test
    public void testGetEnding() {
        Instant result = period1.getEnding();
        assertFalse(period2.getEnding().equals(result));
    }

    /**
     * Test of setEnding method, of class DefaultPeriod.
     */
    @Test
    public void testSetEnding_Instant() {
        Instant result = period1.getEnding();
        Instant newInstant = new DefaultInstant(new DefaultPosition(new Date()));
        ((DefaultPeriod) period1).setEnding(newInstant);
        assertFalse(period1.getEnding().equals(result));
    }

    /**
     * Test of setEnding method, of class DefaultPeriod.
     */
    @Test
    public void testSetEnding_Date() {
        Date result = period1.getEnding().getPosition().getDate();
        ((DefaultPeriod) period1).setEnding(new Date());
        assertFalse(period1.getEnding().getPosition().getDate().equals(result));
    }

    /**
     * Test of equals method, of class DefaultPeriod.
     */
    @Test
    public void testEquals() {
        assertFalse(period1.equals(null));
        assertEquals(period1, period1);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test of hashCode method, of class DefaultPeriod.
     */
    @Test
    public void testHashCode() {
        int result = period1.hashCode();
        assertFalse(period2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultPeriod.
     */
    @Test
    public void testToString() {
        String result = period1.toString();
        assertFalse(period2.toString().equals(result));
    }
}
