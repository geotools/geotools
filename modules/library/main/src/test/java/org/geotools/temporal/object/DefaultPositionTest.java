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

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import org.geotools.api.temporal.Position;
import org.geotools.api.temporal.TemporalPosition;
import org.geotools.api.util.InternationalString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Mehdi Sidhoum (Geomatys) */
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

    /** Test of anyOther method, of class DefaultPosition. */
    @Test
    public void testAnyOther() {
        TemporalPosition result = position1.anyOther();
        assertEquals(position2.anyOther(), result);
    }

    /** Test of getDate method, of class DefaultPosition. */
    @Test
    public void testGetDate() {
        Date result = position1.getDate();
        assertNotEquals(position2.getDate(), result);
    }

    /** Test of getTime method, of class DefaultPosition. */
    @Test
    public void testGetTime() {
        Time result = position1.getTime();
        assertEquals(position2.getTime(), result);
    }

    /** Test of getDateTime method, of class DefaultPosition. */
    @Test
    public void testGetDateTime() {
        InternationalString result = position1.getDateTime();
        assertNotEquals(position2.getDateTime(), result);
    }

    /** Test of equals method, of class DefaultPosition. */
    @Test
    public void testEquals() {
        assertNotEquals(null, position1);
        assertEquals(position1, position1);
        assertNotEquals(position1, position2);
    }

    /** Test of hashCode method, of class DefaultPosition. */
    @Test
    public void testHashCode() {
        int result = position1.hashCode();
        assertNotEquals(position2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultPosition. */
    @Test
    public void testToString() {
        String result = position1.toString();
        assertNotEquals(position2.toString(), result);
    }
}
