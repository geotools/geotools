/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Summary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *          Reports the total number of catalogue items modified by a transaction
 *          request (i.e, inserted, updated, deleted). If the client did not
 *          specify a requestId, the server may assign one (a URI value).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalInserted <em>Total Inserted</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalUpdated <em>Total Updated</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalDeleted <em>Total Deleted</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionSummaryType#getRequestId <em>Request Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getTransactionSummaryType()
 * @model extendedMetaData="name='TransactionSummaryType' kind='elementOnly'"
 * @generated
 */
public interface TransactionSummaryType extends EObject {
    /**
     * Returns the value of the '<em><b>Total Inserted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Total Inserted</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Total Inserted</em>' attribute.
     * @see #setTotalInserted(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionSummaryType_TotalInserted()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='element' name='totalInserted' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTotalInserted();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalInserted <em>Total Inserted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Total Inserted</em>' attribute.
     * @see #getTotalInserted()
     * @generated
     */
    void setTotalInserted(BigInteger value);

    /**
     * Returns the value of the '<em><b>Total Updated</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Total Updated</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Total Updated</em>' attribute.
     * @see #setTotalUpdated(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionSummaryType_TotalUpdated()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='element' name='totalUpdated' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTotalUpdated();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalUpdated <em>Total Updated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Total Updated</em>' attribute.
     * @see #getTotalUpdated()
     * @generated
     */
    void setTotalUpdated(BigInteger value);

    /**
     * Returns the value of the '<em><b>Total Deleted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Total Deleted</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Total Deleted</em>' attribute.
     * @see #setTotalDeleted(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionSummaryType_TotalDeleted()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='element' name='totalDeleted' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTotalDeleted();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalDeleted <em>Total Deleted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Total Deleted</em>' attribute.
     * @see #getTotalDeleted()
     * @generated
     */
    void setTotalDeleted(BigInteger value);

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
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionSummaryType_RequestId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='requestId'"
     * @generated
     */
    String getRequestId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionSummaryType#getRequestId <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Id</em>' attribute.
     * @see #getRequestId()
     * @generated
     */
    void setRequestId(String value);

} // TransactionSummaryType
