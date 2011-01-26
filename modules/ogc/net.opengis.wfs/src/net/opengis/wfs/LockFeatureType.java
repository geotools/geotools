/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lock Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines the LockFeature operation.  The LockFeature
 *             element contains one or more Lock elements that define which
 *             features of a particular type should be locked.  A lock
 *             identifier (lockId) is returned to the client application which
 *             can be used by subsequent operations to reference the locked
 *             features.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getLock <em>Lock</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getLockFeatureType()
 * @model extendedMetaData="name='LockFeatureType' kind='elementOnly'"
 * @generated
 */
public interface LockFeatureType extends BaseRequestType {
	/**
     * Returns the value of the '<em><b>Lock</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.LockType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                         The lock element is used to indicate which feature
     *                         instances of particular type are to be locked.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lock</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getLockFeatureType_Lock()
     * @model type="net.opengis.wfs.LockType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Lock' namespace='##targetNamespace'"
     * @generated
     */
	EList getLock();

	/**
     * Returns the value of the '<em><b>Expiry</b></em>' attribute.
     * The default value is <code>"5"</code>.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      The expiry attribute is used to set the length
     *                      of time (expressed in minutes) that features will
     *                      remain locked as a result of a LockFeature
     *                      request.  After the expiry period elapses, the
     *                      locked resources must be released.  If the
     *                      expiry attribute is not set, then the default
     *                      value of 5 minutes will be enforced.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #setExpiry(BigInteger)
     * @see net.opengis.wfs.WfsPackage#getLockFeatureType_Expiry()
     * @model default="5" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='expiry'"
     * @generated
     */
	BigInteger getExpiry();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @generated
     */
	void setExpiry(BigInteger value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
	void unsetExpiry();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Expiry</em>' attribute is set.
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
	boolean isSetExpiry();

	/**
     * Returns the value of the '<em><b>Lock Action</b></em>' attribute.
     * The default value is <code>"ALL"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs.AllSomeType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      The lockAction attribute is used to indicate what
     *                      a Web Feature Service should do when it encounters
     *                      a feature instance that has already been locked by
     *                      another client application.
     * 
     *                      Valid values are ALL or SOME.
     * 
     *                      ALL means that the Web Feature Service must acquire
     *                      locks on all the requested feature instances.  If it
     *                      cannot acquire those locks then the request should
     *                      fail.  In this instance, all locks acquired by the
     *                      operation should be released.
     * 
     *                      SOME means that the Web Feature Service should lock
     *                      as many of the requested features as it can.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lock Action</em>' attribute.
     * @see net.opengis.wfs.AllSomeType
     * @see #isSetLockAction()
     * @see #unsetLockAction()
     * @see #setLockAction(AllSomeType)
     * @see net.opengis.wfs.WfsPackage#getLockFeatureType_LockAction()
     * @model default="ALL" unique="false" unsettable="true"
     *        extendedMetaData="kind='attribute' name='lockAction'"
     * @generated
     */
	AllSomeType getLockAction();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Action</em>' attribute.
     * @see net.opengis.wfs.AllSomeType
     * @see #isSetLockAction()
     * @see #unsetLockAction()
     * @see #getLockAction()
     * @generated
     */
	void setLockAction(AllSomeType value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
	void unsetLockAction();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Lock Action</em>' attribute is set.
     * @see #unsetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
	boolean isSetLockAction();

} // LockFeatureType
