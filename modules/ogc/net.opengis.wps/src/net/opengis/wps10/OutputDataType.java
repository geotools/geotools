/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Value of one output from a process.
 * In this use, the DescriptionType shall describe this process output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.OutputDataType#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDataType#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getOutputDataType()
 * @model extendedMetaData="name='OutputDataType' kind='elementOnly'"
 * @generated
 */
public interface OutputDataType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this output as a web accessible resource, and references that resource.  This element shall only be used for complex data. This element shall be used by a server when "store" in the Execute request is "true".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(OutputReferenceType)
     * @see net.opengis.wps10.Wps10Package#getOutputDataType_Reference()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace'"
     * @generated
     */
    OutputReferenceType getReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDataType#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(OutputReferenceType value);

    /**
     * Returns the value of the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this output value as a data embedded in this response, and includes that data. This element shall be used by a server when "store" in the Execute request is "false".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data</em>' containment reference.
     * @see #setData(DataType)
     * @see net.opengis.wps10.Wps10Package#getOutputDataType_Data()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Data' namespace='##targetNamespace'"
     * @generated
     */
    DataType getData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDataType#getData <em>Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data</em>' containment reference.
     * @see #getData()
     * @generated
     */
    void setData(DataType value);

} // OutputDataType
