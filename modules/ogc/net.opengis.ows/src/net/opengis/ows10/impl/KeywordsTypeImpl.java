/**
 */
package net.opengis.ows10.impl;

import java.util.Collection;
import net.opengis.ows10.CodeType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Keywords Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.KeywordsTypeImpl#getKeyword <em>Keyword</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.KeywordsTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class KeywordsTypeImpl extends EObjectImpl implements KeywordsType {
  /**
   * The cached value of the '{@link #getKeyword() <em>Keyword</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKeyword()
   * @generated
   * @ordered
   */
  protected EList<String> keyword;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected EList<CodeType> type;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected KeywordsTypeImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getKeywordsType();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getKeyword() {
    if (keyword == null) {
      keyword = new EDataTypeUniqueEList<String>(String.class, this, Ows10Package.KEYWORDS_TYPE__KEYWORD);
    }
    return keyword;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<CodeType> getType() {
    if (type == null) {
      type = new EObjectContainmentEList<CodeType>(CodeType.class, this, Ows10Package.KEYWORDS_TYPE__TYPE);
    }
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Ows10Package.KEYWORDS_TYPE__TYPE:
        return ((InternalEList<?>)getType()).basicRemove(otherEnd, msgs);
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
      case Ows10Package.KEYWORDS_TYPE__KEYWORD:
        return getKeyword();
      case Ows10Package.KEYWORDS_TYPE__TYPE:
        return getType();
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
      case Ows10Package.KEYWORDS_TYPE__KEYWORD:
        getKeyword().clear();
        getKeyword().addAll((Collection<? extends String>)newValue);
        return;
      case Ows10Package.KEYWORDS_TYPE__TYPE:
        getType().clear();
        getType().addAll((Collection<? extends CodeType>)newValue);
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
      case Ows10Package.KEYWORDS_TYPE__KEYWORD:
        getKeyword().clear();
        return;
      case Ows10Package.KEYWORDS_TYPE__TYPE:
        getType().clear();
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
      case Ows10Package.KEYWORDS_TYPE__KEYWORD:
        return keyword != null && !keyword.isEmpty();
      case Ows10Package.KEYWORDS_TYPE__TYPE:
        return type != null && !type.isEmpty();
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
    result.append(" (keyword: ");
    result.append(keyword);
    result.append(')');
    return result.toString();
  }

} //KeywordsTypeImpl
