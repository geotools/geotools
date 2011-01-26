/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import java.util.Calendar;
import java.util.Collection;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.geotools.feature.FeatureCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl#getTimeStamp <em>Time Stamp</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl#getNumberOfFeatures <em>Number Of Features</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureCollectionTypeImpl extends EObjectImpl implements FeatureCollectionType {
	/**
     * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
	protected static final String LOCK_ID_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
	protected String lockId = LOCK_ID_EDEFAULT;

	/**
     * The default value of the '{@link #getTimeStamp() <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTimeStamp()
     * @generated
     * @ordered
     */
	protected static final Calendar TIME_STAMP_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTimeStamp() <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTimeStamp()
     * @generated
     * @ordered
     */
	protected Calendar timeStamp = TIME_STAMP_EDEFAULT;

	/**
     * The default value of the '{@link #getNumberOfFeatures() <em>Number Of Features</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getNumberOfFeatures()
     * @generated
     * @ordered
     */
	protected static final BigInteger NUMBER_OF_FEATURES_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getNumberOfFeatures() <em>Number Of Features</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getNumberOfFeatures()
     * @generated
     * @ordered
     */
	protected BigInteger numberOfFeatures = NUMBER_OF_FEATURES_EDEFAULT;

	/**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeature()
     * @generated
     * @ordered
     */
	protected EList feature;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected FeatureCollectionTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.FEATURE_COLLECTION_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getLockId() {
        return lockId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLockId(String newLockId) {
        String oldLockId = lockId;
        lockId = newLockId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_COLLECTION_TYPE__LOCK_ID, oldLockId, lockId));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Calendar getTimeStamp() {
        return timeStamp;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTimeStamp(Calendar newTimeStamp) {
        Calendar oldTimeStamp = timeStamp;
        timeStamp = newTimeStamp;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_COLLECTION_TYPE__TIME_STAMP, oldTimeStamp, timeStamp));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getNumberOfFeatures() {
        return numberOfFeatures;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setNumberOfFeatures(BigInteger newNumberOfFeatures) {
        BigInteger oldNumberOfFeatures = numberOfFeatures;
        numberOfFeatures = newNumberOfFeatures;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES, oldNumberOfFeatures, numberOfFeatures));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFeature() {
        if (feature == null) {
            feature = new EDataTypeUniqueEList(FeatureCollection.class, this, WfsPackage.FEATURE_COLLECTION_TYPE__FEATURE);
        }
        return feature;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.FEATURE_COLLECTION_TYPE__LOCK_ID:
                return getLockId();
            case WfsPackage.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                return getTimeStamp();
            case WfsPackage.FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES:
                return getNumberOfFeatures();
            case WfsPackage.FEATURE_COLLECTION_TYPE__FEATURE:
                return getFeature();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.FEATURE_COLLECTION_TYPE__LOCK_ID:
                setLockId((String)newValue);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                setTimeStamp((Calendar)newValue);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES:
                setNumberOfFeatures((BigInteger)newValue);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__FEATURE:
                getFeature().clear();
                getFeature().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.FEATURE_COLLECTION_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                setTimeStamp(TIME_STAMP_EDEFAULT);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES:
                setNumberOfFeatures(NUMBER_OF_FEATURES_EDEFAULT);
                return;
            case WfsPackage.FEATURE_COLLECTION_TYPE__FEATURE:
                getFeature().clear();
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.FEATURE_COLLECTION_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
            case WfsPackage.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                return TIME_STAMP_EDEFAULT == null ? timeStamp != null : !TIME_STAMP_EDEFAULT.equals(timeStamp);
            case WfsPackage.FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES:
                return NUMBER_OF_FEATURES_EDEFAULT == null ? numberOfFeatures != null : !NUMBER_OF_FEATURES_EDEFAULT.equals(numberOfFeatures);
            case WfsPackage.FEATURE_COLLECTION_TYPE__FEATURE:
                return feature != null && !feature.isEmpty();
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (lockId: ");
        result.append(lockId);
        result.append(", timeStamp: ");
        result.append(timeStamp);
        result.append(", numberOfFeatures: ");
        result.append(numberOfFeatures);
        result.append(", feature: ");
        result.append(feature);
        result.append(')');
        return result.toString();
    }

} //FeatureCollectionTypeImpl