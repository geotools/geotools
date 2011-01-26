/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.TimeSequenceType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.DomainSubsetTypeImpl#getSpatialSubset <em>Spatial Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DomainSubsetTypeImpl#getTemporalSubset <em>Temporal Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DomainSubsetTypeImpl#getTemporalSubset1 <em>Temporal Subset1</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainSubsetTypeImpl extends EObjectImpl implements DomainSubsetType {
    /**
	 * The cached value of the '{@link #getSpatialSubset() <em>Spatial Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSpatialSubset()
	 * @generated
	 * @ordered
	 */
    protected SpatialSubsetType spatialSubset;

    /**
	 * The cached value of the '{@link #getTemporalSubset() <em>Temporal Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTemporalSubset()
	 * @generated
	 * @ordered
	 */
    protected TimeSequenceType temporalSubset;

    /**
	 * The cached value of the '{@link #getTemporalSubset1() <em>Temporal Subset1</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTemporalSubset1()
	 * @generated
	 * @ordered
	 */
    protected TimeSequenceType temporalSubset1;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DomainSubsetTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.DOMAIN_SUBSET_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SpatialSubsetType getSpatialSubset() {
		return spatialSubset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSpatialSubset(SpatialSubsetType newSpatialSubset, NotificationChain msgs) {
		SpatialSubsetType oldSpatialSubset = spatialSubset;
		spatialSubset = newSpatialSubset;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET, oldSpatialSubset, newSpatialSubset);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSpatialSubset(SpatialSubsetType newSpatialSubset) {
		if (newSpatialSubset != spatialSubset) {
			NotificationChain msgs = null;
			if (spatialSubset != null)
				msgs = ((InternalEObject)spatialSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET, null, msgs);
			if (newSpatialSubset != null)
				msgs = ((InternalEObject)newSpatialSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET, null, msgs);
			msgs = basicSetSpatialSubset(newSpatialSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET, newSpatialSubset, newSpatialSubset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeSequenceType getTemporalSubset() {
		return temporalSubset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTemporalSubset(TimeSequenceType newTemporalSubset, NotificationChain msgs) {
		TimeSequenceType oldTemporalSubset = temporalSubset;
		temporalSubset = newTemporalSubset;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, oldTemporalSubset, newTemporalSubset);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemporalSubset(TimeSequenceType newTemporalSubset) {
		if (newTemporalSubset != temporalSubset) {
			NotificationChain msgs = null;
			if (temporalSubset != null)
				msgs = ((InternalEObject)temporalSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
			if (newTemporalSubset != null)
				msgs = ((InternalEObject)newTemporalSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
			msgs = basicSetTemporalSubset(newTemporalSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, newTemporalSubset, newTemporalSubset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeSequenceType getTemporalSubset1() {
		return temporalSubset1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTemporalSubset1(TimeSequenceType newTemporalSubset1, NotificationChain msgs) {
		TimeSequenceType oldTemporalSubset1 = temporalSubset1;
		temporalSubset1 = newTemporalSubset1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1, oldTemporalSubset1, newTemporalSubset1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemporalSubset1(TimeSequenceType newTemporalSubset1) {
		if (newTemporalSubset1 != temporalSubset1) {
			NotificationChain msgs = null;
			if (temporalSubset1 != null)
				msgs = ((InternalEObject)temporalSubset1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1, null, msgs);
			if (newTemporalSubset1 != null)
				msgs = ((InternalEObject)newTemporalSubset1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1, null, msgs);
			msgs = basicSetTemporalSubset1(newTemporalSubset1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1, newTemporalSubset1, newTemporalSubset1));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET:
				return basicSetSpatialSubset(null, msgs);
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
				return basicSetTemporalSubset(null, msgs);
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1:
				return basicSetTemporalSubset1(null, msgs);
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
			case Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET:
				return getSpatialSubset();
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
				return getTemporalSubset();
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1:
				return getTemporalSubset1();
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
			case Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET:
				setSpatialSubset((SpatialSubsetType)newValue);
				return;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
				setTemporalSubset((TimeSequenceType)newValue);
				return;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1:
				setTemporalSubset1((TimeSequenceType)newValue);
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
			case Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET:
				setSpatialSubset((SpatialSubsetType)null);
				return;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
				setTemporalSubset((TimeSequenceType)null);
				return;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1:
				setTemporalSubset1((TimeSequenceType)null);
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
			case Wcs10Package.DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET:
				return spatialSubset != null;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
				return temporalSubset != null;
			case Wcs10Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1:
				return temporalSubset1 != null;
		}
		return super.eIsSet(featureID);
	}

} //DomainSubsetTypeImpl
