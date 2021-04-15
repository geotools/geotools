/**
 */
package net.opengis.wps20;

import net.opengis.ows20.AllowedValuesType;
import net.opengis.ows20.AnyValueType;
import net.opengis.ows20.DomainMetadataType;
import net.opengis.ows20.ValueType;
import net.opengis.ows20.ValuesReferenceType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Data Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				A literal data domain consists of a value type and range,
 * 				and optionally a unit of measurement and a default value.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType()
 * @model extendedMetaData="name='LiteralDataDomainType' kind='elementOnly'"
 * @generated
 */
public interface LiteralDataDomainType extends EObject {
	/**
	 * Returns the value of the '<em><b>Allowed Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * List of all the valid values and/or ranges of values for
	 *       this quantity. For numeric quantities, signed values should be ordered
	 *       from negative infinity to positive infinity.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Allowed Values</em>' containment reference.
	 * @see #setAllowedValues(AllowedValuesType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_AllowedValues()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='AllowedValues' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	AllowedValuesType getAllowedValues();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getAllowedValues <em>Allowed Values</em>}' containment reference.
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
	 * Specifies that any value is allowed for this
	 *       parameter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any Value</em>' containment reference.
	 * @see #setAnyValue(AnyValueType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_AnyValue()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='AnyValue' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	AnyValueType getAnyValue();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getAnyValue <em>Any Value</em>}' containment reference.
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
	 * Reference to externally specified list of all the valid
	 *       values and/or ranges of values for this quantity. (Informative: This
	 *       element was simplified from the metaDataProperty element in GML
	 *       3.0.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Values Reference</em>' containment reference.
	 * @see #setValuesReference(ValuesReferenceType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_ValuesReference()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='ValuesReference' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	ValuesReferenceType getValuesReference();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getValuesReference <em>Values Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Values Reference</em>' containment reference.
	 * @see #getValuesReference()
	 * @generated
	 */
	void setValuesReference(ValuesReferenceType value);

	/**
	 * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the data type of this set of values. In
	 *       this case, the xlink:href attribute can reference a URN for a well-known
	 *       data type. For example, such a URN could be a data type identification
	 *       URN defined in the "ogc" URN namespace.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Type</em>' containment reference.
	 * @see #setDataType(DomainMetadataType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_DataType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='DataType' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	DomainMetadataType getDataType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getDataType <em>Data Type</em>}' containment reference.
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
	 * Definition of the unit of measure of this set of values.
	 *       In this case, the xlink:href attribute can reference a URN for a
	 *       well-known unit of measure (uom). For example, such a URN could be a UOM
	 *       identification URN defined in the "ogc" URN namespace.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>UOM</em>' containment reference.
	 * @see #setUOM(DomainMetadataType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_UOM()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='UOM' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	DomainMetadataType getUOM();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getUOM <em>UOM</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UOM</em>' containment reference.
	 * @see #getUOM()
	 * @generated
	 */
	void setUOM(DomainMetadataType value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The default value for a quantity for which multiple
	 *       values are allowed.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Value</em>' containment reference.
	 * @see #setDefaultValue(ValueType)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType_DefaultValue()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='DefaultValue' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	ValueType getDefaultValue();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType#getDefaultValue <em>Default Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' containment reference.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(ValueType value);

} // LiteralDataDomainType
