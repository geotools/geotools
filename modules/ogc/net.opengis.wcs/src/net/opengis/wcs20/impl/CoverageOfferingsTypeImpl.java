/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.CoverageOfferingsType;
import net.opengis.wcs20.OfferedCoverageType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.CoverageOfferingsTypeImpl#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageOfferingsTypeImpl#getOfferedCoverage <em>Offered Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageOfferingsTypeImpl extends EObjectImpl implements CoverageOfferingsType {
    /**
     * The cached value of the '{@link #getServiceMetadata() <em>Service Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceMetadata()
     * @generated
     * @ordered
     */
    protected ServiceMetadataType serviceMetadata;

    /**
     * The cached value of the '{@link #getOfferedCoverage() <em>Offered Coverage</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOfferedCoverage()
     * @generated
     * @ordered
     */
    protected EList<OfferedCoverageType> offeredCoverage;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageOfferingsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.COVERAGE_OFFERINGS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceMetadataType getServiceMetadata() {
        return serviceMetadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceMetadata(ServiceMetadataType newServiceMetadata, NotificationChain msgs) {
        ServiceMetadataType oldServiceMetadata = serviceMetadata;
        serviceMetadata = newServiceMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA, oldServiceMetadata, newServiceMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceMetadata(ServiceMetadataType newServiceMetadata) {
        if (newServiceMetadata != serviceMetadata) {
            NotificationChain msgs = null;
            if (serviceMetadata != null)
                msgs = ((InternalEObject)serviceMetadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA, null, msgs);
            if (newServiceMetadata != null)
                msgs = ((InternalEObject)newServiceMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA, null, msgs);
            msgs = basicSetServiceMetadata(newServiceMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA, newServiceMetadata, newServiceMetadata));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<OfferedCoverageType> getOfferedCoverage() {
        if (offeredCoverage == null) {
            offeredCoverage = new EObjectContainmentEList<OfferedCoverageType>(OfferedCoverageType.class, this, Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE);
        }
        return offeredCoverage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA:
                return basicSetServiceMetadata(null, msgs);
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE:
                return ((InternalEList<?>)getOfferedCoverage()).basicRemove(otherEnd, msgs);
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
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA:
                return getServiceMetadata();
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE:
                return getOfferedCoverage();
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
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)newValue);
                return;
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE:
                getOfferedCoverage().clear();
                getOfferedCoverage().addAll((Collection<? extends OfferedCoverageType>)newValue);
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
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)null);
                return;
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE:
                getOfferedCoverage().clear();
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
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA:
                return serviceMetadata != null;
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE:
                return offeredCoverage != null && !offeredCoverage.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CoverageOfferingsTypeImpl
