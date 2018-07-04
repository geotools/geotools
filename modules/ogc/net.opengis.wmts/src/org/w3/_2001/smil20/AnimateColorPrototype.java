/**
 */
package org.w3._2001.smil20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Animate Color Prototype</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getAccumulate <em>Accumulate</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getAdditive <em>Additive</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeType <em>Attribute Type</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getBy <em>By</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getFrom <em>From</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getTo <em>To</em>}</li>
 *   <li>{@link org.w3._2001.smil20.AnimateColorPrototype#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype()
 * @model extendedMetaData="name='animateColorPrototype' kind='empty'"
 * @generated
 */
public interface AnimateColorPrototype extends EObject {
    /**
     * Returns the value of the '<em><b>Accumulate</b></em>' attribute.
     * The default value is <code>"none"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.AccumulateType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Accumulate</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Accumulate</em>' attribute.
     * @see org.w3._2001.smil20.AccumulateType
     * @see #isSetAccumulate()
     * @see #unsetAccumulate()
     * @see #setAccumulate(AccumulateType)
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_Accumulate()
     * @model default="none" unsettable="true"
     *        extendedMetaData="kind='attribute' name='accumulate'"
     * @generated
     */
    AccumulateType getAccumulate();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAccumulate <em>Accumulate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Accumulate</em>' attribute.
     * @see org.w3._2001.smil20.AccumulateType
     * @see #isSetAccumulate()
     * @see #unsetAccumulate()
     * @see #getAccumulate()
     * @generated
     */
    void setAccumulate(AccumulateType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAccumulate <em>Accumulate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAccumulate()
     * @see #getAccumulate()
     * @see #setAccumulate(AccumulateType)
     * @generated
     */
    void unsetAccumulate();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAccumulate <em>Accumulate</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Accumulate</em>' attribute is set.
     * @see #unsetAccumulate()
     * @see #getAccumulate()
     * @see #setAccumulate(AccumulateType)
     * @generated
     */
    boolean isSetAccumulate();

    /**
     * Returns the value of the '<em><b>Additive</b></em>' attribute.
     * The default value is <code>"replace"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.AdditiveType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additive</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additive</em>' attribute.
     * @see org.w3._2001.smil20.AdditiveType
     * @see #isSetAdditive()
     * @see #unsetAdditive()
     * @see #setAdditive(AdditiveType)
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_Additive()
     * @model default="replace" unsettable="true"
     *        extendedMetaData="kind='attribute' name='additive'"
     * @generated
     */
    AdditiveType getAdditive();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAdditive <em>Additive</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additive</em>' attribute.
     * @see org.w3._2001.smil20.AdditiveType
     * @see #isSetAdditive()
     * @see #unsetAdditive()
     * @see #getAdditive()
     * @generated
     */
    void setAdditive(AdditiveType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAdditive <em>Additive</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAdditive()
     * @see #getAdditive()
     * @see #setAdditive(AdditiveType)
     * @generated
     */
    void unsetAdditive();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAdditive <em>Additive</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Additive</em>' attribute is set.
     * @see #unsetAdditive()
     * @see #getAdditive()
     * @see #setAdditive(AdditiveType)
     * @generated
     */
    boolean isSetAdditive();

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
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_AttributeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='attributeName'"
     * @generated
     */
    String getAttributeName();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeName <em>Attribute Name</em>}' attribute.
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
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_AttributeType()
     * @model default="auto" unsettable="true"
     *        extendedMetaData="kind='attribute' name='attributeType'"
     * @generated
     */
    AttributeTypeType getAttributeType();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeType <em>Attribute Type</em>}' attribute.
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
     * Unsets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeType <em>Attribute Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAttributeType()
     * @see #getAttributeType()
     * @see #setAttributeType(AttributeTypeType)
     * @generated
     */
    void unsetAttributeType();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeType <em>Attribute Type</em>}' attribute is set.
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
     * Returns the value of the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>By</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>By</em>' attribute.
     * @see #setBy(String)
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_By()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='by'"
     * @generated
     */
    String getBy();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getBy <em>By</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>By</em>' attribute.
     * @see #getBy()
     * @generated
     */
    void setBy(String value);

    /**
     * Returns the value of the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>From</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>From</em>' attribute.
     * @see #setFrom(String)
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_From()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='from'"
     * @generated
     */
    String getFrom();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getFrom <em>From</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>From</em>' attribute.
     * @see #getFrom()
     * @generated
     */
    void setFrom(String value);

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
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_To()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='to'"
     * @generated
     */
    String getTo();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getTo <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>To</em>' attribute.
     * @see #getTo()
     * @generated
     */
    void setTo(String value);

    /**
     * Returns the value of the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Values</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Values</em>' attribute.
     * @see #setValues(String)
     * @see org.w3._2001.smil20.Smil20Package#getAnimateColorPrototype_Values()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='values'"
     * @generated
     */
    String getValues();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.AnimateColorPrototype#getValues <em>Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values</em>' attribute.
     * @see #getValues()
     * @generated
     */
    void setValues(String value);

} // AnimateColorPrototype
