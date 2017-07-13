/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TopoSurfacePropertyType;
import net.opengis.gml311.TopoSurfaceType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topo Surface Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopoSurfacePropertyTypeImpl#getTopoSurface <em>Topo Surface</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopoSurfacePropertyTypeImpl extends MinimalEObjectImpl.Container implements TopoSurfacePropertyType {
    /**
     * The cached value of the '{@link #getTopoSurface() <em>Topo Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopoSurface()
     * @generated
     * @ordered
     */
    protected TopoSurfaceType topoSurface;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TopoSurfacePropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopoSurfacePropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoSurfaceType getTopoSurface() {
        return topoSurface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTopoSurface(TopoSurfaceType newTopoSurface, NotificationChain msgs) {
        TopoSurfaceType oldTopoSurface = topoSurface;
        topoSurface = newTopoSurface;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE, oldTopoSurface, newTopoSurface);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTopoSurface(TopoSurfaceType newTopoSurface) {
        if (newTopoSurface != topoSurface) {
            NotificationChain msgs = null;
            if (topoSurface != null)
                msgs = ((InternalEObject)topoSurface).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE, null, msgs);
            if (newTopoSurface != null)
                msgs = ((InternalEObject)newTopoSurface).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE, null, msgs);
            msgs = basicSetTopoSurface(newTopoSurface, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE, newTopoSurface, newTopoSurface));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE:
                return basicSetTopoSurface(null, msgs);
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
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE:
                return getTopoSurface();
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
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE:
                setTopoSurface((TopoSurfaceType)newValue);
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
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE:
                setTopoSurface((TopoSurfaceType)null);
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
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE__TOPO_SURFACE:
                return topoSurface != null;
        }
        return super.eIsSet(featureID);
    }

} //TopoSurfacePropertyTypeImpl
