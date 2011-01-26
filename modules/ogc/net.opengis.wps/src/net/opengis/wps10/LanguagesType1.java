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
 * A representation of the model object '<em><b>Languages Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.LanguagesType1#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.LanguagesType1#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getLanguagesType1()
 * @model extendedMetaData="name='Languages_._type' kind='elementOnly'"
 * @generated
 */
public interface LanguagesType1 extends EObject {
    /**
     * Returns the value of the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the default language that will be used unless the operation request specifies another supported language.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' containment reference.
     * @see #setDefault(DefaultType2)
     * @see net.opengis.wps10.Wps10Package#getLanguagesType1_Default()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Default' namespace='##targetNamespace'"
     * @generated
     */
    DefaultType2 getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LanguagesType1#getDefault <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' containment reference.
     * @see #getDefault()
     * @generated
     */
    void setDefault(DefaultType2 value);

    /**
     * Returns the value of the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of references to all of the languages supported by this service. The default language shall be included in this list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported</em>' containment reference.
     * @see #setSupported(LanguagesType)
     * @see net.opengis.wps10.Wps10Package#getLanguagesType1_Supported()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Supported' namespace='##targetNamespace'"
     * @generated
     */
    LanguagesType getSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LanguagesType1#getSupported <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported</em>' containment reference.
     * @see #getSupported()
     * @generated
     */
    void setSupported(LanguagesType value);

} // LanguagesType1
