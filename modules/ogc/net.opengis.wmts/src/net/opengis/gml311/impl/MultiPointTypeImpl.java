/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiPointType;
import net.opengis.gml311.PointArrayPropertyType;
import net.opengis.gml311.PointPropertyType;

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
 * An implementation of the model object '<em><b>Multi Point Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiPointTypeImpl#getPointMember <em>Point Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MultiPointTypeImpl#getPointMembers <em>Point Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiPointTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiPointType {
    /**
     * The cached value of the '{@link #getPointMember() <em>Point Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPointMember()
     * @generated
     * @ordered
     */
    protected EList<PointPropertyType> pointMember;

    /**
     * The cached value of the '{@link #getPointMembers() <em>Point Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPointMembers()
     * @generated
     * @ordered
     */
    protected PointArrayPropertyType pointMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiPointTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiPointType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointMember() {
        if (pointMember == null) {
            pointMember = new EObjectContainmentEList<PointPropertyType>(PointPropertyType.class, this, Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER);
        }
        return pointMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointArrayPropertyType getPointMembers() {
        return pointMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPointMembers(PointArrayPropertyType newPointMembers, NotificationChain msgs) {
        PointArrayPropertyType oldPointMembers = pointMembers;
        pointMembers = newPointMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS, oldPointMembers, newPointMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPointMembers(PointArrayPropertyType newPointMembers) {
        if (newPointMembers != pointMembers) {
            NotificationChain msgs = null;
            if (pointMembers != null)
                msgs = ((InternalEObject)pointMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS, null, msgs);
            if (newPointMembers != null)
                msgs = ((InternalEObject)newPointMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS, null, msgs);
            msgs = basicSetPointMembers(newPointMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS, newPointMembers, newPointMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER:
                return ((InternalEList<?>)getPointMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS:
                return basicSetPointMembers(null, msgs);
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
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER:
                return getPointMember();
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS:
                return getPointMembers();
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
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER:
                getPointMember().clear();
                getPointMember().addAll((Collection<? extends PointPropertyType>)newValue);
                return;
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS:
                setPointMembers((PointArrayPropertyType)newValue);
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
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER:
                getPointMember().clear();
                return;
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS:
                setPointMembers((PointArrayPropertyType)null);
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
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBER:
                return pointMember != null && !pointMember.isEmpty();
            case Gml311Package.MULTI_POINT_TYPE__POINT_MEMBERS:
                return pointMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiPointTypeImpl
