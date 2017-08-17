/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LinearRingPropertyType;
import net.opengis.gml311.LinearRingType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Linear Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.LinearRingPropertyTypeImpl#getLinearRing <em>Linear Ring</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LinearRingPropertyTypeImpl extends MinimalEObjectImpl.Container implements LinearRingPropertyType {
    /**
     * The cached value of the '{@link #getLinearRing() <em>Linear Ring</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLinearRing()
     * @generated
     * @ordered
     */
    protected LinearRingType linearRing;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LinearRingPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getLinearRingPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearRingType getLinearRing() {
        return linearRing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLinearRing(LinearRingType newLinearRing, NotificationChain msgs) {
        LinearRingType oldLinearRing = linearRing;
        linearRing = newLinearRing;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING, oldLinearRing, newLinearRing);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLinearRing(LinearRingType newLinearRing) {
        if (newLinearRing != linearRing) {
            NotificationChain msgs = null;
            if (linearRing != null)
                msgs = ((InternalEObject)linearRing).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING, null, msgs);
            if (newLinearRing != null)
                msgs = ((InternalEObject)newLinearRing).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING, null, msgs);
            msgs = basicSetLinearRing(newLinearRing, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING, newLinearRing, newLinearRing));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING:
                return basicSetLinearRing(null, msgs);
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
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING:
                return getLinearRing();
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
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING:
                setLinearRing((LinearRingType)newValue);
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
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING:
                setLinearRing((LinearRingType)null);
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
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE__LINEAR_RING:
                return linearRing != null;
        }
        return super.eIsSet(featureID);
    }

} //LinearRingPropertyTypeImpl
