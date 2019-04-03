/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersOnlineTest;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

public class PostgisSpatialFiltersOnlineTest extends JDBCSpatialFiltersOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

    /** Check pgNearest filter with 1 result */
    @Test
    public void testNearestNativeFilterFirst() throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("road"));
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = dataStore.getGeometryFactory();
        Function nearest =
                ff.function(
                        "pgNearest",
                        ff.literal(gf.createPoint(new Coordinate(-0.5, -0.5))),
                        ff.literal(1));
        Query q = new Query(tname("road"), ff.equals(nearest, ff.literal(true)));
        SimpleFeature feature = DataUtilities.first(fs.getFeatures(q));
        assertEquals(0, (int) feature.getAttribute("id"));
    }

    /** Check pgNearest filter with 2 results */
    @Test
    public void testNearestNativeFilterNumResults() throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("road"));
        FilterFactory ff = dataStore.getFilterFactory();
        GeometryFactory gf = dataStore.getGeometryFactory();
        Function nearest =
                ff.function(
                        "pgNearest",
                        ff.literal(gf.createPoint(new Coordinate(-0.5, -0.5))),
                        ff.literal(2));
        Query q = new Query(tname("road"), ff.equals(nearest, ff.literal(true)));
        ContentFeatureCollection fc = fs.getFeatures(q);
        try (Stream<SimpleFeature> featuresStream = FeatureStreams.toFeatureStream(fc)) {
            List<SimpleFeature> featuresList = featuresStream.collect(Collectors.toList());
            assertEquals(2, featuresList.size());
            assertEquals(0, (int) featuresList.get(0).getAttribute("id"));
            assertEquals(1, (int) featuresList.get(1).getAttribute("id"));
        }
    }
}
