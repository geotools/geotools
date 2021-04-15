/**
 */
package net.opengis.wps20;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Format Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * References the XML schema, format, and encoding of a complex value. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.FormatType#isDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps20.FormatType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.FormatType#getMaximumMegabytes <em>Maximum Megabytes</em>}</li>
 *   <li>{@link net.opengis.wps20.FormatType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.FormatType#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getFormatType()
 * @model extendedMetaData="name='Format_._type' kind='empty'"
 * @generated
 */
public interface FormatType extends EObject {
	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #setDefault(boolean)
	 * @see net.opengis.wps20.Wps20Package#getFormatType_Default()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	boolean isDefault();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.FormatType#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #isDefault()
	 * @generated
	 */
	void setDefault(boolean value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.FormatType#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	void unsetDefault();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.FormatType#isDefault <em>Default</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default</em>' attribute is set.
	 * @see #unsetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	boolean isSetDefault();

	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						Encoding procedure or character set of the data (e.g. raw or base64).
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #setEncoding(String)
	 * @see net.opengis.wps20.Wps20Package#getFormatType_Encoding()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.FormatType#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Returns the value of the '<em><b>Maximum Megabytes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The maximum size of the input data, in megabytes.
	 * 							If the input exceeds this size, the server may return an error
	 * 							instead of processing the inputs.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Maximum Megabytes</em>' attribute.
	 * @see #setMaximumMegabytes(BigInteger)
	 * @see net.opengis.wps20.Wps20Package#getFormatType_MaximumMegabytes()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
	 *        extendedMetaData="kind='attribute' name='maximumMegabytes'"
	 * @generated
	 */
	BigInteger getMaximumMegabytes();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.FormatType#getMaximumMegabytes <em>Maximum Megabytes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Megabytes</em>' attribute.
	 * @see #getMaximumMegabytes()
	 * @generated
	 */
	void setMaximumMegabytes(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						Media type of the data.
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mime Type</em>' attribute.
	 * @see #setMimeType(String)
	 * @see net.opengis.wps20.Wps20Package#getFormatType_MimeType()
	 * @model dataType="net.opengis.ows20.MimeType"
	 *        extendedMetaData="kind='attribute' name='mimeType'"
	 * @generated
	 */
	String getMimeType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.FormatType#getMimeType <em>Mime Type</em>}' attribute.
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
	 * 						Identification of the data schema.
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see net.opengis.wps20.Wps20Package#getFormatType_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.FormatType#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

} // FormatType
