/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiPointCoverageType;
import net.opengis.gml311.MultiPointDomainType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Point Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiPointCoverageTypeImpl#getMultiPointDomain <em>Multi Point Domain</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiPointCoverageTypeImpl extends AbstractDiscreteCoverageTypeImpl implements MultiPointCoverageType {
    /**
     * The cached value of the '{@link #getMultiPointDomain() <em>Multi Point Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMultiPointDomain()
     * @generated
     * @ordered
     */
    protected MultiPointDomainType multiPointDomain;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiPointCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiPointCoverageType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointDomainType getMultiPointDomain() {
        return multiPointDomain;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMultiPointDomain(MultiPointDomainType newMultiPointDomain, NotificationChain msgs) {
        MultiPointDomainType oldMultiPointDomain = multiPointDomain;
        multiPointDomain = newMultiPointDomain;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN, oldMultiPointDomain, newMultiPointDomain);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMultiPointDomain(MultiPointDomainType newMultiPointDomain) {
        if (newMultiPointDomain != multiPointDomain) {
            NotificationChain msgs = null;
            if (multiPointDomain != null)
                msgs = ((InternalEObject)multiPointDomain).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN, null, msgs);
            if (newMultiPointDomain != null)
                msgs = ((InternalEObject)newMultiPointDomain).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN, null, msgs);
            msgs = basicSetMultiPointDomain(newMultiPointDomain, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN, newMultiPointDomain, newMultiPointDomain));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN:
                return basicSetMultiPointDomain(null, msgs);
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
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN:
                return getMultiPointDomain();
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
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN:
                setMultiPointDomain((MultiPointDomainType)newValue);
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
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN:
                setMultiPointDomain((MultiPointDomainType)null);
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
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE__MULTI_POINT_DOMAIN:
                return multiPointDomain != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiPointCoverageTypeImpl
