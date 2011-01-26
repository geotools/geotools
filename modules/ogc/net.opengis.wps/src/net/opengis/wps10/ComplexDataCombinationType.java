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
 * A representation of the model object '<em><b>Complex Data Combination Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifies the default Format, Encoding, and Schema supported for this input or output. The process shall expect input in or produce output in this combination of Format/Encoding/Schema unless the Execute request specifies otherwise..
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ComplexDataCombinationType#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getComplexDataCombinationType()
 * @model extendedMetaData="name='ComplexDataCombinationType' kind='elementOnly'"
 * @generated
 */
public interface ComplexDataCombinationType extends EObject {
    /**
     * Returns the value of the '<em><b>Format</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The default combination of MimeType/Encoding/Schema supported for this Input/Output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' containment reference.
     * @see #setFormat(ComplexDataDescriptionType)
     * @see net.opengis.wps10.Wps10Package#getComplexDataCombinationType_Format()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Format'"
     * @generated
     */
    ComplexDataDescriptionType getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ComplexDataCombinationType#getFormat <em>Format</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' containment reference.
     * @see #getFormat()
     * @generated
     */
    void setFormat(ComplexDataDescriptionType value);

} // ComplexDataCombinationType
