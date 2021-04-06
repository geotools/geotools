/**
 */
package org.w3._2001.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.w3._2001.schema.ComplexContentType;
import org.w3._2001.schema.ComplexRestrictionType;
import org.w3._2001.schema.ExtensionType;
import org.w3._2001.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Content Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.ComplexContentTypeImpl#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexContentTypeImpl#getExtension <em>Extension</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexContentTypeImpl#isMixed <em>Mixed</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComplexContentTypeImpl extends AnnotatedImpl implements ComplexContentType {
	/**
	 * The cached value of the '{@link #getRestriction() <em>Restriction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestriction()
	 * @generated
	 * @ordered
	 */
	protected ComplexRestrictionType restriction;

	/**
	 * The cached value of the '{@link #getExtension() <em>Extension</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtension()
	 * @generated
	 * @ordered
	 */
	protected ExtensionType extension;

	/**
	 * The default value of the '{@link #isMixed() <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMixed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MIXED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMixed() <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMixed()
	 * @generated
	 * @ordered
	 */
	protected boolean mixed = MIXED_EDEFAULT;

	/**
	 * This is true if the Mixed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean mixedESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComplexContentTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.COMPLEX_CONTENT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexRestrictionType getRestriction() {
		return restriction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRestriction(ComplexRestrictionType newRestriction, NotificationChain msgs) {
		ComplexRestrictionType oldRestriction = restriction;
		restriction = newRestriction;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION, oldRestriction, newRestriction);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRestriction(ComplexRestrictionType newRestriction) {
		if (newRestriction != restriction) {
			NotificationChain msgs = null;
			if (restriction != null)
				msgs = ((InternalEObject)restriction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION, null, msgs);
			if (newRestriction != null)
				msgs = ((InternalEObject)newRestriction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION, null, msgs);
			msgs = basicSetRestriction(newRestriction, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION, newRestriction, newRestriction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionType getExtension() {
		return extension;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExtension(ExtensionType newExtension, NotificationChain msgs) {
		ExtensionType oldExtension = extension;
		extension = newExtension;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION, oldExtension, newExtension);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtension(ExtensionType newExtension) {
		if (newExtension != extension) {
			NotificationChain msgs = null;
			if (extension != null)
				msgs = ((InternalEObject)extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION, null, msgs);
			if (newExtension != null)
				msgs = ((InternalEObject)newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION, null, msgs);
			msgs = basicSetExtension(newExtension, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION, newExtension, newExtension));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMixed() {
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMixed(boolean newMixed) {
		boolean oldMixed = mixed;
		mixed = newMixed;
		boolean oldMixedESet = mixedESet;
		mixedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED, oldMixed, mixed, !oldMixedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMixed() {
		boolean oldMixed = mixed;
		boolean oldMixedESet = mixedESet;
		mixed = MIXED_EDEFAULT;
		mixedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED, oldMixed, MIXED_EDEFAULT, oldMixedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMixed() {
		return mixedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION:
				return basicSetRestriction(null, msgs);
			case SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION:
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
			case SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION:
				return getRestriction();
			case SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION:
				return getExtension();
			case SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED:
				return isMixed();
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
			case SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION:
				setRestriction((ComplexRestrictionType)newValue);
				return;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION:
				setExtension((ExtensionType)newValue);
				return;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED:
				setMixed((Boolean)newValue);
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
			case SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION:
				setRestriction((ComplexRestrictionType)null);
				return;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION:
				setExtension((ExtensionType)null);
				return;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED:
				unsetMixed();
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
			case SchemaPackage.COMPLEX_CONTENT_TYPE__RESTRICTION:
				return restriction != null;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__EXTENSION:
				return extension != null;
			case SchemaPackage.COMPLEX_CONTENT_TYPE__MIXED:
				return isSetMixed();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (mixed: ");
		if (mixedESet) result.append(mixed); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ComplexContentTypeImpl
