/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Date;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONArrayIOTest {

    JSONArrayIO io = new JSONArrayIO();

    //   @Rule public ExpectedException exceptionRule = ExpectedException.none();
    private static TimeZone DEFAULT;

    @BeforeClass
    public static void setupTimeZone() {
        DEFAULT = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @AfterClass
    public static void resetTimeZone() {
        TimeZone.setDefault(DEFAULT);
    }

    @Test
    public void testParseValid() {
        String[] parsed = io.parse("[\"a\",\"b\",\"c\"]", String.class, null);
        assertArrayEquals(new String[] {"a", "b", "c"}, parsed);
    }

    @Test
    public void testParseNestedObject() {
        assertThrows(
                java.lang.IllegalArgumentException.class,
                () -> {
                    String[] parsed =
                            io.parse("[\"a\",\"b\", { \"key\" = \"value\"}]", String.class, null);
                });
    }

    @Test
    public void testParseIntegers() {
        Integer[] parsed = io.parse("[1, 2, 3]", Integer.class, null);
        assertArrayEquals(new Integer[] {1, 2, 3}, parsed);
    }

    @Test
    public void testDates() {
        Date[] parsed =
                io.parse("[\"2000-11-10\",\"2002-08-09\",\"2003-05-20\"]", Date.class, null);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTime(parsed[0]);
        assertEquals(2000, c.get(Calendar.YEAR));
        assertEquals(10, c.get(Calendar.MONTH)); // month field is zero based
        assertEquals(10, c.get(Calendar.DAY_OF_MONTH));
        c.setTime(parsed[1]);
        assertEquals(2002, c.get(Calendar.YEAR));
        assertEquals(7, c.get(Calendar.MONTH)); // month field is zero based
        assertEquals(9, c.get(Calendar.DAY_OF_MONTH));
        c.setTime(parsed[2]);
        assertEquals(2003, c.get(Calendar.YEAR));
        assertEquals(4, c.get(Calendar.MONTH)); // month field is zero based
        assertEquals(20, c.get(Calendar.DAY_OF_MONTH));
    }
}
