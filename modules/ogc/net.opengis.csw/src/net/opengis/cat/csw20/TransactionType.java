/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Users may insert, update, or delete catalogue entries. If the
 *             verboseResponse attribute has the value "true", then one or more
 *             csw:InsertResult elements must be included in the response.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionType#isVerboseResponse <em>Verbose Response</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getTransactionType()
 * @model extendedMetaData="name='TransactionType' kind='elementOnly'"
 * @generated
 */
public interface TransactionType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:2'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Insert</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.InsertType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Insert</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Insert</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_Insert()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Insert' namespace='##targetNamespace' group='#group:2'"
     * @generated
     */
    EList<InsertType> getInsert();

    /**
     * Returns the value of the '<em><b>Update</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.UpdateType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Update</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Update</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_Update()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Update' namespace='##targetNamespace' group='#group:2'"
     * @generated
     */
    EList<UpdateType> getUpdate();

    /**
     * Returns the value of the '<em><b>Delete</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.DeleteType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Delete</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Delete</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_Delete()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Delete' namespace='##targetNamespace' group='#group:2'"
     * @generated
     */
    EList<DeleteType> getDelete();

    /**
     * Returns the value of the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Request Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Request Id</em>' attribute.
     * @see #setRequestId(String)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_RequestId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='requestId'"
     * @generated
     */
    String getRequestId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionType#getRequestId <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Id</em>' attribute.
     * @see #getRequestId()
     * @generated
     */
    void setRequestId(String value);

    /**
     * Returns the value of the '<em><b>Verbose Response</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Verbose Response</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Verbose Response</em>' attribute.
     * @see #isSetVerboseResponse()
     * @see #unsetVerboseResponse()
     * @see #setVerboseResponse(boolean)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionType_VerboseResponse()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='verboseResponse'"
     * @generated
     */
    boolean isVerboseResponse();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionType#isVerboseResponse <em>Verbose Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Verbose Response</em>' attribute.
     * @see #isSetVerboseResponse()
     * @see #unsetVerboseResponse()
     * @see #isVerboseResponse()
     * @generated
     */
    void setVerboseResponse(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.TransactionType#isVerboseResponse <em>Verbose Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVerboseResponse()
     * @see #isVerboseResponse()
     * @see #setVerboseResponse(boolean)
     * @generated
     */
    void unsetVerboseResponse();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.TransactionType#isVerboseResponse <em>Verbose Response</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Verbose Response</em>' attribute is set.
     * @see #unsetVerboseResponse()
     * @see #isVerboseResponse()
     * @see #setVerboseResponse(boolean)
     * @generated
     */
    boolean isSetVerboseResponse();

} // TransactionType
