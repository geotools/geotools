/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.ConventionalUnitType;
import net.opengis.gml311.ConversionToPreferredUnitType;
import net.opengis.gml311.DerivationUnitTermType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conventional Unit Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ConventionalUnitTypeImpl#getConversionToPreferredUnit <em>Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ConventionalUnitTypeImpl#getRoughConversionToPreferredUnit <em>Rough Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ConventionalUnitTypeImpl#getDerivationUnitTerm <em>Derivation Unit Term</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConventionalUnitTypeImpl extends UnitDefinitionTypeImpl implements ConventionalUnitType {
    /**
     * The cached value of the '{@link #getConversionToPreferredUnit() <em>Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConversionToPreferredUnit()
     * @generated
     * @ordered
     */
    protected ConversionToPreferredUnitType conversionToPreferredUnit;

    /**
     * The cached value of the '{@link #getRoughConversionToPreferredUnit() <em>Rough Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoughConversionToPreferredUnit()
     * @generated
     * @ordered
     */
    protected ConversionToPreferredUnitType roughConversionToPreferredUnit;

    /**
     * The cached value of the '{@link #getDerivationUnitTerm() <em>Derivation Unit Term</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDerivationUnitTerm()
     * @generated
     * @ordered
     */
    protected EList<DerivationUnitTermType> derivationUnitTerm;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConventionalUnitTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getConventionalUnitType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConversionToPreferredUnitType getConversionToPreferredUnit() {
        return conversionToPreferredUnit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConversionToPreferredUnit(ConversionToPreferredUnitType newConversionToPreferredUnit, NotificationChain msgs) {
        ConversionToPreferredUnitType oldConversionToPreferredUnit = conversionToPreferredUnit;
        conversionToPreferredUnit = newConversionToPreferredUnit;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT, oldConversionToPreferredUnit, newConversionToPreferredUnit);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConversionToPreferredUnit(ConversionToPreferredUnitType newConversionToPreferredUnit) {
        if (newConversionToPreferredUnit != conversionToPreferredUnit) {
            NotificationChain msgs = null;
            if (conversionToPreferredUnit != null)
                msgs = ((InternalEObject)conversionToPreferredUnit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT, null, msgs);
            if (newConversionToPreferredUnit != null)
                msgs = ((InternalEObject)newConversionToPreferredUnit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT, null, msgs);
            msgs = basicSetConversionToPreferredUnit(newConversionToPreferredUnit, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT, newConversionToPreferredUnit, newConversionToPreferredUnit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConversionToPreferredUnitType getRoughConversionToPreferredUnit() {
        return roughConversionToPreferredUnit;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRoughConversionToPreferredUnit(ConversionToPreferredUnitType newRoughConversionToPreferredUnit, NotificationChain msgs) {
        ConversionToPreferredUnitType oldRoughConversionToPreferredUnit = roughConversionToPreferredUnit;
        roughConversionToPreferredUnit = newRoughConversionToPreferredUnit;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT, oldRoughConversionToPreferredUnit, newRoughConversionToPreferredUnit);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRoughConversionToPreferredUnit(ConversionToPreferredUnitType newRoughConversionToPreferredUnit) {
        if (newRoughConversionToPreferredUnit != roughConversionToPreferredUnit) {
            NotificationChain msgs = null;
            if (roughConversionToPreferredUnit != null)
                msgs = ((InternalEObject)roughConversionToPreferredUnit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT, null, msgs);
            if (newRoughConversionToPreferredUnit != null)
                msgs = ((InternalEObject)newRoughConversionToPreferredUnit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT, null, msgs);
            msgs = basicSetRoughConversionToPreferredUnit(newRoughConversionToPreferredUnit, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT, newRoughConversionToPreferredUnit, newRoughConversionToPreferredUnit));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DerivationUnitTermType> getDerivationUnitTerm() {
        if (derivationUnitTerm == null) {
            derivationUnitTerm = new EObjectContainmentEList<DerivationUnitTermType>(DerivationUnitTermType.class, this, Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM);
        }
        return derivationUnitTerm;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT:
                return basicSetConversionToPreferredUnit(null, msgs);
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT:
                return basicSetRoughConversionToPreferredUnit(null, msgs);
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM:
                return ((InternalEList<?>)getDerivationUnitTerm()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT:
                return getConversionToPreferredUnit();
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT:
                return getRoughConversionToPreferredUnit();
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM:
                return getDerivationUnitTerm();
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
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT:
                setConversionToPreferredUnit((ConversionToPreferredUnitType)newValue);
                return;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT:
                setRoughConversionToPreferredUnit((ConversionToPreferredUnitType)newValue);
                return;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM:
                getDerivationUnitTerm().clear();
                getDerivationUnitTerm().addAll((Collection<? extends DerivationUnitTermType>)newValue);
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
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT:
                setConversionToPreferredUnit((ConversionToPreferredUnitType)null);
                return;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT:
                setRoughConversionToPreferredUnit((ConversionToPreferredUnitType)null);
                return;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM:
                getDerivationUnitTerm().clear();
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
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__CONVERSION_TO_PREFERRED_UNIT:
                return conversionToPreferredUnit != null;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__ROUGH_CONVERSION_TO_PREFERRED_UNIT:
                return roughConversionToPreferredUnit != null;
            case Gml311Package.CONVENTIONAL_UNIT_TYPE__DERIVATION_UNIT_TERM:
                return derivationUnitTerm != null && !derivationUnitTerm.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ConventionalUnitTypeImpl
