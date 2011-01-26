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

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.temporal.Position;
import static org.junit.Assert.*;
import org.opengis.temporal.TemporalPosition;
import org.opengis.util.InternationalString;


/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultPositionTest {

    private Position position1;
    private Position position2;

    @Before
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(1981, 6, 25);
        position1 = new DefaultPosition(cal.getTime());
        cal.set(2012, 0, 1);
        position2 = new DefaultPosition(cal.getTime());
    }

    @After
    public void tearDown() {
        position1 = null;
        position2 = null;
    }

    /**
     * Test of anyOther method, of class DefaultPosition.
     */
    @Test
    public void testAnyOther() {
        TemporalPosition result = position1.anyOther();
        assertEquals(position2.anyOther(), result);
    }

    /**
     * Test of getDate method, of class DefaultPosition.
     */
    @Test
    public void testGetDate() {
        Date result = position1.getDate();
        assertFalse(position2.getDate().equals(result));
    }
  

    /**
     * Test of getTime method, of class DefaultPosition.
     */
    @Test
    public void testGetTime() {
        Time result = position1.getTime();
        assertEquals(position2.getTime(), result);
    }

    /**
     * Test of getDateTime method, of class DefaultPosition.
     */
    @Test
    public void testGetDateTime() {
        InternationalString result = position1.getDateTime();
        assertFalse(position2.getDateTime().equals(result));
    }

    /**
     * Test of equals method, of class DefaultPosition.
     */
    @Test
    public void testEquals() {
        assertFalse(position1.equals(null));
        assertEquals(position1, position1);
        assertFalse(position1.equals(position2));
    }

    /**
     * Test of hashCode method, of class DefaultPosition.
     */
    @Test
    public void testHashCode() {
        int result = position1.hashCode();
        assertFalse(position2.hashCode() == result);
    }

    /**
     * Test of toString method, of class DefaultPosition.
     */
    @Test
    public void testToString() {
        String result = position1.toString();
        assertFalse(position2.toString().equals(result));
    }
}
