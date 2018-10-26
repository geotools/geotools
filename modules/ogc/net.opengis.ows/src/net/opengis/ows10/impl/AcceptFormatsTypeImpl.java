/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.Collection;

import net.opengis.ows10.AcceptFormatsType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.AcceptFormatsTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AcceptFormatsTypeImpl extends EObjectImpl implements AcceptFormatsType {
	/**
   * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOutputFormat()
   * @generated
   * @ordered
   */
	protected EList<String> outputFormat;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected AcceptFormatsTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getAcceptFormatsType();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<String> getOutputFormat() {
    if (outputFormat == null) {
      outputFormat = new EDataTypeUniqueEList<String>(String.class, this, Ows10Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);
    }
    return outputFormat;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case Ows10Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
        return getOutputFormat();
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
      case Ows10Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
        getOutputFormat().clear();
        getOutputFormat().addAll((Collection<? extends String>)newValue);
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
      case Ows10Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
        getOutputFormat().clear();
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
      case Ows10Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
        return outputFormat != null && !outputFormat.isEmpty();
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
    result.append(" (outputFormat: ");
    result.append(outputFormat);
    result.append(')');
    return result.toString();
  }

} //AcceptFormatsTypeImpl
