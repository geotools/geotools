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
package org.geotools.geometry.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.geotools.geometry.iso.PrecisionModel;

/**
 * the {@code GeometryTest} class is a container that holds a {@code List} of {@code
 * GeometryTestCase}s and provides a way to execute them all.
 *
 * @author <a href="mailto:joel@lggi.com">Joel Skelton</a>
 */
public class GeometryTestContainer {
    private List<GeometryTestCase> testCases;
    private PrecisionModel precisionModel;

    /** Constructor */
    public GeometryTestContainer() {
        testCases = new ArrayList<GeometryTestCase>();
    }

    /** Adds a constructed test case into the list of available tests */
    public void addTestCase(GeometryTestCase testCase) {
        testCases.add(testCase);
    }

    /**
     * Runs all tests currently contained. Returns true if all tests pass, false otherwise
     *
     * @return true if all tests pass, false otherwise
     */
    public boolean runAllTestCases(TestResult result) {
        for (GeometryTestCase testCase : testCases) {
            if (!testCase.runTestCases(result)) {
                return false;
            }
        }
        return true;
    }

    public void addToTestSuite(String name, TestSuite suite, Properties excludes) {
        for (GeometryTestCase testCase : testCases) {
            // only add the test case if its description is NOT in the excludes list
            if (!GeometryConformanceTestSuite.isExcluded(excludes, testCase.getDescription())) {
                testCase.setName(name);
                // check for overrides on operations
                // System.out.println(
                //        testCase.toString() + " - test count: " + testCase.getOperationCount());
                testCase = GeometryConformanceTestSuite.overrideOps(testCase, excludes);
                suite.addTest(testCase);
            }
        }
    }

    public void checkTestOverrides(String name, Properties excludes) {
        for (GeometryTestCase testCase : testCases) {
            // only add the test case if its description is NOT in the excludes list
            if (!GeometryConformanceTestSuite.isExcluded(excludes, testCase.getDescription())) {
                testCase.setName(name);
                // check for overrides on operations
                testCase = GeometryConformanceTestSuite.overrideOps(testCase, excludes);
            }
        }
    }

    protected PrecisionModel getPrecisionModel() {
        return precisionModel;
    }

    protected void setPrecisionModel(PrecisionModel precisionModel) {
        this.precisionModel = precisionModel;
    }
}
