/**
 */
package net.opengis.wps20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xml.type.AnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 					This element is used to embed the data in a WPS request or response.
 * 					The content can be XML data, plain character data, or specially encoded binary data (i.e. base64).
 * 				
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.DataType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.DataType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.DataType#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getDataType()
 * @model extendedMetaData="name='Data_._type' kind='mixed'"
 * @generated
 */
public interface DataType extends EObject, AnyType {
	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Encoding procedure or character set used (e.g. raw, base64, or UTF-8).
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #setEncoding(String)
	 * @see net.opengis.wps20.Wps20Package#getDataType_Encoding()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataType#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Media type of the data.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mime Type</em>' attribute.
	 * @see #setMimeType(String)
	 * @see net.opengis.wps20.Wps20Package#getDataType_MimeType()
	 * @model dataType="net.opengis.ows20.MimeType"
	 *        extendedMetaData="kind='attribute' name='mimeType'"
	 * @generated
	 */
	String getMimeType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataType#getMimeType <em>Mime Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mime Type</em>' attribute.
	 * @see #getMimeType()
	 * @generated
	 */
	void setMimeType(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Identification of the data schema.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see net.opengis.wps20.Wps20Package#getDataType_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataType#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

} // DataType
