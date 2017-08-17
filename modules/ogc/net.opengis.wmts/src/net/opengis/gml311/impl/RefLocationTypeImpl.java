/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AffinePlacementType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RefLocationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ref Location Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RefLocationTypeImpl#getAffinePlacement <em>Affine Placement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RefLocationTypeImpl extends MinimalEObjectImpl.Container implements RefLocationType {
    /**
     * The cached value of the '{@link #getAffinePlacement() <em>Affine Placement</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAffinePlacement()
     * @generated
     * @ordered
     */
    protected AffinePlacementType affinePlacement;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RefLocationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRefLocationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AffinePlacementType getAffinePlacement() {
        return affinePlacement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAffinePlacement(AffinePlacementType newAffinePlacement, NotificationChain msgs) {
        AffinePlacementType oldAffinePlacement = affinePlacement;
        affinePlacement = newAffinePlacement;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT, oldAffinePlacement, newAffinePlacement);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAffinePlacement(AffinePlacementType newAffinePlacement) {
        if (newAffinePlacement != affinePlacement) {
            NotificationChain msgs = null;
            if (affinePlacement != null)
                msgs = ((InternalEObject)affinePlacement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT, null, msgs);
            if (newAffinePlacement != null)
                msgs = ((InternalEObject)newAffinePlacement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT, null, msgs);
            msgs = basicSetAffinePlacement(newAffinePlacement, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT, newAffinePlacement, newAffinePlacement));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT:
                return basicSetAffinePlacement(null, msgs);
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
            case Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT:
                return getAffinePlacement();
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
            case Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT:
                setAffinePlacement((AffinePlacementType)newValue);
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
            case Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT:
                setAffinePlacement((AffinePlacementType)null);
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
            case Gml311Package.REF_LOCATION_TYPE__AFFINE_PLACEMENT:
                return affinePlacement != null;
        }
        return super.eIsSet(featureID);
    }

} //RefLocationTypeImpl
