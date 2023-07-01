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

import javax.measure.Unit;
import javax.measure.quantity.Time;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.temporal.IntervalLength;
import si.uom.SI;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultIntervalLengthTest {

    private IntervalLength intervalLength1;
    private IntervalLength intervalLength2;

    @Before
    public void setUp() {
        Unit<Time> unit1 = SI.SECOND, unit2 = SI.SECOND.multiply(3600);
        int radix1 = 10, radix2 = 10;
        int factor1 = 3, factor2 = 6;
        int value1 = 7, value2 = 12;
        intervalLength1 = new DefaultIntervalLength(unit1, radix1, factor1, value1);
        intervalLength2 = new DefaultIntervalLength(unit2, radix2, factor2, value2);
    }

    @After
    public void tearDown() {
        intervalLength1 = null;
        intervalLength2 = null;
    }

    /** Test of getUnit method, of class DefaultIntervalLength. */
    @Test
    public void testGetUnit() {
        Unit result = intervalLength1.getUnit();
        assertNotEquals(intervalLength2.getUnit(), result);
    }

    /** Test of getRadix method, of class DefaultIntervalLength. */
    @Test
    public void testGetRadix() {
        int result = intervalLength1.getRadix();
        assertEquals(intervalLength2.getRadix(), result);
    }

    /** Test of getFactor method, of class DefaultIntervalLength. */
    @Test
    public void testGetFactor() {
        int result = intervalLength1.getFactor();
        assertNotEquals(intervalLength2.getFactor(), result);
    }

    /** Test of getValue method, of class DefaultIntervalLength. */
    @Test
    public void testGetValue() {
        int result = intervalLength1.getValue();
        assertNotEquals(intervalLength2.getValue(), result);
    }

    /** Test of equals method, of class DefaultIntervalLength. */
    @Test
    public void testEquals() {
        assertNotEquals(null, intervalLength1);
        assertEquals(intervalLength1, intervalLength1);
        assertNotEquals(intervalLength1, intervalLength2);
    }

    /** Test of hashCode method, of class DefaultIntervalLength. */
    @Test
    public void testHashCode() {
        int result = intervalLength1.hashCode();
        assertNotEquals(intervalLength2.hashCode(), result);
    }

    /** Test of toString method, of class DefaultIntervalLength. */
    @Test
    public void testToString() {
        String result = intervalLength1.toString();
        assertNotEquals(intervalLength2.toString(), result);
    }
}
