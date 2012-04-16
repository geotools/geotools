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

import com.vividsolutions.jts.geom.MultiLineString;

/**
 * 
 * 
 * @source $URL$
 */
public class LRSSegmentProcessTest {
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
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs_bad", "to_lrs", 1.0,
                    2.0);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs_bad", 1.0,
                    2.0);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testnullParamFromLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, null, "to_lrs", 1.0, 2.0);
            Assert.fail("Expected error from bad from_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNullParamToLrs() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", null, 1.0, 2.0);
            Assert.fail("Expected error from bad to_lrs name");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadFromMeasure() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs_bad", null,
                    2.0);
            Assert.fail("Expected error from bad measure value");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testBadToMeasure() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        try {
            FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs_bad", 1.0,
                    null);
            Assert.fail("Expected error from bad measure value");
        } catch (ProcessException e) {
            // Successful
        }
    }

    @Test
    public void testNoFeaturesGiven() throws Exception {
        LRSSegmentProcess process = new LRSSegmentProcess();
        FeatureCollection origional = FeatureCollections.newCollection();

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", 1.0, 2.0);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSegmentInOneFeature() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", 0.5, 1.5);
        Assert.assertEquals(1, result.size());
        Feature feature = result.features().next();
        MultiLineString segment = (MultiLineString) feature.getDefaultGeometryProperty().getValue();
        Assert.assertEquals(2, segment.getNumPoints());
        Assert.assertEquals(0.5, segment.getCoordinates()[0].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[0].y, 0.0);
        Assert.assertEquals(1.5, segment.getCoordinates()[1].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[1].y, 0.0);
    }

    @Test
    public void testSegmentInTwoFeature() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", 0.5, 3.5);
        Assert.assertEquals(1, result.size());
        Feature feature = result.features().next();
        MultiLineString segment = (MultiLineString) feature.getDefaultGeometryProperty().getValue();
        Assert.assertEquals(3, segment.getNumPoints());
        Assert.assertEquals(0.5, segment.getCoordinates()[0].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[0].y, 0.0);
        Assert.assertEquals(3.5, segment.getCoordinates()[2].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[2].y, 0.0);
    }

    @Test
    public void testSegmentInThreeFeature() throws Exception {
        SimpleFeatureSource source = featureSource.getFeatureSource("lrssimple");
        LRSSegmentProcess process = new LRSSegmentProcess();
        SimpleFeatureCollection origional = source.getFeatures();

        FeatureCollection result = process.execute(origional, "from_lrs", "to_lrs", 0.5, 5.5);
        Assert.assertEquals(1, result.size());
        Feature feature = result.features().next();
        MultiLineString segment = (MultiLineString) feature.getDefaultGeometryProperty().getValue();
        Assert.assertEquals(4, segment.getNumPoints());
        Assert.assertEquals(0.5, segment.getCoordinates()[0].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[0].y, 0.0);
        Assert.assertEquals(5.5, segment.getCoordinates()[3].x, 0.0);
        Assert.assertEquals(0.0, segment.getCoordinates()[3].y, 0.0);
    }

}
