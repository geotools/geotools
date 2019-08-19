/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.windbarbs;

import org.junit.Assert;
import org.junit.Test;
import systems.uom.common.USCustomary;

/** @author Simone Giannecchini, GeoSolutions */
public class SpeedConverterTest extends Assert {

    @Test
    public void test() {

        // 1 m/s
        double speed = SpeedConverter.toKnots(1, "m/s");
        assertEquals(1.94384449, speed, 1E-6);

        // -1 m/s
        speed = SpeedConverter.toKnots(-1, "m/s");
        assertEquals(-1.94384449, speed, 1E-6);

        // 100 cm/s
        speed = SpeedConverter.toKnots(100, "cm/s");
        assertEquals(1.94384449, speed, 1E-6);

        // 10 cm/s
        speed = SpeedConverter.toKnots(10, "cm/s");
        assertEquals(.194384449, speed, 1E-6);

        // knot to knot
        speed = SpeedConverter.toKnots(10, "kn");
        assertEquals(10, speed, 1E-6);

        // knot to knot
        speed = SpeedConverter.toKnots(10, "kts");
        assertEquals(10, speed, 1E-6);

        // mph
        speed = SpeedConverter.toKnots(1, "mph");
        assertEquals(0.868976242, speed, 1E-6);

        // km/h
        speed = SpeedConverter.toKnots(1, "km/h");
        assertEquals(0.539956803, speed, 1E-6);

        // NaN
        speed = SpeedConverter.toKnots(Double.NaN, "km/h");
        assertEquals(Double.NaN, speed, 1E-6);

        // infinite
        try {
            speed = SpeedConverter.toKnots(Double.NEGATIVE_INFINITY, "km/h");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            speed = SpeedConverter.toKnots(Double.POSITIVE_INFINITY, "km/h");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }

        // feet/s
        speed = SpeedConverter.toKnots(1, USCustomary.FOOT.toString() + "/s");
        assertEquals(0.592483801, speed, 1E-6);

        try {
            speed = SpeedConverter.toKnots(1, "a/s");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
