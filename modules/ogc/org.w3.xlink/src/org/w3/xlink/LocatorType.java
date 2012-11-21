/**
 */
package org.w3.xlink;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Locator Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.w3.xlink.LocatorType#getTitleGroup <em>Title Group</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getHref <em>Href</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getLabel <em>Label</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getRole <em>Role</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getTitle1 <em>Title1</em>}</li>
 *   <li>{@link org.w3.xlink.LocatorType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.w3.xlink.XlinkPackage#getLocatorType()
 * @model extendedMetaData="name='locatorType' kind='elementOnly'"
 * @generated
 */
public interface LocatorType extends EObject {
    /**
     * Returns the value of the '<em><b>Title Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title Group</em>' attribute list.
     * @see org.w3.xlink.XlinkPackage#getLocatorType_TitleGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='title:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getTitleGroup();

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link org.w3.xlink.TitleEltType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Title()
     * @model type="org.w3.xlink.TitleEltType" containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='title' namespace='##targetNamespace' group='title:group'"
     * @generated
     */
    EList getTitle();

    /**
     * Returns the value of the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Href</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Href()
     * @model dataType="org.w3.xlink.HrefType" required="true"
     *        extendedMetaData="kind='attribute' name='href' namespace='##targetNamespace'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link org.w3.xlink.LocatorType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

    /**
     * Returns the value of the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *      label is not required, but locators have no particular
     *      XLink function if they are not labeled.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Label</em>' attribute.
     * @see #setLabel(String)
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Label()
     * @model dataType="org.w3.xlink.LabelType"
     *        extendedMetaData="kind='attribute' name='label' namespace='##targetNamespace'"
     * @generated
     */
    String getLabel();

    /**
     * Sets the value of the '{@link org.w3.xlink.LocatorType#getLabel <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label</em>' attribute.
     * @see #getLabel()
     * @generated
     */
    void setLabel(String value);

    /**
     * Returns the value of the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Role</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Role</em>' attribute.
     * @see #setRole(String)
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Role()
     * @model dataType="org.w3.xlink.RoleType"
     *        extendedMetaData="kind='attribute' name='role' namespace='##targetNamespace'"
     * @generated
     */
    String getRole();

    /**
     * Sets the value of the '{@link org.w3.xlink.LocatorType#getRole <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' attribute.
     * @see #getRole()
     * @generated
     */
    void setRole(String value);

    /**
     * Returns the value of the '<em><b>Title1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title1</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title1</em>' attribute.
     * @see #setTitle1(String)
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Title1()
     * @model dataType="org.w3.xlink.TitleAttrType"
     *        extendedMetaData="kind='attribute' name='title' namespace='##targetNamespace'"
     * @generated
     */
    String getTitle1();

    /**
     * Sets the value of the '{@link org.w3.xlink.LocatorType#getTitle1 <em>Title1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title1</em>' attribute.
     * @see #getTitle1()
     * @generated
     */
    void setTitle1(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>"locator"</code>.
     * The literals are from the enumeration {@link org.w3.xlink.TypeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.w3.xlink.TypeType
     * @see #isSetType()
     * @see #unsetType()
     * @see #setType(TypeType)
     * @see org.w3.xlink.XlinkPackage#getLocatorType_Type()
     * @model default="locator" unsettable="true" required="true"
     *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
     * @generated
     */
    TypeType getType();

    /**
     * Sets the value of the '{@link org.w3.xlink.LocatorType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see org.w3.xlink.TypeType
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(TypeType value);

    /**
     * Unsets the value of the '{@link org.w3.xlink.LocatorType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link org.w3.xlink.LocatorType#getType <em>Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    boolean isSetType();

} // LocatorType
