/**
 */
package org.w3._2001.schema.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.schema.Facet;
import org.w3._2001.schema.LocalSimpleType;
import org.w3._2001.schema.NoFixedFacet;
import org.w3._2001.schema.NumFacet;
import org.w3._2001.schema.PatternType;
import org.w3._2001.schema.RestrictionType1;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.TotalDigitsType;
import org.w3._2001.schema.WhiteSpaceType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Restriction Type1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getFacets <em>Facets</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMinExclusive <em>Min Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMinInclusive <em>Min Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMaxExclusive <em>Max Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMaxInclusive <em>Max Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getTotalDigits <em>Total Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getFractionDigits <em>Fraction Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getLength <em>Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMinLength <em>Min Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getWhiteSpace <em>White Space</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RestrictionType1Impl#getBase <em>Base</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RestrictionType1Impl extends AnnotatedImpl implements RestrictionType1 {
	/**
	 * The cached value of the '{@link #getSimpleType() <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleType()
	 * @generated
	 * @ordered
	 */
	protected LocalSimpleType simpleType;

	/**
	 * The cached value of the '{@link #getFacets() <em>Facets</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacets()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap facets;

	/**
	 * The default value of the '{@link #getBase() <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase()
	 * @generated
	 * @ordered
	 */
	protected static final QName BASE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBase() <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase()
	 * @generated
	 * @ordered
	 */
	protected QName base = BASE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RestrictionType1Impl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.RESTRICTION_TYPE1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalSimpleType getSimpleType() {
		return simpleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleType(LocalSimpleType newSimpleType, NotificationChain msgs) {
		LocalSimpleType oldSimpleType = simpleType;
		simpleType = newSimpleType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE, oldSimpleType, newSimpleType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleType(LocalSimpleType newSimpleType) {
		if (newSimpleType != simpleType) {
			NotificationChain msgs = null;
			if (simpleType != null)
				msgs = ((InternalEObject)simpleType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE, null, msgs);
			if (newSimpleType != null)
				msgs = ((InternalEObject)newSimpleType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE, null, msgs);
			msgs = basicSetSimpleType(newSimpleType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE, newSimpleType, newSimpleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getFacets() {
		if (facets == null) {
			facets = new BasicFeatureMap(this, SchemaPackage.RESTRICTION_TYPE1__FACETS);
		}
		return facets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Facet> getMinExclusive() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MIN_EXCLUSIVE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Facet> getMinInclusive() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MIN_INCLUSIVE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Facet> getMaxExclusive() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MAX_EXCLUSIVE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Facet> getMaxInclusive() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MAX_INCLUSIVE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TotalDigitsType> getTotalDigits() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__TOTAL_DIGITS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NumFacet> getFractionDigits() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__FRACTION_DIGITS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NumFacet> getLength() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__LENGTH);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NumFacet> getMinLength() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MIN_LENGTH);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NumFacet> getMaxLength() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__MAX_LENGTH);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NoFixedFacet> getEnumeration() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__ENUMERATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<WhiteSpaceType> getWhiteSpace() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__WHITE_SPACE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PatternType> getPattern() {
		return getFacets().list(SchemaPackage.Literals.RESTRICTION_TYPE1__PATTERN);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getBase() {
		return base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase(QName newBase) {
		QName oldBase = base;
		base = newBase;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.RESTRICTION_TYPE1__BASE, oldBase, base));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE:
				return basicSetSimpleType(null, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__FACETS:
				return ((InternalEList<?>)getFacets()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MIN_EXCLUSIVE:
				return ((InternalEList<?>)getMinExclusive()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MIN_INCLUSIVE:
				return ((InternalEList<?>)getMinInclusive()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MAX_EXCLUSIVE:
				return ((InternalEList<?>)getMaxExclusive()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MAX_INCLUSIVE:
				return ((InternalEList<?>)getMaxInclusive()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__TOTAL_DIGITS:
				return ((InternalEList<?>)getTotalDigits()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__FRACTION_DIGITS:
				return ((InternalEList<?>)getFractionDigits()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__LENGTH:
				return ((InternalEList<?>)getLength()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MIN_LENGTH:
				return ((InternalEList<?>)getMinLength()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__MAX_LENGTH:
				return ((InternalEList<?>)getMaxLength()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__ENUMERATION:
				return ((InternalEList<?>)getEnumeration()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__WHITE_SPACE:
				return ((InternalEList<?>)getWhiteSpace()).basicRemove(otherEnd, msgs);
			case SchemaPackage.RESTRICTION_TYPE1__PATTERN:
				return ((InternalEList<?>)getPattern()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE:
				return getSimpleType();
			case SchemaPackage.RESTRICTION_TYPE1__FACETS:
				if (coreType) return getFacets();
				return ((FeatureMap.Internal)getFacets()).getWrapper();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_EXCLUSIVE:
				return getMinExclusive();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_INCLUSIVE:
				return getMinInclusive();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_EXCLUSIVE:
				return getMaxExclusive();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_INCLUSIVE:
				return getMaxInclusive();
			case SchemaPackage.RESTRICTION_TYPE1__TOTAL_DIGITS:
				return getTotalDigits();
			case SchemaPackage.RESTRICTION_TYPE1__FRACTION_DIGITS:
				return getFractionDigits();
			case SchemaPackage.RESTRICTION_TYPE1__LENGTH:
				return getLength();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_LENGTH:
				return getMinLength();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_LENGTH:
				return getMaxLength();
			case SchemaPackage.RESTRICTION_TYPE1__ENUMERATION:
				return getEnumeration();
			case SchemaPackage.RESTRICTION_TYPE1__WHITE_SPACE:
				return getWhiteSpace();
			case SchemaPackage.RESTRICTION_TYPE1__PATTERN:
				return getPattern();
			case SchemaPackage.RESTRICTION_TYPE1__BASE:
				return getBase();
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
			case SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE:
				setSimpleType((LocalSimpleType)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__FACETS:
				((FeatureMap.Internal)getFacets()).set(newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_EXCLUSIVE:
				getMinExclusive().clear();
				getMinExclusive().addAll((Collection<? extends Facet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_INCLUSIVE:
				getMinInclusive().clear();
				getMinInclusive().addAll((Collection<? extends Facet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_EXCLUSIVE:
				getMaxExclusive().clear();
				getMaxExclusive().addAll((Collection<? extends Facet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_INCLUSIVE:
				getMaxInclusive().clear();
				getMaxInclusive().addAll((Collection<? extends Facet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__TOTAL_DIGITS:
				getTotalDigits().clear();
				getTotalDigits().addAll((Collection<? extends TotalDigitsType>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__FRACTION_DIGITS:
				getFractionDigits().clear();
				getFractionDigits().addAll((Collection<? extends NumFacet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__LENGTH:
				getLength().clear();
				getLength().addAll((Collection<? extends NumFacet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_LENGTH:
				getMinLength().clear();
				getMinLength().addAll((Collection<? extends NumFacet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_LENGTH:
				getMaxLength().clear();
				getMaxLength().addAll((Collection<? extends NumFacet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__ENUMERATION:
				getEnumeration().clear();
				getEnumeration().addAll((Collection<? extends NoFixedFacet>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__WHITE_SPACE:
				getWhiteSpace().clear();
				getWhiteSpace().addAll((Collection<? extends WhiteSpaceType>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__PATTERN:
				getPattern().clear();
				getPattern().addAll((Collection<? extends PatternType>)newValue);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__BASE:
				setBase((QName)newValue);
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
			case SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE:
				setSimpleType((LocalSimpleType)null);
				return;
			case SchemaPackage.RESTRICTION_TYPE1__FACETS:
				getFacets().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_EXCLUSIVE:
				getMinExclusive().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_INCLUSIVE:
				getMinInclusive().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_EXCLUSIVE:
				getMaxExclusive().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_INCLUSIVE:
				getMaxInclusive().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__TOTAL_DIGITS:
				getTotalDigits().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__FRACTION_DIGITS:
				getFractionDigits().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__LENGTH:
				getLength().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MIN_LENGTH:
				getMinLength().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__MAX_LENGTH:
				getMaxLength().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__ENUMERATION:
				getEnumeration().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__WHITE_SPACE:
				getWhiteSpace().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__PATTERN:
				getPattern().clear();
				return;
			case SchemaPackage.RESTRICTION_TYPE1__BASE:
				setBase(BASE_EDEFAULT);
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
			case SchemaPackage.RESTRICTION_TYPE1__SIMPLE_TYPE:
				return simpleType != null;
			case SchemaPackage.RESTRICTION_TYPE1__FACETS:
				return facets != null && !facets.isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_EXCLUSIVE:
				return !getMinExclusive().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_INCLUSIVE:
				return !getMinInclusive().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_EXCLUSIVE:
				return !getMaxExclusive().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_INCLUSIVE:
				return !getMaxInclusive().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__TOTAL_DIGITS:
				return !getTotalDigits().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__FRACTION_DIGITS:
				return !getFractionDigits().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__LENGTH:
				return !getLength().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MIN_LENGTH:
				return !getMinLength().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__MAX_LENGTH:
				return !getMaxLength().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__ENUMERATION:
				return !getEnumeration().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__WHITE_SPACE:
				return !getWhiteSpace().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__PATTERN:
				return !getPattern().isEmpty();
			case SchemaPackage.RESTRICTION_TYPE1__BASE:
				return BASE_EDEFAULT == null ? base != null : !BASE_EDEFAULT.equals(base);
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
		result.append(" (facets: ");
		result.append(facets);
		result.append(", base: ");
		result.append(base);
		result.append(')');
		return result.toString();
	}

} //RestrictionType1Impl
