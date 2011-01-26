/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Reference to an input or output value that is a web accessible resource.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getHeader <em>Header</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getBody <em>Body</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getBodyReference <em>Body Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getMethod <em>Method</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.InputReferenceType#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getInputReferenceType()
 * @model extendedMetaData="name='InputReferenceType' kind='elementOnly'"
 * @generated
 */
public interface InputReferenceType extends EObject {
    /**
     * Returns the value of the '<em><b>Header</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.HeaderType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extra HTTP request headers needed by the service identified in ../Reference/@href.  For example, an HTTP SOAP request requires a SOAPAction header.  This permits the creation of a complete and valid POST request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Header</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Header()
     * @model type="net.opengis.wps10.HeaderType" containment="true"
     *        extendedMetaData="kind='element' name='Header' namespace='##targetNamespace'"
     * @generated
     */
    EList getHeader();

    /**
     * Returns the value of the '<em><b>Body</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The contents of this element to be used as the body of the HTTP request message to be sent to the service identified in ../Reference/@href.  For example, it could be an XML encoded WFS request using HTTP POST
     * <!-- end-model-doc -->
     * @return the value of the '<em>Body</em>' containment reference.
     * @see #setBody(EObject)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Body()
     * @model
     */
    Object getBody();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getBody <em>Body</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Body</em>' attribute.
     * @see #getBody()
     * @generated
     */
    void setBody(Object value);

    /**
     * Returns the value of the '<em><b>Body Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a remote document to be used as the body of the an HTTP POST request message to the service identified in ../Reference/@href.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Body Reference</em>' containment reference.
     * @see #setBodyReference(BodyReferenceType)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_BodyReference()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='BodyReference' namespace='##targetNamespace'"
     * @generated
     */
    BodyReferenceType getBodyReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getBodyReference <em>Body Reference</em>}' containment reference.
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
     * The encoding of this input or requested for this output (e.g., UTF-8). This "encoding" shall be included whenever the encoding required is not the default encoding indicated in the Process full description. When included, this encoding shall be one published for this output or input in the Process full description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Encoding</em>' attribute.
     * @see #setEncoding(String)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Encoding()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='encoding'"
     * @generated
     */
    String getEncoding();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getEncoding <em>Encoding</em>}' attribute.
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
     * Reference to a web-accessible resource that can be used as input, or is provided by the process as output. This attribute shall contain a URL from which this input/output can be electronically retrieved.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Href()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

    /**
     * Returns the value of the '<em><b>Method</b></em>' attribute.
     * The default value is <code>"GET"</code>.
     * The literals are from the enumeration {@link net.opengis.wps10.MethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the HTTP method.  Allows a choice of GET or POST.  Default is GET.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method</em>' attribute.
     * @see net.opengis.wps10.MethodType
     * @see #isSetMethod()
     * @see #unsetMethod()
     * @see #setMethod(MethodType)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Method()
     * @model default="GET" unsettable="true"
     *        extendedMetaData="kind='attribute' name='method'"
     * @generated
     */
    MethodType getMethod();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getMethod <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method</em>' attribute.
     * @see net.opengis.wps10.MethodType
     * @see #isSetMethod()
     * @see #unsetMethod()
     * @see #getMethod()
     * @generated
     */
    void setMethod(MethodType value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.InputReferenceType#getMethod <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMethod()
     * @see #getMethod()
     * @see #setMethod(MethodType)
     * @generated
     */
    void unsetMethod();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.InputReferenceType#getMethod <em>Method</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Method</em>' attribute is set.
     * @see #unsetMethod()
     * @see #getMethod()
     * @see #setMethod(MethodType)
     * @generated
     */
    boolean isSetMethod();

    /**
     * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The Format of this input or requested for this output (e.g., text/xml). This element shall be omitted when the Format is indicated in the http header of the output. When included, this format shall be one published for this output or input in the Process full description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Mime Type</em>' attribute.
     * @see #setMimeType(String)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_MimeType()
     * @model dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='attribute' name='mimeType'"
     * @generated
     */
    String getMimeType();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getMimeType <em>Mime Type</em>}' attribute.
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
     * Web-accessible XML Schema Document that defines the content model of this complex resource (e.g., encoded using GML 2.2 Application Schema).  This reference should be included for XML encoded complex resources to facilitate validation.
     * PS I changed the name of this attribute to be consistent with the ProcessDescription.  The original was giving me validation troubles in XMLSpy.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Schema</em>' attribute.
     * @see #setSchema(String)
     * @see net.opengis.wps10.Wps10Package#getInputReferenceType_Schema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='schema'"
     * @generated
     */
    String getSchema();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputReferenceType#getSchema <em>Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema</em>' attribute.
     * @see #getSchema()
     * @generated
     */
    void setSchema(String value);

} // InputReferenceType
