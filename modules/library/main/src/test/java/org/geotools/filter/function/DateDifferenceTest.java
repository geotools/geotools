/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

public class DateDifferenceTest {

    @Test
    public void testDifference() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Date d1 = new Date();
        Date d2 = new Date(d1.getTime() + 10);
        Function function = ff.function("dateDifference", ff.literal(d2), ff.literal(d1));
        assertEquals(10, function.evaluate(null, Integer.class), 0d);
    }

    @Test
    public void testDifferenceWithUnit() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Date d1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(d1);

        // Adding 1 hour and getting the difference in minutes
        calendar.add(Calendar.HOUR, 1);
        Date d2 = calendar.getTime();
        Function function =
                ff.function("dateDifference", ff.literal(d2), ff.literal(d1), ff.literal("m"));
        assertEquals(60, function.evaluate(null, Integer.class), 0d);

        // Adding 5 days and getting the difference in days
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date d3 = calendar.getTime();
        function = ff.function("dateDifference", ff.literal(d3), ff.literal(d2), ff.literal("d"));
        assertEquals(5, function.evaluate(null, Integer.class), 0d);

        // Adding 1 second and getting the default difference
        calendar.add(Calendar.SECOND, 1);
        Date d4 = calendar.getTime();
        function = ff.function("dateDifference", ff.literal(d4), ff.literal(d3));
        assertEquals(1000, function.evaluate(null, Integer.class), 0d);
    }

    @Test
    public void testDifferenceWithNow() throws InterruptedException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function function = ff.function("now");

        // Get the current time
        long now = function.evaluate(null, Long.class);
        Date d1 = new Date();
        d1.setTime(now);

        // let's waste a second :)
        Thread.sleep(1000);
        function =
                ff.function("dateDifference", ff.function("now"), ff.literal(d1), ff.literal("s"));

        // Make sure that the difference in seconds is greater than 1
        assertTrue(function.evaluate(null, Integer.class) >= 1);
    }
}
