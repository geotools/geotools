/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import net.opengis.ows10.MetadataType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.MetadataTypeImpl#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.MetadataTypeImpl#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.MetadataTypeImpl#getAbout <em>About</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataTypeImpl extends EObjectImpl implements MetadataType {
	/**
   * The cached value of the '{@link #getAbstractMetaDataGroup() <em>Abstract Meta Data Group</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAbstractMetaDataGroup()
   * @generated
   * @ordered
   */
	protected FeatureMap abstractMetaDataGroup;

	/**
   * The default value of the '{@link #getAbout() <em>About</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAbout()
   * @generated
   * @ordered
   */
	protected static final String ABOUT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getAbout() <em>About</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAbout()
   * @generated
   * @ordered
   */
	protected String about = ABOUT_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected MetadataTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getMetadataType();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FeatureMap getAbstractMetaDataGroup() {
    if (abstractMetaDataGroup == null) {
      abstractMetaDataGroup = new BasicFeatureMap(this, Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP);
    }
    return abstractMetaDataGroup;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EObject getAbstractMetaData() {
    return (EObject)getAbstractMetaDataGroup().get(Ows10Package.eINSTANCE.getMetadataType_AbstractMetaData(), true);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetAbstractMetaData(EObject newAbstractMetaData, NotificationChain msgs) {
    return ((FeatureMap.Internal)getAbstractMetaDataGroup()).basicAdd(Ows10Package.eINSTANCE.getMetadataType_AbstractMetaData(), newAbstractMetaData, msgs);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getAbout() {
    return about;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAbout(String newAbout) {
    String oldAbout = about;
    about = newAbout;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.METADATA_TYPE__ABOUT, oldAbout, about));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
        return ((InternalEList<?>)getAbstractMetaDataGroup()).basicRemove(otherEnd, msgs);
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA:
        return basicSetAbstractMetaData(null, msgs);
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
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
        if (coreType) return getAbstractMetaDataGroup();
        return ((FeatureMap.Internal)getAbstractMetaDataGroup()).getWrapper();
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA:
        return getAbstractMetaData();
      case Ows10Package.METADATA_TYPE__ABOUT:
        return getAbout();
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
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
        ((FeatureMap.Internal)getAbstractMetaDataGroup()).set(newValue);
        return;
      case Ows10Package.METADATA_TYPE__ABOUT:
        setAbout((String)newValue);
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
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
        getAbstractMetaDataGroup().clear();
        return;
      case Ows10Package.METADATA_TYPE__ABOUT:
        setAbout(ABOUT_EDEFAULT);
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
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA_GROUP:
        return abstractMetaDataGroup != null && !abstractMetaDataGroup.isEmpty();
      case Ows10Package.METADATA_TYPE__ABSTRACT_META_DATA:
        return getAbstractMetaData() != null;
      case Ows10Package.METADATA_TYPE__ABOUT:
        return ABOUT_EDEFAULT == null ? about != null : !ABOUT_EDEFAULT.equals(about);
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
    result.append(" (abstractMetaDataGroup: ");
    result.append(abstractMetaDataGroup);
    result.append(", about: ");
    result.append(about);
    result.append(')');
    return result.toString();
  }

} //MetadataTypeImpl
