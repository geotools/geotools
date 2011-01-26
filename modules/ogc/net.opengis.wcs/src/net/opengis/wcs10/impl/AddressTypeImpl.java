/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.AddressType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Address Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getDeliveryPoint <em>Delivery Point</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getCity <em>City</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getAdministrativeArea <em>Administrative Area</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getPostalCode <em>Postal Code</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getCountry <em>Country</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AddressTypeImpl#getElectronicMailAddress <em>Electronic Mail Address</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddressTypeImpl extends EObjectImpl implements AddressType {
    /**
	 * The default value of the '{@link #getDeliveryPoint() <em>Delivery Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryPoint()
	 * @generated
	 * @ordered
	 */
	protected static final String DELIVERY_POINT_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getDeliveryPoint() <em>Delivery Point</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDeliveryPoint()
	 * @generated
	 * @ordered
	 */
    protected String deliveryPoint = DELIVERY_POINT_EDEFAULT;

    /**
	 * The default value of the '{@link #getCity() <em>City</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCity()
	 * @generated
	 * @ordered
	 */
    protected static final String CITY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCity() <em>City</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCity()
	 * @generated
	 * @ordered
	 */
    protected String city = CITY_EDEFAULT;

    /**
	 * The default value of the '{@link #getAdministrativeArea() <em>Administrative Area</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAdministrativeArea()
	 * @generated
	 * @ordered
	 */
    protected static final String ADMINISTRATIVE_AREA_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getAdministrativeArea() <em>Administrative Area</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAdministrativeArea()
	 * @generated
	 * @ordered
	 */
    protected String administrativeArea = ADMINISTRATIVE_AREA_EDEFAULT;

    /**
	 * The default value of the '{@link #getPostalCode() <em>Postal Code</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getPostalCode()
	 * @generated
	 * @ordered
	 */
    protected static final String POSTAL_CODE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPostalCode() <em>Postal Code</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getPostalCode()
	 * @generated
	 * @ordered
	 */
    protected String postalCode = POSTAL_CODE_EDEFAULT;

    /**
	 * The default value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
    protected static final String COUNTRY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
    protected String country = COUNTRY_EDEFAULT;

    /**
	 * The default value of the '{@link #getElectronicMailAddress() <em>Electronic Mail Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElectronicMailAddress()
	 * @generated
	 * @ordered
	 */
	protected static final String ELECTRONIC_MAIL_ADDRESS_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getElectronicMailAddress() <em>Electronic Mail Address</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getElectronicMailAddress()
	 * @generated
	 * @ordered
	 */
    protected String electronicMailAddress = ELECTRONIC_MAIL_ADDRESS_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected AddressTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.ADDRESS_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDeliveryPoint() {
		return deliveryPoint;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeliveryPoint(String newDeliveryPoint) {
		String oldDeliveryPoint = deliveryPoint;
		deliveryPoint = newDeliveryPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__DELIVERY_POINT, oldDeliveryPoint, deliveryPoint));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCity() {
		return city;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCity(String newCity) {
		String oldCity = city;
		city = newCity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__CITY, oldCity, city));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getAdministrativeArea() {
		return administrativeArea;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAdministrativeArea(String newAdministrativeArea) {
		String oldAdministrativeArea = administrativeArea;
		administrativeArea = newAdministrativeArea;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA, oldAdministrativeArea, administrativeArea));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPostalCode() {
		return postalCode;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPostalCode(String newPostalCode) {
		String oldPostalCode = postalCode;
		postalCode = newPostalCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__POSTAL_CODE, oldPostalCode, postalCode));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCountry() {
		return country;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCountry(String newCountry) {
		String oldCountry = country;
		country = newCountry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__COUNTRY, oldCountry, country));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getElectronicMailAddress() {
		return electronicMailAddress;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElectronicMailAddress(String newElectronicMailAddress) {
		String oldElectronicMailAddress = electronicMailAddress;
		electronicMailAddress = newElectronicMailAddress;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS, oldElectronicMailAddress, electronicMailAddress));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.ADDRESS_TYPE__DELIVERY_POINT:
				return getDeliveryPoint();
			case Wcs10Package.ADDRESS_TYPE__CITY:
				return getCity();
			case Wcs10Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
				return getAdministrativeArea();
			case Wcs10Package.ADDRESS_TYPE__POSTAL_CODE:
				return getPostalCode();
			case Wcs10Package.ADDRESS_TYPE__COUNTRY:
				return getCountry();
			case Wcs10Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
				return getElectronicMailAddress();
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
			case Wcs10Package.ADDRESS_TYPE__DELIVERY_POINT:
				setDeliveryPoint((String)newValue);
				return;
			case Wcs10Package.ADDRESS_TYPE__CITY:
				setCity((String)newValue);
				return;
			case Wcs10Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
				setAdministrativeArea((String)newValue);
				return;
			case Wcs10Package.ADDRESS_TYPE__POSTAL_CODE:
				setPostalCode((String)newValue);
				return;
			case Wcs10Package.ADDRESS_TYPE__COUNTRY:
				setCountry((String)newValue);
				return;
			case Wcs10Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
				setElectronicMailAddress((String)newValue);
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
			case Wcs10Package.ADDRESS_TYPE__DELIVERY_POINT:
				setDeliveryPoint(DELIVERY_POINT_EDEFAULT);
				return;
			case Wcs10Package.ADDRESS_TYPE__CITY:
				setCity(CITY_EDEFAULT);
				return;
			case Wcs10Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
				setAdministrativeArea(ADMINISTRATIVE_AREA_EDEFAULT);
				return;
			case Wcs10Package.ADDRESS_TYPE__POSTAL_CODE:
				setPostalCode(POSTAL_CODE_EDEFAULT);
				return;
			case Wcs10Package.ADDRESS_TYPE__COUNTRY:
				setCountry(COUNTRY_EDEFAULT);
				return;
			case Wcs10Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
				setElectronicMailAddress(ELECTRONIC_MAIL_ADDRESS_EDEFAULT);
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
			case Wcs10Package.ADDRESS_TYPE__DELIVERY_POINT:
				return DELIVERY_POINT_EDEFAULT == null ? deliveryPoint != null : !DELIVERY_POINT_EDEFAULT.equals(deliveryPoint);
			case Wcs10Package.ADDRESS_TYPE__CITY:
				return CITY_EDEFAULT == null ? city != null : !CITY_EDEFAULT.equals(city);
			case Wcs10Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
				return ADMINISTRATIVE_AREA_EDEFAULT == null ? administrativeArea != null : !ADMINISTRATIVE_AREA_EDEFAULT.equals(administrativeArea);
			case Wcs10Package.ADDRESS_TYPE__POSTAL_CODE:
				return POSTAL_CODE_EDEFAULT == null ? postalCode != null : !POSTAL_CODE_EDEFAULT.equals(postalCode);
			case Wcs10Package.ADDRESS_TYPE__COUNTRY:
				return COUNTRY_EDEFAULT == null ? country != null : !COUNTRY_EDEFAULT.equals(country);
			case Wcs10Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
				return ELECTRONIC_MAIL_ADDRESS_EDEFAULT == null ? electronicMailAddress != null : !ELECTRONIC_MAIL_ADDRESS_EDEFAULT.equals(electronicMailAddress);
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
		result.append(" (deliveryPoint: ");
		result.append(deliveryPoint);
		result.append(", city: ");
		result.append(city);
		result.append(", administrativeArea: ");
		result.append(administrativeArea);
		result.append(", postalCode: ");
		result.append(postalCode);
		result.append(", country: ");
		result.append(country);
		result.append(", electronicMailAddress: ");
		result.append(electronicMailAddress);
		result.append(')');
		return result.toString();
	}

} //AddressTypeImpl
