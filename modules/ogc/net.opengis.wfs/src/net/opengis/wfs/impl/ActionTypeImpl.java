/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Action Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.ActionTypeImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.ActionTypeImpl#getCode <em>Code</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.ActionTypeImpl#getLocator <em>Locator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ActionTypeImpl extends EObjectImpl implements ActionType {
	/**
     * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMessage()
     * @generated
     * @ordered
     */
	protected static final String MESSAGE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMessage()
     * @generated
     * @ordered
     */
	protected String message = MESSAGE_EDEFAULT;

	/**
     * The default value of the '{@link #getCode() <em>Code</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getCode()
     * @generated
     * @ordered
     */
	protected static final String CODE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getCode() <em>Code</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getCode()
     * @generated
     * @ordered
     */
	protected String code = CODE_EDEFAULT;

	/**
     * The default value of the '{@link #getLocator() <em>Locator</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLocator()
     * @generated
     * @ordered
     */
	protected static final String LOCATOR_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getLocator() <em>Locator</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLocator()
     * @generated
     * @ordered
     */
	protected String locator = LOCATOR_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected ActionTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.ACTION_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getMessage() {
        return message;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setMessage(String newMessage) {
        String oldMessage = message;
        message = newMessage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.ACTION_TYPE__MESSAGE, oldMessage, message));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getCode() {
        return code;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setCode(String newCode) {
        String oldCode = code;
        code = newCode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.ACTION_TYPE__CODE, oldCode, code));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getLocator() {
        return locator;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLocator(String newLocator) {
        String oldLocator = locator;
        locator = newLocator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.ACTION_TYPE__LOCATOR, oldLocator, locator));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.ACTION_TYPE__MESSAGE:
                return getMessage();
            case WfsPackage.ACTION_TYPE__CODE:
                return getCode();
            case WfsPackage.ACTION_TYPE__LOCATOR:
                return getLocator();
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
            case WfsPackage.ACTION_TYPE__MESSAGE:
                setMessage((String)newValue);
                return;
            case WfsPackage.ACTION_TYPE__CODE:
                setCode((String)newValue);
                return;
            case WfsPackage.ACTION_TYPE__LOCATOR:
                setLocator((String)newValue);
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
            case WfsPackage.ACTION_TYPE__MESSAGE:
                setMessage(MESSAGE_EDEFAULT);
                return;
            case WfsPackage.ACTION_TYPE__CODE:
                setCode(CODE_EDEFAULT);
                return;
            case WfsPackage.ACTION_TYPE__LOCATOR:
                setLocator(LOCATOR_EDEFAULT);
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
            case WfsPackage.ACTION_TYPE__MESSAGE:
                return MESSAGE_EDEFAULT == null ? message != null : !MESSAGE_EDEFAULT.equals(message);
            case WfsPackage.ACTION_TYPE__CODE:
                return CODE_EDEFAULT == null ? code != null : !CODE_EDEFAULT.equals(code);
            case WfsPackage.ACTION_TYPE__LOCATOR:
                return LOCATOR_EDEFAULT == null ? locator != null : !LOCATOR_EDEFAULT.equals(locator);
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
        result.append(" (message: ");
        result.append(message);
        result.append(", code: ");
        result.append(code);
        result.append(", locator: ");
        result.append(locator);
        result.append(')');
        return result.toString();
    }

} //ActionTypeImpl