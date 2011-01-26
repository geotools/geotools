/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The TransactionType defines the Transaction operation.  A
 *             Transaction element contains one or more Insert, Update
 *             Delete and Native elements that allow a client application
 *             to create, modify or remove feature instances from the
 *             feature repository that a Web Feature Service controls.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.TransactionType#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionType#getReleaseAction <em>Release Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getTransactionType()
 * @model extendedMetaData="name='TransactionType' kind='elementOnly'"
 * @generated
 */
public interface TransactionType extends BaseRequestType {
	/**
     * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                         In order for a client application to operate upon
     *                         locked feature instances, the Transaction request
     *                         must include the LockId element.  The content of
     *                         this element must be the lock identifier the client
     *                         application obtained from a previous
     *                         GetFeatureWithLock or LockFeature operation.
     * 
     *                         If the correct lock identifier is specified the Web
     *                         Feature Service knows that the client application may
     *                         operate upon the locked feature instances.
     * 
     *                         No LockId element needs to be specified to operate upon
     *                         unlocked features.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lock Id</em>' attribute.
     * @see #setLockId(String)
     * @see net.opengis.wfs.WfsPackage#getTransactionType_LockId()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='LockId' namespace='##targetNamespace'"
     * @generated
     */
	String getLockId();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
	void setLockId(String value);

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
     * @see net.opengis.wfs.WfsPackage#getTransactionType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:4'"
     * @generated
     */
	FeatureMap getGroup();

	/**
     * Returns the value of the '<em><b>Insert</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.InsertElementType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insert</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Insert</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getTransactionType_Insert()
     * @model type="net.opengis.wfs.InsertElementType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Insert' namespace='##targetNamespace' group='#group:4'"
     * @generated
     */
	EList getInsert();

	/**
     * Returns the value of the '<em><b>Update</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.UpdateElementType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Update</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Update</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getTransactionType_Update()
     * @model type="net.opengis.wfs.UpdateElementType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Update' namespace='##targetNamespace' group='#group:4'"
     * @generated
     */
	EList getUpdate();

	/**
     * Returns the value of the '<em><b>Delete</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.DeleteElementType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delete</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Delete</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getTransactionType_Delete()
     * @model type="net.opengis.wfs.DeleteElementType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Delete' namespace='##targetNamespace' group='#group:4'"
     * @generated
     */
	EList getDelete();

	/**
     * Returns the value of the '<em><b>Native</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.NativeType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Native</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Native</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getTransactionType_Native()
     * @model type="net.opengis.wfs.NativeType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Native' namespace='##targetNamespace' group='#group:4'"
     * @generated
     */
	EList getNative();

	/**
     * Returns the value of the '<em><b>Release Action</b></em>' attribute.
     * The default value is <code>"ALL"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs.AllSomeType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      The releaseAction attribute is used to control how a Web
     *                      Feature service releases locks on feature instances after
     *                      a Transaction request has been processed.
     * 
     *                      Valid values are ALL or SOME.
     * 
     *                      A value of ALL means that the Web Feature Service should
     *                      release the locks of all feature instances locked with the
     *                      specified lockId regardless or whether or not the features
     *                      were actually modified.
     * 
     *                      A value of SOME means that the Web Feature Service will
     *                      only release the locks held on feature instances that
     *                      were actually operated upon by the transaction.  The
     *                      lockId that the client application obtained shall remain
     *                      valid and the other, unmodified, feature instances shall
     *                      remain locked.
     * 
     *                      If the expiry attribute was specified in the original
     *                      operation that locked the feature instances, then the
     *                      expiry counter will be reset to give the client
     *                      application that same amount of time to post subsequent
     *                      transactions against the locked features.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Release Action</em>' attribute.
     * @see net.opengis.wfs.AllSomeType
     * @see #isSetReleaseAction()
     * @see #unsetReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @see net.opengis.wfs.WfsPackage#getTransactionType_ReleaseAction()
     * @model default="ALL" unique="false" unsettable="true"
     *        extendedMetaData="kind='attribute' name='releaseAction'"
     * @generated
     */
	AllSomeType getReleaseAction();

	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionType#getReleaseAction <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Release Action</em>' attribute.
     * @see net.opengis.wfs.AllSomeType
     * @see #isSetReleaseAction()
     * @see #unsetReleaseAction()
     * @see #getReleaseAction()
     * @generated
     */
	void setReleaseAction(AllSomeType value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.TransactionType#getReleaseAction <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetReleaseAction()
     * @see #getReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @generated
     */
	void unsetReleaseAction();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.TransactionType#getReleaseAction <em>Release Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Release Action</em>' attribute is set.
     * @see #unsetReleaseAction()
     * @see #getReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @generated
     */
	boolean isSetReleaseAction();

} // TransactionType
