/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Geo EObject</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute <em>Non Geo Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getNonGeoEObject()
 * @model
 * @generated
 */
public interface NonGeoEObject extends EObject {
    /**
     * Returns the value of the '<em><b>Non Geo Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Non Geo Attribute</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Non Geo Attribute</em>' attribute.
     * @see #setNonGeoAttribute(int)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getNonGeoEObject_NonGeoAttribute()
     * @model
     * @generated
     */
    int getNonGeoAttribute();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute <em>Non Geo Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Non Geo Attribute</em>' attribute.
     * @see #getNonGeoAttribute()
     * @generated
     */
    void setNonGeoAttribute(int value);

} // NonGeoEObject
