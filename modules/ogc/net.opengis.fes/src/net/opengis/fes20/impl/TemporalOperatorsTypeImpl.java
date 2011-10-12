/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.Collection;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.TemporalOperatorType;
import net.opengis.fes20.TemporalOperatorsType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Temporal Operators Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.TemporalOperatorsTypeImpl#getTemporalOperator <em>Temporal Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TemporalOperatorsTypeImpl extends EObjectImpl implements TemporalOperatorsType {
    /**
     * The cached value of the '{@link #getTemporalOperator() <em>Temporal Operator</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalOperator()
     * @generated
     * @ordered
     */
    protected EList<TemporalOperatorType> temporalOperator;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TemporalOperatorsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.TEMPORAL_OPERATORS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TemporalOperatorType> getTemporalOperator() {
        if (temporalOperator == null) {
            temporalOperator = new EObjectContainmentEList<TemporalOperatorType>(TemporalOperatorType.class, this, Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR);
        }
        return temporalOperator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR:
                return ((InternalEList<?>)getTemporalOperator()).basicRemove(otherEnd, msgs);
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
            case Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR:
                return getTemporalOperator();
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
            case Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR:
                getTemporalOperator().clear();
                getTemporalOperator().addAll((Collection<? extends TemporalOperatorType>)newValue);
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
            case Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR:
                getTemporalOperator().clear();
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
            case Fes20Package.TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR:
                return temporalOperator != null && !temporalOperator.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TemporalOperatorsTypeImpl
