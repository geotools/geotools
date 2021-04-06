/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.ContentsType;
import net.opengis.wps20.ProcessSummaryType;
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
 * An implementation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.ContentsTypeImpl#getProcessSummary <em>Process Summary</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContentsTypeImpl extends MinimalEObjectImpl.Container implements ContentsType {
	/**
	 * The cached value of the '{@link #getProcessSummary() <em>Process Summary</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessSummary()
	 * @generated
	 * @ordered
	 */
	protected EList<ProcessSummaryType> processSummary;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContentsTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.CONTENTS_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProcessSummaryType> getProcessSummary() {
		if (processSummary == null) {
			processSummary = new EObjectContainmentEList<ProcessSummaryType>(ProcessSummaryType.class, this, Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY);
		}
		return processSummary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY:
				return ((InternalEList<?>)getProcessSummary()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY:
				return getProcessSummary();
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
			case Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY:
				getProcessSummary().clear();
				getProcessSummary().addAll((Collection<? extends ProcessSummaryType>)newValue);
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
			case Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY:
				getProcessSummary().clear();
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
			case Wps20Package.CONTENTS_TYPE__PROCESS_SUMMARY:
				return processSummary != null && !processSummary.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ContentsTypeImpl
