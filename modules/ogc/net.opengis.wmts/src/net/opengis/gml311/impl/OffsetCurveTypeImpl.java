/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CurvePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LengthType;
import net.opengis.gml311.OffsetCurveType;
import net.opengis.gml311.VectorType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Offset Curve Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OffsetCurveTypeImpl#getOffsetBase <em>Offset Base</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OffsetCurveTypeImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OffsetCurveTypeImpl#getRefDirection <em>Ref Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OffsetCurveTypeImpl extends AbstractCurveSegmentTypeImpl implements OffsetCurveType {
    /**
     * The cached value of the '{@link #getOffsetBase() <em>Offset Base</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOffsetBase()
     * @generated
     * @ordered
     */
    protected CurvePropertyType offsetBase;

    /**
     * The cached value of the '{@link #getDistance() <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDistance()
     * @generated
     * @ordered
     */
    protected LengthType distance;

    /**
     * The cached value of the '{@link #getRefDirection() <em>Ref Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefDirection()
     * @generated
     * @ordered
     */
    protected VectorType refDirection;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OffsetCurveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOffsetCurveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurvePropertyType getOffsetBase() {
        return offsetBase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOffsetBase(CurvePropertyType newOffsetBase, NotificationChain msgs) {
        CurvePropertyType oldOffsetBase = offsetBase;
        offsetBase = newOffsetBase;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE, oldOffsetBase, newOffsetBase);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOffsetBase(CurvePropertyType newOffsetBase) {
        if (newOffsetBase != offsetBase) {
            NotificationChain msgs = null;
            if (offsetBase != null)
                msgs = ((InternalEObject)offsetBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE, null, msgs);
            if (newOffsetBase != null)
                msgs = ((InternalEObject)newOffsetBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE, null, msgs);
            msgs = basicSetOffsetBase(newOffsetBase, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE, newOffsetBase, newOffsetBase));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LengthType getDistance() {
        return distance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDistance(LengthType newDistance, NotificationChain msgs) {
        LengthType oldDistance = distance;
        distance = newDistance;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__DISTANCE, oldDistance, newDistance);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDistance(LengthType newDistance) {
        if (newDistance != distance) {
            NotificationChain msgs = null;
            if (distance != null)
                msgs = ((InternalEObject)distance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__DISTANCE, null, msgs);
            if (newDistance != null)
                msgs = ((InternalEObject)newDistance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__DISTANCE, null, msgs);
            msgs = basicSetDistance(newDistance, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__DISTANCE, newDistance, newDistance));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VectorType getRefDirection() {
        return refDirection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRefDirection(VectorType newRefDirection, NotificationChain msgs) {
        VectorType oldRefDirection = refDirection;
        refDirection = newRefDirection;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION, oldRefDirection, newRefDirection);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRefDirection(VectorType newRefDirection) {
        if (newRefDirection != refDirection) {
            NotificationChain msgs = null;
            if (refDirection != null)
                msgs = ((InternalEObject)refDirection).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION, null, msgs);
            if (newRefDirection != null)
                msgs = ((InternalEObject)newRefDirection).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION, null, msgs);
            msgs = basicSetRefDirection(newRefDirection, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION, newRefDirection, newRefDirection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE:
                return basicSetOffsetBase(null, msgs);
            case Gml311Package.OFFSET_CURVE_TYPE__DISTANCE:
                return basicSetDistance(null, msgs);
            case Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION:
                return basicSetRefDirection(null, msgs);
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
            case Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE:
                return getOffsetBase();
            case Gml311Package.OFFSET_CURVE_TYPE__DISTANCE:
                return getDistance();
            case Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION:
                return getRefDirection();
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
            case Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE:
                setOffsetBase((CurvePropertyType)newValue);
                return;
            case Gml311Package.OFFSET_CURVE_TYPE__DISTANCE:
                setDistance((LengthType)newValue);
                return;
            case Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION:
                setRefDirection((VectorType)newValue);
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
            case Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE:
                setOffsetBase((CurvePropertyType)null);
                return;
            case Gml311Package.OFFSET_CURVE_TYPE__DISTANCE:
                setDistance((LengthType)null);
                return;
            case Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION:
                setRefDirection((VectorType)null);
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
            case Gml311Package.OFFSET_CURVE_TYPE__OFFSET_BASE:
                return offsetBase != null;
            case Gml311Package.OFFSET_CURVE_TYPE__DISTANCE:
                return distance != null;
            case Gml311Package.OFFSET_CURVE_TYPE__REF_DIRECTION:
                return refDirection != null;
        }
        return super.eIsSet(featureID);
    }

} //OffsetCurveTypeImpl
