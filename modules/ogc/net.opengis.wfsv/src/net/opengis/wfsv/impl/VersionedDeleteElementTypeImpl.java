/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfs.impl.DeleteElementTypeImpl;

import net.opengis.wfsv.VersionedDeleteElementType;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Versioned Delete Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.VersionedDeleteElementTypeImpl#getFeatureVersion <em>Feature Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VersionedDeleteElementTypeImpl extends DeleteElementTypeImpl implements VersionedDeleteElementType {
    /**
     * The default value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureVersion()
     * @generated
     * @ordered
     */
    protected static final String FEATURE_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureVersion()
     * @generated
     * @ordered
     */
    protected String featureVersion = FEATURE_VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected VersionedDeleteElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.VERSIONED_DELETE_ELEMENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFeatureVersion() {
        return featureVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureVersion(String newFeatureVersion) {
        String oldFeatureVersion = featureVersion;
        featureVersion = newFeatureVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION, oldFeatureVersion, featureVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION:
                return getFeatureVersion();
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
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION:
                setFeatureVersion((String)newValue);
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
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION:
                setFeatureVersion(FEATURE_VERSION_EDEFAULT);
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
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION:
                return FEATURE_VERSION_EDEFAULT == null ? featureVersion != null : !FEATURE_VERSION_EDEFAULT.equals(featureVersion);
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
        result.append(" (featureVersion: ");
        result.append(featureVersion);
        result.append(')');
        return result.toString();
    }

} //VersionedDeleteElementTypeImpl
