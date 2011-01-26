/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessDescriptionsTypeImpl#getProcessDescription <em>Process Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessDescriptionsTypeImpl extends ResponseBaseTypeImpl implements ProcessDescriptionsType {
    /**
     * The cached value of the '{@link #getProcessDescription() <em>Process Description</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessDescription()
     * @generated
     * @ordered
     */
    protected EList processDescription;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessDescriptionsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_DESCRIPTIONS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getProcessDescription() {
        if (processDescription == null) {
            processDescription = new EObjectContainmentEList(ProcessDescriptionType.class, this, Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION);
        }
        return processDescription;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION:
                return ((InternalEList)getProcessDescription()).basicRemove(otherEnd, msgs);
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
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION:
                return getProcessDescription();
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
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION:
                getProcessDescription().clear();
                getProcessDescription().addAll((Collection)newValue);
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
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION:
                getProcessDescription().clear();
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
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION:
                return processDescription != null && !processDescription.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ProcessDescriptionsTypeImpl
