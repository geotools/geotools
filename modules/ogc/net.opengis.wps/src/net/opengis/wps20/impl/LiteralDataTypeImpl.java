/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.LiteralDataDomainType1;
import net.opengis.wps20.LiteralDataType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.LiteralDataTypeImpl#getLiteralDataDomain <em>Literal Data Domain</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LiteralDataTypeImpl extends DataDescriptionTypeImpl implements LiteralDataType {
	/**
	 * The cached value of the '{@link #getLiteralDataDomain() <em>Literal Data Domain</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteralDataDomain()
	 * @generated
	 * @ordered
	 */
	protected EList<LiteralDataDomainType1> literalDataDomain;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteralDataTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.LITERAL_DATA_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LiteralDataDomainType1> getLiteralDataDomain() {
		if (literalDataDomain == null) {
			literalDataDomain = new EObjectContainmentEList<LiteralDataDomainType1>(LiteralDataDomainType1.class, this, Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN);
		}
		return literalDataDomain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN:
				return ((InternalEList<?>)getLiteralDataDomain()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN:
				return getLiteralDataDomain();
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
			case Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN:
				getLiteralDataDomain().clear();
				getLiteralDataDomain().addAll((Collection<? extends LiteralDataDomainType1>)newValue);
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
			case Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN:
				getLiteralDataDomain().clear();
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
			case Wps20Package.LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN:
				return literalDataDomain != null && !literalDataDomain.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //LiteralDataTypeImpl
