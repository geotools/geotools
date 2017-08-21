/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiSurfaceType;
import net.opengis.gml311.SurfaceArrayPropertyType;
import net.opengis.gml311.SurfacePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Surface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiSurfaceTypeImpl#getSurfaceMember <em>Surface Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MultiSurfaceTypeImpl#getSurfaceMembers <em>Surface Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiSurfaceTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiSurfaceType {
    /**
     * The cached value of the '{@link #getSurfaceMember() <em>Surface Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSurfaceMember()
     * @generated
     * @ordered
     */
    protected EList<SurfacePropertyType> surfaceMember;

    /**
     * The cached value of the '{@link #getSurfaceMembers() <em>Surface Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSurfaceMembers()
     * @generated
     * @ordered
     */
    protected SurfaceArrayPropertyType surfaceMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiSurfaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiSurfaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SurfacePropertyType> getSurfaceMember() {
        if (surfaceMember == null) {
            surfaceMember = new EObjectContainmentEList<SurfacePropertyType>(SurfacePropertyType.class, this, Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER);
        }
        return surfaceMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceArrayPropertyType getSurfaceMembers() {
        return surfaceMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSurfaceMembers(SurfaceArrayPropertyType newSurfaceMembers, NotificationChain msgs) {
        SurfaceArrayPropertyType oldSurfaceMembers = surfaceMembers;
        surfaceMembers = newSurfaceMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS, oldSurfaceMembers, newSurfaceMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSurfaceMembers(SurfaceArrayPropertyType newSurfaceMembers) {
        if (newSurfaceMembers != surfaceMembers) {
            NotificationChain msgs = null;
            if (surfaceMembers != null)
                msgs = ((InternalEObject)surfaceMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS, null, msgs);
            if (newSurfaceMembers != null)
                msgs = ((InternalEObject)newSurfaceMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS, null, msgs);
            msgs = basicSetSurfaceMembers(newSurfaceMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS, newSurfaceMembers, newSurfaceMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER:
                return ((InternalEList<?>)getSurfaceMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS:
                return basicSetSurfaceMembers(null, msgs);
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
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER:
                return getSurfaceMember();
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS:
                return getSurfaceMembers();
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
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER:
                getSurfaceMember().clear();
                getSurfaceMember().addAll((Collection<? extends SurfacePropertyType>)newValue);
                return;
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS:
                setSurfaceMembers((SurfaceArrayPropertyType)newValue);
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
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER:
                getSurfaceMember().clear();
                return;
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS:
                setSurfaceMembers((SurfaceArrayPropertyType)null);
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
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBER:
                return surfaceMember != null && !surfaceMember.isEmpty();
            case Gml311Package.MULTI_SURFACE_TYPE__SURFACE_MEMBERS:
                return surfaceMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiSurfaceTypeImpl
