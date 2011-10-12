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
 * A representation of the model object '<em><b>Tuple Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.TupleType#getMember <em>Member</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getTupleType()
 * @model extendedMetaData="name='TupleType' kind='elementOnly'"
 * @generated
 */
public interface TupleType extends EObject {
    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.MemberPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Member</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Member</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getTupleType_Member()
     * @model containment="true" lower="2"
     *        extendedMetaData="kind='element' name='member' namespace='##targetNamespace'"
     * @generated
     */
    EList<MemberPropertyType> getMember();

} // TupleType
