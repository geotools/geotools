/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.DataDescriptionType;
import net.opengis.wps20.OutputDescriptionType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.OutputDescriptionTypeImpl#getDataDescriptionGroup <em>Data Description Group</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDescriptionTypeImpl#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDescriptionTypeImpl#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OutputDescriptionTypeImpl extends DescriptionTypeImpl implements OutputDescriptionType {
	/**
	 * The cached value of the '{@link #getDataDescriptionGroup() <em>Data Description Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataDescriptionGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap dataDescriptionGroup;

	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected EList<OutputDescriptionType> output;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutputDescriptionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.OUTPUT_DESCRIPTION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getDataDescriptionGroup() {
		if (dataDescriptionGroup == null) {
			dataDescriptionGroup = new BasicFeatureMap(this, Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP);
		}
		return dataDescriptionGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataDescriptionType getDataDescription() {
		return (DataDescriptionType)getDataDescriptionGroup().get(Wps20Package.Literals.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDataDescription(DataDescriptionType newDataDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getDataDescriptionGroup()).basicAdd(Wps20Package.Literals.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION, newDataDescription, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OutputDescriptionType> getOutput() {
		if (output == null) {
			output = new EObjectContainmentEList<OutputDescriptionType>(OutputDescriptionType.class, this, Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT);
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
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				return ((InternalEList<?>)getDataDescriptionGroup()).basicRemove(otherEnd, msgs);
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return basicSetDataDescription(null, msgs);
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT:
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
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				if (coreType) return getDataDescriptionGroup();
				return ((FeatureMap.Internal)getDataDescriptionGroup()).getWrapper();
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return getDataDescription();
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT:
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
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				((FeatureMap.Internal)getDataDescriptionGroup()).set(newValue);
				return;
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT:
				getOutput().clear();
				getOutput().addAll((Collection<? extends OutputDescriptionType>)newValue);
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
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				getDataDescriptionGroup().clear();
				return;
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT:
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
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				return dataDescriptionGroup != null && !dataDescriptionGroup.isEmpty();
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return getDataDescription() != null;
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE__OUTPUT:
				return output != null && !output.isEmpty();
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
		result.append(" (dataDescriptionGroup: ");
		result.append(dataDescriptionGroup);
		result.append(')');
		return result.toString();
	}

} //OutputDescriptionTypeImpl
