/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a subset of the named coverage range(s). Currently, only a value enumeration definition of a range subset.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.RangeSubsetType#getAxisSubset <em>Axis Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getRangeSubsetType()
 * @model extendedMetaData="name='RangeSubsetType' kind='elementOnly'"
 * @generated
 */
public interface RangeSubsetType extends EObject {
    /**
	 * Returns the value of the '<em><b>Axis Subset</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.AxisSubsetType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Ordered sequence of points and/or intervals along one axis of a compound range set.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Axis Subset</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSubsetType_AxisSubset()
	 * @model type="net.opengis.wcs10.AxisSubsetType" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='axisSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getAxisSubset();

} // RangeSubsetType
