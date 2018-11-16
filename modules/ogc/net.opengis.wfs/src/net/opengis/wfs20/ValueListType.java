/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wfs20.ValueListType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.ValueListType#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see net.opengis.wfs20.Wfs20Package#getValueListType()
 * @model extendedMetaData="name='ValueListType' kind='elementOnly'"
 * @generated
 */
public interface ValueListType extends EObject {
    /**
   * Returns the value of the '<em><b>Group</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @return the value of the '<em>Group</em>' attribute list.
   * @see net.opengis.wfs20.Wfs20Package#getValueListType_Group()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='group' name='group:0'"
   * @generated
   */
    FeatureMap getGroup();

    /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference list.
   * @see net.opengis.wfs20.Wfs20Package#getValueListType_Value()
   * @model containment="true" required="true" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace' group='#group:0'"
   * @generated
   */
    EList<EObject> getValue();

} // ValueListType
