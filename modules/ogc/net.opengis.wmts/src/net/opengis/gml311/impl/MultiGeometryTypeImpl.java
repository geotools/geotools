/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.GeometryArrayPropertyType;
import net.opengis.gml311.GeometryPropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiGeometryType;

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
 * An implementation of the model object '<em><b>Multi Geometry Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiGeometryTypeImpl#getGeometryMember <em>Geometry Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MultiGeometryTypeImpl#getGeometryMembers <em>Geometry Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiGeometryTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiGeometryType {
    /**
     * The cached value of the '{@link #getGeometryMember() <em>Geometry Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryMember()
     * @generated
     * @ordered
     */
    protected EList<GeometryPropertyType> geometryMember;

    /**
     * The cached value of the '{@link #getGeometryMembers() <em>Geometry Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryMembers()
     * @generated
     * @ordered
     */
    protected GeometryArrayPropertyType geometryMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiGeometryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiGeometryType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeometryPropertyType> getGeometryMember() {
        if (geometryMember == null) {
            geometryMember = new EObjectContainmentEList<GeometryPropertyType>(GeometryPropertyType.class, this, Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER);
        }
        return geometryMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryArrayPropertyType getGeometryMembers() {
        return geometryMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGeometryMembers(GeometryArrayPropertyType newGeometryMembers, NotificationChain msgs) {
        GeometryArrayPropertyType oldGeometryMembers = geometryMembers;
        geometryMembers = newGeometryMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS, oldGeometryMembers, newGeometryMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGeometryMembers(GeometryArrayPropertyType newGeometryMembers) {
        if (newGeometryMembers != geometryMembers) {
            NotificationChain msgs = null;
            if (geometryMembers != null)
                msgs = ((InternalEObject)geometryMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS, null, msgs);
            if (newGeometryMembers != null)
                msgs = ((InternalEObject)newGeometryMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS, null, msgs);
            msgs = basicSetGeometryMembers(newGeometryMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS, newGeometryMembers, newGeometryMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER:
                return ((InternalEList<?>)getGeometryMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS:
                return basicSetGeometryMembers(null, msgs);
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
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER:
                return getGeometryMember();
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS:
                return getGeometryMembers();
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
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER:
                getGeometryMember().clear();
                getGeometryMember().addAll((Collection<? extends GeometryPropertyType>)newValue);
                return;
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS:
                setGeometryMembers((GeometryArrayPropertyType)newValue);
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
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER:
                getGeometryMember().clear();
                return;
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS:
                setGeometryMembers((GeometryArrayPropertyType)null);
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
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBER:
                return geometryMember != null && !geometryMember.isEmpty();
            case Gml311Package.MULTI_GEOMETRY_TYPE__GEOMETRY_MEMBERS:
                return geometryMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiGeometryTypeImpl
