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
package org.geotools.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.geotools.util.Converter;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;

public class ArrayConverterFactoryTest {
    ArrayConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ArrayConverterFactory();
    }

    @Test
    public void testArrayToSingle() throws Exception {
        String[] array = new String[] {"text"};
        Converter converter = factory.createConverter(array.getClass(), String.class, new Hints());
        assertEquals("text", converter.convert(array, String.class));
    }

    @Test
    public void testArrayToSingleWrongType() throws Exception {
        String[] array = new String[] {"text"};
        Converter converter = factory.createConverter(array.getClass(), Integer.class, new Hints());
        assertNull(converter);
    }

    @Test
    public void testArrayToSingleTooManyElements() throws Exception {
        String[] array = new String[] {"text1", "text2"};
        Converter converter = factory.createConverter(array.getClass(), String.class, new Hints());
        assertNull(converter.convert(array, String.class));
    }

    @Test
    public void testSingleToArray() throws Exception {
        String single = "text";
        Converter converter =
                factory.createConverter(single.getClass(), String[].class, new Hints());
        assertEquals(1, converter.convert(single, String[].class).length);
        assertEquals("text", converter.convert(single, String[].class)[0]);
    }

    @Test
    public void testSingleToArrayWrongType() throws Exception {
        String single = "1";
        Converter converter =
                factory.createConverter(single.getClass(), Integer[].class, new Hints());
        assertNull(converter);
    }

    @Test
    public void testArrayToArray() throws Exception {
        String[] array = new String[] {"1", "2", "3"};
        Converter converter =
                factory.createConverter(array.getClass(), Integer[].class, new Hints());
        assertEquals(3, converter.convert(array, Integer[].class).length);
        assertEquals((Integer) 1, converter.convert(array, Integer[].class)[0]);
        assertEquals((Integer) 2, converter.convert(array, Integer[].class)[1]);
        assertEquals((Integer) 3, converter.convert(array, Integer[].class)[2]);
    }

    @Test
    public void testArrayToArrayPrimitive() throws Exception {
        String[] array = new String[] {"1", "2", "3"};
        Converter converter =
                factory.createConverter(array.getClass(), Integer[].class, new Hints());
        assertEquals(3, converter.convert(array, int[].class).length);
        assertEquals((int) 1, converter.convert(array, int[].class)[0]);
        assertEquals((int) 2, converter.convert(array, int[].class)[1]);
        assertEquals((int) 3, converter.convert(array, int[].class)[2]);
    }
}
