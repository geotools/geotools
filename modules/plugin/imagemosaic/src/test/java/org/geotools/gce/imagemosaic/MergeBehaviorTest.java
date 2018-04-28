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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class MergeBehaviorTest {

    @Test
    public void testGetValuesAsString() {
        String[] values = MergeBehavior.valuesAsStrings();
        assertNotNull(values);
        Set<String> testSet = new HashSet<>(Arrays.asList(values));
        Set<String> expectedSet =
                Arrays.stream(MergeBehavior.values())
                        .map(v -> v.name())
                        .collect(Collectors.toSet());
        assertEquals(expectedSet, testSet);
    }
}
