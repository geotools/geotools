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
 * A representation of the model object '<em><b>Action Results Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ActionResultsType#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getActionResultsType()
 * @model extendedMetaData="name='ActionResultsType' kind='elementOnly'"
 * @generated
 */
public interface ActionResultsType extends EObject {
    /**
     * Returns the value of the '<em><b>Feature</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.CreatedOrModifiedFeatureType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getActionResultsType_Feature()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Feature' namespace='##targetNamespace'"
     * @generated
     */
    EList<CreatedOrModifiedFeatureType> getFeature();

} // ActionResultsType
