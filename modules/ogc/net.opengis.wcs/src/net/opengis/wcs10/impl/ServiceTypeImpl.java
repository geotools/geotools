/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.gml.CodeListType;

import net.opengis.wcs10.KeywordsType;
import net.opengis.wcs10.ResponsiblePartyType;
import net.opengis.wcs10.ServiceType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getResponsibleParty <em>Responsible Party</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ServiceTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceTypeImpl extends AbstractDescriptionTypeImpl implements ServiceType {
    /**
	 * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getKeywords()
	 * @generated
	 * @ordered
	 */
    protected EList keywords;

    /**
	 * The cached value of the '{@link #getResponsibleParty() <em>Responsible Party</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getResponsibleParty()
	 * @generated
	 * @ordered
	 */
    protected ResponsiblePartyType responsibleParty;

    /**
	 * The cached value of the '{@link #getFees() <em>Fees</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFees()
	 * @generated
	 * @ordered
	 */
    protected CodeListType fees;

    /**
	 * The cached value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAccessConstraints()
	 * @generated
	 * @ordered
	 */
    protected EList accessConstraints;

    /**
	 * The default value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected static final String UPDATE_SEQUENCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected String updateSequence = UPDATE_SEQUENCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String VERSION_EDEFAULT = "1.0.0";

    /**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected String version = VERSION_EDEFAULT;

    /**
	 * This is true if the Version attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean versionESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ServiceTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.SERVICE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getKeywords() {
		if (keywords == null) {
			keywords = new EObjectContainmentEList(KeywordsType.class, this, Wcs10Package.SERVICE_TYPE__KEYWORDS);
		}
		return keywords;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ResponsiblePartyType getResponsibleParty() {
		return responsibleParty;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetResponsibleParty(ResponsiblePartyType newResponsibleParty, NotificationChain msgs) {
		ResponsiblePartyType oldResponsibleParty = responsibleParty;
		responsibleParty = newResponsibleParty;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY, oldResponsibleParty, newResponsibleParty);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResponsibleParty(ResponsiblePartyType newResponsibleParty) {
		if (newResponsibleParty != responsibleParty) {
			NotificationChain msgs = null;
			if (responsibleParty != null)
				msgs = ((InternalEObject)responsibleParty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY, null, msgs);
			if (newResponsibleParty != null)
				msgs = ((InternalEObject)newResponsibleParty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY, null, msgs);
			msgs = basicSetResponsibleParty(newResponsibleParty, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY, newResponsibleParty, newResponsibleParty));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeListType getFees() {
		return fees;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFees(CodeListType newFees, NotificationChain msgs) {
		CodeListType oldFees = fees;
		fees = newFees;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__FEES, oldFees, newFees);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFees(CodeListType newFees) {
		if (newFees != fees) {
			NotificationChain msgs = null;
			if (fees != null)
				msgs = ((InternalEObject)fees).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.SERVICE_TYPE__FEES, null, msgs);
			if (newFees != null)
				msgs = ((InternalEObject)newFees).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.SERVICE_TYPE__FEES, null, msgs);
			msgs = basicSetFees(newFees, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__FEES, newFees, newFees));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAccessConstraints() {
		if (accessConstraints == null) {
			accessConstraints = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS);
		}
		return accessConstraints;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getUpdateSequence() {
		return updateSequence;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpdateSequence(String newUpdateSequence) {
		String oldUpdateSequence = updateSequence;
		updateSequence = newUpdateSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVersion() {
		return version;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SERVICE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.SERVICE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetVersion() {
		return versionESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.SERVICE_TYPE__KEYWORDS:
				return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY:
				return basicSetResponsibleParty(null, msgs);
			case Wcs10Package.SERVICE_TYPE__FEES:
				return basicSetFees(null, msgs);
			case Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS:
				return ((InternalEList)getAccessConstraints()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.SERVICE_TYPE__KEYWORDS:
				return getKeywords();
			case Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY:
				return getResponsibleParty();
			case Wcs10Package.SERVICE_TYPE__FEES:
				return getFees();
			case Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS:
				return getAccessConstraints();
			case Wcs10Package.SERVICE_TYPE__UPDATE_SEQUENCE:
				return getUpdateSequence();
			case Wcs10Package.SERVICE_TYPE__VERSION:
				return getVersion();
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
			case Wcs10Package.SERVICE_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
				return;
			case Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY:
				setResponsibleParty((ResponsiblePartyType)newValue);
				return;
			case Wcs10Package.SERVICE_TYPE__FEES:
				setFees((CodeListType)newValue);
				return;
			case Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS:
				getAccessConstraints().clear();
				getAccessConstraints().addAll((Collection)newValue);
				return;
			case Wcs10Package.SERVICE_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence((String)newValue);
				return;
			case Wcs10Package.SERVICE_TYPE__VERSION:
				setVersion((String)newValue);
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
			case Wcs10Package.SERVICE_TYPE__KEYWORDS:
				getKeywords().clear();
				return;
			case Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY:
				setResponsibleParty((ResponsiblePartyType)null);
				return;
			case Wcs10Package.SERVICE_TYPE__FEES:
				setFees((CodeListType)null);
				return;
			case Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS:
				getAccessConstraints().clear();
				return;
			case Wcs10Package.SERVICE_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
				return;
			case Wcs10Package.SERVICE_TYPE__VERSION:
				unsetVersion();
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
			case Wcs10Package.SERVICE_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case Wcs10Package.SERVICE_TYPE__RESPONSIBLE_PARTY:
				return responsibleParty != null;
			case Wcs10Package.SERVICE_TYPE__FEES:
				return fees != null;
			case Wcs10Package.SERVICE_TYPE__ACCESS_CONSTRAINTS:
				return accessConstraints != null && !accessConstraints.isEmpty();
			case Wcs10Package.SERVICE_TYPE__UPDATE_SEQUENCE:
				return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
			case Wcs10Package.SERVICE_TYPE__VERSION:
				return isSetVersion();
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
		result.append(" (updateSequence: ");
		result.append(updateSequence);
		result.append(", version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ServiceTypeImpl
