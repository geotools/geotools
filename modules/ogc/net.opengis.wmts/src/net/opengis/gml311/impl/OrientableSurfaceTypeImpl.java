/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OrientableSurfaceType;
import net.opengis.gml311.SignType;
import net.opengis.gml311.SurfacePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Orientable Surface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OrientableSurfaceTypeImpl#getBaseSurface <em>Base Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OrientableSurfaceTypeImpl#getOrientation <em>Orientation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OrientableSurfaceTypeImpl extends AbstractSurfaceTypeImpl implements OrientableSurfaceType {
    /**
     * The cached value of the '{@link #getBaseSurface() <em>Base Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseSurface()
     * @generated
     * @ordered
     */
    protected SurfacePropertyType baseSurface;

    /**
     * The default value of the '{@link #getOrientation() <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected static final SignType ORIENTATION_EDEFAULT = SignType._1;

    /**
     * The cached value of the '{@link #getOrientation() <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected SignType orientation = ORIENTATION_EDEFAULT;

    /**
     * This is true if the Orientation attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean orientationESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OrientableSurfaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOrientableSurfaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfacePropertyType getBaseSurface() {
        return baseSurface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBaseSurface(SurfacePropertyType newBaseSurface, NotificationChain msgs) {
        SurfacePropertyType oldBaseSurface = baseSurface;
        baseSurface = newBaseSurface;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE, oldBaseSurface, newBaseSurface);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBaseSurface(SurfacePropertyType newBaseSurface) {
        if (newBaseSurface != baseSurface) {
            NotificationChain msgs = null;
            if (baseSurface != null)
                msgs = ((InternalEObject)baseSurface).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE, null, msgs);
            if (newBaseSurface != null)
                msgs = ((InternalEObject)newBaseSurface).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE, null, msgs);
            msgs = basicSetBaseSurface(newBaseSurface, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE, newBaseSurface, newBaseSurface));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SignType getOrientation() {
        return orientation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrientation(SignType newOrientation) {
        SignType oldOrientation = orientation;
        orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
        boolean oldOrientationESet = orientationESet;
        orientationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION, oldOrientation, orientation, !oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOrientation() {
        SignType oldOrientation = orientation;
        boolean oldOrientationESet = orientationESet;
        orientation = ORIENTATION_EDEFAULT;
        orientationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION, oldOrientation, ORIENTATION_EDEFAULT, oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOrientation() {
        return orientationESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE:
                return basicSetBaseSurface(null, msgs);
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
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE:
                return getBaseSurface();
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION:
                return getOrientation();
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
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE:
                setBaseSurface((SurfacePropertyType)newValue);
                return;
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION:
                setOrientation((SignType)newValue);
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
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE:
                setBaseSurface((SurfacePropertyType)null);
                return;
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION:
                unsetOrientation();
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
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__BASE_SURFACE:
                return baseSurface != null;
            case Gml311Package.ORIENTABLE_SURFACE_TYPE__ORIENTATION:
                return isSetOrientation();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (orientation: ");
        if (orientationESet) result.append(orientation); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //OrientableSurfaceTypeImpl
