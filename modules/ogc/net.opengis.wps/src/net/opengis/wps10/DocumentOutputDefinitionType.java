/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.LanguageStringType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a format, encoding,  schema, and unit-of-measure for an output to be returned from a process.
 * In this use, the DescriptionType shall describe this process input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DocumentOutputDefinitionType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentOutputDefinitionType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentOutputDefinitionType#isAsReference <em>As Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDocumentOutputDefinitionType()
 * @model extendedMetaData="name='DocumentOutputDefinitionType' kind='elementOnly'"
 * @generated
 */
public interface DocumentOutputDefinitionType extends OutputDefinitionType {
    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Title of the process output, normally available for display to a human. This element should be used if the client wishes to customize the Title in the execute response. This element should not be used if the Title provided for this output in the ProcessDescription is adequate.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference.
     * @see #setTitle(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getDocumentOutputDefinitionType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getTitle();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentOutputDefinitionType#getTitle <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' containment reference.
     * @see #getTitle()
     * @generated
     */
    void setTitle(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Brief narrative description of a process output, normally available for display to a human. This element should be used if the client wishes to customize the Abstract in the execute response. This element should not be used if the Abstract provided for this output in the ProcessDescription is adequate.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference.
     * @see #setAbstract(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getDocumentOutputDefinitionType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentOutputDefinitionType#getAbstract <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' containment reference.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>As Reference</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies if this output should be stored by the process as a web-accessible resource. If asReference is "true", the server shall store this output so that the client can retrieve it as required. If store is "false", all the output shall be encoded in the Execute operation response document. This parameter only applies to ComplexData outputs.  This parameter shall not be included unless the corresponding "storeSupported" parameter is included and is "true" in the ProcessDescription for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>As Reference</em>' attribute.
     * @see #isSetAsReference()
     * @see #unsetAsReference()
     * @see #setAsReference(boolean)
     * @see net.opengis.wps10.Wps10Package#getDocumentOutputDefinitionType_AsReference()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='asReference'"
     * @generated
     */
    boolean isAsReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentOutputDefinitionType#isAsReference <em>As Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>As Reference</em>' attribute.
     * @see #isSetAsReference()
     * @see #unsetAsReference()
     * @see #isAsReference()
     * @generated
     */
    void setAsReference(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.DocumentOutputDefinitionType#isAsReference <em>As Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAsReference()
     * @see #isAsReference()
     * @see #setAsReference(boolean)
     * @generated
     */
    void unsetAsReference();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.DocumentOutputDefinitionType#isAsReference <em>As Reference</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>As Reference</em>' attribute is set.
     * @see #unsetAsReference()
     * @see #isAsReference()
     * @see #setAsReference(boolean)
     * @generated
     */
    boolean isSetAsReference();

} // DocumentOutputDefinitionType
