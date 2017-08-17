/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A parameter value, ordered sequence of values, or reference to a file of parameter values. This concrete complexType can be used for operation methods without using an Application Schema that defines operation-method-specialized element names and contents, especially for methods with only one instance. This complexType can be used, extended, or restricted for well-known operation methods, especially for methods with many instances. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getDmsAngleValue <em>Dms Angle Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getStringValue <em>String Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getIntegerValue <em>Integer Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#isBooleanValue <em>Boolean Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getValueList <em>Value List</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getIntegerValueList <em>Integer Value List</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getValueFile <em>Value File</em>}</li>
 *   <li>{@link net.opengis.gml311.ParameterValueType#getValueOfParameter <em>Value Of Parameter</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getParameterValueType()
 * @model extendedMetaData="name='ParameterValueType' kind='elementOnly'"
 * @generated
 */
public interface ParameterValueType extends AbstractGeneralParameterValueType {
    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Numeric value of an operation parameter, with its associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_Value()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(MeasureType value);

    /**
     * Returns the value of the '<em><b>Dms Angle Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Value of an angle operation parameter, in either degree-minute-second format or single value format. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dms Angle Value</em>' containment reference.
     * @see #setDmsAngleValue(DMSAngleType)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_DmsAngleValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='dmsAngleValue' namespace='##targetNamespace'"
     * @generated
     */
    DMSAngleType getDmsAngleValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getDmsAngleValue <em>Dms Angle Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dms Angle Value</em>' containment reference.
     * @see #getDmsAngleValue()
     * @generated
     */
    void setDmsAngleValue(DMSAngleType value);

    /**
     * Returns the value of the '<em><b>String Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * String value of an operation parameter. A string value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>String Value</em>' attribute.
     * @see #setStringValue(String)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_StringValue()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='stringValue' namespace='##targetNamespace'"
     * @generated
     */
    String getStringValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getStringValue <em>String Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>String Value</em>' attribute.
     * @see #getStringValue()
     * @generated
     */
    void setStringValue(String value);

    /**
     * Returns the value of the '<em><b>Integer Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Positive integer value of an operation parameter, usually used for a count. An integer value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Integer Value</em>' attribute.
     * @see #setIntegerValue(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_IntegerValue()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='element' name='integerValue' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getIntegerValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getIntegerValue <em>Integer Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Integer Value</em>' attribute.
     * @see #getIntegerValue()
     * @generated
     */
    void setIntegerValue(BigInteger value);

    /**
     * Returns the value of the '<em><b>Boolean Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Boolean value of an operation parameter. A Boolean value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean Value</em>' attribute.
     * @see #isSetBooleanValue()
     * @see #unsetBooleanValue()
     * @see #setBooleanValue(boolean)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_BooleanValue()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='element' name='booleanValue' namespace='##targetNamespace'"
     * @generated
     */
    boolean isBooleanValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#isBooleanValue <em>Boolean Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean Value</em>' attribute.
     * @see #isSetBooleanValue()
     * @see #unsetBooleanValue()
     * @see #isBooleanValue()
     * @generated
     */
    void setBooleanValue(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ParameterValueType#isBooleanValue <em>Boolean Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetBooleanValue()
     * @see #isBooleanValue()
     * @see #setBooleanValue(boolean)
     * @generated
     */
    void unsetBooleanValue();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ParameterValueType#isBooleanValue <em>Boolean Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Boolean Value</em>' attribute is set.
     * @see #unsetBooleanValue()
     * @see #isBooleanValue()
     * @see #setBooleanValue(boolean)
     * @generated
     */
    boolean isSetBooleanValue();

    /**
     * Returns the value of the '<em><b>Value List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of two or more numeric values of an operation parameter list, where each value has the same associated unit of measure. An element of this type contains a space-separated sequence of double values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value List</em>' containment reference.
     * @see #setValueList(MeasureListType)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_ValueList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='valueList' namespace='##targetNamespace'"
     * @generated
     */
    MeasureListType getValueList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getValueList <em>Value List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value List</em>' containment reference.
     * @see #getValueList()
     * @generated
     */
    void setValueList(MeasureListType value);

    /**
     * Returns the value of the '<em><b>Integer Value List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of two or more integer values of an operation parameter list, usually used for counts. These integer values do not have an associated unit of measure. An element of this type contains a space-separated sequence of integer values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Integer Value List</em>' attribute.
     * @see #setIntegerValueList(List)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_IntegerValueList()
     * @model dataType="net.opengis.gml311.IntegerList" many="false"
     *        extendedMetaData="kind='element' name='integerValueList' namespace='##targetNamespace'"
     * @generated
     */
    List<BigInteger> getIntegerValueList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getIntegerValueList <em>Integer Value List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Integer Value List</em>' attribute.
     * @see #getIntegerValueList()
     * @generated
     */
    void setIntegerValueList(List<BigInteger> value);

    /**
     * Returns the value of the '<em><b>Value File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a file or a part of a file containing one or more parameter values, each numeric value with its associated unit of measure. When referencing a part of a file, that file must contain multiple identified parts, such as an XML encoded document. Furthermore, the referenced file or part of a file can reference another part of the same or different files, as allowed in XML documents. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value File</em>' attribute.
     * @see #setValueFile(String)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_ValueFile()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='valueFile' namespace='##targetNamespace'"
     * @generated
     */
    String getValueFile();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getValueFile <em>Value File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value File</em>' attribute.
     * @see #getValueFile()
     * @generated
     */
    void setValueFile(String value);

    /**
     * Returns the value of the '<em><b>Value Of Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation parameter that this is a value of. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Of Parameter</em>' containment reference.
     * @see #setValueOfParameter(OperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getParameterValueType_ValueOfParameter()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='valueOfParameter' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterRefType getValueOfParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ParameterValueType#getValueOfParameter <em>Value Of Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Of Parameter</em>' containment reference.
     * @see #getValueOfParameter()
     * @generated
     */
    void setValueOfParameter(OperationParameterRefType value);

} // ParameterValueType
