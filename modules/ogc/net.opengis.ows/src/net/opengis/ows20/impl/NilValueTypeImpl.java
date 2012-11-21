/**
 */
package net.opengis.ows20.impl;

import net.opengis.ows20.NilValueType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Nil Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.NilValueTypeImpl#getNilReason <em>Nil Reason</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NilValueTypeImpl extends CodeTypeImpl implements NilValueType {
    /**
     * The default value of the '{@link #getNilReason() <em>Nil Reason</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNilReason()
     * @generated
     * @ordered
     */
    protected static final String NIL_REASON_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNilReason() <em>Nil Reason</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNilReason()
     * @generated
     * @ordered
     */
    protected String nilReason = NIL_REASON_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NilValueTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.NIL_VALUE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNilReason() {
        return nilReason;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNilReason(String newNilReason) {
        String oldNilReason = nilReason;
        nilReason = newNilReason;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.NIL_VALUE_TYPE__NIL_REASON, oldNilReason, nilReason));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.NIL_VALUE_TYPE__NIL_REASON:
                return getNilReason();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows20Package.NIL_VALUE_TYPE__NIL_REASON:
                setNilReason((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Ows20Package.NIL_VALUE_TYPE__NIL_REASON:
                setNilReason(NIL_REASON_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows20Package.NIL_VALUE_TYPE__NIL_REASON:
                return NIL_REASON_EDEFAULT == null ? nilReason != null : !NIL_REASON_EDEFAULT.equals(nilReason);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (nilReason: ");
        result.append(nilReason);
        result.append(')');
        return result.toString();
    }

} //NilValueTypeImpl
