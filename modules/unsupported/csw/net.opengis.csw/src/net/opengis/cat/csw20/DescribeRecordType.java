/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import javax.xml.namespace.QName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This request allows a user to discover elements of the
 *          information model supported by the catalogue. If no TypeName
 *          elements are included, then all of the schemas for the
 *          information model must be returned.
 * 
 *          schemaLanguage - preferred schema language
 *                           (W3C XML Schema by default)
 *          outputFormat - preferred output format (application/xml by default)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.DescribeRecordType#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DescribeRecordType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage <em>Schema Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordType()
 * @model extendedMetaData="name='DescribeRecordType' kind='elementOnly'"
 * @generated
 */
public interface DescribeRecordType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(QName)
     * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordType_TypeName()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.QName"
     *        extendedMetaData="kind='element' name='TypeName' namespace='##targetNamespace'"
     * @generated
     */
    QName getTypeName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(QName value);

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"application/xml"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #setOutputFormat(String)
     * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordType_OutputFormat()
     * @model default="application/xml" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    void unsetOutputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getOutputFormat <em>Output Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Output Format</em>' attribute is set.
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    boolean isSetOutputFormat();

    /**
     * Returns the value of the '<em><b>Schema Language</b></em>' attribute.
     * The default value is <code>"http://www.w3.org/XML/Schema"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema Language</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema Language</em>' attribute.
     * @see #isSetSchemaLanguage()
     * @see #unsetSchemaLanguage()
     * @see #setSchemaLanguage(String)
     * @see net.opengis.cat.csw20.Csw20Package#getDescribeRecordType_SchemaLanguage()
     * @model default="http://www.w3.org/XML/Schema" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='schemaLanguage'"
     * @generated
     */
    String getSchemaLanguage();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema Language</em>' attribute.
     * @see #isSetSchemaLanguage()
     * @see #unsetSchemaLanguage()
     * @see #getSchemaLanguage()
     * @generated
     */
    void setSchemaLanguage(String value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSchemaLanguage()
     * @see #getSchemaLanguage()
     * @see #setSchemaLanguage(String)
     * @generated
     */
    void unsetSchemaLanguage();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage <em>Schema Language</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Schema Language</em>' attribute is set.
     * @see #unsetSchemaLanguage()
     * @see #getSchemaLanguage()
     * @see #setSchemaLanguage(String)
     * @generated
     */
    boolean isSetSchemaLanguage();

} // DescribeRecordType
