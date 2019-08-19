/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Test;
import org.opengis.filter.expression.Function;

public class LiterateTest {

    FilterFactoryImpl ff = new FilterFactoryImpl();

    @Test
    public void testLiterate() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(4, 3, 2, 1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list1", list1);
        map.put("list2", list2);

        Function exp =
                ff.function(
                        "literate",
                        ff.property("index"),
                        ff.function("size", ff.property("list1")),
                        ff.multiply(
                                ff.function("litem", ff.property("list1"), ff.property("index")),
                                ff.function("litem", ff.property("list2"), ff.property("index"))));
        Object value = exp.evaluate(map);
        assertTrue(value instanceof List);
        assertEquals(Arrays.asList(4.0, 6.0, 6.0, 4.0), ((List<?>) value));
    }
}
