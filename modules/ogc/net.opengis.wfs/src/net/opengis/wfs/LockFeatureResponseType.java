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
 * A representation of the model object '<em><b>Lock Feature Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The LockFeatureResponseType is used to define an
 *             element to contains the response to a LockFeature
 *             operation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.LockFeatureResponseType#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getLockFeatureResponseType()
 * @model extendedMetaData="name='LockFeatureResponseType' kind='elementOnly'"
 * @generated
 */
public interface LockFeatureResponseType extends EObject {
	/**
     * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The LockFeatureResponse includes a LockId element
     *                   that contains a lock identifier.  The lock identifier
     *                   can be used by a client, in subsequent operations, to
     *                   operate upon the locked feature instances.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lock Id</em>' attribute.
     * @see #setLockId(String)
     * @see net.opengis.wfs.WfsPackage#getLockFeatureResponseType_LockId()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='LockId' namespace='##targetNamespace'"
     * @generated
     */
	String getLockId();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockFeatureResponseType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
	void setLockId(String value);

	/**
     * Returns the value of the '<em><b>Features Locked</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The LockFeature or GetFeatureWithLock operations
     *                   identify and attempt to lock a set of feature
     *                   instances that satisfy the constraints specified
     *                   in the request.  In the event that the lockAction
     *                   attribute (on the LockFeature or GetFeatureWithLock
     *                   elements) is set to SOME, a Web Feature Service will
     *                   attempt to lock as many of the feature instances from
     *                   the result set as possible.
     * 
     *                   The FeaturesLocked element contains list of ogc:FeatureId
     *                   elements enumerating the feature instances that a WFS
     *                   actually managed to lock.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Features Locked</em>' containment reference.
     * @see #setFeaturesLocked(FeaturesLockedType)
     * @see net.opengis.wfs.WfsPackage#getLockFeatureResponseType_FeaturesLocked()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeaturesLocked' namespace='##targetNamespace'"
     * @generated
     */
	FeaturesLockedType getFeaturesLocked();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Features Locked</em>' containment reference.
     * @see #getFeaturesLocked()
     * @generated
     */
	void setFeaturesLocked(FeaturesLockedType value);

	/**
     * Returns the value of the '<em><b>Features Not Locked</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   In contrast to the FeaturesLocked element, the
     *                   FeaturesNotLocked element contains a list of
     *                   ogc:Filter elements identifying feature instances
     *                   that a WFS did not manage to lock because they were
     *                   already locked by another process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Features Not Locked</em>' containment reference.
     * @see #setFeaturesNotLocked(FeaturesNotLockedType)
     * @see net.opengis.wfs.WfsPackage#getLockFeatureResponseType_FeaturesNotLocked()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeaturesNotLocked' namespace='##targetNamespace'"
     * @generated
     */
	FeaturesNotLockedType getFeaturesNotLocked();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Features Not Locked</em>' containment reference.
     * @see #getFeaturesNotLocked()
     * @generated
     */
	void setFeaturesNotLocked(FeaturesNotLockedType value);

} // LockFeatureResponseType
