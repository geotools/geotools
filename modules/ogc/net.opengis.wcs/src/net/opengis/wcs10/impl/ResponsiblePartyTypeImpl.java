/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.ContactType;
import net.opengis.wcs10.ResponsiblePartyType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Responsible Party Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl#getOrganisationName1 <em>Organisation Name1</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl#getContactInfo <em>Contact Info</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResponsiblePartyTypeImpl extends EObjectImpl implements ResponsiblePartyType {
    /**
	 * The default value of the '{@link #getIndividualName() <em>Individual Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getIndividualName()
	 * @generated
	 * @ordered
	 */
    protected static final String INDIVIDUAL_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getIndividualName() <em>Individual Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getIndividualName()
	 * @generated
	 * @ordered
	 */
    protected String individualName = INDIVIDUAL_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getOrganisationName() <em>Organisation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getOrganisationName()
	 * @generated
	 * @ordered
	 */
    protected static final String ORGANISATION_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getOrganisationName() <em>Organisation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getOrganisationName()
	 * @generated
	 * @ordered
	 */
    protected String organisationName = ORGANISATION_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getOrganisationName1() <em>Organisation Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getOrganisationName1()
	 * @generated
	 * @ordered
	 */
    protected static final String ORGANISATION_NAME1_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getOrganisationName1() <em>Organisation Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getOrganisationName1()
	 * @generated
	 * @ordered
	 */
    protected String organisationName1 = ORGANISATION_NAME1_EDEFAULT;

    /**
	 * The default value of the '{@link #getPositionName() <em>Position Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getPositionName()
	 * @generated
	 * @ordered
	 */
    protected static final String POSITION_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPositionName() <em>Position Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getPositionName()
	 * @generated
	 * @ordered
	 */
    protected String positionName = POSITION_NAME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getContactInfo() <em>Contact Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getContactInfo()
	 * @generated
	 * @ordered
	 */
    protected ContactType contactInfo;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ResponsiblePartyTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.RESPONSIBLE_PARTY_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getIndividualName() {
		return individualName;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIndividualName(String newIndividualName) {
		String oldIndividualName = individualName;
		individualName = newIndividualName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME, oldIndividualName, individualName));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getOrganisationName() {
		return organisationName;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOrganisationName(String newOrganisationName) {
		String oldOrganisationName = organisationName;
		organisationName = newOrganisationName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME, oldOrganisationName, organisationName));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getOrganisationName1() {
		return organisationName1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOrganisationName1(String newOrganisationName1) {
		String oldOrganisationName1 = organisationName1;
		organisationName1 = newOrganisationName1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1, oldOrganisationName1, organisationName1));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPositionName() {
		return positionName;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPositionName(String newPositionName) {
		String oldPositionName = positionName;
		positionName = newPositionName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__POSITION_NAME, oldPositionName, positionName));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ContactType getContactInfo() {
		return contactInfo;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetContactInfo(ContactType newContactInfo, NotificationChain msgs) {
		ContactType oldContactInfo = contactInfo;
		contactInfo = newContactInfo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO, oldContactInfo, newContactInfo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContactInfo(ContactType newContactInfo) {
		if (newContactInfo != contactInfo) {
			NotificationChain msgs = null;
			if (contactInfo != null)
				msgs = ((InternalEObject)contactInfo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO, null, msgs);
			if (newContactInfo != null)
				msgs = ((InternalEObject)newContactInfo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO, null, msgs);
			msgs = basicSetContactInfo(newContactInfo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO, newContactInfo, newContactInfo));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO:
				return basicSetContactInfo(null, msgs);
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
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME:
				return getIndividualName();
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME:
				return getOrganisationName();
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1:
				return getOrganisationName1();
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__POSITION_NAME:
				return getPositionName();
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO:
				return getContactInfo();
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
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME:
				setIndividualName((String)newValue);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME:
				setOrganisationName((String)newValue);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1:
				setOrganisationName1((String)newValue);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__POSITION_NAME:
				setPositionName((String)newValue);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO:
				setContactInfo((ContactType)newValue);
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
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME:
				setIndividualName(INDIVIDUAL_NAME_EDEFAULT);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME:
				setOrganisationName(ORGANISATION_NAME_EDEFAULT);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1:
				setOrganisationName1(ORGANISATION_NAME1_EDEFAULT);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__POSITION_NAME:
				setPositionName(POSITION_NAME_EDEFAULT);
				return;
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO:
				setContactInfo((ContactType)null);
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
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME:
				return INDIVIDUAL_NAME_EDEFAULT == null ? individualName != null : !INDIVIDUAL_NAME_EDEFAULT.equals(individualName);
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME:
				return ORGANISATION_NAME_EDEFAULT == null ? organisationName != null : !ORGANISATION_NAME_EDEFAULT.equals(organisationName);
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1:
				return ORGANISATION_NAME1_EDEFAULT == null ? organisationName1 != null : !ORGANISATION_NAME1_EDEFAULT.equals(organisationName1);
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__POSITION_NAME:
				return POSITION_NAME_EDEFAULT == null ? positionName != null : !POSITION_NAME_EDEFAULT.equals(positionName);
			case Wcs10Package.RESPONSIBLE_PARTY_TYPE__CONTACT_INFO:
				return contactInfo != null;
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
		result.append(" (individualName: ");
		result.append(individualName);
		result.append(", organisationName: ");
		result.append(organisationName);
		result.append(", organisationName1: ");
		result.append(organisationName1);
		result.append(", positionName: ");
		result.append(positionName);
		result.append(')');
		return result.toString();
	}

} //ResponsiblePartyTypeImpl
