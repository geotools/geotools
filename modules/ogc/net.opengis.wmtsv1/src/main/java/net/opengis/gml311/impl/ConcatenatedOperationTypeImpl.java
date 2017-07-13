/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.ConcatenatedOperationType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SingleOperationRefType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Concatenated Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ConcatenatedOperationTypeImpl#getUsesSingleOperation <em>Uses Single Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConcatenatedOperationTypeImpl extends AbstractCoordinateOperationTypeImpl implements ConcatenatedOperationType {
    /**
     * The cached value of the '{@link #getUsesSingleOperation() <em>Uses Single Operation</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesSingleOperation()
     * @generated
     * @ordered
     */
    protected EList<SingleOperationRefType> usesSingleOperation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConcatenatedOperationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getConcatenatedOperationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SingleOperationRefType> getUsesSingleOperation() {
        if (usesSingleOperation == null) {
            usesSingleOperation = new EObjectContainmentEList<SingleOperationRefType>(SingleOperationRefType.class, this, Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION);
        }
        return usesSingleOperation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION:
                return ((InternalEList<?>)getUsesSingleOperation()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION:
                return getUsesSingleOperation();
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
            case Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION:
                getUsesSingleOperation().clear();
                getUsesSingleOperation().addAll((Collection<? extends SingleOperationRefType>)newValue);
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
            case Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION:
                getUsesSingleOperation().clear();
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
            case Gml311Package.CONCATENATED_OPERATION_TYPE__USES_SINGLE_OPERATION:
                return usesSingleOperation != null && !usesSingleOperation.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ConcatenatedOperationTypeImpl
