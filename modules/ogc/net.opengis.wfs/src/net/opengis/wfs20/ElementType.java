/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import net.opengis.ows11.MetadataType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ElementType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.ElementType#getValueList <em>Value List</em>}</li>
 *   <li>{@link net.opengis.wfs20.ElementType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.ElementType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getElementType()
 * @model extendedMetaData="name='ElementType' kind='elementOnly'"
 * @generated
 */
public interface ElementType extends EObject {
    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference.
     * @see #setMetadata(MetadataType)
     * @see net.opengis.wfs20.Wfs20Package#getElementType_Metadata()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    MetadataType getMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ElementType#getMetadata <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' containment reference.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(MetadataType value);

    /**
     * Returns the value of the '<em><b>Value List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value List</em>' containment reference.
     * @see #setValueList(ValueListType)
     * @see net.opengis.wfs20.Wfs20Package#getElementType_ValueList()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ValueList' namespace='##targetNamespace'"
     * @generated
     */
    ValueListType getValueList();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ElementType#getValueList <em>Value List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value List</em>' containment reference.
     * @see #getValueList()
     * @generated
     */
    void setValueList(ValueListType value);

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
     * @see net.opengis.wfs20.Wfs20Package#getElementType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ElementType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(QName)
     * @see net.opengis.wfs20.Wfs20Package#getElementType_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='type'"
     * @generated
     */
    QName getType();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ElementType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(QName value);

} // ElementType
