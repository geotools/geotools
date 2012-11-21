/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Languages Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.LanguagesType#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getLanguagesType()
 * @model extendedMetaData="name='Languages_._type' kind='elementOnly'"
 * @generated
 */
public interface LanguagesType extends EObject {
    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of a language used by the data(set) contents.
     *       This language identifier shall be as specified in IETF RFC 4646. The
     *       language tags shall be either complete 5 character codes (e.g. "en-CA"),
     *       or abbreviated 2 character codes (e.g. "en"). In addition to the RFC
     *       4646 codes, the server shall support the single special value "
     * " which
     *       is used to indicate "any language".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see net.opengis.ows20.Ows20Package#getLanguagesType_Language()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Language" required="true"
     *        extendedMetaData="kind='element' name='Language' namespace='##targetNamespace'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link net.opengis.ows20.LanguagesType#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

} // LanguagesType
