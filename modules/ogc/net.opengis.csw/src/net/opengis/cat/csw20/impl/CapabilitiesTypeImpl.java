/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import net.opengis.cat.csw20.CapabilitiesType;
import net.opengis.cat.csw20.Csw20Package;

import net.opengis.ows10.impl.CapabilitiesBaseTypeImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.opengis.filter.capability.FilterCapabilities;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.CapabilitiesTypeImpl#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements CapabilitiesType {
    /**
     * The cached value of the '{@link #getFilterCapabilities() <em>Filter Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilterCapabilities()
     * @generated
     * @ordered
     */
    protected FilterCapabilities filterCapabilities;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFilterCapabilities(FilterCapabilities newFilterCapabilities, NotificationChain msgs) {
        FilterCapabilities oldFilterCapabilities = filterCapabilities;
        filterCapabilities = newFilterCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES, oldFilterCapabilities, newFilterCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public void setFilterCapabilities(FilterCapabilities newFilterCapabilities) {
        this.filterCapabilities = newFilterCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return basicSetFilterCapabilities(null, msgs);
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
            case Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return getFilterCapabilities();
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
            case Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilities)newValue);
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
            case Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilities)null);
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
            case Csw20Package.CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return filterCapabilities != null;
        }
        return super.eIsSet(featureID);
    }

} //CapabilitiesTypeImpl
