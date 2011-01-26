/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.AnyValueType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Input Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a process input that consists of a simple literal value (e.g., "2.1"). (Informative: This type is a subset of the ows:UnNamedDomainType defined in owsDomaintype.xsd.)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.LiteralInputType#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralInputType#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralInputType#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralInputType#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getLiteralInputType()
 * @model extendedMetaData="name='LiteralInputType' kind='elementOnly'"
 * @generated
 */
public interface LiteralInputType extends LiteralOutputType {
    /**
     * Returns the value of the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that there are a finite set of values and ranges allowed for this input, and contains list of all the valid values and/or ranges of values. Notice that these values and ranges can be displayed to a human client.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Allowed Values</em>' containment reference.
     * @see #setAllowedValues(AllowedValuesType)
     * @see net.opengis.wps10.Wps10Package#getLiteralInputType_AllowedValues()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AllowedValues' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    AllowedValuesType getAllowedValues();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralInputType#getAllowedValues <em>Allowed Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Allowed Values</em>' containment reference.
     * @see #getAllowedValues()
     * @generated
     */
    void setAllowedValues(AllowedValuesType value);

    /**
     * Returns the value of the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that any value is allowed for this input. This element shall be included when there are no restrictions, except for data type, on the allowable value of this input.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Any Value</em>' containment reference.
     * @see #setAnyValue(AnyValueType)
     * @see net.opengis.wps10.Wps10Package#getLiteralInputType_AnyValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AnyValue' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    AnyValueType getAnyValue();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralInputType#getAnyValue <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Any Value</em>' containment reference.
     * @see #getAnyValue()
     * @generated
     */
    void setAnyValue(AnyValueType value);

    /**
     * Returns the value of the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that there are a finite set of values and ranges allowed for this input, which are specified in the referenced list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Reference</em>' containment reference.
     * @see #setValuesReference(ValuesReferenceType)
     * @see net.opengis.wps10.Wps10Package#getLiteralInputType_ValuesReference()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ValuesReference'"
     * @generated
     */
    ValuesReferenceType getValuesReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralInputType#getValuesReference <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Reference</em>' containment reference.
     * @see #getValuesReference()
     * @generated
     */
    void setValuesReference(ValuesReferenceType value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional default value for this quantity, which should be included when this quantity has a default value.  The DefaultValue shall be understood to be consistent with the unit of measure selected in the Execute request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Value</em>' attribute.
     * @see #setDefaultValue(String)
     * @see net.opengis.wps10.Wps10Package#getLiteralInputType_DefaultValue()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='DefaultValue'"
     * @generated
     */
    String getDefaultValue();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralInputType#getDefaultValue <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' attribute.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(String value);

} // LiteralInputType
