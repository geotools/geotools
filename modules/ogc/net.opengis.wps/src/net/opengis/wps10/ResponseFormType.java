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
 * A representation of the model object '<em><b>Response Form Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the response type of the WPS, either raw data or XML document
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ResponseFormType#getResponseDocument <em>Response Document</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseFormType#getRawDataOutput <em>Raw Data Output</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getResponseFormType()
 * @model extendedMetaData="name='ResponseFormType' kind='elementOnly'"
 * @generated
 */
public interface ResponseFormType extends EObject {
    /**
     * Returns the value of the '<em><b>Response Document</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that the outputs shall be returned as part of a WPS response document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Response Document</em>' containment reference.
     * @see #setResponseDocument(ResponseDocumentType)
     * @see net.opengis.wps10.Wps10Package#getResponseFormType_ResponseDocument()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ResponseDocument' namespace='##targetNamespace'"
     * @generated
     */
    ResponseDocumentType getResponseDocument();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseFormType#getResponseDocument <em>Response Document</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Response Document</em>' containment reference.
     * @see #getResponseDocument()
     * @generated
     */
    void setResponseDocument(ResponseDocumentType value);

    /**
     * Returns the value of the '<em><b>Raw Data Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that the output shall be returned directly as raw data, without a WPS response document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Raw Data Output</em>' containment reference.
     * @see #setRawDataOutput(OutputDefinitionType)
     * @see net.opengis.wps10.Wps10Package#getResponseFormType_RawDataOutput()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='RawDataOutput' namespace='##targetNamespace'"
     * @generated
     */
    OutputDefinitionType getRawDataOutput();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseFormType#getRawDataOutput <em>Raw Data Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Raw Data Output</em>' containment reference.
     * @see #getRawDataOutput()
     * @generated
     */
    void setRawDataOutput(OutputDefinitionType value);

} // ResponseFormType
