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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.test.TestData;
import org.geotools.validation.dto.ArgumentDTO;
import org.geotools.validation.dto.PlugInDTO;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;
import org.geotools.validation.xml.XMLReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * ValidationProcessor Runs validation tests against Features and reports the outcome of the tests.
 *
 * <p>The validation processor contains two main data structures. Each one is a HashMap of
 * ArrayLists that hold Validations. The first one, featureLookup, holds per-feature validation
 * tests (tests that operate on one feature at a time with no knowledge of any other features. The
 * second one, integrityLookup, holds integrity validations (validations that span multiple features
 * and/or multiple feature types).
 *
 * <p>Each HashMap of validations is hashed with a key whose value is a FeatureTypeName. This key
 * provides access to an ArrayList of validations that are to be performed on this FeatureTypeInfo.
 *
 * <p>Validations are added via the two addValidation() methods.
 *
 * <p>The validations are run when runFeatureTests() and runIntegrityTests() are called. It is
 * recommended that the user call runFeatureTests() before runIntegrityTests() as it is usually the
 * case that integrity tests are much more time consuming. If a Feature is incorrect, it can
 * probably be detected early on, and quickly, in the feature validation tests.
 *
 * <p>For validations that are performed on every FeatureTypeInfo, a value called ANYTYPENAME has
 * been created and can be stored in the validationLookup tables if a validation specifies that it
 * is run against all FeatureTypes. The value that causes a validation to be run against all
 * FeatureTypes is null. Or Validation.ALL
 *
 * <p>Results of the validation tests are handled using a Visitor pattern. This visitor is a
 * ValidationResults object that is passed into the runFeatureTests() and runIntegrityTests()
 * methods. Each individual validation will record error messages in the ValidationResults visitor.
 *
 * <p>Example Use:
 *
 * <pre><code>
 * ValidationProcessor processor = new ValidationProcessor();<br>
 * processor.addValidation(FeatureValidation1);<br>
 * processor.addValidation(FeatureValidation2);<br>
 * processor.addValidation(IntegrityValidation1);<br>
 * processor.addValidation(FeatureValidation3);<br>
 * <p>
 * processor.runFeatureTests(FeatureTypeInfo, Feature, ValidationResults);<br>
 * processor.runIntegrityTests(layers, Envelope, ValidationResults);<br>
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class ValidationProcessor {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ValidationProcessor.class);

    /** Magic key used to hold the place of any featureType */
    final Object ANYTYPENAME = new Object();

    /**
     * Stores Lists of FeatureTests by featureType.
     *
     * <p>Map of ArrayLists by featureType (the lists contain FeatureValidation instances)
     */
    protected Map featureLookup;

    /**
     * Stores Lists of IntegrityValidation by featureType.
     *
     * <p>A Map with featureTypes as keys that map to array lists of integrity validation tests.
     *
     * <p>How are tests that map against all FeatureTypes stored?
     */
    protected Map integrityLookup; //

    /** List of feature types that have been modified. */
    protected ArrayList modifiedFeatureTypes;

    /**
     * ValidationProcessor constructor.
     *
     * <p>Initializes the data structure to hold the validations.
     */
    public ValidationProcessor() {
        featureLookup = new HashMap();
        integrityLookup = new HashMap();
    }

    /**
     * addToLookup
     *
     * <p>Description: Add the validation test to the map for every featureType that it validates.
     * If the FeatureTypes array is ALL, then the validation is added to the ANYTYPENAME entry.
     */
    private void addToFVLookup(FeatureValidation validation) {
        String[] featureTypeList = validation.getTypeRefs();

        if (featureTypeList == Validation.ALL) // if null (ALL)
        {
            ArrayList tests = (ArrayList) featureLookup.get(ANYTYPENAME);

            if (tests == null) { // if an ALL test doesn't exist yet
                tests = new ArrayList(); // create it
            }

            tests.add(validation);
            featureLookup.put(ANYTYPENAME, tests); // add the ALL test to it
        } else // a non ALL FeatureTypeInfo validation
        {
            for (int i = 0; i < featureTypeList.length; i++) {
                ArrayList tests = (ArrayList) featureLookup.get(featureTypeList[i]);

                if (tests == null) { // if this FeatureTypeInfo doesn't have a validation test yet
                    tests = new ArrayList(); // put it in the list
                }

                tests.add(validation);
                featureLookup.put(featureTypeList[i], tests); // add a validation to it
            }
        }
    }

    /**
     * addToIVLookup
     *
     * <p>Add the validation test to the map for every featureType that it validates. If the
     * FeatureTypes array is ALL, then the validation is added to the ANYTYPENAME entry.
     */
    private void addToIVLookup(IntegrityValidation validation) {
        String[] integrityTypeList = validation.getTypeRefs();

        if (integrityTypeList == Validation.ALL) // if null (ALL)
        {
            ArrayList tests = (ArrayList) integrityLookup.get(ANYTYPENAME);

            if (tests == null) { // if an ALL test doesn't exist yet
                tests = new ArrayList(); // create it
            }

            tests.add(validation);
            integrityLookup.put(ANYTYPENAME, tests); // add the ALL test to it
        } else {
            for (int i = 0; i < integrityTypeList.length; i++) {
                ArrayList tests = (ArrayList) integrityLookup.get(integrityTypeList[i]);

                if (tests == null) { // if this FeatureTypeInfo doesn't have a validation test yet
                    tests = new ArrayList(); // put it in the list
                }

                tests.add(validation);
                integrityLookup.put(integrityTypeList[i], tests); // add a validation to it
            }
        }
    }

    /**
     * addValidation
     *
     * <p>Add a FeatureValidation to the list of Feature tests
     */
    public void addValidation(FeatureValidation validation) {
        addToFVLookup((FeatureValidation) validation);
    }

    /**
     * addValidation
     *
     * <p>Add an IntegrityValidation to the list of Integrity tests
     */
    public void addValidation(IntegrityValidation validation) {
        addToIVLookup((IntegrityValidation) validation);
    }

    /**
     * getDependencies purpose.
     *
     * <p>Gets all the FeatureTypes that this FeatureTypeInfo uses.
     *
     * @param typeName the FeatureTypeName
     * @return all the FeatureTypes that this FeatureTypeInfo uses.
     */
    public Set getDependencies(String typeName) {
        ArrayList validations = (ArrayList) integrityLookup.get(typeName);
        HashSet s = new HashSet();

        if (validations != null) {
            for (int i = 0; i < validations.size(); i++) // for each validation
            {
                String[] types = ((Validation) validations.get(i)).getTypeRefs();

                for (int j = 0; j < types.length; j++) // for each FeatureTypeInfo
                s.add(types[j]); // add it to the list
            }
        }

        return s;
    }

    /**
     * runFeatureTests Change: Uses a SimpleFeatureIterator now instead of a FeatureCollection.
     *
     * <p>Performs a lookup on the FeatureTypeInfo name to determine what FeatureTests need to be
     * performed. Once these tests are gathered, they are run on each feature in the
     * FeatureCollection. The first validation test lookup checks to see if there are any
     * validations that are to be performed on every FeatureTypeInfo. An example of this could be an
     * isValid() test on all geometries in all FeatureTypes. Once those tests have been gathered, a
     * lookup is performed on the TypeName of the FeatureTypeInfo to check for specific
     * FeatureTypeInfo validation tests. A list of validation tests is returned from each lookup, if
     * any exist. When all the validation tests have been gathered, each test is iterated through
     * then run on each Feature, with the ValidationResults coming along for the ride, collecting
     * error information. Parameter "SimpleFeatureCollection collection" should be changed later to
     * take in a SimpleFeatureSource so not everything is loaded into memory.
     *
     * @param dsID data Store id.
     * @param collection The collection of features, of a particulare FeatureTypeInfo "type", that
     *     are to be validated.
     * @param results Storage for the results of the validation tests.
     * @throws Exception FeatureValidations throw Exceptions
     */
    public void runFeatureTests(
            String dsID, SimpleFeatureCollection collection, ValidationResults results)
            throws Exception {
        SimpleFeatureType type = collection.getSchema();

        // check for any tests that are to be performed on ALL features
        ArrayList tests = (ArrayList) featureLookup.get(ANYTYPENAME);

        // check for any FeatureTypeInfo specific tests
        String typeRef = dsID + ":" + type.getTypeName();
        ArrayList FT_tests = (ArrayList) featureLookup.get(typeRef);

        // append featureType specific tests to the list of tests
        if (FT_tests != null) {
            if (tests != null) {
                Iterator it = FT_tests.iterator();

                while (it.hasNext()) tests.add((FeatureValidation) it.next());
            } else {
                tests = FT_tests;
            }
        }

        if (tests != null) // if we found some tests to be performed on this FeatureTypeInfo
        {
            SimpleFeatureIterator features = collection.features();
            try {
                while (features.hasNext()) // iterate through each feature and run the test on it
                {
                    SimpleFeature feature = (SimpleFeature) features.next();

                    // for each test that is to be performed on this feature
                    for (int i = 0; i < tests.size(); i++) {
                        FeatureValidation validator = (FeatureValidation) tests.get(i);
                        results.setValidation(validator);
                        try {
                            validator.validate(feature, type, results);
                        } catch (Throwable e) {
                            results.error(feature, e.getMessage());
                        }
                    }
                }
            } finally {
                features.close();
            }
        }
    }

    /**
     * runIntegrityTests
     *
     * <p>Performs a lookup on the FeatureTypeInfo name to determine what IntegrityTests need to be
     * performed. Once these tests are gathered, they are run on the collection features in the
     * Envelope, defined by a SimpleFeatureSource (not a FeatureCollection!). The first validation
     * test lookup checks to see if there are any validations that are to be performed on every
     * FeatureTypeInfo. An example of this could be a uniqueID() test on a unique column value in
     * all FeatureTypes. Once those tests have been gathered, a lookup is performed on the TypeName
     * of the FeatureTypeInfo to check for specific Integrity validation tests. A list of validation
     * tests is returned from each lookup, if any exist. When all the validation tests have been
     * gathered, each test is iterated through then run on each Feature, with the ValidationResults
     * coming along for the ride, collecting error information.
     *
     * @param typeRefs List of modified features, or null to use stores.keySet()
     * @param stores the Map of effected features (Map of key=typeRef, value="featureSource"
     * @param envelope The bounding box that contains all modified Features
     * @param results Storage for the results of the validation tests.
     * @throws Exception Throws an exception if the HashMap contains a value that is not a
     *     FeatureSource
     */
    public void runIntegrityTests(
            Set<Name> typeRefs, Map stores, ReferencedEnvelope envelope, ValidationResults results)
            throws Exception {
        if ((integrityLookup == null) || (integrityLookup.size() == 0)) {
            LOGGER.fine("No tests defined by integrityLookup - validation not needed");

            return;
        }

        LOGGER.fine("Starting validation tests for:" + typeRefs);
        LOGGER.fine("Marshalled " + stores.size() + " FeatureSources for testing");
        LOGGER.fine("Testing limited to " + envelope);

        if (typeRefs == null) {
            LOGGER.finer("Using default typeRegs for stores");
            typeRefs = stores.keySet();
        } else if (typeRefs.isEmpty()) {
            LOGGER.finer("Validation test abandond - nothing was modified");
        }

        // convert each HashMap element into FeatureSources
        //
        List tests = new ArrayList();

        // check for any tests that are to be performed on ALL features
        //
        LOGGER.finer("Finding tests for everybody");

        List anyTests = (List) integrityLookup.get(ANYTYPENAME);

        if ((anyTests != null) && !anyTests.isEmpty()) {
            tests.addAll(anyTests);
        }

        LOGGER.finer("Found " + tests.size() + " tests (so far)");

        // for each modified FeatureTypeInfo
        //
        LOGGER.finer("Finding tests for modified typeRefs");

        for (Name name : typeRefs) {
            String typeRef = typeRef(name);

            LOGGER.finer("Finding tests for typeRef:" + typeRef);

            List moreTests = (List) integrityLookup.get(typeRef);

            if ((moreTests != null) && !moreTests.isEmpty()) {
                tests.addAll(moreTests);
            }
        }

        if (tests.isEmpty()) {
            LOGGER.finer("Validation test abandond - no tests found to run");

            return;
        }

        LOGGER.finer("Validation test about to run - " + tests.size() + " tests found");

        for (Iterator j = tests.iterator(); j.hasNext(); ) {
            IntegrityValidation validator = (IntegrityValidation) j.next();

            LOGGER.finer("Running test:" + validator.getName());
            results.setValidation(validator);

            try {
                boolean success = validator.validate(stores, envelope, results);

                if (!success) {
                    results.error(null, "Was not successful");
                }
            } catch (Throwable e) {
                LOGGER.finer("Validation test died:" + validator.getName());

                String error = e.getClass().getName();

                if (e.getMessage() != null) {
                    error += (" - " + e.getMessage());
                }

                LOGGER.log(Level.WARNING, validator.getName() + " failed with " + error, e);
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                results.error(null, error);
            }
        }
    }
    /** Convert a Name to a type reference (namespace ":" name) */
    protected String typeRef(Name name) {
        return name.getNamespaceURI() + ":" + name.getLocalPart();
    }

    protected static final Set queryPlugInNames(Map testSuiteDTOs) {
        Set plugInNames = new HashSet();

        Iterator i = testSuiteDTOs.keySet().iterator();

        // go through each test suite
        // and gather up all the required plugInNames
        //
        while (i.hasNext()) {
            String testSuite = (String) i.next();
            TestSuiteDTO dto = (TestSuiteDTO) testSuiteDTOs.get(testSuite);
            Iterator j = dto.getTests().keySet().iterator();

            // go through each test plugIn
            //
            while (j.hasNext()) {
                TestDTO tdto = (TestDTO) dto.getTests().get(j.next());
                plugInNames.add(tdto.getPlugIn().getName());
            }
        }

        return plugInNames;
    }

    /**
     * Load testsuites from provided directories.
     *
     * <p>This is mostly useful for testing, you may want to write your own load method with
     * enhanced error reporting.
     */
    public void load(File plugins, File testsuites) throws Exception {
        Map pluginDTOs = XMLReader.loadPlugIns(TestData.file(this, "plugins"));
        Map testSuiteDTOs =
                XMLReader.loadValidations(TestData.file(this, "validation"), pluginDTOs);
        load(testSuiteDTOs, pluginDTOs);
    }

    /**
     * Populates this validation processor against the provided DTO objects.
     *
     * <p>This method is useful for testing, it is not forgiving and will error out if things go
     * bad.
     */
    public void load(Map plugInDTOs, Map testSuiteDTOs) throws Exception {
        // step 1 make a list required plug-ins
        //
        Set plugInNames = queryPlugInNames(testSuiteDTOs);

        // step 2 set up real plug-ins
        // configured with defaults
        //
        // (This is a map of PlugIn by name)
        Map plugIns = new HashMap(plugInNames.size());

        // go through each plugIn
        //
        for (Iterator i = plugInNames.iterator(); i.hasNext(); ) {
            String plugInName = (String) i.next();
            PlugInDTO dto = (PlugInDTO) plugInDTOs.get(plugInName);
            Class plugInClass = null;

            plugInClass = Class.forName(dto.getClassName());

            if (plugInClass == null) {
                throw new ClassNotFoundException(
                        "Could class for "
                                + plugInName
                                + ": class "
                                + dto.getClassName()
                                + " not found");
            }

            Map plugInArgs = dto.getArgs();

            if (plugInArgs == null) {
                plugInArgs = new HashMap();
            }

            PlugIn plugIn = new PlugIn(plugInName, plugInClass, dto.getDescription(), plugInArgs);
            plugIns.put(plugInName, plugIn);
        }

        // step 3
        // set up tests and add to processor
        //
        for (Iterator i = testSuiteDTOs.keySet().iterator(); i.hasNext(); ) {
            TestSuiteDTO tdto = (TestSuiteDTO) testSuiteDTOs.get(i.next());
            Iterator j = tdto.getTests().keySet().iterator();

            // for each TEST in the test suite
            while (j.hasNext()) {
                TestDTO dto = (TestDTO) tdto.getTests().get(j.next());

                // deal with test
                Map testArgs = dto.getArgs();

                if (testArgs == null) {
                    testArgs = new HashMap();
                } else {
                    Map m = new HashMap();
                    Iterator k = testArgs.keySet().iterator();

                    while (k.hasNext()) {
                        ArgumentDTO adto = (ArgumentDTO) testArgs.get(k.next());
                        m.put(adto.getName(), adto.getValue());
                    }

                    testArgs = m;
                }

                PlugIn plugIn =
                        (org.geotools.validation.PlugIn) plugIns.get(dto.getPlugIn().getName());
                Validation validation =
                        plugIn.createValidation(dto.getName(), dto.getDescription(), testArgs);

                if (validation instanceof FeatureValidation) {
                    addValidation((FeatureValidation) validation);
                }

                if (validation instanceof IntegrityValidation) {
                    addValidation((IntegrityValidation) validation);
                }
            }
        } // end each test suite
    } // load method
}
