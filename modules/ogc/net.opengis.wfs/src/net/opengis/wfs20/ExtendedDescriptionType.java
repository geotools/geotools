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
 * A representation of the model object '<em><b>Extended Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ExtendedDescriptionType#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getExtendedDescriptionType()
 * @model extendedMetaData="name='ExtendedDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface ExtendedDescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Element</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.ElementType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getExtendedDescriptionType_Element()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Element' namespace='##targetNamespace'"
     * @generated
     */
    EList<ElementType> getElement();

} // ExtendedDescriptionType
