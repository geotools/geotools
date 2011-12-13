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
package org.geotools.data.store;

import java.util.Iterator;

import org.geotools.feature.collection.MaxSimpleFeatureCollection;

/**
 * 
 *
 * @source $URL$
 */
public class MaxFeaturesFeatureCollectionTest extends
		FeatureCollectionWrapperTestSupport {

    public void testSize() throws Exception {
        // in the common case it's as big as the max
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 2);
        assertEquals(2, max.size());

        // however if we skip much it's going to be just as big as the remainder
        max = new MaxSimpleFeatureCollection(delegate, delegate.size() - 1, 10);
        assertEquals(1, max.size());
        
        // and if we skip more than the size
        max = new MaxSimpleFeatureCollection(delegate, delegate.size() + 1, 10);
        assertEquals(0, max.size());
    }

    public void testIteratorMax() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 2);
        Iterator i = max.iterator();
        for (int x = 0; x < 2; x++) {
            assertTrue(i.hasNext());
            i.next();
        }

        assertFalse(i.hasNext());
    }
    
    public void testIteratorSkipMax() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, delegate.size() - 1, 2);
        Iterator i = max.iterator();
        assertTrue(i.hasNext());
        i.next();
        assertFalse(i.hasNext());
    }
    
    public void testIteratorSkipMoreSize() throws Exception {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, delegate.size() + 1, 2);
        Iterator i = max.iterator();
        assertFalse(i.hasNext());
    }
}
