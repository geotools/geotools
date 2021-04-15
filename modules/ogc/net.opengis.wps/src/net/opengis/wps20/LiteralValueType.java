/**
 */
package net.opengis.wps20;

import net.opengis.ows20.ValueType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 					Representation of a simple literal value (such as an integer, a real number, or a string).
 * 				
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.LiteralValueType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps20.LiteralValueType#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getLiteralValueType()
 * @model extendedMetaData="name='LiteralValue_._type' kind='simple'"
 * @generated
 */
public interface LiteralValueType extends ValueType {
	/**
	 * Returns the value of the '<em><b>Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The data type of the value.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Type</em>' attribute.
	 * @see #setDataType(String)
	 * @see net.opengis.wps20.Wps20Package#getLiteralValueType_DataType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='dataType'"
	 * @generated
	 */
	String getDataType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralValueType#getDataType <em>Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Type</em>' attribute.
	 * @see #getDataType()
	 * @generated
	 */
	void setDataType(String value);

	/**
	 * Returns the value of the '<em><b>Uom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The unit of measurement of the value.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Uom</em>' attribute.
	 * @see #setUom(String)
	 * @see net.opengis.wps20.Wps20Package#getLiteralValueType_Uom()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='uom'"
	 * @generated
	 */
	String getUom();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralValueType#getUom <em>Uom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uom</em>' attribute.
	 * @see #getUom()
	 * @generated
	 */
	void setUom(String value);

} // LiteralValueType
