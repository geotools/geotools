/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.Collection;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.MetadataType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.OperationTypeImpl#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationTypeImpl extends EObjectImpl implements OperationType {
	/**
	 * The cached value of the '{@link #getDCP() <em>DCP</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDCP()
	 * @generated
	 * @ordered
	 */
	protected EList dCP;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList parameter;

	/**
	 * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint()
	 * @generated
	 * @ordered
	 */
	protected EList constraint;

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected EList metadata;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Ows10Package.eINSTANCE.getOperationType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDCP() {
		if (dCP == null) {
			dCP = new EObjectContainmentEList(DCPType.class, this, Ows10Package.OPERATION_TYPE__DCP);
		}
		return dCP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList(DomainType.class, this, Ows10Package.OPERATION_TYPE__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getConstraint() {
		if (constraint == null) {
			constraint = new EObjectContainmentEList(DomainType.class, this, Ows10Package.OPERATION_TYPE__CONSTRAINT);
		}
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMetadata() {
		if (metadata == null) {
			metadata = new EObjectContainmentEList(MetadataType.class, this, Ows10Package.OPERATION_TYPE__METADATA);
		}
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.OPERATION_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Ows10Package.OPERATION_TYPE__DCP:
				return ((InternalEList)getDCP()).basicRemove(otherEnd, msgs);
			case Ows10Package.OPERATION_TYPE__PARAMETER:
				return ((InternalEList)getParameter()).basicRemove(otherEnd, msgs);
			case Ows10Package.OPERATION_TYPE__CONSTRAINT:
				return ((InternalEList)getConstraint()).basicRemove(otherEnd, msgs);
			case Ows10Package.OPERATION_TYPE__METADATA:
				return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Ows10Package.OPERATION_TYPE__DCP:
				return getDCP();
			case Ows10Package.OPERATION_TYPE__PARAMETER:
				return getParameter();
			case Ows10Package.OPERATION_TYPE__CONSTRAINT:
				return getConstraint();
			case Ows10Package.OPERATION_TYPE__METADATA:
				return getMetadata();
			case Ows10Package.OPERATION_TYPE__NAME:
				return getName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Ows10Package.OPERATION_TYPE__DCP:
				getDCP().clear();
				getDCP().addAll((Collection)newValue);
				return;
			case Ows10Package.OPERATION_TYPE__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection)newValue);
				return;
			case Ows10Package.OPERATION_TYPE__CONSTRAINT:
				getConstraint().clear();
				getConstraint().addAll((Collection)newValue);
				return;
			case Ows10Package.OPERATION_TYPE__METADATA:
				getMetadata().clear();
				getMetadata().addAll((Collection)newValue);
				return;
			case Ows10Package.OPERATION_TYPE__NAME:
				setName((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case Ows10Package.OPERATION_TYPE__DCP:
				getDCP().clear();
				return;
			case Ows10Package.OPERATION_TYPE__PARAMETER:
				getParameter().clear();
				return;
			case Ows10Package.OPERATION_TYPE__CONSTRAINT:
				getConstraint().clear();
				return;
			case Ows10Package.OPERATION_TYPE__METADATA:
				getMetadata().clear();
				return;
			case Ows10Package.OPERATION_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Ows10Package.OPERATION_TYPE__DCP:
				return dCP != null && !dCP.isEmpty();
			case Ows10Package.OPERATION_TYPE__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case Ows10Package.OPERATION_TYPE__CONSTRAINT:
				return constraint != null && !constraint.isEmpty();
			case Ows10Package.OPERATION_TYPE__METADATA:
				return metadata != null && !metadata.isEmpty();
			case Ows10Package.OPERATION_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //OperationTypeImpl
