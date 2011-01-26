/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.CoverageOfferingBriefType;
import net.opengis.wcs10.KeywordsType;
import net.opengis.wcs10.LonLatEnvelopeType;
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
 * An implementation of the model object '<em><b>Coverage Offering Brief Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl#getLonLatEnvelope <em>Lon Lat Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageOfferingBriefTypeImpl extends AbstractDescriptionTypeImpl implements CoverageOfferingBriefType {
    /**
	 * The cached value of the '{@link #getLonLatEnvelope() <em>Lon Lat Envelope</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getLonLatEnvelope()
	 * @generated
	 * @ordered
	 */
    protected LonLatEnvelopeType lonLatEnvelope;

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
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected CoverageOfferingBriefTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.COVERAGE_OFFERING_BRIEF_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public LonLatEnvelopeType getLonLatEnvelope() {
		return lonLatEnvelope;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLonLatEnvelope(LonLatEnvelopeType newLonLatEnvelope, NotificationChain msgs) {
		LonLatEnvelopeType oldLonLatEnvelope = lonLatEnvelope;
		lonLatEnvelope = newLonLatEnvelope;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE, oldLonLatEnvelope, newLonLatEnvelope);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLonLatEnvelope(LonLatEnvelopeType newLonLatEnvelope) {
		if (newLonLatEnvelope != lonLatEnvelope) {
			NotificationChain msgs = null;
			if (lonLatEnvelope != null)
				msgs = ((InternalEObject)lonLatEnvelope).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE, null, msgs);
			if (newLonLatEnvelope != null)
				msgs = ((InternalEObject)newLonLatEnvelope).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE, null, msgs);
			msgs = basicSetLonLatEnvelope(newLonLatEnvelope, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE, newLonLatEnvelope, newLonLatEnvelope));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getKeywords() {
		if (keywords == null) {
			keywords = new EObjectContainmentEList(KeywordsType.class, this, Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS);
		}
		return keywords;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE:
				return basicSetLonLatEnvelope(null, msgs);
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS:
				return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE:
				return getLonLatEnvelope();
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS:
				return getKeywords();
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
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE:
				setLonLatEnvelope((LonLatEnvelopeType)newValue);
				return;
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
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
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE:
				setLonLatEnvelope((LonLatEnvelopeType)null);
				return;
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS:
				getKeywords().clear();
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
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE:
				return lonLatEnvelope != null;
			case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CoverageOfferingBriefTypeImpl
