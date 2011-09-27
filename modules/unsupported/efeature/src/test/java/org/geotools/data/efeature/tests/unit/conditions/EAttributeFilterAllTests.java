/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.unit.conditions;

import org.geotools.data.efeature.EFeature;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;


/**
 * Test suite for {@link EFeature} implementation.
 * 
 * @author kengu
 *
 *
 * @source $URL$
 */
public class EAttributeFilterAllTests extends TestSuite {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public EAttributeFilterAllTests(String name) {
        super(name);
    }
    
    public static Test suite() {
        //
        // Create test suite
        //
        TestSuite suite = new EAttributeFilterAllTests("EAttribute Filter Tests");
        //
        // Add EAttribute value tests
        //
        suite.addTestSuite(EAttributeValueIsIDTest.class);
        suite.addTestSuite(EAttributeValueIsNullTest.class);
        suite.addTestSuite(EAttributeValueIsEqualTest.class);
        suite.addTestSuite(EAttributeValueIsNotEqualTest.class);
        suite.addTestSuite(EAttributeValueIsLessThanTest.class);
        suite.addTestSuite(EAttributeValueIsLessEqualTest.class);
        suite.addTestSuite(EAttributeValueIsGreaterThanTest.class);
        suite.addTestSuite(EAttributeValueIsLikeTest.class);
        suite.addTestSuite(EAttributeValueIsBetweenTest.class);
        suite.addTestSuite(EAttributeValueIsOutsideTest.class);
        //
        // Add EGeometry value tests
        //
        suite.addTestSuite(EGeometryValueBBoxTest.class);
        suite.addTestSuite(EGeometryValueEqualsTest.class);
        suite.addTestSuite(EGeometryValueIntersectsTest.class);
        suite.addTestSuite(EGeometryValueContainsTest.class);
        suite.addTestSuite(EGeometryValueCrossesTest.class);
        suite.addTestSuite(EGeometryValueDisjointTest.class);
        suite.addTestSuite(EGeometryValueOverlapsTest.class);
        suite.addTestSuite(EGeometryValueTouchesTest.class);
        suite.addTestSuite(EGeometryValueWithinTest.class);
        suite.addTestSuite(EGeometryValueBeyondTest.class);
        //
        // Ready to execute
        //
        return suite;
    }


} //EFeatureAllTests
