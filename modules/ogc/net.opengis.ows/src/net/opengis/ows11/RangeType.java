/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A range of values of a numeric parameter. This range can be continuous or discrete, defined by a fixed spacing between adjacent valid values. If the MinimumValue or MaximumValue is not included, there is no value limit in that direction. Inclusion of the specified minimum and maximum values in the range shall be defined by the rangeClosure. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.RangeType#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.RangeType#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.RangeType#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.ows11.RangeType#getRangeClosure <em>Range Closure</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getRangeType()
 * @model extendedMetaData="name='RangeType' kind='elementOnly'"
 * @generated
 */
public interface RangeType extends EObject {
    /**
     * Returns the value of the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Minimum value of this numeric parameter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Minimum Value</em>' containment reference.
     * @see #setMinimumValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getRangeType_MinimumValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MinimumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMinimumValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.RangeType#getMinimumValue <em>Minimum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minimum Value</em>' containment reference.
     * @see #getMinimumValue()
     * @generated
     */
    void setMinimumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Maximum value of this numeric parameter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Value</em>' containment reference.
     * @see #setMaximumValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getRangeType_MaximumValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MaximumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMaximumValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.RangeType#getMaximumValue <em>Maximum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Value</em>' containment reference.
     * @see #getMaximumValue()
     * @generated
     */
    void setMaximumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Shall be included when the allowed values are NOT continuous in this range. Shall not be included when the allowed values are continuous in this range. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Spacing</em>' containment reference.
     * @see #setSpacing(ValueType)
     * @see net.opengis.ows11.Ows11Package#getRangeType_Spacing()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Spacing' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getSpacing();

    /**
     * Sets the value of the '{@link net.opengis.ows11.RangeType#getSpacing <em>Spacing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spacing</em>' containment reference.
     * @see #getSpacing()
     * @generated
     */
    void setSpacing(ValueType value);

    /**
     * Returns the value of the '<em><b>Range Closure</b></em>' attribute.
     * The default value is <code>"closed"</code>.
     * The literals are from the enumeration {@link net.opengis.ows11.RangeClosureType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Shall be included unless the default value applies. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.ows11.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @see net.opengis.ows11.Ows11Package#getRangeType_RangeClosure()
     * @model default="closed" unsettable="true"
     *        extendedMetaData="kind='attribute' name='rangeClosure' namespace='##targetNamespace'"
     * @generated
     */
    RangeClosureType getRangeClosure();

    /**
     * Sets the value of the '{@link net.opengis.ows11.RangeType#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.ows11.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @generated
     */
    void setRangeClosure(RangeClosureType value);

    /**
     * Unsets the value of the '{@link net.opengis.ows11.RangeType#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    void unsetRangeClosure();

    /**
     * Returns whether the value of the '{@link net.opengis.ows11.RangeType#getRangeClosure <em>Range Closure</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Range Closure</em>' attribute is set.
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    boolean isSetRangeClosure();

} // RangeType
