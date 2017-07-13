/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DirectedObservationAtDistanceType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MeasureType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Directed Observation At Distance Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DirectedObservationAtDistanceTypeImpl#getDistance <em>Distance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DirectedObservationAtDistanceTypeImpl extends DirectedObservationTypeImpl implements DirectedObservationAtDistanceType {
    /**
     * The cached value of the '{@link #getDistance() <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDistance()
     * @generated
     * @ordered
     */
    protected MeasureType distance;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DirectedObservationAtDistanceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDirectedObservationAtDistanceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getDistance() {
        return distance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDistance(MeasureType newDistance, NotificationChain msgs) {
        MeasureType oldDistance = distance;
        distance = newDistance;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE, oldDistance, newDistance);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDistance(MeasureType newDistance) {
        if (newDistance != distance) {
            NotificationChain msgs = null;
            if (distance != null)
                msgs = ((InternalEObject)distance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE, null, msgs);
            if (newDistance != null)
                msgs = ((InternalEObject)newDistance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE, null, msgs);
            msgs = basicSetDistance(newDistance, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE, newDistance, newDistance));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE:
                return basicSetDistance(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE:
                return getDistance();
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
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE:
                setDistance((MeasureType)newValue);
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
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE:
                setDistance((MeasureType)null);
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
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE__DISTANCE:
                return distance != null;
        }
        return super.eIsSet(featureID);
    }

} //DirectedObservationAtDistanceTypeImpl
