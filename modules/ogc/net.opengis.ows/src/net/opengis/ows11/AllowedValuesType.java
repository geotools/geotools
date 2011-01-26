/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Allowed Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.AllowedValuesType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.ows11.AllowedValuesType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows11.AllowedValuesType#getRange <em>Range</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getAllowedValuesType()
 * @model extendedMetaData="name='AllowedValues_._type' kind='elementOnly'"
 * @generated
 */
public interface AllowedValuesType extends EObject {
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
     * @see net.opengis.ows11.Ows11Package#getAllowedValuesType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.ValueType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getAllowedValuesType_Value()
     * @model type="net.opengis.ows11.ValueType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList getValue();

    /**
     * Returns the value of the '<em><b>Range</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.RangeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getAllowedValuesType_Range()
     * @model type="net.opengis.ows11.RangeType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Range' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList getRange();

} // AllowedValuesType
