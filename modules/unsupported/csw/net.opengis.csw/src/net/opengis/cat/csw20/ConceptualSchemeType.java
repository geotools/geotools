/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Conceptual Scheme Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.ConceptualSchemeType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.ConceptualSchemeType#getDocument <em>Document</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.ConceptualSchemeType#getAuthority <em>Authority</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getConceptualSchemeType()
 * @model extendedMetaData="name='ConceptualSchemeType' kind='elementOnly'"
 * @generated
 */
public interface ConceptualSchemeType extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getConceptualSchemeType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='Name' namespace='##targetNamespace'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.ConceptualSchemeType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Document</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Document</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Document</em>' attribute.
     * @see #setDocument(String)
     * @see net.opengis.cat.csw20.Csw20Package#getConceptualSchemeType_Document()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='Document' namespace='##targetNamespace'"
     * @generated
     */
    String getDocument();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.ConceptualSchemeType#getDocument <em>Document</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Document</em>' attribute.
     * @see #getDocument()
     * @generated
     */
    void setDocument(String value);

    /**
     * Returns the value of the '<em><b>Authority</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Authority</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Authority</em>' attribute.
     * @see #setAuthority(String)
     * @see net.opengis.cat.csw20.Csw20Package#getConceptualSchemeType_Authority()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='Authority' namespace='##targetNamespace'"
     * @generated
     */
    String getAuthority();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.ConceptualSchemeType#getAuthority <em>Authority</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Authority</em>' attribute.
     * @see #getAuthority()
     * @generated
     */
    void setAuthority(String value);

} // ConceptualSchemeType
