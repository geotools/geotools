/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Code Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.CodeTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.CodeTypeImpl#getCodeSpace <em>Code Space</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CodeTypeImpl extends EObjectImpl implements CodeType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getCodeSpace() <em>Code Space</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCodeSpace()
     * @generated
     * @ordered
     */
    protected static final String CODE_SPACE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCodeSpace() <em>Code Space</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCodeSpace()
     * @generated
     * @ordered
     */
    protected String codeSpace = CODE_SPACE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CodeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.CODE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.CODE_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCodeSpace() {
        return codeSpace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCodeSpace(String newCodeSpace) {
        String oldCodeSpace = codeSpace;
        codeSpace = newCodeSpace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.CODE_TYPE__CODE_SPACE, oldCodeSpace, codeSpace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.CODE_TYPE__VALUE:
                return getValue();
            case Ows11Package.CODE_TYPE__CODE_SPACE:
                return getCodeSpace();
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
            case Ows11Package.CODE_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case Ows11Package.CODE_TYPE__CODE_SPACE:
                setCodeSpace((String)newValue);
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
            case Ows11Package.CODE_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Ows11Package.CODE_TYPE__CODE_SPACE:
                setCodeSpace(CODE_SPACE_EDEFAULT);
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
            case Ows11Package.CODE_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Ows11Package.CODE_TYPE__CODE_SPACE:
                return CODE_SPACE_EDEFAULT == null ? codeSpace != null : !CODE_SPACE_EDEFAULT.equals(codeSpace);
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
        result.append(" (value: ");
        result.append(value);
        result.append(", codeSpace: ");
        result.append(codeSpace);
        result.append(')');
        return result.toString();
    }

} //CodeTypeImpl
