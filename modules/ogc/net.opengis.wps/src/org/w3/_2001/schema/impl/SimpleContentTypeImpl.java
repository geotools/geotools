/**
 */
package org.w3._2001.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.SimpleContentType;
import org.w3._2001.schema.SimpleExtensionType;
import org.w3._2001.schema.SimpleRestrictionType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Content Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.SimpleContentTypeImpl#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SimpleContentTypeImpl#getExtension <em>Extension</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleContentTypeImpl extends AnnotatedImpl implements SimpleContentType {
	/**
	 * The cached value of the '{@link #getRestriction() <em>Restriction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestriction()
	 * @generated
	 * @ordered
	 */
	protected SimpleRestrictionType restriction;

	/**
	 * The cached value of the '{@link #getExtension() <em>Extension</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtension()
	 * @generated
	 * @ordered
	 */
	protected SimpleExtensionType extension;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleContentTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SIMPLE_CONTENT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleRestrictionType getRestriction() {
		return restriction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRestriction(SimpleRestrictionType newRestriction, NotificationChain msgs) {
		SimpleRestrictionType oldRestriction = restriction;
		restriction = newRestriction;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION, oldRestriction, newRestriction);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRestriction(SimpleRestrictionType newRestriction) {
		if (newRestriction != restriction) {
			NotificationChain msgs = null;
			if (restriction != null)
				msgs = ((InternalEObject)restriction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION, null, msgs);
			if (newRestriction != null)
				msgs = ((InternalEObject)newRestriction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION, null, msgs);
			msgs = basicSetRestriction(newRestriction, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION, newRestriction, newRestriction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleExtensionType getExtension() {
		return extension;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExtension(SimpleExtensionType newExtension, NotificationChain msgs) {
		SimpleExtensionType oldExtension = extension;
		extension = newExtension;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION, oldExtension, newExtension);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtension(SimpleExtensionType newExtension) {
		if (newExtension != extension) {
			NotificationChain msgs = null;
			if (extension != null)
				msgs = ((InternalEObject)extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION, null, msgs);
			if (newExtension != null)
				msgs = ((InternalEObject)newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION, null, msgs);
			msgs = basicSetExtension(newExtension, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION, newExtension, newExtension));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION:
				return basicSetRestriction(null, msgs);
			case SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION:
				return basicSetExtension(null, msgs);
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
			case SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION:
				return getRestriction();
			case SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION:
				return getExtension();
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
			case SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION:
				setRestriction((SimpleRestrictionType)newValue);
				return;
			case SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION:
				setExtension((SimpleExtensionType)newValue);
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
			case SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION:
				setRestriction((SimpleRestrictionType)null);
				return;
			case SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION:
				setExtension((SimpleExtensionType)null);
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
			case SchemaPackage.SIMPLE_CONTENT_TYPE__RESTRICTION:
				return restriction != null;
			case SchemaPackage.SIMPLE_CONTENT_TYPE__EXTENSION:
				return extension != null;
		}
		return super.eIsSet(featureID);
	}

} //SimpleContentTypeImpl
