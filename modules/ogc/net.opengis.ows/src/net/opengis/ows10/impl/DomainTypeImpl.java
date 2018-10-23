/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.Collection;

import net.opengis.ows10.DomainType;
import net.opengis.ows10.MetadataType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DomainTypeImpl extends EObjectImpl implements DomainType {
	/**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected static final String VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected String value = VALUE_EDEFAULT;

	/**
   * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getMetadata()
   * @generated
   * @ordered
   */
	protected EList<MetadataType> metadata;

	/**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected EList<String> name;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected DomainTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getDomainType();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */

  public String getValue() {
    return value;
  }


  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<MetadataType> getMetadata() {
    if (metadata == null) {
      metadata = new EObjectContainmentEList<MetadataType>(MetadataType.class, this, Ows10Package.DOMAIN_TYPE__METADATA);
    }
    return metadata;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<String> getName() {
    if (name == null) {
      name = new EDataTypeEList<String>(String.class, this, Ows10Package.DOMAIN_TYPE__NAME);
    }
    return name;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Ows10Package.DOMAIN_TYPE__METADATA:
        return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
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
      case Ows10Package.DOMAIN_TYPE__VALUE:
        return getValue();
      case Ows10Package.DOMAIN_TYPE__METADATA:
        return getMetadata();
      case Ows10Package.DOMAIN_TYPE__NAME:
        return getName();
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
      case Ows10Package.DOMAIN_TYPE__VALUE:
        setValue((String)newValue);
        return;
      case Ows10Package.DOMAIN_TYPE__METADATA:
        getMetadata().clear();
        getMetadata().addAll((Collection<? extends MetadataType>)newValue);
        return;
      case Ows10Package.DOMAIN_TYPE__NAME:
        getName().clear();
        getName().addAll((Collection<? extends String>)newValue);
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
      case Ows10Package.DOMAIN_TYPE__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
      case Ows10Package.DOMAIN_TYPE__METADATA:
        getMetadata().clear();
        return;
      case Ows10Package.DOMAIN_TYPE__NAME:
        getName().clear();
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
      case Ows10Package.DOMAIN_TYPE__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case Ows10Package.DOMAIN_TYPE__METADATA:
        return metadata != null && !metadata.isEmpty();
      case Ows10Package.DOMAIN_TYPE__NAME:
        return name != null && !name.isEmpty();
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
    result.append(" (value: ");
    result.append(value);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

  @Override
  public void setValue(String value) {
    // TODO Auto-generated method stub
    
  }

} //DomainTypeImpl
