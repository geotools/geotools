/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insert Result Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Returns a "brief" view of any newly created catalogue records.
 *             The handle attribute may reference a particular statement in
 *             the corresponding transaction request.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.InsertResultType#getBriefRecord <em>Brief Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.InsertResultType#getHandleRef <em>Handle Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getInsertResultType()
 * @model extendedMetaData="name='InsertResultType' kind='elementOnly'"
 * @generated
 */
public interface InsertResultType extends EObject {
    /**
     * Returns the value of the '<em><b>Brief Record</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.BriefRecordType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Brief Record</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Brief Record</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getInsertResultType_BriefRecord()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='BriefRecord' namespace='##targetNamespace'"
     * @generated
     */
    EList<BriefRecordType> getBriefRecord();

    /**
     * Returns the value of the '<em><b>Handle Ref</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Handle Ref</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle Ref</em>' attribute.
     * @see #setHandleRef(String)
     * @see net.opengis.cat.csw20.Csw20Package#getInsertResultType_HandleRef()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='handleRef'"
     * @generated
     */
    String getHandleRef();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.InsertResultType#getHandleRef <em>Handle Ref</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle Ref</em>' attribute.
     * @see #getHandleRef()
     * @generated
     */
    void setHandleRef(String value);

} // InsertResultType
