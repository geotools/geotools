/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.DefaultType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Default Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.DefaultTypeImpl#getCRS <em>CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefaultTypeImpl extends EObjectImpl implements DefaultType {
    /**
     * The default value of the '{@link #getCRS() <em>CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCRS()
     * @generated
     * @ordered
     */
    protected static final String CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCRS() <em>CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCRS()
     * @generated
     * @ordered
     */
    protected String cRS = CRS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DefaultTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.DEFAULT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCRS() {
        return cRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCRS(String newCRS) {
        String oldCRS = cRS;
        cRS = newCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DEFAULT_TYPE__CRS, oldCRS, cRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.DEFAULT_TYPE__CRS:
                return getCRS();
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
            case Wps10Package.DEFAULT_TYPE__CRS:
                setCRS((String)newValue);
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
            case Wps10Package.DEFAULT_TYPE__CRS:
                setCRS(CRS_EDEFAULT);
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
            case Wps10Package.DEFAULT_TYPE__CRS:
                return CRS_EDEFAULT == null ? cRS != null : !CRS_EDEFAULT.equals(cRS);
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
        result.append(" (cRS: ");
        result.append(cRS);
        result.append(')');
        return result.toString();
    }

} //DefaultTypeImpl
