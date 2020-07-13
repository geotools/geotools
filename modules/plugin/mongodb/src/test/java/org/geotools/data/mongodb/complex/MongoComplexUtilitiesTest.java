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
package org.geotools.data.mongodb.complex;

import static org.junit.Assert.assertTrue;

import com.mongodb.BasicDBList;
import java.util.Collections;
import org.junit.Test;

public class MongoComplexUtilitiesTest {

    /**
     * Checks no exception occurs when an empty list is evaluated by the complex utilities get value
     * method.
     */
    @Test
    public void testGetValueEmptyList() {
        final BasicDBList list = new BasicDBList();
        Object value = MongoComplexUtilities.getValue(list, Collections.emptyMap(), "path1.path2");
        assertTrue(value == null);
    }
}
