/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.BodyReferenceType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Body Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.BodyReferenceTypeImpl#getHref <em>Href</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BodyReferenceTypeImpl extends EObjectImpl implements BodyReferenceType {
    /**
   * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #getHref()
   * @generated
   * @ordered
   */
    protected static final String HREF_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #getHref()
   * @generated
   * @ordered
   */
    protected String href = HREF_EDEFAULT;

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    protected BodyReferenceTypeImpl() {
    super();
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    protected EClass eStaticClass() {
    return Wps10Package.Literals.BODY_REFERENCE_TYPE;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public String getHref() {
    return href;
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public void setHref(String newHref) {
    String oldHref = href;
    href = newHref;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.BODY_REFERENCE_TYPE__HREF, oldHref, href));
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case Wps10Package.BODY_REFERENCE_TYPE__HREF:
        return getHref();
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
      case Wps10Package.BODY_REFERENCE_TYPE__HREF:
        setHref((String)newValue);
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
      case Wps10Package.BODY_REFERENCE_TYPE__HREF:
        setHref(HREF_EDEFAULT);
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
      case Wps10Package.BODY_REFERENCE_TYPE__HREF:
        return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
    }
    return super.eIsSet(featureID);
  }

    /**
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
    public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (href: ");
    result.append(href);
    result.append(')');
    return result.toString();
  }

} //BodyReferenceTypeImpl
