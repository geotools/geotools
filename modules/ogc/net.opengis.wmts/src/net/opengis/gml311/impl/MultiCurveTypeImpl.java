/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CurveArrayPropertyType;
import net.opengis.gml311.CurvePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiCurveType;

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
 * An implementation of the model object '<em><b>Multi Curve Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiCurveTypeImpl#getCurveMember <em>Curve Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MultiCurveTypeImpl#getCurveMembers <em>Curve Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiCurveTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiCurveType {
    /**
     * The cached value of the '{@link #getCurveMember() <em>Curve Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCurveMember()
     * @generated
     * @ordered
     */
    protected EList<CurvePropertyType> curveMember;

    /**
     * The cached value of the '{@link #getCurveMembers() <em>Curve Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCurveMembers()
     * @generated
     * @ordered
     */
    protected CurveArrayPropertyType curveMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiCurveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiCurveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CurvePropertyType> getCurveMember() {
        if (curveMember == null) {
            curveMember = new EObjectContainmentEList<CurvePropertyType>(CurvePropertyType.class, this, Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER);
        }
        return curveMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveArrayPropertyType getCurveMembers() {
        return curveMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCurveMembers(CurveArrayPropertyType newCurveMembers, NotificationChain msgs) {
        CurveArrayPropertyType oldCurveMembers = curveMembers;
        curveMembers = newCurveMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS, oldCurveMembers, newCurveMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCurveMembers(CurveArrayPropertyType newCurveMembers) {
        if (newCurveMembers != curveMembers) {
            NotificationChain msgs = null;
            if (curveMembers != null)
                msgs = ((InternalEObject)curveMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS, null, msgs);
            if (newCurveMembers != null)
                msgs = ((InternalEObject)newCurveMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS, null, msgs);
            msgs = basicSetCurveMembers(newCurveMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS, newCurveMembers, newCurveMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER:
                return ((InternalEList<?>)getCurveMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS:
                return basicSetCurveMembers(null, msgs);
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
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER:
                return getCurveMember();
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS:
                return getCurveMembers();
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
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER:
                getCurveMember().clear();
                getCurveMember().addAll((Collection<? extends CurvePropertyType>)newValue);
                return;
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS:
                setCurveMembers((CurveArrayPropertyType)newValue);
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
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER:
                getCurveMember().clear();
                return;
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS:
                setCurveMembers((CurveArrayPropertyType)null);
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
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBER:
                return curveMember != null && !curveMember.isEmpty();
            case Gml311Package.MULTI_CURVE_TYPE__CURVE_MEMBERS:
                return curveMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiCurveTypeImpl
