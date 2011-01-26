/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl;

import net.opengis.wfsv.DescribeVersionedFeatureTypeType;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Versioned Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.DescribeVersionedFeatureTypeTypeImpl#isVersioned <em>Versioned</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeVersionedFeatureTypeTypeImpl extends DescribeFeatureTypeTypeImpl implements DescribeVersionedFeatureTypeType {
    /**
     * The default value of the '{@link #isVersioned() <em>Versioned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVersioned()
     * @generated
     * @ordered
     */
    protected static final boolean VERSIONED_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isVersioned() <em>Versioned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVersioned()
     * @generated
     * @ordered
     */
    protected boolean versioned = VERSIONED_EDEFAULT;

    /**
     * This is true if the Versioned attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean versionedESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeVersionedFeatureTypeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isVersioned() {
        return versioned;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersioned(boolean newVersioned) {
        boolean oldVersioned = versioned;
        versioned = newVersioned;
        boolean oldVersionedESet = versionedESet;
        versionedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED, oldVersioned, versioned, !oldVersionedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVersioned() {
        boolean oldVersioned = versioned;
        boolean oldVersionedESet = versionedESet;
        versioned = VERSIONED_EDEFAULT;
        versionedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED, oldVersioned, VERSIONED_EDEFAULT, oldVersionedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVersioned() {
        return versionedESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED:
                return isVersioned() ? Boolean.TRUE : Boolean.FALSE;
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
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED:
                setVersioned(((Boolean)newValue).booleanValue());
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
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED:
                unsetVersioned();
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
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED:
                return isSetVersioned();
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
        result.append(" (versioned: ");
        if (versionedESet) result.append(versioned); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //DescribeVersionedFeatureTypeTypeImpl
