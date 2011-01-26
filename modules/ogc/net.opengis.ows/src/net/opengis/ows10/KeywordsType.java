/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Keywords Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can reference the type name authority and/or thesaurus.
 * For OWS use, the optional thesaurusName element was omitted as being complex information that could be referenced by the codeSpace attribute of the Type element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.KeywordsType#getKeyword <em>Keyword</em>}</li>
 *   <li>{@link net.opengis.ows10.KeywordsType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getKeywordsType()
 * @model extendedMetaData="name='KeywordsType' kind='elementOnly'"
 * @generated
 */
public interface KeywordsType extends EObject {
	/**
	 * Returns the value of the '<em><b>Keyword</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keyword</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Keyword</em>' attribute list.
	 * @see net.opengis.ows10.Ows10Package#getKeywordsType_Keyword()
	 * @model dataType="net.opengis.ows10.MimeType"
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
	 * @see net.opengis.ows10.Ows10Package#getKeywordsType_Type()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Type' namespace='##targetNamespace'"
	 * @generated
	 */
	CodeType getType();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.KeywordsType#getType <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' containment reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(CodeType value);

} // KeywordsType
