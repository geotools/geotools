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
 * A representation of the model object '<em><b>Supported Complex Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Formats, encodings, and schemas supported by a process input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.SupportedComplexDataType#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.SupportedComplexDataType#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataType()
 * @model extendedMetaData="name='SupportedComplexDataType' kind='elementOnly'"
 * @generated
 */
public interface SupportedComplexDataType extends EObject {
    /**
     * Returns the value of the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the default combination of Format, Encoding, and Schema supported for this Input/Output. The process shall expect input in or produce output in this combination of MimeType/Encoding/Schema unless the Execute request specifies otherwise.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' containment reference.
     * @see #setDefault(ComplexDataCombinationType)
     * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataType_Default()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Default'"
     * @generated
     */
    ComplexDataCombinationType getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedComplexDataType#getDefault <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' containment reference.
     * @see #getDefault()
     * @generated
     */
    void setDefault(ComplexDataCombinationType value);

    /**
     * Returns the value of the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of combinations of format, encoding, and schema supported for this Input/Output. This element shall be repeated for each combination of MimeType/Encoding/Schema that is supported for this Input/Output. This list shall include the default MimeType/Encoding/Schema.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported</em>' containment reference.
     * @see #setSupported(ComplexDataCombinationsType)
     * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataType_Supported()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Supported'"
     * @generated
     */
    ComplexDataCombinationsType getSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedComplexDataType#getSupported <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported</em>' containment reference.
     * @see #getSupported()
     * @generated
     */
    void setSupported(ComplexDataCombinationsType value);

} // SupportedComplexDataType
