/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.NonGeoEObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Non Geo EObject</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.impl.NonGeoEObjectImpl#getNonGeoAttribute <em>Non Geo Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class NonGeoEObjectImpl extends EObjectImpl implements NonGeoEObject {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NonGeoEObjectImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return EFeatureTestsPackage.Literals.NON_GEO_EOBJECT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getNonGeoAttribute() {
        return (Integer)eGet(EFeatureTestsPackage.Literals.NON_GEO_EOBJECT__NON_GEO_ATTRIBUTE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setNonGeoAttribute(int newNonGeoAttribute) {
        eSet(EFeatureTestsPackage.Literals.NON_GEO_EOBJECT__NON_GEO_ATTRIBUTE, newNonGeoAttribute);
    }

} //NonGeoEObjectImpl
