/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Value Group Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A group of related parameter values. The same group can be repeated more than once in a Conversion, Transformation, or higher level parameterValueGroup, if those instances contain different values of one or more parameterValues which suitably distinquish among those groups. This concrete complexType can be used for operation methods without using an Application Schema that defines operation-method-specialized element names and contents, especially for methods with only one instance. This complexType can be used, extended, or restricted for well-known operation methods, especially for methods with many instances. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ParameterValueGroupType#getIncludesValue <em>Includes Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueGroupType#getValuesOfGroup <em>Values Of Group</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getParameterValueGroupType()
 * @model extendedMetaData="name='ParameterValueGroupType' kind='elementOnly'"
 * @generated
 */
public interface ParameterValueGroupType extends AbstractGeneralParameterValueType {
    /**
     * Returns the value of the '<em><b>Includes Value</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractGeneralParameterValueType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of composition associations to the parameter values and groups of values included in this group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Includes Value</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getParameterValueGroupType_IncludesValue()
     * @model containment="true" lower="2"
     *        extendedMetaData="kind='element' name='includesValue' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractGeneralParameterValueType> getIncludesValue();

    /**
     * Returns the value of the '<em><b>Values Of Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation parameter group for which this element provides parameter values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Of Group</em>' containment reference.
     * @see #setValuesOfGroup(OperationParameterGroupRefType)
     * @see net.opengis.gml311.Gml311Package#getParameterValueGroupType_ValuesOfGroup()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='valuesOfGroup' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterGroupRefType getValuesOfGroup();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueGroupType#getValuesOfGroup <em>Values Of Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Of Group</em>' containment reference.
     * @see #getValuesOfGroup()
     * @generated
     */
    void setValuesOfGroup(OperationParameterGroupRefType value);

} // ParameterValueGroupType
