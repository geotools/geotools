/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Enum Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of all the valid values and/or ranges of values for this variable. For numeric variables, signed values shall be ordered from negative infinity to positive infinity. For intervals, the "type" and "semantic" attributes are inherited by children elements, but can be superceded by them.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ValueEnumBaseType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueEnumBaseType#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueEnumBaseType#getSingleValue <em>Single Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getValueEnumBaseType()
 * @model extendedMetaData="name='valueEnumBaseType' kind='elementOnly'"
 * @generated
 */
public interface ValueEnumBaseType extends EObject {
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
	 * @see net.opengis.wcs10.Wcs10Package#getValueEnumBaseType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
    FeatureMap getGroup();

    /**
	 * Returns the value of the '<em><b>Interval</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.IntervalType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interval</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Interval</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getValueEnumBaseType_Interval()
	 * @model type="net.opengis.wcs10.IntervalType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='interval' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
    EList getInterval();

    /**
	 * Returns the value of the '<em><b>Single Value</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.TypedLiteralType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A single value for a quantity.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Single Value</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getValueEnumBaseType_SingleValue()
	 * @model type="net.opengis.wcs10.TypedLiteralType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='singleValue' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
    EList getSingleValue();

} // ValueEnumBaseType
