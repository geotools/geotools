/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessOfferingsTypeImpl#getProcess <em>Process</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessOfferingsTypeImpl extends EObjectImpl implements ProcessOfferingsType {
    /**
     * The cached value of the '{@link #getProcess() <em>Process</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcess()
     * @generated
     * @ordered
     */
    protected EList process;

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
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_OFFERINGS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getProcess() {
        if (process == null) {
            process = new EObjectContainmentEList(ProcessBriefType.class, this, Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS);
        }
        return process;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS:
                return ((InternalEList)getProcess()).basicRemove(otherEnd, msgs);
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
            case Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS:
                return getProcess();
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
            case Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS:
                getProcess().clear();
                getProcess().addAll((Collection)newValue);
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
            case Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS:
                getProcess().clear();
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
            case Wps10Package.PROCESS_OFFERINGS_TYPE__PROCESS:
                return process != null && !process.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ProcessOfferingsTypeImpl
