/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Enum Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of all the valid values and/or intervals of values for this variable. For numeric variables, signed values shall be ordered from negative infinity to positive infinity. For intervals, the type and semantic attributes are inherited by children elements, but can be superceded here.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ValueEnumType#getSemantic <em>Semantic</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueEnumType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getValueEnumType()
 * @model extendedMetaData="name='valueEnumType' kind='elementOnly'"
 * @generated
 */
public interface ValueEnumType extends ValueEnumBaseType {
    /**
	 * Returns the value of the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Should be included if the semantics or meaning is not clearly specified elsewhere, and the valueEnumBaseType does not include any "interval" elements that include this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Semantic</em>' attribute.
	 * @see #setSemantic(String)
	 * @see net.opengis.wcs10.Wcs10Package#getValueEnumType_Semantic()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='semantic' namespace='##targetNamespace'"
	 * @generated
	 */
    String getSemantic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueEnumType#getSemantic <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic</em>' attribute.
	 * @see #getSemantic()
	 * @generated
	 */
    void setSemantic(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Should be included if the data type is not string, and the valueEnumBaseType does not include any "interval" elements that include this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see net.opengis.wcs10.Wcs10Package#getValueEnumType_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueEnumType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

} // ValueEnumType
