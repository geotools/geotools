/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.ComparisonOperatorsType;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.LogicalOperatorsType;
import net.opengis.fes20.ScalarCapabilitiesType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scalar Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl#getLogicalOperators <em>Logical Operators</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl#getComparisonOperators <em>Comparison Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScalarCapabilitiesTypeImpl extends EObjectImpl implements ScalarCapabilitiesType {
    /**
     * The cached value of the '{@link #getLogicalOperators() <em>Logical Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLogicalOperators()
     * @generated
     * @ordered
     */
    protected LogicalOperatorsType logicalOperators;

    /**
     * The cached value of the '{@link #getComparisonOperators() <em>Comparison Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComparisonOperators()
     * @generated
     * @ordered
     */
    protected ComparisonOperatorsType comparisonOperators;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScalarCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SCALAR_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LogicalOperatorsType getLogicalOperators() {
        return logicalOperators;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLogicalOperators(LogicalOperatorsType newLogicalOperators, NotificationChain msgs) {
        LogicalOperatorsType oldLogicalOperators = logicalOperators;
        logicalOperators = newLogicalOperators;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS, oldLogicalOperators, newLogicalOperators);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLogicalOperators(LogicalOperatorsType newLogicalOperators) {
        if (newLogicalOperators != logicalOperators) {
            NotificationChain msgs = null;
            if (logicalOperators != null)
                msgs = ((InternalEObject)logicalOperators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS, null, msgs);
            if (newLogicalOperators != null)
                msgs = ((InternalEObject)newLogicalOperators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS, null, msgs);
            msgs = basicSetLogicalOperators(newLogicalOperators, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS, newLogicalOperators, newLogicalOperators));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComparisonOperatorsType getComparisonOperators() {
        return comparisonOperators;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComparisonOperators(ComparisonOperatorsType newComparisonOperators, NotificationChain msgs) {
        ComparisonOperatorsType oldComparisonOperators = comparisonOperators;
        comparisonOperators = newComparisonOperators;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS, oldComparisonOperators, newComparisonOperators);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComparisonOperators(ComparisonOperatorsType newComparisonOperators) {
        if (newComparisonOperators != comparisonOperators) {
            NotificationChain msgs = null;
            if (comparisonOperators != null)
                msgs = ((InternalEObject)comparisonOperators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS, null, msgs);
            if (newComparisonOperators != null)
                msgs = ((InternalEObject)newComparisonOperators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS, null, msgs);
            msgs = basicSetComparisonOperators(newComparisonOperators, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS, newComparisonOperators, newComparisonOperators));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS:
                return basicSetLogicalOperators(null, msgs);
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS:
                return basicSetComparisonOperators(null, msgs);
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
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS:
                return getLogicalOperators();
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS:
                return getComparisonOperators();
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
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS:
                setLogicalOperators((LogicalOperatorsType)newValue);
                return;
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS:
                setComparisonOperators((ComparisonOperatorsType)newValue);
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
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS:
                setLogicalOperators((LogicalOperatorsType)null);
                return;
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS:
                setComparisonOperators((ComparisonOperatorsType)null);
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
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS:
                return logicalOperators != null;
            case Fes20Package.SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS:
                return comparisonOperators != null;
        }
        return super.eIsSet(featureID);
    }

} //ScalarCapabilitiesTypeImpl
