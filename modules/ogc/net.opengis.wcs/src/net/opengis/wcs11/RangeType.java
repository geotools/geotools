/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the fields (categories, measures, or values) in the range records available for each location in the coverage domain. Each such field may be a scalar (numeric or text) value, such as population density, or a vector (compound or tensor) of many similar values, such as incomes by race, or radiances by wavelength. Each range field is typically an observable whose meaning and reference system are referenced by URIs. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.RangeType#getField <em>Field</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getRangeType()
 * @model extendedMetaData="name='RangeType' kind='elementOnly'"
 * @generated
 */
public interface RangeType extends EObject {
    /**
     * Returns the value of the '<em><b>Field</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs11.FieldType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of the Fields in the Range of a coverage. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Field</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getRangeType_Field()
     * @model type="net.opengis.wcs11.FieldType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Field' namespace='##targetNamespace'"
     * @generated
     */
    EList getField();

} // RangeType
