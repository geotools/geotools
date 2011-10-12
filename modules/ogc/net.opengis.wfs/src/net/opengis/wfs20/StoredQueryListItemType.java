/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stored Query List Item Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.StoredQueryListItemType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryListItemType#getReturnFeatureType <em>Return Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryListItemType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getStoredQueryListItemType()
 * @model extendedMetaData="name='StoredQueryListItemType' kind='elementOnly'"
 * @generated
 */
public interface StoredQueryListItemType extends EObject {
    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.TitleType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryListItemType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    EList<TitleType> getTitle();

    /**
     * Returns the value of the '<em><b>Return Feature Type</b></em>' attribute list.
     * The list contents are of type {@link javax.xml.namespace.QName}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Return Feature Type</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Return Feature Type</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryListItemType_ReturnFeatureType()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='element' name='ReturnFeatureType' namespace='##targetNamespace'"
     * @generated
     */
    EList<QName> getReturnFeatureType();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryListItemType_Id()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='id'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.StoredQueryListItemType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

} // StoredQueryListItemType
