/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.CodeType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a format, encoding,  schema, and unit-of-measure for an output to be returned from a process.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.OutputDefinitionType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDefinitionType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDefinitionType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDefinitionType#getSchema <em>Schema</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDefinitionType#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType()
 * @model extendedMetaData="name='OutputDefinitionType' kind='elementOnly'"
 * @generated
 */
public interface OutputDefinitionType extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier or name of an output, unique for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDefinitionType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The encoding of this input or requested for this output (e.g., UTF-8). This "encoding" shall be included whenever the encoding required is not the default encoding indicated in the Process full description. When included, this encoding shall be one published for this output or input in the Process full description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Encoding</em>' attribute.
     * @see #setEncoding(String)
     * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType_Encoding()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='encoding'"
     * @generated
     */
    String getEncoding();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDefinitionType#getEncoding <em>Encoding</em>}' attribute.
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
     * The Format of this input or requested for this output (e.g., text/xml). This element shall be omitted when the Format is indicated in the http header of the output. When included, this format shall be one published for this output or input in the Process full description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Mime Type</em>' attribute.
     * @see #setMimeType(String)
     * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType_MimeType()
     * @model dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='attribute' name='mimeType'"
     * @generated
     */
    String getMimeType();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDefinitionType#getMimeType <em>Mime Type</em>}' attribute.
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
     * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType_Schema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='schema'"
     * @generated
     */
    String getSchema();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDefinitionType#getSchema <em>Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema</em>' attribute.
     * @see #getSchema()
     * @generated
     */
    void setSchema(String value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the unit of measure (if any) requested for this output. A uom can be referenced when a client wants to specify one of the units of measure supported for this output. This uom shall be a unit of measure referenced for this output of this process in the Process full description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.wps10.Wps10Package#getOutputDefinitionType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDefinitionType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // OutputDefinitionType
