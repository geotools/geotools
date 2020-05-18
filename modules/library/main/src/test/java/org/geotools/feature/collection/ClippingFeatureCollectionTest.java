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
package org.geotools.feature.collection;

import junit.framework.TestCase;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ClippingFeatureCollectionTest extends TestCase {

    DefaultFeatureCollection delegate;

    @Override
    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("geom", Point.class);
        tb.add("name", String.class);

        SimpleFeatureType featureType = tb.buildFeatureType();

        delegate = new DefaultFeatureCollection(null, featureType);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        GeometryFactory factory = new GeometryFactory();
        for (int i = 0; i < 10; i++) {
            b.add(factory.createPoint(new Coordinate(i, i)));
            b.add(String.valueOf(i));
            SimpleFeature feature = b.buildFeature("fid." + i);
            if (i < 2) {
                // adding a Point that will not intersect the first
                // two features
                feature.getUserData()
                        .put(
                                Hints.GEOMETRY_CLIP,
                                factory.createPoint(new Coordinate(i + 1, i + 1)));
            }
            delegate.add(feature);
        }
    }

    @Test
    public void testClipping() {
        ClippingFeatureCollection clipping = new ClippingFeatureCollection(delegate);
        SimpleFeatureIterator it = clipping.features();
        try {
            int i = 0;
            while (it.hasNext()) {
                it.next();
                i++;
            }
            // first two points have been removed
            assertEquals(8, i);
        } finally {
            it.close();
        }
    }

    @Test
    public void testSize() {
        ClippingFeatureCollection clipping = new ClippingFeatureCollection(delegate);
        // first two points have been removed
        assertEquals(8, clipping.size());
    }
}
