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

import org.geotools.data.DataTestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.spatial.IsValidGeometryValidation;


/**
 * FeatureValidationTest purpose.
 * 
 * <p>
 * Description of FeatureValidationTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class FeatureValidationTest extends DataTestCase {
    MemoryDataStore store;

    /**
     * FeatureValidationTest constructor.
     * 
     * <p>
     * Run test <code>testName</code>.
     * </p>
     *
     * @param testName
     */
    public FeatureValidationTest(String testName) {
        super(testName);
    }

    /**
     * Construct data store for use.
     *
     * @throws Exception
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
     * @throws Exception
     *
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        store = null;
        super.tearDown();
    }

    public void testIsValidFeatureValidation() {
        // the visitor
        RoadValidationResults validationResults = new RoadValidationResults();

        IsValidGeometryValidation validator = new IsValidGeometryValidation();
        validator.setName("isValidRoad");
        validator.setDescription("Tests to see if a road is valid");
        validator.setTypeRef( "*" );
        validationResults.setValidation(validator);
        assertTrue(validator.validate(this.newRoad, this.roadType,
                validationResults));

        try {
            this.newRoad = SimpleFeatureBuilder.build(this.roadType,(new Object[] {
                        new Integer(2), line(new int[] { 1, 2, 1, 2 }), "r4"
                    }), "road.rd4");
        } catch (IllegalAttributeException e) {
        }

        assertTrue(!validator.validate(this.newRoad, this.roadType,
                validationResults)); // validate will return false
    }

    /**
     * testABunchOfValidations purpose.
     * 
     * <p>
     * Description ...
     * </p>
     */
    public void testABunchOfValidations() {
        // the visitor
        RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();

        // various FeatureType tests
        IsValidGeometryValidation isValidValidator1 = new IsValidGeometryValidation();
        isValidValidator1.setName("isValidRoad");
        isValidValidator1.setDescription("Tests to see if a road is valid");
        isValidValidator1.setTypeRef( "roads" );
        
        IsValidGeometryValidation isValidValidator2 = new IsValidGeometryValidation();
        isValidValidator2.setName("isValidRail");
        isValidValidator2.setDescription("Tests to see if a railway is valid");
        isValidValidator2.setTypeRef( "rails" );
        
        IsValidGeometryValidation isValidValidator3 = new IsValidGeometryValidation();
        isValidValidator3.setName("isValidRiver");
        isValidValidator3.setDescription("Tests to see if a river is valid");
        isValidValidator3.setTypeRef( "rivers" );
        
        IsValidGeometryValidation isValidValidator4 = new IsValidGeometryValidation();
        isValidValidator4.setName("isValidAll");
        isValidValidator4.setDescription("Tests to see if all geometries are valid");
        isValidValidator4.setTypeRef( "*" );
        
        // various Integrity tests
        //
        //
        //
        // tell the RoadValidator what tests to use
        roadValidationResults.setValidation(isValidValidator1);
        roadValidationResults.setValidation(isValidValidator2);
        roadValidationResults.setValidation(isValidValidator3);
        roadValidationResults.setValidation(isValidValidator4);

        // run each feature validation test on the featureTypes it tests
        String[] types1 = isValidValidator1.getTypeNames();

        for (int i = 0; i < types1.length; i++) {
            //isValidValidator1.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
        }

        String[] types2 = isValidValidator1.getTypeNames();

        for (int i = 0; i < types2.length; i++) {
            //isValidValidator2.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
        }

        String[] types3 = isValidValidator1.getTypeNames();

        for (int i = 0; i < types3.length; i++) {
            //isValidValidator3.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
        }

        String[] types4 = isValidValidator1.getTypeNames();

        for (int i = 0; i < types4.length; i++) {
            //isValidValidator4.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
        }

        // check the results of the roadValidator
        String[] roadFailures = roadValidationResults.getFailedMessages();
        String[] roadWarnings = roadValidationResults.getWarningMessages();
        boolean roadsPassed = true;

        if (roadFailures.length != 0) {
            roadsPassed = false;

            for (int i = 0; i < roadFailures.length; i++) {
                System.out.println(roadFailures[i]);
            }
        }

        if (roadWarnings.length != 0) {
            for (int i = 0; i < roadWarnings.length; i++) {
                System.out.println(roadWarnings[i]);
            }
        }

        assertTrue(roadsPassed);
    }
}
