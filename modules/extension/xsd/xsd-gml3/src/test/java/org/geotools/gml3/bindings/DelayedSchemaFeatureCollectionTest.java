/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import org.geotools.data.DataTestCase;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.Test;

public class DelayedSchemaFeatureCollectionTest extends DataTestCase {

    public DelayedSchemaFeatureCollectionTest() {
        super(DelayedSchemaFeatureCollectionTest.class.getSimpleName());
    }

    @Test
    public void testEmpty() {
        DelayedSchemaFeatureCollection fc = new DelayedSchemaFeatureCollection();
        assertEquals(DelayedSchemaFeatureCollection.PLACEHOLDER, fc.getSchema());
        assertTrue(fc.getBounds().isEmpty());
        fc.add(riverFeatures[0]);
        assertFalse(fc.getBounds().isEmpty());
        assertEquals(riverType, fc.getSchema());
    }

    @Test
    public void testTwoTypes() {
        DelayedSchemaFeatureCollection fc = new DelayedSchemaFeatureCollection();
        fc.add(riverFeatures[0]);
        fc.add(lakeFeatures[0]);
        // the type is the one of the first feature
        assertEquals(riverType, fc.getSchema());
        // but we can get both un-modified
        try (SimpleFeatureIterator it = fc.features()) {
            assertEquals(riverFeatures[0], it.next());
            assertEquals(lakeFeatures[0], it.next());
            assertFalse(it.hasNext());
        }
    }
}
