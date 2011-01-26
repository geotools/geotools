/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.AnyValueType;

import net.opengis.wps10.LiteralInputType;
import net.opengis.wps10.ValuesReferenceType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Input Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.LiteralInputTypeImpl#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.LiteralInputTypeImpl#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.LiteralInputTypeImpl#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.LiteralInputTypeImpl#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LiteralInputTypeImpl extends LiteralOutputTypeImpl implements LiteralInputType {
    /**
     * The cached value of the '{@link #getAllowedValues() <em>Allowed Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAllowedValues()
     * @generated
     * @ordered
     */
    protected AllowedValuesType allowedValues;

    /**
     * The cached value of the '{@link #getAnyValue() <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnyValue()
     * @generated
     * @ordered
     */
    protected AnyValueType anyValue;

    /**
     * The cached value of the '{@link #getValuesReference() <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValuesReference()
     * @generated
     * @ordered
     */
    protected ValuesReferenceType valuesReference;

    /**
     * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LiteralInputTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.LITERAL_INPUT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType getAllowedValues() {
        return allowedValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAllowedValues(AllowedValuesType newAllowedValues, NotificationChain msgs) {
        AllowedValuesType oldAllowedValues = allowedValues;
        allowedValues = newAllowedValues;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES, oldAllowedValues, newAllowedValues);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAllowedValues(AllowedValuesType newAllowedValues) {
        if (newAllowedValues != allowedValues) {
            NotificationChain msgs = null;
            if (allowedValues != null)
                msgs = ((InternalEObject)allowedValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES, null, msgs);
            if (newAllowedValues != null)
                msgs = ((InternalEObject)newAllowedValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES, null, msgs);
            msgs = basicSetAllowedValues(newAllowedValues, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES, newAllowedValues, newAllowedValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType getAnyValue() {
        return anyValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyValue(AnyValueType newAnyValue, NotificationChain msgs) {
        AnyValueType oldAnyValue = anyValue;
        anyValue = newAnyValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE, oldAnyValue, newAnyValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyValue(AnyValueType newAnyValue) {
        if (newAnyValue != anyValue) {
            NotificationChain msgs = null;
            if (anyValue != null)
                msgs = ((InternalEObject)anyValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE, null, msgs);
            if (newAnyValue != null)
                msgs = ((InternalEObject)newAnyValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE, null, msgs);
            msgs = basicSetAnyValue(newAnyValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE, newAnyValue, newAnyValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType getValuesReference() {
        return valuesReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValuesReference(ValuesReferenceType newValuesReference, NotificationChain msgs) {
        ValuesReferenceType oldValuesReference = valuesReference;
        valuesReference = newValuesReference;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE, oldValuesReference, newValuesReference);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesReference(ValuesReferenceType newValuesReference) {
        if (newValuesReference != valuesReference) {
            NotificationChain msgs = null;
            if (valuesReference != null)
                msgs = ((InternalEObject)valuesReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE, null, msgs);
            if (newValuesReference != null)
                msgs = ((InternalEObject)newValuesReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE, null, msgs);
            msgs = basicSetValuesReference(newValuesReference, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE, newValuesReference, newValuesReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(String newDefaultValue) {
        String oldDefaultValue = defaultValue;
        defaultValue = newDefaultValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_INPUT_TYPE__DEFAULT_VALUE, oldDefaultValue, defaultValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES:
                return basicSetAllowedValues(null, msgs);
            case Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE:
                return basicSetAnyValue(null, msgs);
            case Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE:
                return basicSetValuesReference(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES:
                return getAllowedValues();
            case Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE:
                return getAnyValue();
            case Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE:
                return getValuesReference();
            case Wps10Package.LITERAL_INPUT_TYPE__DEFAULT_VALUE:
                return getDefaultValue();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)newValue);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE:
                setAnyValue((AnyValueType)newValue);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)newValue);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__DEFAULT_VALUE:
                setDefaultValue((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)null);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE:
                setAnyValue((AnyValueType)null);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)null);
                return;
            case Wps10Package.LITERAL_INPUT_TYPE__DEFAULT_VALUE:
                setDefaultValue(DEFAULT_VALUE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wps10Package.LITERAL_INPUT_TYPE__ALLOWED_VALUES:
                return allowedValues != null;
            case Wps10Package.LITERAL_INPUT_TYPE__ANY_VALUE:
                return anyValue != null;
            case Wps10Package.LITERAL_INPUT_TYPE__VALUES_REFERENCE:
                return valuesReference != null;
            case Wps10Package.LITERAL_INPUT_TYPE__DEFAULT_VALUE:
                return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (defaultValue: ");
        result.append(defaultValue);
        result.append(')');
        return result.toString();
    }

} //LiteralInputTypeImpl
