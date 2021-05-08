/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.FilteredIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class MemoryFeatureCollectionTest extends DataTestCase {
    private MemoryFeatureCollection roads;

    @Override
    public void init() throws Exception {
        super.init();
        roads = new MemoryFeatureCollection(roadType);
        roads.addAll(Arrays.asList(roadFeatures));
    }

    @Test
    public void testAdd() {
        MemoryFeatureCollection rivers = new MemoryFeatureCollection(riverType);
        for (SimpleFeature riverFeature : riverFeatures) {
            rivers.add(riverFeature);
        }
        assertEquals(riverFeatures.length, rivers.size());
    }

    @Test
    public void testAddAll() {
        MemoryFeatureCollection rivers = new MemoryFeatureCollection(riverType);
        rivers.addAll(Arrays.asList(riverFeatures));
    }

    @Test
    public void testSize() {
        assertEquals(roadFeatures.length, roads.size());
    }

    @Test
    @SuppressWarnings("PMD.UseTryWithResources") // tests behavior post closing
    public void testResources() {
        Object[] array = roads.toArray();
        assertEquals(roads.size(), array.length);

        SimpleFeatureIterator i = roads.features();
        try {
            assertTrue(i.hasNext());
        } finally {
            i.close();
        }
        try {
            assertFalse(i.hasNext());
            fail("should be closed");
        } catch (IllegalStateException closed) {
        }
        i = roads.features();
        try {
            assertTrue(i.hasNext());
        } finally {
            i.close();
        }
        try {
            assertFalse(i.hasNext());
            fail("should be closed");
        } catch (IllegalStateException closed) {
        }
    }

    @Test
    public void testBounds() {
        MemoryFeatureCollection rivers = new MemoryFeatureCollection(riverType);
        ReferencedEnvelope expected = new ReferencedEnvelope();
        for (SimpleFeature riverFeature : riverFeatures) {
            rivers.add(riverFeature);
            expected.include(riverFeature.getBounds());
        }
        assertEquals(riverFeatures.length, rivers.size());

        // Should not throw an UnsupportedOperationException
        assertNotNull(rivers.getBounds());
        assertEquals(expected, rivers.getBounds());
    }

    /** This feature collection is still implementing Collection so we best check it works */
    @Test
    public void testIterator() throws Exception {
        int count = 0;
        Iterator<SimpleFeature> it = roads.iterator();
        try {
            while (it.hasNext()) {
                @SuppressWarnings("unused")
                SimpleFeature feature = it.next();
                count++;
            }
        } finally {
            DataUtilities.close(it);
        }
        assertEquals(roads.size(), count);

        count = 0;
        try (FilteredIterator<SimpleFeature> filteredIterator =
                new FilteredIterator<>(roads, rd12Filter)) {
            while (filteredIterator.hasNext()) {
                @SuppressWarnings("unused")
                SimpleFeature feature = filteredIterator.next();
                count++;
            }
        }
        assertEquals(expected(rd12Filter), count);
    }

    @Test
    public void testSubCollection() {
        int count = 0;
        try (SimpleFeatureIterator it = roads.features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (rd12Filter.evaluate(feature)) {
                    count++;
                }
            }
        }
        SimpleFeatureCollection sub = roads.subCollection(rd12Filter);
        assertEquals(count, sub.size());
    }

    @Test
    public void testSubSubCollection() {
        SimpleFeatureCollection sub = roads.subCollection(rd12Filter);
        SimpleFeatureCollection subsub = sub.subCollection(rd1Filter);
        assertEquals(1, subsub.size());
    }

    public void XtestSort() {
        //        FeatureList fList = roads.sort(SortBy.NATURAL_ORDER);
        //        for (Object obj : fList) {
        //            System.out.println(obj);
        //        }
    }
}
