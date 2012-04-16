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
package org.geotools.process.feature.gs;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.process.ProcessException;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * 
 * @source $URL$
 */
public class LRSMeasureProcessTest {
    private DataStore featureSource;

    private final GeometryFactory geometryFactory = new GeometryFactory();

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
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        try {
            FeatureCollection result = process.execute(origional, "from_lrs_bad", "to_lrs", point,
                    null);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs_bad", point,
                    null);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testnullParamFromLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        try {
            FeatureCollection result = process.execute(origional, null, "to_lrs", point, null);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNullParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", null, point, null);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadPoint() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs_bad", null,
                    null);
            Assert.fail("Expected error from bad measure value");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNoFeaturesGiven() throws Exception {
        LRSMeasureProcess process = new LRSMeasureProcess();
        FeatureCollection origional = FeatureCollections.newCollection();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", point, null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGoodMeasure() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSMeasureProcess process = new LRSMeasureProcess();
        SimpleFeatureCollection origional = source.getFeatures();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 0.0));

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", point, null);
        Assert.assertEquals(1, result.size());
        Feature feature = result.features().next();
        Assert.assertNotNull(feature.getProperty("lrs_measure"));
        Assert.assertNotNull(feature.getProperty("lrs_measure").getValue());
        Double measure = (Double) feature.getProperty("lrs_measure").getValue();
        Assert.assertEquals(1.0, measure, 0.0);
    }

}
