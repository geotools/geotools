/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.AddressType;
import net.opengis.ows11.Ows11Package;

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
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getDeliveryPoint <em>Delivery Point</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getCity <em>City</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getAdministrativeArea <em>Administrative Area</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getPostalCode <em>Postal Code</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getCountry <em>Country</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AddressTypeImpl#getElectronicMailAddress <em>Electronic Mail Address</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddressTypeImpl extends EObjectImpl implements AddressType {
    /**
     * The cached value of the '{@link #getDeliveryPoint() <em>Delivery Point</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeliveryPoint()
     * @generated
     * @ordered
     */
    protected EList deliveryPoint;

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
     * The cached value of the '{@link #getElectronicMailAddress() <em>Electronic Mail Address</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElectronicMailAddress()
     * @generated
     * @ordered
     */
    protected EList electronicMailAddress;

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
        return Ows11Package.Literals.ADDRESS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getDeliveryPoint() {
        if (deliveryPoint == null) {
            deliveryPoint = new EDataTypeEList(String.class, this, Ows11Package.ADDRESS_TYPE__DELIVERY_POINT);
        }
        return deliveryPoint;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ADDRESS_TYPE__CITY, oldCity, city));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA, oldAdministrativeArea, administrativeArea));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ADDRESS_TYPE__POSTAL_CODE, oldPostalCode, postalCode));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ADDRESS_TYPE__COUNTRY, oldCountry, country));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getElectronicMailAddress() {
        if (electronicMailAddress == null) {
            electronicMailAddress = new EDataTypeEList(String.class, this, Ows11Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS);
        }
        return electronicMailAddress;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.ADDRESS_TYPE__DELIVERY_POINT:
                return getDeliveryPoint();
            case Ows11Package.ADDRESS_TYPE__CITY:
                return getCity();
            case Ows11Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
                return getAdministrativeArea();
            case Ows11Package.ADDRESS_TYPE__POSTAL_CODE:
                return getPostalCode();
            case Ows11Package.ADDRESS_TYPE__COUNTRY:
                return getCountry();
            case Ows11Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
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
            case Ows11Package.ADDRESS_TYPE__DELIVERY_POINT:
                getDeliveryPoint().clear();
                getDeliveryPoint().addAll((Collection)newValue);
                return;
            case Ows11Package.ADDRESS_TYPE__CITY:
                setCity((String)newValue);
                return;
            case Ows11Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
                setAdministrativeArea((String)newValue);
                return;
            case Ows11Package.ADDRESS_TYPE__POSTAL_CODE:
                setPostalCode((String)newValue);
                return;
            case Ows11Package.ADDRESS_TYPE__COUNTRY:
                setCountry((String)newValue);
                return;
            case Ows11Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
                getElectronicMailAddress().clear();
                getElectronicMailAddress().addAll((Collection)newValue);
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
            case Ows11Package.ADDRESS_TYPE__DELIVERY_POINT:
                getDeliveryPoint().clear();
                return;
            case Ows11Package.ADDRESS_TYPE__CITY:
                setCity(CITY_EDEFAULT);
                return;
            case Ows11Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
                setAdministrativeArea(ADMINISTRATIVE_AREA_EDEFAULT);
                return;
            case Ows11Package.ADDRESS_TYPE__POSTAL_CODE:
                setPostalCode(POSTAL_CODE_EDEFAULT);
                return;
            case Ows11Package.ADDRESS_TYPE__COUNTRY:
                setCountry(COUNTRY_EDEFAULT);
                return;
            case Ows11Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
                getElectronicMailAddress().clear();
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
            case Ows11Package.ADDRESS_TYPE__DELIVERY_POINT:
                return deliveryPoint != null && !deliveryPoint.isEmpty();
            case Ows11Package.ADDRESS_TYPE__CITY:
                return CITY_EDEFAULT == null ? city != null : !CITY_EDEFAULT.equals(city);
            case Ows11Package.ADDRESS_TYPE__ADMINISTRATIVE_AREA:
                return ADMINISTRATIVE_AREA_EDEFAULT == null ? administrativeArea != null : !ADMINISTRATIVE_AREA_EDEFAULT.equals(administrativeArea);
            case Ows11Package.ADDRESS_TYPE__POSTAL_CODE:
                return POSTAL_CODE_EDEFAULT == null ? postalCode != null : !POSTAL_CODE_EDEFAULT.equals(postalCode);
            case Ows11Package.ADDRESS_TYPE__COUNTRY:
                return COUNTRY_EDEFAULT == null ? country != null : !COUNTRY_EDEFAULT.equals(country);
            case Ows11Package.ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS:
                return electronicMailAddress != null && !electronicMailAddress.isEmpty();
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
