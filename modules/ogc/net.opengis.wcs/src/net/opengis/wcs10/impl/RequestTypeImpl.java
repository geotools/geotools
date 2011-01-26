/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.DescribeCoverageType1;
import net.opengis.wcs10.GetCapabilitiesType1;
import net.opengis.wcs10.GetCoverageType1;
import net.opengis.wcs10.RequestType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.RequestTypeImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RequestTypeImpl#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RequestTypeImpl#getGetCoverage <em>Get Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequestTypeImpl extends EObjectImpl implements RequestType {
    /**
	 * The cached value of the '{@link #getGetCapabilities() <em>Get Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getGetCapabilities()
	 * @generated
	 * @ordered
	 */
    protected GetCapabilitiesType1 getCapabilities;

    /**
	 * The cached value of the '{@link #getDescribeCoverage() <em>Describe Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescribeCoverage()
	 * @generated
	 * @ordered
	 */
    protected DescribeCoverageType1 describeCoverage;

    /**
	 * The cached value of the '{@link #getGetCoverage() <em>Get Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getGetCoverage()
	 * @generated
	 * @ordered
	 */
    protected GetCoverageType1 getCoverage;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RequestTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.REQUEST_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GetCapabilitiesType1 getGetCapabilities() {
		return getCapabilities;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType1 newGetCapabilities, NotificationChain msgs) {
		GetCapabilitiesType1 oldGetCapabilities = getCapabilities;
		getCapabilities = newGetCapabilities;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES, oldGetCapabilities, newGetCapabilities);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGetCapabilities(GetCapabilitiesType1 newGetCapabilities) {
		if (newGetCapabilities != getCapabilities) {
			NotificationChain msgs = null;
			if (getCapabilities != null)
				msgs = ((InternalEObject)getCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES, null, msgs);
			if (newGetCapabilities != null)
				msgs = ((InternalEObject)newGetCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES, null, msgs);
			msgs = basicSetGetCapabilities(newGetCapabilities, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES, newGetCapabilities, newGetCapabilities));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DescribeCoverageType1 getDescribeCoverage() {
		return describeCoverage;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDescribeCoverage(DescribeCoverageType1 newDescribeCoverage, NotificationChain msgs) {
		DescribeCoverageType1 oldDescribeCoverage = describeCoverage;
		describeCoverage = newDescribeCoverage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE, oldDescribeCoverage, newDescribeCoverage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescribeCoverage(DescribeCoverageType1 newDescribeCoverage) {
		if (newDescribeCoverage != describeCoverage) {
			NotificationChain msgs = null;
			if (describeCoverage != null)
				msgs = ((InternalEObject)describeCoverage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE, null, msgs);
			if (newDescribeCoverage != null)
				msgs = ((InternalEObject)newDescribeCoverage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE, null, msgs);
			msgs = basicSetDescribeCoverage(newDescribeCoverage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE, newDescribeCoverage, newDescribeCoverage));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GetCoverageType1 getGetCoverage() {
		return getCoverage;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGetCoverage(GetCoverageType1 newGetCoverage, NotificationChain msgs) {
		GetCoverageType1 oldGetCoverage = getCoverage;
		getCoverage = newGetCoverage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__GET_COVERAGE, oldGetCoverage, newGetCoverage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGetCoverage(GetCoverageType1 newGetCoverage) {
		if (newGetCoverage != getCoverage) {
			NotificationChain msgs = null;
			if (getCoverage != null)
				msgs = ((InternalEObject)getCoverage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__GET_COVERAGE, null, msgs);
			if (newGetCoverage != null)
				msgs = ((InternalEObject)newGetCoverage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.REQUEST_TYPE__GET_COVERAGE, null, msgs);
			msgs = basicSetGetCoverage(newGetCoverage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.REQUEST_TYPE__GET_COVERAGE, newGetCoverage, newGetCoverage));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES:
				return basicSetGetCapabilities(null, msgs);
			case Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE:
				return basicSetDescribeCoverage(null, msgs);
			case Wcs10Package.REQUEST_TYPE__GET_COVERAGE:
				return basicSetGetCoverage(null, msgs);
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
			case Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES:
				return getGetCapabilities();
			case Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE:
				return getDescribeCoverage();
			case Wcs10Package.REQUEST_TYPE__GET_COVERAGE:
				return getGetCoverage();
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
			case Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType1)newValue);
				return;
			case Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE:
				setDescribeCoverage((DescribeCoverageType1)newValue);
				return;
			case Wcs10Package.REQUEST_TYPE__GET_COVERAGE:
				setGetCoverage((GetCoverageType1)newValue);
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
			case Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType1)null);
				return;
			case Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE:
				setDescribeCoverage((DescribeCoverageType1)null);
				return;
			case Wcs10Package.REQUEST_TYPE__GET_COVERAGE:
				setGetCoverage((GetCoverageType1)null);
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
			case Wcs10Package.REQUEST_TYPE__GET_CAPABILITIES:
				return getCapabilities != null;
			case Wcs10Package.REQUEST_TYPE__DESCRIBE_COVERAGE:
				return describeCoverage != null;
			case Wcs10Package.REQUEST_TYPE__GET_COVERAGE:
				return getCoverage != null;
		}
		return super.eIsSet(featureID);
	}

} //RequestTypeImpl
