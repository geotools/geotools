/**
 */
package net.opengis.wps20.impl;

import net.opengis.ows20.impl.ValueTypeImpl;

import net.opengis.wps20.LiteralValueType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.LiteralValueTypeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralValueTypeImpl#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LiteralValueTypeImpl extends ValueTypeImpl implements LiteralValueType {
	/**
	 * The default value of the '{@link #getDataType() <em>Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataType()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataType() <em>Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataType()
	 * @generated
	 * @ordered
	 */
	protected String dataType = DATA_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUom() <em>Uom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUom()
	 * @generated
	 * @ordered
	 */
	protected static final String UOM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUom() <em>Uom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUom()
	 * @generated
	 * @ordered
	 */
	protected String uom = UOM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteralValueTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.LITERAL_VALUE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataType(String newDataType) {
		String oldDataType = dataType;
		dataType = newDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_VALUE_TYPE__DATA_TYPE, oldDataType, dataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUom(String newUom) {
		String oldUom = uom;
		uom = newUom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_VALUE_TYPE__UOM, oldUom, uom));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wps20Package.LITERAL_VALUE_TYPE__DATA_TYPE:
				return getDataType();
			case Wps20Package.LITERAL_VALUE_TYPE__UOM:
				return getUom();
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
			case Wps20Package.LITERAL_VALUE_TYPE__DATA_TYPE:
				setDataType((String)newValue);
				return;
			case Wps20Package.LITERAL_VALUE_TYPE__UOM:
				setUom((String)newValue);
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
			case Wps20Package.LITERAL_VALUE_TYPE__DATA_TYPE:
				setDataType(DATA_TYPE_EDEFAULT);
				return;
			case Wps20Package.LITERAL_VALUE_TYPE__UOM:
				setUom(UOM_EDEFAULT);
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
			case Wps20Package.LITERAL_VALUE_TYPE__DATA_TYPE:
				return DATA_TYPE_EDEFAULT == null ? dataType != null : !DATA_TYPE_EDEFAULT.equals(dataType);
			case Wps20Package.LITERAL_VALUE_TYPE__UOM:
				return UOM_EDEFAULT == null ? uom != null : !UOM_EDEFAULT.equals(uom);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (dataType: ");
		result.append(dataType);
		result.append(", uom: ");
		result.append(uom);
		result.append(')');
		return result.toString();
	}

} //LiteralValueTypeImpl
