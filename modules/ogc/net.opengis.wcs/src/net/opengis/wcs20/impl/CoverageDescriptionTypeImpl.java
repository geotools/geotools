/**
 */
package net.opengis.wcs20.impl;

import java.lang.Object;

import java.util.Collection;

import net.opengis.wcs20.CoverageDescriptionType;
import net.opengis.wcs20.ServiceParametersType;
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
 * An implementation of the model object '<em><b>Coverage Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getCoverageFunction <em>Coverage Function</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getRangeType <em>Range Type</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageDescriptionTypeImpl extends EObjectImpl implements CoverageDescriptionType {
    /**
     * The default value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected static final String COVERAGE_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected String coverageId = COVERAGE_ID_EDEFAULT;

    /**
     * The cached value of the '{@link #getCoverageFunction() <em>Coverage Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageFunction()
     * @generated
     * @ordered
     */
    protected Object coverageFunction;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList<Object> metadata;

    /**
     * The cached value of the '{@link #getRangeType() <em>Range Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeType()
     * @generated
     * @ordered
     */
    protected Object rangeType;

    /**
     * The cached value of the '{@link #getServiceParameters() <em>Service Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceParameters()
     * @generated
     * @ordered
     */
    protected ServiceParametersType serviceParameters;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageDescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.COVERAGE_DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCoverageId() {
        return coverageId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageId(String newCoverageId) {
        String oldCoverageId = coverageId;
        coverageId = newCoverageId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID, oldCoverageId, coverageId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getCoverageFunction() {
        return coverageFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageFunction(Object newCoverageFunction, NotificationChain msgs) {
        Object oldCoverageFunction = coverageFunction;
        coverageFunction = newCoverageFunction;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION, oldCoverageFunction, newCoverageFunction);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageFunction(Object newCoverageFunction) {
        if (newCoverageFunction != coverageFunction) {
            NotificationChain msgs = null;
            if (coverageFunction != null)
                msgs = ((InternalEObject)coverageFunction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION, null, msgs);
            if (newCoverageFunction != null)
                msgs = ((InternalEObject)newCoverageFunction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION, null, msgs);
            msgs = basicSetCoverageFunction(newCoverageFunction, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION, newCoverageFunction, newCoverageFunction));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Object> getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList<Object>(Object.class, this, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getDomainSet() {
        // TODO: implement this method to return the 'Domain Set' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDomainSet(Object newDomainSet, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Domain Set' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDomainSet(Object newDomainSet) {
        // TODO: implement this method to set the 'Domain Set' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getRangeType() {
        return rangeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRangeType(Object newRangeType, NotificationChain msgs) {
        Object oldRangeType = rangeType;
        rangeType = newRangeType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE, oldRangeType, newRangeType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeType(Object newRangeType) {
        if (newRangeType != rangeType) {
            NotificationChain msgs = null;
            if (rangeType != null)
                msgs = ((InternalEObject)rangeType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE, null, msgs);
            if (newRangeType != null)
                msgs = ((InternalEObject)newRangeType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE, null, msgs);
            msgs = basicSetRangeType(newRangeType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE, newRangeType, newRangeType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceParametersType getServiceParameters() {
        return serviceParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceParameters(ServiceParametersType newServiceParameters, NotificationChain msgs) {
        ServiceParametersType oldServiceParameters = serviceParameters;
        serviceParameters = newServiceParameters;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS, oldServiceParameters, newServiceParameters);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceParameters(ServiceParametersType newServiceParameters) {
        if (newServiceParameters != serviceParameters) {
            NotificationChain msgs = null;
            if (serviceParameters != null)
                msgs = ((InternalEObject)serviceParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS, null, msgs);
            if (newServiceParameters != null)
                msgs = ((InternalEObject)newServiceParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS, null, msgs);
            msgs = basicSetServiceParameters(newServiceParameters, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS, newServiceParameters, newServiceParameters));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION:
                return basicSetCoverageFunction(null, msgs);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET:
                return basicSetDomainSet(null, msgs);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE:
                return basicSetRangeType(null, msgs);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS:
                return basicSetServiceParameters(null, msgs);
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
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID:
                return getCoverageId();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION:
                return getCoverageFunction();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return getMetadata();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET:
                return getDomainSet();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE:
                return getRangeType();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS:
                return getServiceParameters();
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
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID:
                setCoverageId((String)newValue);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION:
                setCoverageFunction((Object)newValue);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends Object>)newValue);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET:
                setDomainSet((Object)newValue);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE:
                setRangeType((Object)newValue);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)newValue);
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
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID:
                setCoverageId(COVERAGE_ID_EDEFAULT);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION:
                setCoverageFunction((Object)null);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET:
                setDomainSet((Object)null);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE:
                setRangeType((Object)null);
                return;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)null);
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
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID:
                return COVERAGE_ID_EDEFAULT == null ? coverageId != null : !COVERAGE_ID_EDEFAULT.equals(coverageId);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION:
                return coverageFunction != null;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET:
                return getDomainSet() != null;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE:
                return rangeType != null;
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS:
                return serviceParameters != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (coverageId: ");
        result.append(coverageId);
        result.append(')');
        return result.toString();
    }

} //CoverageDescriptionTypeImpl
