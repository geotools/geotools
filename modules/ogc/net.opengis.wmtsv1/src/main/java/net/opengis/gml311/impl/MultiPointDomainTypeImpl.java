/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiPointDomainType;
import net.opengis.gml311.MultiPointType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Point Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiPointDomainTypeImpl#getMultiPoint <em>Multi Point</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiPointDomainTypeImpl extends DomainSetTypeImpl implements MultiPointDomainType {
    /**
     * The cached value of the '{@link #getMultiPoint() <em>Multi Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMultiPoint()
     * @generated
     * @ordered
     */
    protected MultiPointType multiPoint;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiPointDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiPointDomainType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointType getMultiPoint() {
        return multiPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMultiPoint(MultiPointType newMultiPoint, NotificationChain msgs) {
        MultiPointType oldMultiPoint = multiPoint;
        multiPoint = newMultiPoint;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT, oldMultiPoint, newMultiPoint);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMultiPoint(MultiPointType newMultiPoint) {
        if (newMultiPoint != multiPoint) {
            NotificationChain msgs = null;
            if (multiPoint != null)
                msgs = ((InternalEObject)multiPoint).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT, null, msgs);
            if (newMultiPoint != null)
                msgs = ((InternalEObject)newMultiPoint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT, null, msgs);
            msgs = basicSetMultiPoint(newMultiPoint, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT, newMultiPoint, newMultiPoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT:
                return basicSetMultiPoint(null, msgs);
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
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT:
                return getMultiPoint();
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
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT:
                setMultiPoint((MultiPointType)newValue);
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
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT:
                setMultiPoint((MultiPointType)null);
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
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE__MULTI_POINT:
                return multiPoint != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiPointDomainTypeImpl
