/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.opengis.filter.identity.FeatureId;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inserted Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.InsertedFeatureTypeImpl#getFeatureId <em>Feature Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.InsertedFeatureTypeImpl#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InsertedFeatureTypeImpl extends EObjectImpl implements InsertedFeatureType {
	/**
     * The cached value of the '{@link #getFeatureId() <em>Feature Id</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeatureId()
     * @generated
     * @ordered
     */
	protected EList featureId;

	/**
     * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected static final String HANDLE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected String handle = HANDLE_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected InsertedFeatureTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.INSERTED_FEATURE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFeatureId() {
        if (featureId == null) {
            featureId = new EDataTypeUniqueEList(FeatureId.class, this, WfsPackage.INSERTED_FEATURE_TYPE__FEATURE_ID);
        }
        return featureId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getHandle() {
        return handle;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setHandle(String newHandle) {
        String oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.INSERTED_FEATURE_TYPE__HANDLE, oldHandle, handle));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.INSERTED_FEATURE_TYPE__FEATURE_ID:
                return getFeatureId();
            case WfsPackage.INSERTED_FEATURE_TYPE__HANDLE:
                return getHandle();
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
            case WfsPackage.INSERTED_FEATURE_TYPE__FEATURE_ID:
                getFeatureId().clear();
                getFeatureId().addAll((Collection)newValue);
                return;
            case WfsPackage.INSERTED_FEATURE_TYPE__HANDLE:
                setHandle((String)newValue);
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
            case WfsPackage.INSERTED_FEATURE_TYPE__FEATURE_ID:
                getFeatureId().clear();
                return;
            case WfsPackage.INSERTED_FEATURE_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
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
            case WfsPackage.INSERTED_FEATURE_TYPE__FEATURE_ID:
                return featureId != null && !featureId.isEmpty();
            case WfsPackage.INSERTED_FEATURE_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
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
        result.append(" (featureId: ");
        result.append(featureId);
        result.append(", handle: ");
        result.append(handle);
        result.append(')');
        return result.toString();
    }

} //InsertedFeatureTypeImpl