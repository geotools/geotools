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
 * A representation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Human-readable descriptive information for the object it is included within.
 * This type shall be extended if needed for specific OWS use to include additional metadata for each type of information. This type shall not be restricted for a specific OWS to change the multiplicity (or optionality) of some elements.
 * 			If the xml:lang attribute is not included in a Title, Abstract or Keyword element, then no language is specified for that element unless specified by another means.  All Title, Abstract and Keyword elements in the same Description that share the same xml:lang attribute value represent the description of the parent object in that language. Multiple Title or Abstract elements shall not exist in the same Description with the same xml:lang attribute value unless otherwise specified. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.DescriptionType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows11.DescriptionType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows11.DescriptionType#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getDescriptionType()
 * @model extendedMetaData="name='DescriptionType' kind='elementOnly'"
 * @generated
 */
public interface DescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.LanguageStringType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Title of this resource, normally used for display to a human. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDescriptionType_Title()
     * @model type="net.opengis.ows11.LanguageStringType" containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    EList getTitle();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.LanguageStringType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Brief narrative description of this resource, normally used for display to a human. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDescriptionType_Abstract()
     * @model type="net.opengis.ows11.LanguageStringType" containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    EList getAbstract();

    /**
     * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.KeywordsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keywords</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keywords</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDescriptionType_Keywords()
     * @model type="net.opengis.ows11.KeywordsType" containment="true"
     *        extendedMetaData="kind='element' name='Keywords' namespace='##targetNamespace'"
     * @generated
     */
    EList getKeywords();

} // DescriptionType
