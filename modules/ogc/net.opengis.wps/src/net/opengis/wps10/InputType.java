/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.LanguageStringType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Value of one input to a process.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.InputType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.InputType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wps10.InputType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wps10.InputType#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.InputType#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getInputType()
 * @model extendedMetaData="name='InputType' kind='elementOnly'"
 * @generated
 */
public interface InputType extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier or name of a process, unique for this server, or unambiguous identifier or name of an output, unique for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wps10.Wps10Package#getInputType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Title of a process or output, normally available for display to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference.
     * @see #setTitle(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getInputType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getTitle();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputType#getTitle <em>Title</em>}' containment reference.
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
     * Brief narrative description of a process or output, normally available for display to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference.
     * @see #setAbstract(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getInputType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputType#getAbstract <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' containment reference.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this input value as a web accessible resource, and references that resource.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(InputReferenceType)
     * @see net.opengis.wps10.Wps10Package#getInputType_Reference()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace'"
     * @generated
     */
    InputReferenceType getReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputType#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(InputReferenceType value);

    /**
     * Returns the value of the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this input value as a data embedded in this request, and includes that data.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data</em>' containment reference.
     * @see #setData(DataType)
     * @see net.opengis.wps10.Wps10Package#getInputType_Data()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Data' namespace='##targetNamespace'"
     * @generated
     */
    DataType getData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputType#getData <em>Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data</em>' containment reference.
     * @see #getData()
     * @generated
     */
    void setData(DataType value);

} // InputType
