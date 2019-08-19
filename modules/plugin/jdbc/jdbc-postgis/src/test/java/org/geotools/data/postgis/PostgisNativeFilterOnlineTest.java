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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.util.FeatureStreams;
import org.geotools.jdbc.JDBCNativeFilterOnlineTest;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.expression.Function;

public final class PostgisNativeFilterOnlineTest extends JDBCNativeFilterOnlineTest {

    @Override
    protected PostgisNativeFilterTestSetup createTestSetup() {
        return new PostgisNativeFilterTestSetup(new PostGISTestSetup());
    }

    @Override
    protected NativeFilter getNativeFilter() {
        return filterFactory.nativeFilter("(TYPE = 'temperature' OR TYPE = 'wind') AND value > 15");
    }

    /** Check pgNearest filter with 1 result */
    @Test
    public void testNearestNativeFilterFirst() throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("gt_jdbc_test_measurements"));
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = dataStore.getGeometryFactory();
        Function nearest =
                ff.function(
                        "pgNearest",
                        ff.literal(gf.createPoint(new Coordinate(0, 0))),
                        ff.literal(1));
        Query q =
                new Query(tname("gt_jdbc_test_measurements"), ff.equals(nearest, ff.literal(true)));
        SimpleFeature feature = DataUtilities.first(fs.getFeatures(q));
        assertEquals("POINT (1 2)", feature.getDefaultGeometryProperty().getValue().toString());
    }

    /** Check pgNearest filter with 3 results */
    @Test
    public void testNearestNativeFilterNumResults() throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("gt_jdbc_test_measurements"));
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = dataStore.getGeometryFactory();
        Function nearest =
                ff.function(
                        "pgNearest",
                        ff.literal(gf.createPoint(new Coordinate(0, 0))),
                        ff.literal(3));
        Query q =
                new Query(tname("gt_jdbc_test_measurements"), ff.equals(nearest, ff.literal(true)));
        ContentFeatureCollection fc = fs.getFeatures(q);
        try (Stream<SimpleFeature> featuresStream = FeatureStreams.toFeatureStream(fc)) {
            List<SimpleFeature> featuresList = featuresStream.collect(Collectors.toList());
            assertEquals(3, featuresList.size());
            assertEquals(
                    "POINT (1 2)",
                    featuresList.get(0).getDefaultGeometryProperty().getValue().toString());
            assertEquals(
                    "POINT (2 2)",
                    featuresList.get(1).getDefaultGeometryProperty().getValue().toString());
            assertEquals(
                    "POINT (1 4)",
                    featuresList.get(2).getDefaultGeometryProperty().getValue().toString());
        }
    }
}
