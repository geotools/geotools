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

import java.util.HashMap;
import org.geotools.data.DataTestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.validation.attributes.UniqueFIDValidation;

/**
 * IntegrityValidationTest purpose.
 *
 * <p>Description of IntegrityValidationTest ...
 *
 * <p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class IntegrityValidationTest extends DataTestCase {
    MemoryDataStore store;

    /**
     * FeatureValidationTest constructor.
     *
     * <p>Run test <code>testName</code>.
     */
    public IntegrityValidationTest(String testName) {
        super(testName);
    }

    /**
     * Construct data store for use.
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        store = new MemoryDataStore();
        store.addFeatures(roadFeatures);
        store.addFeatures(riverFeatures);
    }

    /**
     * Override tearDown.
     *
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        store = null;
        super.tearDown();
    }

    public void testUniqueFIDIntegrityValidation() throws Exception {
        // the visitor
        RoadValidationResults validationResults = new RoadValidationResults();

        UniqueFIDValidation validator = new UniqueFIDValidation();
        validator.setName("isValidRoad");
        validator.setDescription("Tests to see if a road is valid");
        validator.setTypeRef("*");
        validationResults.setValidation(validator);

        HashMap layers = new HashMap();
        layers.put("road", store.getFeatureSource("road"));
        layers.put("river", store.getFeatureSource("river"));

        assertTrue(
                validator.validate(layers, null, validationResults)); // validate will return true
    }
}
