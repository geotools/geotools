/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.Collection;

import net.opengis.ows10.AcceptVersionsType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Versions Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.AcceptVersionsTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AcceptVersionsTypeImpl extends EObjectImpl implements AcceptVersionsType {
	/**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
	protected EList<String> version;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected AcceptVersionsTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getAcceptVersionsType();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<String> getVersion() {
    if (version == null) {
      version = new EDataTypeUniqueEList<String>(String.class, this, Ows10Package.ACCEPT_VERSIONS_TYPE__VERSION);
    }
    return version;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case Ows10Package.ACCEPT_VERSIONS_TYPE__VERSION:
        return getVersion();
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
      case Ows10Package.ACCEPT_VERSIONS_TYPE__VERSION:
        getVersion().clear();
        getVersion().addAll((Collection<? extends String>)newValue);
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
      case Ows10Package.ACCEPT_VERSIONS_TYPE__VERSION:
        getVersion().clear();
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
      case Ows10Package.ACCEPT_VERSIONS_TYPE__VERSION:
        return version != null && !version.isEmpty();
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
    result.append(" (version: ");
    result.append(version);
    result.append(')');
    return result.toString();
  }

} //AcceptVersionsTypeImpl
