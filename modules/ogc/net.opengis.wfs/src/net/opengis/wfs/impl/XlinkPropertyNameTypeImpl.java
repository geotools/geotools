/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import net.opengis.wfs.WfsPackage;
import net.opengis.wfs.XlinkPropertyNameType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Xlink Property Name Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.XlinkPropertyNameTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.XlinkPropertyNameTypeImpl#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.XlinkPropertyNameTypeImpl#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XlinkPropertyNameTypeImpl extends EObjectImpl implements XlinkPropertyNameType {
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
     * The default value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected static final String TRAVERSE_XLINK_DEPTH_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected String traverseXlinkDepth = TRAVERSE_XLINK_DEPTH_EDEFAULT;

	/**
     * The default value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected static final BigInteger TRAVERSE_XLINK_EXPIRY_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected BigInteger traverseXlinkExpiry = TRAVERSE_XLINK_EXPIRY_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected XlinkPropertyNameTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.XLINK_PROPERTY_NAME_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.XLINK_PROPERTY_NAME_TYPE__VALUE, oldValue, value));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getTraverseXlinkDepth() {
        return traverseXlinkDepth;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkDepth(String newTraverseXlinkDepth) {
        String oldTraverseXlinkDepth = traverseXlinkDepth;
        traverseXlinkDepth = newTraverseXlinkDepth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH, oldTraverseXlinkDepth, traverseXlinkDepth));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getTraverseXlinkExpiry() {
        return traverseXlinkExpiry;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkExpiry(BigInteger newTraverseXlinkExpiry) {
        BigInteger oldTraverseXlinkExpiry = traverseXlinkExpiry;
        traverseXlinkExpiry = newTraverseXlinkExpiry;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY, oldTraverseXlinkExpiry, traverseXlinkExpiry));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__VALUE:
                return getValue();
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH:
                return getTraverseXlinkDepth();
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY:
                return getTraverseXlinkExpiry();
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
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth((String)newValue);
                return;
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry((BigInteger)newValue);
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
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth(TRAVERSE_XLINK_DEPTH_EDEFAULT);
                return;
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry(TRAVERSE_XLINK_EXPIRY_EDEFAULT);
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
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH:
                return TRAVERSE_XLINK_DEPTH_EDEFAULT == null ? traverseXlinkDepth != null : !TRAVERSE_XLINK_DEPTH_EDEFAULT.equals(traverseXlinkDepth);
            case WfsPackage.XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY:
                return TRAVERSE_XLINK_EXPIRY_EDEFAULT == null ? traverseXlinkExpiry != null : !TRAVERSE_XLINK_EXPIRY_EDEFAULT.equals(traverseXlinkExpiry);
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
        result.append(", traverseXlinkDepth: ");
        result.append(traverseXlinkDepth);
        result.append(", traverseXlinkExpiry: ");
        result.append(traverseXlinkExpiry);
        result.append(')');
        return result.toString();
    }

} //XlinkPropertyNameTypeImpl