/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.unit;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>efeature</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class EFeatureTestsTests extends TestSuite {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Test suite() {
        TestSuite suite = new EFeatureTestsTests("efeature Tests"); //$NON-NLS-1$
        suite.addTestSuite(EFeatureDataTest.class);
        return suite;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureTestsTests(String name) {
        super(name);
    }

} //EFeatureTestsTests
