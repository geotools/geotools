/**
 */
package net.opengis.wps20.impl;

import net.opengis.ows20.AllowedValuesType;
import net.opengis.ows20.AnyValueType;
import net.opengis.ows20.DomainMetadataType;
import net.opengis.ows20.ValueType;
import net.opengis.ows20.ValuesReferenceType;

import net.opengis.wps20.LiteralDataDomainType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Data Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LiteralDataDomainTypeImpl extends MinimalEObjectImpl.Container implements LiteralDataDomainType {
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
	 * The cached value of the '{@link #getDataType() <em>Data Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataType()
	 * @generated
	 * @ordered
	 */
	protected DomainMetadataType dataType;

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
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected ValueType defaultValue;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteralDataDomainTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.LITERAL_DATA_DOMAIN_TYPE;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES, oldAllowedValues, newAllowedValues);
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
				msgs = ((InternalEObject)allowedValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES, null, msgs);
			if (newAllowedValues != null)
				msgs = ((InternalEObject)newAllowedValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES, null, msgs);
			msgs = basicSetAllowedValues(newAllowedValues, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES, newAllowedValues, newAllowedValues));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE, oldAnyValue, newAnyValue);
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
				msgs = ((InternalEObject)anyValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE, null, msgs);
			if (newAnyValue != null)
				msgs = ((InternalEObject)newAnyValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE, null, msgs);
			msgs = basicSetAnyValue(newAnyValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE, newAnyValue, newAnyValue));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE, oldValuesReference, newValuesReference);
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
				msgs = ((InternalEObject)valuesReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE, null, msgs);
			if (newValuesReference != null)
				msgs = ((InternalEObject)newValuesReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE, null, msgs);
			msgs = basicSetValuesReference(newValuesReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE, newValuesReference, newValuesReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomainMetadataType getDataType() {
		return dataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDataType(DomainMetadataType newDataType, NotificationChain msgs) {
		DomainMetadataType oldDataType = dataType;
		dataType = newDataType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE, oldDataType, newDataType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataType(DomainMetadataType newDataType) {
		if (newDataType != dataType) {
			NotificationChain msgs = null;
			if (dataType != null)
				msgs = ((InternalEObject)dataType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE, null, msgs);
			if (newDataType != null)
				msgs = ((InternalEObject)newDataType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE, null, msgs);
			msgs = basicSetDataType(newDataType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE, newDataType, newDataType));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM, oldUOM, newUOM);
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
				msgs = ((InternalEObject)uOM).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM, null, msgs);
			if (newUOM != null)
				msgs = ((InternalEObject)newUOM).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM, null, msgs);
			msgs = basicSetUOM(newUOM, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM, newUOM, newUOM));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueType getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDefaultValue(ValueType newDefaultValue, NotificationChain msgs) {
		ValueType oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE, oldDefaultValue, newDefaultValue);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultValue(ValueType newDefaultValue) {
		if (newDefaultValue != defaultValue) {
			NotificationChain msgs = null;
			if (defaultValue != null)
				msgs = ((InternalEObject)defaultValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE, null, msgs);
			if (newDefaultValue != null)
				msgs = ((InternalEObject)newDefaultValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE, null, msgs);
			msgs = basicSetDefaultValue(newDefaultValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE, newDefaultValue, newDefaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES:
				return basicSetAllowedValues(null, msgs);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE:
				return basicSetAnyValue(null, msgs);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE:
				return basicSetValuesReference(null, msgs);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE:
				return basicSetDataType(null, msgs);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM:
				return basicSetUOM(null, msgs);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE:
				return basicSetDefaultValue(null, msgs);
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
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES:
				return getAllowedValues();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE:
				return getAnyValue();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE:
				return getValuesReference();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE:
				return getDataType();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM:
				return getUOM();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE:
				return getDefaultValue();
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
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES:
				setAllowedValues((AllowedValuesType)newValue);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE:
				setAnyValue((AnyValueType)newValue);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE:
				setValuesReference((ValuesReferenceType)newValue);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE:
				setDataType((DomainMetadataType)newValue);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM:
				setUOM((DomainMetadataType)newValue);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE:
				setDefaultValue((ValueType)newValue);
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
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES:
				setAllowedValues((AllowedValuesType)null);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE:
				setAnyValue((AnyValueType)null);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE:
				setValuesReference((ValuesReferenceType)null);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE:
				setDataType((DomainMetadataType)null);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM:
				setUOM((DomainMetadataType)null);
				return;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE:
				setDefaultValue((ValueType)null);
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
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES:
				return allowedValues != null;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE:
				return anyValue != null;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE:
				return valuesReference != null;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE:
				return dataType != null;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__UOM:
				return uOM != null;
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE:
				return defaultValue != null;
		}
		return super.eIsSet(featureID);
	}

} //LiteralDataDomainTypeImpl
