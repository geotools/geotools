/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import net.opengis.ows10.AddressType;
import net.opengis.ows10.ContactType;
import net.opengis.ows10.OnlineResourceType;
import net.opengis.ows10.Ows10Package;
import net.opengis.ows10.TelephoneType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contact Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.ContactTypeImpl#getPhone <em>Phone</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.ContactTypeImpl#getAddress <em>Address</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.ContactTypeImpl#getOnlineResource <em>Online Resource</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.ContactTypeImpl#getHoursOfService <em>Hours Of Service</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.ContactTypeImpl#getContactInstructions <em>Contact Instructions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContactTypeImpl extends EObjectImpl implements ContactType {
	/**
	 * The cached value of the '{@link #getPhone() <em>Phone</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhone()
	 * @generated
	 * @ordered
	 */
	protected TelephoneType phone;

	/**
	 * The cached value of the '{@link #getAddress() <em>Address</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress()
	 * @generated
	 * @ordered
	 */
	protected AddressType address;

	/**
	 * The cached value of the '{@link #getOnlineResource() <em>Online Resource</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOnlineResource()
	 * @generated
	 * @ordered
	 */
	protected OnlineResourceType onlineResource;

	/**
	 * The default value of the '{@link #getHoursOfService() <em>Hours Of Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHoursOfService()
	 * @generated
	 * @ordered
	 */
	protected static final String HOURS_OF_SERVICE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHoursOfService() <em>Hours Of Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHoursOfService()
	 * @generated
	 * @ordered
	 */
	protected String hoursOfService = HOURS_OF_SERVICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getContactInstructions() <em>Contact Instructions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContactInstructions()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTACT_INSTRUCTIONS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContactInstructions() <em>Contact Instructions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContactInstructions()
	 * @generated
	 * @ordered
	 */
	protected String contactInstructions = CONTACT_INSTRUCTIONS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContactTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Ows10Package.eINSTANCE.getContactType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TelephoneType getPhone() {
		return phone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPhone(TelephoneType newPhone, NotificationChain msgs) {
		TelephoneType oldPhone = phone;
		phone = newPhone;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__PHONE, oldPhone, newPhone);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhone(TelephoneType newPhone) {
		if (newPhone != phone) {
			NotificationChain msgs = null;
			if (phone != null)
				msgs = ((InternalEObject)phone).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__PHONE, null, msgs);
			if (newPhone != null)
				msgs = ((InternalEObject)newPhone).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__PHONE, null, msgs);
			msgs = basicSetPhone(newPhone, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__PHONE, newPhone, newPhone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddressType getAddress() {
		return address;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAddress(AddressType newAddress, NotificationChain msgs) {
		AddressType oldAddress = address;
		address = newAddress;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__ADDRESS, oldAddress, newAddress);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAddress(AddressType newAddress) {
		if (newAddress != address) {
			NotificationChain msgs = null;
			if (address != null)
				msgs = ((InternalEObject)address).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__ADDRESS, null, msgs);
			if (newAddress != null)
				msgs = ((InternalEObject)newAddress).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__ADDRESS, null, msgs);
			msgs = basicSetAddress(newAddress, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__ADDRESS, newAddress, newAddress));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnlineResourceType getOnlineResource() {
		return onlineResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOnlineResource(OnlineResourceType newOnlineResource, NotificationChain msgs) {
		OnlineResourceType oldOnlineResource = onlineResource;
		onlineResource = newOnlineResource;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE, oldOnlineResource, newOnlineResource);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOnlineResource(OnlineResourceType newOnlineResource) {
		if (newOnlineResource != onlineResource) {
			NotificationChain msgs = null;
			if (onlineResource != null)
				msgs = ((InternalEObject)onlineResource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE, null, msgs);
			if (newOnlineResource != null)
				msgs = ((InternalEObject)newOnlineResource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE, null, msgs);
			msgs = basicSetOnlineResource(newOnlineResource, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE, newOnlineResource, newOnlineResource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHoursOfService() {
		return hoursOfService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHoursOfService(String newHoursOfService) {
		String oldHoursOfService = hoursOfService;
		hoursOfService = newHoursOfService;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__HOURS_OF_SERVICE, oldHoursOfService, hoursOfService));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContactInstructions() {
		return contactInstructions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContactInstructions(String newContactInstructions) {
		String oldContactInstructions = contactInstructions;
		contactInstructions = newContactInstructions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CONTACT_TYPE__CONTACT_INSTRUCTIONS, oldContactInstructions, contactInstructions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Ows10Package.CONTACT_TYPE__PHONE:
				return basicSetPhone(null, msgs);
			case Ows10Package.CONTACT_TYPE__ADDRESS:
				return basicSetAddress(null, msgs);
			case Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE:
				return basicSetOnlineResource(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Ows10Package.CONTACT_TYPE__PHONE:
				return getPhone();
			case Ows10Package.CONTACT_TYPE__ADDRESS:
				return getAddress();
			case Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE:
				return getOnlineResource();
			case Ows10Package.CONTACT_TYPE__HOURS_OF_SERVICE:
				return getHoursOfService();
			case Ows10Package.CONTACT_TYPE__CONTACT_INSTRUCTIONS:
				return getContactInstructions();
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
			case Ows10Package.CONTACT_TYPE__PHONE:
				setPhone((TelephoneType)newValue);
				return;
			case Ows10Package.CONTACT_TYPE__ADDRESS:
				setAddress((AddressType)newValue);
				return;
			case Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE:
				setOnlineResource((OnlineResourceType)newValue);
				return;
			case Ows10Package.CONTACT_TYPE__HOURS_OF_SERVICE:
				setHoursOfService((String)newValue);
				return;
			case Ows10Package.CONTACT_TYPE__CONTACT_INSTRUCTIONS:
				setContactInstructions((String)newValue);
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
			case Ows10Package.CONTACT_TYPE__PHONE:
				setPhone((TelephoneType)null);
				return;
			case Ows10Package.CONTACT_TYPE__ADDRESS:
				setAddress((AddressType)null);
				return;
			case Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE:
				setOnlineResource((OnlineResourceType)null);
				return;
			case Ows10Package.CONTACT_TYPE__HOURS_OF_SERVICE:
				setHoursOfService(HOURS_OF_SERVICE_EDEFAULT);
				return;
			case Ows10Package.CONTACT_TYPE__CONTACT_INSTRUCTIONS:
				setContactInstructions(CONTACT_INSTRUCTIONS_EDEFAULT);
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
			case Ows10Package.CONTACT_TYPE__PHONE:
				return phone != null;
			case Ows10Package.CONTACT_TYPE__ADDRESS:
				return address != null;
			case Ows10Package.CONTACT_TYPE__ONLINE_RESOURCE:
				return onlineResource != null;
			case Ows10Package.CONTACT_TYPE__HOURS_OF_SERVICE:
				return HOURS_OF_SERVICE_EDEFAULT == null ? hoursOfService != null : !HOURS_OF_SERVICE_EDEFAULT.equals(hoursOfService);
			case Ows10Package.CONTACT_TYPE__CONTACT_INSTRUCTIONS:
				return CONTACT_INSTRUCTIONS_EDEFAULT == null ? contactInstructions != null : !CONTACT_INSTRUCTIONS_EDEFAULT.equals(contactInstructions);
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
		result.append(" (hoursOfService: ");
		result.append(hoursOfService);
		result.append(", contactInstructions: ");
		result.append(contactInstructions);
		result.append(')');
		return result.toString();
	}

} //ContactTypeImpl
