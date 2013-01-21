/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Of Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.RangeOfValuesType#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.RangeOfValuesType#getMaxValue <em>Max Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType()
 * @model extendedMetaData="name='RangeOfValuesType' kind='elementOnly'"
 * @generated
 */
public interface RangeOfValuesType extends EObject {
    /**
     * Returns the value of the '<em><b>Min Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Min Value</em>' containment reference.
     * @see #setMinValue(EObject)
     * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType_MinValue()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='MinValue' namespace='##targetNamespace'"
     * @generated
     */
    EObject getMinValue();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RangeOfValuesType#getMinValue <em>Min Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Value</em>' containment reference.
     * @see #getMinValue()
     * @generated
     */
    void setMinValue(EObject value);

    /**
     * Returns the value of the '<em><b>Max Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Max Value</em>' containment reference.
     * @see #setMaxValue(EObject)
     * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType_MaxValue()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='MaxValue' namespace='##targetNamespace'"
     * @generated
     */
    EObject getMaxValue();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RangeOfValuesType#getMaxValue <em>Max Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Value</em>' containment reference.
     * @see #getMaxValue()
     * @generated
     */
    void setMaxValue(EObject value);

} // RangeOfValuesType
