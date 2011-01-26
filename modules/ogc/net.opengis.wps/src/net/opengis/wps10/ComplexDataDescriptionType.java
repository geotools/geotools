/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Data Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A combination of format, encoding, and/or schema supported by a process input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ComplexDataDescriptionType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps10.ComplexDataDescriptionType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps10.ComplexDataDescriptionType#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getComplexDataDescriptionType()
 * @model extendedMetaData="name='ComplexDataDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface ComplexDataDescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Mime type supported for this input or output (e.g., text/xml).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Mime Type</em>' attribute.
     * @see #setMimeType(String)
     * @see net.opengis.wps10.Wps10Package#getComplexDataDescriptionType_MimeType()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='MimeType'"
     * @generated
     */
    String getMimeType();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ComplexDataDescriptionType#getMimeType <em>Mime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mime Type</em>' attribute.
     * @see #getMimeType()
     * @generated
     */
    void setMimeType(String value);

    /**
     * Returns the value of the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to an encoding supported for this input or output (e.g., UTF-8).  This element shall be omitted if Encoding does not apply to this Input/Output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Encoding</em>' attribute.
     * @see #setEncoding(String)
     * @see net.opengis.wps10.Wps10Package#getComplexDataDescriptionType_Encoding()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='Encoding'"
     * @generated
     */
    String getEncoding();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ComplexDataDescriptionType#getEncoding <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Encoding</em>' attribute.
     * @see #getEncoding()
     * @generated
     */
    void setEncoding(String value);

    /**
     * Returns the value of the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a definition of XML elements or types supported for this Input/Output (e.g., GML 2.1 Application Schema). Each of these XML elements or types shall be defined in a separate XML Schema Document. This parameter shall be included when this input/output is XML encoded using an XML schema. When included, the input/output shall validate against the referenced XML Schema. This element shall be omitted if Schema does not apply to this Input/Output. Note: If the Input/Output uses a profile of a larger schema, the server administrator should provide that schema profile for validation purposes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Schema</em>' attribute.
     * @see #setSchema(String)
     * @see net.opengis.wps10.Wps10Package#getComplexDataDescriptionType_Schema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='Schema'"
     * @generated
     */
    String getSchema();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ComplexDataDescriptionType#getSchema <em>Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema</em>' attribute.
     * @see #getSchema()
     * @generated
     */
    void setSchema(String value);

} // ComplexDataDescriptionType
