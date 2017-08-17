/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DirectedObservationType;
import net.opengis.gml311.DirectionPropertyType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Directed Observation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DirectedObservationTypeImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DirectedObservationTypeImpl extends ObservationTypeImpl implements DirectedObservationType {
    /**
     * The cached value of the '{@link #getDirection() <em>Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected DirectionPropertyType direction;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DirectedObservationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDirectedObservationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionPropertyType getDirection() {
        return direction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDirection(DirectionPropertyType newDirection, NotificationChain msgs) {
        DirectionPropertyType oldDirection = direction;
        direction = newDirection;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION, oldDirection, newDirection);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDirection(DirectionPropertyType newDirection) {
        if (newDirection != direction) {
            NotificationChain msgs = null;
            if (direction != null)
                msgs = ((InternalEObject)direction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION, null, msgs);
            if (newDirection != null)
                msgs = ((InternalEObject)newDirection).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION, null, msgs);
            msgs = basicSetDirection(newDirection, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION, newDirection, newDirection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION:
                return basicSetDirection(null, msgs);
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
            case Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION:
                return getDirection();
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
            case Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION:
                setDirection((DirectionPropertyType)newValue);
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
            case Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION:
                setDirection((DirectionPropertyType)null);
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
            case Gml311Package.DIRECTED_OBSERVATION_TYPE__DIRECTION:
                return direction != null;
        }
        return super.eIsSet(featureID);
    }

} //DirectedObservationTypeImpl
