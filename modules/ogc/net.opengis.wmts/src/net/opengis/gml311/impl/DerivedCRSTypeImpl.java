/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CoordinateSystemRefType;
import net.opengis.gml311.DerivedCRSType;
import net.opengis.gml311.DerivedCRSTypeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Derived CRS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DerivedCRSTypeImpl#getDerivedCRSType <em>Derived CRS Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DerivedCRSTypeImpl#getUsesCS <em>Uses CS</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DerivedCRSTypeImpl extends AbstractGeneralDerivedCRSTypeImpl implements DerivedCRSType {
    /**
     * The cached value of the '{@link #getDerivedCRSType() <em>Derived CRS Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDerivedCRSType()
     * @generated
     * @ordered
     */
    protected DerivedCRSTypeType derivedCRSType;

    /**
     * The cached value of the '{@link #getUsesCS() <em>Uses CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesCS()
     * @generated
     * @ordered
     */
    protected CoordinateSystemRefType usesCS;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DerivedCRSTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDerivedCRSType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivedCRSTypeType getDerivedCRSType() {
        return derivedCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDerivedCRSType(DerivedCRSTypeType newDerivedCRSType, NotificationChain msgs) {
        DerivedCRSTypeType oldDerivedCRSType = derivedCRSType;
        derivedCRSType = newDerivedCRSType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE, oldDerivedCRSType, newDerivedCRSType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDerivedCRSType(DerivedCRSTypeType newDerivedCRSType) {
        if (newDerivedCRSType != derivedCRSType) {
            NotificationChain msgs = null;
            if (derivedCRSType != null)
                msgs = ((InternalEObject)derivedCRSType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE, null, msgs);
            if (newDerivedCRSType != null)
                msgs = ((InternalEObject)newDerivedCRSType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE, null, msgs);
            msgs = basicSetDerivedCRSType(newDerivedCRSType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE, newDerivedCRSType, newDerivedCRSType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateSystemRefType getUsesCS() {
        return usesCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesCS(CoordinateSystemRefType newUsesCS, NotificationChain msgs) {
        CoordinateSystemRefType oldUsesCS = usesCS;
        usesCS = newUsesCS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DERIVED_CRS_TYPE__USES_CS, oldUsesCS, newUsesCS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesCS(CoordinateSystemRefType newUsesCS) {
        if (newUsesCS != usesCS) {
            NotificationChain msgs = null;
            if (usesCS != null)
                msgs = ((InternalEObject)usesCS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DERIVED_CRS_TYPE__USES_CS, null, msgs);
            if (newUsesCS != null)
                msgs = ((InternalEObject)newUsesCS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DERIVED_CRS_TYPE__USES_CS, null, msgs);
            msgs = basicSetUsesCS(newUsesCS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DERIVED_CRS_TYPE__USES_CS, newUsesCS, newUsesCS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE:
                return basicSetDerivedCRSType(null, msgs);
            case Gml311Package.DERIVED_CRS_TYPE__USES_CS:
                return basicSetUsesCS(null, msgs);
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
            case Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE:
                return getDerivedCRSType();
            case Gml311Package.DERIVED_CRS_TYPE__USES_CS:
                return getUsesCS();
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
            case Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE:
                setDerivedCRSType((DerivedCRSTypeType)newValue);
                return;
            case Gml311Package.DERIVED_CRS_TYPE__USES_CS:
                setUsesCS((CoordinateSystemRefType)newValue);
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
            case Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE:
                setDerivedCRSType((DerivedCRSTypeType)null);
                return;
            case Gml311Package.DERIVED_CRS_TYPE__USES_CS:
                setUsesCS((CoordinateSystemRefType)null);
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
            case Gml311Package.DERIVED_CRS_TYPE__DERIVED_CRS_TYPE:
                return derivedCRSType != null;
            case Gml311Package.DERIVED_CRS_TYPE__USES_CS:
                return usesCS != null;
        }
        return super.eIsSet(featureID);
    }

} //DerivedCRSTypeImpl
