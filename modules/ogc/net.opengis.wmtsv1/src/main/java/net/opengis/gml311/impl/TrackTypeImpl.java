/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MovingObjectStatusType;
import net.opengis.gml311.TrackType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Track Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TrackTypeImpl#getMovingObjectStatus <em>Moving Object Status</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TrackTypeImpl extends HistoryPropertyTypeImpl implements TrackType {
    /**
     * The cached value of the '{@link #getMovingObjectStatus() <em>Moving Object Status</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMovingObjectStatus()
     * @generated
     * @ordered
     */
    protected EList<MovingObjectStatusType> movingObjectStatus;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TrackTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTrackType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MovingObjectStatusType> getMovingObjectStatus() {
        if (movingObjectStatus == null) {
            movingObjectStatus = new EObjectContainmentEList<MovingObjectStatusType>(MovingObjectStatusType.class, this, Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS);
        }
        return movingObjectStatus;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS:
                return ((InternalEList<?>)getMovingObjectStatus()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS:
                return getMovingObjectStatus();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS:
                getMovingObjectStatus().clear();
                getMovingObjectStatus().addAll((Collection<? extends MovingObjectStatusType>)newValue);
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
            case Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS:
                getMovingObjectStatus().clear();
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
            case Gml311Package.TRACK_TYPE__MOVING_OBJECT_STATUS:
                return movingObjectStatus != null && !movingObjectStatus.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TrackTypeImpl
