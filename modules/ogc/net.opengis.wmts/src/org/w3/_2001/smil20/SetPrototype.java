/**
 */
package org.w3._2001.smil20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Prototype</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.SetPrototype#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.w3._2001.smil20.SetPrototype#getAttributeType <em>Attribute Type</em>}</li>
 *   <li>{@link org.w3._2001.smil20.SetPrototype#getTo <em>To</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.smil20.Smil20Package#getSetPrototype()
 * @model extendedMetaData="name='setPrototype' kind='empty'"
 * @generated
 */
public interface SetPrototype extends EObject {
    /**
     * Returns the value of the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute Name</em>' attribute.
     * @see #setAttributeName(String)
     * @see org.w3._2001.smil20.Smil20Package#getSetPrototype_AttributeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='attributeName'"
     * @generated
     */
    String getAttributeName();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.SetPrototype#getAttributeName <em>Attribute Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attribute Name</em>' attribute.
     * @see #getAttributeName()
     * @generated
     */
    void setAttributeName(String value);

    /**
     * Returns the value of the '<em><b>Attribute Type</b></em>' attribute.
     * The default value is <code>"auto"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.AttributeTypeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute Type</em>' attribute.
     * @see org.w3._2001.smil20.AttributeTypeType
     * @see #isSetAttributeType()
     * @see #unsetAttributeType()
     * @see #setAttributeType(AttributeTypeType)
     * @see org.w3._2001.smil20.Smil20Package#getSetPrototype_AttributeType()
     * @model default="auto" unsettable="true"
     *        extendedMetaData="kind='attribute' name='attributeType'"
     * @generated
     */
    AttributeTypeType getAttributeType();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.SetPrototype#getAttributeType <em>Attribute Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attribute Type</em>' attribute.
     * @see org.w3._2001.smil20.AttributeTypeType
     * @see #isSetAttributeType()
     * @see #unsetAttributeType()
     * @see #getAttributeType()
     * @generated
     */
    void setAttributeType(AttributeTypeType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.SetPrototype#getAttributeType <em>Attribute Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAttributeType()
     * @see #getAttributeType()
     * @see #setAttributeType(AttributeTypeType)
     * @generated
     */
    void unsetAttributeType();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.SetPrototype#getAttributeType <em>Attribute Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Attribute Type</em>' attribute is set.
     * @see #unsetAttributeType()
     * @see #getAttributeType()
     * @see #setAttributeType(AttributeTypeType)
     * @generated
     */
    boolean isSetAttributeType();

    /**
     * Returns the value of the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>To</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>To</em>' attribute.
     * @see #setTo(String)
     * @see org.w3._2001.smil20.Smil20Package#getSetPrototype_To()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='to'"
     * @generated
     */
    String getTo();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.SetPrototype#getTo <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>To</em>' attribute.
     * @see #getTo()
     * @generated
     */
    void setTo(String value);

} // SetPrototype
