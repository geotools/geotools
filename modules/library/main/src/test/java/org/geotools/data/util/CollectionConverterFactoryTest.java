/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class CollectionConverterFactoryTest {

    @Test
    public void testCollectionToCollection() throws Exception {
        List<Integer> source = new ArrayList<>();
        source.add(1);
        source.add(2);

        Object converted =
                CollectionConverterFactory.CollectionToCollection.convert(source, Set.class);

        Assert.assertTrue(converted instanceof Set);
        Set target = (Set) converted;
        Assert.assertTrue(target.contains(1));
        Assert.assertTrue(target.contains(2));
    }

    @Test
    public void testCollectionToArray() throws Exception {
        List<Integer> source = new ArrayList<>();
        source.add(1);
        source.add(2);

        Object converted =
                CollectionConverterFactory.CollectionToArray.convert(source, Integer[].class);
        Assert.assertTrue(converted instanceof Integer[]);
        Integer[] target = (Integer[]) converted;
        Assert.assertEquals(Integer.valueOf(1), target[0]);
        Assert.assertEquals(Integer.valueOf(2), target[1]);
    }

    @Test
    public void testArrayToCollection() throws Exception {
        Integer[] source = {1, 2};

        Object converted = CollectionConverterFactory.ArrayToCollection.convert(source, List.class);
        Assert.assertTrue(converted instanceof List);
        List target = (List) converted;
        Assert.assertEquals(Integer.valueOf(1), target.get(0));
        Assert.assertEquals(Integer.valueOf(2), target.get(1));
    }

    @Test
    public void testArrayToArray() throws Exception {
        Integer[] source = {1, 2};

        Object converted = CollectionConverterFactory.ArrayToArray.convert(source, Number[].class);
        Assert.assertTrue(converted instanceof Number[]);
        Number[] target = (Number[]) converted;
        Assert.assertEquals(1, target[0].intValue());
        Assert.assertEquals(2, target[1].intValue());
    }
}
