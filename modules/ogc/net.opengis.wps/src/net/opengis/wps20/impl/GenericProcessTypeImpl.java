/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.GenericInputType;
import net.opengis.wps20.GenericOutputType;
import net.opengis.wps20.GenericProcessType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generic Process Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.GenericProcessTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.GenericProcessTypeImpl#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GenericProcessTypeImpl extends DescriptionTypeImpl implements GenericProcessType {
	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<GenericInputType> input;

	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected EList<GenericOutputType> output;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenericProcessTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.GENERIC_PROCESS_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GenericInputType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<GenericInputType>(GenericInputType.class, this, Wps20Package.GENERIC_PROCESS_TYPE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GenericOutputType> getOutput() {
		if (output == null) {
			output = new EObjectContainmentEList<GenericOutputType>(GenericOutputType.class, this, Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT);
		}
		return output;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.GENERIC_PROCESS_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
			case Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT:
				return ((InternalEList<?>)getOutput()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.GENERIC_PROCESS_TYPE__INPUT:
				return getInput();
			case Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT:
				return getOutput();
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
			case Wps20Package.GENERIC_PROCESS_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends GenericInputType>)newValue);
				return;
			case Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT:
				getOutput().clear();
				getOutput().addAll((Collection<? extends GenericOutputType>)newValue);
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
			case Wps20Package.GENERIC_PROCESS_TYPE__INPUT:
				getInput().clear();
				return;
			case Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT:
				getOutput().clear();
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
			case Wps20Package.GENERIC_PROCESS_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case Wps20Package.GENERIC_PROCESS_TYPE__OUTPUT:
				return output != null && !output.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //GenericProcessTypeImpl
