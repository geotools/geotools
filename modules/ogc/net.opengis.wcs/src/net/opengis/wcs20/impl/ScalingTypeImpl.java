/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.ScaleAxisByFactorType;
import net.opengis.wcs20.ScaleByFactorType;
import net.opengis.wcs20.ScaleToExtentType;
import net.opengis.wcs20.ScaleToSizeType;
import net.opengis.wcs20.ScalingType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scaling Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ScalingTypeImpl#getScaleByFactor <em>Scale By Factor</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ScalingTypeImpl#getScaleAxesByFactor <em>Scale Axes By Factor</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ScalingTypeImpl#getScaleToSize <em>Scale To Size</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ScalingTypeImpl#getScaleToExtent <em>Scale To Extent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScalingTypeImpl extends EObjectImpl implements ScalingType {
    /**
     * The cached value of the '{@link #getScaleByFactor() <em>Scale By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleByFactor()
     * @generated
     * @ordered
     */
    protected ScaleByFactorType scaleByFactor;

    /**
     * The cached value of the '{@link #getScaleAxesByFactor() <em>Scale Axes By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleAxesByFactor()
     * @generated
     * @ordered
     */
    protected ScaleAxisByFactorType scaleAxesByFactor;

    /**
     * The cached value of the '{@link #getScaleToSize() <em>Scale To Size</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleToSize()
     * @generated
     * @ordered
     */
    protected ScaleToSizeType scaleToSize;

    /**
     * The cached value of the '{@link #getScaleToExtent() <em>Scale To Extent</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleToExtent()
     * @generated
     * @ordered
     */
    protected ScaleToExtentType scaleToExtent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScalingTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SCALING_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleByFactorType getScaleByFactor() {
        if (scaleByFactor != null && scaleByFactor.eIsProxy()) {
            InternalEObject oldScaleByFactor = (InternalEObject)scaleByFactor;
            scaleByFactor = (ScaleByFactorType)eResolveProxy(oldScaleByFactor);
            if (scaleByFactor != oldScaleByFactor) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR, oldScaleByFactor, scaleByFactor));
            }
        }
        return scaleByFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleByFactorType basicGetScaleByFactor() {
        return scaleByFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleByFactor(ScaleByFactorType newScaleByFactor) {
        ScaleByFactorType oldScaleByFactor = scaleByFactor;
        scaleByFactor = newScaleByFactor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR, oldScaleByFactor, scaleByFactor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleAxisByFactorType getScaleAxesByFactor() {
        if (scaleAxesByFactor != null && scaleAxesByFactor.eIsProxy()) {
            InternalEObject oldScaleAxesByFactor = (InternalEObject)scaleAxesByFactor;
            scaleAxesByFactor = (ScaleAxisByFactorType)eResolveProxy(oldScaleAxesByFactor);
            if (scaleAxesByFactor != oldScaleAxesByFactor) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR, oldScaleAxesByFactor, scaleAxesByFactor));
            }
        }
        return scaleAxesByFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleAxisByFactorType basicGetScaleAxesByFactor() {
        return scaleAxesByFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleAxesByFactor(ScaleAxisByFactorType newScaleAxesByFactor) {
        ScaleAxisByFactorType oldScaleAxesByFactor = scaleAxesByFactor;
        scaleAxesByFactor = newScaleAxesByFactor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR, oldScaleAxesByFactor, scaleAxesByFactor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleToSizeType getScaleToSize() {
        if (scaleToSize != null && scaleToSize.eIsProxy()) {
            InternalEObject oldScaleToSize = (InternalEObject)scaleToSize;
            scaleToSize = (ScaleToSizeType)eResolveProxy(oldScaleToSize);
            if (scaleToSize != oldScaleToSize) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE, oldScaleToSize, scaleToSize));
            }
        }
        return scaleToSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleToSizeType basicGetScaleToSize() {
        return scaleToSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleToSize(ScaleToSizeType newScaleToSize) {
        ScaleToSizeType oldScaleToSize = scaleToSize;
        scaleToSize = newScaleToSize;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE, oldScaleToSize, scaleToSize));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleToExtentType getScaleToExtent() {
        if (scaleToExtent != null && scaleToExtent.eIsProxy()) {
            InternalEObject oldScaleToExtent = (InternalEObject)scaleToExtent;
            scaleToExtent = (ScaleToExtentType)eResolveProxy(oldScaleToExtent);
            if (scaleToExtent != oldScaleToExtent) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT, oldScaleToExtent, scaleToExtent));
            }
        }
        return scaleToExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleToExtentType basicGetScaleToExtent() {
        return scaleToExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleToExtent(ScaleToExtentType newScaleToExtent) {
        ScaleToExtentType oldScaleToExtent = scaleToExtent;
        scaleToExtent = newScaleToExtent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT, oldScaleToExtent, scaleToExtent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR:
                if (resolve) return getScaleByFactor();
                return basicGetScaleByFactor();
            case Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR:
                if (resolve) return getScaleAxesByFactor();
                return basicGetScaleAxesByFactor();
            case Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE:
                if (resolve) return getScaleToSize();
                return basicGetScaleToSize();
            case Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT:
                if (resolve) return getScaleToExtent();
                return basicGetScaleToExtent();
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
            case Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR:
                setScaleByFactor((ScaleByFactorType)newValue);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR:
                setScaleAxesByFactor((ScaleAxisByFactorType)newValue);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE:
                setScaleToSize((ScaleToSizeType)newValue);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT:
                setScaleToExtent((ScaleToExtentType)newValue);
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
            case Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR:
                setScaleByFactor((ScaleByFactorType)null);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR:
                setScaleAxesByFactor((ScaleAxisByFactorType)null);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE:
                setScaleToSize((ScaleToSizeType)null);
                return;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT:
                setScaleToExtent((ScaleToExtentType)null);
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
            case Wcs20Package.SCALING_TYPE__SCALE_BY_FACTOR:
                return scaleByFactor != null;
            case Wcs20Package.SCALING_TYPE__SCALE_AXES_BY_FACTOR:
                return scaleAxesByFactor != null;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_SIZE:
                return scaleToSize != null;
            case Wcs20Package.SCALING_TYPE__SCALE_TO_EXTENT:
                return scaleToExtent != null;
        }
        return super.eIsSet(featureID);
    }

} //ScalingTypeImpl
