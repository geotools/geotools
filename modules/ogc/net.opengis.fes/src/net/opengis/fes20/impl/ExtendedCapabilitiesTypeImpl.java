/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.AdditionalOperatorsType;
import net.opengis.fes20.ExtendedCapabilitiesType;
import net.opengis.fes20.Fes20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extended Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.ExtendedCapabilitiesTypeImpl#getAdditionalOperators <em>Additional Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtendedCapabilitiesTypeImpl extends EObjectImpl implements ExtendedCapabilitiesType {
    /**
     * The cached value of the '{@link #getAdditionalOperators() <em>Additional Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionalOperators()
     * @generated
     * @ordered
     */
    protected AdditionalOperatorsType additionalOperators;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExtendedCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.EXTENDED_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalOperatorsType getAdditionalOperators() {
        return additionalOperators;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAdditionalOperators(AdditionalOperatorsType newAdditionalOperators, NotificationChain msgs) {
        AdditionalOperatorsType oldAdditionalOperators = additionalOperators;
        additionalOperators = newAdditionalOperators;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS, oldAdditionalOperators, newAdditionalOperators);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAdditionalOperators(AdditionalOperatorsType newAdditionalOperators) {
        if (newAdditionalOperators != additionalOperators) {
            NotificationChain msgs = null;
            if (additionalOperators != null)
                msgs = ((InternalEObject)additionalOperators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS, null, msgs);
            if (newAdditionalOperators != null)
                msgs = ((InternalEObject)newAdditionalOperators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS, null, msgs);
            msgs = basicSetAdditionalOperators(newAdditionalOperators, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS, newAdditionalOperators, newAdditionalOperators));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS:
                return basicSetAdditionalOperators(null, msgs);
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
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS:
                return getAdditionalOperators();
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
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS:
                setAdditionalOperators((AdditionalOperatorsType)newValue);
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
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS:
                setAdditionalOperators((AdditionalOperatorsType)null);
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
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS:
                return additionalOperators != null;
        }
        return super.eIsSet(featureID);
    }

} //ExtendedCapabilitiesTypeImpl
