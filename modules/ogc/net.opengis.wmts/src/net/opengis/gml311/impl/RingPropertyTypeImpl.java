/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RingPropertyType;
import net.opengis.gml311.RingType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RingPropertyTypeImpl#getRing <em>Ring</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RingPropertyTypeImpl extends MinimalEObjectImpl.Container implements RingPropertyType {
    /**
     * The cached value of the '{@link #getRing() <em>Ring</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRing()
     * @generated
     * @ordered
     */
    protected RingType ring;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RingPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRingPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RingType getRing() {
        return ring;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRing(RingType newRing, NotificationChain msgs) {
        RingType oldRing = ring;
        ring = newRing;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RING_PROPERTY_TYPE__RING, oldRing, newRing);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRing(RingType newRing) {
        if (newRing != ring) {
            NotificationChain msgs = null;
            if (ring != null)
                msgs = ((InternalEObject)ring).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RING_PROPERTY_TYPE__RING, null, msgs);
            if (newRing != null)
                msgs = ((InternalEObject)newRing).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RING_PROPERTY_TYPE__RING, null, msgs);
            msgs = basicSetRing(newRing, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RING_PROPERTY_TYPE__RING, newRing, newRing));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.RING_PROPERTY_TYPE__RING:
                return basicSetRing(null, msgs);
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
            case Gml311Package.RING_PROPERTY_TYPE__RING:
                return getRing();
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
            case Gml311Package.RING_PROPERTY_TYPE__RING:
                setRing((RingType)newValue);
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
            case Gml311Package.RING_PROPERTY_TYPE__RING:
                setRing((RingType)null);
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
            case Gml311Package.RING_PROPERTY_TYPE__RING:
                return ring != null;
        }
        return super.eIsSet(featureID);
    }

} //RingPropertyTypeImpl
