/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.wps10.OutputDescriptionType;
import net.opengis.wps10.ProcessOutputsType;
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
 * An implementation of the model object '<em><b>Process Outputs Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessOutputsTypeImpl#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessOutputsTypeImpl extends EObjectImpl implements ProcessOutputsType {
    /**
   * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #getOutput()
   * @generated
   * @ordered
   */
    protected EList output;

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    protected ProcessOutputsTypeImpl() {
    super();
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    protected EClass eStaticClass() {
    return Wps10Package.Literals.PROCESS_OUTPUTS_TYPE;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public EList getOutput() {
    if (output == null) {
      output = new EObjectContainmentEList(OutputDescriptionType.class, this, Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT);
    }
    return output;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT:
        return ((InternalEList)getOutput()).basicRemove(otherEnd, msgs);
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
      case Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT:
        return getOutput();
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
      case Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT:
        getOutput().clear();
        getOutput().addAll((Collection)newValue);
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
      case Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT:
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
    public boolean eIsSet(int featureID) {
    switch (featureID) {
      case Wps10Package.PROCESS_OUTPUTS_TYPE__OUTPUT:
        return output != null && !output.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ProcessOutputsTypeImpl
