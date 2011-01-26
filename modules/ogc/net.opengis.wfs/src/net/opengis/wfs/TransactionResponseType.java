/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The response for a transaction request that was successfully
 *             completed. If the transaction failed for any reason, an
 *             exception report is returned instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionResponseType#getTransactionResults <em>Transaction Results</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionResponseType#getInsertResults <em>Insert Results</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionResponseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getTransactionResponseType()
 * @model extendedMetaData="name='TransactionResponseType' kind='elementOnly'"
 * @generated
 */
public interface TransactionResponseType extends EObject {
	/**
     * Returns the value of the '<em><b>Transaction Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The TransactionSummary element is used to summarize
     *                   the number of feature instances affected by the
     *                   transaction.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Transaction Summary</em>' containment reference.
     * @see #setTransactionSummary(TransactionSummaryType)
     * @see net.opengis.wfs.WfsPackage#getTransactionResponseType_TransactionSummary()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TransactionSummary' namespace='##targetNamespace'"
     * @generated
     */
	TransactionSummaryType getTransactionSummary();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Summary</em>' containment reference.
     * @see #getTransactionSummary()
     * @generated
     */
	void setTransactionSummary(TransactionSummaryType value);

	/**
     * Returns the value of the '<em><b>Transaction Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   For systems that do not support atomic transactions,
     *                   the TransactionResults element may be used to report
     *                   exception codes and messages for all actions of a
     *                   transaction that failed to execute successfully.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Transaction Results</em>' containment reference.
     * @see #setTransactionResults(TransactionResultsType)
     * @see net.opengis.wfs.WfsPackage#getTransactionResponseType_TransactionResults()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TransactionResults' namespace='##targetNamespace'"
     * @generated
     */
	TransactionResultsType getTransactionResults();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionResponseType#getTransactionResults <em>Transaction Results</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Results</em>' containment reference.
     * @see #getTransactionResults()
     * @generated
     */
	void setTransactionResults(TransactionResultsType value);

	/**
     * Returns the value of the '<em><b>Insert Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   A transaction is a collection of Insert,Update and Delete
     *                   actions.  The Update and Delete actions modify features
     *                   that already exist.  The Insert action, however, creates
     *                   new features.  The InsertResults element is used to
     *                   report the identifiers of the newly created features.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Insert Results</em>' containment reference.
     * @see #setInsertResults(InsertResultsType)
     * @see net.opengis.wfs.WfsPackage#getTransactionResponseType_InsertResults()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='InsertResults' namespace='##targetNamespace'"
     * @generated
     */
	InsertResultsType getInsertResults();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionResponseType#getInsertResults <em>Insert Results</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Insert Results</em>' containment reference.
     * @see #getInsertResults()
     * @generated
     */
	void setInsertResults(InsertResultsType value);

	/**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>"1.1.0"</code>.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The version attribute contains the version of the request
     *                that generated this response.  So a V1.1.0 transaction
     *                request generates a V1.1.0 transaction response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #setVersion(String)
     * @see net.opengis.wfs.WfsPackage#getTransactionResponseType_Version()
     * @model default="1.1.0" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
	String getVersion();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #getVersion()
     * @generated
     */
	void setVersion(String value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.TransactionResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
	void unsetVersion();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.TransactionResponseType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
	boolean isSetVersion();

} // TransactionResponseType
