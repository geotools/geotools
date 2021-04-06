/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.BoundingBoxDataType;
import net.opengis.wps20.SupportedCRSType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bounding Box Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.BoundingBoxDataTypeImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BoundingBoxDataTypeImpl extends DataDescriptionTypeImpl implements BoundingBoxDataType {
	/**
	 * The cached value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSupportedCRS()
	 * @generated
	 * @ordered
	 */
	protected EList<SupportedCRSType> supportedCRS;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BoundingBoxDataTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.BOUNDING_BOX_DATA_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SupportedCRSType> getSupportedCRS() {
		if (supportedCRS == null) {
			supportedCRS = new EObjectContainmentEList<SupportedCRSType>(SupportedCRSType.class, this, Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS);
		}
		return supportedCRS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS:
				return ((InternalEList<?>)getSupportedCRS()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS:
				return getSupportedCRS();
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
			case Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS:
				getSupportedCRS().clear();
				getSupportedCRS().addAll((Collection<? extends SupportedCRSType>)newValue);
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
			case Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS:
				getSupportedCRS().clear();
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
			case Wps20Package.BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS:
				return supportedCRS != null && !supportedCRS.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BoundingBoxDataTypeImpl
