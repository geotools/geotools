/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Parameter Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic encoding for operation parameter objects, simplifying and restricting the DefinitionType as needed. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OperationParameterBaseType#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOperationParameterBaseType()
 * @model abstract="true"
 *        extendedMetaData="name='OperationParameterBaseType' kind='elementOnly'"
 * @generated
 */
public interface OperationParameterBaseType extends AbstractGeneralOperationParameterType {
    /**
     * Returns the value of the '<em><b>Parameter Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this operation parameter is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Parameter Name</em>' containment reference.
     * @see #setParameterName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getOperationParameterBaseType_ParameterName()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='parameterName' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getParameterName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationParameterBaseType#getParameterName <em>Parameter Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Name</em>' containment reference.
     * @see #getParameterName()
     * @generated
     */
    void setParameterName(CodeType value);

} // OperationParameterBaseType
