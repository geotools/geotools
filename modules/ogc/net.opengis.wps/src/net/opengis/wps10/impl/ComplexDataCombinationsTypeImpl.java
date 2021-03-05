/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.wps10.ComplexDataCombinationsType;
import net.opengis.wps10.ComplexDataDescriptionType;
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
 * An implementation of the model object '<em><b>Complex Data Combinations Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataCombinationsTypeImpl#getFormat <em>Format</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComplexDataCombinationsTypeImpl extends EObjectImpl implements ComplexDataCombinationsType {
    /**
   * The cached value of the '{@link #getFormat() <em>Format</em>}' containment reference list.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #getFormat()
   * @generated
   * @ordered
   */
    protected EList format;

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    protected ComplexDataCombinationsTypeImpl() {
    super();
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
    return Wps10Package.Literals.COMPLEX_DATA_COMBINATIONS_TYPE;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public EList getFormat() {
    if (format == null) {
      format = new EObjectContainmentEList(ComplexDataDescriptionType.class, this, Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT);
    }
    return format;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT:
        return ((InternalEList)getFormat()).basicRemove(otherEnd, msgs);
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
      case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT:
        return getFormat();
    }
    return super.eGet(featureID, resolve, coreType);
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT:
        getFormat().clear();
        getFormat().addAll((Collection)newValue);
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
      case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT:
        getFormat().clear();
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
      case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT:
        return format != null && !format.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ComplexDataCombinationsTypeImpl
