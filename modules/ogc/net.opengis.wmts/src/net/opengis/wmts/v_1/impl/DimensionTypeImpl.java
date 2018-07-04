/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DomainMetadataType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dimension Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#getUnitSymbol <em>Unit Symbol</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#isCurrent <em>Current</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DimensionTypeImpl extends DescriptionTypeImpl implements DimensionType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The cached value of the '{@link #getUOM() <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOM()
     * @generated
     * @ordered
     */
    protected DomainMetadataType uOM;

    /**
     * The default value of the '{@link #getUnitSymbol() <em>Unit Symbol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUnitSymbol()
     * @generated
     * @ordered
     */
    protected static final String UNIT_SYMBOL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUnitSymbol() <em>Unit Symbol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUnitSymbol()
     * @generated
     * @ordered
     */
    protected String unitSymbol = UNIT_SYMBOL_EDEFAULT;

    /**
     * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected String default_ = DEFAULT_EDEFAULT;

    /**
     * The default value of the '{@link #isCurrent() <em>Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCurrent()
     * @generated
     * @ordered
     */
    protected static final boolean CURRENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isCurrent() <em>Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isCurrent()
     * @generated
     * @ordered
     */
    protected boolean current = CURRENT_EDEFAULT;

    /**
     * This is true if the Current attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean currentESet;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected EList<String> value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DimensionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.DIMENSION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getUOM() {
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOM(DomainMetadataType newUOM, NotificationChain msgs) {
        DomainMetadataType oldUOM = uOM;
        uOM = newUOM;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__UOM, oldUOM, newUOM);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(DomainMetadataType newUOM) {
        if (newUOM != uOM) {
            NotificationChain msgs = null;
            if (uOM != null)
                msgs = ((InternalEObject)uOM).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.DIMENSION_TYPE__UOM, null, msgs);
            if (newUOM != null)
                msgs = ((InternalEObject)newUOM).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.DIMENSION_TYPE__UOM, null, msgs);
            msgs = basicSetUOM(newUOM, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__UOM, newUOM, newUOM));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUnitSymbol() {
        return unitSymbol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUnitSymbol(String newUnitSymbol) {
        String oldUnitSymbol = unitSymbol;
        unitSymbol = newUnitSymbol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__UNIT_SYMBOL, oldUnitSymbol, unitSymbol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefault() {
        return default_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefault(String newDefault) {
        String oldDefault = default_;
        default_ = newDefault;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__DEFAULT, oldDefault, default_));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isCurrent() {
        return current;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCurrent(boolean newCurrent) {
        boolean oldCurrent = current;
        current = newCurrent;
        boolean oldCurrentESet = currentESet;
        currentESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.DIMENSION_TYPE__CURRENT, oldCurrent, current, !oldCurrentESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetCurrent() {
        boolean oldCurrent = current;
        boolean oldCurrentESet = currentESet;
        current = CURRENT_EDEFAULT;
        currentESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.DIMENSION_TYPE__CURRENT, oldCurrent, CURRENT_EDEFAULT, oldCurrentESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetCurrent() {
        return currentESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getValue() {
        if (value == null) {
            value = new EDataTypeEList<String>(String.class, this, wmtsv_1Package.DIMENSION_TYPE__VALUE);
        }
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case wmtsv_1Package.DIMENSION_TYPE__UOM:
                return basicSetUOM(null, msgs);
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
            case wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER:
                return getIdentifier();
            case wmtsv_1Package.DIMENSION_TYPE__UOM:
                return getUOM();
            case wmtsv_1Package.DIMENSION_TYPE__UNIT_SYMBOL:
                return getUnitSymbol();
            case wmtsv_1Package.DIMENSION_TYPE__DEFAULT:
                return getDefault();
            case wmtsv_1Package.DIMENSION_TYPE__CURRENT:
                return isCurrent();
            case wmtsv_1Package.DIMENSION_TYPE__VALUE:
                return getValue();
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
            case wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__UOM:
                setUOM((DomainMetadataType)newValue);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__UNIT_SYMBOL:
                setUnitSymbol((String)newValue);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__DEFAULT:
                setDefault((String)newValue);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__CURRENT:
                setCurrent((Boolean)newValue);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__VALUE:
                getValue().clear();
                getValue().addAll((Collection<? extends String>)newValue);
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
            case wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__UOM:
                setUOM((DomainMetadataType)null);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__UNIT_SYMBOL:
                setUnitSymbol(UNIT_SYMBOL_EDEFAULT);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__DEFAULT:
                setDefault(DEFAULT_EDEFAULT);
                return;
            case wmtsv_1Package.DIMENSION_TYPE__CURRENT:
                unsetCurrent();
                return;
            case wmtsv_1Package.DIMENSION_TYPE__VALUE:
                getValue().clear();
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
            case wmtsv_1Package.DIMENSION_TYPE__IDENTIFIER:
                return identifier != null;
            case wmtsv_1Package.DIMENSION_TYPE__UOM:
                return uOM != null;
            case wmtsv_1Package.DIMENSION_TYPE__UNIT_SYMBOL:
                return UNIT_SYMBOL_EDEFAULT == null ? unitSymbol != null : !UNIT_SYMBOL_EDEFAULT.equals(unitSymbol);
            case wmtsv_1Package.DIMENSION_TYPE__DEFAULT:
                return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
            case wmtsv_1Package.DIMENSION_TYPE__CURRENT:
                return isSetCurrent();
            case wmtsv_1Package.DIMENSION_TYPE__VALUE:
                return value != null && !value.isEmpty();
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
        result.append(" (unitSymbol: ");
        result.append(unitSymbol);
        result.append(", default: ");
        result.append(default_);
        result.append(", current: ");
        if (currentESet) result.append(current); else result.append("<unset>");
        result.append(", value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

} //DimensionTypeImpl
