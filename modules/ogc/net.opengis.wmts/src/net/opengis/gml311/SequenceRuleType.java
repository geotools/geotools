/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence Rule Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.SequenceRuleType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.SequenceRuleType#getOrder <em>Order</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getSequenceRuleType()
 * @model extendedMetaData="name='SequenceRuleType' kind='simple'"
 * @generated
 */
public interface SequenceRuleType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.SequenceRuleNames}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see net.opengis.gml311.SequenceRuleNames
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(SequenceRuleNames)
     * @see net.opengis.gml311.Gml311Package#getSequenceRuleType_Value()
     * @model unsettable="true"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    SequenceRuleNames getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.SequenceRuleType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see net.opengis.gml311.SequenceRuleNames
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #getValue()
     * @generated
     */
    void setValue(SequenceRuleNames value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.SequenceRuleType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(SequenceRuleNames)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.SequenceRuleType#getValue <em>Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Value</em>' attribute is set.
     * @see #unsetValue()
     * @see #getValue()
     * @see #setValue(SequenceRuleNames)
     * @generated
     */
    boolean isSetValue();

    /**
     * Returns the value of the '<em><b>Order</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.IncrementOrder}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Order</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Order</em>' attribute.
     * @see net.opengis.gml311.IncrementOrder
     * @see #isSetOrder()
     * @see #unsetOrder()
     * @see #setOrder(IncrementOrder)
     * @see net.opengis.gml311.Gml311Package#getSequenceRuleType_Order()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='order'"
     * @generated
     */
    IncrementOrder getOrder();

    /**
     * Sets the value of the '{@link net.opengis.gml311.SequenceRuleType#getOrder <em>Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Order</em>' attribute.
     * @see net.opengis.gml311.IncrementOrder
     * @see #isSetOrder()
     * @see #unsetOrder()
     * @see #getOrder()
     * @generated
     */
    void setOrder(IncrementOrder value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.SequenceRuleType#getOrder <em>Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOrder()
     * @see #getOrder()
     * @see #setOrder(IncrementOrder)
     * @generated
     */
    void unsetOrder();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.SequenceRuleType#getOrder <em>Order</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Order</em>' attribute is set.
     * @see #unsetOrder()
     * @see #getOrder()
     * @see #setOrder(IncrementOrder)
     * @generated
     */
    boolean isSetOrder();

} // SequenceRuleType
