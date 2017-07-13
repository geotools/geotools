/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Parameter Group Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The definition of a group of parameters used by an operation method. This complexType is expected to be used or extended for all applicable operation methods, without defining operation-method-specialized element names.  
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OperationParameterGroupType#getGroupID <em>Group ID</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationParameterGroupType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationParameterGroupType#getMaximumOccurs <em>Maximum Occurs</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationParameterGroupType#getIncludesParameter <em>Includes Parameter</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupType()
 * @model extendedMetaData="name='OperationParameterGroupType' kind='elementOnly'"
 * @generated
 */
public interface OperationParameterGroupType extends OperationParameterGroupBaseType {
    /**
     * Returns the value of the '<em><b>Group ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this operation parameter group. The first groupID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Group ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupType_GroupID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='groupID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getGroupID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this operation parameter group, including source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationParameterGroupType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Maximum Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The maximum number of times that values for this parameter group can be included. If this attribute is omitted, the maximum number is one. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Occurs</em>' attribute.
     * @see #setMaximumOccurs(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupType_MaximumOccurs()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='element' name='maximumOccurs' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMaximumOccurs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationParameterGroupType#getMaximumOccurs <em>Maximum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Occurs</em>' attribute.
     * @see #getMaximumOccurs()
     * @generated
     */
    void setMaximumOccurs(BigInteger value);

    /**
     * Returns the value of the '<em><b>Includes Parameter</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractGeneralOperationParameterRefType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of associations to the set of operation parameters that are members of this group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Includes Parameter</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getOperationParameterGroupType_IncludesParameter()
     * @model containment="true" lower="2"
     *        extendedMetaData="kind='element' name='includesParameter' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractGeneralOperationParameterRefType> getIncludesParameter();

} // OperationParameterGroupType
