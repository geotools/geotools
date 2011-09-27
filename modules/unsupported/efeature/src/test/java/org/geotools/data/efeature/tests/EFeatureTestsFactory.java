/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests;

import com.vividsolutions.jts.geom.Geometry;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage
 * @generated
 *
 * @source $URL$
 */
public interface EFeatureTestsFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EFeatureTestsFactory eINSTANCE = org.geotools.data.efeature.tests.impl.EFeatureTestsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>EFeature Data</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>EFeature Data</em>'.
     * @generated
     */
    <A, G extends Geometry> EFeatureData<A, G> createEFeatureData();

    /**
     * Returns a new object of class '<em>Non Geo EObject</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Non Geo EObject</em>'.
     * @generated
     */
    NonGeoEObject createNonGeoEObject();

    /**
     * Returns a new object of class '<em>EFeature Compatible Data</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>EFeature Compatible Data</em>'.
     * @generated
     */
    <A, G extends Geometry> EFeatureCompatibleData<A, G> createEFeatureCompatibleData();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    EFeatureTestsPackage getEFeatureTestsPackage();

} //EFeatureTestsFactory
