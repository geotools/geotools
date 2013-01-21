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
 * A representation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The response for a transaction request that was successfully
 *             completed. If the transaction failed for any reason, a service
 *             exception report indicating a TransactionFailure is returned
 *             instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionResponseType#getInsertResult <em>Insert Result</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.TransactionResponseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getTransactionResponseType()
 * @model extendedMetaData="name='TransactionResponseType' kind='elementOnly'"
 * @generated
 */
public interface TransactionResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Transaction Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transaction Summary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transaction Summary</em>' containment reference.
     * @see #setTransactionSummary(TransactionSummaryType)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionResponseType_TransactionSummary()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TransactionSummary' namespace='##targetNamespace'"
     * @generated
     */
    TransactionSummaryType getTransactionSummary();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Summary</em>' containment reference.
     * @see #getTransactionSummary()
     * @generated
     */
    void setTransactionSummary(TransactionSummaryType value);

    /**
     * Returns the value of the '<em><b>Insert Result</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.InsertResultType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Insert Result</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Insert Result</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionResponseType_InsertResult()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='InsertResult' namespace='##targetNamespace'"
     * @generated
     */
    EList<InsertResultType> getInsertResult();

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.cat.csw20.Csw20Package#getTransactionResponseType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.TransactionResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

} // TransactionResponseType
