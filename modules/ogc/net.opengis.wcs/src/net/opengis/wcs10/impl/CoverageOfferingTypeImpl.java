/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.CoverageOfferingType;
import net.opengis.wcs10.DomainSetType;
import net.opengis.wcs10.RangeSetType1;
import net.opengis.wcs10.SupportedCRSsType;
import net.opengis.wcs10.SupportedFormatsType;
import net.opengis.wcs10.SupportedInterpolationsType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Offering Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl#getSupportedCRSs <em>Supported CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl#getSupportedFormats <em>Supported Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl#getSupportedInterpolations <em>Supported Interpolations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageOfferingTypeImpl extends CoverageOfferingBriefTypeImpl implements CoverageOfferingType {
    /**
	 * The cached value of the '{@link #getDomainSet() <em>Domain Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDomainSet()
	 * @generated
	 * @ordered
	 */
    protected DomainSetType domainSet;

    /**
	 * The cached value of the '{@link #getRangeSet() <em>Range Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRangeSet()
	 * @generated
	 * @ordered
	 */
    protected RangeSetType1 rangeSet;

    /**
	 * The cached value of the '{@link #getSupportedCRSs() <em>Supported CR Ss</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSupportedCRSs()
	 * @generated
	 * @ordered
	 */
    protected SupportedCRSsType supportedCRSs;

    /**
	 * The cached value of the '{@link #getSupportedFormats() <em>Supported Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSupportedFormats()
	 * @generated
	 * @ordered
	 */
    protected SupportedFormatsType supportedFormats;

    /**
	 * The cached value of the '{@link #getSupportedInterpolations() <em>Supported Interpolations</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSupportedInterpolations()
	 * @generated
	 * @ordered
	 */
    protected SupportedInterpolationsType supportedInterpolations;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected CoverageOfferingTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.COVERAGE_OFFERING_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainSetType getDomainSet() {
		return domainSet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDomainSet(DomainSetType newDomainSet, NotificationChain msgs) {
		DomainSetType oldDomainSet = domainSet;
		domainSet = newDomainSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET, oldDomainSet, newDomainSet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDomainSet(DomainSetType newDomainSet) {
		if (newDomainSet != domainSet) {
			NotificationChain msgs = null;
			if (domainSet != null)
				msgs = ((InternalEObject)domainSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET, null, msgs);
			if (newDomainSet != null)
				msgs = ((InternalEObject)newDomainSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET, null, msgs);
			msgs = basicSetDomainSet(newDomainSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET, newDomainSet, newDomainSet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeSetType1 getRangeSet() {
		return rangeSet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRangeSet(RangeSetType1 newRangeSet, NotificationChain msgs) {
		RangeSetType1 oldRangeSet = rangeSet;
		rangeSet = newRangeSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET, oldRangeSet, newRangeSet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRangeSet(RangeSetType1 newRangeSet) {
		if (newRangeSet != rangeSet) {
			NotificationChain msgs = null;
			if (rangeSet != null)
				msgs = ((InternalEObject)rangeSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET, null, msgs);
			if (newRangeSet != null)
				msgs = ((InternalEObject)newRangeSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET, null, msgs);
			msgs = basicSetRangeSet(newRangeSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET, newRangeSet, newRangeSet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedCRSsType getSupportedCRSs() {
		return supportedCRSs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedCRSs(SupportedCRSsType newSupportedCRSs, NotificationChain msgs) {
		SupportedCRSsType oldSupportedCRSs = supportedCRSs;
		supportedCRSs = newSupportedCRSs;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS, oldSupportedCRSs, newSupportedCRSs);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedCRSs(SupportedCRSsType newSupportedCRSs) {
		if (newSupportedCRSs != supportedCRSs) {
			NotificationChain msgs = null;
			if (supportedCRSs != null)
				msgs = ((InternalEObject)supportedCRSs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS, null, msgs);
			if (newSupportedCRSs != null)
				msgs = ((InternalEObject)newSupportedCRSs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS, null, msgs);
			msgs = basicSetSupportedCRSs(newSupportedCRSs, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS, newSupportedCRSs, newSupportedCRSs));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedFormatsType getSupportedFormats() {
		return supportedFormats;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedFormats(SupportedFormatsType newSupportedFormats, NotificationChain msgs) {
		SupportedFormatsType oldSupportedFormats = supportedFormats;
		supportedFormats = newSupportedFormats;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS, oldSupportedFormats, newSupportedFormats);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedFormats(SupportedFormatsType newSupportedFormats) {
		if (newSupportedFormats != supportedFormats) {
			NotificationChain msgs = null;
			if (supportedFormats != null)
				msgs = ((InternalEObject)supportedFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS, null, msgs);
			if (newSupportedFormats != null)
				msgs = ((InternalEObject)newSupportedFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS, null, msgs);
			msgs = basicSetSupportedFormats(newSupportedFormats, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS, newSupportedFormats, newSupportedFormats));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedInterpolationsType getSupportedInterpolations() {
		return supportedInterpolations;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedInterpolations(SupportedInterpolationsType newSupportedInterpolations, NotificationChain msgs) {
		SupportedInterpolationsType oldSupportedInterpolations = supportedInterpolations;
		supportedInterpolations = newSupportedInterpolations;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS, oldSupportedInterpolations, newSupportedInterpolations);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedInterpolations(SupportedInterpolationsType newSupportedInterpolations) {
		if (newSupportedInterpolations != supportedInterpolations) {
			NotificationChain msgs = null;
			if (supportedInterpolations != null)
				msgs = ((InternalEObject)supportedInterpolations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS, null, msgs);
			if (newSupportedInterpolations != null)
				msgs = ((InternalEObject)newSupportedInterpolations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS, null, msgs);
			msgs = basicSetSupportedInterpolations(newSupportedInterpolations, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS, newSupportedInterpolations, newSupportedInterpolations));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET:
				return basicSetDomainSet(null, msgs);
			case Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET:
				return basicSetRangeSet(null, msgs);
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS:
				return basicSetSupportedCRSs(null, msgs);
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS:
				return basicSetSupportedFormats(null, msgs);
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS:
				return basicSetSupportedInterpolations(null, msgs);
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
			case Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET:
				return getDomainSet();
			case Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET:
				return getRangeSet();
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS:
				return getSupportedCRSs();
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS:
				return getSupportedFormats();
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS:
				return getSupportedInterpolations();
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
			case Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET:
				setDomainSet((DomainSetType)newValue);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET:
				setRangeSet((RangeSetType1)newValue);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS:
				setSupportedCRSs((SupportedCRSsType)newValue);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS:
				setSupportedFormats((SupportedFormatsType)newValue);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS:
				setSupportedInterpolations((SupportedInterpolationsType)newValue);
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
			case Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET:
				setDomainSet((DomainSetType)null);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET:
				setRangeSet((RangeSetType1)null);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS:
				setSupportedCRSs((SupportedCRSsType)null);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS:
				setSupportedFormats((SupportedFormatsType)null);
				return;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS:
				setSupportedInterpolations((SupportedInterpolationsType)null);
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
			case Wcs10Package.COVERAGE_OFFERING_TYPE__DOMAIN_SET:
				return domainSet != null;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__RANGE_SET:
				return rangeSet != null;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS:
				return supportedCRSs != null;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS:
				return supportedFormats != null;
			case Wcs10Package.COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS:
				return supportedInterpolations != null;
		}
		return super.eIsSet(featureID);
	}

} //CoverageOfferingTypeImpl
