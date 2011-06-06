/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.unit;

import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>EFeature Data</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class EFeatureDataTest extends TestCase {

    /**
     * The fixture for this EFeature Data test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EFeatureData<?, ?> fixture = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main(String[] args) {
        TestRunner.run(EFeatureDataTest.class);
    }

    /**
     * Constructs a new EFeature Data test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureDataTest(String name) {
        super(name);
    }

    /**
     * Sets the fixture for this EFeature Data test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void setFixture(EFeatureData<?, ?> fixture) {
        this.fixture = fixture;
    }

    /**
     * Returns the fixture for this EFeature Data test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EFeatureData<?, ?> getFixture() {
        return fixture;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#setUp()
     * @generated
     */
    @Override
    protected void setUp() throws Exception {
        setFixture(EFeatureTestsFactory.eINSTANCE.createEFeatureData());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#tearDown()
     * @generated
     */
    @Override
    protected void tearDown() throws Exception {
        setFixture(null);
    }

} //EFeatureDataTest
