/**
 */
package org.w3.xlink;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extended</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     Intended for use as the type of user-declared elements to make them
 *     extended links.
 *     Note that the elements referenced in the content model are all abstract.
 *     The intention is that by simply declaring elements with these as their
 *     substitutionGroup, all the right things will happen.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.w3.xlink.Extended#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getResource <em>Resource</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getLocator <em>Locator</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getArc <em>Arc</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getRole <em>Role</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getType <em>Type</em>}</li>
 *   <li>{@link org.w3.xlink.Extended#getTitleAttribute <em>Title Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.w3.xlink.XlinkPackage#getExtended()
 * @model extendedMetaData="name='extended' kind='elementOnly'"
 * @generated
 */
public interface Extended extends EObject {
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
     * @see org.w3.xlink.XlinkPackage#getExtended_Title()
     * @model type="org.w3.xlink.TitleEltType" containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='title' namespace='##targetNamespace' group='title:group'"
     * @generated
     */
    EList getTitle();

   
    /**
     * Returns the value of the '<em><b>Resource</b></em>' containment reference list.
     * The list contents are of type {@link org.w3.xlink.ResourceType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource</em>' containment reference list.
     * @see org.w3.xlink.XlinkPackage#getExtended_Resource()
     * @model type="org.w3.xlink.ResourceType" containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='resource' namespace='##targetNamespace' group='resource:group'"
     * @generated
     */
    EList getResource();

    /**
     * Returns the value of the '<em><b>Locator</b></em>' containment reference list.
     * The list contents are of type {@link org.w3.xlink.LocatorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Locator</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Locator</em>' containment reference list.
     * @see org.w3.xlink.XlinkPackage#getExtended_Locator()
     * @model type="org.w3.xlink.LocatorType" containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='locator' namespace='##targetNamespace' group='locator:group'"
     * @generated
     */
    EList getLocator();

    /**
     * Returns the value of the '<em><b>Arc</b></em>' containment reference list.
     * The list contents are of type {@link org.w3.xlink.ArcType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc</em>' containment reference list.
     * @see org.w3.xlink.XlinkPackage#getExtended_Arc()
     * @model type="org.w3.xlink.ArcType" containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arc' namespace='##targetNamespace' group='arc:group'"
     * @generated
     */
    EList getArc();

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
     * @see org.w3.xlink.XlinkPackage#getExtended_Role()
     * @model dataType="org.w3.xlink.RoleType"
     *        extendedMetaData="kind='attribute' name='role' namespace='##targetNamespace'"
     * @generated
     */
    String getRole();

    /**
     * Sets the value of the '{@link org.w3.xlink.Extended#getRole <em>Role</em>}' attribute.
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
     * @see org.w3.xlink.XlinkPackage#getExtended_Title1()
     * @model dataType="org.w3.xlink.TitleAttrType"
     *        extendedMetaData="kind='attribute' name='title' namespace='##targetNamespace'"
     */
    String getTitleAttribute();

    /**
     * Sets the value of the '{@link org.w3.xlink.Extended#getTitleAttribute <em>Title Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title Attribute</em>' attribute.
     * @see #getTitleAttribute()
     * @generated
     */
    void setTitleAttribute(String value);


    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>"extended"</code>.
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
     * @see org.w3.xlink.XlinkPackage#getExtended_Type()
     * @model default="extended" unsettable="true" required="true"
     *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
     * @generated
     */
    TypeType getType();

    /**
     * Sets the value of the '{@link org.w3.xlink.Extended#getType <em>Type</em>}' attribute.
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
     * Unsets the value of the '{@link org.w3.xlink.Extended#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link org.w3.xlink.Extended#getType <em>Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    boolean isSetType();

} // Extended
