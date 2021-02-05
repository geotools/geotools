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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.function.Predicate;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

public class PostGIS3DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new PostGIS3DTestSetup(new PostGISTestSetup());
    }

    public void testForce2DHint() throws Exception {

        Query q = new Query(tname(getLine3d()));
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(getLine3d()));
        // no force2D hint, tests that Z is present
        checkGeometryDimension(q, fs, c -> !Double.isNaN(c.getZ()));
        Hints hints = new Hints(Hints.FEATURE_2D, true);
        q.setHints(hints);

        // now 2D should be forced, tests Z is NaN
        checkGeometryDimension(q, fs, c -> Double.isNaN(c.getZ()));
    }

    private void checkGeometryDimension(
            Query q, ContentFeatureSource fs, Predicate<Coordinate> testCondition)
            throws IOException {
        SimpleFeatureCollection fc = fs.getFeatures(q);
        try (FeatureIterator<SimpleFeature> fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                Geometry geom = (Geometry) f.getDefaultGeometry();
                Coordinate[] coors = geom.getCoordinates();
                for (Coordinate c : coors) {
                    assertTrue(testCondition.test(c));
                }
            }
        }
    }
}
