/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import junit.framework.TestCase;

public class MaxSimpleFeatureCollectionTest extends TestCase {

    SimpleFeatureCollection delegate;

    @Override
    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("geom", Point.class);
        tb.add("name", String.class);
        
        SimpleFeatureType featureType = tb.buildFeatureType();

        delegate = new DefaultFeatureCollection(null, featureType);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        for (int i = 0; i < 10; i++) {
            b.add(new GeometryFactory().createPoint(new Coordinate(i,i)));
            b.add(String.valueOf(i));
            delegate.add(b.buildFeature("fid." + i));
        }
    }

    public void testSize() {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        assertEquals(5, max.size());
        
        max = new MaxSimpleFeatureCollection(delegate, 7, 5);
        assertEquals(3, max.size());
    }

    public void testIsEmpty() {
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        assertFalse(max.isEmpty());
        
        max = new MaxSimpleFeatureCollection(delegate, 9, 5);
        assertFalse(max.isEmpty());
        
        max = new MaxSimpleFeatureCollection(delegate, 10, 5);
        assertTrue(max.isEmpty());

        max = new MaxSimpleFeatureCollection(delegate, 0, 0);
        assertTrue(max.isEmpty());
    }

    public void testIterator() {
        
        MaxSimpleFeatureCollection max = new MaxSimpleFeatureCollection(delegate, 5);
        SimpleFeatureIterator it = max.features();
        try {
            for (int i = 0; i < 5; i++) {
                assertTrue(it.hasNext());
                assertNotNull(it.next());
            }
            assertFalse(it.hasNext());
        }
        finally {
            it.close();
        }

        max = new MaxSimpleFeatureCollection(delegate, 7, 5);
        it = max.features();
        try {
            for (int i = 0; i < 3; i++) {
                assertTrue(it.hasNext());
                assertNotNull(it.next());
            }
            assertFalse(it.hasNext());
        }
        finally {
            it.close();
        }
    }
}
