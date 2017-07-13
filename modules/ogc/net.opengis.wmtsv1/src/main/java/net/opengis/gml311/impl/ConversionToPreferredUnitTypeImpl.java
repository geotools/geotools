/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.ConversionToPreferredUnitType;
import net.opengis.gml311.FormulaType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conversion To Preferred Unit Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ConversionToPreferredUnitTypeImpl#getFactor <em>Factor</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ConversionToPreferredUnitTypeImpl#getFormula <em>Formula</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConversionToPreferredUnitTypeImpl extends UnitOfMeasureTypeImpl implements ConversionToPreferredUnitType {
    /**
     * The default value of the '{@link #getFactor() <em>Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFactor()
     * @generated
     * @ordered
     */
    protected static final double FACTOR_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getFactor() <em>Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFactor()
     * @generated
     * @ordered
     */
    protected double factor = FACTOR_EDEFAULT;

    /**
     * This is true if the Factor attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean factorESet;

    /**
     * The cached value of the '{@link #getFormula() <em>Formula</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormula()
     * @generated
     * @ordered
     */
    protected FormulaType formula;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConversionToPreferredUnitTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getConversionToPreferredUnitType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getFactor() {
        return factor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFactor(double newFactor) {
        double oldFactor = factor;
        factor = newFactor;
        boolean oldFactorESet = factorESet;
        factorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR, oldFactor, factor, !oldFactorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetFactor() {
        double oldFactor = factor;
        boolean oldFactorESet = factorESet;
        factor = FACTOR_EDEFAULT;
        factorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR, oldFactor, FACTOR_EDEFAULT, oldFactorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetFactor() {
        return factorESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FormulaType getFormula() {
        return formula;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFormula(FormulaType newFormula, NotificationChain msgs) {
        FormulaType oldFormula = formula;
        formula = newFormula;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA, oldFormula, newFormula);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormula(FormulaType newFormula) {
        if (newFormula != formula) {
            NotificationChain msgs = null;
            if (formula != null)
                msgs = ((InternalEObject)formula).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA, null, msgs);
            if (newFormula != null)
                msgs = ((InternalEObject)newFormula).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA, null, msgs);
            msgs = basicSetFormula(newFormula, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA, newFormula, newFormula));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA:
                return basicSetFormula(null, msgs);
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
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR:
                return getFactor();
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA:
                return getFormula();
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
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR:
                setFactor((Double)newValue);
                return;
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA:
                setFormula((FormulaType)newValue);
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
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR:
                unsetFactor();
                return;
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA:
                setFormula((FormulaType)null);
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
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FACTOR:
                return isSetFactor();
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE__FORMULA:
                return formula != null;
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
        result.append(" (factor: ");
        if (factorESet) result.append(factor); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ConversionToPreferredUnitTypeImpl
