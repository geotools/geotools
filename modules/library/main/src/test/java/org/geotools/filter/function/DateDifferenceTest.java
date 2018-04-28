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

import java.util.Date;
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
}
