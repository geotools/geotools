/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import java.io.File;
import java.io.IOException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessException;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

public class LRSGeocodeProcessTest {
    private DataStore featureSource;

    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null);
        featureSource = new PropertyDataStore(file);
    }

    @After
    public void tearDown() {
        featureSource.dispose();
    }

    @Test
    public void testBadParamFromLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            process.execute(origional, "from_lrs_bad", "to_lrs", 1.0);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            process.execute(origional, "from_lrs", "to_lrs_bad", 1.0);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testnullParamFromLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            process.execute(origional, null, "to_lrs", 1.0);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNullParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            process.execute(origional, "from_lrs", null, 1.0);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadMeasure() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            process.execute(origional, "from_lrs", "to_lrs_bad", null);
            Assert.fail("Expected error from bad measure value");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNoFeaturesGiven() throws Exception {
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        DefaultFeatureCollection origional = new DefaultFeatureCollection();

        FeatureCollection<? extends FeatureType, ? extends Feature> result =
                process.execute(origional, "from_lrs", "to_lrs", 1.0);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGoodGeocode() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSGeocodeProcess process = new LRSGeocodeProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", 1.0);
        Assert.assertEquals(1, result.size());
        Feature feature = result.features().next();
        Point point = (Point) feature.getDefaultGeometryProperty().getValue();
        Assert.assertEquals(1.0, point.getX(), 0.0);
        Assert.assertEquals(0.0, point.getY(), 0.0);
    }
}
