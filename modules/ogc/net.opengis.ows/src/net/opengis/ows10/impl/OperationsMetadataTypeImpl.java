/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.lang.Object;
import java.util.Collection;

import net.opengis.ows10.DomainType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operations Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.OperationsMetadataTypeImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationsMetadataTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationsMetadataTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.OperationsMetadataTypeImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationsMetadataTypeImpl extends EObjectImpl implements OperationsMetadataType {
	/**
     * The cached value of the '{@link #getOperation() <em>Operation</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOperation()
     * @generated
     * @ordered
     */
	protected EList operation;

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
     * The cached value of the '{@link #getExtendedCapabilities() <em>Extended Capabilities</em>}' reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getExtendedCapabilities()
     * @generated
     * @ordered
     */
	protected Object extendedCapabilities;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected OperationsMetadataTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return Ows10Package.eINSTANCE.getOperationsMetadataType();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getOperation() {
        if (operation == null) {
            operation = new EObjectContainmentEList(OperationType.class, this, Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION);
        }
        return operation;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getParameter() {
        if (parameter == null) {
            parameter = new EObjectContainmentEList(DomainType.class, this, Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER);
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
            constraint = new EObjectContainmentEList(DomainType.class, this, Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT);
        }
        return constraint;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object getExtendedCapabilities() {
        return extendedCapabilities;
    }

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedCapabilities(Object newExtendedCapabilities) {
        Object oldExtendedCapabilities = extendedCapabilities;
        extendedCapabilities = newExtendedCapabilities;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, oldExtendedCapabilities, extendedCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return ((InternalEList)getOperation()).basicRemove(otherEnd, msgs);
            case Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return ((InternalEList)getParameter()).basicRemove(otherEnd, msgs);
            case Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return ((InternalEList)getConstraint()).basicRemove(otherEnd, msgs);
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
            case Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return getOperation();
            case Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return getParameter();
            case Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return getConstraint();
            case Ows10Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
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
            case Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION:
                getOperation().clear();
                getOperation().addAll((Collection)newValue);
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                getParameter().clear();
                getParameter().addAll((Collection)newValue);
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                getConstraint().clear();
                getConstraint().addAll((Collection)newValue);
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((Object)newValue);
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
            case Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION:
                getOperation().clear();
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                getParameter().clear();
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                getConstraint().clear();
                return;
            case Ows10Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((Object)null);
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
            case Ows10Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return operation != null && !operation.isEmpty();
            case Ows10Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return parameter != null && !parameter.isEmpty();
            case Ows10Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return constraint != null && !constraint.isEmpty();
            case Ows10Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                return extendedCapabilities != null;
        }
        return super.eIsSet(featureID);
    }

} //OperationsMetadataTypeImpl
