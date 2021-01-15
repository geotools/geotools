/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.spatial.IsValidGeometryValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * ValidationProcessorTest purpose.
 *
 * <p>Description of ValidationProcessorTest ...
 *
 * <p>Capabilities:
 *
 * <ul>
 * </ul>
 *
 * Example Use:
 *
 * <pre><code>
 * ValidationProcessorTest x = new ValidationProcessorTest(...);
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class ValidationProcessorTest extends DataTestCase {
    MemoryDataStore store;

    ValidationProcessor processor;
    RoadNetworkValidationResults results;

    @Rule public TestName testName = new TestName();

    /*
     * @see TestCase#setUp()
     */
    public void init() throws Exception {
        super.init();
        store = new MemoryDataStore();
        store.addFeatures(roadFeatures);
        store.addFeatures(riverFeatures);
        processor = new ValidationProcessor();
        results = new RoadNetworkValidationResults();
    }

    /*
     * @see TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
        store = null;
        super.tearDown();
    }

    @Test
    public void testIsValidFeatureValidation() throws Exception {
        IsValidGeometryValidation geom = new IsValidGeometryValidation();
        geom.setName("IsValidGeometry");
        geom.setDescription("IsValid geomtry test for Junit Test +" + testName.getMethodName());
        geom.setTypeRef("*");
        processor.addValidation(geom);

        // test the correct roads
        processor.runFeatureTests(
                "dataStoreId", DataUtilities.collection(this.roadFeatures), results);
        assertEquals(0, results.getFailedMessages().length);

        // test the broken road
        // make an incorrect line
        try {
            this.newRoad =
                    SimpleFeatureBuilder.build(
                            this.roadType,
                            new Object[] {Integer.valueOf(2), line(new int[] {1, 2, 1, 2}), "r4"},
                            "road.rd4");
        } catch (IllegalAttributeException e) {
        }

        SimpleFeature[] singleRoad = new SimpleFeature[1];
        singleRoad[0] = this.newRoad;
        SimpleFeatureCollection features = DataUtilities.collection(singleRoad);
        processor.runFeatureTests("dataStoreId", features, results);
        assertTrue(results.getFailedMessages().length > 0);

        // run integrity tests
        // make a map of FeatureSources
        Map<String, SimpleFeatureSource> map = new HashMap<>();
        String[] typeNames = this.store.getTypeNames();
        for (String typeName : typeNames) map.put(typeName, this.store.getFeatureSource(typeName));
        map.put("newThing", this.store.getFeatureSource(typeNames[0]));

        processor.runIntegrityTests(null, map, null, results);
        assertTrue(results.getFailedMessages().length > 0);
        /*
        String[] messages = validationResults.getFailedMessages();
        for (int i=0; i<validationResults.getFailedMessages().length; i++)
        	// System.out.println(messages[i]);
        */

    }
}
