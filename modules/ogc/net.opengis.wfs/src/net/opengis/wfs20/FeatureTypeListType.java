/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Type List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.FeatureTypeListType#getFeatureType <em>Feature Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeListType()
 * @model extendedMetaData="name='FeatureTypeListType' kind='elementOnly'"
 * @generated
 */
public interface FeatureTypeListType extends EObject {
    /**
     * Returns the value of the '<em><b>Feature Type</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.FeatureTypeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Type</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Type</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeListType_FeatureType()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='FeatureType' namespace='##targetNamespace'"
     * @generated
     */
    EList<FeatureTypeType> getFeatureType();

} // FeatureTypeListType
