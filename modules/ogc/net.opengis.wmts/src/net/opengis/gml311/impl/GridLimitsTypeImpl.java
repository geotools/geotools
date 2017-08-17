/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GridEnvelopeType;
import net.opengis.gml311.GridLimitsType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Limits Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GridLimitsTypeImpl#getGridEnvelope <em>Grid Envelope</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GridLimitsTypeImpl extends MinimalEObjectImpl.Container implements GridLimitsType {
    /**
     * The cached value of the '{@link #getGridEnvelope() <em>Grid Envelope</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridEnvelope()
     * @generated
     * @ordered
     */
    protected GridEnvelopeType gridEnvelope;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridLimitsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGridLimitsType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridEnvelopeType getGridEnvelope() {
        return gridEnvelope;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGridEnvelope(GridEnvelopeType newGridEnvelope, NotificationChain msgs) {
        GridEnvelopeType oldGridEnvelope = gridEnvelope;
        gridEnvelope = newGridEnvelope;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE, oldGridEnvelope, newGridEnvelope);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridEnvelope(GridEnvelopeType newGridEnvelope) {
        if (newGridEnvelope != gridEnvelope) {
            NotificationChain msgs = null;
            if (gridEnvelope != null)
                msgs = ((InternalEObject)gridEnvelope).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE, null, msgs);
            if (newGridEnvelope != null)
                msgs = ((InternalEObject)newGridEnvelope).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE, null, msgs);
            msgs = basicSetGridEnvelope(newGridEnvelope, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE, newGridEnvelope, newGridEnvelope));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE:
                return basicSetGridEnvelope(null, msgs);
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
            case Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE:
                return getGridEnvelope();
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
            case Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE:
                setGridEnvelope((GridEnvelopeType)newValue);
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
            case Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE:
                setGridEnvelope((GridEnvelopeType)null);
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
            case Gml311Package.GRID_LIMITS_TYPE__GRID_ENVELOPE:
                return gridEnvelope != null;
        }
        return super.eIsSet(featureID);
    }

} //GridLimitsTypeImpl
