/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supported UO Ms Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Listing of the Unit of Measure (U0M) support for this process input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.SupportedUOMsType#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.SupportedUOMsType#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getSupportedUOMsType()
 * @model extendedMetaData="name='SupportedUOMsType' kind='elementOnly'"
 * @generated
 */
public interface SupportedUOMsType extends EObject {
    /**
     * Returns the value of the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the default UOM supported for this input or output, if UoM is applicable. The process shall expect input in or produce output in this UOM unless the Execute request specifies another supported UOM.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' containment reference.
     * @see #setDefault(DefaultType1)
     * @see net.opengis.wps10.Wps10Package#getSupportedUOMsType_Default()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Default'"
     * @generated
     */
    DefaultType1 getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedUOMsType#getDefault <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' containment reference.
     * @see #getDefault()
     * @generated
     */
    void setDefault(DefaultType1 value);

    /**
     * Returns the value of the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of references to all of the UOMs supported for this input or output, if UOM is applicable. The default UOM shall be included in this list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported</em>' containment reference.
     * @see #setSupported(UOMsType)
     * @see net.opengis.wps10.Wps10Package#getSupportedUOMsType_Supported()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Supported'"
     * @generated
     */
    UOMsType getSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedUOMsType#getSupported <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported</em>' containment reference.
     * @see #getSupported()
     * @generated
     */
    void setSupported(UOMsType value);

} // SupportedUOMsType
