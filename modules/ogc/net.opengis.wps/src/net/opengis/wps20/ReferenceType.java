/**
 */
package net.opengis.wps20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				Reference to an input (output) value that is a web accessible resource.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ReferenceType#getBody <em>Body</em>}</li>
 *   <li>{@link net.opengis.wps20.ReferenceType#getBodyReference <em>Body Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.ReferenceType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.ReferenceType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wps20.ReferenceType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.ReferenceType#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getReferenceType()
 * @model extendedMetaData="name='ReferenceType' kind='elementOnly'"
 * @generated
 */
public interface ReferenceType extends EObject {
	/**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						The contents of this element to be used as the body of the HTTP request
	 * 						message to be sent to the service identified in ../Reference/@href. 
	 * 						For example, it could be an XML encoded WFS request using HTTP/POST.
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Body</em>' containment reference.
	 * @see #setBody(EObject)
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_Body()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Body' namespace='##targetNamespace'"
	 * @generated
	 */
	EObject getBody();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getBody <em>Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' containment reference.
	 * @see #getBody()
	 * @generated
	 */
	void setBody(EObject value);

	/**
	 * Returns the value of the '<em><b>Body Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						Reference to a remote document to be used as the body of the an HTTP/POST request message
	 * 						to the service identified in the href element in the Reference structure.
	 * 					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Body Reference</em>' containment reference.
	 * @see #setBodyReference(BodyReferenceType)
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_BodyReference()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='BodyReference' namespace='##targetNamespace'"
	 * @generated
	 */
	BodyReferenceType getBodyReference();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getBodyReference <em>Body Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body Reference</em>' containment reference.
	 * @see #getBodyReference()
	 * @generated
	 */
	void setBodyReference(BodyReferenceType value);

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
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_Encoding()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Returns the value of the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					HTTP URI that points to the remote resource where the data may be retrieved.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Href</em>' attribute.
	 * @see #setHref(String)
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_Href()
	 * @model dataType="org.w3.xlink.HrefType" required="true"
	 *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
	String getHref();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getHref <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Href</em>' attribute.
	 * @see #getHref()
	 * @generated
	 */
	void setHref(String value);

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
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_MimeType()
	 * @model dataType="net.opengis.ows20.MimeType"
	 *        extendedMetaData="kind='attribute' name='mimeType'"
	 * @generated
	 */
	String getMimeType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getMimeType <em>Mime Type</em>}' attribute.
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
	 * @see net.opengis.wps20.Wps20Package#getReferenceType_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ReferenceType#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

} // ReferenceType
