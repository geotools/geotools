/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insert Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Submits one or more records to the catalogue. The representation
 *             is defined by the application profile. The handle attribute
 *             may be included to specify a local identifier for the action
 *             (it must be unique within the context of the transaction).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.InsertType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.InsertType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.InsertType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getInsertType()
 * @model extendedMetaData="name='InsertType' kind='elementOnly'"
 * @generated
 */
public interface InsertType extends EObject {
    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getInsertType_Any()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':0' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Handle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.cat.csw20.Csw20Package#getInsertType_Handle()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.InsertType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getInsertType_TypeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='typeName'"
     * @generated
     */
    String getTypeName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.InsertType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(String value);

} // InsertType
