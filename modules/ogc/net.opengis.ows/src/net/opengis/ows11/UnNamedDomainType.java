/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Un Named Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Valid domain (or allowed set of values) of one quantity, with needed metadata but without a quantity name or identifier. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.ows11.UnNamedDomainType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType()
 * @model extendedMetaData="name='UnNamedDomainType' kind='elementOnly'"
 * @generated
 */
public interface UnNamedDomainType extends EObject {
    /**
     * Returns the value of the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of all the valid values and/or ranges of values for this quantity. For numeric quantities, signed values should be ordered from negative infinity to positive infinity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Allowed Values</em>' containment reference.
     * @see #setAllowedValues(AllowedValuesType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_AllowedValues()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AllowedValues' namespace='##targetNamespace'"
     * @generated
     */
    AllowedValuesType getAllowedValues();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getAllowedValues <em>Allowed Values</em>}' containment reference.
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
     * Specifies that any value is allowed for this parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Any Value</em>' containment reference.
     * @see #setAnyValue(AnyValueType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_AnyValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AnyValue' namespace='##targetNamespace'"
     * @generated
     */
    AnyValueType getAnyValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getAnyValue <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Any Value</em>' containment reference.
     * @see #getAnyValue()
     * @generated
     */
    void setAnyValue(AnyValueType value);

    /**
     * Returns the value of the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies that no values are allowed for this parameter or quantity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>No Values</em>' containment reference.
     * @see #setNoValues(NoValuesType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_NoValues()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='NoValues' namespace='##targetNamespace'"
     * @generated
     */
    NoValuesType getNoValues();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getNoValues <em>No Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>No Values</em>' containment reference.
     * @see #getNoValues()
     * @generated
     */
    void setNoValues(NoValuesType value);

    /**
     * Returns the value of the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to externally specified list of all the valid values and/or ranges of values for this quantity. (Informative: This element was simplified from the metaDataProperty element in GML 3.0.) 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Reference</em>' containment reference.
     * @see #setValuesReference(ValuesReferenceType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_ValuesReference()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ValuesReference' namespace='##targetNamespace'"
     * @generated
     */
    ValuesReferenceType getValuesReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getValuesReference <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Reference</em>' containment reference.
     * @see #getValuesReference()
     * @generated
     */
    void setValuesReference(ValuesReferenceType value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional default value for this quantity, which should be included when this quantity has a default value. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Value</em>' containment reference.
     * @see #setDefaultValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_DefaultValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DefaultValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getDefaultValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getDefaultValue <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' containment reference.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Meaning metadata should be referenced or included for each quantity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meaning</em>' containment reference.
     * @see #setMeaning(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_Meaning()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Meaning' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getMeaning();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getMeaning <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meaning</em>' containment reference.
     * @see #getMeaning()
     * @generated
     */
    void setMeaning(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This data type metadata should be referenced or included for each quantity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' containment reference.
     * @see #setDataType(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_DataType()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataType' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getDataType();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getDataType <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' containment reference.
     * @see #getDataType()
     * @generated
     */
    void setDataType(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of unit of measure of this set of values. Should be included then this set of values has units (and not a more complete reference system). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference.
     * @see #setUOM(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_UOM()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='UOM' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getUOM();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getUOM <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UOM</em>' containment reference.
     * @see #getUOM()
     * @generated
     */
    void setUOM(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of reference system used by this set of values. Should be included then this set of values has a reference system (not just units). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference System</em>' containment reference.
     * @see #setReferenceSystem(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_ReferenceSystem()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ReferenceSystem' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getReferenceSystem();

    /**
     * Sets the value of the '{@link net.opengis.ows11.UnNamedDomainType#getReferenceSystem <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference System</em>' containment reference.
     * @see #getReferenceSystem()
     * @generated
     */
    void setReferenceSystem(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of other metadata about this quantity. A list of required and optional other metadata elements for this quantity should be specified in the Implementation Specification for this service. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getUnNamedDomainType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace'"
     * @generated
     */
    EList getMetadata();

} // UnNamedDomainType
