/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Parameter Group Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic encoding for operation parameter group objects, simplifying and restricting the DefinitionType as needed. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OperationParameterGroupBaseType#getGroupName <em>Group Name</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupBaseType()
 * @model abstract="true"
 *        extendedMetaData="name='OperationParameterGroupBaseType' kind='elementOnly'"
 * @generated
 */
public interface OperationParameterGroupBaseType extends AbstractGeneralOperationParameterType {
    /**
     * Returns the value of the '<em><b>Group Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this operation parameter group is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Group Name</em>' containment reference.
     * @see #setGroupName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupBaseType_GroupName()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='groupName' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getGroupName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationParameterGroupBaseType#getGroupName <em>Group Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group Name</em>' containment reference.
     * @see #getGroupName()
     * @generated
     */
    void setGroupName(CodeType value);

} // OperationParameterGroupBaseType
