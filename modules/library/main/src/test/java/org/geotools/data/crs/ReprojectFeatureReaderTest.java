/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.crs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Contains tests for the reproject feature reader. */
public final class ReprojectFeatureReaderTest {

    @Test
    public void testReprojectWithUserData() throws Exception {
        // create a feature collection wit a single feature
        SimpleFeatureType featureType =
                DataUtilities.createType("feature", "id:string,geometry:Point:srid=4326");
        SimpleFeature feature = DataUtilities.createFeature(featureType, "1|POINT(1 2)");
        ListFeatureCollection features = new ListFeatureCollection(featureType);
        features.add(feature);
        // add some user data to the feature
        feature.getUserData().put("someKey", "someValue");
        // instantiate the reproject reader
        CoordinateReferenceSystem sphericalMercator = CRS.decode("EPSG:3857");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                DataUtilities.reader((SimpleFeatureCollection) features);
        int featuresCount = 0;
        try (ReprojectFeatureReader reprojected =
                new ReprojectFeatureReader(reader, sphericalMercator)) {
            // check that the feature was correctly reprojected
            SimpleFeature reprojectedFeature = reprojected.next();
            assertThat(reprojectedFeature, notNullValue());
            Point geometry = (Point) reprojectedFeature.getDefaultGeometry();
            checkDoubleValue(geometry.getX(), 222638.9816, 0.0001);
            checkDoubleValue(geometry.getY(), 111325.1429, 0.0001);
            // check that the user data was preserved
            assertThat(reprojectedFeature.getUserData().get("someKey"), is("someValue"));
            // increment the features counter
            featuresCount++;
        }
        // check that we iterate over the feature
        assertThat(featuresCount, is(1));
    }

    /** Compare two double values with an accepted error. */
    private void checkDoubleValue(double value, double expected, double error) {
        double difference = Math.abs(value - expected);
        assertThat(difference < error, is(true));
    }
}
