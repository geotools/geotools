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
 * A representation of the model object '<em><b>Default Type2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DefaultType2#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDefaultType2()
 * @model extendedMetaData="name='Default_._type' kind='elementOnly'"
 * @generated
 */
public interface DefaultType2 extends EObject {
    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the default language supported by the service.  This language identifier shall be as specified in IETF RFC 4646.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see net.opengis.wps10.Wps10Package#getDefaultType2_Language()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Language" required="true"
     *        extendedMetaData="kind='element' name='Language' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DefaultType2#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

} // DefaultType2
