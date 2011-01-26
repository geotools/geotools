/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Keywords Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can reference the type name authority and/or thesaurus.
 * 			If the xml:lang attribute is not included in a Keyword element, then no language is specified for that element unless specified by another means.  All Keyword elements in the same Keywords element that share the same xml:lang attribute value represent different keywords in that language. 
 * For OWS use, the optional thesaurusName element was omitted as being complex information that could be referenced by the codeSpace attribute of the Type element. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.KeywordsType#getKeyword <em>Keyword</em>}</li>
 *   <li>{@link net.opengis.ows11.KeywordsType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getKeywordsType()
 * @model extendedMetaData="name='KeywordsType' kind='elementOnly'"
 * @generated
 */
public interface KeywordsType extends EObject {
    /**
     * Returns the value of the '<em><b>Keyword</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.LanguageStringType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keyword</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keyword</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getKeywordsType_Keyword()
     * @model type="net.opengis.ows11.LanguageStringType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Keyword' namespace='##targetNamespace'"
     * @generated
     */
    EList getKeyword();

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(CodeType)
     * @see net.opengis.ows11.Ows11Package#getKeywordsType_Type()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Type' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getType();

    /**
     * Sets the value of the '{@link net.opengis.ows11.KeywordsType#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(CodeType value);

} // KeywordsType
