/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.ProcessOfferingType;
import net.opengis.wps20.ProcessOfferingsType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.ProcessOfferingsTypeImpl#getProcessOffering <em>Process Offering</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessOfferingsTypeImpl extends MinimalEObjectImpl.Container implements ProcessOfferingsType {
	/**
	 * The cached value of the '{@link #getProcessOffering() <em>Process Offering</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessOffering()
	 * @generated
	 * @ordered
	 */
	protected EList<ProcessOfferingType> processOffering;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProcessOfferingsTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.PROCESS_OFFERINGS_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProcessOfferingType> getProcessOffering() {
		if (processOffering == null) {
			processOffering = new EObjectContainmentEList<ProcessOfferingType>(ProcessOfferingType.class, this, Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING);
		}
		return processOffering;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING:
				return ((InternalEList<?>)getProcessOffering()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING:
				return getProcessOffering();
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
			case Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING:
				getProcessOffering().clear();
				getProcessOffering().addAll((Collection<? extends ProcessOfferingType>)newValue);
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
			case Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING:
				getProcessOffering().clear();
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
			case Wps20Package.PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING:
				return processOffering != null && !processOffering.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ProcessOfferingsTypeImpl
